package com.fruit.sys.admin.model.neworder;

import com.fruit.sys.admin.model.order.BizFileVo;

import java.util.List;

public class LogisticsDetailVO {
	
	private String orderNo;

	private long logisticsId;

	private long containerId;

	private String containerBoxNo;

	private String containerName;

	private String containerStatusDesc;

	private String transportNumber;

	private  int type;

	private  String typeDesc;

	private String driverName;

	private String driverMobile;

	private String detailInfo;
	
	private String preReceiveTime;

	private String signer;

	private String signerMobile;

	private String lockId;

	private List<BizFileVo> filePaths;


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public long getLogisticsId() {
		return logisticsId;
	}

	public void setLogisticsId(long logisticsId) {
		this.logisticsId = logisticsId;
	}

	public long getContainerId() {
		return containerId;
	}

	public void setContainerId(long containerId) {
		this.containerId = containerId;
	}

	public String getContainerBoxNo() {
		return containerBoxNo;
	}

	public void setContainerBoxNo(String containerBoxNo) {
		this.containerBoxNo = containerBoxNo;
	}

	public String getTransportNumber() {
		return transportNumber;
	}

	public void setTransportNumber(String transportNumber) {
		this.transportNumber = transportNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getContainerStatusDesc() {
		return containerStatusDesc;
	}

	public void setContainerStatusDesc(String containerStatusDesc) {
		this.containerStatusDesc = containerStatusDesc;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getPreReceiveTime() {
		return preReceiveTime;
	}

	public void setPreReceiveTime(String preReceiveTime) {
		this.preReceiveTime = preReceiveTime;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getSignerMobile() {
		return signerMobile;
	}

	public void setSignerMobile(String signerMobile) {
		this.signerMobile = signerMobile;
	}

	public String getLockId() {
		return lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	public List<BizFileVo> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<BizFileVo> filePaths) {
		this.filePaths = filePaths;
	}
}
