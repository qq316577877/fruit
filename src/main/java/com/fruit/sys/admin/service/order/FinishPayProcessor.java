package com.fruit.sys.admin.service.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.WechatConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.order.biz.common.ContainerStatusEnum;
import com.fruit.order.biz.common.OrderEventEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.ContainerDetailDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerDetailService;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.sys.admin.model.order.OrderProcessRequest;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.trade.OrderTaskHelper;

import javax.swing.text.DateFormatter;

@Service
public class FinishPayProcessor extends DefaultOrderProcessor{

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
	private UserLoanManagementListService userLoanManagementListService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	public FinishPayProcessor(){
		super(OrderEventEnum.SETTLEMENT);
	}

	/**
	 * 判断是否可以结算
	 *
	 * @param
	 */
	@Override
	protected boolean handleExtraBefore(OrderDTO orderDTO) {

		boolean finishFlag = true;
		//判断订单每个货柜的状态为已通关，预期到货时间不为空(大于xxxx)
		List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());

		Date nullDate = DateUtil.StringToDate("0001-01-01","YYYY-MM-DD");
		if (containers != null && !containers.isEmpty()) {
			for (ContainerDTO dto : containers) {
				//判断货柜状态是否为已清关.预期到货时间大于XX
				if ( !(dto.getStatus() >= ContainerStatusEnum.CLEARED.getStatus()
						&& dto.getPreReceiveTime().compareTo(nullDate) > 0) ) {
					//未清关
					finishFlag = false;
					break;
				}
			}
		}
		return finishFlag;
	}

	@Override
	protected void handleExtra(OrderDTO orderDTO, int status, OrderProcessRequest request) {
		// 首先判断货柜是否有贷款 如果没有贷款直接更新状态为待收货
		// 如果有贷款更新为代还款
		List<ContainerDTO> containers = containerService.listByOrderNo(orderDTO.getNo());
		if (containers != null && containers.size() != 0) {
			for (ContainerDTO dto : containers) {
				//扫描贷款信息
				List<String> transactionNoList = new ArrayList<String>();
				transactionNoList.add(dto.getTransactionNo());
				List<LoanInfoDTO> loanInfos = userLoanManagementListService.loadGivenByTransNoList(transactionNoList);
				if(loanInfos != null && loanInfos.size() > 0){
					dto.setStatus(ContainerStatusEnum.REPAYMENT.getStatus());
				}else{
					dto.setStatus(ContainerStatusEnum.SETTLEMENTED.getStatus());
				}

				containerService.updateById(dto);
				List<ContainerDetailDTO> orderContainerDetails = containerDetailService.listByContainerNo(dto.getNo());
				if (orderContainerDetails != null && orderContainerDetails.size() != 0) {
					for (ContainerDetailDTO cdtdto : orderContainerDetails) {
						if(loanInfos != null && loanInfos.size() > 0){
							cdtdto.setStatus(ContainerStatusEnum.REPAYMENT.getStatus());
						}else{
							cdtdto.setStatus(ContainerStatusEnum.SETTLEMENTED.getStatus());
						}
						containerDetailService.updateById(cdtdto);
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
