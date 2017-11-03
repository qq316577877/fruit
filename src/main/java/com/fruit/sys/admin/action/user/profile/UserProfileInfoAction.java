/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.profile;

import com.fruit.account.biz.request.sys.SysUserProfileRequest;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.user.profile.UserProfileModel;
import com.fruit.sys.admin.service.user.profile.UserProfileInfoService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-05
 * Project        : fruit
 * File Name      : UserProfileInfoAction.java
 */
@Component
@UriMapping("/user/profile")
public class UserProfileInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileInfoAction.class);


    @Autowired
    private UserProfileInfoService userProfileInfoService;


    /**
     * 存储国内物流公司
     * @return
     */
    @UriMapping(value = "/save_nationalLogistics_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveNationalLogisticsInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        int edit_nationalLogistics = (Integer) validationResults.get("edit_nationalLogistics");

        try
        {

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            UserProfileModel userProfileModel = userProfileInfoService.loadUserProfileModel(userId);

            SysUserProfileRequest sysUserProfileRequest = new SysUserProfileRequest();
            sysUserProfileRequest.setUserId(userId);
            sysUserProfileRequest.setNationalLogistics(edit_nationalLogistics);
            if(userProfileModel!=null){
                //使用update
                userProfileInfoService.updateUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }else{
                //使用add
                userProfileInfoService.addUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }

            UserProfileModel userProfileModelRet = userProfileInfoService.loadUserProfileModel(userId);
            Validate.isTrue(null != userProfileModelRet, "用户账号异常!");
            return new JsonResultDTO(userProfileModelRet);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/profile/save_nationalLogistics_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }



    /**
     * 存储国际物流公司
     * @return
     */
    @UriMapping(value = "/save_internationalLogistics_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveInternationalLogisticsInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        int edit_internationalLogistics = (Integer) validationResults.get("edit_internationalLogistics");

        try
        {

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            UserProfileModel userProfileModel = userProfileInfoService.loadUserProfileModel(userId);

            SysUserProfileRequest sysUserProfileRequest = new SysUserProfileRequest();
            sysUserProfileRequest.setUserId(userId);
            sysUserProfileRequest.setInternationalLogistics(edit_internationalLogistics);
            if(userProfileModel!=null){
                //使用update
                userProfileInfoService.updateUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }else{
                //使用add
                userProfileInfoService.addUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }

            UserProfileModel userProfileModelRet = userProfileInfoService.loadUserProfileModel(userId);
            Validate.isTrue(null != userProfileModelRet, "用户账号异常!");
            return new JsonResultDTO(userProfileModelRet);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/profile/save_internationalLogistics_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    /**
     * 存储国内清关公司
     * @return
     */
    @UriMapping(value = "/save_nationalClearance_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveNationalClearanceInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        int edit_nationalClearance = (Integer) validationResults.get("edit_nationalClearance");

        try
        {

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            UserProfileModel userProfileModel = userProfileInfoService.loadUserProfileModel(userId);

            SysUserProfileRequest sysUserProfileRequest = new SysUserProfileRequest();
            sysUserProfileRequest.setUserId(userId);
            sysUserProfileRequest.setNationalClearance(edit_nationalClearance);
            if(userProfileModel!=null){
                //使用update
                userProfileInfoService.updateUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }else{
                //使用add
                userProfileInfoService.addUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }

            UserProfileModel userProfileModelRet = userProfileInfoService.loadUserProfileModel(userId);
            Validate.isTrue(null != userProfileModelRet, "用户账号异常!");
            return new JsonResultDTO(userProfileModelRet);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/profile/save_nationalClearance_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }

    /**
     * 存储国际清关公司
     * @return
     */
    @UriMapping(value = "/save_internationalClearance_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveInternationalClearanceInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        int edit_internationalClearance = (Integer) validationResults.get("edit_internationalClearance");

        try
        {

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            UserProfileModel userProfileModel = userProfileInfoService.loadUserProfileModel(userId);

            SysUserProfileRequest sysUserProfileRequest = new SysUserProfileRequest();
            sysUserProfileRequest.setUserId(userId);
            sysUserProfileRequest.setInternationalClearance(edit_internationalClearance);
            if(userProfileModel!=null){
                //使用update
                userProfileInfoService.updateUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }else{
                //使用add
                userProfileInfoService.addUserProfileInfo(sysUserProfileRequest,userInfo.getUserName(), super.getUserIp());
            }

            UserProfileModel userProfileModelRet = userProfileInfoService.loadUserProfileModel(userId);
            Validate.isTrue(null != userProfileModelRet, "用户账号异常!");
            return new JsonResultDTO(userProfileModelRet);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/profile/save_internationalClearance_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }



}
