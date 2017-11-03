/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action;

import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.CacheManageService;
import com.fruit.sys.admin.service.common.RedBankService;
import com.fruit.sys.admin.spy.SpyManageService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : partal-main-web
 * File Name      : CacheManagementAction.java
 */
@Component
@UriMapping("/cache")
public class CacheManagementAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagementAction.class);

    @Autowired
    private CacheManageService cacheManageService;

    @Autowired
    private RedBankService redBankService;

    @Autowired
    private SpyManageService spyManageService;

    @UriMapping(value = "/manager")
    public String show()
    {
        return "/tool/cache_manage";
    }

    @UriMapping(value = "/sync_user_model_ajax")
    public JsonResult syncUserModel()
    {
        UserInfo userInfo = super.getUserInfo();
        int userId = super.getIntParameter("user_id", 0);
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            Validate.isTrue(userId > 0, "参数错误!");
            cacheManageService.syncUserModel(userId);
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步用户信息失败! userId:" + userId, e);
            return new JsonResult("同步用户信息失败:" + e.getMessage());
        }
    }
    
    @UriMapping(value = "/sync_product_info_ajax")
    public JsonResult syncProductInfo()
    {
        UserInfo userInfo = super.getUserInfo();
//        int productId = super.getIntParameter("product_id", 0);
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
//            Validate.isTrue(productId > 0, "参数错误!");
            cacheManageService.syncProduct();
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步商品信息失败! syncProduct:" , e);
            return new JsonResult("同步商品信息失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_seller_rule_ajax")
    public JsonResult syncSellerRule()
    {
        UserInfo userInfo = super.getUserInfo();
        int sellerId = super.getIntParameter("seller_id", 0);
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            Validate.isTrue(sellerId > 0, "参数错误!");
            cacheManageService.syncSellerRule(sellerId);
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步卖家售卖规则失败! sellerId:" + sellerId, e);
            return new JsonResult("同步卖家售卖规则失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_order_content_ajax")
    public JsonResult syncOrderContent()
    {
        UserInfo userInfo = super.getUserInfo();
        long orderId = super.getLongParameter("order_id", 0);
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            Validate.isTrue(orderId > 0, "参数错误!");
            cacheManageService.syncOrderContent(orderId);
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步订单内容失败! orderId:" + orderId, e);
            return new JsonResult("同步订单内容失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_order_detail_ajax")
    public JsonResult syncOrderDetail()
    {
        UserInfo userInfo = super.getUserInfo();
        long orderId = super.getLongParameter("order_id", 0);
        int status = super.getIntParameter("status", 0);
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            Validate.isTrue(orderId > 0, "参数错误!");
            cacheManageService.syncOrderDetail(orderId, status);
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步订单详情失败! orderId:" + orderId + " status=" + status, e);
            return new JsonResult("同步订单详情失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_home_data_ajax")
    public JsonResult syncHomeData()
    {
        UserInfo userInfo = super.getUserInfo();
//        String type = super.getStringParameter("type");
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
//            Validate.isTrue(StringUtils.isNotBlank(type), "参数错误!");
//            cacheManageService.syncHomeData(type);
            cacheManageService.syncHomeData();
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步首页数据失败!", e);
            return new JsonResult("同步首页数据失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_global_context_ajax")
    public JsonResult syncGlobalContext()
    {
        UserInfo userInfo = super.getUserInfo();
        int userId = super.getIntParameter("user_id", 0);
        String userToken = super.getStringParameter("user_token");
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            Validate.isTrue(userId > 0 || StringUtils.isNotBlank(userToken), "参数错误!");
            cacheManageService.syncGlobalContext(userId, userToken);
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步全局上下文失败! userId:" + userId + " userToken=" + userToken, e);
            return new JsonResult("同步全局上下文失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/sync_mc_context_ajax")
    public JsonResult syncMCContext()
    {
        UserInfo userInfo = super.getUserInfo();
        String bizId = super.getStringParameter("biz_id", "");
        try
        {
            Validate.isTrue(null != userInfo, "请先登录!");
            redBankService.syncFireEvent(bizId, MessageTypeEnum.MC_RELOAD_SUBSCRIBER, userInfo.getUserName());
            return new JsonResult();
        }
        catch (Exception e)
        {
            LOGGER.info("同步消息中心上下文失败! bizId:" + bizId, e);
            return new JsonResult("同步消息中心上下文失败:" + e.getMessage());
        }
    }

    @UriMapping(value = "/spy_sync_ajax")
    public JsonResult spySync()
    {
        JsonResult<Boolean> jsonResult = new JsonResult<Boolean>();
        boolean err = false;

        String type = super.getStringParameter("type");
        try
        {
            if (StringUtils.isNotBlank(type))
            {
                if ("category".equals(type))
                {
                    this.spyManageService.handleSpy("metadataService", "refreshCategoryCache");
                }
                else if ("brand".equals(type))
                {
                    this.spyManageService.handleSpy("metadataService", "refreshBrandCache");
                }
                else
                {
                    err = true;
                    LOGGER.error("illegal param : type");
                }
            }
            else
            {
                err = true;
                LOGGER.error("illegal param : type");
            }
        }
        catch (Exception e)
        {
            err = true;
            e.printStackTrace();
            LOGGER.error("spy sync error", e);
        }


        if (err)
        {
            jsonResult.setResult(false);
            jsonResult.setMsg("缓存刷新失败");
        }
        else
        {
            jsonResult.setResult(true);
        }

        return jsonResult;
    }

}
