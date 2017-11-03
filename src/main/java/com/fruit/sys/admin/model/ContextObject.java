/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model;

import com.ovfintech.arch.utils.useragent.UserAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : ContextObject.java
 */
public class ContextObject implements Serializable
{
    private String userToken = "";

    private int userId;

    private int userType;

    private String userIp;

    private UserAgent userAgentModel;

    private String userAgent;

    private Map<String, Object> contents = new HashMap<String, Object>();

    public Map<String, Object> getContents()
    {
        return contents;
    }

    public void setContents(Map<String, Object> contents)
    {
        this.contents = contents;
    }

    public UserAgent getUserAgentModel()
    {
        return userAgentModel;
    }

    public void setUserAgentModel(UserAgent userAgentModel)
    {
        this.userAgentModel = userAgentModel;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getUserIp()
    {
        return userIp;
    }

    public void setUserIp(String userIp)
    {
        this.userIp = userIp;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken(String userToken)
    {
        this.userToken = userToken;
    }

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public void clear()
    {
        this.setUserId(0);
        this.setUserType(0);
        this.setUserAgent(null);
        this.setContents(new HashMap<String, Object>());
        this.setUserAgentModel(null);
        this.setUserIp(null);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("ContextObject{");
        sb.append("contents=").append(contents);
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", userType=").append(userType);
        sb.append(", userIp='").append(userIp).append('\'');
        sb.append(", userAgentModel=").append(userAgentModel);
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
