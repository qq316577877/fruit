package com.fruit.sys.admin.service.common;

import com.fruit.base.biz.dto.RuntimeConfigDTO;
import com.fruit.base.biz.service.RuntimeConfigService;
import com.fruit.sys.admin.service.EnvService;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2016-06-01
 * Project        : points-biz-api
 * File Name      : RuntimeConfigService.java
 */
@Service
public class RuntimeConfigurationService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeConfigurationService.class);

    public static final String RUNTIME_CONFIG_PROJECT_PORTAL = "_www.fruit.com.config";

    public static final String RUNTIME_CONFIG_PROJECT_LOAN = "loan-biz";

    @Resource
    protected CacheClient cacheClient;

    @Autowired
    private RuntimeConfigService runtimeConfigService;

    @Autowired
    private EnvService envService;

    public String getConfig(String key)
    {
        String cachekey = RUNTIME_CONFIG_PROJECT_PORTAL + "-" +  key;
        String result = this.cacheClient.get(cachekey);
        if(result == null){
            RuntimeConfigDTO config = this.runtimeConfigService.loadByName(RUNTIME_CONFIG_PROJECT_PORTAL, key);
            Validate.notNull(config, "加载系统配置失败");
            result = this.getCurrentConfig(config);
            LOGGER.info("[load runtime config] project-key: {}, value：{}" , cachekey, result);
            this.cacheClient.set(cachekey, result);
        }
        return result;
    }

    public void removeConfigCache(String key)
    {
        this.cacheClient.del(RUNTIME_CONFIG_PROJECT_PORTAL + "-" +  key);
    }

    private String getCurrentConfig(RuntimeConfigDTO config)
    {
        String value;
        if ("product".equals(this.envService.getEnv()))
        {
            value = config.getProduct();
        }
        else if ("beta".equals(this.envService.getEnv()))
        {
            value = config.getBeta();
        }
        else if ("dev".equals(this.envService.getEnv()))
        {
            value = config.getDev();
        }
        else // if ("alpha".equals(this.envService.getEnv()))
        {
            value = config.getAlpha();
        }
        return value;
    }


    /**
     * 通过项目名+key传参
     * @param projectString
     * @param key
     * @return
     */
    public String getConfig(String projectString,String key)
    {
        String cachekey = projectString + "-" +  key;
        String result = this.cacheClient.get(cachekey);
        if(result == null){
            RuntimeConfigDTO config = this.runtimeConfigService.loadByName(projectString, key);
            Validate.notNull(config, "加载系统配置失败");
            result = this.getCurrentConfig(config);
            LOGGER.info("[load runtime config] project-key: {}, value：{}" , cachekey, result);
            this.cacheClient.set(cachekey, result);
        }
        return result;
    }

}
