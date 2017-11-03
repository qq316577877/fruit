package com.fruit.sys.admin.model.order;

import java.util.List;

public class OrderDetailVo {

	private String orderNo;

	private String date;

	private String supplierName;

	private int orderStatus;

	private String orderStatusDesc;

	private String orderDetailUrl;

	private List<ContainerDetailVo> containerDetails;

	private String purchaseName;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<ContainerDetailVo> getContainerDetails() {
		return containerDetails;
	}

	public void setContainerDetails(List<ContainerDetailVo> containerDetails) {
		this.containerDetails = containerDetails;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public String getOrderDetailUrl() {
		return orderDetailUrl;
	}

	public void setOrderDetailUrl(String orderDetailUrl) {
		this.orderDetailUrl = orderDetailUrl;
	}


	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}
}
