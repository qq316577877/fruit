package com.fruit.sys.admin.service.neworder.process;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.newOrder.biz.common.ContractStatusEnum;
import com.fruit.newOrder.biz.common.OrderEventEnum;
import com.fruit.newOrder.biz.common.OrderStatusEnum;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.request.OrderProcessRequest;
import com.fruit.newOrder.biz.service.impl.DefaultOrderProcessor;
import com.fruit.newOrder.biz.service.impl.OrderStateMachine;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.neworder.OrderInfoService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.WechatConstants;
import com.ovft.contract.api.bean.CreateEcontractBean;
import com.ovft.contract.api.bean.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟旋 on 2017/10/19.
 */
@Service
public class ConfirmSysOrderProcessor extends DefaultOrderProcessor {


    public ConfirmSysOrderProcessor() {
        super(OrderEventEnum.SYS_CONFIRM);
    }


    @Autowired
    private OrderStateMachine newOrderStateMachine;


    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private WeChatBaseService weChatBaseService;



    @Override
    protected void  handleExtraAfter(OrderProcessRequest request, OrderStatusEnum nextStatus){

        OrderNewInfoDTO orderNewInfoDTO = request.getOrderInfo();

        //短信通知
        final String content =  newOrderStateMachine.getSmsTemplate(request.getStatusBefore(),OrderEventEnum.SYS_CONFIRM)
                .replace("{orderNo}", orderNewInfoDTO.getOrderNo());

        final int userIdSms = orderNewInfoDTO.getUserId();
        UserAccountDTO userInfo = userAccountService.loadById(userIdSms);
        Validate.notNull(userInfo,"用户信息不存在");
        final String mobile = userInfo.getMobile();

        messageService.sendSms(userIdSms, mobile, content);

        //微信提醒
        String openid = userInfo.getOpenid();
        if(StringUtils.isNotBlank(openid)) {
            TemplateVO template = new TemplateVO();
            template.setToUser(openid);
            String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_2);
            String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
            template.setTemplateId(templateId);
            template.setUrl(urlEnter.replaceFirst("state=1","state=2"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
            dataListP.add(new TemplateParamVO("first", "尊敬的客户，您的订单信息经由平台客服与您沟通确认修改后审核通过.", "#000000"));
            dataListP.add(new TemplateParamVO("keyword1", orderNewInfoDTO.getOrderNo(), "#000000"));
            dataListP.add(new TemplateParamVO("keyword2", "客服小九", "#000000"));
            dataListP.add(new TemplateParamVO("keyword3", format.format(new Date()), "#000000"));
            dataListP.add(new TemplateParamVO("remark", "请您尽快登录平台查看订单详情，并确认提交订单。", "#000000"));


            template.setTemplateParamList(dataListP);

            weChatBaseService.sendMessage(template);
        }

    }
}
