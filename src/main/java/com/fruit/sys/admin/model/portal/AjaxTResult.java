/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.portal;

import java.io.Serializable;

/**
 * Description: Ajax访问后台接口时统一的返回结果对象
 * <code>code</code>的取值请参考<code>AjaxResultCode</code>
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-15
 * Project        : fruit
 * File Name      : AjaxResult.java
 */
public class AjaxTResult<T> implements Serializable
{
    private static final long serialVersionUID = -3545836597920598732L;

    /**
     * 错误码
     */
    private int code = AjaxResultCode.OK.getCode();

    /**
     * 错误信息
     */
    private String msg = "success";

    /**
     * 返回数据
     */
    private T data = null;

    /**
     * 默认构造器(code=200， msg="success", data=null)
     */
    public AjaxTResult()
    {
    }

    /**
     * 正常返回数据(code=200， msg="success")
     *
     * @param data 需要返回的数据
     */
    public AjaxTResult(T data)
    {
        this.data = data;
    }

    /**
     * 返回错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public AjaxTResult(int code, String msg)
    {
        this(code, msg, null);
    }

    /**
     *
     * @param code
     * @param msg
     * @param data
     */
    public AjaxTResult(int code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode()
    {
        return code;
    }

    public AjaxTResult setCode(int code)
    {
        this.code = code;
        return this;
    }

    public String getMsg()
    {
        return msg;
    }

    public AjaxTResult setMsg(String msg)
    {
        this.msg = msg;
        return this;
    }

    public T getData()
    {
        return data;
    }

    public AjaxTResult setData(T data)
    {
        this.data = data;
        return this;
    }

}
