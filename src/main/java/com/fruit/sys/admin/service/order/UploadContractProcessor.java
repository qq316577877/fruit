package com.fruit.sys.admin.service.order;

import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.WechatConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadContractProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerDetailService containerDetailService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	public UploadContractProcessor() {
		super(OrderEventEnum.SIGN_CONTRACT);
	}

	/*@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {

		List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());
		if (containers != null && containers.size() != 0) {
			for (ContainerDTO dto : containers) {
				dto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
				containerService.updateById(dto);
				List<ContainerDetailDTO> orderContainerDetails = containerDetailService.listByContainerNo(dto.getNo());
				if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
					for (ContainerDetailDTO cdtdto : orderContainerDetails) {
						cdtdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
						containerDetailService.updateById(cdtdto);
					}
				}
			}
		}
	}*/

	@Override
	protected void handleExtraAfter(final OrderDTO orderDTO, int status, String smsTemplate) {
		final String content = smsTemplate.replace("{orderId}", orderDTO.getNo());
		UserAccountDTO userInfo = userAccountService.loadById(orderDTO.getUserId());
		final String mobile = userInfo.getMobile();
		orderTaskHelper.submitRunnable(new Runnable() {

			@Override
			public void run() {
				messageService.sendSms(orderDTO.getUserId(), mobile, content);
			}
		});

	}
}
