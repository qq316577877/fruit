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
public class MetaCountry implements Serializable
{
    private int id;

    private String name;

    private String enName;

    private List<MetaProvince> provinces = new ArrayList<MetaProvince>();

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

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    public List<MetaProvince> getProvinces()
    {
        return provinces;
    }

    public void setProvinces(List<MetaProvince> provinces)
    {
        this.provinces = provinces;
    }
}
