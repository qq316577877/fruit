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

import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.utils.*;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.EnvService;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 功能描述:  <p>
 *
 * @author : terry <p>
 * @version 1.0 2015-03-11
 * @since process-parent 1.0
 */
public class AuthorizeFilter implements Filter
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeFilter.class);

    private static final List<String> IGNORE_PATH = Arrays.asList("index;noPermission;main;notice/center/inquire;logout".split(";"));

    private FilterConfig config;

    private CoreService coreService;

    private AdminService adminService;

    private EnvService envService;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.config = filterConfig;
        this.coreService = SpringBeanUtils.getBean(CoreService.class);
        this.envService = SpringBeanUtils.getBean(EnvService.class);
        this.adminService = SpringBeanUtils.getBean(AdminService.class);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //解决app跨域问题
        response.setContentType("text/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.setHeader("Cache-Control", "no-cache");

        int sysId = getSysId(request);
        this.doAuthorize(request, response, sysId);
        chain.doFilter(request, response);
    }

    private void doAuthorize(HttpServletRequest request, HttpServletResponse response, int sysId) throws IOException
    {
        String requestURI = request.getRequestURI();
        String noLoginUrl = this.config.getInitParameter("noLoginUrl");
        boolean noNeedLogin = (requestURI != null && requestURI.startsWith(noLoginUrl)) || (WebContextFilter.isStaticFile(requestURI));

        if(!noNeedLogin){//老司机的请求，不要求登录态   统计需求也不要求登录态
            String driversUrl = this.config.getInitParameter("driversUrl");
            String statisticsUrl = this.config.getInitParameter("statisticsUrl");
            if(requestURI != null &&
                    (requestURI.startsWith(driversUrl) ||  requestURI.startsWith(statisticsUrl))
                    ){
                noNeedLogin = true;
            }
        }

        if (!noNeedLogin)
        {
            if (sysId > 0)
            {
                String adminMenus = "";
                String userMenus = "";
                Set<String> userNamespaceSet = null;
                UserInfo userInfo = this.adminService.loadUserInfo(sysId);
                if (userInfo != null)
                {
                    request.setAttribute(BizConstants.ATTR_USER, userInfo);
                    request.setAttribute(SystemConstant.SESSION_USERINFO, userInfo);

                    adminMenus = coreService.adminMenus();
                    userMenus = coreService.userMenus(userInfo.getSysId());
                    userNamespaceSet = coreService.userNamespace(userInfo.getSysId());
                    request.setAttribute(SystemConstant.SESSION_ADMIN_MENUS, adminMenus);
                    request.setAttribute(SystemConstant.SESSION_USER_MENUS, userMenus);
                    request.setAttribute("logoutUrl", UrlUtils.getLogoutUrl(this.envService.getDomain()));

                    if (!userInfo.isAdmin())
                    {
                        String basePath = request.getServletPath() + "/";
                        String requestPath = request.getRequestURI().replaceAll(basePath, "");
                        requestPath = requestPath.split(";")[0];

                        if (IGNORE_PATH.contains(requestPath))      // 名单内直接放行
                        {
                            return;
                        }

                        if (adminMenus.indexOf(requestPath) != -1)      //链接属于系统菜单
                        {
                            if (userMenus.indexOf(requestPath) == -1)       // 用户没有菜单权限
                            {
                                response.sendRedirect(basePath + "noPermission");
                            }
                        }

                        else        // 非菜单链接，可能为AJAX
                        {

                            // TODO: 2017/7/15   非菜单的暂时不做判断 
//                            boolean illegal = true;
//                            for (String namespace : userNamespaceSet)
//                            {
//                                if (requestPath.startsWith(namespace))
//                                {
//                                    illegal = false;
//                                    break;
//                                }
//                            }
//
//                            if (illegal)
//                            {
//                                response.sendRedirect(basePath + "noPermission");
//                            }
                        }
                    }
                }
            }
            else
            {
                response.sendRedirect("http://" + UrlUtils.getLoginUrlWithRedir(this.envService.getDomain(), request));
            }
        }
    }

    /**
     * 处理用户登录相关的cookie
     *
     * @param httpServletRequest
     */
    private int getSysId(HttpServletRequest httpServletRequest)
    {
        int sysId = 0;
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie userCookie = null; // 正式登陆用户cookie
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (BizConstants.COOKIE_USER.equals(cookie.getName())) //已登录用户
                {
                    userCookie = cookie;
                    break;
                }
            }
        }

        if (userCookie != null)
        {
            String value = userCookie.getValue();
            String idValue = CookieUtil.getIdFromPassport(value);
            httpServletRequest.setAttribute(BizConstants.COOKIE_USER, value);
            sysId = NumberUtils.toInt(idValue);
            httpServletRequest.setAttribute(BizConstants.ATTR_USER, sysId);
        }
        return sysId;
    }

    public void destroy()
    {
        this.config = null;
    }

}
