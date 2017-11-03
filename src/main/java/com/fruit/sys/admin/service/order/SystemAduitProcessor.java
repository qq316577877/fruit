package com.fruit.sys.admin.service.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
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
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;

@Service
public class SystemAduitProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	public SystemAduitProcessor() {
		super(OrderEventEnum.SYS_CONFIRM);
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {

		List<ContainerTmpDTO> orderContainers = containerTmpService.listByOrderNo(orderDTO.getNo());
		if (orderContainers != null && orderContainers.size() != 0) {
			for (ContainerTmpDTO ctdto : orderContainers) {
				ctdto.setStatus(ContainerStatusEnum.EGIS.getStatus());
				containerTmpService.updateById(ctdto);
				List<ContainerDetailTmpDTO> orderContainerDetails = containerDetailTmpService.listByContainerNo(ctdto
						.getNo());
				if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
					for (ContainerDetailTmpDTO cdtdto : orderContainerDetails) {
						cdtdto.setStatus(ContainerStatusEnum.EGIS.getStatus());
						containerDetailTmpService.updateById(cdtdto);
					}
				}
			}
		}
	}

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
