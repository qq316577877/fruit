package com.fruit.sys.admin.action.neworder;

import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.neworder.LogisticsDetailInput;
import com.fruit.sys.admin.model.neworder.OrderNewInfoVO;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.neworder.ContainerNewService;
import com.fruit.sys.admin.service.neworder.OrderInfoService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟旋 on 2017/9/20.
 */

@Component
@UriMapping("/neworder/center/container")
public class ContainerInfoAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(ContainerInfoAction.class);

    @Autowired
    private ContainerNewService containerNewService;



    /*
   * 修改订单
   *
   * @return
   */
    @UriMapping(value = "/modify")
    public AjaxResult modifyContainer() {
        AjaxResult  result =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            int userId = super.getLoginUserId();
            String domain = super.getDomain();



            result =  containerNewService.sumbitContainer(requesMap,userId,domain,"1");

        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/modify].Exception:{}", e);
            result.setCode(AjaxResultCode.REQUEST_BAD_PARAM.getCode());
            result.setMsg(e.getMessage());
            return result;
        }catch (Exception e) {
            logger.error("[/neworder/center/container/modify].Exception:{}", e.getMessage());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg(e.getMessage());
            return result;
        }

        return result;
    }

    /*
	 * 审核
	 *
	 * @return
	 */
    @UriMapping(value = "/containerSubmit")
    public AjaxResult containerSubmit() {
        AjaxResult  result =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            int userId = super.getLoginUserId();
            String domain = super.getDomain();

            result =  containerNewService.sumbitContainer(requesMap,userId,domain,"2");

        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/containerSubmit].Exception:{}", e);
            result.setCode(AjaxResultCode.REQUEST_BAD_PARAM.getCode());
            result.setMsg(e.getMessage());
            return result;
        }catch (Exception e) {
            logger.error("[/neworder/center/container/containerSubmit].Exception:{}", e.getMessage());
            result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
            result.setMsg(e.getMessage());
            return result;
        }

       return result;
    }


    /**
     * 分页查询订单列表
     *
     * @return
     */
    @UriMapping(value = "/find_container_byPage_ajax")
    public AjaxResult searchOrderByPage() {

        try{
            Map requesMap  = super.getBodyObject(HashMap.class);

            PageResult<OrderNewInfoVO> result = new PageResult<OrderNewInfoVO>();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            int pageSize = Integer.parseInt(String.valueOf(requesMap.get("pageSize")));
            int pageNo = Integer.parseInt(String.valueOf(requesMap.get("pageNo")));
            int status = Integer.parseInt(String.valueOf(requesMap.get("status")));
            String containerNo = String.valueOf(requesMap.get("containerNo"));
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

            result = containerNewService.searchOrderByPage(result,status,beginTime,endTime,containerNo);

             return  new AjaxResult(result);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/find_container_byPage_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/find_container_byPage_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }


    /**
     * 货柜详情
     *
     * @return
     */
    @UriMapping(value = "/container_detail_id_ajax")
    public AjaxResult getDetailContainerById() {

        try{
            Map requesMap  = super.getBodyObject(HashMap.class);
            long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));

            return  containerNewService.searchContainerDetailByContainerId(containerId);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/container_detail_id_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/container_detail_id_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

    }

    /**
     * 查询货柜的贷款分配金额
     *
     * @return
     */
    @UriMapping(value = "/loan_amount_ajax")
    public AjaxResult queryLoanAmount() {
        try{
            Map requesMap  = super.getBodyObject(HashMap.class);
            long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            BigDecimal productAmount = new BigDecimal(String.valueOf(requesMap.get("productAmount")));

            return new AjaxResult(containerNewService.calculationLoanAmount(orderNo,productAmount,containerId));
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/loan_amount_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/loan_amount_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 货柜详情
     *
     * @return
     */
    @UriMapping(value = "/container_detail_orderno_ajax")
    public AjaxResult getDetailContainer() {
        try{

            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            return  containerNewService.searchContainerDetailByOrderNo(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/container_detail_orderno_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/container_detail_orderno_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"查询失败.");
        }

    }


//    /**
//     * 确认收货
//     *
//     * @return
//     */
//    @UriMapping(value = "/confirm_receive_ajax", interceptors = "userLoginCheckInterceptor")
//    public AjaxResult<Map<String, Object>> confirmReceive() {
//        int userId = super.getLoginUserId();
//        String orderId = super.getStringParameter("orderId");
//        String domain = super.getDomain();
//        return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.RECEIVE, domain,
//                OrderUpdateTypeEnum.STATUS.getType());
//    }

    /*
   * 添加物流信息
   */
    @UriMapping(value = "/logistics/add")
    public AjaxResult addLogisticsDetail() {
        try {

            LogisticsDetailInput input = super.getBodyObject(LogisticsDetailInput.class);
            containerNewService.addLogisticsDetail(input);
            return new AjaxResult(AjaxResultCode.OK.getCode(), "success");
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/logistics/add].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/logistics/add].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    /*
 * 查询物流类型
 */
    @UriMapping(value = "/logistics_init_ajax")
    public AjaxResult getLogisticsInit() {
        try {

            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));


            return containerNewService.initContainerLogisticsInfo(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/logistics_init_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/logistics_init_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 查询物流详情列表
     *
     * @return
     */
    @UriMapping(value = "/logistics_detail_list_ajax")
    public AjaxResult queryLogisticsList() {
        try {

            Map requesMap  = super.getBodyObject(HashMap.class);
            long containerId = Long.parseLong(String.valueOf(requesMap.get("containerId")));
            return new AjaxResult(containerNewService.getLogisticsDetailHeardList(containerId));
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/logistics_detail_list_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/logistics_detail_list_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 查询物流详情列表
     *
     * @return
     */
    @UriMapping(value = "/logistics_detail_ajax")
    public AjaxResult queryLogistics() {
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            long logisticsId = Long.parseLong(String.valueOf(requesMap.get("logisticsId")));
            return new AjaxResult(containerNewService.getLogisticsDetail(logisticsId));
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/container/logistics_detail_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/container/logistics_detail_ajax].Exception:{}", e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 查询保险信息
     *
     * @return
     */
    @UriMapping(value = "/get_insurance_ajax")
    public AjaxResult queryInsurance() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));

            resultAjax = containerNewService.getInsurance(orderNo);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/get_insurance_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/get_insurance_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }

    /**
     * 更新保险信息
     *
     * @return
     */
    @UriMapping(value = "/update_insurance_ajax")
    public AjaxResult updateInsurance() {
        AjaxResult  resultAjax =  new AjaxResult();
        try {
            int userId = super.getLoginUserId();
            Map requesMap  = super.getBodyObject(HashMap.class);
            String orderNo = String.valueOf(requesMap.get("orderNo"));
            List<Map<String,Object>> insuranceList = (List<Map<String, Object>>) requesMap.get("insuranceList");

            resultAjax = containerNewService.updateInsurance(orderNo,insuranceList);
        } catch (IllegalArgumentException e) {
            logger.error("[/neworder/center/update_insurance_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(),e.getMessage());
        }catch (Exception e) {
            logger.error("[/neworder/center/update_insurance_ajax].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }

        return resultAjax;
    }


}
