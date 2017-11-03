/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.tool;

import com.ovfintech.cache.client.CacheClient;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : StaticFileVersionManageService.java
 */
@Service
public class StaticFileVersionRefreshService
{
    private static final String STATIC_VERSION_PREFIX = "fruit.static.file.version";

    @Resource
    private CacheClient cacheClient;

    public void refreshVersion() throws Exception{
        this.cacheClient.del(STATIC_VERSION_PREFIX);
    }


}
