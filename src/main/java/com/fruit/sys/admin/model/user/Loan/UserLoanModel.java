/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.user.Loan;

import com.fruit.sys.admin.model.user.EnterpriseVO;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:    用户基础信息
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UserModel.java
 */
public class UserLoanModel implements Serializable
{
    private int id;

    private int type;

    private int userId;

    private String enterpriseName;

    private String credential;

    private String username;

    private String identity;

    private String mobile;

    private int maritalStatus;

    private String partnerName;

    private String partnerIdentity;

    private int countryId;

    private int provinceId;

    private int cityId;

    private int districtId;

    private String address;

    private int status;

    private String rejectNote;

    public String getRejectDescription() {
        return rejectDescription;
    }

    public void setRejectDescription(String rejectDescription) {
        this.rejectDescription = rejectDescription;
    }

    private String rejectDescription;

    private Date addTime;

    private Date updateTime;

    private String addTimeString;

    private String updateTimeString;

    private static final long serialVersionUID = 1L;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    private EnterpriseVO enterprise;

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential == null ? null : credential.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(int maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerIdentity() {
        return partnerIdentity;
    }

    public void setPartnerIdentity(String partnerIdentity) {
        this.partnerIdentity = partnerIdentity == null ? null : partnerIdentity.trim();
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote == null ? null : rejectNote.trim();
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

    private UserLoanVO userLoanVO;

    public UserLoanVO getUserLoanVO() {
        return userLoanVO;
    }

    public void setUserLoanVO(UserLoanVO userLoanVO) {
        this.userLoanVO = userLoanVO;
    }
}
