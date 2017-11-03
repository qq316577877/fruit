/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.cache;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : CacheSyncRequest.java
 */
public class CacheSyncRequest implements Serializable
{
    private int userId;

    private long orderId;

    private int status;

    private String userToken;

    private String type;

    private int productId;

    private int bizId;

    public CacheSyncRequest(int userId)
    {
        this.userId = userId;
    }

    public CacheSyncRequest()
    {
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken(String userToken)
    {
        this.userToken = userToken;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getBizId()
    {
        return bizId;
    }

    public void setBizId(int bizId)
    {
        this.bizId = bizId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("userId=").append(userId);
        sb.append(", orderId=").append(orderId);
        sb.append(", status=").append(status);
        sb.append(", userToken='").append(userToken).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", productId=").append(productId);
        sb.append(", bizId=").append(productId);
        sb.append('}');
        return sb.toString();
    }
}
