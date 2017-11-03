/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

import com.ovfintech.arch.web.mvc.utils.BigDecimalSerializer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:  <p>
 *
 * @author : xiaopeng.li <p>
 * @version 1.0 2012-11-14
 * @since open-platform-web 1.0
 */
public class JacksonMapper
{
    /**
     * 静态初始化
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static
    {
        SimpleModule module = new SimpleModule("BigDecimal", Version.unknownVersion());
        module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.registerModule(module);
    }

    /**
     * 功能描述：线程安全的方法，获取全局的Object mapper
     * <p/>
     * 前置条件：
     * <p/>
     * 方法影响：
     * <p/>
     * Author xiaopeng.li, 2012-11-14
     *
     * @return
     * @since open-platform-web 1.0
     */
    public static ObjectMapper getMapper()
    {
        return objectMapper;
    }

    /**
     * 功能描述：将对象转为JSON，一般用于简单的JSON序列化<p>
     * <p/>
     * 前置条件：<p>
     * <p/>
     * 方法影响： <p>
     * <p/>
     * Author xiaopeng.li, Sep 26, 2013
     *
     * @param obj
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @since open-platform-base 1.0
     */
    public static String toJson(Object obj)
    {
        try
        {
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            return "{}";
        }
    }

    /**
     * 功能描述：将JSON字符串转为Map，用于一般的JSON反序列化<p>
     * <p/>
     * 前置条件：<p>
     * <p/>
     * 方法影响： <p>
     * <p/>
     * Author xiaopeng.li, Sep 26, 2013
     *
     * @param json
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @since open-platform-base 1.0
     */
    public static Map<String, Object> toMap(String json)
    {
        try
        {
            return objectMapper.readValue(json, Map.class);
        }
        catch (Exception e)
        {
            return new HashMap<String, Object>(0);
        }
    }

    /**
     *  将Json转化为model
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz)
    {
        try
        {
            return objectMapper.readValue(json, clazz);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
