/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : EnterpriseVO.java
 */
public class EnterpriseVO implements Serializable
{
    private static final long serialVersionUID = 800477390041519619L;

    private int id;

    private int userId;

    private int type;

    private String enterpriseName;

    private String typeDesc;

    private String name;

    private String identity;

    private int countryId;

    private String countryName;

    private int districtId;

    private String districtName;

    private String phoneNum;

    private String address;

    private int provinceId;

    private String provinceName;

    private int cityId;

    private String cityName;

    private String licence;

    private String licenceUrl;

    private String credential;

    private String identityFrontUrl;

    private String identityFront;

    private String identityBack;

    private String identityBackUrl;

    private String attachmentOne;

    private String attachmentOneUrl;

    private String attachmentTwo;

    private String attachmentTwoUrl;

    public String getLicenceUrl() {
        return licenceUrl;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    public String getIdentityFrontUrl() {
        return identityFrontUrl;
    }

    public void setIdentityFrontUrl(String identityFrontUrl) {
        this.identityFrontUrl = identityFrontUrl;
    }

    public String getIdentityBackUrl() {
        return identityBackUrl;
    }

    public void setIdentityBackUrl(String identityBackUrl) {
        this.identityBackUrl = identityBackUrl;
    }

    public String getAttachmentOneUrl() {
        return attachmentOneUrl;
    }

    public void setAttachmentOneUrl(String attachmentOneUrl) {
        this.attachmentOneUrl = attachmentOneUrl;
    }

    public String getAttachmentTwoUrl() {
        return attachmentTwoUrl;
    }

    public void setAttachmentTwoUrl(String attachmentTwoUrl) {
        this.attachmentTwoUrl = attachmentTwoUrl;
    }

    private int status;

    private String statusDesc;

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    private String rejectNote;

    private String description;

    private int memberIdentification;

    public int getMemberIdentification() {
        return memberIdentification;
    }

    public void setMemberIdentification(int memberIdentification) {
        this.memberIdentification = memberIdentification;
    }

    private String lastEditor;

    private Date addTime;

    private Date updateTime;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getIdentityFront() {
        return identityFront;
    }

    public void setIdentityFront(String identityFront) {
        this.identityFront = identityFront;
    }

    public String getIdentityBack() {
        return identityBack;
    }

    public void setIdentityBack(String identityBack) {
        this.identityBack = identityBack;
    }

    public String getAttachmentOne() {
        return attachmentOne;
    }

    public void setAttachmentOne(String attachmentOne) {
        this.attachmentOne = attachmentOne;
    }

    public String getAttachmentTwo() {
        return attachmentTwo;
    }

    public void setAttachmentTwo(String attachmentTwo) {
        this.attachmentTwo = attachmentTwo;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getCityId()
    {
        return cityId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getRejectNote()
    {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote)
    {
        this.rejectNote = rejectNote;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLastEditor()
    {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor)
    {
        this.lastEditor = lastEditor;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
