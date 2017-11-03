/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.listener;/*
 * Create Author  : chao.ji
 * Create  Time   : 15/4/9 下午2:21
 * Project        : promotion
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

import com.fruit.sys.admin.meta.MetadataService;
import com.ovfintech.arch.utils.ServerIpUtils;
import com.ovfintech.arch.validation.ValidationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartListener implements ServletContextListener
{
    private static final Logger LOG = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        LOG.warn("[StartListener] begin to contextInitialized .........");
        ServletContext servletContext = sce.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        webApplicationContext.getBean(ValidationFactory.class).loadValidators();
        webApplicationContext.getBean(MetadataService.class).loadMetadata();

        LOG.warn("[StartListener] begin to detech server ip.........");

        ServerIpUtils.getServerIp();

        LOG.warn("////////////////////////////////////////////////////////////////////");
        LOG.warn("//							_ooOoo_								  //");
        LOG.warn("//						   o8888888o							  //");
        LOG.warn("//						   88\" . \"88							  //");
        LOG.warn("//						   (| -_- |)							  //");
        LOG.warn("//						   O\\  =  /O							  //");
        LOG.warn("//						____/`---'\\____							  //");
        LOG.warn("//					  .'  \\|     |//  `.						  //");
        LOG.warn("//					 /  \\\\|||  :  |||//  \\						  //");
        LOG.warn("//				    /  _||||| -:- |||||-  \\						  //");
        LOG.warn("//				    |   | \\\\\\  -  /// |   |						  // ");
        LOG.warn("//					| \\_|  ''\\---/''  |   |						  //");
        LOG.warn("//					\\  .-\\__  `-`  ___/-. /						  //");
        LOG.warn("//				  ___`. .'  /--.--\\ `. . ___					  //");
        LOG.warn("//				.\"\" '<  `.___\\_<|>_/___.'  >'\"\".				  //");
        LOG.warn("//			  | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |				  //");
        LOG.warn("//			  \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /                 //");
        LOG.warn("//		========`-.____`-.___\\_____/___.-`____.-'========		  //");
        LOG.warn("//				             `=---='                              //");
        LOG.warn("//		^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        showExitLog();
    }

    private void showExitLog()
    {
        LOG.error("--------------------------------------------------------------");
        LOG.error("---                                                        ---");
        LOG.error("---                         ERROR                          ---");
        LOG.error("---               Sys  Admin    Web stoped     !!!!!         ---");
        LOG.error("---                    www.fruit.com                    ---");
        LOG.error("---                                                        ---");
        LOG.error("--------------------------------------------------------------");
    }
}
