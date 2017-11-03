package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderContainer {

	private long id;

	private String productName;
	
	private String batchNumber;

	private int productId;

	private BigDecimal totalQuantity;
	
	private BigDecimal productAmount;

    private BigDecimal agencyAmount;
    
    private int maxQuantity;

    private BigDecimal premiumAmount;

	private BigDecimal totalPrice;

	private int status;
	
	private List<OrderContainerDetail> orderContainerDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(BigDecimal totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<OrderContainerDetail> getOrderContainerDetails() {
		return orderContainerDetails;
	}

	public void setOrderContainerDetails(List<OrderContainerDetail> orderContainerDetails) {
		this.orderContainerDetails = orderContainerDetails;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(BigDecimal premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public int getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

}
