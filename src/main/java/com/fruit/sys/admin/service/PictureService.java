/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import com.fruit.base.file.upload.aliyun.client.FileUploadClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureService
{
    private static final String OPEN_PICTURE_DOMAIN = "//picture.0ku.com/";

    @Autowired
    private FileUploadClient fileUploadClient;

    public String buildPictureUrl(String key, boolean open)
    {
        String url = "";
        if(StringUtils.isNotBlank(key))
        {
            if(key.startsWith("//") || key.startsWith("http://") || key.startsWith("https://"))
            {
                url = key;
            }
            else
            {
                if(open)
                {
                    url = OPEN_PICTURE_DOMAIN + key;
                }
                else
                {
                    url = this.fileUploadClient.generateUrl(key, open);
                    url = url.replace("0ku-private-pictures.oss.aliyuncs.com", "private.0ku.com");
                }
            }

        }
        return url;
    }

}
