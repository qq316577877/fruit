/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserIdentificationEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.meta.MetaProvince;
import com.fruit.sys.admin.meta.MetadataProvider;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.service.user.MemberService;
import com.fruit.sys.admin.service.user.UserListService;
import com.fruit.sys.admin.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.site.lookup.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class UserAuthDetailAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthDetailAction.class);

    @Autowired
    private UserListService userListService;

    @Autowired
    private AdminService adminService;


    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;



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

    @UriMapping("/authdetail")
    public String showAuthDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_auth_list_url", UrlUtils.getUserAuthInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/authdetail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/user_auth_detail";
    }

    @UriMapping("/authdetail/base")
    public String showAuthBaseInfo()
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
            LOGGER.error("[system][/user/authdetail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

            return "/user/user_auth_detail_base";
    }

    @UriMapping("/authdetail/enterprise")
    public String showEnterpriseDetailInfo()
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
            LOGGER.error("[system][/user/authdetail/enterprise].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }


        return "/user/user_auth_detail_enterprise";
    }


    /**
     * 审核操作
     * @return
     */
    @UriMapping("/authverify")
    public String showAuthVerify()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_auth_list_url", UrlUtils.getUserAuthInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/authverify].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/user_auth_verify";
    }


    @UriMapping("/authverify/base")
    public String showAuthVerifyBaseInfo()
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
            LOGGER.error("[system][/user/authverify/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/user_auth_verify_base";
    }

    @UriMapping("/authverify/enterprise")
    public String showEnterpriseVerifyInfo()
    {
        try{
            int userId = super.getIntParameter("id", 0);
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("user", userModel);
            request.setAttribute("id", userId);
            request.setAttribute("user_identification_list", USER_IDENTIFICATION_LIST);

            request.setAttribute("auth_enterprise_person_pass_ajax", UrlUtils.getAuthEnterprisePersonPassUrl(super.getDomain()));
            request.setAttribute("auth_enterprise_person_unpass_ajax", UrlUtils.getAuthEnterprisePersonUnPassUrl(super.getDomain()));
            request.setAttribute("auth_enterprise_enterprise_pass_ajax", UrlUtils.getAuthEnterpriseEnterprisePassUrl(super.getDomain()));
            request.setAttribute("auth_enterprise_enterprise_unpass_ajax", UrlUtils.getAuthEnterpriseEnterpriseUnPassUrl(super.getDomain()));
            request.setAttribute("user_auth_list_url", UrlUtils.getUserAuthInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/authverify/enterprise].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }


        return "/user/user_auth_verify_enterprise";
    }


    /**
     * 认证通过--个人
     * @return
     */
    @UriMapping(value = "/auth_enterprise_person_pass_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO authEnterpirsePersonPass()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("edit-personal-userid");
//        int id = (Integer) validationResults.get("edit-enterprise-id");
        int memberIdentification = (Integer) validationResults.get("edit-personal-identification");

        String rejectNote = (String) validationResults.get("edit-personal-rejectNote");
        String description = (String) validationResults.get("edit-personal-description");

        try
        {
            if(StringUtils.isEmpty(rejectNote)){
                rejectNote = "";
            }
            if(StringUtils.isEmpty(description)){
                description = "";
            }

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");

            int status = UserEnterpriseStatusEnum.VERIFIED.getStatus();

            EnterpriseVO enterpriseVO = new EnterpriseVO();
            enterpriseVO.setUserId(userId);
//            enterpriseVO.setId(id);
            enterpriseVO.setMemberIdentification(memberIdentification);
            enterpriseVO.setRejectNote(rejectNote);
            enterpriseVO.setDescription(description);
            enterpriseVO.setLastEditor(userInfo.getUserName());
            enterpriseVO.setStatus(status);//设置状态为已认证
            enterpriseVO.setType(UserTypeEnum.PERSONAL.getType());
            memberService.updateEnterpriseVerify(enterpriseVO,super.getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/auth_enterprise_person_pass_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    /**
     * 认证未通过--个人
     * @return
     */
    @UriMapping(value = "/auth_enterprise_person_unpass_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO authEnterpirsePersonUnpass()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("edit-personal-userid");
        int memberIdentification = (Integer) validationResults.get("edit-personal-identification");

        String rejectNote = (String) validationResults.get("edit-personal-rejectNote");
        String description = (String) validationResults.get("edit-personal-description");

        try
        {
            if(StringUtils.isEmpty(rejectNote)){
                rejectNote = "";
            }
            if(StringUtils.isEmpty(description)){
                description = "";
            }

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");

            int status = UserEnterpriseStatusEnum.REJECTED.getStatus();

            EnterpriseVO enterpriseVO = new EnterpriseVO();
            enterpriseVO.setUserId(userId);
//            enterpriseVO.setId(id);
            enterpriseVO.setMemberIdentification(memberIdentification);
            enterpriseVO.setRejectNote(rejectNote);
            enterpriseVO.setDescription(description);
            enterpriseVO.setLastEditor(userInfo.getUserName());
            enterpriseVO.setStatus(status);//设置状态为认证不通过
            enterpriseVO.setType(UserTypeEnum.PERSONAL.getType());
            memberService.updateEnterpriseVerify(enterpriseVO,super.getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/auth_enterprise_person_unpass_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }



    /**
     * 认证通过--企业
     * @return
     */
    @UriMapping(value = "/auth_enterprise_enterprise_pass_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO authEnterpirseEnterprisePass()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("edit-enterprise-userid");
        int memberIdentification = (Integer) validationResults.get("edit-enterprise-identification");

        String rejectNote = (String) validationResults.get("edit-enterprise-rejectNote");
        String description = (String) validationResults.get("edit-enterprise-description");

        try
        {
            if(StringUtils.isEmpty(rejectNote)){
                rejectNote = "";
            }
            if(StringUtils.isEmpty(description)){
                description = "";
            }

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");

            int status = UserEnterpriseStatusEnum.VERIFIED.getStatus();

            EnterpriseVO enterpriseVO = new EnterpriseVO();
            enterpriseVO.setUserId(userId);
            enterpriseVO.setMemberIdentification(memberIdentification);
            enterpriseVO.setRejectNote(rejectNote);
            enterpriseVO.setDescription(description);
            enterpriseVO.setLastEditor(userInfo.getUserName());
            enterpriseVO.setStatus(status);//设置状态为已认证
            enterpriseVO.setType(UserTypeEnum.ENTERPRISE.getType());
            memberService.updateEnterpriseVerify(enterpriseVO,super.getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/auth_enterprise_enterprise_pass_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    /**
     * 认证未通过--企业
     * @return
     */
    @UriMapping(value = "/auth_enterprise_enterprise_unpass_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO authEnterpirseEnterpirseUnpass()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("edit-enterprise-userid");
        int memberIdentification = (Integer) validationResults.get("edit-enterprise-identification");
        String rejectNote = (String) validationResults.get("edit-enterprise-rejectNote");
        String description = (String) validationResults.get("edit-enterprise-description");

        try
        {
            if(StringUtils.isEmpty(rejectNote)){
                rejectNote = "";
            }
            if(StringUtils.isEmpty(description)){
                description = "";
            }

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");

            int status = UserEnterpriseStatusEnum.REJECTED.getStatus();

            EnterpriseVO enterpriseVO = new EnterpriseVO();
            enterpriseVO.setUserId(userId);
            enterpriseVO.setMemberIdentification(memberIdentification);
            enterpriseVO.setRejectNote(rejectNote);
            enterpriseVO.setDescription(description);
            enterpriseVO.setLastEditor(userInfo.getUserName());
            enterpriseVO.setStatus(status);//设置状态为认证不通过
            enterpriseVO.setType(UserTypeEnum.ENTERPRISE.getType());
            memberService.updateEnterpriseVerify(enterpriseVO,super.getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/auth_enterprise_enterprise_unpass_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }

}
