/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.cache;

import com.dianping.open.common.util.SecurityUtils;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.HttpUtil;
import com.fruit.sys.admin.utils.JacksonMapper;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : CacheCluster.java
 */
@Service
public class CacheCluster
{
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheCluster.class);

    @Autowired
    private EnvService envService;

    private List<String> clusterServerIps;

    private String serverIp = ServerIpUtils.getServerIp();

    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    private List<String> getClusterServerIps()
    {
        if (this.clusterServerIps == null)
        {
            String ipdsProperty = this.envService.getConfig("cache.cluster.ips");
            this.clusterServerIps = Arrays.asList(ipdsProperty.split(","));
        }
        return clusterServerIps;
    }

    public void syncUserModel(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/user/remove_ajax");
    }

    public void syncSellerRule(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/seller/remove_rule_ajax");
    }

    public void syncOrderContent(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/order/remove_content_ajax");
    }

    public void syncOrderDetail(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/order/remove_detail_ajax");
    }

    public void syncProduct(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/product/remove_ajax");
    }

    public void syncHomeData(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/home/remove_ajax");
    }

    public void syncGlobalContext(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/global/remove_context_ajax");
    }

    public void syncProductSeo(CacheSyncRequest request)
    {
        String query = this.buildQuery(request);
        this.call(request, query, "/cache/product/seo/remove_ajax");
    }

    protected void call(final CacheSyncRequest request, final String query, final String urlPath)
    {
        for (String clusterIp : this.getClusterServerIps())
        {
            if (clusterIp.equals(this.serverIp))
            {
                LOGGER.warn("[call] ignore with local: {}", request);
                continue;
            }
            String jsonResult = "";
            try
            {
                jsonResult = HttpUtil.doGet("http://" + clusterIp + urlPath, query);
                AjaxResult ajaxResult = JacksonMapper.getMapper().readValue(jsonResult, AjaxResult.class);
                int code = ajaxResult.getCode();
                boolean successful = code == AjaxResultCode.OK.getCode();
                if (!successful)
                {
                    String msg = "fail to call: " + request + ", result=" + jsonResult + ", query=" + query + ", clusterIp=" + clusterIp + ", urlPath=" + urlPath;
                    LOGGER.error(msg);
                    EventHelper.triggerEvent(this.eventPublishers, request.toString(), msg, EventLevel.IMPORTANT, null, this.serverIp);
                }
            }
            catch (IOException e)
            {
                String msg = "fail to call: " + request + ", response:" + jsonResult + ", query=" + query + ", urlPath=" + urlPath;
                LOGGER.error(msg, e);
                EventHelper.triggerEvent(this.eventPublishers, request.toString(), msg, EventLevel.IMPORTANT, e, this.serverIp);
            }
        }

    }

    protected String buildQuery(CacheSyncRequest request)
    {
        Map<String, String> params = new HashMap<String, String>();
        if (request.getUserId() > 0)
        {
            params.put("user_id", String.valueOf(request.getUserId()));
        }
        if (request.getOrderId() > 0)
        {
            params.put("order_id", String.valueOf(request.getOrderId()));
        }
        if (request.getStatus() > 0)
        {
            params.put("status", String.valueOf(request.getStatus()));
        }
        if (StringUtils.isNotBlank(request.getUserToken()))
        {
            params.put("user_token", request.getUserToken());
        }
        if (StringUtils.isNotBlank(request.getType()))
        {
            params.put("type", request.getType());
        }
        if (request.getProductId() > 0)
        {
            params.put("product_id", String.valueOf(request.getProductId()));
        }
        if (request.getBizId() > 0)
        {
            params.put("biz_id", String.valueOf(request.getBizId()));
        }
        String sign = SecurityUtils.encryptMD5(this.getAppkey(), this.getAppSecret(), params);
        params.put("sign", sign);
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return queryString.substring(0, queryString.length() - 1);
    }

    public String getAppkey()
    {
        return this.envService.getConfig("cache.cluster.appkey");
    }

    public String getAppSecret()
    {
        return this.envService.getConfig("cache.cluster.secret");
    }
}
