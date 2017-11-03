/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import java.io.Serializable;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : partal-main-web
 * File Name      : IdValueVO.java
 */
public class IdValueVO implements Serializable
{
    private static final long serialVersionUID = 5714597589419971215L;

    private int id;

    private String value;

    /**
     * 0.未选中，1.选中
     */
    private int selected = 0;

    public IdValueVO(int id, String value)
    {
        this(id, value, 0);
    }

    public IdValueVO(int id, String value, int selected)
    {
        this.id = id;
        this.value = value;
        this.selected = selected;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int getSelected()
    {
        return selected;
    }

    public void setSelected(int selected)
    {
        this.selected = selected;
    }
}
