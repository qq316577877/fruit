/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : partal-main-web
 * File Name      : BaseService.java
 */
@Service
public class BaseService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    private static String portalDomain;

    @Autowired
    protected EnvService envService;

    private static final List<Integer> USER_BACKLIST_IDS = new ArrayList<Integer>();

    @PostConstruct
    private void init()
    {
        loadConfigIds(USER_BACKLIST_IDS, "user.blacklist.ids");
    }

    private void loadConfigIds(List<Integer> idList, String configName)
    {
        idList.clear();
        String config = envService.getConfig(configName);
        if (StringUtils.isNotBlank(config))
        {
            String idStrs[] = config.split(",");
            if (idStrs.length > 0)
            {
                for (String idStr : idStrs)
                {
                    int id = NumberUtils.toInt(StringUtils.trim(idStr), 0);
                    if (id > 0)
                    {
                        idList.add(id);
                    }
                }
            }
        }
    }

    protected List<Integer> getUserBacklistIds()
    {
        return USER_BACKLIST_IDS;
    }

    protected synchronized String getPortalDomain()
    {
        if (portalDomain == null)
        {
            portalDomain = this.envService.getPortalDomain();
        }
        return portalDomain;
    }

}
