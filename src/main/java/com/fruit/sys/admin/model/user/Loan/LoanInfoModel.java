/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.user.Loan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:    用户资金服务信息
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-13
 * Project        : fruit
 * File Name      : LoanInfoModel.java
 */
public class LoanInfoModel implements Serializable
{
    private long id;

    private int userId;

    private String orderNo;

    private int orderStatus;

    private String orderStatusDesc;

    private String transactionNo;

    private int transactionStatus;

    private String transactionStatusDesc;

    private Date deliveryTime;

    private Date preReceiveTime;

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

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionStatusDesc() {
        return transactionStatusDesc;
    }

    public void setTransactionStatusDesc(String transactionStatusDesc) {
        this.transactionStatusDesc = transactionStatusDesc;
    }

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

    private String statusDesc;

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    private Date addTime;

    private Date updateTime;

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
}
