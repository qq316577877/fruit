package com.fruit.sys.admin.model.neworder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContainerInsuranceInfoVO implements Serializable {
    private long containerId;

    private String containerSerialNo;

    private String containerNo;

    private BigDecimal containerTotalAmount;

    private int deliveryType;

    private String deliveryTypeDesc;

    private String containerBoxNo;

    private String foreignTravelNo;

    private String mainTravelNo;

    private String applicationNo;

    private Date addTime;

    private int updateUser;

    private Date updateTime;

    private int changeFlag;

    private static final long serialVersionUID = 1L;

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public String getContainerSerialNo() {
        return containerSerialNo;
    }

    public void setContainerSerialNo(String containerSerialNo) {
        this.containerSerialNo = containerSerialNo == null ? null : containerSerialNo.trim();
    }

    public BigDecimal getContainerTotalAmount() {
        return containerTotalAmount;
    }

    public void setContainerTotalAmount(BigDecimal containerTotalAmount) {
        this.containerTotalAmount = containerTotalAmount;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryTypeDesc() {
        return deliveryTypeDesc;
    }

    public void setDeliveryTypeDesc(String deliveryTypeDesc) {
        this.deliveryTypeDesc = deliveryTypeDesc;
    }

    public String getContainerBoxNo() {
        return containerBoxNo;
    }

    public void setContainerBoxNo(String containerBoxNo) {
        this.containerBoxNo = containerBoxNo;
    }

    public String getForeignTravelNo() {
        return foreignTravelNo;
    }

    public void setForeignTravelNo(String foreignTravelNo) {
        this.foreignTravelNo = foreignTravelNo;
    }

    public String getMainTravelNo() {
        return mainTravelNo;
    }

    public void setMainTravelNo(String mainTravelNo) {
        this.mainTravelNo = mainTravelNo;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(int updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(int changeFlag) {
        this.changeFlag = changeFlag;
    }
}