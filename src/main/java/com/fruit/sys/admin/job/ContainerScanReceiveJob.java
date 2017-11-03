/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.order.ContainerScanReceive;
import com.fruit.sys.admin.utils.NetworkUtil;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-10
 * Project        : fruit
 * File Name      : LoanScanAvaliableJob.java
 */
public class ContainerScanReceiveJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerScanReceiveJob.class);

    @Autowired
    private ContainerScanReceive containerScanReceive;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.containerScanReceive.changeStatus();;
    }

    private boolean canRun()
    {
        String taskServer = this.envService.getConfig("task.server");
        String serverIp = NetworkUtil.getNetworkAddress();
        boolean match = taskServer.equals(serverIp);

        if(!match)
        {
            LOGGER.warn("[run] ignore with non-task server {}, task server should be {}", serverIp, taskServer);
        }
        return match;
    }

}
