/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model.portal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Ajax访问后台接口时统一的返回结果对象
 * <code>code</code>的取值请参考<code>AjaxResultCode</code>
 * <p/>
 * Create Author  : terry
 */
public class AjaxResult implements Serializable
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
     * 请求验证token
     */
    private String token;

    /**
     * 返回数据
     */
    private Object data = null;

    /**
     * 默认构造器(code=200， msg="success", data={"successful"=1})
     */
    public AjaxResult()
    {
        Map<String, Integer> defaultData = new HashMap<String, Integer>();
        defaultData.put("successful", 1);
        data = defaultData;
    }

    /**
     * 正常返回数据(code=200， msg="success")
     *
     * @param data 需要返回的数据
     */
    public AjaxResult(Object data)
    {
        this.data = data;
    }

    /**
     * 返回错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public AjaxResult(int code, String msg)
    {
        this(code, msg, null);
    }

    /**
     * @param code
     * @param msg
     * @param data
     */
    public AjaxResult(int code, String msg, Object data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode()
    {
        return code;
    }

    public AjaxResult setCode(int code)
    {
        this.code = code;
        return this;
    }

    public String getMsg()
    {
        return msg;
    }

    public AjaxResult setMsg(String msg)
    {
        this.msg = msg;
        return this;
    }

    public Object getData()
    {
        return data;
    }

    public AjaxResult setData(Object data)
    {
        this.data = data;
        return this;
    }

    public String getToken()
    {
        return token;
    }

    public AjaxResult setToken(String token)
    {
        this.token = token;
        return this;
    }
}
