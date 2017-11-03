package com.fruit.sys.admin.queue;

import com.fruit.loan.biz.common.InterfaceRequestTypeEnum;
import com.fruit.loan.biz.common.LoanInfoContractStatusEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.loan.biz.socket.util.DateUtil;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.model.DebtMQbean;
import com.fruit.sys.admin.model.ProviderLoanBean;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementResultService;
import com.fruit.sys.admin.utils.JsonUtil;
import com.ovft.contract.api.bean.EcontractCaptchaSignBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.text.MessageFormat;
import java.util.Date;

/**
 * 签署借据服务类
 * 
 * @author ovfintech
 *
 */
public class ReceiveProviderLoanListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ReceiveProviderLoanListener.class);

	@Resource
	private LoanInfoService loanInfoService;

	@Resource
	private RuntimeConfigurationService runtimeConfigurationService;

	@Resource
	private ContainerService containerService;

	@Resource PushContainerProcessProxy pushContainerProcessProxy;

	@Resource
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Resource
	private LoanMessageService loanMessageService;

	/**
	 * 消息服务，资金放款消费者
	 * @param message
	 */

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void onMessage(Message message) {
		logger.info("资金放款消费："+JsonUtil.toString(message));
		if (message instanceof TextMessage) {
			try {
				ProviderLoanBean loanBean = JsonUtil.toBean(((TextMessage) message).getText(), ProviderLoanBean.class);
				final String transactionNo = loanBean.getTransactionNo();
				Validate.notEmpty(transactionNo);
				LoanInfoDTO loanInfoDTO  = loanInfoService.loadByTransactionNo(transactionNo);
				//借据到期日
				Date dbtExpTime =getDbtExpTime();
				loanInfoDTO.setDbtExpDt(dbtExpTime);
				int loanFlag = loanInfoService.singleStepLoan(loanInfoDTO, InterfaceRequestTypeEnum.ADMIN);

//				//如果放贷成功则短信通知
//				if(loanFlag > 0){
//					sendMsgOfPayPre(loanInfoDTO);
//				}
//				ContainerProcessBean containerProcessBean = new ContainerProcessBean();
//				containerProcessBean.setEventCode(ContainerEventEnum.OFFER.getCode());
//				containerProcessBean.setLoanInfoId(loanInfoDTO.getId());
//				pushContainerProcessProxy.sendMsgCon(JsonUtil.toString(containerProcessBean));

			} catch (Exception ex) {
				logger.error("JMSException {}", ex);
				throw new RuntimeException(ex);
			}
		} else {
			logger.error("IllegalArgumentException, Message must be of type TextMessage");
			throw new RuntimeException("IllegalArgumentException, Message must be of type TextMessage");
		}
	}


	/**
	 * 获取借据到期时间
	 * @return
	 */
	private Date getDbtExpTime(){
		int loanBorrowingTime = Integer.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_LOAN, LoanInterfaceConfig.LOAN_BORROWING_TIME));
		return DateUtil.addDay(new Date(),loanBorrowingTime);
	}


	/**
	 * 放款通知提醒
	 * @param loanInfoDTO
	 */
//	private void sendMsgOfPayPre(LoanInfoDTO loanInfoDTO){
//
//		String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}已发货，该货柜资金服务申请已审批通过，资金预计将在2小时左右发放，请留意您的银行卡到账通知，如有疑问，请致电4008-265-128。";
//
//		if(loanInfoDTO!=null && loanInfoDTO.getStatus()== LoanInfoStatusEnum.PENDING_AUDIT.getStatus()) {
//
//			int userId = loanInfoDTO.getUserId();
//
//			ContainerDTO container = containerService.loadByTransactionNo(loanInfoDTO.getLoanTransNo());
//
//			//根据userId查询实名认证信息
//			LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);
//
//			String content = MessageFormat.format(template, container.getOrderNo(), container.getNo() + container.getProductName());
//
//			loanMessageService.sendSms(loanInfoDTO.getUserId(),loanUserAuthInfoDTO.getMobile(), content);
//		}
//
//	}

}
