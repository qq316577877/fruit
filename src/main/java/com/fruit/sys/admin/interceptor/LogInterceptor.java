/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : tst.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.interceptor;

/**
 * 功能描述:  <p>
 *
 * @author : terry <p>
 * @version 1.0 2015-06-19
 * @since beauty-admin-web 1.0
 */

import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.utils.NetworkUtil;
import com.fruit.sys.admin.utils.SystemConstant;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("logInterceptor")
public class LogInterceptor implements IDispatchInterceptor
{
    @Autowired
    private CoreService coreService;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        String url = uriMeta.getUri();
        String method = uriMeta.getMethod().getName();
        HttpServletRequest request = WebContext.getRequest();

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(SystemConstant.SESSION_USERINFO);
        int dper = 0;
        if (userInfo != null)
        {
            dper = userInfo.getSysId();
        }
        String ip = NetworkUtil.getIpAddress(request);
        String content = String.format("url:%s,method:%s", url, method);

        coreService.log(ip, dper, content);

        InterceptorResult interceptorResult = new InterceptorResult(true);
        return interceptorResult;
    }

}