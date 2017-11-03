/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.credit;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.loan.biz.common.LoanUserApplyCreditRejectNodeEnum;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.common.LoanUserMarriageCodeEnum;
import com.fruit.loan.biz.dto.LoanUserApplyCreditDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.sys.SysLoanUserApplyCreditService;
import com.fruit.newOrder.biz.common.ContainerStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.user.Loan.UserLoanModel;
import com.fruit.sys.admin.model.user.credit.UserCreditModel;
import com.fruit.sys.admin.model.user.credit.UserCreditVO;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.credit.UserCreditListService;
import com.fruit.sys.admin.service.user.credit.UserCreditResultService;
import com.fruit.sys.admin.service.user.loan.UserLoanListService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.admin.utils.WechatConstants;
import com.fruit.sys.biz.dto.AdminRoleDTO;
import com.fruit.sys.biz.service.AdminRoleService;
import com.fruit.sys.biz.service.AdminRoleUserService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import freemarker.template.utility.NumberUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserDetailAction.java
 */
@Component
@UriMapping("/user/credit")
public class UserCreditDetailAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreditDetailAction.class);

    private static final List<IdValueVO> Credit_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        Credit_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        LoanUserCreditStatusEnum[] values = LoanUserCreditStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanUserCreditStatusEnum status : values)
            {
                Credit_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }

    private static final List<IdValueVO> LOAN_REJECT_LIST = new ArrayList<IdValueVO>();

    static
    {

        LoanUserApplyCreditRejectNodeEnum[] values = LoanUserApplyCreditRejectNodeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanUserApplyCreditRejectNodeEnum status : values)
            {
                LOAN_REJECT_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }

    private static final List<IdValueVO> LOAN_MARRIAGE_LIST = new ArrayList<IdValueVO>();

    static
    {

        LoanUserMarriageCodeEnum[] values = LoanUserMarriageCodeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanUserMarriageCodeEnum status : values)
            {
                LOAN_MARRIAGE_LIST.add(new IdValueVO(status.getCode(), status.getMessage()));
            }
        }
    }


    @Autowired
    private UserCreditListService userCreditListService;

    @Autowired
    private UserCreditResultService userCreditResultService;

    @Autowired
    private SysLoanUserApplyCreditService sysLoanUserApplyCreditService;

    @Autowired
    private UserLoanListService userLoanListService;

    @Autowired
    private LoanMessageService loanMessageService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private WeChatBaseService weChatBaseService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AdminRoleUserService adminRoleUserService;

    @UriMapping("/detail")
    public String showDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_credit_list_url", UrlUtils.getUserCreditInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/loan/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/credit/user_credit_detail";
    }

    @UriMapping("/detail/base")
    public String showBaseInfo()
    {
        try {
            int userCreditId = super.getIntParameter("id", 0);
            UserCreditModel userCreditModel = userCreditListService.loadUserCreditModel(userCreditId);
            Validate.isTrue(null != userCreditModel, "用户账号异常!");

            boolean displayFlag = true;
//            //获取当前用户角色  暂时不做权限控制
//            int currentUserId = super.getLoginUserId();
//            //登陆用户的角色
//            List<Integer> roleList = adminRoleUserService.loadRoleIdsBySysId(currentUserId);
            //判断是否可以修改保证合同编码、贷款银行账户
//            for(Integer roleDemo:roleList){
//
//            }
            //当前信息的状态
            int status = userCreditModel.getStatus();
            if(LoanUserCreditStatusEnum.UNAUTHORIZED.getStatus() == status){
                //已授信未激活
                displayFlag =false;
            }

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("userCredit", userCreditModel);
            request.setAttribute("id", userCreditId);
            request.setAttribute("displayFlag", displayFlag);
            request.setAttribute("credit_status_list", Credit_STATUS_LIST);
            request.setAttribute("update_user_credit_info_ajax", UrlUtils.getUserCreditInfoListUpdateUrl(super.getDomain()));
            request.setAttribute("update_contract_credit_info_ajax", UrlUtils.getContractCreditInfoListUpdateUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/credit/detail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/credit/user_credit_detail_base";
    }

    @UriMapping("/detail/loan")
    public String showLoanDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_loan_list_url", UrlUtils.getUserLoanInfoListUrl(super.getDomain()));
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/loan/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/loan/user_loan_detail";
    }

    @UriMapping("/detail/apply")
    public String showApplyBaseInfo()
    {
        try {
            int userLoanId = super.getIntParameter("id", 0);
            UserCreditModel userCreditModel = userCreditListService.loadUserCreditModel(userLoanId);
            LoanUserApplyCreditDTO userApplyInfo = sysLoanUserApplyCreditService.loadByUserId(userCreditModel.getUserId());
            UserLoanModel userLoanModel = userLoanListService.loadUserLoanModel(userApplyInfo.getId(), true);
            userLoanModel.setId(userCreditModel.getId());
            userLoanModel.setStatus(userCreditModel.getStatus());
            userLoanModel.setRejectDescription((userCreditModel.getDescription()));
            userLoanModel.setRejectNote((userCreditModel.getRejectNote()));

            Validate.isTrue(null != userCreditModel, "用户账号异常!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("userLoan", userLoanModel);
            request.setAttribute("id", userLoanId);
            request.setAttribute("save_base_info_ajax", UrlUtils.getSaveUserBaseInfoUrl(super.getDomain()));
            request.setAttribute("update_user_credit_info_ajax", UrlUtils.getUserCreditInfoListUpdateUrl(super.getDomain()));
            request.setAttribute("update_contract_credit_info_ajax", UrlUtils.getContractCreditInfoListUpdateUrl(super.getDomain()));
            request.setAttribute("loan_status_list", Credit_STATUS_LIST);
            request.setAttribute("user_loan_list_url",UrlUtils.getUserLoanInfoListUrl(super.getDomain()));
            request.setAttribute("loan_marriage_list", LOAN_MARRIAGE_LIST);
            request.setAttribute("loan_reject_list", LOAN_REJECT_LIST);

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/loan/user_loan_detail_apply";
    }

    @UriMapping(value = "/detail/update_user_credit_info_ajax" ,interceptors = "validationInterceptor")
    public JsonResultDTO updateUserCreditInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        int userCreditId = (Integer) validationResults.get("id");
        int status = (Integer) validationResults.get("status");
        String rejectNote = (String) validationResults.get("rejectNote");
        String description = (String) validationResults.get("rejectDescription");

        try{
            UserCreditVO userCreditVO = new UserCreditVO();
            userCreditVO.setId(userCreditId);
            userCreditVO.setStatus(status);
            userCreditVO.setRejectNote(rejectNote);
            userCreditVO.setDescription(description);

            HttpServletRequest request  = WebContext.getRequest();
            request.setAttribute("userLoanId", userCreditId);
            userCreditResultService.updateUserCreditInfo(userCreditVO,super.getUserIp());

            UserCreditModel userCreditModel = userCreditListService.loadUserCreditModel(userCreditId);
            LoanUserApplyCreditDTO userApplyInfo = sysLoanUserApplyCreditService.loadByUserId(userCreditModel.getUserId());
            UserLoanModel userLoanModel = userLoanListService.loadUserLoanModel(userApplyInfo.getId(), true);
            userLoanModel.setId(userCreditModel.getId());
            userLoanModel.setStatus(userCreditModel.getStatus());
            userLoanModel.setRejectDescription((userCreditModel.getDescription()));


            //如果是拒绝批复，则发送短信通知
            if(LoanUserCreditStatusEnum.UNPASS.getStatus()==status) {
                loanMessageService.sendSms(userLoanModel.getUserId(), userLoanModel.getMobile(), "【九创金服】尊敬的客户，您在九创金服提交的资金服务申请已审批拒绝，如有疑问，请咨询您的专属客户经理。");


                //微信通知
                //注册信息
                UserAccountDTO userAccountDTO = userAccountService.loadById(userLoanModel.getUserId());

                //微信提示
                String openid = userAccountDTO.getOpenid();
                if (StringUtils.isNotBlank(openid)) {
                    TemplateVO templateVO = new TemplateVO();
                    templateVO.setToUser(openid);
                    String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_4);
                    String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
                    templateVO.setTemplateId(templateId);
                    templateVO.setUrl(urlEnter);

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
                    dataListP.add(new TemplateParamVO("first", "尊敬的客户，您在九创金服提交的资金服务申请已审批拒绝.", "#000000"));
                    dataListP.add(new TemplateParamVO("keyword1", userApplyInfo.getUsername(), "#000000"));
                    dataListP.add(new TemplateParamVO("keyword2", userApplyInfo.getEnterpriseName(), "#000000"));
                    dataListP.add(new TemplateParamVO("keyword3", format.format(userApplyInfo.getAddTime()), "#000000"));
                    dataListP.add(new TemplateParamVO("remark", "如有疑问，请咨询您的专属客户经理。", "#000000"));

                    templateVO.setTemplateParamList(dataListP);

                    weChatBaseService.sendMessage(templateVO);
                }
            }


            return new JsonResultDTO(userLoanModel);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/detail/update_user_credit_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }

    }

    @UriMapping(value = "/detail/update_contract_credit_info_ajax" ,interceptors={"logInterceptor","validationInterceptor"})
    public JsonResultDTO updateContractCreditInfo()
    {

        int userCreditId = (Integer) super.getIntParameter("id");
        String ctrBankNo = (String) super.getStringParameter("ctrBankNo");
        String insureCtrNo = (String) super.getStringParameter("insureCtrNo");


        try{
            Validate.notEmpty(ctrBankNo);
            Validate.notEmpty(insureCtrNo);
            Pattern patternBankno = Pattern.compile("[0-9]*");
            Pattern patterninsureNo = Pattern.compile("[a-z0-9A-Z]*");

            if(!patternBankno.matcher(ctrBankNo).matches() ||  !patterninsureNo.matcher(insureCtrNo).matches()){
                throw new IllegalArgumentException();
            }
            UserCreditVO userCreditVO = new UserCreditVO();
            userCreditVO.setId(userCreditId);
            userCreditVO.setCtrBankNo(ctrBankNo);
            userCreditVO.setInsureCtrNo(insureCtrNo);

            LoanUserCreditInfoDTO userLoanModel = userCreditResultService.updateContractCreditInfo(userCreditVO,super.getUserIp());


            return new JsonResultDTO(userLoanModel);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/detail/update_user_credit_info_ajax].Exception:{}",e);
            return new JsonResultDTO("请输入有效的银行账号或合同编号！");
        }

    }

}
