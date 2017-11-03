/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.user.profile;

import com.fruit.sys.admin.model.configure.EnterpriseInfoModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:    用户信息配置---清关公司、物流公司
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-03
 * Project        : fruit
 * File Name      : UserProfileModel.java
 */
public class UserProfileModel implements Serializable
{
    private static final long serialVersionUID = 8351352534795376930L;

    private int id;

    private int userId;

    private int nationalLogistics;

    private EnterpriseInfoModel nationalLogisticsModel;

    private int internationalLogistics;

    private EnterpriseInfoModel internationalLogisticsModel;

    private int nationalClearance;

    private EnterpriseInfoModel nationalClearanceModel;

    private int internationalClearance;

    private EnterpriseInfoModel internationalClearanceModel;

    private int status;

    private String statusDesc;

    private Date addTime;

    private Date updateTime;

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

    public int getNationalLogistics() {
        return nationalLogistics;
    }

    public void setNationalLogistics(int nationalLogistics) {
        this.nationalLogistics = nationalLogistics;
    }


    public int getInternationalLogistics() {
        return internationalLogistics;
    }

    public void setInternationalLogistics(int internationalLogistics) {
        this.internationalLogistics = internationalLogistics;
    }


    public int getNationalClearance() {
        return nationalClearance;
    }

    public void setNationalClearance(int nationalClearance) {
        this.nationalClearance = nationalClearance;
    }


    public int getInternationalClearance() {
        return internationalClearance;
    }

    public void setInternationalClearance(int internationalClearance) {
        this.internationalClearance = internationalClearance;
    }


    public int getStatus() {
        return status;
    }

    public EnterpriseInfoModel getNationalLogisticsModel() {
        return nationalLogisticsModel;
    }

    public void setNationalLogisticsModel(EnterpriseInfoModel nationalLogisticsModel) {
        this.nationalLogisticsModel = nationalLogisticsModel;
    }

    public EnterpriseInfoModel getInternationalLogisticsModel() {
        return internationalLogisticsModel;
    }

    public void setInternationalLogisticsModel(EnterpriseInfoModel internationalLogisticsModel) {
        this.internationalLogisticsModel = internationalLogisticsModel;
    }

    public EnterpriseInfoModel getNationalClearanceModel() {
        return nationalClearanceModel;
    }

    public void setNationalClearanceModel(EnterpriseInfoModel nationalClearanceModel) {
        this.nationalClearanceModel = nationalClearanceModel;
    }

    public EnterpriseInfoModel getInternationalClearanceModel() {
        return internationalClearanceModel;
    }

    public void setInternationalClearanceModel(EnterpriseInfoModel internationalClearanceModel) {
        this.internationalClearanceModel = internationalClearanceModel;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
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
