/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user;


import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserIdentificationEnum;
import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.request.sys.SysUserListRequest;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.account.biz.service.sys.SysUserAccountService;
import com.fruit.account.biz.service.sys.SysUserEnterpriseService;
import com.fruit.sys.admin.meta.*;
import com.fruit.sys.admin.model.BaseUserInfo;
import com.fruit.sys.admin.model.UserInfoQueryRequest;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.service.BaseInfoService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.common.FileUploadService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserListService.java
 */
@Service
public class UserListService extends BaseService
{

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SysUserAccountService sysUserAccountService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private SysUserEnterpriseService sysUserEnterpriseService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private FileUploadService fileUploadService;


    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public List<UserModel> loadUserList(UserInfoQueryRequest queryRequest, int pageNo, int pageSize)
    {
        SysUserListRequest request = this.transfer(queryRequest);

        List<UserModel> result = Collections.emptyList();

        List<UserAccountDTO> userAccountDTOs = sysUserAccountService.searchByKeyword(request, pageNo, pageSize);
        if (CollectionUtils.isNotEmpty(userAccountDTOs))
        {
            List<UserModel> userModelList = this.getUserModelLists(userAccountDTOs);
            result = userModelList;
        }

        return result;
    }

    public List<Integer> queryUserIds(String enterpriseName)
    {
        List<Integer> userIds = null;
        if (StringUtils.isNotBlank(enterpriseName))
        {
            userIds = this.searchUserIds(enterpriseName.trim());
        }
        return userIds;
    }

    public SysUserListRequest transfer(UserInfoQueryRequest queryRequest)
    {
        SysUserListRequest request = new SysUserListRequest();
        try
        {
            org.apache.commons.beanutils.BeanUtils.copyProperties(request, queryRequest);
            String keyword = request.getKeyword();
            List<Integer> unionIds = Collections.EMPTY_LIST;
            if (StringUtils.isNotBlank(keyword))
            {
                unionIds = this.searchUserIds(keyword);
            }
            request.setUnionIds(unionIds);
            request.setExcludeIds(super.getUserBacklistIds());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return request;
    }

    private List<UserModel> getUserModelLists(List<UserAccountDTO> userAccountDTOs)
    {
        List<FutureTask<UserModel>> tasks = new ArrayList<FutureTask<UserModel>>(userAccountDTOs.size());
        List<UserModel> userModelList = new ArrayList<UserModel>(userAccountDTOs.size());
        if (CollectionUtils.isNotEmpty(userAccountDTOs))
        {
            for (UserAccountDTO user : userAccountDTOs)
            {
                FutureTask<UserModel> futureTask = new FutureTask(new UserModelCallable(user));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<UserModel> task : tasks)
            {
                try
                {
                    UserModel userModel = task.get();
                    userModelList.add(userModel);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return userModelList;
    }

    public List<Integer> searchUserIds(String keyword)
    {
        int intValue = NumberUtils.toInt(keyword);
        if (intValue > 0)
        {
            return Arrays.asList(-1);//当为纯数字的时候，匹配电话号码，以及数据ID为-1的数据
        }
        else
        {
            return sysUserEnterpriseService.searchIdsByKeyword(keyword);
        }
    }

    class UserModelCallable implements Callable<UserModel>
    {
        private UserAccountDTO user;

        public UserModelCallable(UserAccountDTO user)
        {
            this.user = user;
        }

        @Override
        public UserModel call() throws Exception
        {
            try {
                return loadUserModel(user, true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }

    public UserModel loadUserModel(int userId, boolean loadEnterprise)
    {
        UserModel userModel = null;
        if (userId > 0)
        {
            UserAccountDTO userAccountDTO = userAccountService.loadById(userId);
            if (null != userAccountDTO)
            {
                return loadUserModel(userAccountDTO, loadEnterprise);
            }
        }
        return userModel;
    }


    /**
     * 加载用户，后一个参数为是否加载企业信息
     * @param userAccountDTO
     * @param loadEnterprise
     * @return
     */
    protected UserModel loadUserModel(UserAccountDTO userAccountDTO, boolean loadEnterprise)
    {
        UserModel userModel = null;
        if (userAccountDTO != null)
        {
            userModel = new UserModel();
            userModel.setMobile(userAccountDTO.getMobile());
            userModel.setMail(userAccountDTO.getMail());
            userModel.setUserId(userAccountDTO.getId());
            userModel.setType(userAccountDTO.getType());
            userModel.setTypeDes(UserTypeEnum.get(userAccountDTO.getType()).getMessage());
            userModel.setStatus(userAccountDTO.getStatus());
            UserStatusEnum statusEnum = UserStatusEnum.get(userAccountDTO.getStatus());
            if (null != statusEnum)
            {
                userModel.setStatusDes(statusEnum.getMessage());
            }
            userModel.setEnterpriseVerifyStatus(userAccountDTO.getEnterpriseVerifyStatus());

            UserEnterpriseStatusEnum enterpriseStatusEnum = UserEnterpriseStatusEnum.get(userAccountDTO.getEnterpriseVerifyStatus());
            if (null != enterpriseStatusEnum)
            {
                userModel.setEnterpriseVerifyStatusDesc(enterpriseStatusEnum.getMessage());
            }


            userModel.setQQ(userAccountDTO.getQQ());
            userModel.setAddTime(userAccountDTO.getAddTime());
            if (loadEnterprise)
            {
                loadEnterpriseInfo(userAccountDTO, userModel);
            }
            loadAddUser(userAccountDTO, userModel);
            userModel.setDescription(userAccountDTO.getDescription());

        }
        return userModel;
    }

    private void loadEnterpriseInfo(UserAccountDTO userAccountDTO, UserModel userModel)
    {
        UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userAccountDTO.getId());
        if (userEnterpriseDTO != null)
        {
            EnterpriseVO enterpriseVO = new EnterpriseVO();
            BeanUtils.copyProperties(userEnterpriseDTO, enterpriseVO);
            userModel.setEnterprise(enterpriseVO);
            enterpriseVO.setTypeDesc(UserTypeEnum.get(userEnterpriseDTO.getType()).getMessage());

            enterpriseVO.setStatusDesc(UserEnterpriseStatusEnum.get(userEnterpriseDTO.getStatus()).getMessage());

            if (userEnterpriseDTO.getCountryId() > 0)
            {
                MetaCountry country = this.metadataProvider.getCountry(userEnterpriseDTO.getCountryId());
                if (null != country)
                {
                    enterpriseVO.setCountryName(country.getName());
                }
            }
            if (userEnterpriseDTO.getProvinceId() > 0)
            {
                MetaProvince province = this.metadataProvider.getProvince(userEnterpriseDTO.getProvinceId());
                if (null != province)
                {
                    enterpriseVO.setProvinceName(province.getName());
                }
            }
            if (userEnterpriseDTO.getCityId() > 0)
            {
                MetaCity city = this.metadataProvider.getCity(userEnterpriseDTO.getCityId());
                if (null != city)
                {
                    enterpriseVO.setCityName(city.getName());
                }
            }

            if (userEnterpriseDTO.getDistrictId() > 0)
            {
                MetaArea area = this.metadataProvider.getArea(userEnterpriseDTO.getDistrictId());
                if (null != area)
                {
                    enterpriseVO.setDistrictName(area.getName());
                }
            }

            if(StringUtils.isNotEmpty(userEnterpriseDTO.getLicence())){
                enterpriseVO.setLicenceUrl(fileUploadService.buildDiskUrl(userEnterpriseDTO.getLicence()));
            }

            if(StringUtils.isNotEmpty(userEnterpriseDTO.getIdentityFront())){
                enterpriseVO.setIdentityFrontUrl(fileUploadService.buildDiskUrl(userEnterpriseDTO.getIdentityFront()));
            }

            if(StringUtils.isNotEmpty(userEnterpriseDTO.getIdentityBack())){
                enterpriseVO.setIdentityBackUrl(fileUploadService.buildDiskUrl(userEnterpriseDTO.getIdentityBack()));
            }

            if(StringUtils.isNotEmpty(userEnterpriseDTO.getAttachmentOne())){
                enterpriseVO.setAttachmentOneUrl(fileUploadService.buildDiskUrl(userEnterpriseDTO.getAttachmentOne()));
            }

            if(StringUtils.isNotEmpty(userEnterpriseDTO.getAttachmentTwo())){
                enterpriseVO.setAttachmentTwoUrl(fileUploadService.buildDiskUrl(userEnterpriseDTO.getAttachmentTwo()));
            }

            //默认是新客户
            int memberIdentification = UserIdentificationEnum.get(userEnterpriseDTO.getMemberIdentification()).getType();
            enterpriseVO.setMemberIdentification(memberIdentification);

        }
    }

    private void loadAddUser(UserAccountDTO userAccountDTO, UserModel userModel)
    {
        int addUserId = userAccountDTO.getAddUserId();
        if (addUserId > 0)
        {
            userModel.setAddUserId(addUserId);
            BaseUserInfo baseUserInfo = this.baseInfoService.loadUserInfo(addUserId);
            if (baseUserInfo != null)
            {
                String enterpriseName = baseUserInfo.getEnterpriseName();
                String userName = baseUserInfo.getUserName();
                if (StringUtils.isNotBlank(enterpriseName))
                {
                    userModel.setAddUserName(enterpriseName);
                }
                else if (StringUtils.isNotBlank(userName))
                {
                    userModel.setAddUserName(userName);
                }
            }
        }
    }


    /**
     * 获取用户列表---去掉已认证状态的
     * @param queryRequest
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<UserModel> loadUserAuthList(UserInfoQueryRequest queryRequest, int pageNo, int pageSize)
    {
        //认证状态排除
        List<Integer> notIncludeStatus = new ArrayList<Integer>(3);
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.DELETED.getStatus()));
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.VERIFIED.getStatus()));
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.NOT_YET.getStatus()));//20170807
        queryRequest.setNotIncludeStatus(notIncludeStatus);
        //用户类型排除 20170807
        UserTypeEnum type = queryRequest.getType();
        List<Integer>  types = new ArrayList<Integer>();
        if(type != null){
            types.add(type.getType());
        }
        //查询用户类型的 用户
        List<Integer> userIds = sysUserEnterpriseService.searchIdsByTypesStatus(types,null);

        queryRequest.setIncludeIds(userIds);

        SysUserListRequest request = this.transfer(queryRequest);

        List<UserModel> result = Collections.emptyList();

        List<UserAccountDTO> userAccountDTOs = sysUserAccountService.searchByKeywordWithoutVerified(request, pageNo, pageSize);
        if (CollectionUtils.isNotEmpty(userAccountDTOs))
        {
            List<UserModel> userModelList = this.getUserModelLists(userAccountDTOs);
            result = userModelList;
        }

        return result;
    }

}
