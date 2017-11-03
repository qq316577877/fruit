/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.CookieUtil;
import com.fruit.sys.admin.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
@UriMapping("/")
public class LoginAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private EnvService envService;

    @UriMapping(value = "/login")
    public String show()
    {
        String redir = StringUtils.trimToEmpty(super.getStringParameter("redir"));
        if (StringUtils.isBlank(redir))
        {
            redir = "/admin/index";
        }
        int sysId = super.getLoginUserId();
        if (sysId > 0)
        {
            UserInfo userInfo = this.adminService.loadUserInfo(sysId);
            if (null != userInfo) // 已登录用户直接进主页
            {
                return "redirect:" + redir;
            }
            else // 非法用户强制登出
            {
                this.logout();
            }
        }
        WebContext.getRequest().setAttribute("login_ajax_url", UrlUtils.getLoginAjaxUrl(super.getDomain()));
        WebContext.getRequest().setAttribute("redir_url", redir);
        return "login/login";
    }

    @UriMapping(value = "/login_ajax")
    public JsonResult loginAjax()
    {
        String username = StringUtils.trimToEmpty(super.getStringParameter("username"));
        String password = StringUtils.trimToEmpty(super.getStringParameter("password"));
        int auto = super.getIntParameter("auto", 0);
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(username), "用户名不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(password), "密码不能为空!");
            UserInfo userInfo = this.adminService.adminUserLogin(username, password);
            this.addUserCookie(auto, userInfo);
            return new JsonResult();
        }
        catch (IllegalArgumentException e)
        {
            super.log(0, username + "," + password + " 登录失败:" + e.getMessage());
            return new JsonResult(e.getMessage());
        }
    }

    protected void addUserCookie(int auto, UserInfo userInfo)
    {
        Cookie cookie = new Cookie(BizConstants.COOKIE_USER, CookieUtil.generatePassportByUserInfo(userInfo));
        cookie.setDomain(this.envService.getCookieDomain());
        cookie.setPath("/");
        cookie.setMaxAge(auto == 1 ? BizConstants.COOKIE_USER_TIMEOUT : -1);
        WebContext.getResponse().addCookie(cookie);
    }

    @UriMapping(value = "/logout")
    public String logout()
    {
        clearCookie(BizConstants.COOKIE_USER);
        return "redirect:" + UrlUtils.getLoginUrl(super.getDomain());
    }

}


