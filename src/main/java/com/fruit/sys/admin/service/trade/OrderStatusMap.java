package com.fruit.sys.admin.service.trade;

import java.util.HashMap;
import java.util.Map;

import com.fruit.order.biz.common.OrderStatusEnum;

public class OrderStatusMap {

	private static Map<Integer, String> orderStatusDescMap = new HashMap<Integer, String>();

	private static Map<Integer, String> containerStatusDescMap = new HashMap<Integer, String>();

	private static Map<Integer, String> loanStatusDescMap = new HashMap<Integer, String>();

	static {
		for (OrderStatusEnum status : OrderStatusEnum.values()){
			orderStatusDescMap.put(status.getStatus(), status.getUserDesc());
		}
		
		containerStatusDescMap.put(1, "暂存");
		containerStatusDescMap.put(1, "暂存");
		containerStatusDescMap.put(1, "暂存");
		containerStatusDescMap.put(1, "暂存");
		containerStatusDescMap.put(1, "暂存");
		containerStatusDescMap.put(1, "暂存");
		
		loanStatusDescMap.put(1, "暂存");
		loanStatusDescMap.put(1, "暂存");
		loanStatusDescMap.put(1, "暂存");
		loanStatusDescMap.put(1, "暂存");
		loanStatusDescMap.put(1, "暂存");
	}

}
