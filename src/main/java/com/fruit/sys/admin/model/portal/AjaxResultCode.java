/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.portal;

/**
 * Description:
 * <pre>
 *     200 - OK
 *     1XX - 用户类型错误
 *     4XX - 请求错误
 *     500 - 服务器错误
 * </pre>
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : AjaxResultCode.java
 */
public enum AjaxResultCode
{
    OK(200),

    /**
     * 未登录
     */
    USER_UNLOGIN(100),

    /**
     * 用户被禁用
     */
    USER_FORBIDDEN(101),

    /**
     * 用户未认证
     */
    USER_NO_ENTERPRISE(102),

    /**
     * 账户异常
     */
    USER_ACCOUNT_RISK(103),

    /**
     * 用户未获取品牌授权
     */
    USER_NO_AUTHED(103),

    /**
     * 非法请求
     */
    REQUEST_INVALID(400),

    /**
     * token 安全验证失败
     */
    REQUEST_TOKEN_ERROR(401),

    /**
     * 缺少必要参数
     */
    REQUEST_MISSING_PARAM(402),

    /**
     * 缺少必要参数
     */
    REQUEST_ERROR_METHOD(403),

    /**
     * 请求URL不存在
     */
    REQUEST_BAD_URL(404),

    /**
     * 请求参数验证不通过
     */
    REQUEST_BAD_PARAM(405),

    /**
     * 内部错误
     */
    SERVER_ERROR(500),;

    private int code;

    AjaxResultCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
