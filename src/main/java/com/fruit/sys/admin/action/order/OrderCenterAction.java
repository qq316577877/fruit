package com.fruit.sys.admin.action.order;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.model.Product;
import com.fruit.order.biz.request.OrderSearchRequest;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.order.AddLogisticsInfoBean;
import com.fruit.sys.admin.model.order.ContainerSimpleInfoVo;
import com.fruit.sys.admin.model.order.OrderDetailShowBean;
import com.fruit.sys.admin.model.order.OrderForLogisticsVo;
import com.fruit.sys.admin.model.order.OrderLogistics;
import com.fruit.sys.admin.model.order.OrderVo;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.order.OrderGeneralService;
import com.fruit.sys.admin.service.order.OrderProcessService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

/**
 * 订单中心
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/order")
public class OrderCenterAction extends BaseAction {

	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private OrderProcessService orderProcessService;

	private static final Logger logger = LoggerFactory.getLogger(OrderCenterAction.class);

	/**
	 * 查询所有商品信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_all_goods")
	public AjaxResult findAllGoods() {
		return new AjaxResult(orderGeneralService.findAllGoods());
	}

	/**
	 * 根据商品ID查询商品信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_good_byId_ajax")
	public AjaxResult findGoodsById() {
		String str = super.getBodyString();
		Product p = JSON.parseObject(str, Product.class);
		String productId = p.getId().toString();
		return new AjaxResult(orderGeneralService.getGoodsInfoById(productId));
	}

	@UriMapping("/list")
	public String toList() {
		return "/order/order_info_list";
	}

	/**
	 * 查看订单详情
	 * 
	 * @return
	 */
	@UriMapping(value = "/detail", interceptors = "validationInterceptor")
	public String queryOrderDetail() {
		String id = super.getStringParameter("id");
		HttpServletRequest request = WebContext.getRequest();
		OrderDetailShowBean orders = orderGeneralService.queryOrderDetail(id);
		request.setAttribute("_DATA", JSON.toJSON(orders));
		return "/order/detail";
	}

	@UriMapping(value = "/detail1")
	public AjaxResult queryOrderDetail1() {
		String id = super.getStringParameter("id");
		OrderDetailShowBean orders = orderGeneralService.queryOrderDetail(id);
		return new AjaxResult(orders);
	}

	/**
	 * 分页查询订单列表
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_order_byPage_ajax")
	public AjaxResult searchOrderByPage() {
		String str = super.getBodyString();
		OrderSearchRequest request = JSON.parseObject(str, OrderSearchRequest.class);
		return new AjaxResult(orderGeneralService.searchOrderByPage(request));
	}

	/**
	 * 取消订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/order_cancle_ajax")
	public AjaxResult cancalOrder() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.SYS_CANCEL, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 确认收货
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_receive_ajax")
	public AjaxResult confirmReceive() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.RECEIVE, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 审核订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_audit_ajax")
	public AjaxResult audit() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.SYS_CONFIRM, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 确认上传合同
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_contract_ajax")
	public AjaxResult confirmUploadContract() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.SIGN_CONTRACT, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 确认收款
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_pay_ajax")
	public AjaxResult confirmPay() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.PAY, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 确认结算
	 * 
	 * @return
	 */
	@UriMapping(value = "/finish_pay_ajax")
	public AjaxResult finishPay() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.SETTLEMENT, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 添加投保时弹窗查询货柜信息接口
	 * 
	 * @return
	 */
	@UriMapping(value = "/simple_query_container_ajax")
	public AjaxResult queryContainerIdByOrderId() {
		String orderId = super.getStringParameter("orderId");
		List<ContainerSimpleInfoVo> result = orderGeneralService.queryContainerSimpleInfo(orderId);
		return new AjaxResult(result);
	}

	/**
	 * 添加或更新保单号保险单号
	 * 
	 * @return
	 */
	@UriMapping(value = "/add_insurance_ajax")
	public AjaxResult addInsurance() {
		try {
			String str = super.getBodyString();
			List<ContainerSimpleInfoVo> insuranceList = JSON.parseArray(str, ContainerSimpleInfoVo.class);
			// List<JSONObject> insurances = super.getBodyObject(List.class);
			if (insuranceList == null || insuranceList.size() == 0) {
				return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(), "入参不能为空");
			}
			orderGeneralService.addInsurance(insuranceList);
			return new AjaxResult();
		} catch (Exception e) {
			return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), e.toString());
		}
	}

	/**
	 * 确认提交订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/confirm_submit_ajax")
	public AjaxResult confirmSubmit() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		String domain = super.getDomain();
		return orderProcessService.operateOrder(userId, orderId, OrderEventEnum.CONFIRM, domain,
				OrderUpdateTypeEnum.STATUS.getType());
	}

	/**
	 * 创建订单
	 * 
	 * @return
	 */
	@UriMapping(value = "/create_order_ajax")
	public AjaxResult createOrder() {
		OrderVo order = super.getBodyObject(OrderVo.class);
		order.setUserId(super.getLoginUserId());
		String remoteIp = WebContext.getRequest().getRemoteHost();
		order.setUserIp(remoteIp);
		return orderGeneralService.createOrder(order);
	}

	@UriMapping(value = "/detail/first")
	public String toOrderDetail() {
		String orderId = super.getStringParameter("orderId");
		HttpServletRequest request = WebContext.getRequest();
		request.setAttribute("orderId", orderId);
		return "/order/order_info_detail";
	}

	/**
	 * 订单审核页预留接口
	 * 
	 * @return
	 */
	@UriMapping(value = "/last_order_ajax")
	public AjaxResult lastOrder() {
		String orderId = super.getStringParameter("orderId");
		return orderGeneralService.lastOrder(orderId);
	}

	/**
	 * 查询订单详情--Tab第一页
	 * 
	 * @return
	 */
	@UriMapping(value = "/tab_order_ajax")
	public AjaxResult orderDetail() {
		int userId = super.getLoginUserId();
		String orderId = super.getStringParameter("orderId");
		return orderGeneralService.orderDetail(orderId, userId);
	}

	/**
	 * 查询物流服务信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/logistics/service/query")
	public AjaxResult logisticsDetail() {
		String orderId = super.getStringParameter("orderId");
		return orderGeneralService.logisticsDetail(orderId);
	}

	/**
	 * 更新物流服务信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/logistics/service/update")
	public AjaxResult updateLogistics() {
        try {
            OrderLogistics dto = super.getBodyObject(OrderLogistics.class);
            return orderGeneralService.updateLogistics(dto);
        }catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                logger.error("[/order/logistics/update].Exception:{}", e);
                return new AjaxResult(100010, e.getMessage());
            }
            logger.error("[/order/logistics/update].Exception:{}", e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
	}

	/*
	 * 添加物流信息
	 */
	@UriMapping(value = "/logistics/add")
	public AjaxResult addLogisticsDetail() {
		try {
			AddLogisticsInfoBean logistics = super.getBodyObject(AddLogisticsInfoBean.class);
			orderGeneralService.addLogisticsDetail(logistics);
			return new AjaxResult(AjaxResultCode.OK.getCode(), "success");
		} catch (Exception e) {
			if(e instanceof IllegalArgumentException){
				logger.error("[/order/logistics/add].Exception:{}", e);
				return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
			}
			logger.error("[/order/logistics/add].Exception:{}", e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	/**
	 * 查询物流详情
	 * 
	 * @return
	 */
	@UriMapping(value = "/logistics_detail_ajax")
	public AjaxResult queryLogistics() {
		String id = super.getStringParameter("id");
		return new AjaxResult(orderGeneralService.queryLogistics(id));
	}

	/*
	 * 查看物流页头部展示信息接口
	 */
	@UriMapping(value = "/logistics/show")
	public String queryOrderForLogistics() {
		String orderId = super.getStringParameter("orderId");
		OrderForLogisticsVo data = orderGeneralService.queryOrderForLogistics(orderId);
		HttpServletRequest request = WebContext.getRequest();
		request.setAttribute("data", data);
		return "/order/order_logistics_detail";
	}

	/**
	 * 资金服务查询货柜信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/container_detail_ajax")
	public AjaxResult queryContainer() {
		String orderId = super.getStringParameter("orderId");
	//	int userId = super.getLoginUserId();
		return new AjaxResult(orderGeneralService.queryContainer(orderId));
	}

	/**
	 * 根据订单号查询订单信息
	 * 
	 * @return
	 */
	@UriMapping(value = "/query_order_ajax")
	public AjaxResult queryOrderMemo() {
		String orderId = super.getStringParameter("orderId");
		return new AjaxResult(orderGeneralService.queryOrderInfo(orderId));
	}

	/**
	 * 更新订单备注
	 * 
	 * @return
	 */
	@UriMapping(value = "/memo_add_ajax")
	public AjaxResult updateOrderMemo() {
		try {
			String orderId = super.getStringParameter("orderId");
			String backMemo = super.getStringParameter("backMemo");
			orderGeneralService.updateOrderMemo(orderId, backMemo);
			return new AjaxResult();
		} catch (Exception e) {
			return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), e.toString());
		}
	}

	@UriMapping(value = "/logistics/map")
	public String queryLogisticsMap() {
		String containerId = super.getStringParameter("containerId");
		try {
			Map<String, Object> result = orderGeneralService.queryLogisticsMap(containerId);
			HttpServletRequest request = WebContext.getRequest();
			request.setAttribute("containerNo", containerId);
			request.setAttribute("clearFlag", result.get("clearFlag"));
			request.setAttribute("clearAddress", result.get("clearAddress"));
			request.setAttribute("clearanceTime", result.get("clearanceTime"));
			request.setAttribute("receiverAddress", result.get("receiverAddress"));
			request.setAttribute("deliveryTime", result.get("deliveryTime"));
			request.setAttribute("preReceiveTime", result.get("preReceiveTime"));
		} catch (Exception e) {
			logger.error("[order][/logistics/map].Exception:{}", e);
			WebContext.getRequest().setAttribute("error_msg", e.getMessage());
		}
		return "/user/loanManagement/map";
	}
}
