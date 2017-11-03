/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.configure;

import com.fruit.base.biz.common.DBStatusEnum;
import com.fruit.base.biz.common.EnterpriseTypeEnum;
import com.fruit.base.biz.common.LocationTypeEnum;
import com.fruit.base.biz.dto.EnterpriseInfoDTO;
import com.fruit.base.biz.request.EnterpriseInfoRequest;
import com.fruit.base.biz.request.sys.SysEnterpriseInfoListRequest;
import com.fruit.base.biz.service.EnterpriseInfoService;
import com.fruit.base.biz.service.sys.SysEnterpriseInfoService;
import com.fruit.sys.admin.model.configure.EnterpriseInfoModel;
import com.fruit.sys.admin.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-06-03
 * Project        : fruit
 * File Name      : EnterpriseInfoListService.java
 */
@Service
public class EnterpriseInfoListService extends BaseService
{

    @Autowired
    private SysEnterpriseInfoService sysEnterpriseInfoService;

    @Autowired
    private EnterpriseInfoService enterpriseInfoService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    private final static String NATIONAL_LOGISTICS = "nationalLogistics";

    private final static String INTERNATIONAL_LOGISTICS = "internationalLogistics";

    private final static String NATIONAL_CLEARANCE = "nationalClearance";

    private final static String INTERNATIONAL_CLEARANCE = "internationalClearance";


    public List<EnterpriseInfoModel> loadEnterpriseInfoList(SysEnterpriseInfoListRequest queryRequest, int pageNo, int pageSize)
    {

        List<EnterpriseInfoModel> result = Collections.emptyList();

        List<EnterpriseInfoDTO> enterpriseInfoDTOs = sysEnterpriseInfoService.searchByKeyword(queryRequest, pageNo, pageSize);


        if (CollectionUtils.isNotEmpty(enterpriseInfoDTOs))
        {
            List<EnterpriseInfoModel> enterpriseInfoModelList = this.getEnterpriseInfoModeLists(enterpriseInfoDTOs);
            result = enterpriseInfoModelList;
        }



        return result;
    }



    private List<EnterpriseInfoModel> getEnterpriseInfoModeLists(List<EnterpriseInfoDTO> enterpriseInfoDTOs)
    {
        List<FutureTask<EnterpriseInfoModel>> tasks = new ArrayList<FutureTask<EnterpriseInfoModel>>(enterpriseInfoDTOs.size());

        List<EnterpriseInfoModel> enterpriseInfoModelList = new ArrayList<EnterpriseInfoModel>(enterpriseInfoDTOs.size());
        if (CollectionUtils.isNotEmpty(enterpriseInfoDTOs))
        {
            for (EnterpriseInfoDTO enterpriseInfo : enterpriseInfoDTOs)
            {
                FutureTask<EnterpriseInfoModel> futureTask = new FutureTask(new EnterpriseInfoModelCallable(enterpriseInfo));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<EnterpriseInfoModel> task : tasks)
            {
                try
                {
                    EnterpriseInfoModel enterpriseInfoModel = task.get();
                    enterpriseInfoModelList.add(enterpriseInfoModel);
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
        return enterpriseInfoModelList;
    }

    class EnterpriseInfoModelCallable implements Callable<EnterpriseInfoModel>
    {
        private EnterpriseInfoDTO enterpriseInfo;

        public EnterpriseInfoModelCallable(EnterpriseInfoDTO enterpriseInfo)
        {
            this.enterpriseInfo = enterpriseInfo;
        }

        @Override
        public EnterpriseInfoModel call() throws Exception
        {
            try {
                return loadEnterpriseInfoModel(enterpriseInfo);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }



    /**
     * 加载信息
     * @param enterpriseInfoDTO
     * @return
     */
    protected EnterpriseInfoModel loadEnterpriseInfoModel(EnterpriseInfoDTO enterpriseInfoDTO)
    {
        EnterpriseInfoModel enterpriseInfoModel = null;
        if (enterpriseInfoDTO != null)
        {
            enterpriseInfoModel = new EnterpriseInfoModel();

            BeanUtils.copyProperties(enterpriseInfoDTO, enterpriseInfoModel);


            DBStatusEnum statusEnum = DBStatusEnum.get(enterpriseInfoDTO.getStatus());
            if (null != statusEnum)
            {
                enterpriseInfoModel.setStatusDesc(statusEnum.getMessage());
            }

            EnterpriseTypeEnum typeEnum = EnterpriseTypeEnum.get(enterpriseInfoDTO.getType());
            if (null != typeEnum)
            {
                enterpriseInfoModel.setTypeDesc(typeEnum.getMessage());
            }


            LocationTypeEnum locationTypeEnum = LocationTypeEnum.get(enterpriseInfoDTO.getLocationType());
            if (null != locationTypeEnum)
            {
                enterpriseInfoModel.setLocationTypeDesc(locationTypeEnum.getMessage());
            }

        }
        return enterpriseInfoModel;
    }


    public EnterpriseInfoDTO loadEnterpriseInfoById(int id)
    {
        EnterpriseInfoDTO enterpriseInfoDTO = null;
        if (id > 0)
        {
            enterpriseInfoDTO = enterpriseInfoService.loadById(id);
        }

        return enterpriseInfoDTO;
    }


    public EnterpriseInfoModel loadEnterpriseModelById(int id)
    {
        EnterpriseInfoModel enterpriseInfoModel = null;
        if (id > 0)
        {
            EnterpriseInfoDTO enterpriseInfoDTO = enterpriseInfoService.loadById(id);
            enterpriseInfoModel = loadEnterpriseInfoModel(enterpriseInfoDTO);
        }

        return enterpriseInfoModel;
    }

    /**
     * 验证是否有业务重复的公司出现
     * @param enterpriseInfoRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int countEnterpriseInfo(EnterpriseInfoRequest enterpriseInfoRequest)
    {

        int count = 0;
        if(enterpriseInfoRequest!=null){
            count  = enterpriseInfoService.countByExample(enterpriseInfoRequest);
        }

        return count;
    }


    @Transactional(rollbackFor = Exception.class)
    public int createEnterpriseInfo(EnterpriseInfoRequest enterpriseInfoRequest)
    {

        int id = 0;
        if(enterpriseInfoRequest!=null){
            id  = sysEnterpriseInfoService.create(enterpriseInfoRequest);
        }

        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteEnterpriseInfos(List<Integer> ids)
    {
        Validate.notEmpty(ids);

        return sysEnterpriseInfoService.delete(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEnterpriseInfo(EnterpriseInfoRequest enterpriseInfoRequest)
    {
        if(enterpriseInfoRequest!=null){
//            enterpriseInfoService.update(enterpriseInfo);//此方法会同时修改status
            sysEnterpriseInfoService.update(enterpriseInfoRequest);
        }
    }

    public Map<String, String> getEnterpriseListBySelector(String key){
        List<EnterpriseInfoDTO> enterpriseInfoDTOs = null;
        Map<String, String> enterpriseListBySelector = null;

        //国内-物流
        if(StringUtils.equals(key,NATIONAL_LOGISTICS)){
            enterpriseInfoDTOs = enterpriseInfoService.listByType(EnterpriseTypeEnum.LOGISTICS_COMPANY.getType(),LocationTypeEnum.INTERNAL.getType());
        }

        //国际-物流
        if(StringUtils.equals(key,INTERNATIONAL_LOGISTICS)){
            enterpriseInfoDTOs = enterpriseInfoService.listByType(EnterpriseTypeEnum.LOGISTICS_COMPANY.getType(),LocationTypeEnum.INTERNATIONAL.getType());
        }

        //国内-清关
        if(StringUtils.equals(key,NATIONAL_CLEARANCE)){
            enterpriseInfoDTOs = enterpriseInfoService.listByType(EnterpriseTypeEnum.CUSTOMS_CLEARANCE_COMPANY.getType(),LocationTypeEnum.INTERNAL.getType());
        }


        //国际-清关
        if(StringUtils.equals(key,INTERNATIONAL_CLEARANCE)){
            enterpriseInfoDTOs = enterpriseInfoService.listByType(EnterpriseTypeEnum.CUSTOMS_CLEARANCE_COMPANY.getType(),LocationTypeEnum.INTERNATIONAL.getType());
        }

        enterpriseListBySelector = getEnterpriseListByDTOs(enterpriseInfoDTOs);

        return enterpriseListBySelector;
    }


    private Map<String, String> getEnterpriseListByDTOs(List<EnterpriseInfoDTO> enterpriseInfoDTOs){
        Map<String, String> enterpriseListBySelector = new LinkedHashMap<String, String>();

        if(CollectionUtils.isNotEmpty(enterpriseInfoDTOs))
        {
            for(EnterpriseInfoDTO enterpriseInfo: enterpriseInfoDTOs)
            {
                enterpriseListBySelector.put(String.valueOf(enterpriseInfo.getId()),enterpriseInfo.getName());
            }
        }

        return enterpriseListBySelector;
    }

}
