/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : process-parent
 * File Name      : AuthorizeFilter.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.filter;

import com.fruit.sys.admin.source.DictionarySource;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述:  <p>
 *
 * @author : terry <p>
 * @version 1.0 2015-03-11
 * @since process-parent 1.0
 */
public class SystemFilter implements Filter
{
    private FilterConfig config;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.config = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getSession().getAttribute("BASEPATH") == null)
        {
            request.getSession().setAttribute("BASEPATH", WebContext.getRequest().getServletPath());
            request.getSession().setAttribute("DicParse", new DictionarySource());
        }

        chain.doFilter(request, response);

    }

    public void destroy()
    {
        this.config = null;
    }
}
