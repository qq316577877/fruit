package com.fruit.sys.admin.queue;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.fruit.loan.biz.common.InterfaceRequestTypeEnum;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.loan.biz.socket.util.DateUtil;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.TaskProcessUtil;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovfintech.concurrent.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanInfoContractStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.sys.admin.model.DebtMQbean;
import com.fruit.sys.admin.model.ProviderLoanBean;
import com.fruit.sys.admin.utils.JsonUtil;
import com.ovft.contract.api.bean.EcontractCaptchaSignBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;

import java.util.Date;

/**
 * 签署借据服务类
 * 
 * @author ovfintech
 *
 */
public class ReceiveDebtContractListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ReceiveDebtContractListener.class);

	@Resource
	private LoanInfoService loanInfoService;

	@Resource
	private EcontractService econtractService;

	@Resource
	private EnvService envService;

	@Resource
	private RuntimeConfigurationService runtimeConfigurationService;


	@Resource
	private PushContainerProcessProxy pushContainerProcessProxy;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				DebtMQbean debtBean = JsonUtil.toBean(((TextMessage) message).getText(), DebtMQbean.class);
				final String custProjectCode = debtBean.getProjectCode();
				String source = envService.getConfig("contract.source");
				int customerId = debtBean.getUserId();
				long contractId = debtBean.getContractId();
				String captchaCode = debtBean.getCaptchaCode();
				String signLocationIp = debtBean.getSignLocationIp();
				EcontractCaptchaSignBean bean = new EcontractCaptchaSignBean();
				bean.setCustomerId(customerId);
				bean.setSource(source);
				bean.setContractId(contractId);
				bean.setCaptchCode(captchaCode);
				bean.setProjectCode(custProjectCode);
				bean.setSignature("公章");
				bean.setXaxisOffset(50f);
				bean.setYaxisOffset(-15f);
				bean.setSignLocationIp(signLocationIp);
				ResponseVo response = econtractService.signEcontract(bean);
				if (!response.isSuccess()) {
					logger.info("银行签署借据失败 具体原因: {}", response.getMessage());
					throw new RuntimeException(response.getMessage());
				}
				//合同状态更新
                LoanInfoDTO loanInfoDTO = updateLoanInfoStatus(custProjectCode);

                // 发送消息给资金服务 通知开始发放贷款
				offerLoan(loanInfoDTO);

			} catch (Exception ex) {
				logger.error("JMSException {}", ex);
			}
		} else {
			logger.error("IllegalArgumentException, Message must be of type TextMessage");
		}
	}

    private LoanInfoDTO updateLoanInfoStatus(String custProjectCode) {
        // 银行签署成功修改表loan_info的status为已授信
        LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(custProjectCode);
        LoanInfoDTO model = new LoanInfoDTO();
        BeanUtils.copyProperties(loanInfoDTO, model);
        model.setContractStatus(LoanInfoContractStatusEnum.SIGNED_COMPLETED.getStatus());
        loanInfoService.update(model);
        return loanInfoDTO;
    }

    private void offerLoan( LoanInfoDTO loanInfoDTO) {
		try {
            Date dbtExpTime =getDbtExpTime();
            loanInfoDTO.setDbtExpDt(dbtExpTime);
            loanInfoService.singleStepLoan(loanInfoDTO, InterfaceRequestTypeEnum.ADMIN);

        }catch (Exception ex){
            logger.error("借据合同签署后，放款失败。");
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

}
