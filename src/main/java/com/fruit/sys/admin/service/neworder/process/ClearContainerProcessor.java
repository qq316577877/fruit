package com.fruit.sys.admin.service.neworder.process;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.loan.biz.common.LoanInfoContractStatusEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.newOrder.biz.dto.ContainerInfoDTO;
import com.fruit.newOrder.biz.request.ContainerProcessRequest;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.newOrder.biz.service.impl.ContainerStateMachine;
import com.fruit.newOrder.biz.service.impl.DefaultContainerProcessor;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.MathUtil;
import com.fruit.sys.admin.utils.WechatConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * 提交审核
 */
@Service
public class ClearContainerProcessor extends DefaultContainerProcessor {



	public ClearContainerProcessor() {
		super(ContainerEventEnum.CLEARANCE);
	}


	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RuntimeConfigurationService runtimeConfigurationService;

	@Autowired
	private WeChatBaseService weChatBaseService;

	@Autowired
	private ContainerStateMachine containerStateMachine;

	@Autowired
	private OrderNewInfoService orderNewInfoService;

	@Autowired
	private LoanMessageService loanMessageService;

	@Autowired
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Autowired
	private LoanInfoService loanInfoService;

	@Autowired
	private UserLoanManagementListService userLoanManagementListService;


	@Override
	protected void handleExtraAfter(ContainerProcessRequest request, ContainerStatusEnum nextStatus) {

		ContainerInfoDTO containerInfoDTO = request.getContainerInfo();

		UserAccountDTO userAccountDTO = userAccountService.loadById(containerInfoDTO.getUserid());

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		String preReceiveTime = sdf2.format(containerInfoDTO.getPreReceiveTime());

		//清关
		final String content =  containerStateMachine.getSmsTemplate(request.getStatusBefore(),ContainerEventEnum.CLEARANCE)
				.replace("{orderNo}", containerInfoDTO.getOrderNo()).replace("{containerNo}", containerInfoDTO.getContainerNo())
				.replace("{containerName}", containerInfoDTO.getContainerName()).replace("{preReceiveTime}",preReceiveTime);

		messageService.sendSms(userAccountDTO.getId(), userAccountDTO.getMobile(), content);

		String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_3);
		//微信提醒
		String openid = userAccountDTO.getOpenid();
		if (StringUtils.isNotBlank(openid)) {
			TemplateVO template = new TemplateVO();
			template.setToUser(openid);
			template.setTemplateId(templateId);
			String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
			template.setUrl(urlEnter.replaceFirst("state=1","state=2"));

			StringBuilder firstStr = new StringBuilder("尊敬的客户，您有货柜已通关.");
			firstStr.append(" 货柜批次号：");
			firstStr.append(containerInfoDTO.getContainerNo());
			firstStr.append(" 货柜状态：");
			firstStr.append(nextStatus.getUserDesc());

			StringBuilder remark = new StringBuilder("");
			remark.append(" 商品金额：");
			remark.append(containerInfoDTO.getProductAmount() +" 元");
			remark.append(" 贷款金额：");
			remark.append(containerInfoDTO.getLoanAmount()+" 元");
			remark.append(" 如有疑问，请致电4008-265-128。");

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
			dataListP.add(new TemplateParamVO("first", firstStr.toString(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword1", containerInfoDTO.getContainerName(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword2", containerInfoDTO.getOrderNo(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword3", "该货柜预计"+preReceiveTime+"到货，请于该日期前结算订单尾款", "#ff0000"));
			dataListP.add(new TemplateParamVO("remark", remark.toString(), "#000000"));


			template.setTemplateParamList(dataListP);

			weChatBaseService.sendMessage(template);
		}



		LoanInfoDTO loanInfo = loanInfoService.loadByTransactionNo(containerInfoDTO.getContainerSerialNo());
		// 有贷款信息
		if (loanInfo != null && StringUtils.isNotEmpty(loanInfo.getDbtNo())
				&& (loanInfo.getStatus() == LoanInfoStatusEnum.SECURED_LOAN.getStatus() || loanInfo.getStatus() == LoanInfoStatusEnum.REPAYMENT.getStatus())
				) {
			//更新贷款状态

			userLoanManagementListService.updateFromLoanToPrepay(containerInfoDTO.getContainerSerialNo(),
					containerInfoDTO.getPreReceiveTime());


			// 根据userId查询实名认证信息
			LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(loanInfo.getUserId());

			BigDecimal offerLoan = loanInfo.getOfferLoan();// 本金
			Date OfferTime = loanInfo.getOfferTime();
			Date expiresTime = containerInfoDTO.getPreReceiveTime();// 到期强制还款时间  预计到货时间
			String expiresTimeStr = DateUtil.getDate(expiresTime);
			int interestDays = DateUtil.getIntervalDays(OfferTime, expiresTime);
			// 利率
			double percent = Double.valueOf(runtimeConfigurationService.getConfig(
					RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE));
			// 利息
			double interest = calculateInterest(offerLoan, percent, interestDays);

			double principal = offerLoan.doubleValue() + interest;
			String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}所使用资金将于{2}到期，请于{3}15:00前预存{4}元，以免影响您的会员等级。如有疑问，请致电4008-265-128，感谢您的理解和支持！";
			String contentSms = MessageFormat.format(template, containerInfoDTO.getOrderNo(),
					containerInfoDTO.getContainerNo() + " " + containerInfoDTO.getContainerName(), expiresTimeStr, expiresTimeStr + " ", principal);

			loanMessageService.sendSms(loanInfo.getUserId(), loanUserAuthInfoDTO.getMobile(), contentSms);

			//微信提示

			if(StringUtils.isNotBlank(openid)) {
				TemplateVO templateVO = new TemplateVO();
				templateVO.setToUser(openid);

				templateVO.setTemplateId(templateId);
				String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
				templateVO.setUrl(urlEnter.replaceFirst("state=1","state=2"));

				StringBuilder firstStr = new StringBuilder("尊敬的客户，您有货柜所使用资金即将到期.");
				firstStr.append("货柜批次号：");
				firstStr.append(containerInfoDTO.getContainerNo());
				firstStr.append("货柜状态：");
				firstStr.append("待还款");

				StringBuilder remark = new StringBuilder("");
				remark.append("商品金额：");
				remark.append(containerInfoDTO.getProductAmount()+" 元");
				remark.append("贷款金额：");
				remark.append(loanInfo.getOfferLoan()+" 元");
				remark.append("如有疑问，请致电4008-265-128。");

				List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
				dataListP.add(new TemplateParamVO("first", firstStr.toString(), "#000000"));
				dataListP.add(new TemplateParamVO("keyword1", containerInfoDTO.getContainerName(), "#000000"));
				dataListP.add(new TemplateParamVO("keyword2", containerInfoDTO.getOrderNo(), "#000000"));
				dataListP.add(new TemplateParamVO("keyword3", "该货柜所使用资金将于"+expiresTimeStr+"到期，请于"+expiresTimeStr+"15:00前预存"+principal+"元，以免影响您的会员等级。", "#ff0000"));
				dataListP.add(new TemplateParamVO("remark", remark.toString(), "#000000"));

				templateVO.setTemplateParamList(dataListP);

				weChatBaseService.sendMessage(templateVO);
			}

		}

	}


	/**
	 * 通过本金、利率、借款天数来计算利息
	 *
	 * @param offerLoan
	 * @param percent
	 * @param interestDays
	 */
	private double calculateInterest(BigDecimal offerLoan, double percent, int interestDays) {
		double interest = offerLoan.doubleValue() * percent * interestDays / 360;

		BigDecimal interestBig = MathUtil.doubleToBigDecima(interest);// 保留两位小数

		return interestBig.doubleValue();
	}

}
