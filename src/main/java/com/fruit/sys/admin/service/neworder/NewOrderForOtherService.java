package com.fruit.sys.admin.service.neworder;

import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.base.biz.service.AreaService;
import com.fruit.base.biz.service.CityService;
import com.fruit.base.biz.service.ProvinceService;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.service.*;
import com.fruit.order.biz.service.DeliveryInfoService;
import com.fruit.sys.admin.model.order.OrderInfoForLoanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewOrderForOtherService {


	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private CityService cityService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ContainerInfoService containerNewInfoService;

	@Autowired
	private OrderNewInfoService orderNewInfoService;

	@Autowired
	private ContainerInsuranceService containerNewInsuranceService;

	@Autowired
	private DeliveryInfoService deliveryInfoService;

	@Autowired
	private UserDeliveryAddressService userDeliveryAddressService;

	@Autowired
	private ContainerDeliveryAddressService containerDeliveryAddressService;


	/**
	 * 新订单获取给信贷的信息
	 * @param transactionNo
	 * @return
     */
	public OrderInfoForLoanVo handllerByNewOrderSystem(String transactionNo){
		OrderInfoForLoanVo result = new OrderInfoForLoanVo();
		ContainerInfoDTO containerNew = containerNewInfoService.loadByContainerSerialNo(transactionNo);

		int containerStatus = containerNew.getStatus();
		result.setContainerStatus(containerStatus);
		result.setContainerStatusDesc(ContainerStatusEnum.getSysStatusDesc(containerStatus));
		result.setOrderId(containerNew.getOrderNo());
		result.setContainerId(containerNew.getContainerNo());
		result.setTransactionNo(containerNew.getContainerSerialNo());
		OrderNewInfoDTO orderNew = orderNewInfoService.loadByOrderNo(containerNew.getOrderNo());

		result.setOrderStatusDesc(com.fruit.newOrder.biz.common.OrderStatusEnum.get(orderNew.getStatus()).getUserDesc());
		if (containerStatus >= com.fruit.newOrder.biz.common.ContainerStatusEnum.SHIPPED.getStatus()) {
			// 如果货柜状态是已发货，设置发货时间
			result.setDeliveryTime(containerNew.getDeliveryTime());
		}
		if (containerStatus >= com.fruit.newOrder.biz.common.ContainerStatusEnum.CLEARED.getStatus()) {
			// 如果货柜状态是已清关，设置发货时间
			result.setPreReceiveTime(containerNew.getPreReceiveTime());
			result.setClearFlag(true);
		}

		ContainerInsuranceDTO containerInsuranceDTO  = containerNewInsuranceService.loadByContainerSerialNo(containerNew.getContainerSerialNo());
		if (containerInsuranceDTO != null) {
			result.setContractNumber(containerInsuranceDTO.getApplicationNo());
		}

		// 加载收货地址信息
//		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadById(containerNew.getDeliveryId());
		//edit 20171026 by duzhongpeng
	//	UserDeliveryAddressDTO deliveryInfoDto = userDeliveryAddressService.loadById(containerNew.getDeliveryId());

		ContainerDeliveryAddressDTO deliveryInfoDto = containerDeliveryAddressService.loadByContainerId(containerNew.getId());

		StringBuilder receiverAddress = new StringBuilder();
		if(deliveryInfoDto != null) {
			receiverAddress.append(provinceService.loadById(deliveryInfoDto.getProvinceId()).getName())
					.append(cityService.loadById(deliveryInfoDto.getCityId()).getName())
					.append(areaService.loadById(deliveryInfoDto.getDistrictId()).getName());
		}

		result.setReceiverAddress(receiverAddress.toString());
		return result;
	}
}
