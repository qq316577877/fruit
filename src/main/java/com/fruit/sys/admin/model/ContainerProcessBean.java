package com.fruit.sys.admin.model;


public class ContainerProcessBean {

	private int eventCode;

	private long loanInfoId;

	private String signLocationIp;


	public int getEventCode() {
		return eventCode;
	}

	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}

	public long getLoanInfoId() {
		return loanInfoId;
	}

	public void setLoanInfoId(long loanInfoId) {
		this.loanInfoId = loanInfoId;
	}

	public String getSignLocationIp() {
		return signLocationIp;
	}

	public void setSignLocationIp(String signLocationIp) {
		this.signLocationIp = signLocationIp;
	}
}
