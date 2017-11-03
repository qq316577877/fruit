package com.fruit.sys.admin.queue;

import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.loan.biz.socket.util.DateUtil;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.common.ContainerUpdateTypeEnum;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.request.ContainerProcessRequest;
import com.fruit.newOrder.biz.request.ContainerProcessResponse;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.IContainerProcessService;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * 签署借据服务类
 * 
 * @author ovfintech
 *
 */
public class ReceiveContanierProcessListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ReceiveContanierProcessListener.class);

	@Resource
	private LoanInfoService loanInfoService;

	@Resource
	private RuntimeConfigurationService runtimeConfigurationService;

	@Resource
	private ContainerInfoService containerInfoService;

	@Resource
	private EnvService envService;

	@Resource
	private IContainerProcessService containerProcessService;


	/**
	 * 修改货柜状态
	 * @param message
	 */

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void onMessage(Message message) {
	//	logger.info("修改货柜状态："+JsonUtil.toString(message));
		if (message instanceof TextMessage) {
			try {
				ContainerProcessBean loanBean = JsonUtil.toBean(((TextMessage) message).getText(), ContainerProcessBean.class);

				long loanId = loanBean.getLoanInfoId();
				int eventCode = loanBean.getEventCode();

				LoanInfoDTO loanInfoDTO = loanInfoService.loadById(loanId);
				ContainerEventEnum eventEnum = ContainerEventEnum.get(eventCode);


				ContainerInfoDTO containerInfoDTO = containerInfoService.loadByContainerSerialNo(loanInfoDTO.getTransactionNo());

				if(containerInfoDTO != null ) {

					ContainerProcessRequest processRequest = new ContainerProcessRequest();
					processRequest.setUserId(loanInfoDTO.getUserId());
					processRequest.setOrderNo(loanInfoDTO.getOrderNo());
					processRequest.setOperatorType(ContainerUpdateTypeEnum.STATUS.getType());
					processRequest.setEvent(eventEnum);
					processRequest.setUserIp(loanBean.getSignLocationIp());
					processRequest.setDomain(envService.getDomain());
					processRequest.setContainerInfo(containerInfoDTO);
					processRequest.setStatusBefore(containerInfoDTO.getStatus());
					processRequest.setUpdateGoodsFlag(false);//需要更新商品信息

					ContainerProcessResponse containerProcessResponse = containerProcessService.operateOrder(processRequest);

					if (containerProcessResponse.isSuccessful()) {
						logger.info("修改货柜状态成功!-" + containerProcessResponse.getMessage());
					} else {
						logger.info("修改货柜状态失败:" + containerProcessResponse.getMessage());
					}
				}else{
					logger.info("旧渠道订单处理，不做处理!-" + loanInfoDTO.getOrderNo());
				}

			} catch (Exception ex) {
				logger.error("修改货柜状态失败 JMSException {}", ex);
				throw new RuntimeException(ex);
			}
		} else {
			logger.error("修改货柜状态失败 IllegalArgumentException, Message must be of type TextMessage");
			throw new RuntimeException("修改货柜状态失败 IllegalArgumentException, Message must be of type TextMessage");
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
