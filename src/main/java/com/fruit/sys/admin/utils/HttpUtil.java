/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : HttpUtil.java
 */
public class HttpUtil
{
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String doGet(String url, String queryString)
    {
        StringBuffer response = new StringBuffer();
        HttpClientParams httpConnectionParams = new HttpClientParams();
        httpConnectionParams.setConnectionManagerTimeout(15000);
        httpConnectionParams.setSoTimeout(15000);
        HttpClient client = new HttpClient(httpConnectionParams);
        HttpMethod method = new GetMethod(url);
        try
        {
            if (StringUtils.isNotBlank(queryString))
            {
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }
            method.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            client.executeMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), BizConstants.UTF_8));
            String line;
            while ((line = reader.readLine()) != null)
            {
                response.append(line).append("\n");
            }
            reader.close();
        }
        catch (URIException e)
        {
            LOGGER.error("can not get from url: " + url + "?" + queryString, e);
        }
        catch (IOException e)
        {
            LOGGER.error("can not get from url: " + url + "?" + queryString, e);
        }
        finally
        {
            method.releaseConnection();
        }
        return response.toString();
    }
}
