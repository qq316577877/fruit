/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : UserInfo.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import java.io.Serializable;

public class UserWithdrawModel implements Serializable
{
    private int userId;

    private String account;

    private String accountName;

    private String bank;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getBank()
    {
        return bank;
    }

    public void setBank(String bank)
    {
        this.bank = bank;
    }
}
