package com.fruit.sys.admin.action.neworder;

import com.fruit.newOrder.biz.common.SearchOrderCondtionEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.neworder.OrderNewInfoVO;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.neworder.OrderInfoService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟旋 on 2017/9/20.
 */

@Component
@UriMapping("/neworder/center")
public class OrderInfoAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoAction.class);

    @Autowired
    private OrderInfoService ordersInfoService;


    /**
     * 供应商发送短信验证码
     *
     * @return
     */
    @UriMapping(value = "/contract/confirm_captcha_send_ajax", interceptors = "logInterceptor")
    public AjaxResult sendDebtCaptcha() {

        AjaxResult  result =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = (String) requesMap.get("orderNo");

            result =  ordersInfoService.sendConfirmCaptcha(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/contract/confirm_captcha_send_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/contract/confirm_captcha_send_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return result;
    }


    /*
   * 修改订单
   *
   * @return
   */
    @UriMapping(value = "/modifyOrder")
    public AjaxResult modifyOrder() {
        AjaxResult  result =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            int userId = super.getLoginUserId();
            String domain = super.getDomain();

            result =  ordersInfoService.authOrder(requesMap,userId,domain,"1");
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/modify].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/modify].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return result;
    }

    /*
	 * 审核通过
	 *
	 * @return
	 */
    @UriMapping(value = "/auditOrder")
    public AjaxResult orderAudit() {
        AjaxResult  result =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            int userId = super.getLoginUserId();
            String domain = super.getDomain();

            result =  ordersInfoService.authOrder(requesMap,userId,domain,"2");
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/auditOrder].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/auditOrder].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

       return result;
    }

    /**
     * 订单列表查询 状态选项
     * @
     */
    @UriMapping(value = "/get_order_status_ajax")
    public AjaxResult getStatusOrderFind(){
        AjaxResult  result =  new AjaxResult();
        try {
            Map<Integer,String> condtion = new HashMap<Integer,String>();

            condtion.put(SearchOrderCondtionEnum.ALL.getType(),SearchOrderCondtionEnum.ALL.getMessage());
            condtion.put(SearchOrderCondtionEnum.SUBMITING.getType(),SearchOrderCondtionEnum.SUBMITING.getMessage());
            condtion.put(SearchOrderCondtionEnum.SIGNED.getType(),SearchOrderCondtionEnum.SIGNED.getMessage());

            result.setData(condtion);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/get_order_status_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/get_order_status_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return result;

    }


    /**
     * 分页查询订单列表
     *
     * @return
     */
    @UriMapping(value = "/find_order_byPage_ajax")
    public AjaxResult searchOrderByPage() {

        AjaxResult  resultAjax =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);

            PageResult<OrderNewInfoVO> result = new PageResult<OrderNewInfoVO>();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int pageSize = Integer.parseInt(String.valueOf(requesMap.get("pageSize")));
            int pageNo = Integer.parseInt(String.valueOf(requesMap.get("pageNo")));
            int status = Integer.parseInt(String.valueOf(requesMap.get("status")));
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            String  beginTimeStr = String.valueOf(requesMap.get("beginTime"));
            String  endTimeStr = String.valueOf(requesMap.get("endTime"));
            Date beginTime = null;
            Date endTime = null;

            if(StringUtils.isNotBlank(beginTimeStr) && !"null".equals(beginTimeStr)){
                beginTime =formatter.parse(beginTimeStr);
            }
            if(StringUtils.isNotBlank(endTimeStr) && !"null".equals(endTimeStr)){
                endTime =formatter.parse(endTimeStr);
            }

            if (pageSize < 1) {
                pageSize = 10;
            }

            result.setPageSize(pageSize);
            result.setPageNo(pageNo);

            result = ordersInfoService.searchOrderByPage(result,status,beginTime,endTime,orderNo);
            resultAjax.setData(result);

        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/find_order_byPage_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/find_order_byPage_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }

    /**
     * 取消订单
     *
     * @return
     */
    @UriMapping(value = "/order_cancle_ajax")
    public AjaxResult cancalOrder() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            String domain = super.getDomain();
            resultAjax = ordersInfoService.cancelOrder(userId, orderNo, domain);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/order_cancle_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/order_cancle_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }

    /**
     * 查看和生成合同
     *
     * @return
     */
    @UriMapping(value = "/contract/view")
    public AjaxResult orderContract() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);

            String orderNo = String.valueOf(requesMap.get("orderNo"));

            resultAjax =  ordersInfoService.viewBuyContract(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/contract/view].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/contract/view].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;

    }

    /**
     * 订单头部信息
     *
     * @return
     */
    @UriMapping(value = "/head_order_ajax")
    public AjaxResult lastOrder() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            resultAjax =  ordersInfoService.lastOrder(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/head_order_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/head_order_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }

    /**
     * 订单详情
     *
     * @return
     */
    @UriMapping(value = "/order_detail_ajax")
    public AjaxResult getDetailOrder() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {

            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            resultAjax =  ordersInfoService.searchOrderDetailByOrderNo(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/order_detail_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/order_detail_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;

    }

    /**
     * 预付款
     *
     * @return
     */
    @UriMapping(value = "/prepaid_receive_ajax")
    public AjaxResult prepaidReceive() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            String detailInfo = String.valueOf(requesMap.get("detailInfo"));

            BigDecimal prepaidAmount = new BigDecimal(String.valueOf(requesMap.get("prepaidAmount")));

            resultAjax = ordersInfoService.prepaidReceive(userId,orderNo,prepaidAmount,detailInfo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/prepaid_receive_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/prepaid_receive_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }


    /**
     * 尾款
     *
     * @return
     */
    @UriMapping(value = "/tail_receive_ajax")
    public AjaxResult tailReceive() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            BigDecimal tailAmount = new BigDecimal(String.valueOf(requesMap.get("tailAmount")));
            String detailInfo = String.valueOf(requesMap.get("detailInfo"));

            resultAjax = ordersInfoService.tailAmountReceive(userId,orderNo,tailAmount,detailInfo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/tail_receive_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/tail_receive_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }


    /**
     * 收款信息
     *
     * @return
     */
    @UriMapping(value = "/get_receive_info_ajax")
    public AjaxResult getRecevieInfo() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            resultAjax = ordersInfoService.getRecevieInfo(userId,orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/get_receive_info_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/get_receive_info_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }



}
