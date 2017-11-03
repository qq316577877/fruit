/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.request;

import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.loan.biz.common.LoanUserApplyCreditStatusEnum;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;

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
public class UserCreditInfoQueryRequest implements Serializable
{
    private String keyword;

    private UserTypeEnum type;

    private List<Integer> statusList;

    private String sortKey;

    private boolean desc;

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

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public List<Integer> getStatusList() {

        return statusList;
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
