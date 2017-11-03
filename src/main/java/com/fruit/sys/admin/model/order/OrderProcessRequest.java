package com.fruit.sys.admin.model.order;

import java.util.Map;

import com.fruit.order.biz.common.OrderEventEnum;

public class OrderProcessRequest {

	private int userId;

    private String orderId;

    private int operatorType;
    
    private OrderEventEnum event;

    private Map<String, Object> params;

    private String platform;

    private String userIp;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public OrderEventEnum getEvent() {
		return event;
	}

	public void setEvent(OrderEventEnum event) {
		this.event = event;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}
}
