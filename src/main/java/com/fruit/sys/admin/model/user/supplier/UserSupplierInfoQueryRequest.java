/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.user.supplier;

import com.fruit.account.biz.common.*;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-02
 * Project        : fruit
 * File Name      : UserSupplierInfoQueryRequest.java
 */
public class UserSupplierInfoQueryRequest implements Serializable
{
    private String keyword;

    private String sortKey;

    private boolean desc;

    private DBStatusEnum status;

    public DBStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DBStatusEnum status) {
        this.status = status;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getSortKey()
    {
        return sortKey;
    }

    public void setSortKey(String sortKey)
    {
        this.sortKey = sortKey;
    }

    public boolean isDesc()
    {
        return desc;
    }

    public void setDesc(boolean desc)
    {
        this.desc = desc;
    }


}
