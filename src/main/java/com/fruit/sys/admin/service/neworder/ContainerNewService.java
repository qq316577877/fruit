/*
 *
 * Copyright (c) 2017 by wuhan  Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.neworder;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.fruit.account.biz.common.LoginLogTypeEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.base.biz.common.BizFileEnum;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.BizFileDTO;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.BizFileService;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.newOrder.biz.common.*;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.newOrder.biz.common.DBStatusEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderStatusEnum;
import com.fruit.newOrder.biz.common.OrderUpdateTypeEnum;
import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.request.*;
import com.fruit.newOrder.biz.service.*;
import com.fruit.newOrder.biz.service.impl.ContainerStateMachine;
import com.fruit.newOrder.biz.service.impl.OrderProcessService;
import com.fruit.newOrder.biz.service.impl.OrderTaskHelper;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.neworder.*;
import com.fruit.sys.admin.model.order.BizFileVo;
import com.fruit.sys.admin.model.order.LogisticsDetailBean;
import com.fruit.sys.admin.model.order.LogisticsDetailVo;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.user.account.AddressVo;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.FileUploadService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.drivers.DriversMemberService;
import com.fruit.sys.admin.service.user.account.DeliveryAddressService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.MathUtil;
import com.fruit.sys.admin.utils.WechatConstants;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.IllegalStateException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description:
 * Create Author  : ivan
 * Create Date    : 2017-09-20
 * Project        : partal-main-web
 * File Name      : OrdersInfoService.java
 */
@Service
public class ContainerNewService
{

    private static final Logger logger = LoggerFactory.getLogger(ContainerNewService.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Resource
    private CacheClient cacheClient;

    @Autowired
    private ContainerInfoService containerInfoService;

    @Autowired
    private DeliveryAddressService delieryAddressService;

    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private ContainerGoodsInfoService containerGoodsInfoService;

    @Autowired
    private IContainerProcessService containerProcessService;


    @Autowired
    private  GoodsCommodityInfoService goodsCommodityInfoService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private  GoodsVarietyService goodsVarietyService;

    @Autowired
    private ContainerNewRecordTimeService containerNewRecordTimeService;

    @Autowired
    private EnvService envService;

    @Autowired
    private ContainerLogisticsDetailService containerLogisticsDetailService;

    @Autowired
    private BizFileService bizFileService;

    @Autowired
    private OrderProcessService newOrderProcessService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ContainerInsuranceService containerInsuranceService;

    @Autowired
    private ContainerDeliveryAddressService containerDeliveryAddressService;


    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    private static final int REDIS_TIME_OUT = 3;

    private static final BigDecimal MULTIPLE_PARAM = new BigDecimal("0.6");

    private static final BigDecimal ZERO_PARAM = new BigDecimal("0.00");

    private static final BigDecimal THOUSAND_PARAM = new BigDecimal("1000");


    /**
     * 修改/审核订单 发生异常则进行回滚
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult sumbitContainer(Map requesMap, int userId,String domain,String type) {

        ContainerProcessRequest  processRequest = new ContainerProcessRequest();

        ContainerInfoDTO request = new ContainerInfoDTO();
        AjaxResult result = new AjaxResult();

        try {
            //货柜ID
            Long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));
            Validate.isTrue(containerId > 0,"货柜ID不能为空");
            request.setId(containerId);
            //查询货柜信息
            request = containerInfoService.loadById(containerId);

            request.setUpdateUser(userId);

            //收货地址
            int deliveryId = Integer.parseInt(String.valueOf(requesMap.get("deliveryId")));
            request.setDeliveryId(deliveryId);

            //运输方式
            int deliveryType = Integer.parseInt(String.valueOf(requesMap.get("deliveryType")));//1-海运 2-陆运
            request.setDeliveryType(deliveryType);

            //商品总数量
            BigDecimal goodsNum = new BigDecimal(String.valueOf(requesMap.get("goodsNum")));//商品总数量
            request.setTotalQuantity(goodsNum);

            //货柜批次号
            String containerNo = String.valueOf(requesMap.get("containerNo"));
            Validate.isTrue(StringUtils.isNotBlank(containerNo)&& !"null".equals(containerNo),"批次号号不能为空");
            //判断是否有重复的货柜批次号
            ContainerInfoDTO containerInfoDTOtemp =containerInfoService.loadByContainerNo(containerNo);
            if(containerInfoDTOtemp != null){
                Validate.isTrue(containerInfoDTOtemp.getId() == containerId,"货柜批次号已存在，请使用其他的批次号.");
            }

            request.setContainerNo(containerNo);
            //货柜总金额
            BigDecimal productAmount =  new BigDecimal(String.valueOf(requesMap.get("productAmount")));
            Validate.isTrue(productAmount.compareTo(ZERO_PARAM) > 0 ,"请添加货柜商品数量.");

            request.setProductAmount(productAmount);

            //计算贷款金额
            Validate.notNull(String.valueOf(requesMap.get("containerLoan")),"请输入货柜贷款金额");
            BigDecimal containerLoan =  new BigDecimal(String.valueOf(requesMap.get("containerLoan")));

            result = calculationLoanAmount(request.getOrderNo(),productAmount,request.getId());
            if(result.getCode() != AjaxResultCode.OK.getCode()){
                return result;
            }else{
                Validate.isTrue(new BigDecimal(String.valueOf(result.getData())).compareTo(containerLoan) >= 0,"您输入的货柜贷款金额超过限制金额"+result.getData());
            }
            request.setLoanAmount(containerLoan);

            //商品明细
            List<Map<String,Object>> goodsList = (List<Map<String, Object>>) requesMap.get("goodsList");
            Validate.isTrue(CollectionUtils.isNotEmpty(goodsList),"请添加货柜商品!");

            List<ContainerGoodsInfoDTO>  containerGoodsInfoList = new ArrayList<ContainerGoodsInfoDTO>();

            //货柜商品明细
            for (Map<String,Object> goods : goodsList){
                ContainerGoodsInfoDTO  dto = new ContainerGoodsInfoDTO();
                dto.setContainerSerialNo(request.getContainerSerialNo());
                dto.setCommodityId(Integer.parseInt( String.valueOf(goods.get("commodityId"))));
                dto.setCommodityNum(Integer.parseInt( String.valueOf(goods.get("commodityNum"))));
                dto.setDealPrice(new BigDecimal( String.valueOf(goods.get("dealPrice"))));
                dto.setTotalAmount(new BigDecimal( String.valueOf(goods.get("totalAmout"))));
                dto.setStatus(DBStatusEnum.NORMAL.getStatus());
                dto.setUpdateUser(userId);

                containerGoodsInfoList.add(dto);
            }

            processRequest.setUserId(userId);
            processRequest.setOrderNo(request.getOrderNo());
            processRequest.setOperatorType(ContainerUpdateTypeEnum.STATUS.getType());
            if("1".equals(type)){
                processRequest.setEvent(ContainerEventEnum.MODIFY);
            }else {
                processRequest.setEvent(ContainerEventEnum.SUBMIT);
            }

            processRequest.setUserIp(WebContext.getRequest().getRemoteHost());
            processRequest.setDomain(domain);
            processRequest.setContainerInfo(request);
            processRequest.setContainerGoodsInfoList(containerGoodsInfoList);
            processRequest.setStatusBefore(request.getStatus());
            processRequest.setUpdateGoodsFlag(true);//需要更新商品信息

            ContainerProcessResponse containerProcessResponse = containerProcessService.operateOrder(processRequest);
            result.setMsg(containerProcessResponse.getMessage());
            if(containerProcessResponse.isSuccessful()){
                result.setCode(AjaxResultCode.OK.getCode());
            }else{
                result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
                result.setMsg(containerProcessResponse.getMessage());
                throw new RuntimeException(containerProcessResponse.getMessage());
            }
        } catch (IllegalArgumentException e) {
            logger.error("修改/审核货柜异常.", e);
            result.setCode(AjaxResultCode.REQUEST_BAD_PARAM.getCode());
            result.setMsg(e.getMessage());
            return result;
        } catch (IllegalStateException e) {
            logger.error("修改/审核货柜异常.", e);
            result.setCode(AjaxResultCode.REQUEST_BAD_PARAM.getCode());
            result.setMsg(e.getMessage());
            return result;
        } catch (Exception e) {
            logger.error("the userId {} occur exception when committing container, the exception is : {}",
                    userId, e.getMessage());
            EventHelper.triggerEvent(this.eventPublishers, "create.order." + userId,
                    "failed to commit container " + JSON.toJSONString(request), EventLevel.URGENT, e,
                    ServerIpUtils.getServerIp());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg("修改/审核货柜出现异常");
            throw new RuntimeException("修改/审核货柜出现异常.");
        }finally {
        }
        return result;
    }

    /**
     * 计算货柜贷款金额
     *
     * @param orderNo
     * @return
     */
    public AjaxResult calculationLoanAmount(String orderNo,BigDecimal amount,Long containerId) throws Exception {

        //本货柜贷款金额
        BigDecimal containerLoan = new BigDecimal("0.00");

        try {
            Validate.notEmpty(orderNo, "订单号不能为空.");
            Validate.isTrue(containerId > 0  , "货柜ID不能为空.");
            ContainerInfoDTO inputDto = containerInfoService.loadById(containerId);
            Validate.notNull(inputDto,"货柜信息不存在");
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);
            List<ContainerInfoDTO> containerList = containerInfoService.loadListByOrderNo(orderNo);
            BigDecimal totalAmount =  orderNewInfoDTO.getTotalAmount();
            BigDecimal loanAmount = orderNewInfoDTO.getLoanAmount();

            //货柜的商品总价值的百分之60
            BigDecimal containerLimit = inputDto.getProductAmount().multiply(MULTIPLE_PARAM);

            if(inputDto.getStatus() >= ContainerStatusEnum.SUBMITED.getStatus()){
                //已提交
                return  new AjaxResult(inputDto.getLoanAmount());
            }

            if(orderNewInfoDTO.getNeedLoan() == 1 && loanAmount.compareTo(new BigDecimal("0.00")) > 0
                    && amount.compareTo(new BigDecimal("0.00")) > 0 && totalAmount.compareTo(new BigDecimal("0.00")) > 0) {
                //最后一个货柜标识
                boolean lastFlag = true;
                BigDecimal loanContainer = new BigDecimal("0.00");//已贷款金额
                for (ContainerInfoDTO cDto : containerList) {
                    if (containerId != cDto.getId()) {
                        if (cDto.getStatus() >= ContainerStatusEnum.SUBMITED.getStatus()) {
                            loanContainer = loanContainer.add(cDto.getLoanAmount());
                        } else {
                            lastFlag = false;
                            break;
                        }
                    }
                }

                if (lastFlag) {
                    //最后一个货柜
                    containerLoan = loanAmount.subtract(loanContainer);
                    containerLoan =containerLoan.compareTo(containerLimit) > 0 ? containerLimit: containerLoan;
                } else {
                    containerLoan = (amount.multiply(loanAmount)).divide(totalAmount,0,RoundingMode.FLOOR);
                    if((loanAmount.subtract(loanContainer)).compareTo(containerLoan) < 0){
                        containerLoan = loanAmount.subtract(loanContainer);
                        containerLoan =containerLoan.compareTo(containerLimit) > 0 ? containerLimit: containerLoan;
                    }
                }
                if(containerLoan.compareTo(new BigDecimal("0.00")) < 0){
                    containerLoan = new BigDecimal("0.00");
                }
            }else{
                containerLoan = new BigDecimal("0.00");
            }

            containerLoan = containerLoan.divide(THOUSAND_PARAM,0,RoundingMode.FLOOR).multiply(THOUSAND_PARAM);


        }catch (IllegalArgumentException e){
            logger.error("计算贷款金额失败:"+e.getMessage());
            throw new IllegalArgumentException("计算贷款金额失败."+e.getMessage());
        }catch (Exception ex){
            logger.error("计算贷款金额失败:"+ex.getMessage());
            throw new IllegalStateException("计算贷款金额失败.");
        }

        return  new AjaxResult(containerLoan);

    }


    /**
     * 分页查询货柜
     *
     * @param result
     * @param beginTime
     *@param endTime @return
     */
    public PageResult<OrderNewInfoVO> searchOrderByPage(PageResult<OrderNewInfoVO> result, int status, Date beginTime, Date endTime, String containerNo) throws Exception{

        ContainerSearchRequest request = new ContainerSearchRequest();

        List<Integer>  statusList = new ArrayList<Integer>();

        if(status == 0){
            statusList.clear();
        }else{
            statusList.add(status);
        }

        request.setStatusList(statusList);
        request.setBeginTime(beginTime);
        request.setEndTime(endTime);
        if(StringUtils.isNotBlank(containerNo) && !containerNo.equals("null")){
            request.setContainerNo(containerNo);
        }
        request.setPageSize(result.getPageSize());
        request.setPageNo(result.getPageNo());
        try {
            //订单信息
            Map<String, OrderNewInfoVO> infoMap = new HashMap<String, OrderNewInfoVO>();

            int count = containerInfoService.countByRequest(request);
            if (count > 0) {
                result.setTotalCount(count);
                List<ContainerInfoDTO> containerDtos = containerInfoService.loadListByRequest(request);

                for (ContainerInfoDTO dto : containerDtos) {
                    ContainerInfoVO vo = new ContainerInfoVO();
                    BeanUtils.copyProperties(dto, vo);

                    //状态描述
                    vo.setStatusDesc(ContainerStatusEnum.get(vo.getStatus()).getSysDesc());
                    //贷款状态
                    LoanInfoDTO loanDto = loanInfoService.loadByTransactionNo(vo.getContainerSerialNo());
                    if (loanDto != null) {
                        vo.setLoanStatusDesc(LoanInfoStatusEnum.get(loanDto.getStatus()).getMessage());
                    }

                    if (vo.getAddTime() != null) {
                        vo.setAddTimeStr(DateUtil.DateToString(vo.getAddTime(), DateStyle.YYYY_MM_DD));
                    }
                    //货柜提交时间
                    ContainerNewRecordTimeDTO containerNewRecordTimeDTO =  containerNewRecordTimeService.loadLastByOrderId(vo.getId());
                    if (containerNewRecordTimeDTO.getSubmitTime() != null) {
                        vo.setSubmitTimeStr(DateUtil.DateToString(containerNewRecordTimeDTO.getSubmitTime(), DateStyle.YYYY_MM_DD));
                    }


                    if (!infoMap.containsKey(vo.getOrderNo())) {
                        //查询订单信息
                        OrderNewInfoDTO orderDto = orderNewInfoService.loadByOrderNo(vo.getOrderNo());
                        OrderNewInfoVO orderVo = transfer(orderDto);

                        orderVo.setContainerInfoList(new ArrayList<ContainerInfoVO>());

                        infoMap.put(vo.getOrderNo(), orderVo);

                    }
                    //已存在订单，则添加货柜信息
                    infoMap.get(vo.getOrderNo()).getContainerInfoList().add(vo);

                }

                result.setPageNo(result.getPageNo() == 0 ? 1 : result.getPageNo());
                result.setSuccessful(true);
                result.setTotalPage((int) Math.ceil(result.getTotalCount() * 1.0 / result.getPageSize()));

                List<OrderNewInfoVO> dataList = this.transferMapValues(infoMap);

                Collections.sort(dataList, new Comparator<OrderNewInfoVO>(){

                    public int compare(OrderNewInfoVO o1, OrderNewInfoVO o2) {

                        //
                        if(o1.getOrderNo().compareTo(o2.getOrderNo()) > 0){
                            return -1;
                        }
                        if(o1.getOrderNo() == o2.getOrderNo()){
                            return 0;
                        }
                        return 1;
                    }
                });

                for(OrderNewInfoVO oVo:dataList){
                    List<ContainerInfoVO> containerInfoVOS = oVo.getContainerInfoList();
                    Collections.sort(containerInfoVOS, new Comparator<ContainerInfoVO>(){

                        public int compare(ContainerInfoVO o1, ContainerInfoVO o2) {

                            //
                            if(o1.getSubmitTimeStr().compareTo(o2.getSubmitTimeStr()) > 0){
                                return -1;
                            }
                            if(o1.getSubmitTimeStr() == o2.getSubmitTimeStr()){
                                return 0;
                            }
                            return 1;
                        }
                    });
                }

                result.setRecords(dataList);

            } else {
                result.setSuccessful(false);
                result.setErrorMessage("暂无数据");
            }
        }catch (IllegalArgumentException e){
            logger.error("查询货柜信息失败:"+e.getMessage());
            throw new IllegalArgumentException("查询货柜信息失败,参数有误."+e.getMessage());
        }catch (Exception ex){
            logger.error("查询货柜信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询货柜信息失败.");
        }

        return result;
    }

    /**
     * 查询货柜详情
     *
     * @param containerId
     * @return
     */
    public AjaxResult searchContainerDetailByContainerId(long containerId) throws Exception{

        AjaxResult result = new AjaxResult();
        try {
            Validate.isTrue(containerId > 0, "货柜ID不能为空.");
            ContainerInfoDTO containerDto = containerInfoService.loadById(containerId);
            Validate.notNull(containerDto, "您查询的货柜不存在.");


            result.setData(transferContainer(containerDto));
        }catch (IllegalArgumentException e){
            logger.error("查询货柜信息失败:"+e.getMessage());
            throw new IllegalArgumentException("查询货柜信息失败."+e.getMessage());
        }catch (Exception ex){
            logger.error("查询货柜信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询货柜信息失败.");
        }

        return  result;

    }

    /**
     * 查询货柜详情
     *
     * @param orderNo
     * @return
     */
    public AjaxResult searchContainerDetailByOrderNo(String orderNo) throws Exception {

        AjaxResult result = new AjaxResult();
        try {
            Validate.notEmpty(orderNo , "订单号不能为空.");

            List<ContainerInfoDTO> containerDtoList = containerInfoService.loadListByOrderNo(orderNo);
            Validate.isTrue(CollectionUtils.isNotEmpty(containerDtoList), "您查询的货柜未生成.");


            result.setData(transferContainer(containerDtoList));
        }catch (IllegalArgumentException e){
            logger.error("查询货柜信息失败:"+e.getMessage());
            throw new IllegalArgumentException("查询货柜信息失败."+e.getMessage());
        }catch (Exception ex){
            logger.error("查询货柜信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询货柜信息失败.");
        }

        return  result;

    }

    class TransferContainerCallable implements Callable<ContainerInfoVO>
    {
        private ContainerInfoDTO containerInfoDTO;

        public TransferContainerCallable(ContainerInfoDTO containerInfoDTO)
        {
            this.containerInfoDTO = containerInfoDTO;
        }

        @Override
        public ContainerInfoVO call() throws Exception
        {
            try {
                return transferContainerSync(containerInfoDTO);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 将loanInfoDTO转换为loanManagementModel
     * @param containerInfoDTO
     * @param
     * @return
     */
    protected ContainerInfoVO transferContainerSync(ContainerInfoDTO containerInfoDTO)
    {

        ContainerInfoVO vo = new ContainerInfoVO();
        BeanUtils.copyProperties(containerInfoDTO, vo);
        //状态描述
        vo.setStatusDesc(ContainerStatusEnum.get(vo.getStatus()).getSysDesc());
        //运输方式
        if (vo.getDeliveryType() == 1) {
            vo.setDeliveryTypeDesc("海运");
        } else {
            vo.setDeliveryTypeDesc("陆运");
        }
        //收货地址
        if(vo.getDeliveryId() >0) {

            UserDeliveryAddressDTO userDeliveryAddressDTO = new UserDeliveryAddressDTO();

            ContainerDeliveryAddressDTO containerDeliveryAddressDTO = containerDeliveryAddressService.loadByContainerId(vo.getId());
            if(containerDeliveryAddressDTO != null){

                BeanUtils.copyProperties(containerDeliveryAddressDTO,userDeliveryAddressDTO);
                userDeliveryAddressDTO.setId(containerDeliveryAddressDTO.getDeliveryId());

            }else {
                userDeliveryAddressDTO = delieryAddressService.loadAddressById(vo.getDeliveryId());
            }
            AddressVo addressVo = delieryAddressService.wrapVO(userDeliveryAddressDTO);
            vo.setDeliveryAdress(addressVo);
        }

        //订单的贷款限额
        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(vo.getOrderNo());
        List<ContainerInfoDTO> containerList = containerInfoService.loadListByOrderNo(vo.getOrderNo());
        Validate.notNull(orderNewInfoDTO, "您的订单信息不存在.");
        BigDecimal loanAmount = orderNewInfoDTO.getLoanAmount();
        vo.setOrderLoanAmount(loanAmount);

        BigDecimal loanContainer = new BigDecimal("0.00");//已贷款金额
        for (ContainerInfoDTO cDto : containerList) {
            if (cDto.getStatus() >= ContainerStatusEnum.SUBMITED.getStatus()) {
                loanContainer = loanContainer.add(cDto.getLoanAmount());
            }
        }
        vo.setOrderLoaned(loanContainer);

        //商品明细
        List<ContainerGoodsInfoDTO> goodsList = containerGoodsInfoService.loadListByContainerSerialNo(vo.getContainerSerialNo());
        List<ContainerGoodsInfoVO> containerGoodsList = new ArrayList<ContainerGoodsInfoVO>(goodsList.size());
        for (ContainerGoodsInfoDTO goodDto : goodsList) {
            ContainerGoodsInfoVO gVo = new ContainerGoodsInfoVO();
            BeanUtils.copyProperties(goodDto, gVo);

            GoodsCommodityInfoDTO goodsCommodityInfoDTO = goodsCommodityInfoService.loadById(goodDto.getCommodityId());
            gVo.setCommodityName(goodsCommodityInfoDTO.getName());

            GoodsVarietyDTO goodsVarietyDTO =  goodsVarietyService.loadById(goodsCommodityInfoDTO.getVarietyId());
            gVo.setVarietyName(goodsVarietyDTO.getName());

            containerGoodsList.add(gVo);
        }
        vo.setContainerGoodsList(containerGoodsList);


        return vo;

    }

    private List<ContainerInfoVO> transferContainer(List<ContainerInfoDTO> dtoList){

        List<ContainerInfoVO> gVoList = new ArrayList<ContainerInfoVO>(dtoList.size());

        List<FutureTask<ContainerInfoVO>> tasks = new ArrayList<FutureTask<ContainerInfoVO>>(dtoList.size());

        if (CollectionUtils.isNotEmpty(dtoList)) {

            for(ContainerInfoDTO dto:dtoList) {
                FutureTask<ContainerInfoVO> futureTask = new FutureTask(new TransferContainerCallable(dto));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }

            for (FutureTask<ContainerInfoVO> task : tasks)
            {
                try
                {
                    ContainerInfoVO containerInfoVO = task.get();
                    gVoList.add(containerInfoVO);
                }
                catch (InterruptedException e)
                {
                    logger.error("货柜信息转换异常中断:"+e.getMessage());
                }
                catch (ExecutionException e)
                {
                    logger.error("货柜信息转换异常:"+e.getMessage());
                }
            }
        }

        return gVoList;
    }

    private ContainerInfoVO transferContainer(ContainerInfoDTO dto){


        ContainerInfoVO vo = new ContainerInfoVO();
        BeanUtils.copyProperties(dto,vo);
        //状态描述
        vo.setStatusDesc(ContainerStatusEnum.get(vo.getStatus()).getSysDesc());
        //运输方式
        if(vo.getDeliveryType() == 1){
            vo.setDeliveryTypeDesc("海运");
        }else{
            vo.setDeliveryTypeDesc("陆运");
        }
        //收货地址
        if(vo.getDeliveryId() >0) {

            UserDeliveryAddressDTO userDeliveryAddressDTO = new UserDeliveryAddressDTO();

            ContainerDeliveryAddressDTO containerDeliveryAddressDTO = containerDeliveryAddressService.loadByContainerId(vo.getId());
            if(containerDeliveryAddressDTO != null){

                BeanUtils.copyProperties(containerDeliveryAddressDTO,userDeliveryAddressDTO);
                userDeliveryAddressDTO.setId(containerDeliveryAddressDTO.getDeliveryId());

            }else {
                userDeliveryAddressDTO = delieryAddressService.loadAddressById(vo.getDeliveryId());
            }
            AddressVo addressVo = delieryAddressService.wrapVO(userDeliveryAddressDTO);
            vo.setDeliveryAdress(addressVo);
        }

        //商品明细

        List<ContainerGoodsInfoDTO> goodsList = containerGoodsInfoService.loadListByContainerSerialNo(vo.getContainerSerialNo());
        List<ContainerGoodsInfoVO> containerGoodsList = new ArrayList<ContainerGoodsInfoVO>(goodsList.size());
        for(ContainerGoodsInfoDTO goodDto : goodsList){
            ContainerGoodsInfoVO gVo = new ContainerGoodsInfoVO();
            BeanUtils.copyProperties(goodDto,gVo);

            GoodsCommodityInfoDTO goodsCommodityInfoDTO = goodsCommodityInfoService.loadById(goodDto.getCommodityId());
            gVo.setCommodityName(goodsCommodityInfoDTO.getName());
            containerGoodsList.add(gVo);
        }
        vo.setContainerGoodsList(containerGoodsList);

        //订单的贷款限额
        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(vo.getOrderNo());
        List<ContainerInfoDTO> containerList = containerInfoService.loadListByOrderNo(vo.getOrderNo());
        Validate.notNull(orderNewInfoDTO, "您的订单信息不存在.");
        BigDecimal loanAmount = orderNewInfoDTO.getLoanAmount();
        vo.setOrderLoanAmount(loanAmount);

        BigDecimal loanContainer = new BigDecimal("0.00");//已贷款金额
        for (ContainerInfoDTO cDto : containerList) {
            if (cDto.getStatus() >= ContainerStatusEnum.SUBMITED.getStatus()) {
                loanContainer = loanContainer.add(cDto.getLoanAmount());
            }
        }
        vo.setOrderLoaned(loanContainer);


        return vo;
    }


    private OrderNewInfoVO transfer(OrderNewInfoDTO orderNewInfoDTO) {
        if(orderNewInfoDTO==null){
            return null;
        }

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

        return  orderNewInfoVO;
    }

    /**
     * 添加物流
     * @param input
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLogisticsDetail(LogisticsDetailInput input) throws Exception {

        //物流类型
        int type  = input.getType();
        Validate.isTrue(input.getContainerId() > 0,"请选择要查询的货柜.");

        if(LogisticsTypeEnum.SHIPPED.getType() == type){
            //发货处理
            Validate.notEmpty(input.getContainerBoxNo(),"货柜号不能为空.");
            Validate.notEmpty(input.getTransportNumber(),"请输入车牌号.");
            //发货业务处理
            doShippAction(input);
        }
        if(LogisticsTypeEnum.CLEARED.getType() == type){
            //清关处理
            Validate.notEmpty(input.getPreReceiveTime(),"预计到货时间不能为空.");
            Validate.notEmpty(input.getTransportNumber(),"请输入车牌号.");
            //业务处理
            doClearAction(input);
        }

        if(LogisticsTypeEnum.SIGNUP.getType() == type){
            //签收处理
            Validate.notEmpty(input.getSigner(),"签收人不能为空");
            //业务处理
            doReceiveAction(input);

        }


    }


    private void doShippAction(LogisticsDetailInput input) throws Exception {

        ContainerInfoDTO containerInfoDTO = containerInfoService.loadById(input.getContainerId());
        Validate.notNull(containerInfoDTO,"货柜不存在");

        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(containerInfoDTO.getOrderNo());
        Validate.notNull(orderNewInfoDTO,"订单不存在");
        final int userId = orderNewInfoDTO.getUserId();
        UserAccountDTO userInfo = userAccountService.loadById(userId);
        Validate.notNull(userInfo,"用户信息不存在");
        final String mobile = userInfo.getMobile();

        if(containerInfoDTO.getStatus() == ContainerStatusEnum.SUBMITED.getStatus()){
            //需要更新的信息
            containerInfoDTO.setDeliveryTime(new Date());

            ContainerProcessRequest processRequest = new ContainerProcessRequest();
            //更新货柜
            processRequest.setUserId(userId);
            processRequest.setOrderNo(containerInfoDTO.getOrderNo());
            processRequest.setOperatorType(ContainerUpdateTypeEnum.STATUS.getType());

            processRequest.setEvent(ContainerEventEnum.SHIPPING);

            processRequest.setUserIp(WebContext.getRequest().getRemoteHost());
            processRequest.setDomain(envService.getConfig("contract.source"));
            processRequest.setContainerInfo(containerInfoDTO);
            processRequest.setStatusBefore(containerInfoDTO.getStatus());
            processRequest.setUpdateGoodsFlag(false);//不需要更新商品信息

            ContainerProcessResponse containerProcessResponse = containerProcessService.operateOrder(processRequest);
            if(!containerProcessResponse.isSuccessful()){
                //处理失败 action中捕捉
                throw new Exception(containerProcessResponse.getMessage());
            }

        }
        //物流详情插入
        insertLogistics(input,containerInfoDTO);


    }

    private void doClearAction(LogisticsDetailInput input) throws Exception {


        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        ContainerInfoDTO containerInfoDTO = containerInfoService.loadById(input.getContainerId());
        Validate.notNull(containerInfoDTO,"货柜不存在");

        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(containerInfoDTO.getOrderNo());
        Validate.notNull(orderNewInfoDTO,"订单不存在");
        final int userId = orderNewInfoDTO.getUserId();
        UserAccountDTO userInfo = userAccountService.loadById(userId);
        Validate.notNull(userInfo,"用户信息不存在");
        final String mobile = userInfo.getMobile();
        ContainerProcessRequest processRequest = new ContainerProcessRequest();//货柜处理
        //贷款标识
        boolean loanflag = orderNewInfoDTO.getNeedLoan() == 1 && containerInfoDTO.getLoanAmount().compareTo(new BigDecimal("0.00")) > 0;

        LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(containerInfoDTO.getContainerSerialNo());

        if(loanflag && loanInfoDTO.getStatus() < LoanInfoStatusEnum.SECURED_LOAN.getStatus()){
            throw new IllegalStateException("你的贷款还未放款，无法通关.");
        }

        //预计通关时间
        try{
            if(StringUtils.isBlank(input.getPreReceiveTime())){
                throw new IllegalStateException("请输入您的预计通关时间，否则无法通关.");
            }
            containerInfoDTO.setPreReceiveTime(sdf2.parse(input.getPreReceiveTime()));
        }catch (ParseException ex){
            throw new IllegalStateException("请输入您的预计通关时间格式有误，请重新输入.");
        }

        //需要更新的信息
        containerInfoDTO.setClearanceTime(new Date());
        containerInfoDTO.setLockId(input.getLockId());

        //更新货柜
        processRequest.setUserId(userId);
        processRequest.setOrderNo(orderNewInfoDTO.getOrderNo());
        processRequest.setOperatorType(ContainerUpdateTypeEnum.STATUS.getType());

        if(loanflag){
            processRequest.setEvent(ContainerEventEnum.CLEARANCE);
        }else{
            processRequest.setEvent(ContainerEventEnum.CLEARANCE_NOLOAN);
        }

        processRequest.setUserIp(WebContext.getRequest().getRemoteHost());
        processRequest.setDomain(envService.getConfig("contract.source"));
        processRequest.setContainerInfo(containerInfoDTO);
        processRequest.setStatusBefore(containerInfoDTO.getStatus());
        processRequest.setUpdateGoodsFlag(false);//不需要更新商品信息

        ContainerProcessResponse containerProcessResponse = containerProcessService.operateOrder(processRequest);
        if(!containerProcessResponse.isSuccessful()){
            //处理失败 action中捕捉
            throw new Exception(containerProcessResponse.getMessage());
        }

        //物流详情插入
        insertLogistics(input,containerInfoDTO);


    }

    private void doReceiveAction(LogisticsDetailInput input) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContainerInfoDTO containerInfoDTO = containerInfoService.loadById(input.getContainerId());
        Validate.notNull(containerInfoDTO,"货柜不存在");

        OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(containerInfoDTO.getOrderNo());
        Validate.notNull(orderNewInfoDTO,"订单不存在");
        final int userId = orderNewInfoDTO.getUserId();
        UserAccountDTO userInfo = userAccountService.loadById(userId);
        Validate.notNull(userInfo,"用户信息不存在");
        final String mobile = userInfo.getMobile();
        ContainerProcessRequest processRequest = new ContainerProcessRequest();//货柜处理
        //贷款标识
        boolean loanflag = orderNewInfoDTO.getNeedLoan() == 1 && containerInfoDTO.getLoanAmount().compareTo(new BigDecimal("0.00")) > 0;

        if(loanflag){
            //有贷款
            LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(containerInfoDTO.getContainerSerialNo());

            Validate.isTrue(loanInfoDTO.getStatus() == LoanInfoStatusEnum.REPAYMENTS.getStatus(),"您当前的货柜不能签收，还有贷款未还.");


        }

        //需要更新的信息
        containerInfoDTO.setReceiveTime(new Date());

        //更新货柜
        processRequest.setUserId(userId);
        processRequest.setOrderNo(orderNewInfoDTO.getOrderNo());
        processRequest.setOperatorType(ContainerUpdateTypeEnum.STATUS.getType());
        processRequest.setEvent(ContainerEventEnum.RECEIVE);
        processRequest.setUserIp(WebContext.getRequest().getRemoteHost());
        processRequest.setDomain(envService.getConfig("contract.source"));
        processRequest.setContainerInfo(containerInfoDTO);
        processRequest.setStatusBefore(containerInfoDTO.getStatus());
        processRequest.setUpdateGoodsFlag(false);//不需要更新商品信息

        ContainerProcessResponse containerProcessResponse = containerProcessService.operateOrder(processRequest);
        if(!containerProcessResponse.isSuccessful()){
            //处理失败 action中捕捉
            throw new Exception(containerProcessResponse.getMessage());
        }



        //订单状态判断
        boolean receviceFlag = true;
        List<ContainerInfoDTO>  containerInfoDTOList = containerInfoService.loadListByOrderNo(containerInfoDTO.getOrderNo());
        for (ContainerInfoDTO cDto : containerInfoDTOList){
            if(cDto.getStatus() != ContainerStatusEnum.RECEIVED.getStatus()){
                receviceFlag = false;
            }
        }
        if(receviceFlag) {

            OrderProcessRequest orderProcessRequest = new OrderProcessRequest();
            orderProcessRequest.setUserId(orderNewInfoDTO.getUserId());
            orderProcessRequest.setOrderNo(orderNewInfoDTO.getOrderNo());
            orderProcessRequest.setOperatorType(OrderUpdateTypeEnum.STATUS.getType());
            orderProcessRequest.setEvent(OrderEventEnum.RECEIVE);
            orderProcessRequest.setPlatform(ChannelEnum.PC.getType());
            orderProcessRequest.setUserIp(WebContext.getRequest().getRemoteHost());
            orderProcessRequest.setDomain(envService.getConfig("contract.source"));
            orderProcessRequest.setChannel(ChannelEnum.PC.getType());
            orderProcessRequest.setOrderInfo(orderNewInfoDTO);
            orderProcessRequest.setUpdateGoodsFlag(false);//更新商品信息
            orderProcessRequest.setStatusBefore(orderNewInfoDTO.getStatus());

            OrderProcessResponse orderProcessResponse = newOrderProcessService.operateOrder(orderProcessRequest);

            if (!orderProcessResponse.isSuccessful()) {
                //处理失败 action中捕捉
                throw new Exception(orderProcessResponse.getMessage());
            }
        }

        //物流详情插入
        insertLogistics(input,containerInfoDTO);


    }




    private void  insertLogistics(LogisticsDetailInput input,ContainerInfoDTO containerInfoDTO){
        ContainerLogisticsDetailDTO  logisticsDetailDTO = new ContainerLogisticsDetailDTO();
        BeanUtils.copyProperties(input,logisticsDetailDTO);

        logisticsDetailDTO.setContainerSerialNo(containerInfoDTO.getContainerSerialNo());

        long id = containerLogisticsDetailService.create(logisticsDetailDTO);
        List<String> filePaths = input.getFilePaths();
        if (filePaths != null && filePaths.size() != 0) {
            for (String path : filePaths) {
                BizFileDTO bizFile = new BizFileDTO();
                bizFile.setPath(path);
                bizFile.setAddTime(new Date());
                bizFile.setStatus(1);
                bizFile.setBizType(BizFileEnum.LOGISTICS_DETAIL.getType());
                bizFile.setBizId("n"+logisticsDetailDTO.getId());
                bizFileService.create(bizFile);
            }
        }


    }


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
        return String.valueOf(value);
    }


    /**
     * 查询当前添加的物流的物流类型
     * @param orderNo
     */
    public AjaxResult initContainerLogisticsInfo(String orderNo) throws Exception {
        AjaxResult result = new AjaxResult();
        List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();

        try{
            List<ContainerInfoDTO> cDtoList = containerInfoService.loadListByOrderNo(orderNo);


            for(ContainerInfoDTO dto: cDtoList) {
                List<ContainerLogisticsDetailDTO> detailList = containerLogisticsDetailService.loadListByContainerSerialNo(dto.getContainerSerialNo());
                Map<String,Object> dataMap = new HashMap<String,Object>();
                int type = 0;
                for (ContainerLogisticsDetailDTO detail : detailList) {
                    if (detail.getType() > type) {
                        type = detail.getType();
                    }
                }
                if (type >= LogisticsTypeEnum.SIGNUP.getType()) {
                    //不能新增物流信息.
                    dataMap.put("type", -1);
                    dataMap.put("typeDesc", "已签收，无法新增物流信息");
                }else{
                    dataMap.put("type", type + 1);
                    dataMap.put("typeDesc", LogisticsTypeEnum.get(type + 1).getTypeDesc());
                }
                dataMap.put("containerId",dto.getId());
                dataMap.put("containerNo",dto.getContainerNo());
                dataMap.put("status",dto.getStatus());
                dataMap.put("statusDesc",ContainerStatusEnum.get(dto.getStatus()).getSysDesc());

                dataList.add(dataMap);
            }
            result.setData(dataList);
        }catch (IllegalArgumentException ix) {
            logger.error("查询物流类型失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询物流类型失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询物流类型失败:"+ex.getMessage());
            throw new IllegalStateException("查询物流类型失败.");
        }
        return  result;

    }

    /**
     * 查询物流
     * @param containerNo
     * @return
     */
    public LogisticsDetailVo getLogisticsDetailByContainerNo(String containerNo) {
        LogisticsDetailVo result = new LogisticsDetailVo();
        ContainerInfoDTO containerInfo = containerInfoService.loadByContainerNo(containerNo);
        Validate.notNull(containerInfo,"货柜不存在");
        result.setContainerNo(containerInfo.getContainerNo());
        result.setContainerName(containerInfo.getContainerName());
        int containerStatus = containerInfo.getStatus();
        result.setContainerStatus(containerStatus);
        if (containerStatus >= ContainerStatusEnum.SHIPPED.getStatus()) {
            // 如果货柜状态是已发货，设置发货时间
            result.setDeliveryTime(containerInfo.getDeliveryTime());
        }
        if (containerStatus >= ContainerStatusEnum.CLEARED.getStatus()) {
            // 如果货柜状态是已清关，设置发货时间
            result.setPreReceiveTime(containerInfo.getPreReceiveTime());
            result.setClearFlag(true);
        }

        List<ContainerLogisticsDetailDTO> logisticsDetaiList = containerLogisticsDetailService.loadListByContainerSerialNo(containerInfo.getContainerSerialNo());
        List<LogisticsDetailBean> list = new ArrayList<LogisticsDetailBean>();

        if(CollectionUtils.isNotEmpty(logisticsDetaiList)){
            for (ContainerLogisticsDetailDTO dto : logisticsDetaiList) {
                LogisticsDetailBean bean = new LogisticsDetailBean();
                BeanUtils.copyProperties(dto, bean);
                bean.setContainerNo(containerInfo.getContainerNo());
                bean.setType(containerInfo.getDeliveryType());
                bean.setDetailInfo(LogisticsTypeEnum.get(dto.getType()).getTypeDesc() + dto.getDetailInfo());


                List<BizFileVo> fileList = new ArrayList<BizFileVo>();
                List<BizFileDTO> files = bizFileService.listByBizIdAndType("n"+dto.getId(),
                        BizFileEnum.LOGISTICS_DETAIL.getType());
                if (files != null && files.size() != 0) {
                    for (BizFileDTO biz : files) {
                        BizFileVo bizVo = new BizFileVo();
                        BeanUtils.copyProperties(biz, bizVo);
                        bizVo.setUrl(fileUploadService.buildDiskUrl(biz.getPath()));
                        fileList.add(bizVo);
                    }
                }
                bean.setFilePaths(fileList);
                list.add(bean);
            }

            result.setLogisticsDetails(list);
        }




        return result;
    }


    /**
     * 查询详情列表
     * @param logisticsId
     * @return
     */
    public LogisticsDetailVO getLogisticsDetail(long logisticsId) throws Exception {
        LogisticsDetailVO result = new LogisticsDetailVO();
        Validate.isTrue(logisticsId>0,"物流信息不存在");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            ContainerLogisticsDetailDTO logisticsDetai = containerLogisticsDetailService.loadById(logisticsId);
            BeanUtils.copyProperties(logisticsDetai, result);

            ContainerInfoDTO container = containerInfoService.loadByContainerSerialNo(logisticsDetai.getContainerSerialNo());

            result.setOrderNo(container.getOrderNo());
            result.setContainerId(container.getId());
            result.setPreReceiveTime(sdf.format(container.getPreReceiveTime()));
            result.setLockId(container.getLockId());
            result.setContainerName(container.getContainerName());
            int containerStatus = container.getStatus();
            result.setContainerStatusDesc(ContainerStatusEnum.get(containerStatus).getSysDesc());


            List<BizFileVo> fileList = new ArrayList<BizFileVo>();
            List<BizFileDTO> files = bizFileService.listByBizIdAndType("n"+logisticsDetai.getId(),
                    BizFileEnum.LOGISTICS_DETAIL.getType());
            if (files != null && files.size() != 0) {
                for (BizFileDTO biz : files) {
                    BizFileVo bizVo = new BizFileVo();
                    BeanUtils.copyProperties(biz, bizVo);
                    bizVo.setUrl(fileUploadService.buildDiskUrl(biz.getPath()));
                    fileList.add(bizVo);
                }
            }
            result.setFilePaths(fileList);
        }catch (IllegalArgumentException ix) {
            logger.error("查询物流信息失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询物流信息失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询物流信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询物流信息失败.");
        }

        return result;
    }

    public List<LogisticsHeardVO> getLogisticsDetailHeardList(long containerId) throws Exception {
        List<LogisticsHeardVO>  voList = new ArrayList<LogisticsHeardVO>();
        Validate.isTrue(containerId>0,"货柜不存在");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContainerInfoDTO container = containerInfoService.loadById(containerId);
        LogisticsTypeEnum[]  typeList = LogisticsTypeEnum.values();

        try{
            for (LogisticsTypeEnum typeEnum : typeList ){
                //查询所有类型的最新一条物流
                List<ContainerLogisticsDetailDTO> logisticsDetaiList = containerLogisticsDetailService.loadListByContainerSerialNo(container.getContainerSerialNo(),typeEnum.getType());
               if(CollectionUtils.isNotEmpty(logisticsDetaiList)){
                   //最新一条
                   ContainerLogisticsDetailDTO dto = logisticsDetaiList.get(0);

                   LogisticsHeardVO vo= new LogisticsHeardVO();
                   BeanUtils.copyProperties(dto, vo);
                   vo.setLogisticsId(dto.getId());
                   vo.setContainerId(container.getId());
                   vo.setType(dto.getType());
                   vo.setTypeDesc(LogisticsTypeEnum.get(dto.getType()).getTypeDesc());
                   vo.setAddTimeStr(sdf.format(dto.getAddTime()));

                   List<BizFileVo> fileList = new ArrayList<BizFileVo>();
                   List<BizFileDTO> files = bizFileService.listByBizIdAndType("n"+dto.getId(),
                           BizFileEnum.LOGISTICS_DETAIL.getType());
                   if (files != null && files.size() != 0) {
                       for (BizFileDTO biz : files) {
                           BizFileVo bizVo = new BizFileVo();
                           BeanUtils.copyProperties(biz, bizVo);
                           bizVo.setUrl(fileUploadService.buildDiskUrl(biz.getPath()));
                           fileList.add(bizVo);
                       }
                   }
                   vo.setFilePaths(fileList);

                   voList.add(vo);
               }

            }
        }catch (IllegalArgumentException ix) {
            logger.error("查询物流详情失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询物流详情失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询物流详情失败:"+ex.getMessage());
            throw new IllegalStateException("查询物流详情失败.");
        }


        return voList;
    }

    /**
     * 查询保险信息
     * @param
     * @param orderNo
     * @return
     */
    public AjaxResult getInsurance( String orderNo) throws Exception {
        AjaxResult result = new AjaxResult();

        List<ContainerInsuranceInfoVO> voList = new ArrayList<ContainerInsuranceInfoVO>();

        try {
            List<ContainerInfoDTO> cdtoList = containerInfoService.loadListByOrderNo(orderNo);

            for (ContainerInfoDTO cdto : cdtoList) {
                ContainerInsuranceInfoVO vo = new ContainerInsuranceInfoVO();

                vo.setContainerId(cdto.getId());
                vo.setContainerSerialNo(cdto.getContainerSerialNo());
                vo.setContainerTotalAmount(cdto.getProductAmount());
                vo.setDeliveryType(cdto.getDeliveryType());
                vo.setContainerNo(cdto.getContainerNo());
                //运输方式
                if (vo.getDeliveryType() == 1) {
                    vo.setDeliveryTypeDesc("海运");
                } else {
                    vo.setDeliveryTypeDesc("陆运");
                }
                //查询发货时的 货柜编号  国外车牌
                List<ContainerLogisticsDetailDTO> detailShippList = containerLogisticsDetailService.loadListByContainerSerialNo(cdto.getContainerSerialNo(), LogisticsTypeEnum.SHIPPED.getType());

                if (CollectionUtils.isNotEmpty(detailShippList)) {

                    ContainerLogisticsDetailDTO detailShipp = detailShippList.get(0);

                    vo.setContainerBoxNo(detailShipp.getContainerBoxNo());
                    vo.setForeignTravelNo(detailShipp.getTransportNumber());

                }

                //查询清关时的 货柜编号  国内车牌
                List<ContainerLogisticsDetailDTO> detailClearList = containerLogisticsDetailService.loadListByContainerSerialNo(cdto.getContainerSerialNo(), LogisticsTypeEnum.CLEARED.getType());

                if (CollectionUtils.isNotEmpty(detailClearList)) {

                    ContainerLogisticsDetailDTO detailClear = detailClearList.get(0);

                    vo.setMainTravelNo(detailClear.getTransportNumber());

                }

                //查询保单号等信息
                ContainerInsuranceDTO containerInsuranceDTO = containerInsuranceService.loadByContainerSerialNo(cdto.getContainerSerialNo());

                if (containerInsuranceDTO != null) {
                    vo.setApplicationNo(containerInsuranceDTO.getApplicationNo());
                    vo.setChangeFlag(containerInsuranceDTO.getChangeFlag());
                }


                voList.add(vo);

            }

            result.setData(voList);
        }catch (IllegalArgumentException ix) {
            logger.error("查询保险信息失败:"+ix.getMessage());
            throw new IllegalArgumentException("查询保险信息失败:"+ix.getMessage());
        }catch (Exception ex){
            logger.error("查询保险信息失败:"+ex.getMessage());
            throw new IllegalStateException("查询保险信息失败.");
        }

        return result;
    }

    /**
     * 更新保险信息
     * @param
     * @param orderNo
     * @param inseranceList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateInsurance( String orderNo, List<Map<String, Object>> inseranceList) throws Exception {
        AjaxResult result = new AjaxResult();

       Validate.isTrue(CollectionUtils.isNotEmpty(inseranceList),"没有要更新的数据");

       try {
           for (Map<String, Object> objMap : inseranceList) {

               long containerId = Long.parseLong(String.valueOf(objMap.get("containerId")));
               String applicationNo = String.valueOf(objMap.get("applicationNo"));
               int changeFlag = Integer.parseInt(String.valueOf(objMap.get("changeFlag")));

               ContainerInfoDTO containerInfoDTO = containerInfoService.loadById(containerId);
               Validate.notNull(containerInfoDTO, "货柜信息不存在");

               ContainerInsuranceDTO containerInsuranceDTO = containerInsuranceService.loadByContainerSerialNo(containerInfoDTO.getContainerSerialNo());
               if (containerInsuranceDTO != null) {
                   containerInsuranceDTO.setApplicationNo(applicationNo);
                   containerInsuranceDTO.setChangeFlag(changeFlag);
                   containerInsuranceService.update(containerInsuranceDTO);

               } else {
                   containerInsuranceDTO = new ContainerInsuranceDTO();
                   containerInsuranceDTO.setContainerSerialNo(containerInfoDTO.getContainerSerialNo());
                   containerInsuranceDTO.setApplicationNo(applicationNo);
                   containerInsuranceDTO.setChangeFlag(changeFlag);
                   containerInsuranceDTO.setStatus(DBStatusEnum.NORMAL.getStatus());
                   containerInsuranceService.create(containerInsuranceDTO);
               }


           }
       }catch (IllegalArgumentException ix) {
           logger.error("更新保险信息失败:"+ix.getMessage());
           throw new IllegalArgumentException("更新保险信息失败:"+ix.getMessage());
       }catch (Exception ex){
        logger.error("更新保险信息失败:"+ex.getMessage());
        throw new IllegalStateException("更新保险信息失败.");
       }

        return result;
    }


    private <K,T> List<T> transferMapValues(Map<K,T> data){

        Validate.notEmpty(data,"无数据转换");
        List<T>  resultList = new ArrayList<T>(data.size());

        Set<K> setList = data.keySet();
        for(K k:setList){
            resultList.add(data.get(k));
        }

        return resultList;

    }


}
