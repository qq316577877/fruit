/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : Dictionary.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import java.io.Serializable;

/**
 * 功能描述:  <p>
 *
 * @author : terry <p>
 * @version 1.0 2015-06-12
 * @since beauty-admin-web 1.0
 */
public class DictionaryDto implements Serializable
{
    private String key;

    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
