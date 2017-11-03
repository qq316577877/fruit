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
 * File Name      : MetaProvince.java
 */
public class MetaProvince implements Serializable
{
    private int id;

    private String name;

    private List<MetaCity> cities = new ArrayList<MetaCity>();

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

    public List<MetaCity> getCities()
    {
        return cities;
    }

    public void setCities(List<MetaCity> cities)
    {
        this.cities = cities;
    }
}
