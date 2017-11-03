package com.fruit.sys.admin.service.order;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.model.order.OrderProcessResponse;

public interface IOrderProcessor {

	OrderEventEnum getEvent();

    OrderProcessResponse process(OrderProcessRequest request);
}
