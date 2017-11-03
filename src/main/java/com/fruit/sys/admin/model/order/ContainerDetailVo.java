package com.fruit.sys.admin.model.order;

import java.math.BigDecimal;

public class ContainerDetailVo {

	private String containerId;
	
	private String productName;
	
	private Integer containerStatus;
	
	private String containerStatusDesc;
	
	private String loanNo;
	
	private Integer loanStatus;
	
	private String loanStatusDesc;
	
	private BigDecimal appliyLoan;

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

	public Integer getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(Integer containerStatus) {
		this.containerStatus = containerStatus;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Integer getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(Integer loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String getContainerStatusDesc() {
		return containerStatusDesc;
	}

	public void setContainerStatusDesc(String containerStatusDesc) {
		this.containerStatusDesc = containerStatusDesc;
	}

	public String getLoanStatusDesc() {
		return loanStatusDesc;
	}

	public void setLoanStatusDesc(String loanStatusDesc) {
		this.loanStatusDesc = loanStatusDesc;
	}

	public BigDecimal getAppliyLoan() {
		return appliyLoan;
	}

	public void setAppliyLoan(BigDecimal appliyLoan) {
		this.appliyLoan = appliyLoan;
	}

}
