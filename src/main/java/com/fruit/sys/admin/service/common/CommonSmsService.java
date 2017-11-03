/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.common;

import com.fruit.base.biz.dto.SmsLogDTO;
import com.fruit.base.biz.service.SmsLogService;
import com.fruit.sys.admin.event.ITask;
import com.fruit.sys.admin.event.TaskEvent;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.EnvService;
import com.ovfintech.arch.common.event.EventChannel;
import com.ovfintech.arch.message.common.sms.CommonSmsClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description:   消息服务
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : PortalMessageService.java
 */
@Service
public class CommonSmsService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonSmsService.class);

    @Autowired
    private EnvService envService;

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    public boolean sendMobileMessage(int userId, String mobile, String content, String smsServerKey)
    {
        int result = 0;
        if (!this.isProduct())
        {
            LOGGER.warn("[message service] disable sms && mail under non-product. user: {}, to: {}, content: {}", new Object[]{userId, mobile, content});
        }
        else
        {
            result = CommonSmsClient.sendMobileMessage(mobile, content, smsServerKey);
        }
        addSmsLog(userId, mobile, content, result);
        if (result != 0)
        {
            LOGGER.error("sms send failed for: " + userId + "," + mobile + "," + content);
        }
        return result == 0;
    }

    public void sendMobileMessageAsync(final int userId, final String mobile, final String content, final String smsServerKey)
    {
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                sendMobileMessage(userId, mobile, content, smsServerKey);
            }
        }));
    }

    private void addSmsLog(int userId, String mobile, String content, int result)
    {
        try
        {
            SmsLogDTO smsLogDTO = new SmsLogDTO();
            smsLogDTO.setUserId(userId);
            smsLogDTO.setMobile(mobile);
            smsLogDTO.setContent(StringUtils.abbreviate(content, 128));
            smsLogDTO.setResponseCode(result);
            smsLogDTO.setStatus(result == 0 ? 1 : 0);
            smsLogDTO.setAddTime(new Date());
            this.smsLogService.create(smsLogDTO);
        }
        catch (RuntimeException e)
        {
            LOGGER.error("[sendMail] failed to insert sms log: " + mobile + "-" + content, e);
        }
    }

    private boolean isProduct()
    {
        return EnvService.getEnv().equals("product");
    }

}
