/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.sys.admin.cache.CacheConstants;
import com.fruit.sys.admin.cache.CacheFetcher;
import com.fruit.sys.admin.cache.CacheKeyBuilder;
import com.fruit.sys.admin.model.BaseUserInfo;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class BaseInfoService
{
    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private UserAccountService userAccountService;

    private Cache userCache;


    private CacheFetcher<BaseUserInfo> userCacheFatcher = new CacheFetcher<BaseUserInfo>()
    {
        @Override
        protected BaseUserInfo loadFromService(Object... args)
        {
            return loadUserFromService(args);
        }
    };

    private CacheKeyBuilder singleKeyCacheKeyBuilder = new CacheKeyBuilder()
    {
        @Override
        public Object buildKey(Object... args)
        {
            return args[0];
        }
    };

    private BaseUserInfo loadUserFromService(Object[] args)
    {
        BaseUserInfo baseUserInfo = null;
        UserAccountDTO userAccountDTO = this.userAccountService.loadById((Integer) args[0]);
        if(userAccountDTO != null)
        {
            baseUserInfo = new BaseUserInfo();
            baseUserInfo.setUserId(userAccountDTO.getId());
            baseUserInfo.setMobile(userAccountDTO.getMobile());
            baseUserInfo.setType(userAccountDTO.getType());

            UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userAccountDTO.getId());
            baseUserInfo.setEnterpriseName(userEnterpriseDTO != null ? userEnterpriseDTO.getName() : "");
            if (userEnterpriseDTO != null)
            {
                baseUserInfo.setProvinceId(userEnterpriseDTO.getProvinceId());
                baseUserInfo.setEnterpriseName(userEnterpriseDTO.getName());
                baseUserInfo.setCityId(userEnterpriseDTO.getCityId());
                baseUserInfo.setEnterpriseAddTime(userEnterpriseDTO.getAddTime());
            }
            else
            {
                baseUserInfo.setEnterpriseName("");
            }
        }
        return baseUserInfo;
    }

    @Autowired
    public void init(EhCacheManagerFactoryBean ehCacheManagerFactoryBean)
    {
        CacheManager cacheManager = ehCacheManagerFactoryBean.getObject();
        this.userCache = cacheManager.getCache(CacheConstants.USER_CACHE_NAME);
    }

    public BaseUserInfo loadUserInfo(int userId)
    {
        return this.userCacheFatcher.get(this.userCache, "loadUserInfo", singleKeyCacheKeyBuilder, userId);
    }

    public String loadUserName(int userId)
    {
        BaseUserInfo baseUserInfo = this.loadUserInfo(userId);
        return baseUserInfo != null ? baseUserInfo.getEnterpriseName() : "";
    }

}
