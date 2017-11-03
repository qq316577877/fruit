package com.fruit.sys.admin.exception;

import com.dianping.open.common.exception.BusinessException;

public class ParamInvalidException extends BusinessException
{
    private String param;

    public ParamInvalidException(String message)
    {
        super(message, "param_invalid");
        this.param = "";
    }

    public ParamInvalidException(String message, String param)
    {
        super(message, "param_invalid");
        this.param = param;
    }

    public String getParam()
    {
        return param;
    }
}
