package com.fruit.sys.admin.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerService;

@Service
public class ConfirmReceiptProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerService containerService;
	
	public ConfirmReceiptProcessor() {
		super(OrderEventEnum.RECEIVE);
	}
	
	@Override
	protected boolean handleExtraBefore(OrderDTO orderDTO) {
		List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());
		boolean flag = true;
		if(containers != null && containers.size() != 0){
			for(ContainerDTO dto : containers){
				if(dto.getStatus() != ContainerStatusEnum.RECEIVED.getStatus()){
						flag = false;
						break;
				}
			}
		}
		return flag;
	}
}
