/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : UrlUtils.java
 */
public class UrlUtils
{
    public static String getLoginUrl(String domain)
    {
        return "//" + domain + "/admin/login";
    }

    public static String getLoginUrl(String domain, String redir)
    {
        return "//" + domain + "/admin/login?redir=" + toUtf8String(redir);
    }

    public static String getLoginAjaxUrl(String domain)
    {
        return "//" + domain + "/admin/login_ajax";
    }

    public static String toUtf8String(String source)
    {
        if (StringUtils.isNotBlank(source))
        {
            try
            {
                return URLEncoder.encode(source, "UTF8");
            }
            catch (UnsupportedEncodingException e)
            {
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    public static String getLoginUrlWithRedir(String domain, HttpServletRequest request)
    {
        return getLoginUrl(domain) + "?redir=" + toUtf8String(getFullUrl(request));
    }

    public static String getLogoutUrl(String domain)
    {
        return "//" + domain + "/admin/logout";
    }

    public static String getFullUrl(HttpServletRequest request)
    {
        int serverPort = request.getServerPort();
        String portSegment = serverPort == 80 ? "" : ":" + serverPort;
        String serverName = request.getServerName();
        String queryString = request.getQueryString();
        return  "http://" + serverName + portSegment + request.getRequestURI() + (StringUtils.isNotBlank(queryString) ? ("?" + queryString) : "");
    }

    public static String getFileUploadUrl(String domain)
    {
        return "//" + domain + "/admin/file/upload";
    }

    public static String getAdminUserDetailUrl(String domain)
    {
        return "//" + domain + "/admin/admin/user/detail";
    }

    public static String getSaveAdminUserAjaxUrl(String domain)
    {
        return "//" + domain + "/admin/admin/user/save_ajax";
    }


    /*用户列表*/
    public static String getUserInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/list";
    }

    /*用户详情*/
    public static String getUserInfoDetailUrl(String domain)
    {
        return "//" + domain + "/admin/user/detail";
    }


    /*会员审核列表*/
    public static String getUserAuthInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/authlist";
    }

    /*会员审核用户详情*/
    public static String getUserInfoAuthDetailUrl(String domain)
    {
        return "//" + domain + "/admin/user/authdetail";
    }

    /*会员审核审核操作*/
    public static String getUserInfoAuthVerifyUrl(String domain)
    {
        return "//" + domain + "/admin/user/authverify";
    }

    /*认证通过--个人*/
    public static Object getAuthEnterprisePersonPassUrl(String domain)
    {
        return "//" + domain + "/admin/user/auth_enterprise_person_pass_ajax";
    }

    /*认证未通过--个人*/
    public static Object getAuthEnterprisePersonUnPassUrl(String domain)
    {
        return "//" + domain + "/admin/user/auth_enterprise_person_unpass_ajax";
    }

    /*认证通过--企业*/
    public static Object getAuthEnterpriseEnterprisePassUrl(String domain)
    {
        return "//" + domain + "/admin/user/auth_enterprise_enterprise_pass_ajax";
    }

    /*认证未通过--企业*/
    public static Object getAuthEnterpriseEnterpriseUnPassUrl(String domain)
    {
        return "//" + domain + "/admin/user/auth_enterprise_enterprise_unpass_ajax";
    }

    /*供应商管理-列表*/
    public static String getUserSupplierInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/supplier/list";
    }

    /*供应商管理-供应商详情*/
    public static String getUserSupplierInfoDetailUrl(String domain)
    {
        return "//" + domain + "/admin/user/supplier/detail";
    }


    /*基础信息配置-清关物流公司列表*/
    public static String getEnterpriseInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/list";
    }

    /*基础信息配置-清关物流公司详情*/
    public static String getEnterpriseInfoDetailUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/detail";
    }
    /*基础信息配置-清关物流公司新增*/
    public static String getEnterpriseInfoAddUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/add";
    }

    /*基础信息配置-清关物流公司修改*/
    public static String getEnterpriseInfoUpdateUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/update";
    }

    /*基础信息配置-清关物流公司删除*/
    public static String getEnterpriseInfoDeleteUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/delete";
    }


    /*基础信息配置-清关物流公司新增*/
    public static String getEnterpriseInfoAddInfoUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/enterprise_add_ajax";
    }

    /*基础信息配置-清关物流公司修改*/
    public static String getEnterpriseInfoUpdateInfoUrl(String domain)
    {
        return "//" + domain + "/admin/configure/enterpriseinfo/enterprise_update_ajax";
    }

    /*配置国内物流*/
    public static Object getUpdateNationalLogisticsUrl(String domain)
    {
        return "//" + domain + "/admin/user/profile/save_nationalLogistics_info_ajax";
    }

    /*配置国际物流*/
    public static Object getUpdateInternationalLogisticsUrl(String domain)
    {
        return "//" + domain + "/admin/user/profile/save_internationalLogistics_info_ajax";
    }

    /*配置国内清关*/
    public static Object getUpdateNationalClearanceUrl(String domain)
    {
        return "//" + domain + "/admin/user/profile/save_nationalClearance_info_ajax";
    }

    /*配置国际清关*/
    public static Object getUpdateInternationalClearanceUrl(String domain)
    {
        return "//" + domain + "/admin/user/profile/save_internationalClearance_info_ajax";
    }

    /*产品额度管理*/
    public static Object getProductLoanInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/detail/product/add";
    }

    public static Object getSaveProductLoanInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/product/save_product_loan_ajax";
    }

    public static Object getUpdateProductLoanInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/product/update_product_loan_ajax";
    }


    public static String getPortalDashboardUrl(String portalDomain, String sign)
    {
        return "http://" + portalDomain + "/member/login_with_sign?sign=" + sign;
    }

    public static String getLoginAsUserUrl(String domain)
    {
        return "http://" + domain + "/admin/user/login_as_user";
    }

    public static Object getSaveUserBaseInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/save_base_info_ajax";
    }

    public static Object getUpdateUserPasswordUrl(String domain)
    {
        return "//" + domain + "/admin/user/update_user_password_ajax";
    }

    public static Object getSaveEnterpriseInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/save_enterprise_info_ajax";
    }



    /*授信申请*/
    public static String getUserLoanInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/list/apply";
    }

    public static String getUserLoanInfoDetailUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/detail/loan";
    }

    /*额度管理*/
    public static String getUserCreditInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/list";
    }

    public static String getContractCaptchaSendUrl(String domain)
    {
        return "//" + domain + "/admin/loan/auth/contract/captcha_send_ajax";
    }

    public static String getContractSignBorrowUrl(String domain)
    {
        return "//" + domain + "/admin/loan/auth/contract/online_sign_ajax";
    }

    public static String getUserCreditDetailInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/detail";
    }

    public static String getUserCreditDetailInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/detail/base";
    }

    public static Object getUserCreditInfoListUpdateUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/detail/update_user_credit_info_ajax";
    }

    public static Object getContractCreditInfoListUpdateUrl(String domain)
    {
        return "//" + domain + "/admin/user/credit/detail/update_contract_credit_info_ajax";
    }

    /*贷款管理*/
    public static String getUserLoanManagementInfoListUrl(String domain)
    {
        return "//" + domain + "/admin/user/loanManagement/list";
    }

    public static String getUserLoanManagementDetailInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/loanManagement/detail";
    }

    public static Object getUserLoanManamgementInfoUpdateUrl(String domain)
    {
        return "//" + domain + "/admin/user/loanManagement/detail/update_user_loanManagement_info_ajax";
    }

    public static Object getUserLoanManamgementInfoCheckUrl(String domain)
    {
        return "//" + domain + "/admin/user/loanManagement/detail/loanCheckAjax";
    }
    public static Object getLoanCaptchaSendAjaxUrl(String domain)
    {
        return "//" + domain + "/admin/loan/auth/contract/debt_captcha_send_ajax";
    }
    public static Object getLoanSignDebtContractAjaxUrl(String domain)
    {
        return "//" + domain + "/admin/loan/auth/contract/debt_sign_ajax";
    }


    /*贷后管理*/
    public static String getUserRepaymentListUrl(String domain)
    {
        return "//" + domain + "/admin/user/repayment/list";
    }

    public static String getUserRepaymentDetailInfoUrl(String domain)
    {
        return "//" + domain + "/admin/user/repayment/detail";
    }

    public static Object getUserRepaymentInfoUpdateUrl(String domain)
    {
        return "//" + domain + "/admin/user/repayment/detail/update_user_repayment_info_ajax";
    }


}
