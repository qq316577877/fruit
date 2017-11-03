package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;

public class ContainerLoanVo {

	private String containerId;

	private String transactionNo;

	private String productName;

	private Integer productId;

	private BigDecimal loanQuota;
	
	private BigDecimal applyQuota;
	
	private BigDecimal confirmLoan;
	
	private BigDecimal serviceFee;

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public BigDecimal getLoanQuota() {
		return loanQuota;
	}

	public void setLoanQuota(BigDecimal loanQuota) {
		this.loanQuota = loanQuota;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public BigDecimal getApplyQuota() {
		return applyQuota;
	}

	public void setApplyQuota(BigDecimal applyQuota) {
		this.applyQuota = applyQuota;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public BigDecimal getConfirmLoan() {
		return confirmLoan;
	}

	public void setConfirmLoan(BigDecimal confirmLoan) {
		this.confirmLoan = confirmLoan;
	}

}
