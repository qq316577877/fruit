/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.supplier;

import com.fruit.account.biz.common.DBStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.user.supplier.UserSupplierInfoQueryRequest;
import com.fruit.sys.admin.model.user.supplier.UserSupplierModel;
import com.fruit.sys.admin.service.user.supplier.UserSupplierListService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.biz.common.Page;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-01
 * Project        : fruit
 * File Name      : UserSupplierInfoAction.java
 */
@Component
@UriMapping("/user/supplier")
public class UserSupplierInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSupplierInfoAction.class);

    @Autowired
    private UserSupplierListService userSupplierListService;



    @UriMapping("/list")
    public String userSupplierInfoList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = BizConstants.DEFAULT_PAGE_SIZE;
        String keyword = super.getStringParameter("keyword", "");

//        int status = super.getIntParameter("status", 1);


        try
        {
            DBStatusEnum statusEnum = DBStatusEnum.get(1);//暂时只取状态为1的供应商信息

            UserSupplierInfoQueryRequest queryRequest = new UserSupplierInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setStatus(statusEnum);
            queryRequest.setSortKey("AddTime");

            List<UserSupplierModel> userSupplierModelList = userSupplierListService.loadUserSupplierList(queryRequest, pageNo, BizConstants.DEFAULT_PAGE_SIZE);

            Page<UserSupplierModel> pageDto = new Page<UserSupplierModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(userSupplierModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("user_supplier_list", userSupplierModelList);

            request.setAttribute("user_supplier_list_url", UrlUtils.getUserSupplierInfoListUrl(super.getDomain()));
            request.setAttribute("user_supplier_info_detail_url", UrlUtils.getUserSupplierInfoDetailUrl(super.getDomain()));

            return "/user/supplier/user_supplier_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/supplier/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }


    @UriMapping("/detail")
    public String showSupplierDetail()
    {
        try {
            int supplierId = super.getIntParameter("id", 0);
            Validate.isTrue(supplierId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", supplierId);
            request.setAttribute("user_supplier_list_url", UrlUtils.getUserSupplierInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/supplier/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }

        return "/user/supplier/user_supplier_detail";
    }


    @UriMapping("/detailbase")
    public String showSupplierDetailBase()
    {
        try {
            int supplierId = super.getIntParameter("id", 0);
            Validate.isTrue(supplierId > 0, "参数错误!");

            UserSupplierModel userSupplierModel = userSupplierListService.loadUserSupplierById(supplierId, true,true);
            Validate.isTrue(null != userSupplierModel, "该供应商信息已不存在!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", supplierId);
            request.setAttribute("supplier", userSupplierModel);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/supplier/detailbase].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }

        return "/user/supplier/user_supplier_detail_base";
    }



}
