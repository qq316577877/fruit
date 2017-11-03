/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.loan;

import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.user.Loan.LoanInfoModel;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.loan.UserLoanListService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserLoanInfoAction.java
 */
@Component
@UriMapping("/user/loan")
public class UserLoanInfoAction extends BaseAction
{

    @Autowired
    private UserLoanListService userLoanListService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoanInfoAction.class);

    
    /**
     * 查询资金服务信息
     * 通过流水号
     *
     * @return
     */
    @UriMapping(value = "/loadLoanInfosByTransactionNo")
    public AjaxResult loadLoanInfosByTransactionNo() {
    	String transactionNo = super.getStringParameter("transactionNo");
        Validate.notNull(transactionNo);

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        LoanInfoModel loanInfoModel = null;

        try {
            loanInfoModel = userLoanListService.loadLoanInfosByTransactionNo(transactionNo);

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("loanInfo", loanInfoModel);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/loan/info/loadLoanInfosByTransactionNo].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 查询我的资金服务年利率、月利率
     * @return
     */
    @UriMapping(value = "/get_loan_info_interest_rate_ajax")
    public AjaxResult getLoanInfoInterestRate()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            AjaxResult ajaxResult = new AjaxResult(code, msg);

            //年利率
            double yearInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE));
            //月利率
            double monthInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.MONTH_PERFORMANCE_RATE));

            dataMap.put("yearInterestRate", yearInterestRate);
            dataMap.put("monthInterestRate", monthInterestRate);
            ajaxResult.setData(dataMap);
            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/loan/info/get_loan_info_interest_rate].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }
}
