/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.filter;

import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 功能描述: Web上下文Filter,实现request/response/session/servletContext(application) 的捕获 <p>
 *
 * @author : shang.gao <p>
 * @version 1.0 2013-4-9
 * @since open-web-mvc 1.0
 */
public class WebContextFilter implements Filter
{
    private static final Set<String> STATIC_FILES = new HashSet<String>();

    static
    {
        STATIC_FILES.add("ico");
        STATIC_FILES.add("css");
        STATIC_FILES.add("js");
        STATIC_FILES.add("jpg");
        STATIC_FILES.add("png");
        STATIC_FILES.add("gif");
        STATIC_FILES.add("jpeg");
        STATIC_FILES.add("bmp");
        STATIC_FILES.add("html");
        STATIC_FILES.add("htm");
        STATIC_FILES.add("mp3");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        if (request instanceof HttpServletRequest)
        {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String uri = httpRequest.getPathInfo();
            if(StringUtils.isNotBlank(uri) && isStaticFile(uri))
            {
//                chain.doFilter(request, response);
                return;
            }

            if (httpRequest.getSession() != null && httpRequest.getSession().getServletContext() != null)
            {
                WebContext.load(httpRequest, httpResponse, httpRequest.getSession().getServletContext());
            }
            else
            {
                WebContext.load(httpRequest, httpResponse, null);
            }
        }
        chain.doFilter(request, response);

    }

    public static boolean isStaticFile(String uri)
    {
        String extension = StringUtils.substringAfter(uri, ".");
        return StringUtils.isNotBlank(extension) && STATIC_FILES.contains(extension);
    }

    @Override
    public void destroy()
    {
    }

}
