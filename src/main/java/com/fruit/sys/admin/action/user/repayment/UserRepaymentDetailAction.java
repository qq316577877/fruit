/*
 *
 * Copyright (c) 2017-2020 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.repayment;

import com.fruit.loan.biz.common.InterfaceRequestTypeEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.common.RepaymentMethodsTypeEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.sys.SysLoanInfoService;
import com.fruit.newOrder.biz.common.ContainerEventEnum;
import com.fruit.newOrder.biz.dto.OrderNewInfoDTO;
import com.fruit.newOrder.biz.service.OrderNewInfoService;
import com.fruit.order.biz.dto.LogisticsDTO;
import com.fruit.order.biz.service.LogisticsService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.ContainerProcessBean;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
import com.fruit.sys.admin.model.user.loanManagement.UserLoanManagementVO;
import com.fruit.sys.admin.queue.PushContainerProcessProxy;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementResultService;
import com.fruit.sys.admin.utils.JsonUtil;
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
import java.math.BigDecimal;
import java.util.ArrayList;
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
@UriMapping("/user/repayment")
public class UserRepaymentDetailAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepaymentDetailAction.class);

    public static final String REPAYMENT_METHODS = "REPAYMENT_METHODS";

    public static final String PERFORMANCE_RATE = "PERFORMANCE_RATE";

    private static final List<IdValueVO> REPAYMENT_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        REPAYMENT_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        LoanInfoStatusEnum[] values = LoanInfoStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanInfoStatusEnum status : values)
            {
                REPAYMENT_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }

    private static final List<IdValueVO> REPAYMENT_TYPE_LIST = new ArrayList<IdValueVO>();
    static
    {

        RepaymentMethodsTypeEnum[] values = RepaymentMethodsTypeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (RepaymentMethodsTypeEnum status : values)
            {
                REPAYMENT_TYPE_LIST.add(new IdValueVO(status.getCode(), status.getMessage()));
            }
        }
    }


    @Autowired
    private UserLoanManagementListService userLoanManagementListService;

    @Autowired
    private UserLoanManagementResultService userLoanManagementResultService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private SysLoanInfoService sysLoanInfoService;

    @Autowired
    private PushContainerProcessProxy pushContainerProcessProxy;

    @Resource
    private EnvService envService;

    @Autowired
    private LogisticsService logisticsService;

    @Autowired
    private OrderNewInfoService orderNewInfoService;

    @Autowired
    private EcontractService econtractService;


    @UriMapping("/detail")
    public String showDetail()
    {
        try {
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("id", userId);
            request.setAttribute("user_repayment_list_url", UrlUtils.getUserRepaymentListUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/loan/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/repayment/user_repayment_detail";
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
            //后台管理系统 系统角色为 系统管理员 或 信贷管理员 ，才能看到操作按钮
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


            String repaymentMethod = runtimeConfigurationService.getConfig(REPAYMENT_METHODS);
            BigDecimal Rate = new BigDecimal(runtimeConfigurationService.getConfig(PERFORMANCE_RATE));
            BigDecimal performanceRate = Rate.multiply(new BigDecimal("100"));

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("contractUrl", contractUrl);
            request.setAttribute("userLoanManagement", userLoanManagementModel);
            request.setAttribute("repaymentMethod", repaymentMethod);
            request.setAttribute("performanceRate", performanceRate);
            request.setAttribute("id", userLoanMangementId);
            request.setAttribute("flag", flag);
            request.setAttribute("repayment_status_list",REPAYMENT_STATUS_LIST);
            request.setAttribute("repayment_type_list",REPAYMENT_TYPE_LIST);

            request.setAttribute("update_user_repayment_info_ajax", UrlUtils.getUserRepaymentInfoUpdateUrl(super.getDomain()));

        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/detail/base].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/repayment/user_repayment_detail_base";
    }

    @UriMapping(value = "/detail/update_user_repayment_info_ajax" ,interceptors = "validationInterceptor")
    public JsonResultDTO updateUserLoanInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        int userLoanId = (Integer) validationResults.get("id");
        int status = (Integer) validationResults.get("status");
        int userId = (Integer) validationResults.get("userId");
        String dbtNo = (String) validationResults.get("dbtNo");

        try{

            LoanInfoDTO loanInfoDTO = sysLoanInfoService.loadById(userLoanId);

            try {
                loanInfoService.prepaymentLoan(loanInfoDTO, InterfaceRequestTypeEnum.ADMIN);
                //更新货柜状态
                ContainerProcessBean containerProcessBean = new ContainerProcessBean();
                containerProcessBean.setEventCode(ContainerEventEnum.SETTLEMENT.getCode());
                containerProcessBean.setLoanInfoId(loanInfoDTO.getId());
                pushContainerProcessProxy.sendMsgCon(JsonUtil.toString(containerProcessBean));

            }catch (Exception e) {
                LOGGER.error("[system][/detail/update_user_repayment_info_ajax].Exception:{}",e);
                return new JsonResultDTO(e.getMessage());
            }

            HttpServletRequest request  = WebContext.getRequest();
            request.setAttribute("userLoanId", userLoanId);
            LoanManagementModel loanManagementModel = userLoanManagementListService.loadUserLoanManagementModel(userLoanId,true);
            return new JsonResultDTO(loanManagementModel);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/detail/update_user_repayment_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }

    }

}
