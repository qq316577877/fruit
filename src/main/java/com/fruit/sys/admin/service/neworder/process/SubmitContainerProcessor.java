package com.fruit.sys.admin.service.neworder.process;

import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.account.biz.model.UserDeliveryAddress;
import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.common.EventRoleEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderModeTypeEnum;
import com.fruit.newOrder.biz.dto.*;
import com.fruit.newOrder.biz.request.ContainerProcessRequest;
import com.fruit.newOrder.biz.request.ContainerProcessResponse;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.request.OrderProcessResponse;
import com.fruit.newOrder.biz.service.ContainerDeliveryAddressService;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.GoodsVarietyService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.newOrder.biz.service.impl.DefaultContainerProcessor;
import com.fruit.newOrder.biz.service.impl.DefaultOrderProcessor;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/*
 * 提交审核
 */
@Service
public class SubmitContainerProcessor extends DefaultContainerProcessor {



	public SubmitContainerProcessor() {
		super(ContainerEventEnum.SUBMIT);
	}


	@Autowired
	private LoanInfoService loanInfoService;

	@Autowired
	private GoodsVarietyService goodsVarietyService;

	@Autowired
	private OrderNewInfoService orderNewInfoService;

	@Autowired
	private ContainerDeliveryAddressService containerDeliveryAddressService;

	@Autowired
	private UserDeliveryAddressService userDeliveryAddressService;


	@Override
	protected ContainerProcessResponse handleExtraBefore(ContainerProcessRequest request) {

		ContainerProcessResponse processResponse = new ContainerProcessResponse(true,"","");

		int userId = request.getUserId();//用户ID
		String orderNo = request.getOrderNo();//订单号
		ContainerEventEnum event = request.getEvent();//订单事件
		EventRoleEnum role = event.getRole(); //事件所属角色
		Map<String,Object> dataMap = request.getParams();//参数

		ContainerInfoDTO containerInfoDTO = request.getContainerInfo();
		List<ContainerGoodsInfoDTO> list = request.getContainerGoodsInfoList();


		OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderNo);

		//货柜的收货地址保存
		UserDeliveryAddressDTO userDeliveryAddressDTO = userDeliveryAddressService.loadById(containerInfoDTO.getDeliveryId());

		ContainerDeliveryAddressDTO containerDeliveryAddressDTO= new ContainerDeliveryAddressDTO();

		BeanUtils.copyProperties(userDeliveryAddressDTO,containerDeliveryAddressDTO);
		containerDeliveryAddressDTO.setContainerId(containerInfoDTO.getId());
		containerDeliveryAddressDTO.setUserId(containerInfoDTO.getUserid());

		containerDeliveryAddressService.create(containerDeliveryAddressDTO);


		if(containerInfoDTO.getLoanAmount().compareTo(new BigDecimal("0.00")) > 0) {

			boolean ceateFlag = false;
			LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(containerInfoDTO.getContainerSerialNo());
			if(loanInfoDTO == null ){
				loanInfoDTO = new LoanInfoDTO();
				ceateFlag =true;
			}

			loanInfoDTO.setProjectCode(containerInfoDTO.getContainerSerialNo());

			loanInfoDTO.setOrderNo(orderNo);
			loanInfoDTO.setTransactionNo(containerInfoDTO.getContainerSerialNo());

			GoodsVarietyDTO goodsVarietyDTO = goodsVarietyService.loadById(orderNewInfoDTO.getVarietyId());

			loanInfoDTO.setUserId(orderNewInfoDTO.getUserId());
			loanInfoDTO.setProductId(goodsVarietyDTO.getId());
			loanInfoDTO.setProdictName(goodsVarietyDTO.getName());
			loanInfoDTO.setAvailableLoan(containerInfoDTO.getLoanAmount());
			loanInfoDTO.setAppliyLoan(containerInfoDTO.getLoanAmount());
			loanInfoDTO.setConfirmLoan(containerInfoDTO.getLoanAmount());
			if(ceateFlag) {
				loanInfoService.create(loanInfoDTO);
			}else{
				loanInfoService.update(loanInfoDTO);
			}
		}


		return processResponse;
	}

}
