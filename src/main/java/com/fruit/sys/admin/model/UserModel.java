/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model;

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
public class UserModel implements Serializable
{
    private static final long serialVersionUID = 8351352534795376930L;

    private int userId;

    private String mobile;

    private String mail;

    private String userName;

    private int type;

    private String typeDes;

    private int status;

    private String statusDes;

    private int enterpriseVerifyStatus;

    public String getEnterpriseVerifyStatusDesc() {
        return enterpriseVerifyStatusDesc;
    }

    public void setEnterpriseVerifyStatusDesc(String enterpriseVerifyStatusDesc) {
        this.enterpriseVerifyStatusDesc = enterpriseVerifyStatusDesc;
    }

    private String enterpriseVerifyStatusDesc;

    private int authorizationVerifyStatus;

    private String QQ;

    private int source;

    private String sourceDes;

    private String saleName;

    private int points;

    private Date addTime;

    private String description;

    private int addUserId;

    private String addUserName;

    private String tags;

    private EnterpriseVO enterprise;

    public EnterpriseVO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseVO enterprise) {
        this.enterprise = enterprise;
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

    public String getMail()
    {
        return mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getStatusDes()
    {
        return statusDes;
    }

    public void setStatusDes(String statusDes)
    {
        this.statusDes = statusDes;
    }

    public String getTypeDes()
    {
        return typeDes;
    }

    public void setTypeDes(String typeDes)
    {
        this.typeDes = typeDes;
    }

    public int getEnterpriseVerifyStatus()
    {
        return enterpriseVerifyStatus;
    }

    public void setEnterpriseVerifyStatus(int enterpriseVerifyStatus)
    {
        this.enterpriseVerifyStatus = enterpriseVerifyStatus;
    }

    public int getAuthorizationVerifyStatus()
    {
        return authorizationVerifyStatus;
    }

    public void setAuthorizationVerifyStatus(int authorizationVerifyStatus)
    {
        this.authorizationVerifyStatus = authorizationVerifyStatus;
    }

    public String getQQ()
    {
        return QQ;
    }

    public void setQQ(String QQ)
    {
        this.QQ = QQ;
    }

    public int getSource()
    {
        return source;
    }

    public void setSource(int source)
    {
        this.source = source;
    }

    public String getSourceDes()
    {
        return sourceDes;
    }

    public void setSourceDes(String sourceDes)
    {
        this.sourceDes = sourceDes;
    }

    public String getSaleName()
    {
        return saleName;
    }

    public void setSaleName(String saleName)
    {
        this.saleName = saleName;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getAddUserId()
    {
        return addUserId;
    }

    public void setAddUserId(int addUserId)
    {
        this.addUserId = addUserId;
    }

    public String getAddUserName()
    {
        return addUserName;
    }

    public void setAddUserName(String addUserName)
    {
        this.addUserName = addUserName;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }
}
