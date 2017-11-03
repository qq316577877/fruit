/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fruit.sys.admin.model.ContextObject;
import com.ovfintech.arch.utils.UserAgentEnvUtils;
import com.ovfintech.arch.utils.ip.RemoteIpGetter;
import com.ovfintech.arch.utils.useragent.Browser;
import com.ovfintech.arch.utils.useragent.OperatingSystem;
import com.ovfintech.arch.utils.useragent.UserAgent;

/**
 * Description:
 * <p/>
 * Create Author : terry Create Date : 2017-05-20 Project : fruit File Name :
 * ContextManger.java
 */
public final class ContextManger {

	public static final String USER_AGENT = "User-Agent";

	private final static ThreadLocal<ContextObject> context = new ThreadLocal<ContextObject>();

	private static ContextObject init() {
		ContextObject map = context.get();
		if (map == null) {
			ContextObject value = new ContextObject();
			context.set(value);
		}
		return context.get();
	}

	public static ContextObject getContext() {
		ContextObject init = init();
		return init;
	}

	public static void load(HttpServletRequest request, HttpServletResponse response) {
		ContextObject currentContext = init();
		currentContext.clear();
		currentContext.setUserAgent(request.getHeader(USER_AGENT));
		UserAgent userAgent = parseUserAgent(currentContext.getUserAgent());
		currentContext.setUserIp(RemoteIpGetter.getRemoteAddr(request));
		currentContext.setUserAgentModel(userAgent);
	}

	protected static UserAgent parseUserAgent(String userAgent) {
		try {
			return UserAgentEnvUtils.parseUserAgent(userAgent);
		} catch (RuntimeException e) {
			return new UserAgent(OperatingSystem.UNKNOWN, Browser.UNKNOWN);
		}
	}

}
