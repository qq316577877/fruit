/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : UserInfo.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import java.util.Date;

public class UserInfo
{
    private boolean isAdmin;

    private int sysId;

    private String userName;

    private String departName;

    private String mobile;

    private String mail;

    private int status;

    private Date addTime;

    private Date updateTime;

    private String cnName;

    private String enName;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDepartName()
    {
        return departName;
    }

    public void setDepartName(String departName)
    {
        this.departName = departName;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public int getSysId()
    {
        return sysId;
    }

    public void setSysId(int sysId)
    {
        this.sysId = sysId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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

    public String getCnName()
    {
        return cnName;
    }

    public void setCnName(String cnName)
    {
        this.cnName = cnName;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }
}
