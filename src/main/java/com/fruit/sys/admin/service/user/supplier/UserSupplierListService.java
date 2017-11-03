/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.supplier;


import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.account.biz.request.sys.SysUserSupplierListRequest;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.account.biz.service.UserEnterpriseService;
import com.fruit.account.biz.service.UserSupplierService;
import com.fruit.account.biz.service.sys.SysUserAccountService;
import com.fruit.account.biz.service.sys.SysUserSupplierService;
import com.fruit.sys.admin.meta.*;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.model.user.supplier.UserSupplierInfoQueryRequest;
import com.fruit.sys.admin.model.user.supplier.UserSupplierModel;
import com.fruit.sys.admin.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
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
public class UserSupplierListService extends BaseService
{

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserSupplierService userSupplierService;

    @Autowired
    private SysUserAccountService sysUserAccountService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private SysUserSupplierService sysUserSupplierService;

    @Autowired
    private MetadataProvider metadataProvider;



    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public List<UserSupplierModel> loadUserSupplierList(UserSupplierInfoQueryRequest queryRequest, int pageNo, int pageSize)
    {
        SysUserSupplierListRequest request = this.transfer(queryRequest);


        List<UserSupplierModel> result = Collections.emptyList();

        List<UserSupplierDTO> userSupplierDTOs = sysUserSupplierService.searchByKeyword(request, pageNo, pageSize);
        if (CollectionUtils.isNotEmpty(userSupplierDTOs))
        {
            List<UserSupplierModel> userSupplierModelList = this.getUserSupplierModelLists(userSupplierDTOs);
            result = userSupplierModelList;
        }

        return result;
    }



    private SysUserSupplierListRequest transfer(UserSupplierInfoQueryRequest queryRequest)
    {
        SysUserSupplierListRequest request = new SysUserSupplierListRequest();
        try
        {
            org.apache.commons.beanutils.BeanUtils.copyProperties(request, queryRequest);
            String keyword = request.getKeyword();

            List<Integer> unionIds = Collections.EMPTY_LIST;
            if (StringUtils.isNotBlank(keyword) && NumberUtils.isNumber(keyword))
            {
                unionIds = this.searchUserIds(keyword);//通过手机号查询到userId
            }
            request.setUnionIds(unionIds);
            request.setExcludeIds(super.getUserBacklistIds());//去掉的userId
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return request;
    }

    private List<UserSupplierModel> getUserSupplierModelLists(List<UserSupplierDTO> userSupplierDTOs)
    {
        List<FutureTask<UserSupplierModel>> tasks = new ArrayList<FutureTask<UserSupplierModel>>(userSupplierDTOs.size());

        List<UserSupplierModel> userSupplierModelList = new ArrayList<UserSupplierModel>(userSupplierDTOs.size());
        if (CollectionUtils.isNotEmpty(userSupplierDTOs))
        {
            for (UserSupplierDTO userSupplier : userSupplierDTOs)
            {
                FutureTask<UserSupplierModel> futureTask = new FutureTask(new UserSupplierModelCallable(userSupplier));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<UserSupplierModel> task : tasks)
            {
                try
                {
                    UserSupplierModel userSupplierModel = task.get();
                    userSupplierModelList.add(userSupplierModel);
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
        return userSupplierModelList;
    }

    public List<Integer> searchUserIds(String keyword)
    {
        if(NumberUtils.isNumber(keyword)){
            return sysUserAccountService.searchIdsByKeyword(keyword);
        }else{
            return null;
        }
    }

    class UserSupplierModelCallable implements Callable<UserSupplierModel>
    {
        private UserSupplierDTO userSupplier;

        public UserSupplierModelCallable(UserSupplierDTO userSupplier)
        {
            this.userSupplier = userSupplier;
        }

        @Override
        public UserSupplierModel call() throws Exception
        {
            try {
                return loadUserSupplierModel(userSupplier, true,true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }




    /**
     * 加载供应商
     * @param userSupplierDTO
     * @param loadUserInfo 是否加载用户信息
     * @param loadEnterprise 是否加载企业信息
     * @return
     */
    protected UserSupplierModel loadUserSupplierModel(UserSupplierDTO userSupplierDTO,boolean loadUserInfo, boolean loadEnterprise)
    {
        UserSupplierModel userSupplierModel = null;

        if (userSupplierDTO != null)
        {

            userSupplierModel = new UserSupplierModel();

            userSupplierModel.setId(userSupplierDTO.getId());
            userSupplierModel.setUserId(userSupplierDTO.getUserId());
            userSupplierModel.setSupplierName(userSupplierDTO.getSupplierName());
            userSupplierModel.setCountryId(userSupplierDTO.getCountryId());
            userSupplierModel.setProvinceId(userSupplierDTO.getProvinceId());
            userSupplierModel.setCityId(userSupplierDTO.getCityId());
            userSupplierModel.setDistrictId(userSupplierDTO.getDistrictId());
            userSupplierModel.setAddress(userSupplierDTO.getAddress());
            userSupplierModel.setZipCode(userSupplierDTO.getZipCode());
            userSupplierModel.setSupplierContact(userSupplierDTO.getSupplierContact());
            userSupplierModel.setCellPhone(userSupplierDTO.getCellPhone());
            userSupplierModel.setPhoneNum(userSupplierDTO.getPhoneNum());
            userSupplierModel.setStatus(userSupplierDTO.getStatus());
            userSupplierModel.setAddTime(userSupplierDTO.getAddTime());
            userSupplierModel.setUpdateTime(userSupplierDTO.getUpdateTime());

            if (userSupplierDTO.getCountryId() > 0)
            {
                MetaCountry country = this.metadataProvider.getCountry(userSupplierDTO.getCountryId());
                if (null != country)
                {
                    userSupplierModel.setCountryName(country.getName());
                }
            }
            if (userSupplierDTO.getProvinceId() > 0)
            {
                MetaProvince province = this.metadataProvider.getProvince(userSupplierDTO.getProvinceId());
                if (null != province)
                {
                    userSupplierModel.setProvinceName(province.getName());
                }
            }
            if (userSupplierDTO.getCityId() > 0)
            {
                MetaCity city = this.metadataProvider.getCity(userSupplierDTO.getCityId());
                if (null != city)
                {
                    userSupplierModel.setCityName(city.getName());
                }
            }

            if (userSupplierDTO.getDistrictId() > 0)
            {
                MetaArea area = this.metadataProvider.getArea(userSupplierDTO.getDistrictId());
                if (null != area)
                {
                    userSupplierModel.setDistrictName(area.getName());
                }
            }

            if(loadUserInfo){
                //加入用户基本信息
                loadUserInfo(userSupplierDTO, userSupplierModel);
            }

            if (loadEnterprise)
            {//加入用户企业认证信息
                loadEnterpriseInfo(userSupplierDTO, userSupplierModel);
            }



        }
        return userSupplierModel;
    }

    private void loadEnterpriseInfo(UserSupplierDTO userSupplierDTO, UserSupplierModel userSupplierModel)
    {
        UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userSupplierDTO.getUserId());
        if (userEnterpriseDTO != null)
        {
            EnterpriseVO enterpriseVO = new EnterpriseVO();
            BeanUtils.copyProperties(userEnterpriseDTO, enterpriseVO);
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

            userSupplierModel.setEnterprise(enterpriseVO);

        }
    }


    private void loadUserInfo(UserSupplierDTO userSupplierDTO, UserSupplierModel userSupplierModel)
    {
        UserAccountDTO userAccountDTO = userAccountService.loadById(userSupplierDTO.getUserId());
        if (userAccountDTO != null)
        {
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userAccountDTO, userModel);

            userSupplierModel.setUserInfo(userModel);
        }
    }



    public UserSupplierModel loadUserSupplierById(int supplierId, boolean loadUserInfo, boolean loadEnterprise)
    {
        UserSupplierModel userSupplierModel = null;
        if (supplierId > 0)
        {
            UserSupplierDTO userSupplierDTO = userSupplierService.loadById(supplierId);
            if (null != userSupplierDTO)
            {
                return loadUserSupplierModel(userSupplierDTO,loadUserInfo, loadEnterprise);
            }
        }
        return userSupplierModel;
    }



}
