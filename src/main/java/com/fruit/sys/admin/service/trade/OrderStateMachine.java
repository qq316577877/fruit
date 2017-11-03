/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */


package com.fruit.sys.admin.service.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.sys.admin.model.order.OrderState;

/**
 * Description:    订单状态流转服务
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : OrderStateMachine.java
 */
@Service
public class OrderStateMachine implements InitializingBean
{
    private Map<OrderStatusEnum, OrderState> states = new HashMap<OrderStatusEnum, OrderState>();

    public OrderStatusEnum computeStatusForBuyer(int current, OrderEventEnum event)
    {
        OrderStatusEnum nextStatus = null;
        OrderStatusEnum currentStatus = OrderStatusEnum.get(current);
        if (currentStatus != null)
        {
            OrderState orderState = this.states.get(currentStatus);
            if (orderState != null)
            {
                Map<OrderEventEnum, OrderStatusEnum> events = orderState.getUserEvents();
                nextStatus = events.get(event);
            }
        }
        return nextStatus;
    }

    public OrderStatusEnum computeStatusForSystem(int current, OrderEventEnum event)
    {
        OrderStatusEnum nextStatus = null;
        OrderStatusEnum currentStatus = OrderStatusEnum.get(current);
        if (currentStatus != null)
        {
            OrderState orderState = this.states.get(currentStatus);
            if (orderState != null)
            {
                Map<OrderEventEnum, OrderStatusEnum> events = orderState.getSysEvents();
                nextStatus = events.get(event);
            }
        }
        return nextStatus;
    }

    public List<OrderEventEnum> computeUserOps(int current)
    {
        List<OrderEventEnum> events = new ArrayList<OrderEventEnum>();
        OrderStatusEnum currentStatus = OrderStatusEnum.get(current);
        if (currentStatus != null)
        {
            OrderState orderState = this.states.get(currentStatus);
            if (orderState != null)
            {
                events.addAll(orderState.getUserEvents().keySet());
            }
        }
        return events;
    }

    public List<OrderEventEnum> computeSysOps(int current)
    {
        List<OrderEventEnum> events = new ArrayList<OrderEventEnum>();
        OrderStatusEnum currentStatus = OrderStatusEnum.get(current);
        if (currentStatus != null)
        {
            OrderState orderState = this.states.get(currentStatus);
            if (orderState != null)
            {
                events.addAll(orderState.getSysEvents().keySet());
            }
        }
        return events;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        OrderState state = new OrderState(OrderStatusEnum.SAVED);
        state.getUserEvents().put(OrderEventEnum.SUBMIT, OrderStatusEnum.SUBMITED);
        state.getUserEvents().put(OrderEventEnum.USER_CANCEL, OrderStatusEnum.USER_CANCELED);
        state.getSysEvents().put(OrderEventEnum.SYS_CANCEL, OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SUBMITED);
        state.getUserEvents().put(OrderEventEnum.USER_CANCEL, OrderStatusEnum.USER_CANCELED);
        state.getSysEvents().put(OrderEventEnum.SYS_MODIFY, OrderStatusEnum.SAVED);
        state.getSysEvents().put(OrderEventEnum.SYS_CONFIRM, OrderStatusEnum.SYS_CONFIRMED);
        state.getSysEvents().put(OrderEventEnum.SYS_CANCEL, OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SYS_CONFIRMED);
        state.getUserEvents().put(OrderEventEnum.CONFIRM, OrderStatusEnum.CONFIRMED);
        state.getUserEvents().put(OrderEventEnum.USER_CANCEL, OrderStatusEnum.USER_CANCELED);
        state.getSysEvents().put(OrderEventEnum.SYS_MODIFY, OrderStatusEnum.SYS_CONFIRMED);
        state.getSysEvents().put(OrderEventEnum.SYS_CONFIRM, OrderStatusEnum.SYS_CONFIRMED);
        state.getSysEvents().put(OrderEventEnum.SYS_CANCEL, OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.USER_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.CONFIRMED);
        state.getUserEvents().put(OrderEventEnum.USER_CANCEL, OrderStatusEnum.USER_CANCELED);
        state.getSysEvents().put(OrderEventEnum.SYS_MODIFY, OrderStatusEnum.SYS_CONFIRMED);
        state.getSysEvents().put(OrderEventEnum.SIGN_CONTRACT, OrderStatusEnum.SIGNED_CONTRACT);
        state.getSysEvents().put(OrderEventEnum.SYS_CANCEL, OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SIGNED_CONTRACT);
        state.getSysEvents().put(OrderEventEnum.PAY, OrderStatusEnum.PAID);
        state.getSysEvents().put(OrderEventEnum.EXPIRE, OrderStatusEnum.SYS_CANCELED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.PAID);
        state.getSysEvents().put(OrderEventEnum.SHIPPING, OrderStatusEnum.SHIPPED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SHIPPED);
        state.getSysEvents().put(OrderEventEnum.CLEARANCE, OrderStatusEnum.CLEARANCED);
        state.getSysEvents().put(OrderEventEnum.SETTLEMENT, OrderStatusEnum.SETTLEMENTED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.CLEARANCED);
        state.getSysEvents().put(OrderEventEnum.SETTLEMENT, OrderStatusEnum.SETTLEMENTED);
        states.put(state.getCurernt(), state);

        state = new OrderState(OrderStatusEnum.SETTLEMENTED);
        state.getUserEvents().put(OrderEventEnum.RECEIVE, OrderStatusEnum.RECEIVED);
        states.put(state.getCurernt(), state);

    }
}
