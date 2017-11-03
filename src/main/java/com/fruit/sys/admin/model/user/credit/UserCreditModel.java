/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.user.credit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:    用户基础信息
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UserModel.java
 */
public class UserCreditModel implements Serializable
{
    private int id;

    private int userId;

    private String username;

    private String identity;

    private String mobile;

    private BigDecimal creditLine;

    private BigDecimal balance;

    /**
     * 电子合同新增字段
     */
    private long contractId;

    private String projectCode;

    private String contractUrl;
    /**
     * 新增结算
     */

    private int type;

    private int status;

    private String ctrNo;

    private String crCstNo;

    private String rejectNote;

    private String description;

    private Date expireTime;

    private Date addTime;

    private Date updateTime;

    private String expireTimeString;

    private String addTimeString;

    private String updateTimeString;

    public void setCtrBankNo(String ctrBankNo) {
        this.ctrBankNo = ctrBankNo;
    }

    public void setInsureCtrNo(String insureCtrNo) {
        this.insureCtrNo = insureCtrNo;
    }

    public String getCtrBankNo() {

        return ctrBankNo;
    }

    public String getInsureCtrNo() {
        return insureCtrNo;
    }

    private String ctrBankNo;

    private String insureCtrNo;

    public String getAddTimeString() {
        return addTimeString;
    }

    public void setAddTimeString(String addTimeString) {
        this.addTimeString = addTimeString;
    }

    public String getUpdateTimeString() {
        return updateTimeString;
    }

    public void setUpdateTimeString(String updateTimeString) {
        this.updateTimeString = updateTimeString;
    }

    public String getExpireTimeString() {
        return expireTimeString;
    }

    public void setExpireTimeString(String expireTimeString) {
        this.expireTimeString = expireTimeString;
    }

    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public String getCtrNo() {
        return ctrNo;
    }

    public void setCtrNo(String ctrNo) {
        this.ctrNo = ctrNo == null ? null : ctrNo.trim();
    }

    public String getCrCstNo() {
        return crCstNo;
    }

    public void setCrCstNo(String crCstNo) {
        this.crCstNo = crCstNo == null ? null : crCstNo.trim();
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote == null ? null : rejectNote.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
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

    private BigDecimal Expenditure;

    public BigDecimal getExpenditure() {
        return Expenditure;
    }

    public void setExpenditure(BigDecimal expenditure) {
        Expenditure = expenditure;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
