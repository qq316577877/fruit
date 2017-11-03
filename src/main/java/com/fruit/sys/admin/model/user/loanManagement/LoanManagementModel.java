package com.fruit.sys.admin.model.user.loanManagement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LoanManagementModel implements Serializable {
    private long id;

    private int userId;

    private String orderNo;

    private String transactionNo;

    private int productId;

    private String prodictName;

    private BigDecimal availableLoan;

    private BigDecimal appliyLoan;

    private BigDecimal confirmLoan;

    private BigDecimal offerLoan;

    private String dbtNo;

    private Date dbtExpDt;

    private Date offerTime;

    private Date expiresTime;

    private BigDecimal repaymentAmount;

    private Date repaymentTime;

    private BigDecimal serviceFee;

    private int status;

    private Date addTime;

    private Date updateTime;

    private String username;

    private String identity;

    private String mobile;

    private String orderId;

    private int orderStatus;

    private String orderStatusDesc;

    private String containerId;

    private int containerStatus;

    private String containerStatusDesc;

    private String contractNumber;

    private String deliveryTime;

    private String preReceiveTime;

    private boolean clearFlag;

    private String dbtExpDtString;
    private String offerTimeString;
    private String expiresTimeString;
    private String repaymentTimeString;
    private String addTimeString;

    private BigDecimal repaymentInterest;

    private long contractId;

    private String projectCode;

    private String contractUrl;

    public BigDecimal getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(BigDecimal repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getDbtExpDtString() {
        return dbtExpDtString;
    }

    public void setDbtExpDtString(String dbtExpDtString) {
        this.dbtExpDtString = dbtExpDtString;
    }

    public String getOfferTimeString() {
        return offerTimeString;
    }

    public void setOfferTimeString(String offerTimeString) {
        this.offerTimeString = offerTimeString;
    }

    public String getExpiresTimeString() {
        return expiresTimeString;
    }

    public void setExpiresTimeString(String expiresTimeString) {
        this.expiresTimeString = expiresTimeString;
    }

    public String getRepaymentTimeString() {
        return repaymentTimeString;
    }

    public void setRepaymentTimeString(String repaymentTimeString) {
        this.repaymentTimeString = repaymentTimeString;
    }

    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
    }

    private String updateTimeString;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public int getContainerStatus() {
        return containerStatus;
    }

    public void setContainerStatus(int containerStatus) {
        this.containerStatus = containerStatus;
    }

    public String getContainerStatusDesc() {
        return containerStatusDesc;
    }

    public void setContainerStatusDesc(String containerStatusDesc) {
        this.containerStatusDesc = containerStatusDesc;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPreReceiveTime() {
        return preReceiveTime;
    }

    public void setPreReceiveTime(String preReceiveTime) {
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

    private String clearAddress;

    private String receiverAddress;

    private static final long serialVersionUID = 1L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo == null ? null : transactionNo.trim();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProdictName() {
        return prodictName;
    }

    public void setProdictName(String prodictName) {
        this.prodictName = prodictName == null ? null : prodictName.trim();
    }

    public BigDecimal getAvailableLoan() {
        return availableLoan;
    }

    public void setAvailableLoan(BigDecimal availableLoan) {
        this.availableLoan = availableLoan;
    }

    public BigDecimal getAppliyLoan() {
        return appliyLoan;
    }

    public void setAppliyLoan(BigDecimal appliyLoan) {
        this.appliyLoan = appliyLoan;
    }

    public BigDecimal getConfirmLoan() {
        return confirmLoan;
    }

    public void setConfirmLoan(BigDecimal confirmLoan) {
        this.confirmLoan = confirmLoan;
    }

    public BigDecimal getOfferLoan() {
        return offerLoan;
    }

    public void setOfferLoan(BigDecimal offerLoan) {
        this.offerLoan = offerLoan;
    }

    public String getDbtNo() {
        return dbtNo;
    }

    public void setDbtNo(String dbtNo) {
        this.dbtNo = dbtNo == null ? null : dbtNo.trim();
    }

    public Date getDbtExpDt() {
        return dbtExpDt;
    }

    public void setDbtExpDt(Date dbtExpDt) {
        this.dbtExpDt = dbtExpDt;
    }

    public Date getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(Date offerTime) {
        this.offerTime = offerTime;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
}