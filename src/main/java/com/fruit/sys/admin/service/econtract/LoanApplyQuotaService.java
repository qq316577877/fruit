package com.fruit.sys.admin.service.econtract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.fruit.loan.biz.common.InterfaceRequestTypeEnum;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.common.ContainerUpdateTypeEnum;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.request.ContainerProcessRequest;
import com.fruit.newOrder.biz.request.ContainerProcessResponse;
import com.fruit.newOrder.biz.service.ContainerInfoService;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.queue.PushContainerProcessProxy;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanInfoContractStatusEnum;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.loan.biz.service.sys.SysLoanUserCreditInfoService;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.OrderService;
import com.fruit.sys.admin.model.DebtMQbean;
import com.fruit.sys.admin.model.ProviderLoanBean;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.queue.PushDebtContractProxy;
import com.fruit.sys.admin.queue.PushProviderLoanProxy;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.JsonUtil;
import com.fruit.sys.admin.utils.MoneyUtil;
import com.fruit.sys.admin.utils.TaskProcessUtil;
import com.ovfintech.concurrent.TaskAction;
import com.ovft.contract.api.bean.CreateEcontractBean;
import com.ovft.contract.api.bean.EcontractCaptchaSignBean;
import com.ovft.contract.api.bean.EcontractSignBean;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.bean.SignWithoutCaptchaBean;
import com.ovft.contract.api.bean.VerifyCaptchaBean;
import com.ovft.contract.api.service.EcontractService;

@Service
public class LoanApplyQuotaService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(LoanApplyQuotaService.class);

	@Resource
	private EcontractService econtractService;

	@Resource
	private LoanSmsContactsConfigService loanSmsContactsConfigService;

	@Resource
	private SysLoanUserCreditInfoService sysloanUserCreditInfoService;

	@Resource
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Resource
	private LoanUserCreditInfoService loanUserCreditInfoService;

	@Resource
	private OrderService orderService;

	@Resource
	private OrderNewInfoService orderNewInfoService;

	@Resource
	private EnvService envService;

	@Resource
	private LoanInfoService loanInfoService;

	@Resource
	private PushDebtContractProxy pushDebtContractProxy;

	@Resource
	private PushContainerProcessProxy pushContainerProcessProxy;

	@Resource
	private CacheClient cacheClient;

	@Resource
	private RuntimeConfigurationService runtimeConfigurationService;


	private  static String source ;


	public AjaxResult anxinsendCaptcha(int userId) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
				LoanSmsBizTypeEnum.BANKER_CONTRACT.getType());
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = sysloanUserCreditInfoService.loadByUserId(userId);

		String smsTemplateId = "";
		if("product".equals(EnvService.getEnv())){
			//生产环境的时候
			smsTemplateId = envService.getConfig("contract.sms.template.id");
		}

		ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(getCurrentConfig(loanSmsContactsConfigDTO),
				loanUserCreditInfoDTO.getProjectCode(), 0, smsTemplateId,source));
		if (!response.isSuccess()) {
			logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		ajaxResult.setMsg("短信发送成功!");
		return ajaxResult;
	}

	public AjaxResult signBorrowContract(int userId, long contractId, String captchaCode) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = sysloanUserCreditInfoService.loadByUserId(userId);
		LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
				LoanSmsBizTypeEnum.BANKER_CONTRACT.getType());

		EcontractCaptchaSignBean bean = new EcontractCaptchaSignBean();
		bean.setCustomerId(getCurrentConfig(loanSmsContactsConfigDTO));
		bean.setContractId(contractId);
		bean.setSource(source);
		bean.setCaptchCode(captchaCode);
		bean.setProjectCode(loanUserCreditInfoDTO.getProjectCode());
		bean.setSignature("乙方：(签章)");
		bean.setXaxisOffset(50f);
		bean.setYaxisOffset(-50f);
		bean.setSignLocationIp(WebContext.getRequest().getRemoteHost());
		ResponseVo response = econtractService.signEcontract(bean);
		if (!response.isSuccess()) {
			logger.info("银行签署合同失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(response.getCode());
			ajaxResult.setMsg(response.getMessage());
			return ajaxResult;
		}
		// 银行签署成功修改表loan_user_credit_info的status为已授信
		LoanUserCreditInfoDTO model = new LoanUserCreditInfoDTO();
		BeanUtils.copyProperties(loanUserCreditInfoDTO, model);
		model.setStatus(LoanUserCreditStatusEnum.PASS.getStatus());
		loanUserCreditInfoService.update(model);
		return ajaxResult;
	}

	public AjaxResult createDebt(String transactionNo, String orderId) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanInfoDTO loanInfo = loanInfoService.loadByTransactionNo(transactionNo);
		if (loanInfo.getContractStatus() != LoanInfoContractStatusEnum.NOT_GENERATED.getStatus()) {
			logger.info("货柜流水号为 {}的借据合同已存在", transactionNo);
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("非法请求 借据已存在");
			return ajaxResult;
		}

		int userId = getUserIdByOrderId(orderId);
		LoanUserAuthInfoDTO loanUserAuthInfo = loanUserAuthInfoService.loadByUserId(userId);
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		// if(loanUserCreditInfoDTO.getContractId() == 0 || )
		CreateEcontractBean createBean = new CreateEcontractBean();

		createBean.setSource(source);
		createBean.setCustomerId(loanUserAuthInfo.getCustomerId());
		int templateId = Integer.parseInt(envService.getConfig("debt.template.id"));
		createBean.setTemplateId(templateId);
		Map<String, String> parameters = buildParameters(loanUserAuthInfo, loanUserCreditInfoDTO, loanInfo);
		createBean.setParameters(parameters);
		ResponseVo createResponse = econtractService.createEcontract(createBean);
		if (!createResponse.isSuccess()) {
			logger.info("货柜流水号为 {}的借据创建合同失败", transactionNo);
			ajaxResult.setCode(createResponse.getCode());
			ajaxResult.setMsg(createResponse.getMessage());
			return ajaxResult;
		}
		ResponseVo queryResponse = econtractService
				.queryContractUrlById(new QueryEContractBean((Long) createResponse.getData(),source));
		if (!queryResponse.isSuccess()) {
			logger.info("获取合同编号为{}的借据地址失败", createResponse.getData());
			ajaxResult.setCode(queryResponse.getCode());
			ajaxResult.setMsg(queryResponse.getMessage());
			return ajaxResult;
		}
		ajaxResult.setData(queryResponse.getData());
		LoanInfoDTO model = new LoanInfoDTO();
		BeanUtils.copyProperties(loanInfo, model);
		model.setContractId((Long) createResponse.getData());// 借据合同ID
		model.setContractStatus(LoanInfoContractStatusEnum.UNSIGNED.getStatus());
		model.setProjectCode(transactionNo);
		loanInfoService.update(model);
		return ajaxResult;
	}

	public AjaxResult sendDebtCaptcha(String orderId, String transactionNo) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
				LoanSmsBizTypeEnum.BANKER_CONTRACT.getType());

		String smsTemplateId = "";
		if("product".equals(EnvService.getEnv())){
			//生产环境的时候
			smsTemplateId = envService.getConfig("contract.sms.template.id");
		}

		ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(getCurrentConfig(loanSmsContactsConfigDTO),
				transactionNo, 0, smsTemplateId,source));
		if (!response.isSuccess()) {
			logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		ajaxResult.setMsg("短信发送成功!");
		return ajaxResult;
	}

	@Transactional(rollbackFor = Exception.class)
	public AjaxResult signDebtContract(String orderId, final long contractId, String transactionNo, final String captchaCode) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();

		try {
			// 使用分布式锁防止重复提交
			long count = cacheClient.setnx("USER_" + orderId, String.valueOf(contractId));
			if(count==0){
				ajaxResult.setMsg("不能重复提交");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				return ajaxResult;
			}

			cacheClient.expire("USER_" + orderId, 15);


			LoanInfoDTO loanInfoDTO = loanInfoService.loadByTransactionNo(transactionNo);

			Validate.notNull(loanInfoDTO,"贷款信息不存在.");

			int contractStatus = loanInfoDTO.getContractStatus();

			int userId = getUserIdByOrderId(orderId);
			LoanUserAuthInfoDTO loanUserAuthInfo = loanUserAuthInfoService.loadByUserId(userId);

			final String projectCode = transactionNo;
			LoanSmsContactsConfigDTO loanSmsContactsConfigDTO = loanSmsContactsConfigService.getByProjectAndBizType("fruit",
					LoanSmsBizTypeEnum.BANKER_CONTRACT.getType());

			final int customerId = getCurrentConfig(loanSmsContactsConfigDTO);

			final String signLocationIp  = WebContext.getRequest().getRemoteHost();

			if(LoanInfoContractStatusEnum.SIGNED_COMPLETED.getStatus() != contractStatus){
				// 短信验证
				VerifyCaptchaBean verifyCaptchaBean = new VerifyCaptchaBean();

				verifyCaptchaBean.setSource(source);
				verifyCaptchaBean.setCustomerId(customerId);
				verifyCaptchaBean.setProjectCode(projectCode);
				verifyCaptchaBean.setCaptchCode(captchaCode);
				ResponseVo resVo = econtractService.verifyCaptcha(verifyCaptchaBean);


				if (!resVo.isSuccess()) {
//					ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
//					ajaxResult.setMsg(resVo.getMessage());
//					return ajaxResult;
					throw new IllegalStateException(resVo.getMessage());
				}
			}

			if (contractStatus == LoanInfoContractStatusEnum.UNSIGNED.getStatus()) {
				// 客户未签名
				SignWithoutCaptchaBean custSignBean = new SignWithoutCaptchaBean();
				custSignBean.setContractId(contractId);
				custSignBean.setCustomerId(loanUserAuthInfo.getCustomerId());
				custSignBean.setProjectCode(getTransactionNoByOrderId(orderId));
				custSignBean.setSignature("本单位(人)向九江银行");
				custSignBean.setXaxisOffset(50f);
				custSignBean.setSource(source);
				custSignBean.setYaxisOffset(-50f);
				custSignBean.setSignLocationIp(signLocationIp);
				ResponseVo custSignResponse = econtractService.signEcontractWithoutCaptcha(custSignBean);
				if (!custSignResponse.isSuccess()) {
					logger.info("合同编号为 {}的借据客户(编号为： {} )签署失败", contractId, loanUserAuthInfo.getCustomerId());
					ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
					ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + custSignResponse.getMessage());
					throw new IllegalStateException(custSignResponse.getMessage());
				}
				// 更新状态
				LoanInfoDTO model = new LoanInfoDTO();
				BeanUtils.copyProperties(loanInfoDTO, model);
				model.setContractStatus(LoanInfoContractStatusEnum.BANK_NOT_SIGN.getStatus());
				loanInfoService.update(model);
				contractStatus = LoanInfoContractStatusEnum.BANK_NOT_SIGN.getStatus();
			}

			if (contractStatus == LoanInfoContractStatusEnum.BANK_NOT_SIGN.getStatus()) {
				// 银行未签署

				DebtMQbean bean = new DebtMQbean();
				bean.setProjectCode(projectCode);
				bean.setUserId(customerId);
				bean.setContractId(contractId);
				bean.setCaptchaCode(captchaCode);
				bean.setSignLocationIp(signLocationIp);
				pushDebtContractProxy.sendMsgCon(JsonUtil.toString(bean));

			}

			if (contractStatus == LoanInfoContractStatusEnum.SIGNED_COMPLETED.getStatus()) {
				// 直接推送至资金服务
				//借据到期日
				Date dbtExpTime =getDbtExpTime();
				loanInfoDTO.setDbtExpDt(dbtExpTime);
				int loanFlag = loanInfoService.singleStepLoan(loanInfoDTO, InterfaceRequestTypeEnum.ADMIN);

			}
		} catch (RuntimeException e) {
			logger.info("签署异常.",e.getMessage());
//			ajaxResult.setMsg("签署异常.");
//			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
//			return ajaxResult;
			throw new IllegalStateException("签署异常.");

		}finally {
			// 释放锁
			cacheClient.del("USER_" + orderId);
		}

		return ajaxResult;
	}

	private Integer getCurrentConfig(LoanSmsContactsConfigDTO config) {
		String value;
		if ("product".equals(EnvService.getEnv())) {
			value = config.getProduct();
		} else if ("beta".equals(EnvService.getEnv())) {
			value = config.getBeta();
		} else if ("dev".equals(EnvService.getEnv())) {
			value = config.getDev();
		} else {
			value = config.getAlpha();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 组装借据参数
	 * 
	 * @param loanUserAuthInfo
	 * @param loanUserCreditInfoDTO
	 * @return
	 */
	private Map<String, String> buildParameters(LoanUserAuthInfoDTO loanUserAuthInfo,
			LoanUserCreditInfoDTO loanUserCreditInfoDTO, LoanInfoDTO loanInfo) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("borrower", loanUserAuthInfo.getUsername());
		// 查询借款金额
		//parameters.put("loanAccount", loanUserCreditInfoDTO.getCtrBankNo());
		parameters.put("loanAccount", "");
		parameters.put("savingsAccount", loanUserCreditInfoDTO.getCtrBankNo());
		parameters.put("loanContractNo", loanUserCreditInfoDTO.getCtrNo());
		parameters.put("guaranteeContractNo", loanUserCreditInfoDTO.getInsureCtrNo());
		parameters.put("loanAmount", loanInfo.getConfirmLoan().toString());
		parameters.put("loanAmountZh", MoneyUtil.toChinese(loanInfo.getConfirmLoan().toString()));
		// 获取借据到期时间
		int loanBorrowingTime = Integer.valueOf(runtimeConfigurationService.getConfig(
				RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_LOAN, LoanInterfaceConfig.LOAN_BORROWING_TIME));
		Date expireDate = DateUtil.addDay(new Date(), loanBorrowingTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		parameters.put("loanDate", sdf.format(new Date()));
		parameters.put("expireDate", sdf.format(expireDate));
		// 年利率
		double yearInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(
				RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE)) * 100;
		parameters.put("interestRate", String.valueOf(yearInterestRate));
		return parameters;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		this.source = envService.getConfig("contract.source");

	}

	/**
	 * 获取借据到期时间
	 * @return
	 */
	private Date getDbtExpTime(){
		int loanBorrowingTime = Integer.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_LOAN, LoanInterfaceConfig.LOAN_BORROWING_TIME));
		return com.fruit.loan.biz.socket.util.DateUtil.addDay(new Date(),loanBorrowingTime);
	}


	/**
	 * 根据orderId获取userId
	 * 可能是新订单，可能是旧订单
	 * @return
     */
	private int getUserIdByOrderId(String orderId){
		OrderDTO order = orderService.loadByNo(orderId);
		int userId = 0;
		if(order!=null){
			userId = order.getUserId();
		}else{
			OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderId);
			userId = orderNewInfoDTO.getUserId();
		}
		return userId ;
	}

	/**
	 * 根据orderId获取TransactionNo
	 * 可能是新订单，可能是旧订单
	 * @return
	 */
	private String getTransactionNoByOrderId(String orderId){
		OrderDTO order = orderService.loadByNo(orderId);
		String transactionNo = "";
		if(order!=null){
			transactionNo = order.getTransactionNo();
		}else{
			OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderId);
			transactionNo = orderNewInfoDTO.getOrderSerialNo();
		}
		return transactionNo ;
	}

}
