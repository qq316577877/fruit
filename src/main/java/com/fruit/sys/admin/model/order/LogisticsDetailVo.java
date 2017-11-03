package com.fruit.sys.admin.model.order;

import java.util.Date;
import java.util.List;

public class LogisticsDetailVo {

	private List<LogisticsDetailBean> logisticsDetails;

	private String ContainerNo;

	private String containerName;

	private int containerStatus;
	
	private Date deliveryTime;

	private Date preReceiveTime;

	private boolean clearFlag = false;

	private String clearAddress = "广西凭祥友谊关";

	private String receiverAddress;

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

	public List<LogisticsDetailBean> getLogisticsDetails() {
		return logisticsDetails;
	}

	public void setLogisticsDetails(List<LogisticsDetailBean> logisticsDetails) {
		this.logisticsDetails = logisticsDetails;
	}

	public String getContainerNo() {
		return ContainerNo;
	}

	public void setContainerNo(String containerNo) {
		ContainerNo = containerNo;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public int getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(int containerStatus) {
		this.containerStatus = containerStatus;
	}
}
