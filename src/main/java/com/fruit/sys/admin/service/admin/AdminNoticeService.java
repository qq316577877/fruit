/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.admin;

import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.biz.dto.SysNotifyDTO;
import com.fruit.sys.biz.service.SysNotifyService;
import com.ovfintech.arch.common.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : AdminNoticeService.java
 */
@Service
public class AdminNoticeService extends BaseService
{
    
    @Autowired
    private SysNotifyService sysNotifyService;

    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    public int getNoticeCount(int sysId)
    {
        return this.sysNotifyService.countUncheckedBySysId(sysId);
    }

    public List<SysNotifyDTO> listUnchecked(int sysId)
    {
        return this.sysNotifyService.listUncheckedBySysId(sysId);
    }

    public boolean sysCheck(int sysId, int id)
    {
        return this.sysNotifyService.updateChecked(sysId, id, true) > 0;
    }

    public boolean sysbatchCheck(int sysId, int type)
    {
        return this.sysNotifyService.batchUpdateChecked(sysId, type, true) > 0;
    }
}
