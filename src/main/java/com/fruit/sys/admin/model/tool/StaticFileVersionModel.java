/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.tool;

import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : StaticFileVersionModel.java
 */
public class StaticFileVersionModel
{
    private Integer id;

    private String project;

    private String version;

    private String description;

    private String lastEditor;

    private Date addTime;

    private Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getProject()
    {
        return project;
    }

    public void setProject(String project)
    {
        this.project = project;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLastEditor()
    {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor)
    {
        this.lastEditor = lastEditor;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
