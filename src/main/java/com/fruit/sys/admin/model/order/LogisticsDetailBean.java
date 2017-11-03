package com.fruit.sys.admin.model.order;

import java.util.Date;
import java.util.List;

public class LogisticsDetailBean {

	private long id;

	private long logisticsId;

	private String containerNo;

	private int type;

	private String transportNumber;

	private String detailInfo;

	private int status;

	private List<BizFileVo> filePaths;

	private Date addTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTransportNumber() {
		return transportNumber;
	}

	public void setTransportNumber(String transportNumber) {
		this.transportNumber = transportNumber;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<BizFileVo> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<BizFileVo> filePaths) {
		this.filePaths = filePaths;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
