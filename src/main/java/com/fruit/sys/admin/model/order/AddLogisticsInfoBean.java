package com.fruit.sys.admin.model.order;

import java.util.List;

public class AddLogisticsInfoBean {
	
	private String orderNo;

	private long logisticsId;

	private String containerNo;

	private String detailInfo;
	
	private String preReceiveTime;

	private List<String> filePaths;
	
	private int receiveFlag;

	private String driverMobile;

	public long getLogisticsId() {
		return logisticsId;
	}

	public void setLogisticsId(long logisticsId) {
		this.logisticsId = logisticsId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public List<String> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<String> filePaths) {
		this.filePaths = filePaths;
	}

	public String getPreReceiveTime() {
		return preReceiveTime;
	}

	public void setPreReceiveTime(String preReceiveTime) {
		this.preReceiveTime = preReceiveTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getReceiveFlag() {
		return receiveFlag;
	}

	public void setReceiveFlag(int receiveFlag) {
		this.receiveFlag = receiveFlag;
	}

	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}
}
