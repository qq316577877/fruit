package com.fruit.sys.admin.service.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.sys.SysLoanInfoService;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.WechatConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerDetailDTO;
import com.fruit.order.biz.dto.ContainerDetailTmpDTO;
import com.fruit.order.biz.dto.ContainerTmpDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerDetailTmpService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.ContainerTmpService;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;

@Service
public class SystemCancelProcessor extends DefaultOrderProcessor {

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ContainerTmpService containerTmpService;

	@Autowired
	private ContainerDetailTmpService containerDetailTmpService;

	@Autowired
	private ContainerDetailService containerDetailService;

	@Autowired
	private OrderTaskHelper orderTaskHelper;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private SysLoanInfoService sysLoanInfoService;

	@Autowired
	private LoanInfoService loanInfoService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	@Override
	protected void setExtraOrderInfo(OrderDTO orderDTO) {

	}

	public SystemCancelProcessor() {
		super(OrderEventEnum.SYS_CANCEL);
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {

		//先判断有无贷款，如果有先取消贷款
		List<LoanInfoDTO> loanInfos = loanInfoService.listByOrderNo(orderDTO.getNo());
		if(CollectionUtils.isNotEmpty(loanInfos)){
			sysLoanInfoService.deleteByOrderNo(orderDTO.getUserId(),orderDTO.getNo());
		}

		// 取消订单同时要更新货柜状态
		if (isQueryTmp(status)) {
			List<ContainerTmpDTO> orderContainers = containerTmpService.listByOrderNo(orderDTO.getNo());
			if (orderContainers != null && orderContainers.size() != 0) {
				for (ContainerTmpDTO ctdto : orderContainers) {
					ctdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
					containerTmpService.updateById(ctdto);
					List<ContainerDetailTmpDTO> orderContainerDetails = containerDetailTmpService
							.listByContainerNo(ctdto.getNo());
					if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
						for (ContainerDetailTmpDTO cdtdto : orderContainerDetails) {
							cdtdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
							containerDetailTmpService.updateById(cdtdto);
						}
					}
				}
			}
		} else {
			List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());
			if (containers != null && containers.size() != 0) {
				for (ContainerDTO dto : containers) {
					dto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
					containerService.updateById(dto);
					List<ContainerDetailDTO> orderContainerDetails = containerDetailService.listByContainerNo(dto
							.getNo());
					if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
						for (ContainerDetailDTO cdtdto : orderContainerDetails) {
							cdtdto.setStatus(ContainerStatusEnum.CANCELED.getStatus());
							containerDetailService.updateById(cdtdto);
						}
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

		messageService.sendSms(orderDTO.getUserId(), mobile, content);

	}
}
