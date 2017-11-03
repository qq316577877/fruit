/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.interceptor;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.utils.BizConstants;
import com.ovfintech.arch.validation.ValidationFactory;
import com.ovfintech.arch.validation.exception.BusinessException;
import com.ovfintech.arch.validation.utils.RequestParameterUtils;
import com.ovfintech.arch.web.mvc.common.WebUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMeta;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.IDispatchInterceptor;
import com.ovfintech.arch.web.mvc.dispatch.interceptor.InterceptorResult;
import com.ovfintech.arch.web.mvc.exception.HttpAccessException;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 */
@Service("validationInterceptor")
public class ValidationInterceptor implements IDispatchInterceptor
{

    @Autowired
    private ValidationFactory validationFactory;

    @Override
    public InterceptorResult intercept(UriMeta uriMeta, InterceptorResult lastResult) throws HttpAccessException
    {
        HttpServletRequest request = WebContext.getRequest();
        try
        {
            Map<String, String> paramMap = RequestParameterUtils.buildParamMap(request);
            Map<String, Object> resultMap = validationFactory.validateParamMap(request.getPathInfo(), paramMap);
            request.setAttribute(BizConstants.ATRR_PARAMETER_RESULTS, resultMap);
        }
        catch (BusinessException e)
        {
            boolean asyncRequest = WebUtils.isAsyncRequest(request);
            if (asyncRequest)
            {
                JsonResult jsonResult = new JsonResult();
                jsonResult.setResult(false);
                jsonResult.setMsg("参数不正确：" + e.getMessage());
                return new InterceptorResult(false, jsonResult);
            }
            else
            {
                return new InterceptorResult(false, "error/400");
            }
        }
        return new InterceptorResult(true);
    }
}
