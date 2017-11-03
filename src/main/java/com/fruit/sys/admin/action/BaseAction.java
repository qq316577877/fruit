package com.fruit.sys.admin.action;

import com.alibaba.fastjson.JSON;
import com.fruit.sys.admin.exception.ParamInvalidException;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.BizUtils;
import com.fruit.sys.admin.utils.JacksonMapper;
import com.fruit.sys.admin.utils.SystemConstant;
import com.ovfintech.arch.utils.UserAgentEnvUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import com.ovfintech.arch.utils.useragent.OperatingSystem;
import com.ovfintech.arch.utils.useragent.UserAgent;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseAction
{
    public static final String USER_AGENT = "User-Agent";

    public static final String SUCCESS = "success";

    public static final String FTL_ERROR_400 = "/error/400";

    public static final String FTL_ERROR_404 = "/error/404";

    public static final String FTL_ERROR_500 = "/error/500";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CoreService coreService;

    @Autowired
    private EnvService envService;

    private String domain;

    private String portalDomain;

    protected void log(int sysId, String content)
    {
        this.coreService.log(RemoteIpGetter.getRemoteAddr(WebContext.getRequest()), sysId, content);
    }

    protected UserInfo getLoginUserInfo()
    {
        return (UserInfo) WebContext.getRequest().getAttribute(SystemConstant.SESSION_USERINFO);
    }

    protected int getLoginUserId()
    {
        return getLoginUserId(WebContext.getRequest());
    }
    
    protected String getBodyString()
    {
        try
        {
            return IOUtils.toString(WebContext.getRequest().getInputStream());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("参数错误");
        }
    }

    public static int getLoginUserId(HttpServletRequest request)
    {
        int userId = 0;
        Object userIdObj  = request.getAttribute(BizConstants.ATTR_USER);

        if(userIdObj instanceof Integer)
        {
            userId = (Integer)userIdObj;
        }
        else if(userIdObj instanceof String)
        {
            userId = NumberUtils.toInt((String) userIdObj);
        }
        return userId;
    }

    protected String[] getArrayParameter(String name)
    {
        return WebContext.getRequest().getParameterValues(name);
    }

    protected String getStringParameter(String name)
    {
        return HtmlUtils.htmlEscape(WebContext.getRequest().getParameter(name));
    }

    protected String getStringParameter(String name, String def)
    {
        String param = getStringParameter(name);
        return StringUtils.isBlank(param) ? def : param.trim();
    }

    protected int getIntParameter(String name)
    {
        return this.getIntParameter(name, 0);
    }

    protected long getLongParameter(String name)
    {
        return this.getLongParameter(name, 0);
    }

    protected long getLongParameter(String name, long defaultValue)
    {
        return NumberUtils.toLong(WebContext.getRequest().getParameter(name), defaultValue);
    }

    protected int getIntParameter(String name, int defaultValue)
    {
        return NumberUtils.toInt(WebContext.getRequest().getParameter(name), defaultValue);
    }

    protected byte getByteParameter(String name)
    {
        return this.getByteParameter(name, (byte) 0);
    }

    protected byte getByteParameter(String name, byte defaultValue)
    {
        return (byte) NumberUtils.toInt(WebContext.getRequest().getParameter(name), defaultValue);
    }

    protected boolean getBooleanParameter(String name)
    {
        return BooleanUtils.toBoolean(WebContext.getRequest().getParameter(name));
    }

    protected String getRequiredStringParamter(String paramName, String msg)
    {
        String param = getStringParameter(paramName);
        if (StringUtils.isBlank(param))
        {
            throw new ParamInvalidException(msg);
        }
        return param;
    }

    protected int getRequiredIntParamter(String paramName, String msg)
    {
        return NumberUtils.toInt(getRequiredStringParamter(paramName, msg), 0);
    }

    protected int getRequiredIntParamter(String paramName)
    {
        return getRequiredIntParamter(paramName, paramName + " can't be null!");
    }

    protected String getRequiredStringParamter(String paramName)
    {
        return getRequiredStringParamter(paramName, paramName + " can't be null!");
    }

    protected void setAttribute(String paramName, Object o)
    {
        WebContext.getRequest().setAttribute(paramName, o);
    }

    protected UserInfo getUserInfo()
    {
        return (UserInfo) WebContext.getRequest().getAttribute(SystemConstant.SESSION_USERINFO);
    }

    protected Map<String, Object> fixParams()
    {
        HttpServletRequest request = WebContext.getRequest();
        //表单参数
        Map<String, Object> params = new HashMap<String, Object>();

        Map<String, String[]> map = request.getParameterMap();
        if (MapUtils.isNotEmpty(map))
        {
            for (Map.Entry<String, String[]> e : (map).entrySet())
            {
                if (e.getValue() == null)
                {
                    continue;
                }
                String value = "";
                if (e.getValue().length == 0)
                {
                }
                else if (e.getValue().length == 1)
                {
                    value = request.getParameter(e.getKey()); //get 乱码问题
                }
                else
                {
                    String _t = Arrays.deepToString(e.getValue());
                    value = _t.substring(1, _t.length() - 1);
                }
                params.put(e.getKey(), value);
            }
        }
        return params;
    }

    protected String getDomain()
    {
        if (domain == null)
        {
            domain = this.envService.getDomain();
        }
        return domain;
    }

    protected String getPortalDomain()
    {
        if (portalDomain == null)
        {
            portalDomain = this.envService.getPortalDomain();
        }
        return portalDomain;
    }

    protected UserInfo loadUserInfo()
    {
        return (UserInfo) WebContext.getRequest().getAttribute(BizConstants.ATTR_USER);
    }

    protected String getUserIp()
    {
        return RemoteIpGetter.getRemoteAddr(WebContext.getRequest());
    }

    protected String getPlatform()
    {
        try
        {
            String userAgentStr = BizUtils.abbreviate(WebContext.getRequest().getHeader(USER_AGENT), 250);
            UserAgent userAgent = UserAgentEnvUtils.parseUserAgent(userAgentStr);
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            if (null != operatingSystem)
            {
                return operatingSystem.getName();
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }

    public String i18n(String message, Object... args)
    {
        try
        {
            return this.messageSource.getMessage(message, args, WebContext.getRequest().getLocale());
        }
        catch (NoSuchMessageException e)
        {
            return message;
        }
    }

    protected void clearCookie(String cookieName)
    {
        HttpServletRequest request = WebContext.getRequest();
        HttpServletResponse response = WebContext.getResponse();
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0)
        {
            return;
        }
        for (Cookie cookie : cookies)
        {
            if (cookieName.equals(cookie.getName()))
            {
                Cookie newCookie = new Cookie(cookie.getName(), "");
                newCookie.setMaxAge(0);
                newCookie.setDomain(this.envService.getCookieDomain());
                newCookie.setPath("/");
                response.addCookie(newCookie);
            }
        }
    }

    protected Map<String, Object> getParamsValidationResults()
    {
        HttpServletRequest request = WebContext.getRequest();
        return (Map<String, Object>) request.getAttribute(BizConstants.ATRR_PARAMETER_RESULTS);
    }

    public static String toJson(Object object)
    {
        return JacksonMapper.toJson(object);
    }

    protected String getDefaultDate()
    {
        return BizUtils.formatDate(DateUtils.addDays(new Date(), -1));
    }

    protected String getDefaultStartDate()
    {
        return BizUtils.formatDate(DateUtils.addDays(new Date(), -7));
    }
    
    protected <T> T getBodyObject(Class<T> c)
    {
        try
        {
            String str = IOUtils.toString(WebContext.getRequest().getInputStream(),"UTF-8");
            return JSON.parseObject(str, c);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("参数错误");
        }
    }
}
