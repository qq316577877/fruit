package com.fruit.sys.admin.model;

public class DebtMQbean {

	private long contractId;

	private String projectCode;

	private int userId;

	private String captchaCode;

	private String signLocationIp;

	public long getContractId() {
		return contractId;
	}

	public void setContractId(long contractId) {
		this.contractId = contractId;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	public String getSignLocationIp() {
		return signLocationIp;
	}

	public void setSignLocationIp(String signLocationIp) {
		this.signLocationIp = signLocationIp;
	}
}
