/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.configure;


import java.io.Serializable;
import java.util.Date;

/**
 * Description:    清关物流公司企业信息
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-03
 * Project        : fruit
 * File Name      : EnterpriseInfoModel.java
 */
public class EnterpriseInfoModel implements Serializable
{
    private static final long serialVersionUID = 8351352534795376930L;

    private int id;

    private String name;

    private String enName;

    private String credential;

    private int type;

    private String typeDesc;

    private int locationType;

    private String locationTypeDesc;

    private String contact;

    private Date addTime;

    private Date updateTime;

    private int status;

    private String statusDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
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

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getLocationTypeDesc() {
        return locationTypeDesc;
    }

    public void setLocationTypeDesc(String locationTypeDesc) {
        this.locationTypeDesc = locationTypeDesc;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public int getStatus() {
        return status;
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
}
