/*
 *
 * Copyright (c) 2010-2015 by Shanghai HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.common;

import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.message.biz.utils.RedBankClient;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.EnvService;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2016-04-01
 * Project        : points-biz-api
 * File Name      : RedBankService.java
 */
@Service
public class RedBankService extends BaseService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(RedBankService.class);

    private static final String KEY_MC_SERVER_IP = "mc.server.ip";

    private static final String KEY_PRODUCER_ID = "mc.producer.id";

    private static final String KEY_PRODUCER_PASSWORD = "mc.producer.password";

    @Autowired
    private EnvService envService;

    private RedBankClient redBankClient;

    @Autowired(required = false)
    private List<EventPublisher> eventPublishers;

    @PostConstruct
    protected void init()
    {
        if (null == redBankClient && null != envService)
        {
            String serverIp = envService.getConfig(KEY_MC_SERVER_IP);
            String producerIdStr = envService.getConfig(KEY_PRODUCER_ID);
            String password = envService.getConfig(KEY_PRODUCER_PASSWORD);
            Validate.notNull(producerIdStr, "初始化MC账号信息失败");
            Validate.notNull(password, "初始化MC账号信息失败");
            Validate.notNull(serverIp, "初始化MC账号信息失败");
            int producerId = NumberUtils.toInt(producerIdStr, 0);
            Validate.isTrue(producerId > 0, "初始化MC账号信息失败");
            redBankClient = new RedBankClient(serverIp, producerId, password, false);
        }
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param eventKey
     * @param operator
     */
    public void syncFireEvent(String bizId, String eventKey, String operator)
    {
        Validate.notEmpty(eventKey, "参数错误");
        try
        {
            redBankClient.fire(bizId, eventKey, null, operator);
        }
        catch (Exception e)
        {
            LOGGER.error("Fire message error.", e);
            EventHelper.triggerEvent(RedBankService.this.eventPublishers, "RedBankService." + e.getMessage(),
                    "error when send message with server : " + redBankClient.getServerUrl(), EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param type
     * @param operator
     */
    public void syncFireEvent(String bizId, MessageTypeEnum type, String operator)
    {
        Validate.notNull(type, "参数错误");
        try
        {
            redBankClient.fire(bizId, type.getKey(), null, operator);
        }
        catch (Exception e)
        {
            LOGGER.error("Fire message error.", e);
            EventHelper.triggerEvent(RedBankService.this.eventPublishers, "RedBankService." + e.getMessage(),
                    "error when send message with server : " + redBankClient.getServerUrl(), EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param type
     * @param operator
     */
    public void syncFireEvent(long bizId, MessageTypeEnum type, String operator)
    {
        this.syncFireEvent(bizId, type, null, operator);
    }

    /**
     * 异步发送消息
     *
     * @param bizId
     * @param type
     * @param extraData
     * @param operator
     */
    public void syncFireEvent(long bizId, MessageTypeEnum type, Map<String, String> extraData, String operator)
    {
        Validate.notNull(type, "参数错误");
        try
        {
            redBankClient.fire(Long.toString(bizId), type.getKey(), extraData, operator);
        }
        catch (Exception e)
        {
            LOGGER.error("Fire message error.", e);
            EventHelper.triggerEvent(RedBankService.this.eventPublishers, "RedBankService." + e.getMessage(),
                    "error when send message with server : " + redBankClient.getServerUrl(), EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }
    }

}
