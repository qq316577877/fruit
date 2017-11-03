/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.profile;


import com.fruit.account.biz.common.*;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.dto.UserInfoUpdateLogDTO;
import com.fruit.account.biz.dto.UserProfileDTO;
import com.fruit.account.biz.request.sys.SysUserListRequest;
import com.fruit.account.biz.request.sys.SysUserProfileRequest;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.account.biz.service.UserInfoUpdateLogService;
import com.fruit.account.biz.service.UserProfileService;
import com.fruit.account.biz.service.sys.SysUserAccountService;
import com.fruit.account.biz.service.sys.SysUserEnterpriseService;
import com.fruit.account.biz.service.sys.SysUserProfileService;
import com.fruit.base.biz.dto.EnterpriseInfoDTO;
import com.fruit.sys.admin.event.ITask;
import com.fruit.sys.admin.event.TaskEvent;
import com.fruit.sys.admin.meta.*;
import com.fruit.sys.admin.model.BaseUserInfo;
import com.fruit.sys.admin.model.UserInfoQueryRequest;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.configure.EnterpriseInfoModel;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.model.user.profile.UserProfileModel;
import com.fruit.sys.admin.service.BaseInfoService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.CacheManageService;
import com.fruit.sys.admin.service.configure.EnterpriseInfoListService;
import com.fruit.sys.admin.service.user.UserListService;
import com.ovfintech.arch.common.event.EventChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-03
 * Project        : fruit
 * File Name      : UserProfileInfoService.java
 */
@Service
public class UserProfileInfoService extends BaseService
{

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private SysUserProfileService sysUserProfileService;

    @Autowired
    private EnterpriseInfoListService enterpriseInfoListService;

    @Autowired
    private CacheManageService cacheManageService;

    @Autowired
    private UserInfoUpdateLogService infoUpdateLogService;


    @Autowired
    private UserListService userListService;


    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;





    /**
     * 通过userid获取用户配置信息
     * 清关公司、物流公司信息
     * @param userId
     * @return
     */
    public UserProfileModel loadUserProfileModel(int userId)
    {
        UserProfileModel userProfileModel = null;

        if (userId > 0)
        {
            UserProfileDTO userProfileDTO = userProfileService.loadByUserId(userId);

            if (null != userProfileDTO)
            {
                userProfileModel = giveUserProfileModel(userProfileDTO);
            }
        }
        return userProfileModel;
    }



    /**
     * 加载用户配置信息
     * @param userProfileDTO
     * @return
     */
    protected UserProfileModel giveUserProfileModel(UserProfileDTO userProfileDTO)
    {
        UserProfileModel userProfileModel = null;
        if (userProfileDTO != null)
        {
            userProfileModel = new UserProfileModel();
            BeanUtils.copyProperties(userProfileDTO, userProfileModel);

            DBStatusEnum statusEnum = DBStatusEnum.get(userProfileDTO.getStatus());
            if (null != statusEnum)
            {
                userProfileModel.setStatusDesc(statusEnum.getMessage());
            }

            int internationalClearance = userProfileDTO.getInternationalClearance();
            EnterpriseInfoModel internationalClearanceModel = enterpriseInfoListService.loadEnterpriseModelById(internationalClearance);
            if(internationalClearanceModel!=null){
                userProfileModel.setInternationalClearanceModel(internationalClearanceModel);
            }

            int nationalClearance = userProfileDTO.getNationalClearance();
            EnterpriseInfoModel nationalClearanceModel = enterpriseInfoListService.loadEnterpriseModelById(nationalClearance);
            if(nationalClearanceModel!=null){
                userProfileModel.setNationalClearanceModel(nationalClearanceModel);
            }

            int internationalLogistics = userProfileDTO.getInternationalLogistics();
            EnterpriseInfoModel internationalLogisticsModel = enterpriseInfoListService.loadEnterpriseModelById(internationalLogistics);
            if(internationalLogisticsModel!=null){
                userProfileModel.setInternationalLogisticsModel(internationalLogisticsModel);
            }

            int nationalLogistics = userProfileDTO.getNationalLogistics();
            EnterpriseInfoModel nationalLogisticsModel = enterpriseInfoListService.loadEnterpriseModelById(nationalLogistics);
            if(nationalLogisticsModel!=null){
                userProfileModel.setNationalLogisticsModel(nationalLogisticsModel);
            }

        }
        return userProfileModel;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfileInfo(SysUserProfileRequest sysUserProfileRequest, String lastEditor, String userIp){
        int update = sysUserProfileService.update(sysUserProfileRequest);

        int userId = sysUserProfileRequest.getUserId();
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin update user profile info.", UserInfoUpdateLogTypeEnum.PROFILE_CONFIG.getType(), lastEditor, userIp);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addUserProfileInfo(SysUserProfileRequest sysUserProfileRequest, String lastEditor, String userIp){
        sysUserProfileService.create(sysUserProfileRequest);
        int userId = sysUserProfileRequest.getUserId();
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin create user profile info.", UserInfoUpdateLogTypeEnum.PROFILE_CONFIG.getType(), lastEditor, userIp);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void asyncSaveUpdateLog(final UserModel userModel, final String field, final int type, final String operator, final String userIp)
    {
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                if (null != userModel)
                {
                    UserInfoUpdateLogDTO infoUpdateLogDTO = new UserInfoUpdateLogDTO();
                    infoUpdateLogDTO.setUserId(userModel.getUserId());
                    infoUpdateLogDTO.setEnterpriseName(null != userModel.getEnterprise() ? userModel.getEnterprise().getName() : "");
                    infoUpdateLogDTO.setOperator(operator);
                    infoUpdateLogDTO.setUserIp(userIp);
                    infoUpdateLogDTO.setType(type);
                    infoUpdateLogDTO.setInfo(field);
                    infoUpdateLogService.create(infoUpdateLogDTO);
                }
            }
        }));
    }


}
