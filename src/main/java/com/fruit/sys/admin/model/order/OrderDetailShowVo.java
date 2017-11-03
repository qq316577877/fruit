package com.fruit.sys.admin.model.order;

import java.util.List;

public class OrderDetailShowVo {

	private long id;

	private String orderNo;

	private int status;

	private int type;

	private int supplierId;

	private String orderStatusDesc;

	private Integer needLoan;

	private List<OrderContainer> orderContainers;

	private List<SupplierVo> supplierList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public Integer getNeedLoan() {
		return needLoan;
	}

	public void setNeedLoan(Integer needLoan) {
		this.needLoan = needLoan;
	}

	public List<OrderContainer> getOrderContainers() {
		return orderContainers;
	}

	public void setOrderContainers(List<OrderContainer> orderContainers) {
		this.orderContainers = orderContainers;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<SupplierVo> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<SupplierVo> supplierList) {
		this.supplierList = supplierList;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
}
