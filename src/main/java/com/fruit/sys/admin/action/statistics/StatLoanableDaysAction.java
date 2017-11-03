/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.statistics;

import com.fruit.statistics.biz.model.StatLoanableBalanceDayModel;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.statistics.StatisticsLoanableService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-09-04
 * Project        : fruit
 * File Name      : StatLoanableDaysAction.java
 */
@Component
@UriMapping("/statistics")
public class StatLoanableDaysAction extends BaseAction
{

    @Autowired
    private StatisticsLoanableService statisticsLoanableService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StatLoanableDaysAction.class);


    /**
     * 每日可贷余额图（日）
     * @return
     */
    @UriMapping("/statLoanLimitDays")
    public String statLoanLimitDays()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statLoanLimitDays].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_loan_limit_days";
    }


    /**
     * 查询订单总额走向统计内容（周）
     *
     * @return
     */
    @UriMapping(value = "/listStatLoanableBalanceDays")
    public AjaxResult listStatLoanableBalanceDays() {
        String beginTime = super.getStringParameter("beginTime");//统计开始时间
        String endTime = super.getStringParameter("endTime");//统计结束时间

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatLoanableBalanceDayModel> statLoanableBalanceList = null;

        try {
            statLoanableBalanceList = statisticsLoanableService.listStatLoanableBalanceDays(beginTime,endTime);
            BigDecimal loanTotalAmount = statisticsLoanableService.getNowLoanCreditAmount();

            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("statLoanableBalanceList", statLoanableBalanceList);
            dataMap.put("loanTotalAmount", loanTotalAmount);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatLoanableBalanceDays].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }




}
