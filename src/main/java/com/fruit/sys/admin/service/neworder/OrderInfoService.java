/*
 *
 * Copyright (c) 2017 by wuhan  Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.neworder;

import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanUserAuthStatusEnum;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.newOrder.biz.common.*;
import com.fruit.newOrder.biz.common.DBStatusEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderStatusEnum;
import com.fruit.newOrder.biz.common.OrderUpdateTypeEnum;
import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.model.OrderNewInfo;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.request.OrderProcessResponse;
import com.fruit.newOrder.biz.request.OrderSearchRequest;
import com.fruit.newOrder.biz.service.*;
import com.fruit.newOrder.biz.service.impl.OrderProcessService;
import com.fruit.newOrder.biz.service.impl.OrderStateMachine;
import com.fruit.newOrder.biz.service.impl.OrderTaskHelper;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.neworder.OrderGoodsInfoVO;
import com.fruit.sys.admin.model.neworder.OrderNewInfoVO;
import com.fruit.sys.admin.model.neworder.PayReceviceInfoVO;
import com.fruit.sys.admin.model.order.LastOrderVo;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.WechatConstants;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovfintech.cache.client.CacheClient;
import com.ovft.contract.api.bean.*;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.jms.IllegalStateException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * 商品相关
 * Create Author  : ivan
 * Create Date    : 2017-09-20
 * Project        : partal-main-web
 * File Name      : OrdersInfoService.java
 */
@Service
public class OrderInfoService implements InitializingBean
{

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoService.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private OrderGoodsInfoService orderGoodsInfoService;

    @Autowired
    private OrderProcessService newOrderProcessService;

    @Autowired
    private  GoodsCommodityInfoService goodsCommodityInfoService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Resource
    private EcontractService econtractService;

    @Resource
    private LoanSmsContactsConfigService loanSmsContactsConfigService;

    @Resource
    private EnvService envService;

    @Autowired
    private  GoodsVarietyService goodsVarietyService;

    @Autowired
    private OrderPayReceviceService orderPayReceviceService;

    private  static String source ;

    private  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    private static final int REDIS_TIME_OUT = 3;

    private static final BigDecimal MULTIPLE_PARAM = new BigDecimal("0.6");

    private static final BigDecimal ZERO_PARAM = new BigDecimal("0.00");

    private static final BigDecimal THOUSAND_PARAM = new BigDecimal("1000");


    //全部
    private static final List<Integer> ORDER_INFO_STATUS_LIST = new ArrayList<Integer>();

    static
    {
        OrderStatusEnum[] values = OrderStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (OrderStatusEnum status : values)
            {
                ORDER_INFO_STATUS_LIST.add(status.getStatus());
            }
        }
    }

    //待发货
    private static final List<Integer> ORDER_INFO_STATUS_CONFIRMED_LIST = new ArrayList<Integer>();
    static
    {
        ORDER_INFO_STATUS_CONFIRMED_LIST.add(OrderStatusEnum.SYS_CONFIRMED.getStatus());

    }

    //已生效
    private static final List<Integer> ORDER_INFO_STATUS_SIGNED_LIST = new ArrayList<Integer>();

    static
    {
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.SIGNED_CONTRACT.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.PREPAYMENTS.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.SETTLEMENTED.getStatus());
        ORDER_INFO_STATUS_SIGNED_LIST.add(OrderStatusEnum.FINISHED.getStatus());
    }

    /**
     * 修改/审核订单 发生异常则进行回滚
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult authOrder(Map requesMap, int userId,String domain,String type) {

        OrderProcessRequest  orderProcessRequest = new OrderProcessRequest();

        String remoteIp = WebContext.getRequest().getRemoteHost();

        OrderNewInfoDTO request = new OrderNewInfoDTO();

        request.setUserIp(remoteIp);


        AjaxResult result = new AjaxResult();

        try {

            request.setUpdateUser(userId);
            //订单号
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            Validate.isTrue(StringUtils.isNotBlank(orderNo)&& !"null".equals(orderNo),"订单号不能为空");

            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            BeanUtils.copyProperties(orderNewInfoDTO,request);

            //是否需要资金服务
            boolean flag = (boolean) requesMap.get("needLoan");
            int needLoan = flag ? 1 : 0;
            request.setNeedLoan(needLoan);

            //意向发货开始时间
            String startDate = String.valueOf(requesMap.get("intentStartDate"));
            Validate.notEmpty(startDate,"请输入意向发货开始时间");
            request.setIntentStartDate(sdf.parse(startDate));
            //意向发货完成时间
            String endDate = String.valueOf(requesMap.get("intentEndDate"));
            Validate.notEmpty(endDate,"请输入意向发货完成时间");
            request.setIntentEndDate(sdf.parse(endDate));

            //下单货柜数量
            int containerNum =  Integer.parseInt(String.valueOf(requesMap.get("containerNum")));
            Validate.isTrue(containerNum >0,"请输入下单货柜数量.");
            request.setContainerNum(containerNum);
            //下单商品数量
            int goodsNum =  Integer.parseInt(String.valueOf(requesMap.get("goodsNum")));
            Validate.isTrue(goodsNum >0,"请选择下单的商品.");
            request.setGoodsNum(goodsNum);

            //下单商品总价值
            BigDecimal totalAmout =  new BigDecimal(String.valueOf(requesMap.get("totalAmount")));
            Validate.isTrue(totalAmout.compareTo(ZERO_PARAM)>0,"商品总价值必须大于0.00.");
            request.setTotalAmount(totalAmout);


            if(needLoan == 1) {
                //本单可贷款金额
                BigDecimal loanLimit = new BigDecimal(String.valueOf(requesMap.get("loanLimit")));
                Validate.notNull(loanLimit, "请输入本单可贷款金额");
                //整数校验
                BigDecimal[] rst = loanLimit.divideAndRemainder(THOUSAND_PARAM);
                Validate.isTrue(rst[1].compareTo(ZERO_PARAM) == 0, "本单可贷款金额请输入1000的整数倍.");
                //计算贷款上限 百分之60 并取整数
                BigDecimal orderLimit = (totalAmout.multiply(MULTIPLE_PARAM)).divide(THOUSAND_PARAM, 0, RoundingMode.FLOOR).multiply(THOUSAND_PARAM);

                if (loanLimit.compareTo(orderLimit) > 0) {
                    result.setCode(AjaxResultCode.REQUEST_BAD_PARAM.getCode());
                    result.setMsg("本单可贷款金额超过本单总金额的百分之六十，请调整.");
                    return result;
                }

                request.setLoanLimit(loanLimit);
            }else{
                request.setLoanLimit(ZERO_PARAM);
            }

            //商品明细
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) requesMap.get("goodsList");
            Validate.isTrue(CollectionUtils.isNotEmpty(goodsList),"请添加订单商品!");


            List<OrderGoodsInfoDTO>  orderGoodsInfoList = new ArrayList<OrderGoodsInfoDTO>();

            //明细下单
            for (Map<String,Object> goods : goodsList){

                OrderGoodsInfoDTO  dto = new OrderGoodsInfoDTO();
                dto.setOrderNo(orderNo);
                dto.setUserId(request.getUserId());
                dto.setCommodityId(Integer.parseInt( String.valueOf(goods.get("commodityId"))));
                dto.setCommodityNum(Integer.parseInt( String.valueOf(goods.get("commodityNum"))));
                Validate.isTrue(dto.getCommodityNum() >0,"请输入商品数量.");
                dto.setDealPrice(new BigDecimal( String.valueOf(goods.get("dealPrice"))));
                dto.setTotalAmount(new BigDecimal( String.valueOf(goods.get("totalAmount"))));
                dto.setStatus(DBStatusEnum.NORMAL.getStatus());
                dto.setUpdateUser(request.getUserId());

                orderGoodsInfoList.add(dto);

            }

            //操作前生成合同
            long contractId = createContract(request,orderGoodsInfoList);
            if(contractId >0) {
                request.setContractId(contractId);
                request.setContractStatus(ContractStatusEnum.UNSIGNED.getStatus());
            }


            orderProcessRequest.setUserId(request.getUserId());
            orderProcessRequest.setOrderNo(request.getOrderNo());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            if("1".equals(type)){
                orderProcessRequest.setEvent(OrderEventEnum.SYS_MODIFY);
            }else {
                orderProcessRequest.setEvent(OrderEventEnum.SYS_CONFIRM);
            }
            orderProcessRequest.setPlatform(request.getChannel());
            orderProcessRequest.setUserIp(request.getUserIp());
            orderProcessRequest.setDomain(domain);
            orderProcessRequest.setChannel(request.getChannel());
            orderProcessRequest.setOrderInfo(request);
            orderProcessRequest.setUpdateGoodsFlag(true);//更新商品信息
            orderProcessRequest.setOrderGoodsInfoList(orderGoodsInfoList);
            orderProcessRequest.setStatusBefore(request.getStatus());


            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            result.setMsg(orderProcessResponse.getMessage());
            if(orderProcessResponse.isSuccessful()){
                result.setCode(AjaxResultCode.OK.getCode());

            }else{
                throw new IllegalArgumentException(orderProcessResponse.getMessage());
            }
        }catch(IllegalArgumentException e){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    request.getUserId(), e.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.order." + request.getUserId(),
                    "failed to commit order " + JSON.toJSONString(request), EventLevel.URGENT, e,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("修改/审核订单出现异常");
            throw new IllegalArgumentException("修改/审核订单出现异常:" + e.getMessage());
        }catch (Exception e) {
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    request.getUserId(), e.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.order." + request.getUserId(),
                    "failed to commit order " + JSON.toJSONString(request), EventLevel.URGENT, e,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("修改/审核订单出现异常");
            throw new RuntimeException("修改/审核订单出现异常.");
        }finally {
        }
        return result;
    }

    /**
     * 分页查询订单
     *
     * @param result
     * @param beginTime
     *@param endTime @return
     */
    public PageResult<OrderNewInfoVO> searchOrderByPage(PageResult<OrderNewInfoVO> result, int status, Date beginTime, Date endTime,String orderNo) throws Exception {

        OrderSearchRequest request = new OrderSearchRequest();

        List<Integer>  statusList = new ArrayList<Integer>();

        if(status == 0){
            statusList.clear();
        }else{
            statusList.add(status);
        }

        request.setStatusList(statusList);
        request.setBeginTime(beginTime);
        request.setEndTime(endTime);
        if(StringUtils.isNotBlank(orderNo) && !orderNo.equals("null")){
            request.setOrderNo(orderNo);
        }
        request.setPageSize(result.getPageSize());
        request.setPageNo(result.getPageNo());


        try {
            int count = orderNewInfoService.countByRequest(request);
            if (count > 0) {
                result.setTotalCount(count);
                List<OrderNewInfoDTO> orderDtos = orderNewInfoService.loadListByRequest(request);

                List<OrderNewInfoVO> orderVoList = new ArrayList<OrderNewInfoVO>(orderDtos.size());

                for (OrderNewInfoDTO dto : orderDtos) {
                    OrderNewInfoVO vo = transfer(dto);

                    orderVoList.add(vo);

                    result.setPageNo(result.getPageNo() == 0 ? 1 : result.getPageNo());
                    result.setSuccessful(true);
                    result.setTotalPage((int) Math.ceil(result.getTotalCount() * 1.0 / result.getPageSize()));
                    result.setRecords(orderVoList);
                }
            } else {
                result.setSuccessful(false);
                result.setErrorMessage("暂无数据");
            }
        }catch (IllegalArgumentException ix) {
            logger.error("查询订单信息失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询订单信息失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询订单信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询订单信息失败.");
        }


        return result;
    }

    /**
     * 查询订单详情
     *
     * @param orderNo
     * @return
     */
    public AjaxResult searchOrderDetailByOrderNo(String orderNo) throws Exception {

        OrderNewInfoDTO orderNewInfoDTO ;
        try {
            Validate.notEmpty(orderNo, "订单号不能为空.");
            orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            Validate.notNull(orderNo, "您查询的订单不存在.");
        }catch (IllegalArgumentException ix) {
            logger.error("查询订单详情失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询订单详情失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询订单详情失败:"+ex.getMessage());
            throw new IllegalStateException("查询订单详情失败.");
        }

        return  new AjaxResult(transfer(orderNewInfoDTO));

    }

    private OrderNewInfoVO transfer(OrderNewInfoDTO orderNewInfoDTO) {
        OrderNewInfoVO orderNewInfoVO = new OrderNewInfoVO();
        BeanUtils.copyProperties(orderNewInfoDTO,orderNewInfoVO);

        GoodsVarietyDTO goodsVarietyDTO = goodsVarietyService.loadById(orderNewInfoVO.getVarietyId());
        orderNewInfoVO.setVarietyName(goodsVarietyDTO.getName());

        orderNewInfoVO.setStatusDesc( OrderStatusEnum.get(orderNewInfoVO.getStatus()).getUserDesc());

        orderNewInfoVO.setModeTypeDesc( OrderModeTypeEnum.get(orderNewInfoVO.getModeType()).getMessage());

        orderNewInfoVO.setChannelDesc(ChannelEnum.get(orderNewInfoVO.getChannel()).getMessage());

        String purchaseName = "";
        UserEnterpriseDTO userEnterpriseDTO = userEnterpriseService.loadByUserId(orderNewInfoVO.getUserId());
        if(userEnterpriseDTO != null) {
            purchaseName = userEnterpriseDTO.getType() == UserTypeEnum.PERSONAL.getType() ? userEnterpriseDTO.getName() : userEnterpriseDTO.getEnterpriseName();
        }
        orderNewInfoVO.setPurchaseName(purchaseName);

        List<OrderGoodsInfoDTO> goodslist = orderGoodsInfoService.loadListByOrderNo(orderNewInfoVO.getOrderNo());

        orderNewInfoVO.setGoodsInfoList(transfer(goodslist));

        return  orderNewInfoVO;
    }

    private List<OrderGoodsInfoVO> transfer(List<OrderGoodsInfoDTO> goodslist) {
        List<OrderGoodsInfoVO>  ordergoodsVOlist = new ArrayList<OrderGoodsInfoVO> ();

        for(OrderGoodsInfoDTO dto:goodslist){
            OrderGoodsInfoVO vo = new OrderGoodsInfoVO();
            BeanUtils.copyProperties(dto,vo);
            GoodsCommodityInfoDTO goodsCommodityInfoDTO = goodsCommodityInfoService.loadById(vo.getCommodityId());
            vo.setCommodityName(goodsCommodityInfoDTO.getName());

            ordergoodsVOlist.add(vo);
            goodsCommodityInfoDTO = null;
        }

        return  ordergoodsVOlist;
    }


    /**
     * 取消订单 发生异常则进行回滚
     *
     * @param userId,orderNo,domain
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult cancelOrder(int userId ,String orderNo ,String domain) throws Exception {
        AjaxResult result = new AjaxResult();

        try {
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);

            OrderProcessRequest orderProcessRequest = new OrderProcessRequest();

            orderProcessRequest.setUserId(orderNewInfoDTO.getUserId());
            orderProcessRequest.setOrderNo(orderNo);
            orderProcessRequest.setStatusBefore(orderNewInfoDTO.getStatus());
            orderProcessRequest.setOrderInfo(orderNewInfoDTO);
            orderProcessRequest.setDomain(domain);
            orderProcessRequest.setChannel(ChannelEnum.PC.getType());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            orderProcessRequest.setEvent(OrderEventEnum.SYS_CANCEL);

            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            if (!orderProcessResponse.isSuccessful()) {
                throw new IllegalArgumentException(orderProcessResponse.getMessage());
            }


        }catch (IllegalArgumentException ix) {
            logger.error("取消订单失败:"+ix.getMessage());
            throw new IllegalArgumentException("取消订单失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("取消订单失败:"+ex.getMessage());
            throw new IllegalStateException("取消订单失败.");
        }

        return  result;
    }

    public AjaxResult sendConfirmCaptcha(String orderNo) throws Exception {

        AjaxResult ajaxResult = new AjaxResult();

        Validate.notEmpty(orderNo, "订单号不能为空.");

        try {
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                    LoanSmsBizTypeEnum.SUPPLIER_CONTRACT.getType());

            String smsTemplateId = "";
            if ("product".equals(EnvService.getEnv())) {
                //生产环境的时候
                smsTemplateId = envService.getConfig("contract.sms.template.id");
            }

            ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(Integer.parseInt(getCurrentConfig(loanSmsContactsConfigDTO)),
                    orderNewInfoDTO.getOrderSerialNo(), 0, smsTemplateId, source));
            if (!response.isSuccess()) {
                logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
                throw new IllegalArgumentException(response.getMessage());
            }
            ajaxResult.setMsg("短信发送成功!");
        }catch (IllegalArgumentException e){
            logger.error("合同服务发送短信失败:"+e.getMessage());
            throw new IllegalArgumentException("合同服务发送短信失败."+e.getMessage());
        }catch (Exception ex){
            logger.error("合同服务发送短信失败:"+ex.getMessage());
            throw new IllegalStateException("合同服务发送短信失败.");
        }
        return ajaxResult;
    }

    @SuppressWarnings("rawtypes")
    public AjaxResult verifyAnxinCaptcha(String orderNo, String captchaCode) {
        AjaxResult ajaxResult = new AjaxResult();
        OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
        LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                LoanSmsBizTypeEnum.SUPPLIER_CONTRACT.getType());


        ResponseVo response = econtractService.verifyCaptcha(new VerifyCaptchaBean(Integer.parseInt(getCurrentConfig(loanSmsContactsConfigDTO)),
                order.getOrderSerialNo(), captchaCode,source));
        if (!response.isSuccess()) {
            String errorMessage = response.getMessage();
            logger.info("合同服务校验短信失败 具体原因: {}", errorMessage);
            if(errorMessage.contains("短信验证码不正确") || errorMessage.contains("60030501")){
                ajaxResult.setCode(60030501);
                ajaxResult.setMsg("短信验证码不正确！");
            }else{
                ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
                ajaxResult.setMsg("合同服务授权失败！");
            }
        }
        return ajaxResult;
    }


    /**
     * 查询订单头部信息
     *
     * @param orderNo
     * @return
     */
    public AjaxResult lastOrder(String orderNo) throws Exception {
        LastOrderVo vo = new LastOrderVo();
        try {
            OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
            Validate.notNull(order,"查询的订单不存在");
            vo.setPlaceOrderTime(sdf.format(order.getAddTime()));
            vo.setOrderId(order.getOrderNo());
            vo.setOrderStatus(order.getStatus());
            vo.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getSysDesc());
            OrderSearchRequest request = new OrderSearchRequest();
            request.setUserId(order.getUserId());

            List<Integer> statusList = new ArrayList<Integer>();
            statusList.add(OrderStatusEnum.FINISHED.getStatus());
            request.setStatusList(statusList);
            int count = orderNewInfoService.countByRequest(request);
            vo.setSuccessCount(count);
            if (count != 0) {
                List<OrderNewInfoDTO> orders = orderNewInfoService.loadListByRequest(request);
                vo.setLastOrderId(orders.get(0).getOrderNo());
                vo.setLastTime(sdf.format(orders.get(0).getAddTime()));
            }
            UserEnterpriseDTO enterprise = userEnterpriseService.loadByUserId(order.getUserId());
            vo.setContactMobile(enterprise.getPhoneNum());
            vo.setContactName(enterprise.getName());
            UserAccountDTO userInfo = userAccountService.loadById(order.getUserId());
            vo.setUserId(userInfo.getMobile());

            String purchaseName = "";
            if (enterprise != null) {
                purchaseName = enterprise.getType() == UserTypeEnum.PERSONAL.getType() ? enterprise.getName() : enterprise.getEnterpriseName();
            }
            vo.setPurchaserName(purchaseName);
        }catch (IllegalArgumentException e){
            logger.error("查询订单头部信息失败:"+e.getMessage());
            throw new IllegalArgumentException("查询订单头部信息失败."+e.getMessage());
        }catch (Exception ex){
            logger.error("查询订单头部信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询订单头部信息失败.");
        }

        return new AjaxResult(vo);
    }

    private   long createContract( OrderNewInfoDTO order, List<OrderGoodsInfoDTO> goodsList){

        if(order.getContractStatus() == ContractStatusEnum.NOT_GENERATED.getStatus()
                || order.getContractStatus() == ContractStatusEnum.UNSIGNED.getStatus()) {


            LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                    LoanSmsBizTypeEnum.SUPPLIER_CONTRACT.getType());

            final int customerIdSupplier = Integer.parseInt(getCurrentConfig(loanSmsContactsConfigDTO));

            CreateEcontractBean createEcontractBean = new CreateEcontractBean();
            createEcontractBean.setCustomerId(customerIdSupplier);
            createEcontractBean.setTemplateId(Integer.parseInt(envService.getConfig("ordercontract.template.id")));
            createEcontractBean.setParameters(buildParams(order,goodsList));
            createEcontractBean.setSource(source);

            ResponseVo response = econtractService.createEcontract(createEcontractBean);
            if (!response.isSuccess()) {
                logger.info("创建采购合同失败 具体原因: {}", response.getMessage());
                throw new RuntimeException(response.getMessage());
            }
            Long contractId = (Long) response.getData();
            if (contractId == null) {
                logger.info("创建采购合同失败 具体原因: 强制转换出现异常");
                throw new RuntimeException(response.getMessage());
            }
            // 写入订单信息表
            OrderNewInfoDTO model = new OrderNewInfoDTO();
            BeanUtils.copyProperties(order, model);
            model.setContractId(contractId);
            model.setContractStatus(ContractStatusEnum.UNSIGNED.getStatus());

            orderNewInfoService.update(model);

            return contractId;
        }

        return 0;

    }

    /**
     * 生成采购合同
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult viewBuyContract(String  orderNo){

        AjaxResult result = new AjaxResult();

        Map<String, Object> data = new HashMap<String, Object>();
        try {
            OrderNewInfoDTO order = orderNewInfoService.loadByOrderNo(orderNo);
            Validate.notNull(order,"订单信息不存在");

            List<OrderGoodsInfoDTO> goodsList = orderGoodsInfoService.loadListByOrderNo(order.getOrderNo());
            Validate.notNull(goodsList,"订单商品信息不存在");

            if(order.getContractStatus() == ContractStatusEnum.NOT_GENERATED.getStatus()
                    || order.getContractStatus() == ContractStatusEnum.UNSIGNED.getStatus()) {


                LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
                        LoanSmsBizTypeEnum.SUPPLIER_CONTRACT.getType());

                final int customerIdSupplier = Integer.parseInt(getCurrentConfig(loanSmsContactsConfigDTO));

                CreateEcontractBean createEcontractBean = new CreateEcontractBean();
                createEcontractBean.setCustomerId(customerIdSupplier);
                createEcontractBean.setTemplateId(Integer.parseInt(envService.getConfig("ordercontract.template.id")));
                createEcontractBean.setParameters(buildParams(order,goodsList));
                createEcontractBean.setSource(source);

                ResponseVo response = econtractService.createEcontract(createEcontractBean);
                if (!response.isSuccess()) {
                    logger.info("创建采购合同失败 具体原因: {}", response.getMessage());
                    throw new RuntimeException(response.getMessage());
                }
                Long contractId = (Long) response.getData();
                if (contractId == null) {
                    logger.info("创建采购合同失败 具体原因: 强制转换出现异常");
                    return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), response.getMessage());
                }
                // 写入订单信息表
                OrderNewInfoDTO model = new OrderNewInfoDTO();
                BeanUtils.copyProperties(order, model);
                model.setContractId(contractId);
                model.setContractStatus(ContractStatusEnum.UNSIGNED.getStatus());

                orderNewInfoService.update(model);
                data.put("contractId", contractId);

            }else{
                data.put("contractId", order.getContractId());

            }

            ResponseVo responseVo = econtractService.queryContractUrlById(new QueryEContractBean(Long.parseLong(String.valueOf(data.get("contractId"))), source));
            String contractPath = "";
            if (!responseVo.isSuccess()) {
                logger.info("查询采购合同地址失败 具体原因: {}", responseVo.getMessage());
                throw new IllegalStateException(responseVo.getMessage());
            } else {
                contractPath = (String) responseVo.getData();
            }
            data.put("contractPath", contractPath);


            result.setData(data);

        }catch (IllegalStateException ex){
            logger.info("查询采购合同地址失败 具体原因: {}", ex.getMessage());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("创建采购合同出现异常:"+ex.getMessage());

        }catch (Exception ex){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNo, ex.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.orderConract." + orderNo,
                    "failed to commit order " + JSON.toJSONString(orderNo), EventLevel.URGENT, ex,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("创建采购合同出现异常");
        }finally {

        }

        return  result;

    }

    private Map<String,String> buildParams(OrderNewInfoDTO order,List<OrderGoodsInfoDTO> goodsList) {

        Map<String, String> parameters = new HashMap<String, String>();

        LoanUserAuthInfoDTO userInfo = loanUserAuthInfoService.loadByUserId(order.getUserId());

        OrderNewInfoVO orderNewInfoV0 = transfer(order);
        List<OrderGoodsInfoVO> voList = transfer(goodsList);
        // 参数组装
        DecimalFormat amountFormat = new DecimalFormat("###,###,##0.00");
        DecimalFormat numFormat = new DecimalFormat("###,###,###");

        Calendar calendar = Calendar.getInstance();
        Date nowDate = new Date();
        calendar.setTime(nowDate);

        parameters.put("orderNo", order.getOrderNo());//编号
        parameters.put("secondParty", userInfo.getUsername());//乙方
        parameters.put("goodsNum", numFormat.format(order.getGoodsNum()));//商品数量
        parameters.put("totalAmount", amountFormat.format(order.getTotalAmount()));//总金额
        parameters.put("varietyName", orderNewInfoV0.getVarietyName());//果品名称

        parameters.put("firstSealYear",String.valueOf(calendar.get(Calendar.YEAR)));
        parameters.put("firstSealMonth",String.valueOf(calendar.get(Calendar.MONTH) + 1));
        parameters.put("firstSealDay",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        parameters.put("secondSealYear",String.valueOf(calendar.get(Calendar.YEAR)));
        parameters.put("secondSealMonth",String.valueOf(calendar.get(Calendar.MONTH) + 1));
        parameters.put("secondSealDay",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for(OrderGoodsInfoVO good :voList){
            idx++;
            sb.append("<tr>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(idx);
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(orderNewInfoV0.getVarietyName());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getCommodityName());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getCommodityNum());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getDealPrice());
            sb.append("</p>");
            sb.append("</td>");

            sb.append("<td>");
            sb.append("<p>");
            sb.append(good.getTotalAmount());
            sb.append("</p>");
            sb.append("</td>");



            sb.append("</tr>");

        }



        parameters.put("goodsList",sb.toString());//商品信息


        return parameters;
    }

    /**
     * 获取配置
     *
     * @param config
     */
    private String getCurrentConfig(LoanSmsContactsConfigDTO config) {
        String value;
        if ("product".equals(EnvService.getEnv())) {
            value = config.getProduct();
        } else if ("beta".equals(EnvService.getEnv())) {
            value = config.getBeta();
        } else if ("dev".equals(EnvService.getEnv())) {
            value = config.getDev();
        } else {
            value = config.getAlpha();
        }
        return value;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.source = envService.getConfig("contract.source");
    }

    @Transactional(rollbackFor = Exception.class)
    public AjaxResult prepaidReceive(int userId, String orderNo, BigDecimal prepaidAmount,String dealInfo) {

        AjaxResult result = new AjaxResult();

        try {
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            orderNewInfoDTO.setPrepayments(prepaidAmount);//预付款

            OrderProcessRequest orderProcessRequest = new OrderProcessRequest();

            orderProcessRequest.setUserId(orderNewInfoDTO.getUserId());
            orderProcessRequest.setOrderNo(orderNo);
            orderProcessRequest.setStatusBefore(orderNewInfoDTO.getStatus());
            orderProcessRequest.setOrderInfo(orderNewInfoDTO);
            orderProcessRequest.setDomain(envService.getDomain());
            orderProcessRequest.setChannel(ChannelEnum.PC.getType());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            orderProcessRequest.setEvent(OrderEventEnum.PREPAYMENTS);

            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            if (!orderProcessResponse.isSuccessful()) {
                result.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
                result.setMsg(orderProcessResponse.getMessage());
                throw new RuntimeException(orderProcessResponse.getMessage());
            }



            //记录收付款信息
            OrderPayReceviceInfoDTO orderPayReceviceInfoDTO = new OrderPayReceviceInfoDTO();
            orderPayReceviceInfoDTO.setOrderNo(orderNo);
            orderPayReceviceInfoDTO.setAmount(prepaidAmount);
            orderPayReceviceInfoDTO.setType(PayReceviceEnum.PREPAID.getType());
            orderPayReceviceInfoDTO.setDetailInfo(dealInfo);
            orderPayReceviceInfoDTO.setStatus(DBStatusEnum.NORMAL.getStatus());
            orderPayReceviceInfoDTO.setAddTime(new Date());

            orderPayReceviceService.create(orderPayReceviceInfoDTO);
        }catch (IllegalArgumentException ix) {
            logger.error("预付款支付失败."+ix.getMessage());
            throw new IllegalArgumentException("预付款支付失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNo, ex.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.orderConract." + orderNo,
                    "failed to commit order " + JSON.toJSONString(orderNo), EventLevel.URGENT, ex,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("预付款支付失败.");
            throw new RuntimeException("预付款支付失败:" + ex.getMessage());
        }finally {

        }


        return  result;

    }

    public AjaxResult tailAmountReceive(int userId, String orderNo, BigDecimal tailAmount,String dealInfo) {

        AjaxResult result = new AjaxResult();

        try{
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            orderNewInfoDTO.setTailAmount(tailAmount);//尾款

            OrderProcessRequest  orderProcessRequest = new OrderProcessRequest();

            orderProcessRequest.setUserId(orderNewInfoDTO.getUserId());
            orderProcessRequest.setOrderNo(orderNo);
            orderProcessRequest.setStatusBefore(orderNewInfoDTO.getStatus());
            orderProcessRequest.setOrderInfo(orderNewInfoDTO);
            orderProcessRequest.setDomain(envService.getDomain());
            orderProcessRequest.setChannel(ChannelEnum.PC.getType());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            orderProcessRequest.setEvent(OrderEventEnum.SETTLEMENT);

            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);
            if(!orderProcessResponse.isSuccessful()){
                result.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
                result.setMsg(orderProcessResponse.getMessage());
                throw new RuntimeException(orderProcessResponse.getMessage());
            }


            //记录收付款信息
            OrderPayReceviceInfoDTO orderPayReceviceInfoDTO = new OrderPayReceviceInfoDTO();
            orderPayReceviceInfoDTO.setOrderNo(orderNo);
            orderPayReceviceInfoDTO.setAmount(tailAmount);
            orderPayReceviceInfoDTO.setType(PayReceviceEnum.TAILPAY.getType());
            orderPayReceviceInfoDTO.setDetailInfo(dealInfo);
            orderPayReceviceInfoDTO.setStatus(DBStatusEnum.NORMAL.getStatus());
            orderPayReceviceInfoDTO.setAddTime(new Date());

            orderPayReceviceService.create(orderPayReceviceInfoDTO);
        }catch (IllegalArgumentException ix) {
            logger.error("尾款支付失败."+ix.getMessage());
            throw new IllegalArgumentException("尾款支付失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("the userId {} occur exception when committing order, the exception is : {}",
                    orderNo, ex.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.orderConract." + orderNo,
                    "failed to commit order " + JSON.toJSONString(orderNo), EventLevel.URGENT, ex,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("尾款支付失败.");
            throw new RuntimeException("尾款支付失败.");
        }finally {

        }

        return  result;

    }


    /**
     * 查询收款信息
     * @param userId
     * @param orderNo
     * @return
     */
    public AjaxResult getRecevieInfo(int userId, String orderNo) throws Exception {
        AjaxResult result = new AjaxResult(null);

        try {
            List<OrderPayReceviceInfoDTO> payReceviceInfoDTOList = orderPayReceviceService.loadListByOrderNo(orderNo);
            if (CollectionUtils.isNotEmpty(payReceviceInfoDTOList)) {

                List<PayReceviceInfoVO> voList = new ArrayList<PayReceviceInfoVO>(payReceviceInfoDTOList.size());

                for (OrderPayReceviceInfoDTO dto : payReceviceInfoDTOList) {
                    PayReceviceInfoVO vo = new PayReceviceInfoVO();
                    BeanUtils.copyProperties(dto, vo);

                    vo.setAddTimeStr(sdf.format(dto.getAddTime()));
                    vo.setTypeDesc(PayReceviceEnum.get(dto.getType()).getMessage());
                    voList.add(vo);
                }
                result.setData(voList);
            }
        }catch (IllegalArgumentException ix) {
            logger.error("查询收款信息失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询收款信息失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询收款信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询收款信息失败.");
        }



        return result;
    }
}
