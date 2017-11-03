/*
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.model.order;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.common.OrderStatusEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:    订单状态流转
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : OrderState.java
 */
public class OrderState implements Serializable
{
    private OrderStatusEnum curernt;

    private Map<OrderEventEnum, OrderStatusEnum> userEvents = new HashMap<OrderEventEnum, OrderStatusEnum>();

    private Map<OrderEventEnum, OrderStatusEnum> sysEvents = new HashMap<OrderEventEnum, OrderStatusEnum>();

    public OrderState(OrderStatusEnum curernt)
    {
        this.curernt = curernt;
    }

    public OrderStatusEnum getCurernt()
    {
        return curernt;
    }

    public void setCurernt(OrderStatusEnum curernt)
    {
        this.curernt = curernt;
    }

    public Map<OrderEventEnum, OrderStatusEnum> getUserEvents()
    {
        return userEvents;
    }

    public void setUserEvents(Map<OrderEventEnum, OrderStatusEnum> userEvents)
    {
        this.userEvents = userEvents;
    }

    public Map<OrderEventEnum, OrderStatusEnum> getSysEvents()
    {
        return sysEvents;
    }

    public void setSysEvents(Map<OrderEventEnum, OrderStatusEnum> sysEvents)
    {
        this.sysEvents = sysEvents;
    }
}
