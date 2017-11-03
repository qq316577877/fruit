/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.job;

import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.statistics.StatisticsLoanableService;
import com.fruit.sys.admin.utils.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:  每日任务：统计昨日平台可贷余额 插入statistics_loanable_balance_days表
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-09-04
 * Project        : fruit
 * File Name      : StatisticsLoanableBalanceDaysJob.java
 */
public class StatisticsLoanableBalanceDaysJob
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsLoanableBalanceDaysJob.class);

    @Autowired
    private StatisticsLoanableService statisticsLoanableService;

    @Autowired
    private EnvService envService;

    public void runJob()
    {
        if(!this.canRun())
        {
            LOGGER.info("请在配置文件中配置task.server");
            return;
        }

        this.statisticsLoanableService.statisticsLoanableBalanceEveryDay();;
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
