/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import com.fruit.account.biz.common.UserAccountSourceEnum;
import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;

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
public class UserInfoQueryRequest implements Serializable
{
    private String keyword;

    private UserTypeEnum type;

    private UserStatusEnum status;

    private UserEnterpriseStatusEnum enterpriseStatusEnum;

    private List<Integer> notIncludeStatus;

    public List<Integer> getNotIncludeStatus() {
        return notIncludeStatus;
    }

    public void setNotIncludeStatus(List<Integer> notIncludeStatus) {
        this.notIncludeStatus = notIncludeStatus;
    }

    //20170807 用户类型排除未认证
    private List<Integer> includeIds;

    public List<Integer> getIncludeIds() {
        return includeIds;
    }

    public void setIncludeIds(List<Integer> includeIds) {
        this.includeIds = includeIds;
    }


    public UserEnterpriseStatusEnum getEnterpriseStatusEnum() {
        return enterpriseStatusEnum;
    }

    public void setEnterpriseStatusEnum(UserEnterpriseStatusEnum enterpriseStatusEnum) {
        this.enterpriseStatusEnum = enterpriseStatusEnum;
    }

    private UserAccountSourceEnum source;

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

    public UserStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(UserStatusEnum status)
    {
        this.status = status;
    }

    public UserAccountSourceEnum getSource()
    {
        return source;
    }

    public void setSource(UserAccountSourceEnum source)
    {
        this.source = source;
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
