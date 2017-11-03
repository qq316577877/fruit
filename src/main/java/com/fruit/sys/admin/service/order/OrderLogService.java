package com.fruit.sys.admin.service.order;

import java.util.Date;

import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.dto.StatusRecordTimeDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.order.biz.service.StatusRecordTimeService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.dto.OrderUpdateLogDTO;
import com.fruit.order.biz.service.OrderUpdateLogService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLogService {

	@Autowired
	private OrderUpdateLogService orderUpdateLogService;

	@Autowired
	private StatusRecordTimeService statusRecordTimeService;

	@Autowired
	private OrderService orderService;

	private static final Logger logger = LoggerFactory.getLogger(OrderLogService.class);

	@Transactional(rollbackFor = Exception.class)
	public void addLog(String orderNo, int userId,int type, int fromStatus, int toStatus, String updateInfo) {

		try {
			Date nowDate = new Date();
			OrderStatusEnum status = OrderStatusEnum.get(toStatus);

			OrderUpdateLogDTO dto = new OrderUpdateLogDTO();
			dto.setOrderNo(orderNo);
			dto.setUserId(userId);
			dto.setType(type);
			dto.setFromStatus(fromStatus);
			dto.setToStatus(toStatus);
			dto.setUpdateInfo(updateInfo);
			dto.setAddTime(nowDate);
			orderUpdateLogService.create(dto);

		}catch (Exception ex){
			logger.error(ex.getMessage());
			throw  ex;
		}
	}


	@Transactional(rollbackFor = Exception.class)
	public void addRecordTime(String orderNo,  int toStatus) {

		try {
			Date nowDate = new Date();
			OrderStatusEnum status = OrderStatusEnum.get(toStatus);

			//根据订单号查询订单ID
			OrderDTO  orderDTO = orderService.loadByNo(orderNo);
			Validate.notNull(orderDTO,"订单号不存在!");
			//统计表更新
			StatusRecordTimeDTO statusRecordTimeDTO = statusRecordTimeService.loadLastByOrderId(orderDTO.getId());

			if(statusRecordTimeDTO != null ){
				//更新记录
				statusRecordTimeDTO.setUpdateTime(nowDate);
				statusRecordTimeService.update(fillContent(statusRecordTimeDTO,status));
			}else{
				//创建记录
				statusRecordTimeDTO = new StatusRecordTimeDTO();
				statusRecordTimeDTO.setOrderId(orderDTO.getId());

				statusRecordTimeDTO.setAddTime(nowDate);
				statusRecordTimeDTO.setUpdateTime(nowDate);
				statusRecordTimeService.create(fillContent(statusRecordTimeDTO,status));
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
			throw  ex;
		}
	}

	private StatusRecordTimeDTO fillContent(StatusRecordTimeDTO statusRecordTimeDTO,OrderStatusEnum status)
	{
		Date nowDate = new Date();

		switch (status){

			case SAVED:
				//暂存时间更新
				statusRecordTimeDTO.setSavedTime(nowDate);
				break;
			case SUBMITED:
				//提交待审核
				statusRecordTimeDTO.setSubmitedTime(nowDate);
				break;
			case SYS_CONFIRMED:
				//审核待提交
				statusRecordTimeDTO.setSysConfirmedTime(nowDate);
				break;
			case CONFIRMED:
				//已提交
				statusRecordTimeDTO.setConfirmedTime(nowDate);
				break;
			case SIGNED_CONTRACT:
				//签订合同
				statusRecordTimeDTO.setSignedContractTime(nowDate);
				break;
			case PAID:
				//已付款
				statusRecordTimeDTO.setPaidTime(nowDate);
				break;
			case SHIPPED:
				//已发货
				statusRecordTimeDTO.setShippedTime(nowDate);
				break;
			case CLEARANCED:
				//已清关
				statusRecordTimeDTO.setClearancedTime(nowDate);
				break;
			case SETTLEMENTED:
				//已结算
				statusRecordTimeDTO.setSettlementedTime(nowDate);
				break;
			case RECEIVED:
				//已收货
				statusRecordTimeDTO.setReceivedTime(nowDate);
				break;
			case FINISHED:
				//已完成
				statusRecordTimeDTO.setReceivedTime(nowDate);
				statusRecordTimeDTO.setFinishedTime(nowDate);
				break;
			case USER_CANCELED:
				//用户已取消
				statusRecordTimeDTO.setUserCanceledTime(nowDate);
				break;
			case SYS_CANCELED:
				//平台已取消
				statusRecordTimeDTO.setSysCanceledTime(nowDate);
				break;
			case SYSTEM_CLOSED:
				//平台已关闭订单
				statusRecordTimeDTO.setSystemClosedTime(nowDate);
				break;
			default:
				throw new IllegalArgumentException("订单状态不合法!");
		}

		return  statusRecordTimeDTO;
	}

}
