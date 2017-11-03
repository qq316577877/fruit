package com.fruit.sys.admin.service.neworder.process;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
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
public class ReceiveContainerProcessor extends DefaultContainerProcessor {



	public ReceiveContainerProcessor() {
		super(ContainerEventEnum.RECEIVE);
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

		//签收
		final String content =  containerStateMachine.getSmsTemplate(request.getStatusBefore(),ContainerEventEnum.RECEIVE)
				.replace("{orderNo}", containerInfoDTO.getOrderNo()).replace("{containerNo}", containerInfoDTO.getContainerNo())
				.replace("{containerName}", containerInfoDTO.getContainerName());

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

			StringBuilder firstStr = new StringBuilder("尊敬的客户，您有货柜已签收.");
			firstStr.append(" 货柜批次号：");
			firstStr.append(containerInfoDTO.getContainerNo());
			firstStr.append(" 货柜状态：");
			firstStr.append(nextStatus.getUserDesc());

			StringBuilder remark = new StringBuilder("");
			remark.append(" 商品金额：");
			remark.append(containerInfoDTO.getProductAmount()+" 元");
			remark.append(" 贷款金额：");
			remark.append(containerInfoDTO.getLoanAmount()+" 元");
			remark.append(" 如有疑问，请致电4008-265-128。");

			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
			dataListP.add(new TemplateParamVO("first", firstStr.toString(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword1", containerInfoDTO.getContainerName(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword2", containerInfoDTO.getOrderNo(), "#000000"));
			dataListP.add(new TemplateParamVO("keyword3", "该货柜已签收，请登录平台查看详情。", "#ff0000"));
			dataListP.add(new TemplateParamVO("remark", remark.toString(), "#000000"));


			template.setTemplateParamList(dataListP);

			weChatBaseService.sendMessage(template);
		}
	}


}
