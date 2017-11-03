/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : RoleMenuDto.java
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
 * @version 1.0 2015-06-15
 * @since beauty-admin-web 1.0
 */
public class RoleMenuDto implements Serializable
{
    private Integer id;

    private Integer PId;

    private String name;

    private String checked;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getPId()
    {
        return PId;
    }

    public void setPId(Integer PId)
    {
        this.PId = PId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String checked)
    {
        this.checked = checked;
    }
}
