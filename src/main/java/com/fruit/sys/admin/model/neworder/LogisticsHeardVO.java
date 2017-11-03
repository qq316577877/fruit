package com.fruit.sys.admin.model.neworder;

import com.fruit.sys.admin.model.order.BizFileVo;

import java.util.List;

public class LogisticsHeardVO {


	private long logisticsId;

	private long containerId;

	private  int type;

	private  String typeDesc;

	private String driverName;

	private String driverMobile;

	private String signer;

	private String signerMobile;

	private String addTimeStr;


	private List<BizFileVo> filePaths;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
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

	public String getAddTimeStr() {
		return addTimeStr;
	}

	public void setAddTimeStr(String addTimeStr) {
		this.addTimeStr = addTimeStr;
	}

	public List<BizFileVo> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<BizFileVo> filePaths) {
		this.filePaths = filePaths;
	}
}
