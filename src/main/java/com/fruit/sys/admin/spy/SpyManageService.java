/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.spy;

import com.dianping.open.common.util.SecurityUtils;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.HttpUtil;
import com.fruit.sys.admin.utils.JacksonMapper;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : SpyManageService.java
 */
@Service
public class SpyManageService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(SpyManageService.class);

    @Autowired
    private EnvService envService;

    @Autowired(required = false)
    protected List<EventPublisher> eventPublishers;

    private List<String> spyServerIps;

    public void handleSpy(String name, String method) throws Exception
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("method", method);
//        params.put("timestamp", System.currentTimeMillis() + "");

        String sign = SecurityUtils.encryptPath(this.getAppkey(), this.getAppSecret(), params, true);

        params.put("sign", sign);

        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String query = queryString.substring(0, queryString.length() - 1);

        SpyResult spyResult = new SpyResult();
        for (String spyServerIp : this.getSpyServerIps())
        {
            String jsonResult = "";
            try
            {
                jsonResult = HttpUtil.doGet("http://" + spyServerIp + "/spy", query);
                spyResult = JacksonMapper.getMapper().readValue(jsonResult, SpyResult.class);

                if (!spyResult.isSuccessful())
                {
                    String msg = "fail to call: " + spyResult + ", result=" + jsonResult + ", query=" + query + ", clusterIp=" + spyServerIp + ", urlPath=/spy";
                    LOGGER.error(msg);
                    EventHelper.triggerEvent(this.eventPublishers, spyResult.toString(), msg, EventLevel.IMPORTANT, null, spyServerIp);
                    throw new Exception("spy request failed");
                }
            }
            catch (IOException e)
            {
                String msg = "fail to call: " + spyResult + ", response:" + jsonResult + ", query=" + query + ", urlPath=/spy";
                LOGGER.error(msg, e);
                EventHelper.triggerEvent(this.eventPublishers, spyResult.toString(), msg, EventLevel.IMPORTANT, e, spyServerIp);
                throw new Exception("spy request failed");
            }
        }
    }

    private String getAppkey()
    {
        return this.envService.getConfig("spy.appkey");
    }

    private String getAppSecret()
    {
        return this.envService.getConfig("spy.secret");
    }

    private List<String> getSpyServerIps()
    {
        if (this.spyServerIps == null)
        {
            String ipdsProperty = this.envService.getConfig("spy.ips");
            this.spyServerIps = Arrays.asList(ipdsProperty.split(","));
        }
        return spyServerIps;
    }

    private static class SpyResult implements Serializable {

        private String result;
        private String method;
        private String url;

        public String getResult()
        {
            return result;
        }

        public void setResult(String result)
        {
            this.result = result;
        }

        public String getMethod()
        {
            return method;
        }

        public void setMethod(String method)
        {
            this.method = method;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public boolean isSuccessful() {
            if (this.result == null) {
                return false;
            }
            return this.result.indexOf("Exception") == -1;
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder("SpyResult{");
            sb.append("result='").append(result).append('\'');
            sb.append(", method='").append(method).append('\'');
            sb.append(", url='").append(url).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
