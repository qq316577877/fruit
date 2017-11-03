/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.credit;


import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.request.sys.SysUserCreditListRequest;
import com.fruit.loan.biz.service.sys.SysLoanUserCreditInfoService;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.loan.biz.socket.util.MathUtil;
import com.fruit.sys.admin.model.request.UserCreditInfoQueryRequest;
import com.fruit.sys.admin.model.user.credit.CreditCollectModel;
import com.fruit.sys.admin.model.user.credit.UserCreditModel;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.DateUtil;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
public class UserCreditListService extends BaseService
{
    @Autowired
    private SysLoanUserCreditInfoService sysloanUserCreditInfoService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private EcontractService econtractService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public List<UserCreditModel> loadUserCreditList(UserCreditInfoQueryRequest queryRequest, int pageNo, int pageSize)
    {
        SysUserCreditListRequest request = this.transfer(queryRequest);

        List<UserCreditModel> result = Collections.emptyList();

        List<LoanUserCreditInfoDTO> loanUserCreditInfoDTOs = sysloanUserCreditInfoService.searchByKeyword(request, pageNo, pageSize);

        if (CollectionUtils.isNotEmpty(loanUserCreditInfoDTOs))
        {
            List<UserCreditModel> userCreditModelList = this.getCreditUserModelLists(loanUserCreditInfoDTOs);
            result = userCreditModelList;
        }

        return result;
    }

    private List<UserCreditModel> getCreditUserModelLists(List<LoanUserCreditInfoDTO> loanUserCreditInfoDTOs)
    {
        List<FutureTask<UserCreditModel>> tasks = new ArrayList<FutureTask<UserCreditModel>>(loanUserCreditInfoDTOs.size());

        List<UserCreditModel> userCreditList = new ArrayList<UserCreditModel>(loanUserCreditInfoDTOs.size());

        if (CollectionUtils.isNotEmpty(loanUserCreditInfoDTOs))
        {
            for (LoanUserCreditInfoDTO userCredit : loanUserCreditInfoDTOs)
            {
                FutureTask<UserCreditModel> futureTask = new FutureTask(new UserCreditModelCallable(userCredit));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<UserCreditModel> task : tasks)
            {
                try
                {
                    UserCreditModel userCredit = task.get();
                    userCreditList.add(userCredit);
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
        return userCreditList;
    }




    class UserCreditModelCallable implements Callable<UserCreditModel>
    {
        private LoanUserCreditInfoDTO userCredit;

        public UserCreditModelCallable(LoanUserCreditInfoDTO userCredit)
        {
            this.userCredit = userCredit;
        }

        @Override
        public UserCreditModel call() throws Exception
        {
            try {
                return loadUserCreditModel(userCredit);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 根据id获取用户申请信息
     * @param userCreditId
     * @return UserCreditModel
     */
    public UserCreditModel loadUserCreditModel(int userCreditId)
    {
        UserCreditModel userCreditModel = null;
        if (userCreditId > 0)
        {
                LoanUserCreditInfoDTO loanUserCreditInfoDTO = sysloanUserCreditInfoService.loadById(userCreditId);
            if (null != loanUserCreditInfoDTO)
            {
                return loadUserCreditModel (loanUserCreditInfoDTO);
            }
        }
        return userCreditModel;
    }

    protected UserCreditModel loadUserCreditModel(LoanUserCreditInfoDTO loanUserCreditInfoDTO)
    {
        UserCreditModel userCreditModel = null;
        if (loanUserCreditInfoDTO != null)
        {
            userCreditModel = new UserCreditModel();

            //将loanUserCreditInfoDTO的属性copy到userCreditModel
            BeanUtils.copyProperties(loanUserCreditInfoDTO, userCreditModel);

            userCreditModel.setAddTimeString(DateUtil.DateToString(loanUserCreditInfoDTO.getAddTime(), DateStyle.YYYY_MM_DD));
            userCreditModel.setUpdateTimeString(DateUtil.DateToString(loanUserCreditInfoDTO.getUpdateTime(), DateStyle.YYYY_MM_DD));
            userCreditModel.setExpireTimeString(DateUtil.DateToString(loanUserCreditInfoDTO.getExpireTime(), DateStyle.YYYY_MM_DD));

            // 获取合同ID 查询合同URL
            Long contractId =loanUserCreditInfoDTO.getContractId();
            //dubbo接口查询合同URL地址
            String contractUrl = "";
            if(contractId > 0) {
                String source = envService.getConfig("contract.source");
                ResponseVo response = econtractService.queryContractUrlById(new QueryEContractBean(contractId,source));//
                if (response.isSuccess()) {
                    contractUrl = (String) response.getData();
                }
            }
            //
            userCreditModel.setContractUrl(contractUrl);

            BigDecimal expenditure = userCreditModel.getCreditLine().subtract(userCreditModel.getBalance());
            userCreditModel.setExpenditure(expenditure);

        }
        return userCreditModel;
    }

    private SysUserCreditListRequest transfer(UserCreditInfoQueryRequest queryRequest)
    {
        SysUserCreditListRequest request = new SysUserCreditListRequest();
        try
        {
            org.apache.commons.beanutils.BeanUtils.copyProperties(request, queryRequest);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return request;
    }

    /**
     * 计算总授信、已用贷款、可用贷款
     * @return
     */
    public CreditCollectModel loadLoanCollectModel(List<Integer> statusList) {
        String creditAmountString =
                runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.FRUIT_CAN_LOAN_AMOUNT);    //授信总额
        BigDecimal creditAmount = MathUtil.stringToBigDecima(creditAmountString);//平台总授信额度
        BigDecimal creditLine = new BigDecimal("0");    //授信额度
        BigDecimal creditBlanace = new BigDecimal("0");    //可用额度

        CreditCollectModel creditCollectModel = new CreditCollectModel();

       // List<LoanUserCreditInfoDTO> creditDTOs = sysloanUserCreditInfoService.searchAmount(status); 取消查询
        BigDecimal  searchCreditLine = sysloanUserCreditInfoService.sumCreditLine(statusList);
        BigDecimal  searchCreditBlanace = sysloanUserCreditInfoService.sumBalance(statusList);

        //平台已用贷款额度
        if(searchCreditLine != null){
            creditLine = creditLine.add(searchCreditLine);
        }
        if(searchCreditBlanace !=null){
            creditBlanace = creditBlanace.add(searchCreditBlanace);
        }

        //总贷款额度 默认
        creditCollectModel.setCreditAmount(creditAmount);
        //平台已用贷款额度
        BigDecimal creditUsed = creditLine.subtract(creditBlanace);
        creditCollectModel.setExpenditureTotal(creditUsed);
        //平台可用贷款额度
        creditCollectModel.setBalanceTotal(creditAmount.subtract(creditUsed));


        return creditCollectModel;

    }


}
