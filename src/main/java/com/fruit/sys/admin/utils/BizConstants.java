/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2014-10-13
 * Project        : promotion
 * File Name      : BizConstants.java
 */
public class BizConstants
{
    public static final String UTF_8 = "UTF-8";

    public static final String SLASH = "/";

    public static final String COOKIE_USER = "auser";

    public static final String ATTR_USER = "_auser";

    /**
     * 用户密码MD5加密使用的盐
     */
    public static final String USER_PASSWORD_MD5_SALT = ">_>@www.fruit.com@<_<";

    /**
     * 用户登录cookie过期时间(ms),默认24h
     */
    public static final int COOKIE_USER_TIMEOUT = 60 * 60 * 24;

    /**
     *
     */
    public static final int DEFAULT_PAGE_SIZE = 30;

    /**
     * 短信验证码过期时间(ms),默认30min
     */
    public static final int SMS_CAPTCHA_EXPIRATION_TIME = 60 * 1000 * 30;

    public static final String ATRR_PARAMETER_RESULTS = "_parameter_results";


    public static final String COOKIE_USER_PORTAL = "kuid";

    public static final String COOKIE_GUEST_PORTAL = "gid";

}
