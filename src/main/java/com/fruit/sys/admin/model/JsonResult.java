/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : QueryParamDTO.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonResult<T>
{
    private boolean result;

    private String msg;

    private Map<String, Object> datas;

    private T t;

    /**
     * 无数据的正确返回
     */
    public JsonResult()
    {
        this.result = true;
    }

    /**
     * 无数据的错误返回，带错误信息
     *
     * @param message
     */
    public JsonResult(String message)
    {
        this.result = false;
        this.msg = message;
    }

    /**
     * 有数据(Map)的正确返回
     *
     * @param datas
     */
    public JsonResult(Map<String, Object> datas)
    {
        this.result = true;
        this.datas = datas;
    }

    /**
     * 有数据(T)的正确返回
     *
     * @param t
     */
    public JsonResult(T t)
    {
        this.result = true;
        this.t = t;
    }


    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Map<String, Object> getDatas()
    {
        return datas;
    }

    public void setDatas(Map<String, Object> datas)
    {
        this.datas = datas;
    }

    public void addData(String key, Object data)
    {
        if (this.datas == null)
        {
            this.datas = new HashMap<String, Object>();
        }
        this.datas.put(key, data);
    }

    public T getT()
    {
        return t;
    }

    public void setT(T t)
    {
        this.t = t;
    }
}
