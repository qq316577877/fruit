package com.fruit.sys.admin.model.order;

import java.util.List;

public class OrderForLogisticsVo {

	private String orderNo;

	private long logiticsId;

	private String placeOrderTime;

	private List<String> orderContainers;

	private EnterpriseInfoVo innerExpress;

	private EnterpriseInfoVo outerExpress;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<String> getOrderContainers() {
		return orderContainers;
	}

	public void setOrderContainers(List<String> orderContainers) {
		this.orderContainers = orderContainers;
	}

	public EnterpriseInfoVo getInnerExpress() {
		return innerExpress;
	}

	public void setInnerExpress(EnterpriseInfoVo innerExpress) {
		this.innerExpress = innerExpress;
	}

	public EnterpriseInfoVo getOuterExpress() {
		return outerExpress;
	}

	public void setOuterExpress(EnterpriseInfoVo outerExpress) {
		this.outerExpress = outerExpress;
	}

	public String getPlaceOrderTime() {
		return placeOrderTime;
	}

	public void setPlaceOrderTime(String placeOrderTime) {
		this.placeOrderTime = placeOrderTime;
	}

	public long getLogiticsId() {
		return logiticsId;
	}

	public void setLogiticsId(long logiticsId) {
		this.logiticsId = logiticsId;
	}
}
