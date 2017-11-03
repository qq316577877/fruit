/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : FileUploadRequest.java
 */
public class FileUploadRequest
{
    private Map<String, String> formParameters = new HashMap<String, String>();

    private boolean multipart;

    private byte[] contents;

    private String fileName;

    private String localFilePath;

    private String aliyunFilePath;

    private List<String> urlPath;

    public boolean isMultipart()
    {
        return multipart;
    }

    public void setMultipart(boolean multipart)
    {
        this.multipart = multipart;
    }

    public Map<String, String> getFormParameters()
    {
        return formParameters;
    }

    public byte[] getContents()
    {
        return contents;
    }

    public void setContents(byte[] contents)
    {
        this.contents = contents;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getLocalFilePath()
    {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath)
    {
        this.localFilePath = localFilePath;
    }

    public String getAliyunFilePath()
    {
        return aliyunFilePath;
    }

    public void setAliyunFilePath(String aliyunFilePath)
    {
        this.aliyunFilePath = aliyunFilePath;
    }

    public List<String> getUrlPath()
    {
        return urlPath;
    }

    public void setUrlPath(List<String> urlPath)
    {
        this.urlPath = urlPath;
    }
}
