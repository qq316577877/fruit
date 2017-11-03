/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user;

import com.fruit.account.biz.common.UserIdentificationEnum;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.service.ProductService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.product.UserProductLoanModel;
import com.fruit.sys.admin.model.user.profile.UserProfileModel;
import com.fruit.sys.admin.service.user.UserListService;
import com.fruit.sys.admin.service.user.product.UserProductInfoService;
import com.fruit.sys.admin.service.user.product.UserProductLoanResultService;
import com.fruit.sys.admin.service.user.profile.UserProfileInfoService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserDetailAction.java
 */
@Component
@UriMapping("/user")
public class UserDetailAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailAction.class);

    @Autowired
    private UserListService userListService;

    @Autowired
    private UserProfileInfoService userProfileInfoService;

    @Autowired
    private UserProductInfoService userProductInfoService;

    @Autowired
    private UserProductLoanResultService userProductLoanResultService;


    private static final List<IdValueVO> USER_IDENTIFICATION_LIST = new ArrayList<IdValueVO>();

    static
    {
        UserIdentificationEnum[] values = UserIdentificationEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (UserIdentificationEnum type : values)
            {
                USER_IDENTIFICATION_LIST.add(new IdValueVO(type.getType(), type.getMessage()));
            }
        }
    }

    @UriMapping("/detail")
    public String showDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_list_url", UrlUtils.getUserInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/user/user_detail";
    }

    @UriMapping("/detail/base")
    public String showBaseInfo()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("user", userModel);
            request.setAttribute("id", userId);
            request.setAttribute("login_as_user_url", UrlUtils.getLoginAsUserUrl(super.getDomain()));
            request.setAttribute("save_base_info_ajax", UrlUtils.getSaveUserBaseInfoUrl(super.getDomain()));
            request.setAttribute("update_user_password_ajax", UrlUtils.getUpdateUserPasswordUrl(super.getDomain()));
            request.setAttribute("user_identification_list", USER_IDENTIFICATION_LIST);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
            return "/user/user_detail_base";
    }

    @UriMapping("/detail/enterprise")
    public String showEnterpriseInfo()
    {
        try{
            int userId = super.getIntParameter("id", 0);
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("user", userModel);
            request.setAttribute("id", userId);

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/enterprise].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/user_detail_enterprise";
    }


    /**
     * 会员物流信息
     * @return
     */
    @UriMapping("/detail/logistics")
    public String showLogisticsInfo()
    {
        try{
            int userId = super.getIntParameter("id", 0);
            UserProfileModel userProfileModel = userProfileInfoService.loadUserProfileModel(userId);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("userProfileModel", userProfileModel);
            request.setAttribute("userId", userId);

            request.setAttribute("save_nationalLogistics_info_ajax", UrlUtils.getUpdateNationalLogisticsUrl(super.getDomain()));
            request.setAttribute("save_internationalLogistics_info_ajax", UrlUtils.getUpdateInternationalLogisticsUrl(super.getDomain()));
            request.setAttribute("save_nationalClearance_info_ajax", UrlUtils.getUpdateNationalClearanceUrl(super.getDomain()));
            request.setAttribute("save_internationalClearance_info_ajax", UrlUtils.getUpdateInternationalClearanceUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/logistics].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }


        return "/user/user_detail_logistics";
    }

    /**
     * 单笔贷款额度
     * @return
     */
    @UriMapping("/detail/product")
    public String showProductInfo()
    {
        try{
            int userId = super.getIntParameter("id", 0);
//            int userId = 300;

            int pageNo = super.getIntParameter("pageNo", 1);
            int pageSize = BizConstants.DEFAULT_PAGE_SIZE;

            List<UserProductLoanModel> userProductList = userProductInfoService.loadProductList(userId, pageNo, pageSize);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("userId" ,userId);
            request.setAttribute("user_product_list" ,userProductList);
            request.setAttribute("product_loan_add" ,UrlUtils.getProductLoanInfoUrl(super.getDomain()));
            request.setAttribute("update_product_loan_ajax" ,UrlUtils.getUpdateProductLoanInfoUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/product].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/user/user_detail_product";
    }

    /**
     * 单笔贷款-增加产品
     * @return
     */
    @UriMapping("/detail/product/add")
    public String addProductLoanInfo()
    {
        boolean flag = false;
        int userId = super.getIntParameter("userId", 0);
        List<ProductDTO> listProduct = userProductLoanResultService.loadProductInfo(userId);
        HttpServletRequest request = WebContext.getRequest();
        if(null != listProduct && listProduct.size() > 0){
            flag= true;
        }

        request.setAttribute("product_flag" ,flag);
        request.setAttribute("product_list" ,listProduct);
        request.setAttribute("userId" ,userId);
        request.setAttribute("save_product_loan_ajax" ,UrlUtils.getSaveProductLoanInfoUrl(super.getDomain()));
        return "/user/user_detail_product_add";
    }



}
