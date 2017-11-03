/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.statistics;

import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.loan.biz.socket.util.MathUtil;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.statistics.biz.dto.StatLoanableBalanceDaysDTO;
import com.fruit.statistics.biz.dto.StatOrderLoanDaysDTO;
import com.fruit.statistics.biz.model.StatLoanDayModel;
import com.fruit.statistics.biz.model.StatLoanableBalanceDayModel;
import com.fruit.statistics.biz.model.StatOrderDayModel;
import com.fruit.statistics.biz.model.StatOrderLoanWeekModel;
import com.fruit.statistics.biz.service.StatLoanableBalanceDaysService;
import com.fruit.statistics.biz.service.StatOrderLoanDaysService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.DateUtil;
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
 * Create Date    : 2017-09-04
 * Project        : fruit
 * File Name      : StatisticsLoanableService.java
 */
@Service
public class StatisticsLoanableService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsLoanableService.class);

    private static final int STATISTICS_DAYS_BASE_CYCLES = 30;//统计截止时间前30天的

    private static final int STAT_OUT_USER_ID = 10000;//正式环境排除统计的用户--id在10000名之前的不统计

    @Autowired
    private StatLoanableBalanceDaysService statLoanableBalanceDaysService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired(required = false)
    private List<EventPublisher> eventPublishers;




    /**
     * 查询每日可贷余额统计
     *
     * @param beginTime 统计开始时间
     * @param endTime 统计截止时间
     * @return
     */
    public List<StatLoanableBalanceDayModel> listStatLoanableBalanceDays(String beginTime, String endTime){
        Date endDate = null;
        if(StringUtils.isEmpty(endTime)){
            endTime = DateUtil.getYesterdayDate();
        }
        endDate = DateUtil.StringToDate(endTime,DateStyle.YYYY_MM_DD);

        Date beginDate = null;
        if(StringUtils.isEmpty(beginTime)){
            beginDate = DateUtil.addDay(endDate,-STATISTICS_DAYS_BASE_CYCLES);
        }else{
            beginDate = DateUtil.StringToDate(beginTime,DateStyle.YYYY_MM_DD);
        }

        List<StatLoanableBalanceDayModel> statOrderLoanWeekModelList= statLoanableBalanceDaysService.getStatisticByTimeCycle(beginDate,endDate);

        return statOrderLoanWeekModelList;
    }

    /**
     * 定时任务统计每日信贷可贷余额数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void statisticsLoanableBalanceEveryDay() {
        Date beginDate = new Date();
        LOGGER.info("进入任务statisticsLoanableBalanceEveryDay===========,开始统计昨日数据，开始时间：{}",beginDate);

        try {
            //获取昨天的时间
            Date today = new Date();
//            Date today =  DateUtil.StringToDate("2017-08-26",DateStyle.YYYY_MM_DD);//test
            Date yesterday = DateUtil.addDay(today,-1);

            int outUserId = 0;
            if(EnvService.getEnv().equals("product")){//如果是正式环境，排除的是10000以上的
                outUserId = STAT_OUT_USER_ID;
            }

            BigDecimal creditAmount = getNowLoanCreditAmount();//平台当前配置的最高可贷额度

            //昨日已贷额度
            StatLoanableBalanceDayModel statLoanableBalanceDayModel = statLoanableBalanceDaysService.getLoanableBalanceByDate(yesterday,outUserId);

            //设置插入数据
            String statTimeString = DateUtil.DateToString(yesterday,DateStyle.YYYY_MM_DD);
            Date statTime =  DateUtil.StringToDate(statTimeString,DateStyle.YYYY_MM_DD);

            //先做删除
            statLoanableBalanceDaysService.delete(statTime);
            //再做插入
            BigDecimal usedAmount = statLoanableBalanceDayModel.getUsedAmount();
            BigDecimal loanableBalance = creditAmount.subtract(usedAmount);
            StatLoanableBalanceDaysDTO statLoanableBalanceDaysDTO = new StatLoanableBalanceDaysDTO();
            statLoanableBalanceDaysDTO.setStatTime(statTime);
            statLoanableBalanceDaysDTO.setTotalAmount(creditAmount);
            statLoanableBalanceDaysDTO.setLoanableBalance(loanableBalance);
            statLoanableBalanceDaysDTO.setUsedAmount(usedAmount);

            statLoanableBalanceDaysService.create(statLoanableBalanceDaysDTO);
        }catch(Exception e){
            LOGGER.error("loanIsScanPreparePay job error.{}",e);
            //异常告警
            EventHelper.triggerEvent(this.eventPublishers, "statisticsLoanableBalanceEveryDay." + e.getMessage(),
                    "error when statisticsLoanableBalanceEveryDay " , EventLevel.IMPORTANT, e,
                    ServerIpUtils.getServerIp());
        }

        Date endDate = new Date();
        LOGGER.info("完成任务statisticsLoanableBalanceEveryDay===========,完成时间：{}",endDate);
    }


    /**
     * 获取现在平台的最高信贷额度
     * @return
     */
    public BigDecimal getNowLoanCreditAmount(){
        //获取昨日配置的平台总可贷额度
        String creditAmountString =
                runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.FRUIT_CAN_LOAN_AMOUNT);    //授信总额
        BigDecimal creditAmount = MathUtil.stringToBigDecima(creditAmountString);//平台总授信额度
        return creditAmount;
    }

}
