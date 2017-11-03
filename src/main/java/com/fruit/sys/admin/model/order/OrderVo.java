package com.fruit.sys.admin.model.order;

import java.util.List;

public class OrderVo {

	private String orderId;

	private int userId;

	private int type;

	private int supplierId;

	private String userIp;

	private String frontMemo;

	private String backMemo;

	private List<OrderContainer> orderContainers;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getFrontMemo() {
		return frontMemo;
	}

	public void setFrontMemo(String frontMemo) {
		this.frontMemo = frontMemo;
	}

	public String getBackMemo() {
		return backMemo;
	}

	public void setBackMemo(String backMemo) {
		this.backMemo = backMemo;
	}

	public List<OrderContainer> getOrderContainers() {
		return orderContainers;
	}

	public void setOrderContainers(List<OrderContainer> orderContainers) {
		this.orderContainers = orderContainers;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
