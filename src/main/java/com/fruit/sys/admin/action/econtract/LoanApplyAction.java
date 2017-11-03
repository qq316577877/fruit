package com.fruit.sys.admin.action.econtract;

import com.fruit.sys.admin.model.portal.AjaxResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.econtract.LoanApplyQuotaService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovft.contract.api.service.EcontractService;

/**
 * 2017-08-08<br>
 * 接入电子合同
 * 
 * @author ovfintech
 *
 */
@Component
@UriMapping("/loan/auth")
public class LoanApplyAction extends BaseAction {

	// private static final Logger logger =
	// LoggerFactory.getLogger(LoanApplyAction.class);

	@Autowired
	private LoanApplyQuotaService loanApplyQuotaService;

	@Autowired
	private LoanUserCreditInfoService loanUserCreditInfoService;

	@Autowired
	private EcontractService econtractService;

	@Autowired
	private EnvService envService;

	/**
	 * 签署合同发送短信验证码
	 * 
	 * @return
	 */
	@UriMapping(value = "/contract/captcha_send_ajax", interceptors = "logInterceptor")
	public AjaxResult anxinsendCaptcha() {
		int userId = super.getIntParameter("userId");
		return loanApplyQuotaService.anxinsendCaptcha(userId);
	}

	/**
	 * 银行签署借款合同<br>
	 * 
	 * 
	 * @return
	 */
	@UriMapping(value = "/contract/online_sign_ajax", interceptors = "logInterceptor")
	public AjaxResult signBorrowContract() {
		int userId = super.getIntParameter("userId");
		String contractId = super.getStringParameter("contractId");
		String captchaCode = super.getStringParameter("captchaCode");
		return loanApplyQuotaService.signBorrowContract(userId, Long.parseLong(contractId), captchaCode);
	}

	/**
	 * 创建借据
	 * 
	 * @return
	 */
	@UriMapping(value = "/contract/debt_create_ajax", interceptors = "logInterceptor")
	public AjaxResult createDebt() {
		String orderId = super.getStringParameter("orderId");
		String transactionNo = super.getStringParameter("transactionNo");
		return loanApplyQuotaService.createDebt(transactionNo, orderId);
	}

	/**
	 * 银行发送短信验证码
	 * 
	 * @return
	 */
	@UriMapping(value = "/contract/debt_captcha_send_ajax", interceptors = "logInterceptor")
	public AjaxResult sendDebtCaptcha() {
		String orderId = super.getStringParameter("orderId");
		String transactionNo = super.getStringParameter("transactionNo");
		return loanApplyQuotaService.sendDebtCaptcha(orderId, transactionNo);
	}

	/**
	 * 签署借据
	 * 
	 * @return
	 */
	@UriMapping(value = "/contract/debt_sign_ajax", interceptors = "logInterceptor")
	public AjaxResult signDebtContract() {
		AjaxResult result = new AjaxResult();
		String orderId = super.getStringParameter("orderId");
		String contractId = super.getStringParameter("contractId");
		String transactionNo = super.getStringParameter("transactionNo");
		String captchaCode = super.getStringParameter("captchaCode");
		try {
			result = loanApplyQuotaService.signDebtContract(orderId, Long.parseLong(contractId), transactionNo, captchaCode);
		}catch (Exception ex){
			result.setCode(AjaxResultCode.SERVER_ERROR.getCode());
			result.setMsg(ex.getMessage());
		}
		return result;
	}
}
