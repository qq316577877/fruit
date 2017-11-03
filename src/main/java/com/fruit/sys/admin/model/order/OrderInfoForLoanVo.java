package com.fruit.sys.admin.model.order;

import java.util.Date;

public class OrderInfoForLoanVo {

	private String orderId;

	private int orderStatus;

	private String orderStatusDesc;

	private String containerId;

	private String transactionNo;

	private int containerStatus;

	private String containerStatusDesc;

	private String contractNumber;

	private Date deliveryTime;

	private Date preReceiveTime;

	private boolean clearFlag = false;

	private String clearAddress = "广西凭祥友谊关";

	private String receiverAddress;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public int getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(int containerStatus) {
		this.containerStatus = containerStatus;
	}

	public String getContainerStatusDesc() {
		return containerStatusDesc;
	}

	public void setContainerStatusDesc(String containerStatusDesc) {
		this.containerStatusDesc = containerStatusDesc;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getPreReceiveTime() {
		return preReceiveTime;
	}

	public void setPreReceiveTime(Date preReceiveTime) {
		this.preReceiveTime = preReceiveTime;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public boolean isClearFlag() {
		return clearFlag;
	}

	public void setClearFlag(boolean clearFlag) {
		this.clearFlag = clearFlag;
	}

	public String getClearAddress() {
		return clearAddress;
	}

	public void setClearAddress(String clearAddress) {
		this.clearAddress = clearAddress;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
}
