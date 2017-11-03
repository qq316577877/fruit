package com.fruit.sys.admin.service.order;

import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.sys.admin.service.neworder.NewOrderForOtherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.base.biz.service.AreaService;
import com.fruit.base.biz.service.CityService;
import com.fruit.base.biz.service.ProvinceService;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderStatusEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerInsuranceDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.DeliveryInfoDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerInsuranceService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.order.biz.service.DeliveryInfoService;
import com.fruit.order.biz.service.OrderService;
import com.fruit.sys.admin.model.order.OrderInfoForLoanVo;

@Service
public class OrderForOtherService {

	@Autowired
	private ContainerInsuranceService containerInsuranceService;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerService containerService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryInfoService deliveryInfoService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private CityService cityService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private ContainerInfoService containerNewInfoService;

	@Autowired
	private NewOrderForOtherService newOrderForOtherService;


	// private static final Logger logger =
	// LoggerFactory.getLogger(OrderForOtherService.class);

	public OrderInfoForLoanVo queryOrderInfoForLoan(String transactionNo) {
		OrderInfoForLoanVo result = new OrderInfoForLoanVo();
		// 先查正式表，为null再去查临时表
		ContainerDTO container = containerService.loadByTransactionNo(transactionNo);
		if (container == null) {
			ContainerTmpDTO containerTmp = containerTmpService.loadByTransactionNo(transactionNo);
			container = new ContainerDTO();
			if(containerTmp!=null){
				BeanUtils.copyProperties(containerTmp, container);
			}else{//正式表、临时表都没有，证明在新表里面
				return newOrderForOtherService.handllerByNewOrderSystem(transactionNo);
			}
		}

		if(container==null){
			return null;
		}

		int containerStatus = container.getStatus();
		result.setContainerStatus(containerStatus);
		result.setContainerStatusDesc(ContainerStatusEnum.getStatusDesc(containerStatus));
		result.setOrderId(container.getOrderNo());
		result.setContainerId(container.getNo());
		result.setTransactionNo(container.getTransactionNo());
		OrderDTO order = orderService.loadByNo(container.getOrderNo());
		result.setOrderStatusDesc(OrderStatusEnum.get(order.getStatus()).getUserDesc());
		if (containerStatus >= ContainerStatusEnum.SHIPPED.getStatus()) {
			// 如果货柜状态是已发货，设置发货时间
			result.setDeliveryTime(container.getDeliveryTime());
		}
		if (containerStatus >= ContainerStatusEnum.CLEARED.getStatus()) {
			// 如果货柜状态是已清关，设置发货时间
			result.setPreReceiveTime(container.getPreReceiveTime());
			result.setClearFlag(true);
		}
		ContainerInsuranceDTO insurance = containerInsuranceService.loadByContainerNo(container.getTransactionNo());
		if (insurance != null) {
			result.setContractNumber(insurance.getContractNumber());
		}
		// 加载收货地址信息
		DeliveryInfoDTO deliveryInfoDto = deliveryInfoService.loadByOrderNo(container.getOrderNo());
		StringBuilder receiverAddress = new StringBuilder();
		receiverAddress.append(provinceService.loadById(deliveryInfoDto.getProvinceId()).getName())
				.append(cityService.loadById(deliveryInfoDto.getCityId()).getName())
				.append(areaService.loadById(deliveryInfoDto.getDistrictId()).getName());
				//.append(deliveryInfoDto.getAddress());
		result.setReceiverAddress(receiverAddress.toString());
		return result;
	}



	private ContainerDTO copyFromNewToOld(ContainerInfoDTO containerNew){
		ContainerDTO container = null;
		if(containerNew!=null){
			container = new ContainerDTO();
			container.setId(containerNew.getId());
			container.setNo(containerNew.getContainerNo());
			container.setOrderNo(containerNew.getOrderNo());
			container.setTransactionNo(containerNew.getContainerSerialNo());
			container.setTotalQuantity(containerNew.getTotalQuantity());
			container.setProductAmount(containerNew.getProductAmount());
			container.setAgencyAmount(containerNew.getAgencyAmount());
//		container.setPremiumAmount();
			container.setTotalPrice(containerNew.getTotalPrice());
			container.setDeliveryTime(containerNew.getDeliveryTime());
			container.setPreReceiveTime(containerNew.getPreReceiveTime());
			container.setAddTime(containerNew.getAddTime());
			container.setUpdateTime(containerNew.getUpdateTime());
			container.setStatus(containerNew.getStatus());
			container.setClearanceTime(containerNew.getClearanceTime());
			container.setReceiveTime(containerNew.getReceiveTime());
		}

		return container;
	}



}
