package com.fruit.sys.admin.action.neworder;


import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.service.order.OrderGeneralService;
import com.fruit.sys.admin.service.order.OrderProcessService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单中心
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/newOrder")
public class NewOrderCenterAction extends BaseAction {

	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private OrderProcessService orderProcessService;

	private static final Logger logger = LoggerFactory.getLogger(NewOrderCenterAction.class);


	//订单列表
	@UriMapping("/orderList")
	public String toList() {
		return "/neworder/new_order_info_list";
	}


	//订单审核
	@UriMapping("/orderAudit")
	public String orderAudit() {
		return "/neworder/new_order_audit";
	}

}
