/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.statistics;

import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.statistics.biz.dto.StatOrderLoanDaysDTO;
import com.fruit.statistics.biz.model.StatLoanDayModel;
import com.fruit.statistics.biz.model.StatOrderDayModel;
import com.fruit.statistics.biz.model.StatOrderLoanWeekModel;
import com.fruit.statistics.biz.service.StatOrderLoanDaysService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.MathUtil;
import com.ovfintech.arch.common.event.EventHelper;
import com.ovfintech.arch.common.event.EventLevel;
import com.ovfintech.arch.common.event.EventPublisher;
import com.ovfintech.arch.utils.ServerIpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-08-29
 * Project        : fruit
 * File Name      : StatisticsOrderLoanService.java
 */
@Service
public class StatisticsOrderLoanService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsOrderLoanService.class);

    private static final int DAYS_PER_WEEK = 7;//一周天数

    private static final int STATISTICS_WEEK_BASE_CYCLES = 8;//统计截止时间前8周的

    private static final int STAT_BEGIN_YEAR = 2017;//统计开始年份

    private static final String STAT_BEGIN_DATE = "2017-07-15";

    //查询订单总数排除在外的订单状态:用户取消订单  平台取消订单  订单关闭(101,102,110)
    private static final List<Integer> ORDER_STAT_OUT_STATUS_LIST = new ArrayList<Integer>(3);
    static
    {
        ORDER_STAT_OUT_STATUS_LIST.add(OrderStatusEnum.USER_CANCELED.getStatus());//用户取消订单
        ORDER_STAT_OUT_STATUS_LIST.add(OrderStatusEnum.SYS_CANCELED.getStatus());//平台取消订单
        ORDER_STAT_OUT_STATUS_LIST.add(OrderStatusEnum.SYSTEM_CLOSED.getStatus());//订单关闭
    }

    //查询订单总数排除在外的订单状态---新订单:订单关闭(101)
    private static final List<Integer> NEW_ORDER_STAT_OUT_STATUS_LIST = new ArrayList<Integer>(1);
    static
    {
        NEW_ORDER_STAT_OUT_STATUS_LIST.add(com.fruit.newOrder.biz.common.OrderStatusEnum.CLOSED.getStatus());//订单关闭
    }


    //查询借据总数的借据状态:已放款  待还款  已还款 保证金还款成功 保证金还款失败 对账-待确认
    private static final List<Integer> LOAN_STAT_OUT_STATUS_LIST = new ArrayList<Integer>(6);
    static
    {
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());//已放款
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT.getStatus());//待还款
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());//已还款
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());//保证金还款成功
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());//保证金还款失败
        LOAN_STAT_OUT_STATUS_LIST.add(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus());//对账-待确认
    }


    //查询利息总金额的借据状态:已还款 保证金还款成功
    private static final List<Integer> LOAN_STAT_INTEREST_STATUS_LIST = new ArrayList<Integer>(2);
    static
    {
        LOAN_STAT_INTEREST_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());//已还款
        LOAN_STAT_INTEREST_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());//保证金还款成功
    }

    private static final int STAT_OUT_USER_ID = 10000;//正式环境排除统计的用户--id在10000名之前的不统计

    @Autowired
    private StatOrderLoanDaysService statOrderLoanDaysService;

    @Autowired(required = false)
    private List<EventPublisher> eventPublishers;




    /**
     * 查询周统计内容
     * 以统计截止时间（statTimeEnd）为基准，往前统计（cycleNum）个周期
     * @param cycleNum 统计周期数
     * @param endTime 统计截止时间
     * @return
     */
    public List<StatOrderLoanWeekModel> listStatOrderLoanOfWeeks(int cycleNum, String endTime){
        if(cycleNum<=0){
            cycleNum = STATISTICS_WEEK_BASE_CYCLES;
        }

        Date endDate = null;
        if(StringUtils.isNotEmpty(endTime)){
            endDate = DateUtil.StringToDate(endTime, DateStyle.YYYY_MM_DD);
        }else{
            endDate = new Date();
        }

        //根据截止时间获取当前周的周一和周日
        Date thisWeekMonday = DateUtil.getMonDayByDate(endDate);//周一
        Date thisWeekSunday = DateUtil.getSunDayByDate(endDate);//周日

        Date statTimeBegin = null;
        Date statTimeEnd = null;
        StatOrderLoanWeekModel statOrderLoanWeekModel = null;
        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList= new ArrayList<StatOrderLoanWeekModel>(cycleNum);
        for(int i=cycleNum-1;i>=0;--i){
            statTimeBegin = DateUtil.addDay(thisWeekMonday,-i*DAYS_PER_WEEK);
            statTimeEnd = DateUtil.addDay(thisWeekSunday,-i*DAYS_PER_WEEK);
            statOrderLoanWeekModel = statOrderLoanDaysService.getStatisticByTimeCycle(statTimeBegin,statTimeEnd);
            statOrderLoanWeekModelList.add(statOrderLoanWeekModel);
        }

        return statOrderLoanWeekModelList;
    }


    /**
     * 获取最大订单总额
     * @param statOrderLoanWeekModelList
     * @return
     */
    public BigDecimal getMaxOrderAmount(List<StatOrderLoanWeekModel> statOrderLoanWeekModelList){
        BigDecimal retMaxValue = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(statOrderLoanWeekModelList))
        {
            for(StatOrderLoanWeekModel statOrderLoanWeekModel: statOrderLoanWeekModelList){
                if(retMaxValue.compareTo(statOrderLoanWeekModel.getOrderAmount())==-1){
                    retMaxValue = statOrderLoanWeekModel.getOrderAmount();
                }
            }
        }

        return retMaxValue;
    }

    /**
     * 获取最大周环比数
     * @param statOrderLoanWeekModelList
     * @return
     */
    public BigDecimal getMaxRationValue(List<StatOrderLoanWeekModel> statOrderLoanWeekModelList){
        BigDecimal retMaxValue = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(statOrderLoanWeekModelList))
        {
            for(StatOrderLoanWeekModel statOrderLoanWeekModel: statOrderLoanWeekModelList){
                if(retMaxValue.compareTo(statOrderLoanWeekModel.getOrderAmountWow())==-1){
                    retMaxValue = statOrderLoanWeekModel.getOrderAmountWow();
                }

                if(retMaxValue.compareTo(statOrderLoanWeekModel.getLoanAmountWow())==-1){
                    retMaxValue = statOrderLoanWeekModel.getLoanAmountWow();
                }
            }
        }

        return retMaxValue;
    }

    /**
     * 获取最小周环比数
     * @param statOrderLoanWeekModelList
     * @return
     */
    public BigDecimal getMinRationValue(List<StatOrderLoanWeekModel> statOrderLoanWeekModelList){
        BigDecimal retMinValue = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(statOrderLoanWeekModelList))
        {
            for(StatOrderLoanWeekModel statOrderLoanWeekModel: statOrderLoanWeekModelList){
                if(retMinValue.compareTo(statOrderLoanWeekModel.getOrderAmountWow())==1){
                    retMinValue = statOrderLoanWeekModel.getOrderAmountWow();
                }

                if(retMinValue.compareTo(statOrderLoanWeekModel.getLoanAmountWow())==1){
                    retMinValue = statOrderLoanWeekModel.getLoanAmountWow();
                }
            }
        }

        return retMinValue;
    }


    /**
     * 定时任务统计每日订单、信贷数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void statisticsOrderAndLoanEveryDay() {
        Date beginDate = new Date();
        LOGGER.info("进入任务statisticsOrderAndLoanEveryDay===========,开始统计昨日数据，开始时间：{}",beginDate);

        try {
            //获取昨天的时间
            Date today = new Date();
//            Date today =  DateUtil.StringToDate("2017-09-20",DateStyle.YYYY_MM_DD);//test
            Date yesterday = DateUtil.addDay(today,-1);

            int outUserId = 0;
            if(EnvService.getEnv().equals("product")){//如果是正式环境，排除的是10000以上的
                outUserId = STAT_OUT_USER_ID;
            }
            //昨日订单数、订单总额---老订单
            StatOrderDayModel statOrderDayModel = statOrderLoanDaysService.getOrderStatDayByTime(yesterday,ORDER_STAT_OUT_STATUS_LIST,outUserId);
            //昨日订单数、订单总额---新订单
            StatOrderDayModel statNewOrderDayModel = statOrderLoanDaysService.getNewOrderStatDayByTime(yesterday,NEW_ORDER_STAT_OUT_STATUS_LIST,outUserId);
            int orderCount = 0;//订单数
            BigDecimal orderAmount = new BigDecimal("0.00");//订单总额
            orderCount = statOrderDayModel.getOrderCount() + statNewOrderDayModel.getOrderCount();
            orderAmount = statOrderDayModel.getOrderAmount().add(statNewOrderDayModel.getOrderAmount());

            //昨日借据数、借据总额
            StatLoanDayModel statLoanDayModel = statOrderLoanDaysService.getLoanStatDayByTime(yesterday,LOAN_STAT_OUT_STATUS_LIST,outUserId);

            //昨日利息总额
            StatLoanDayModel statLoanInterestDayModel = statOrderLoanDaysService.getLoanInterestDayByTime(yesterday,LOAN_STAT_INTEREST_STATUS_LIST,outUserId);

            //设置插入数据
            String statTimeString = DateUtil.DateToString(yesterday,DateStyle.YYYY_MM_DD);
            Date statTime =  DateUtil.StringToDate(statTimeString,DateStyle.YYYY_MM_DD);
            //先做删除
            statOrderLoanDaysService.delete(statTime);
            //再做插入
            StatOrderLoanDaysDTO statOrderLoanDaysDTO = new StatOrderLoanDaysDTO();
            statOrderLoanDaysDTO.setStatTime(statTime);
            statOrderLoanDaysDTO.setOrderCount(orderCount);//新老订单和  订单笔数
            statOrderLoanDaysDTO.setOrderAmount(orderAmount);//新老订单和  订单总额
            statOrderLoanDaysDTO.setLoanCount(statLoanDayModel.getLoanCount());
            statOrderLoanDaysDTO.setLoanAmount(statLoanDayModel.getLoanAmount());
            statOrderLoanDaysDTO.setLoanServiceFee(statLoanDayModel.getLoanServiceFee());
            statOrderLoanDaysDTO.setLoanInterest(statLoanInterestDayModel.getLoanInterest());//利息总额

            statOrderLoanDaysService.create(statOrderLoanDaysDTO);
        }catch(Exception e){
            LOGGER.error("loanIsScanPreparePay job error.{}",e);
            //异常告警
            EventHelper.triggerEvent(this.eventPublishers, "statisticsOrderAndLoanEveryDay." + e.getMessage(),
                    "error when statisticsOrderAndLoanEveryDay " , EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }

        Date endDate = new Date();
        LOGGER.info("完成任务statisticsOrderAndLoanEveryDay===========,完成时间：{}",endDate);
    }



    /**
     * 查询订单金额走向统计内容
     * @param dayYear 统计一整年的订单金额（周）
     * @return
     */
    public List<StatOrderLoanWeekModel> listStatOrderAmountOfWeeks(int dayYear){
        if(dayYear<0){
            return null;
        }

        //获取一年的开始时间和结束时间
        Date beginDate = DateUtil.getYearBeginDate(dayYear);
        Date endDate = DateUtil.getYearEndDate(dayYear);
        Date yesterday = DateUtil.StringToDate(DateUtil.getYesterdayDate());
        if(beginDate.getTime()>yesterday.getTime()){//如果统计开始时间大于昨天，则统计开始时间定为2017-01-01
            beginDate = DateUtil.getYearBeginDate(STAT_BEGIN_YEAR);
        }
        if(endDate.getTime()>yesterday.getTime()){//如果统计结束时间大于昨天，则统计结束时间定为昨天
            endDate = yesterday;
        }
        int weeks = DateUtil.getWeekOfYear(dayYear);//此年的周数

        List<StatOrderLoanWeekModel> statOrderAmountList= new ArrayList<StatOrderLoanWeekModel>(weeks);

        //获取第一周的周日和最后一周的周一
        Date thisYearFirstSunday = DateUtil.getSunDayByDate(beginDate);//第一周周日
        thisYearFirstSunday = DateUtil.getDateEndDate(thisYearFirstSunday);//周日最后一秒
        Date thisYearLastMonday = DateUtil.getMonDayByDate(endDate);//最后一周周一
        thisYearLastMonday = DateUtil.getDateBeginDate(thisYearLastMonday);//周一第一秒

        Date statTimeBegin = beginDate;
        Date statTimeEnd = thisYearFirstSunday;
        StatOrderLoanWeekModel statOrderLoanWeekModel = statOrderLoanDaysService.getOrderAmountTotalByTimeCycle(beginDate,statTimeBegin,statTimeEnd);
        statOrderAmountList.add(statOrderLoanWeekModel);//第一周的统计

        //中间周统计
        while(statTimeEnd.getTime() < thisYearLastMonday.getTime()){
            statTimeBegin = DateUtil.getNextWeekMonDayByDate(statTimeBegin);//下周一
            statTimeEnd = DateUtil.getNextWeekSunDayByDate(statTimeEnd);//下周日
            if(statTimeEnd.getTime() < thisYearLastMonday.getTime()){
                statOrderLoanWeekModel = statOrderLoanDaysService.getOrderAmountTotalByTimeCycle(beginDate,statTimeBegin,statTimeEnd);
                statOrderAmountList.add(statOrderLoanWeekModel);
            }
        }

        //最后一周统计
        statOrderLoanWeekModel = statOrderLoanDaysService.getOrderAmountTotalByTimeCycle(beginDate,thisYearLastMonday,endDate);
        statOrderAmountList.add(statOrderLoanWeekModel);

        return statOrderAmountList;
    }


    /**
     * 查询周统计内容（包含周环比）
     * 以统计截止时间（statTimeEnd）为基准，往前统计（cycleNum）个周期
     * @param cycleNum 统计周期数
     * @param endTime 统计截止时间
     * @return
     */
    public List<StatOrderLoanWeekModel> listStatCycleRingOfWeeks(int cycleNum, String endTime){
        if(cycleNum<=0){
            cycleNum = STATISTICS_WEEK_BASE_CYCLES;
        }

        List<StatOrderLoanWeekModel> statOrderLoanWeekModelList = listStatOrderLoanOfWeeks(cycleNum+1,endTime);

        BigDecimal orderAmountWow;//订单金额周环比
        BigDecimal loanAmountWow;//贷款金额周环比
        BigDecimal loanServiceFeeWow;//服务费周环比

        StatOrderLoanWeekModel thisWeekModel = null;
        StatOrderLoanWeekModel lastWeekModel = null;
        List<StatOrderLoanWeekModel> statOrderLoanWeekModelReturnList = null;
        if(CollectionUtils.isNotEmpty(statOrderLoanWeekModelList)){
            statOrderLoanWeekModelReturnList = new ArrayList<StatOrderLoanWeekModel>(statOrderLoanWeekModelList.size());
            for (int i = 1;i<statOrderLoanWeekModelList.size();++i){
                thisWeekModel = statOrderLoanWeekModelList.get(i);
                lastWeekModel = statOrderLoanWeekModelList.get(i-1);
                loanAmountWow = calculationWowByValues(lastWeekModel.getLoanAmount(),thisWeekModel.getLoanAmount());
                orderAmountWow = calculationWowByValues(lastWeekModel.getOrderAmount(),thisWeekModel.getOrderAmount());
                loanServiceFeeWow = calculationWowByValues(lastWeekModel.getLoanServiceFee(),thisWeekModel.getLoanServiceFee());

                thisWeekModel.setLoanAmountWow(loanAmountWow);
                thisWeekModel.setOrderAmountWow(orderAmountWow);
                thisWeekModel.setLoanServiceFeeWow(loanServiceFeeWow);
                statOrderLoanWeekModelReturnList.add(thisWeekModel);
            }
        }
        return statOrderLoanWeekModelReturnList;
    }

    /**
     * 根据上周的值和本周的值，计算周环比
     * @param lastWeek
     * @param thisWeek
     * @return
     */
    private BigDecimal calculationWowByValues(BigDecimal lastWeek,BigDecimal thisWeek){
        BigDecimal wowValue = MathUtil.doubleToBigDecima(0.00);
        if(lastWeek!=null && thisWeek!=null && lastWeek.compareTo(BigDecimal.ZERO)!=0){
            BigDecimal incValue = thisWeek.subtract(lastWeek);//本周减上周
            wowValue = incValue.divide(lastWeek,10,BigDecimal.ROUND_HALF_UP);
        }//任何一个为空或为0，则周环比是0

        return wowValue;
    }


    /**
     * 查询截止statTimeEnd统计内容
     * 以统计截止时间（statTimeEnd）为基准
     * @param statTimeEnd 统计截止时间
     * @return
     */
    public StatOrderLoanWeekModel getStatOrderLoanByTime(String statTimeEnd){
        Date endDate = null;
        if(StringUtils.isNotEmpty(statTimeEnd)){
            endDate = DateUtil.StringToDate(statTimeEnd, DateStyle.YYYY_MM_DD);
        }else{
            endDate = DateUtil.addDay(new Date(),-1);
        }

        Date beginDate = DateUtil.StringToDate(STAT_BEGIN_DATE, DateStyle.YYYY_MM_DD);

        StatOrderLoanWeekModel statOrderLoanWeekModel = statOrderLoanDaysService.getStatisticByTimeCycle(beginDate,endDate);

        return statOrderLoanWeekModel;
    }





}
