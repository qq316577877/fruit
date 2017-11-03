/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import com.fruit.base.file.upload.aliyun.client.FileUploadClient;
import com.fruit.base.file.upload.aliyun.client.UploadRequest;
import com.fruit.base.file.upload.aliyun.client.UploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UploadService.java
 */
@Service
public class UploadService
{
    @Autowired
    private FileUploadClient fileUploadClient;

    public UploadResponse upload(UploadRequest request)
    {
        return this.fileUploadClient.upload(request);
    }

    public String generateUrl(String key, boolean open)
    {
        return this.fileUploadClient.generateUrl(key, open);
    }

}
