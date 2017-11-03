/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : KeyValueModel.java
 */
public class KeyValueModel
{
    private String key;

    private String value;

    /**
     * 0.未选中，1.选中
     */
    private int selected = 0;

    public KeyValueModel(String key, String value, int selected)
    {
        this.key = key;
        this.value = value;
        this.selected = selected;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    public int getSelected()
    {
        return selected;
    }
}
