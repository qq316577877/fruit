/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.loan;


import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserApplyCreditDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.sys.SysLoanUserApplyCreditService;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.sys.admin.meta.*;
import com.fruit.sys.admin.model.user.Loan.LoanInfoModel;
import com.fruit.sys.admin.model.user.Loan.UserLoanModel;
import com.fruit.sys.admin.model.user.Loan.UserLoanVO;
import com.fruit.sys.admin.service.BaseService;

import com.fruit.sys.admin.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserLoanListService.java
 */
@Service
public class UserLoanListService extends BaseService
{
    @Autowired
    private SysLoanUserApplyCreditService sysloanUserApplyCreditService;
    
    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private MetadataProvider metadataProvider;


    /**
     * 根据id获取用户申请信息
     * @param userLoanId
     * @param loadEnterprise
     * @return
     */
    public UserLoanModel loadUserLoanModel(int userLoanId, boolean loadEnterprise)
    {
        UserLoanModel userLoanModel = null;
        if (userLoanId > 0)
        {
            LoanUserApplyCreditDTO loanUserApplyCreditDTO = sysloanUserApplyCreditService.loadById(userLoanId);
            if (null != loanUserApplyCreditDTO)
            {
                return loadUserLoanModel (loanUserApplyCreditDTO , loadEnterprise);
            }
        }
        return userLoanModel;
    }

    protected UserLoanModel loadUserLoanModel(LoanUserApplyCreditDTO loanUserApplyCreditDTO, boolean loadEnterprise)
    {
        UserLoanModel userLoanModel = null;
        if (loanUserApplyCreditDTO != null)
        {
            userLoanModel = new UserLoanModel();

            BeanUtils.copyProperties(loanUserApplyCreditDTO, userLoanModel);

            userLoanModel.setAddTimeString(DateUtil.DateToString(loanUserApplyCreditDTO.getAddTime(), DateStyle.YYYY_MM_DD));
            userLoanModel.setUpdateTimeString(DateUtil.DateToString(loanUserApplyCreditDTO.getUpdateTime(), DateStyle.YYYY_MM_DD));

        }
        loadLoanApplyInfo(loanUserApplyCreditDTO,userLoanModel);

        return userLoanModel;
    }

    /**
     * 获取地址信息
     * @param loanUserApplyCreditDTO
     * @param userLoanModel
     */
    private void loadLoanApplyInfo(LoanUserApplyCreditDTO loanUserApplyCreditDTO, UserLoanModel userLoanModel)
    {
        if (loanUserApplyCreditDTO != null)
        {

            UserLoanVO userLoanVO = new UserLoanVO();
            BeanUtils.copyProperties(loanUserApplyCreditDTO, userLoanVO);
            userLoanModel.setUserLoanVO(userLoanVO);

            if (loanUserApplyCreditDTO.getCountryId() > 0)
            {
                MetaCountry country = this.metadataProvider.getCountry(loanUserApplyCreditDTO.getCountryId());
                if (null != country)
                {
                    userLoanVO.setCountryName(country.getName());
                }
            }
            if (loanUserApplyCreditDTO.getProvinceId() > 0)
            {
                MetaProvince province = this.metadataProvider.getProvince(loanUserApplyCreditDTO.getProvinceId());
                if (null != province)
                {
                    userLoanVO.setProvinceName(province.getName());
                }
            }
            if (loanUserApplyCreditDTO.getCityId() > 0)
            {
                MetaCity city = this.metadataProvider.getCity(loanUserApplyCreditDTO.getCityId());
                if (null != city)
                {
                    userLoanVO.setCityName(city.getName());
                }
            }

            if (loanUserApplyCreditDTO.getDistrictId() > 0)
            {
                MetaArea area = this.metadataProvider.getArea(loanUserApplyCreditDTO.getDistrictId());
                if (null != area)
                {
                    userLoanVO.setDistrictName(area.getName());
                }
            }

        }
    }


    /**
     * 查询资金服务，通过TransactionNoList
     * @param transactionNo
     * @return
     */
    public LoanInfoModel loadLoanInfosByTransactionNo(String transactionNo)
    {
        LoanInfoModel loanInfoModel = null;
        LoanInfoDTO loanInfoDTO =  loanInfoService.loadByTransactionNo(transactionNo);

        if (null != loanInfoDTO)
        {
        	loanInfoModel = new LoanInfoModel();
        	
        	BeanUtils.copyProperties(loanInfoDTO, loanInfoModel);
        	LoanInfoStatusEnum statusEnum = LoanInfoStatusEnum.get(loanInfoDTO.getStatus());
        	loanInfoModel.setStatusDesc(statusEnum.getMessage());
        }

        return loanInfoModel;
    }


}
