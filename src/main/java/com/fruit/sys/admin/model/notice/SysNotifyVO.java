/*
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.notice;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2016-04-08
 * Project        : points-biz-api
 * File Name      : SysNotifyVO.java
 */
public class SysNotifyVO implements Serializable
{

    private static final long serialVersionUID = 4980425087766347592L;

    private int id;

    private int sysId;

    private int type;

    private String typeDesc;

    private int position;

    private int templateId;

    private boolean checked;

    private int status;

    private String bizId;

    private String sysCenterMsg;

    private String sysCenterUrl;

    private String mobileMsg;

    private String mailSubject;

    private String mailMsg;

    private Date readTime;

    private Date addTime;

    private Date updateTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSysId()
    {
        return sysId;
    }

    public void setSysId(int sysId)
    {
        this.sysId = sysId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTypeDesc()
    {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc)
    {
        this.typeDesc = typeDesc;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(int templateId)
    {
        this.templateId = templateId;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getBizId()
    {
        return bizId;
    }

    public void setBizId(String bizId)
    {
        this.bizId = bizId;
    }

    public String getSysCenterMsg()
    {
        return sysCenterMsg;
    }

    public void setSysCenterMsg(String sysCenterMsg)
    {
        this.sysCenterMsg = sysCenterMsg;
    }

    public String getSysCenterUrl()
    {
        return sysCenterUrl;
    }

    public void setSysCenterUrl(String sysCenterUrl)
    {
        this.sysCenterUrl = sysCenterUrl;
    }

    public String getMobileMsg()
    {
        return mobileMsg;
    }

    public void setMobileMsg(String mobileMsg)
    {
        this.mobileMsg = mobileMsg;
    }

    public String getMailSubject()
    {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject)
    {
        this.mailSubject = mailSubject;
    }

    public String getMailMsg()
    {
        return mailMsg;
    }

    public void setMailMsg(String mailMsg)
    {
        this.mailMsg = mailMsg;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
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
