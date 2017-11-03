package com.fruit.sys.admin.service.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.model.order.OrderProcessResponse;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;

@Service
public class OrderProcessService {

	@Autowired
	private OrderProcessFactory orderProcessFactory;

	/**
	 * 操作订单
	 * 
	 * @return
	 */
	public AjaxResult operateOrder(int userId, String orderId, OrderEventEnum event,
			String domain, int operatorType) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		boolean successful = false;
		int code = AjaxResultCode.OK.getCode();
		try {
			OrderProcessRequest request = new OrderProcessRequest();
			request.setOrderId(orderId);
			request.setUserId(userId);
			request.setEvent(event);
			request.setOperatorType(operatorType);
			OrderProcessResponse response = orderProcessFactory.process(request);
			successful = response.isSuccessful();
			String msg = response.getMessage();
			//处理订单流程判断失败时。
			if(!successful){
				code = AjaxResultCode.REQUEST_INVALID.getCode();
			}
			String url = domain + "order/detail/id=" + orderId;
			dataMap.put("url", url);
			dataMap.put("message", msg);
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult(code, msg, dataMap);
		} catch (IllegalArgumentException e) {
			code = AjaxResultCode.REQUEST_BAD_PARAM.getCode();
			String msg = e.getMessage();
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult(code, msg, dataMap);
		} catch (RuntimeException e) {
			code = AjaxResultCode.SERVER_ERROR.getCode();
			String msg = "系统繁忙";
			dataMap.put("successful", successful ? 1 : 0);
			return new AjaxResult(code, msg, dataMap);
		}
	}

}
