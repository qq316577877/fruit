/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.sys.admin.cache.CacheCluster;
import com.fruit.sys.admin.cache.CacheSyncRequest;
import com.ovfintech.cache.client.CacheClient;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : CacheManageService.java
 */
@Service
public class CacheManageService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManageService.class);

    @Autowired
    private CacheCluster cacheCluster;
    
    @Autowired
    private CacheClient cacheClient;
    
    private static final String MEMBER_INFO_PREFIX = "MIF-";
    
    private static final String HOME_DATA_KEY = "__HOME-DATA";
    
    private static final String GOODS_INFO_KEY = "goods";

    public void syncUserModel(int userId)
    {
//        this.cacheCluster.syncUserModel(new CacheSyncRequest(userId));
    	if(cacheClient.exists(MEMBER_INFO_PREFIX + userId)){
    		cacheClient.del(MEMBER_INFO_PREFIX + userId);
    	}
    }

    public void syncSellerRule(int userId)
    {
        this.cacheCluster.syncSellerRule(new CacheSyncRequest(userId));
    }

    public void syncOrderContent(long orderId)
    {
        CacheSyncRequest request = new CacheSyncRequest();
        request.setOrderId(orderId);
        this.cacheCluster.syncOrderContent(request);
    }

    public void syncOrderDetail(long orderId, int status)
    {
        CacheSyncRequest request = new CacheSyncRequest();
        request.setOrderId(orderId);
        request.setStatus(status);
        this.cacheCluster.syncOrderDetail(request);
    }

    public void syncProduct()
    {
//        CacheSyncRequest request = new CacheSyncRequest();
//        request.setProductId(productId);
//        this.cacheCluster.syncProduct(request);
    	if(cacheClient.exists(GOODS_INFO_KEY)){
    		cacheClient.del(GOODS_INFO_KEY);
    	}
    }

    /*public void syncHomeData(String type)
    {
        CacheSyncRequest request = new CacheSyncRequest();
        request.setType(type);
        this.cacheCluster.syncHomeData(request);
    }*/
    
    public void syncHomeData()
    {
    	if(cacheClient.exists(HOME_DATA_KEY)){
    		cacheClient.del(HOME_DATA_KEY);
    	}
    }

    public void syncGlobalContext(int userId, String userToken)
    {
        CacheSyncRequest request = new CacheSyncRequest();
        request.setUserId(userId);
        request.setUserToken(userToken);
        this.cacheCluster.syncGlobalContext(request);
    }

    public void syncProductSeo(int type, int bizId)
    {
        CacheSyncRequest request = new CacheSyncRequest();
        request.setType(String.valueOf(type));
        request.setBizId(bizId);
        this.cacheCluster.syncProductSeo(request);
    }

}
