package com.fruit.sys.admin.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderEventRoleEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.common.OrderUpdateTypeEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.model.order.OrderProcessResponse;
import com.fruit.sys.admin.service.trade.OrderStateMachine;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;

@Service("defaultOrderProcessor")
public class DefaultOrderProcessor implements IOrderProcessor, InitializingBean {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderProcessor.class);

	protected OrderEventEnum event;

	@Autowired
	private OrderService orderService;

	@Autowired
	protected OrderStateMachine orderStateMachine;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired(required = false)
	protected List<EventPublisher> eventPublishers;

	public static Map<OrderStatusEnum, String> smsTemplates = new HashMap<OrderStatusEnum, String>();

	public DefaultOrderProcessor(OrderEventEnum event, boolean needNotify) {
		this.event = event;
	}

	protected DefaultOrderProcessor(OrderEventEnum event) {
		this(event, false);
	}

	public DefaultOrderProcessor() {
		this(OrderEventEnum.SAVE, false);
	}

	@Override
	public OrderEventEnum getEvent() {
		return event;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public OrderProcessResponse process(OrderProcessRequest request) {
		boolean successful = false;
		String message = "";
		try {
			final int userId = request.getUserId();
			final String orderId = request.getOrderId();
			OrderEventEnum event = request.getEvent();
			OrderDTO orderDTO = orderService.loadByNo(orderId);
			OrderEventRoleEnum role = event.getRole();
			Validate.isTrue((role == OrderEventRoleEnum.USER && orderDTO.getUserId() == userId)
					|| (role == OrderEventRoleEnum.SYS), "用户权限错误");
			final int status = orderDTO.getStatus();
			OrderStatusEnum nextStatus = null;
			switch (role) {
			case USER:
				nextStatus = this.orderStateMachine.computeStatusForBuyer(status, event);
				break;
			case SYS:
				nextStatus = this.orderStateMachine.computeStatusForSystem(status, event);
				break;
			}
			if (nextStatus == null) {
				throw new IllegalStateException("no next status for " + role);
			}

			if (!handleExtraBefore(orderDTO)) {
				successful = false;
				message = "订单状态或货柜状态异常";
				if(this instanceof FinishPayProcessor){
					System.out.println("&&");
					message = "您的货柜还未清关，请清关后再结算，谢谢！";
				}
				return new OrderProcessResponse(successful, message);
			}
			this.updateOrder(request, orderDTO, nextStatus);

			successful = true;

			final int toStatus = nextStatus.getStatus();
			final int type = request.getOperatorType();

			//统计表
			orderLogService.addRecordTime(orderDTO.getNo(),toStatus);

			// 同时记录订单变更日志
			orderTaskHelper.submitRunnable(new Runnable() {
				@Override
				public void run() {
					orderLogService.addLog(orderId, userId, type, status, toStatus, OrderUpdateTypeEnum.get(type)
							.getMessage());
				}
			});
		} catch (IllegalArgumentException e) {
			message = "请求不正确：" + e.getMessage();
			LOGGER.warn("[bad args] bad arguments for process: " + request);
			throw new IllegalArgumentException("请求不正确", e);
		} catch (IllegalStateException e) {
			message = "未知的订单状态";
			LOGGER.warn("[bad states] bad state for process: " + request + ", " + e.getMessage());
			throw new IllegalStateException("未知的订单状态", e);
		} catch (RuntimeException e) {
			message = "系统繁忙";
			LOGGER.error("can not process: " + request, e);
			EventHelper.triggerEvent(this.eventPublishers, "change.order." + request.getOrderId(),
					"failed to change order " + JSON.toJSONString(request), EventLevel.URGENT, e,
					ServerIpUtils.getServerIp());
			throw new RuntimeException("系统繁忙", e);
		}

		return new OrderProcessResponse(successful, message);
	}

	protected void updateOrder(OrderProcessRequest request, OrderDTO orderDTO, OrderStatusEnum nextStatus) {
		int status = orderDTO.getStatus();
		orderDTO.setStatus(nextStatus.getStatus());
		orderDTO.setLastEditor(request.getEvent().getRole() + ":" + request.getUserId());
		this.setExtraOrderInfo(orderDTO);
		handleExtra(orderDTO, status, request);
		orderService.update(orderDTO);
		String smsTemplate = smsTemplates.get(nextStatus);
		handleExtraAfter(orderDTO, status,smsTemplate);
	}

	/**
	 * 处理其他操作
	 *
	 * @param orderDTO
	 * @param
	 * @param
	 */
	protected boolean handleExtraBefore(OrderDTO orderDTO) {
		return true;
	}

	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {
	}
	
	protected void handleExtraAfter(OrderDTO orderDTO, int status, String smsTemplate) {
	}

	protected void setExtraOrderInfo(OrderDTO orderDTO) {
	}
	
	/**
	 * 根据订单状态判断查询正式表还是临时表
	 * 
	 * @param status
	 * @return
	 */
	protected boolean isQueryTmp(int status) {
		if (status == 1 || status == 2 || status == 3) {
			return true;
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		smsTemplates.put(OrderStatusEnum.SAVED, "");
		smsTemplates.put(OrderStatusEnum.SUBMITED, "");
		smsTemplates.put(OrderStatusEnum.SYS_CONFIRMED,
				"【九创金服】尊敬的客户，您的订单{orderId}信息经由平台客服与您电话沟通确认修改后审核通过，请您尽快登录平台查看订单详情，并确认提交订单。");
		smsTemplates.put(OrderStatusEnum.CONFIRMED, "");
		smsTemplates.put(OrderStatusEnum.SIGNED_CONTRACT,
				"【九创金服】尊敬的客户，您的订单{orderId}合同已签订，请按合同约定日期支付采购预付款，如有疑问，请致电4008-265-128。");
		smsTemplates.put(OrderStatusEnum.PAID, "【九创金服】尊敬的客户，您的订单{orderId}预付款已确认收款，请登录平台查看详情。");
		smsTemplates.put(OrderStatusEnum.SHIPPED,
				"【九创金服】尊敬的客户，您的订单{orderId}中的货柜{containerId}{containerName}已发货，请登录平台查看详情。");
		smsTemplates
				.put(OrderStatusEnum.CLEARANCED,
						"【九创金服】尊敬的客户，您的订单{orderId}中的货柜{containerId}{containerName}已通关，预计{preReceiveTime}到货，请于该日期前结算订单尾款，如有疑问，请致电4008-265-128。");
		smsTemplates.put(OrderStatusEnum.SETTLEMENTED, "【九创金服】尊敬的客户，您的订单{orderId}费用已结清，请登录平台查看详情。");
		smsTemplates.put(OrderStatusEnum.RECEIVED,
				"【九创金服】尊敬的客户，您的订单{orderId}中的货柜{containerId}{containerName}已签收，请登录平台查看详情。如有疑问，请致电4008-265-128。");
		smsTemplates.put(OrderStatusEnum.FINISHED, "");
		smsTemplates.put(OrderStatusEnum.USER_CANCELED, "");
		smsTemplates.put(OrderStatusEnum.SYS_CANCELED, "【九创金服】尊敬的客户，您的订单{orderId}经由平台客服与您电话沟通确认已取消，请登录平台查看详情。");
		smsTemplates.put(OrderStatusEnum.SYSTEM_CLOSED, "");
	}
}
