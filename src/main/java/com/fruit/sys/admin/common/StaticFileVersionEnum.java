/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.common;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-07-06
 * Project        : sys-admin-web
 * File Name      : StaticFileVersionEnum.java
 */
public enum StaticFileVersionEnum
{

    /**
     *水果项目前台
     */
    PORTAL_MAIN_WEB(1, "portal-main-web"),

    /**
     *水果项目管理后台
     */
    SYS_AMDIN_WEB(2, "sys-amdin-web");

    private int code;

    private String message;

    StaticFileVersionEnum(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    public static StaticFileVersionEnum get(int code)
    {
        StaticFileVersionEnum[] values = StaticFileVersionEnum.values();
        for (StaticFileVersionEnum StaticFileVersionEnum : values)
        {
            if (StaticFileVersionEnum.getCode() == code)
            {
                return StaticFileVersionEnum;
            }
        }
        return null;
    }
}
