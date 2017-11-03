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
 * File Name      : ItemExportResultVO.java
 */
public class FileExportResultVO implements Serializable
{
    private static final long serialVersionUID = -6729150108837072736L;

    private int record_id;

    private int status;

    private String file_url;

    private String error_msg;

    public int getRecord_id()
    {
        return record_id;
    }

    public void setRecord_id(int record_id)
    {
        this.record_id = record_id;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getFile_url()
    {
        return file_url;
    }

    public void setFile_url(String file_url)
    {
        this.file_url = file_url;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }
}
