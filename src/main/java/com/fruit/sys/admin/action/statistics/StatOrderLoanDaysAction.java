/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.statistics;

import com.fruit.statistics.biz.model.StatOrderLoanWeekModel;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.statistics.StatisticsOrderLoanService;
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
 * Create Date    : 2017-08-29
 * Project        : fruit
 * File Name      : StatOrderLoanDaysAction.java
 */
@Component
@UriMapping("/statistics")
public class StatOrderLoanDaysAction extends BaseAction
{

    @Autowired
    private StatisticsOrderLoanService statisticsOrderLoanService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StatOrderLoanDaysAction.class);


    @UriMapping("/statInfoIndex")
    public String statInfoIndex()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statInfoIndex].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_info_index";
    }

    /**
     * 订单贷款基础信息统计（周）
     * @return
     */
    @UriMapping("/statBaseWeeks")
    public String statBaseWeeks()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statBaseWeeks].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_weeks_base";
    }


    /**
     * 订单总额走向图（周）
     * @return
     */
    @UriMapping("/statOrderAmountWeeks")
    public String statOrderAmountWeeks()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statBaseWeeks].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_order_amount_weeks";
    }

    /**
     * 订单信贷周环比（周）
     * @return
     */
    @UriMapping("/statOrderLoanCycleRingWeeks")
    public String statOrderLoanCycleRingWeeks()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statOrderLoanCycleRingWeeks].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_cycle_ring_weeks";
    }


    /**
     * 信贷服务费（周）
     * @return
     */
    @UriMapping("/statLoanServiceChargeWeeks")
    public String statLoanServiceChargeWeeks()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statLoanServiceChargeWeeks].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_service_charge_weeks";
    }



    /**
     * 订单信贷综合统计（周）
     * @return
     */
    @UriMapping("/statOrderLoanComprehensiveWeeks")
    public String statOrderLoanComprehensiveWeeks()
    {
        try {

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/statOrderLoanComprehensiveWeeks].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }
        return "/statistics/stat_comprehensive_weeks";
    }



    
    /**
     * 查询周统计内容
     *
     * @return
     */
    @UriMapping(value = "/listStatOrderLoanOfWeeks")
    public AjaxResult listStatOrderLoanOfWeeks() {
    	int cycleNum = Integer.valueOf(super.getStringParameter("cycleNum"));
        String endTime = super.getStringParameter("endTime");

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList = null;
        StatOrderLoanWeekModel statOrderLoanWeekModel = null;
        BigDecimal maxOrderAmount = null;
        try {
            statOrderLoanWeekModelList = statisticsOrderLoanService.listStatOrderLoanOfWeeks(cycleNum,endTime);
            statOrderLoanWeekModel = statisticsOrderLoanService.getStatOrderLoanByTime(endTime);
            maxOrderAmount = statisticsOrderLoanService.getMaxOrderAmount(statOrderLoanWeekModelList);
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>(3);
            dataMap.put("statOrderLoanWeekModelList", statOrderLoanWeekModelList);
            dataMap.put("maxOrderAmount", maxOrderAmount);
            dataMap.put("statOrderLoanWeekModel", statOrderLoanWeekModel);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatOrderLoanOfWeeks].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 查询订单总额走向统计内容（周）
     *
     * @return
     */
    @UriMapping(value = "/listStatOrderAmountOfWeeks")
    public AjaxResult listStatOrderAmountOfWeeks() {
        int dayYear = super.getIntParameter("dayYear");//年份

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatOrderLoanWeekModel> statOrderLoanAmountList = null;
        BigDecimal maxOrderAmount = null;
        try {
            statOrderLoanAmountList = statisticsOrderLoanService.listStatOrderAmountOfWeeks(dayYear);
            maxOrderAmount = statisticsOrderLoanService.getMaxOrderAmount(statOrderLoanAmountList);
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>(2);
            dataMap.put("statOrderLoanAmountList", statOrderLoanAmountList);
            dataMap.put("maxOrderAmount", maxOrderAmount);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatOrderAmountOfWeeks].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 查询周统计内容(包含订单信贷周环比)
     *
     * @return
     */
    @UriMapping(value = "/listStatCycleRingOfWeeks")
    public AjaxResult listStatCycleRingOfWeeks() {
        int cycleNum = Integer.valueOf(super.getStringParameter("cycleNum"));
        String endTime = super.getStringParameter("endTime");

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList = null;
        BigDecimal maxOrderAmount = null;
        BigDecimal maxRatioValue = null;//最大周环比数
        BigDecimal minRatioValue = null;//最小周环比数
        try {
            statOrderLoanWeekModelList = statisticsOrderLoanService.listStatCycleRingOfWeeks(cycleNum,endTime);
            maxOrderAmount = statisticsOrderLoanService.getMaxOrderAmount(statOrderLoanWeekModelList);
            maxRatioValue = statisticsOrderLoanService.getMaxRationValue(statOrderLoanWeekModelList);
            minRatioValue = statisticsOrderLoanService.getMinRationValue(statOrderLoanWeekModelList);
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>(4);
            dataMap.put("statOrderLoanWeekModelList", statOrderLoanWeekModelList);
            dataMap.put("maxOrderAmount", maxOrderAmount);
            dataMap.put("maxRatioValue", maxRatioValue);
            dataMap.put("minRatioValue", minRatioValue);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatCycleRingOfWeeks].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 查询周统计内容(基础信息+服务费信息)
     *
     * @return
     */
    @UriMapping(value = "/listStatServiceFeeOfWeeks")
    public AjaxResult listStatServiceFeeOfWeeks() {
        int cycleNum = Integer.valueOf(super.getStringParameter("cycleNum"));
        String endTime = super.getStringParameter("endTime");

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList = null;
        BigDecimal maxOrderAmount = null;
        try {
            statOrderLoanWeekModelList = statisticsOrderLoanService.listStatOrderLoanOfWeeks(cycleNum,endTime);
            maxOrderAmount = statisticsOrderLoanService.getMaxOrderAmount(statOrderLoanWeekModelList);
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>(2);
            dataMap.put("statOrderLoanWeekModelList", statOrderLoanWeekModelList);
            dataMap.put("maxOrderAmount", maxOrderAmount);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatServiceFeeOfWeeks].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }


    /**
     * 查询周统计内容(包含订单信贷周环比)
     *
     * @return
     */
    @UriMapping(value = "/listStatComprehensiveOfWeeks")
    public AjaxResult listStatComprehensiveOfWeeks() {
        int cycleNum = Integer.valueOf(super.getStringParameter("cycleNum"));
        String endTime = super.getStringParameter("endTime");

        int code = AjaxResultCode.OK.getCode();
        String msg = "success";

        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList = null;
        BigDecimal maxOrderAmount = null;
        BigDecimal maxRatioValue = null;//最大周环比数
        BigDecimal minRatioValue = null;//最小周环比数
        try {
            statOrderLoanWeekModelList = statisticsOrderLoanService.listStatCycleRingOfWeeks(cycleNum,endTime);
            maxOrderAmount = statisticsOrderLoanService.getMaxOrderAmount(statOrderLoanWeekModelList);
            maxRatioValue = statisticsOrderLoanService.getMaxRationValue(statOrderLoanWeekModelList);
            minRatioValue = statisticsOrderLoanService.getMinRationValue(statOrderLoanWeekModelList);
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            Map<String, Object> dataMap = new HashMap<String, Object>(4);
            dataMap.put("statOrderLoanWeekModelList", statOrderLoanWeekModelList);
            dataMap.put("maxOrderAmount", maxOrderAmount);
            dataMap.put("maxRatioValue", maxRatioValue);
            dataMap.put("minRatioValue", minRatioValue);
            ajaxResult.setData(dataMap);

            return ajaxResult;
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/statistics/listStatComprehensiveOfWeeks].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }




}
