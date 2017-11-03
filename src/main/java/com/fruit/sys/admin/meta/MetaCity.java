/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-23
 * Project        : fruit
 * File Name      : MetaCity.java
 */
public class MetaCity implements Serializable
{
    private int id;

    private String name;

    private List<MetaArea> areas = new ArrayList<MetaArea>();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<MetaArea> getAreas()
    {
        return areas;
    }

    public void setAreas(List<MetaArea> areas)
    {
        this.areas = areas;
    }
}
