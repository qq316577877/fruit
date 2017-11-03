/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.config;

import com.fruit.sys.admin.utils.AESHelper;
import com.ovfintech.arch.config.Environment;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{

    private static Pattern encryptedPattern = Pattern.compile("Encrypted:\\{((\\w|\\-)*)\\}");  //加密属性特征正则

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Set<String> encryptedProps = Collections.emptySet();

    public void setEncryptedProps(Set<String> encryptedProps)
    {
        this.encryptedProps = encryptedProps;
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue)
    {
        if (encryptedProps.contains(propertyName))
        { //如果在加密属性名单中发现该属性
            propertyValue = decryptProperty(propertyValue);
            if (propertyValue == null)
            {//说明解密失败
                logger.error("Decrypt " + propertyName + "=" + propertyValue + " error!");
            }
        }
        return super.convertProperty(propertyName, propertyValue);  //将处理过的值传给父类继续处理
    }

    public static String decryptProperty(String propertyValue)
    {
        if(StringUtils.isNotBlank(propertyValue))
        {
            final Matcher matcher = encryptedPattern.matcher(propertyValue);  //判断该属性是否已经加密
            if (matcher.matches())
            {      //已经加密，进行解密
                String encryptedString = matcher.group(1);    //获得加密值
                String decryptedPropValue = AESHelper.decrypt(encryptedString);  //调用AES进行解密，SEC_KEY与属性名联合做密钥更安全
                if (decryptedPropValue != null)
                {  //!=null说明正常
                    propertyValue = decryptedPropValue; //设置解决后的值
                }
            }
        }
        return propertyValue;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        super.postProcessBeanFactory(beanFactory);    //正常执行属性文件加载
    }

    /**
     * Return a merged Properties instance containing both the
     * loaded properties and properties set on this FactoryBean.
     */
    protected Properties mergeProperties() throws IOException
    {
        Properties result = new Properties();
        if (this.localOverride)
        {
            // Load properties from file upfront, to let local properties override.
            loadProperties(result);
        }
        if (this.localProperties != null)
        {
            for (Properties localProp : this.localProperties)
            {
                CollectionUtils.mergePropertiesIntoMap(localProp, result);
            }
        }
        if (!this.localOverride)
        {
            // Load properties from file afterwards, to let those properties override.
            loadProperties(result);
        }
        return result;
    }

    protected Resource[] locations;

    @Override
    public void setLocations(Resource[] locations)
    {   //由于location是父类私有，所以需要记录到本类的locations中
        String env = Environment.getEnv();

        List<Resource> filteredResources = new ArrayList<Resource>();

        for (Resource resource : locations)
        {
            String name = resource.getFilename();
            if (name.contains(env))
            {
                logger.warn("[setLocations] match resource : {}", resource.getFilename());
                filteredResources.add(resource);
            }
            else
            {
                try
                {
                    resource.getFile().delete();
                }
                catch (IOException e)
                {
                    logger.error("[setLocations] can not delete others prop : {}", resource.getFilename());
                }
            }

        }
        Resource[] objects = filteredResources.toArray(new Resource[0]);
        super.setLocations(objects);
        this.locations = objects;
    }

    @Override
    public void setLocation(Resource location)
    {   //由于location是父类私有，所以需要记录到本类的locations中
        super.setLocation(location);
        this.locations = new Resource[]{location};
    }
}