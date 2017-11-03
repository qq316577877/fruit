/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.request;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.loan.biz.common.LoanUserApplyCreditStatusEnum;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserInfoQueryRequest.java
 */
public class UserLoanInfoQueryRequest implements Serializable
{
    private String keyword;

    private UserTypeEnum type;

    private LoanUserApplyCreditStatusEnum status;

    private String sortKey;

    private boolean desc;

    private String tagIds;

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public UserTypeEnum getType()
    {
        return type;
    }

    public void setType(UserTypeEnum type)
    {
        this.type = type;
    }

    public LoanUserApplyCreditStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(LoanUserApplyCreditStatusEnum status)
    {
        this.status = status;
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

    public String getTagIds()
    {
        return tagIds;
    }

    public void setTagIds(String tagIds)
    {
        this.tagIds = tagIds;
    }
}
