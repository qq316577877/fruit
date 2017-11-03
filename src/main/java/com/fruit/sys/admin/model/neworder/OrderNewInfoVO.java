package com.fruit.sys.admin.model.neworder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderNewInfoVO implements Serializable {
    private long id;

    private String orderNo;

    private String orderSerialNo;

    private int userId;

    private String purchaseName;

    private int type;

    private int status;

    private String statusDesc;

    private int needLoan;

    private BigDecimal loanLimit;

    private BigDecimal loanAmount;

    private BigDecimal prepayments;

    private BigDecimal tailAmount;

    private Date intentStartDate;

    private Date intentEndDate;

    private int varietyId;

    private String varietyName;

    private int containerNum;

    private int goodsNum;

    private BigDecimal totalAmount;

    private int channel;

    private int modeType;

    private String channelDesc;

    private String modeTypeDesc;

    private long contractId;

    private int contractStatus;

    private String userIp;

    private Date addTime;

    private String addTimeStr;

    private int updateUser;

    private Date updateTime;

    private List<OrderGoodsInfoVO> goodsInfoList;

    private  List<ContainerInfoVO> containerInfoList;

    private static final long serialVersionUID = 1L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderSerialNo() {
        return orderSerialNo;
    }

    public void setOrderSerialNo(String orderSerialNo) {
        this.orderSerialNo = orderSerialNo == null ? null : orderSerialNo.trim();
    }

    public List<ContainerInfoVO> getContainerInfoList() {
        return containerInfoList;
    }

    public void setContainerInfoList(List<ContainerInfoVO> containerInfoList) {
        this.containerInfoList = containerInfoList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNeedLoan() {
        return needLoan;
    }

    public void setNeedLoan(int needLoan) {
        this.needLoan = needLoan;
    }

    public BigDecimal getLoanLimit() {
        return loanLimit;
    }

    public void setLoanLimit(BigDecimal loanLimit) {
        this.loanLimit = loanLimit;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(BigDecimal prepayments) {
        this.prepayments = prepayments;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(int contractStatus) {
        this.contractStatus = contractStatus;
    }

    public BigDecimal getTailAmount() {
        return tailAmount;
    }

    public void setTailAmount(BigDecimal tailAmount) {
        this.tailAmount = tailAmount;
    }

    public Date getIntentStartDate() {
        return intentStartDate;
    }

    public void setIntentStartDate(Date intentStartDate) {
        this.intentStartDate = intentStartDate;
    }

    public Date getIntentEndDate() {
        return intentEndDate;
    }

    public void setIntentEndDate(Date intentEndDate) {
        this.intentEndDate = intentEndDate;
    }

    public int getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(int varietyId) {
        this.varietyId = varietyId;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public int getContainerNum() {
        return containerNum;
    }

    public void setContainerNum(int containerNum) {
        this.containerNum = containerNum;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getChannelDesc() {
        return channelDesc;
    }

    public void setChannelDesc(String channelDesc) {
        this.channelDesc = channelDesc;
    }

    public int getModeType() {
        return modeType;
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
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

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getModeTypeDesc() {
        return modeTypeDesc;
    }

    public void setModeTypeDesc(String modeTypeDesc) {
        this.modeTypeDesc = modeTypeDesc;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public void setAddTimeStr(String addTimeStr) {
        this.addTimeStr = addTimeStr;
    }

    public List<OrderGoodsInfoVO> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<OrderGoodsInfoVO> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }
}