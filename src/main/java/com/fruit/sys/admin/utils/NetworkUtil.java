/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : a.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.utils;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 常用获取客户端信息的工具
 */
public final class NetworkUtil
{

	static private final char COLON = ':';

	/**
	 * 
	 * 获取当前机器ip地址
	 * 
	 * 据说多网卡的时候会有问题.
	 */

	public static String getNetworkAddress() {
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(COLON) == -1) {
						return ip.getHostAddress();
					}
				}
			}
			return "127.0.0.1";
		} catch (Exception e) {
			return "127.0.0.1";
		}

	}
	
    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request)
    {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("Proxy-Client-IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("WL-Proxy-Client-IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("HTTP_CLIENT_IP");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");

            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            {
                ip = request.getRemoteAddr();

            }
        }
        else if (ip.length() > 15)
        {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++)
            {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp)))
                {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
