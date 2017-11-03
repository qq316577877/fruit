/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.request;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserInfoQueryRequest.java
 */
public class UserLoanManagementQueryRequest implements Serializable
{
    private String keyword;

    private UserTypeEnum type;

    private LoanInfoStatusEnum status;

    private String sortKey;

    private boolean desc;

    private String tagIds;

    private List<Integer> statusList;

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

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

    public LoanInfoStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(LoanInfoStatusEnum status)
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
