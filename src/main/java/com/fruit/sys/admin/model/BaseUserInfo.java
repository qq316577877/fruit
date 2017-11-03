/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : BaseUserInfo.java
 */
public class BaseUserInfo implements Serializable
{
    private String enterpriseName;

    private String userName;

    private int userId;

    private String mobile;

    private String contact;

    private String sales;

    private int provinceId;

    private int profileType;

    private int type;

    private Date enterpriseAddTime;

    private int cityId;

    public String getEnterpriseName()
    {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getSales()
    {
        return sales;
    }

    public void setSales(String sales)
    {
        this.sales = sales;
    }

    public int getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }

    public int getProfileType()
    {
        return profileType;
    }

    public void setProfileType(int profileType)
    {
        this.profileType = profileType;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public Date getEnterpriseAddTime()
    {
        return enterpriseAddTime;
    }

    public void setEnterpriseAddTime(Date enterpriseAddTime)
    {
        this.enterpriseAddTime = enterpriseAddTime;
    }

    public int getCityId()
    {
        return cityId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }
}
