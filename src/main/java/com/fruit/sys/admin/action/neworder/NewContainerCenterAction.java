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
public class NewContainerCenterAction extends BaseAction {

	@Autowired
	private OrderGeneralService orderGeneralService;

	@Autowired
	private OrderProcessService orderProcessService;

	private static final Logger logger = LoggerFactory.getLogger(NewContainerCenterAction.class);



	//货柜列表
	@UriMapping("/containerList")
	public String toList() {
		return "/neworder/new_container_info_list";
	}

	//货柜审核
	@UriMapping("/containerAudit")
	public String containerAudit() {
		return "/neworder/new_container_audit";
	}

	//物流详情
	@UriMapping("/containerLogistics")
	public String containerLogistics() {
		return "/neworder/new_container_logistics";
	}

}
