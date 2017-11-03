/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import com.fruit.sys.admin.config.EncryptPropertyPlaceholderConfigurer;
import com.ovfintech.arch.config.Environment;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Properties;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : EnvService.java
 */
@Service
public class EnvService implements InitializingBean
{
    private Properties properties;

    private static String env;
    
    public String getConfig(String key)
    {
        String property = this.properties.getProperty(key);
        return EncryptPropertyPlaceholderConfigurer.decryptProperty(property);
    }

    public String getDomain()
    {
        return this.getConfig("domain");
    }

    public String getPortalDomain()
    {
        return this.getConfig("domain.portal");
    }

    public String getCookieDomain()
    {
        return this.getConfig("cookie.domain");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.properties = new Properties();
        env = Environment.getEnv();
        String filePath = "config/properties/${env}.properties";
        filePath = filePath.replace("${env}", env);
        InputStream inputStream = EnvService.class.getClassLoader().getResourceAsStream(filePath);
        if(inputStream != null)
        {
            this.properties.load(inputStream);
            try
            {
                inputStream.close();
            }
            finally
            {

            }
        }
    }

    public static String getEnv()
    {
        return env;
    }
}
