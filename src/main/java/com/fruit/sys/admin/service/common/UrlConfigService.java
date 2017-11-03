/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.sys.admin.service.EnvService;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UrlConfigService.java
 */
@Service
public class UrlConfigService
{
    @Autowired
    private EnvService envService;

    public String getHomeUrl()
    {
        return this.envService.getDomain();
    }

    public String getOrderDetailUrl(String orderId)
    {
        return this.envService.getDomain() + "/order/detail?id=" + orderId;
    }
}
