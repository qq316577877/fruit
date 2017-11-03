/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */


package com.fruit.sys.admin.service.trade;

import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.common.OrderStepEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : OrderStepManger.java
 */
@Service
public class OrderStepManger
{
    /**
     * 常规流程
     */
    private final List<OrderStepEnum> NORMAL_STEPS = new ArrayList<OrderStepEnum>(10);

    /**
     * 买家不付款直接取消
     */
    private final List<OrderStepEnum> USER_CANCEL_STEPS = new ArrayList<OrderStepEnum>(10);

    /**
     * 平台取消
     */
    private final List<OrderStepEnum> SYS_CANCEL_STEPS = new ArrayList<OrderStepEnum>(10);

    public OrderStepManger()
    {
        NORMAL_STEPS.add(OrderStepEnum.SAVE);
        NORMAL_STEPS.add(OrderStepEnum.SUBMIT);
        NORMAL_STEPS.add(OrderStepEnum.SYS_CONFIRM);
        NORMAL_STEPS.add(OrderStepEnum.CONFIRM);
        NORMAL_STEPS.add(OrderStepEnum.SIGN_CONTRACT);
        NORMAL_STEPS.add(OrderStepEnum.PAY);
        NORMAL_STEPS.add(OrderStepEnum.SHIPPING);
        NORMAL_STEPS.add(OrderStepEnum.SETTLEMENT);
        NORMAL_STEPS.add(OrderStepEnum.RECEIVE);

        USER_CANCEL_STEPS.add(OrderStepEnum.SAVE);
        USER_CANCEL_STEPS.add(OrderStepEnum.SUBMIT);
        USER_CANCEL_STEPS.add(OrderStepEnum.SYS_CONFIRM);
        USER_CANCEL_STEPS.add(OrderStepEnum.CONFIRM);
        USER_CANCEL_STEPS.add(OrderStepEnum.USER_CANCEL);

        SYS_CANCEL_STEPS.add(OrderStepEnum.SUBMIT);
        SYS_CANCEL_STEPS.add(OrderStepEnum.SYS_CONFIRM);
        SYS_CANCEL_STEPS.add(OrderStepEnum.CONFIRM);
        SYS_CANCEL_STEPS.add(OrderStepEnum.SYS_CANCEL);

    }

    public List<OrderStepEnum> computeOrderSteps(int currentStatus, int previousStatus)
    {
        List<OrderStepEnum> steps = NORMAL_STEPS;
        OrderStatusEnum status = OrderStatusEnum.get(currentStatus);
        if (status != null)
        {
//            switch (status)
//            {
//                case REFUND_KU_MEDIATION:
//                case REFUND_APPLYING:
//                case REFUND_SELLER_ACCEPT:
//                    steps = REFUND_STEPS;
//                    break;
//                case BUYER_CANCELD:
//                    if (payed)
//                    {
//                        steps = BUYER_PAY_CANCEL_STEPS;
//                    }
//                    else
//                    {
//                        steps = BUYER_NONPAY_CANCEL_STEPS;
//                    }
//                    break;
//                case SELLER_CANCELD:
//                    steps = SELLER_CANCEL_STEPS;
//                    break;
//                case REFUND_SUCCESS:
//                    OrderStatusEnum previousStatusEnum = OrderStatusEnum.get(previousStatus);
//                    if (previousStatusEnum != null)
//                    {
//                        switch (previousStatusEnum)
//                        {
//                            case BUYER_CANCELD:
//                                steps = BUYER_PAY_CANCEL_STEPS;
//                                break;
//                            case SELLER_CANCELD:
//                                steps = SELLER_CANCEL_STEPS;
//                                break;
//                            case REFUND_APPLYING:
//                            case REFUND_SELLER_ACCEPT:
//                            case REFUND_KU_MEDIATION:
//                                steps = REFUND_STEPS;
//                                break;
//                        }
//                    }
//                    break;
//                case OFFLINE_APPLY:
//                case OFFLINE_SUCCESS:
//                    steps = OFFLINE_STEPS;
//                    break;
//                case SYSTEM_CLOSE:
//                    steps = SYSTEM_CLOSE_STEPS;
//                    break;
//                default:
//                    steps = NORMAL_STEPS;
//            }
        }
        return steps;
    }
}
