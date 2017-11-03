/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.job;

import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.neworder.ContainerNewScanReceive;
import com.fruit.sys.admin.service.order.ContainerScanReceive;
import com.fruit.sys.admin.utils.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * <p/>
 * Create Author  : ivan
 * Create Date    : 2017-09-29
 * Project        : fruit
 * File Name      : ContainerNewScanReceiveJob.java
 */
public class ContainerNewScanReceiveJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerNewScanReceiveJob.class);

    @Autowired
    private ContainerNewScanReceive containerNewScanReceive;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.containerNewScanReceive.changeStatus();;
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
