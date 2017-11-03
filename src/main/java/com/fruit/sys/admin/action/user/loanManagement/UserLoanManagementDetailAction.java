/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.loanManagement;


import com.fruit.loan.biz.common.LoanInfoContractStatusEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.order.biz.dto.LogisticsDTO;
import com.fruit.order.biz.service.LogisticsService;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
import com.fruit.sys.admin.model.user.loanManagement.UserLoanManagementVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.econtract.LoanApplyQuotaService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementResultService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.ovft.contract.api.bean.QueryEContractBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.service.EcontractService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserDetailAction.java
 */
@Component
@UriMapping("/user/loanManagement")
public class UserLoanManagementDetailAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoanManagementDetailAction.class);

    private static final List<IdValueVO> LOANMANAGEMENT_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        LoanInfoStatusEnum[] values = LoanInfoStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanInfoStatusEnum status : values)
            {
                LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }



    @Autowired
    private UserLoanManagementListService userLoanManagementListService;

    @Autowired
    private UserLoanManagementResultService userLoanManagementResultService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private EcontractService econtractService;

    @Autowired
    private LoanApplyQuotaService loanApplyQuotaService;

    @Resource
    private EnvService envService;

    @Autowired
    private LogisticsService logisticsService;

    @Autowired
    private  OrderNewInfoService orderNewInfoService;




    @UriMapping("/detail")
    public String showDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_loanManagement_list_url", UrlUtils.getUserLoanManagementInfoListUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/loan/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/loanManagement/user_loanManagement_detail";
    }

    @UriMapping("/detail/base")
    public String showBaseInfo()
    {
        try {
            int userLoanMangementId = super.getIntParameter("id", 0);
            LoanManagementModel userLoanManagementModel = userLoanManagementListService.loadUserLoanManagementModel(userLoanMangementId, true);
            Validate.isTrue(null != userLoanManagementModel, "用户账号异常!");

            UserInfo userInfo = super.getLoginUserInfo();
            boolean flag = false;
            //后台管理系统的角色为 系统管理员 或 信贷客户经理时，能看到按钮
            if (userInfo.getType()==1||userInfo.getType()==6)
            {
                flag = true;
            }

            //添加采购合同
            String contractUrl = "";
            String orderno = userLoanManagementModel.getOrderNo();
            LogisticsDTO logisticsDto = logisticsService.loadByOrderNo(orderno);
            if(logisticsDto != null){
                contractUrl = logisticsDto.getContractUrl();
            }

            //查询新订单表
            OrderNewInfoDTO orderNewInfoDTO = orderNewInfoService.loadByOrderNo(orderno);

            if(orderNewInfoDTO != null && (contractUrl ==null || contractUrl.length() < 1)){

                long contractId = orderNewInfoDTO.getContractId();//采购合同ID

                if(contractId >0) {
                    ResponseVo responseVo = econtractService.queryContractUrlById(new QueryEContractBean(contractId, envService.getConfig("contract.source")));
                    if (!responseVo.isSuccess()) {
                        LOGGER.info("查询采购合同地址失败 具体原因: {}", responseVo.getMessage());
                    } else {
                        contractUrl = (String) responseVo.getData();
                    }
                }

            }

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("userLoanManagement", userLoanManagementModel);
            request.setAttribute("id", userLoanMangementId);
            request.setAttribute("flag", flag);
            request.setAttribute("contractUrl", contractUrl);
            request.setAttribute("loanmanagement_status_list",LOANMANAGEMENT_STATUS_LIST);
            request.setAttribute("update_user_loanManagement_info_ajax", UrlUtils.getUserLoanManamgementInfoUpdateUrl(super.getDomain()));
            request.setAttribute("check_user_loanManagement_info_ajax", UrlUtils.getUserLoanManamgementInfoCheckUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/loanManagement/user_loanManagement_detail_base";
    }

    @UriMapping("/detail/map")
    public String showMapInfo()
    {
        boolean clearFlag  = super.getBooleanParameter("clearFlag");
        String deliveryTime = getStringParameter("deliveryTime");
        String preReceiveTime = getStringParameter("preReceiveTime");
        String containerId = getStringParameter("containerId");

        try {
            String clearAddress =java.net.URLDecoder.decode(super.getStringParameter("clearAddress"),"UTF-8");;
            String receiverAddress = java.net.URLDecoder.decode(super.getStringParameter("receiverAddress"),"UTF-8");
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("containerNo", containerId);
            request.setAttribute("clearFlag", clearFlag);
            request.setAttribute("clearAddress", clearAddress);
            request.setAttribute("receiverAddress", receiverAddress);
            request.setAttribute("deliveryTime", deliveryTime);
            request.setAttribute("preReceiveTime", preReceiveTime);

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("[system][/detail/map].Exception:{}",e);
        }

        return "/user/loanManagement/map";
    }

    @UriMapping(value = "/detail/update_user_loanManagement_info_ajax" ,interceptors = "validationInterceptor")
    public JsonResultDTO updateUserLoanInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        int userLoanId = (Integer) validationResults.get("id");
        int userId = (Integer) validationResults.get("userId");
        int status = (Integer) validationResults.get("status");
        BigDecimal confirmLoan = new BigDecimal(validationResults.get("confirmLoan").toString());
        Date dbtExpDt = DateUtil.StringToDate( (String)validationResults.get("dbtExpDtString"));

        try{
            UserLoanManagementVO userLoanManagementVO = new UserLoanManagementVO();
            userLoanManagementVO.setId(userLoanId);
            userLoanManagementVO.setUserId(userId);
            userLoanManagementVO.setStatus(status);



            if(status==LoanInfoStatusEnum.NOT_THROUGH.getStatus())
            {
                userLoanManagementResultService.updateUserLoanInfo(userLoanManagementVO,super.getUserIp());
            }

            HttpServletRequest request  = WebContext.getRequest();
            request.setAttribute("userLoanId", userLoanId);
            LoanManagementModel loanManagementModel = userLoanManagementListService.loadUserLoanManagementModel(userLoanId,true);
            return new JsonResultDTO(loanManagementModel);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/loanManagement/update_user_loanManagement_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }

    }


    @UriMapping(value = "/detail/loanCheckAjax" ,interceptors = "validationInterceptor")
    public JsonResultDTO checkLoanCondition()
    {
        try {
            //验证是否可以放款
            if(loanInfoService.checkIfLoanSocketTimeScanable()){
            //if(true){
                return new JsonResultDTO();
            }else{
                return new JsonResultDTO("当前时间无法放款，请选择06:00-18:00之间");
            }
        }catch (Exception e) {
            LOGGER.error("[system][/loanManagement/loanCheckAjax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    @UriMapping(value = "/detail/toSignContract" )
    public String toSignContract()
    {

        int userLoanId = super.getIntParameter("id");

        try
        {
            LoanInfoDTO dto = loanInfoService.loadById(userLoanId);
            Validate.notNull(dto);

            int statusContract = dto.getContractStatus();
            String contractUrl = "";
            Long contractId = dto.getContractId();
            String errorMsg = "";
            int errorCode = 0;

            if(LoanInfoContractStatusEnum.NOT_GENERATED.getStatus() == statusContract){
                //未生成合同
                AjaxResult creatrst = loanApplyQuotaService.createDebt(dto.getTransactionNo(),dto.getOrderNo());

                if(creatrst.getCode() == 200){
                    //创建成功
                    contractUrl = (String) creatrst.getData();
                    LoanInfoDTO dtoTemp = loanInfoService.loadById(userLoanId);
                    contractId = dtoTemp.getContractId();
                }else{
                    errorCode =-1;
                    errorMsg = "合同创建失败!";
                }
            }else{
                //已有合同
                String source = envService.getConfig("contract.source");
                ResponseVo queryResponse = econtractService
                        .queryContractUrlById(new QueryEContractBean(contractId,source));
                if (queryResponse.isSuccess()) {
                    contractUrl = queryResponse.getData().toString();
                }else{
                    errorCode = -1;
                    errorMsg = "合同地址查找失败!";
                }

            }


            //预埋数据
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("dataId",userLoanId);
            request.setAttribute("orderNo",dto.getOrderNo());
            request.setAttribute("transactionNo",dto.getTransactionNo());
            request.setAttribute("contractId",contractId);
            request.setAttribute("contractUrl",contractUrl);
            request.setAttribute("contractStatus",statusContract);
            request.setAttribute("errorCode",errorCode);
            request.setAttribute("errorMsg",errorMsg);
            request.setAttribute("captcha_send_ajax", UrlUtils.getLoanCaptchaSendAjaxUrl(super.getDomain()));
            request.setAttribute("sign_debt_contract_ajax", UrlUtils.getLoanSignDebtContractAjaxUrl(super.getDomain()));

            return "/user/loanManagement/loan_signcontract";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/loan/contract/toSignContract].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

}
