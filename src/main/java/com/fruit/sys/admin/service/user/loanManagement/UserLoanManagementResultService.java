/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.loanManagement;


import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.loan.biz.common.InterfaceRequestTypeEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.loan.biz.dto.LoanInfoDTO;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.service.LoanInfoService;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.service.sys.SysLoanInfoService;
import com.fruit.loan.biz.socket.config.LoanInterfaceConfig;
import com.fruit.loan.biz.socket.util.DateUtil;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.OrderDTO;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.OrderService;
import com.fruit.sys.admin.model.DebtMQbean;
import com.fruit.sys.admin.model.user.loanManagement.UserLoanManagementVO;
import com.fruit.sys.admin.queue.PushDebtContractProxy;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.user.MemberService;
import com.fruit.sys.admin.service.user.UserListService;
import com.fruit.sys.admin.utils.JsonUtil;
import com.fruit.sys.admin.utils.TaskProcessUtil;
import com.ovfintech.concurrent.TaskAction;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.RollbackException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserListService.java
 */
@Service
public class UserLoanManagementResultService extends BaseService
{
    @Autowired
    private SysLoanInfoService sysLoanInfoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserListService userListService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private PushDebtContractProxy pushDebtContractProxy;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private LoanUserAuthInfoService loanUserAuthInfoService;

    @Autowired
    private LoanMessageService loanMessageService;


    public void updateUserLoanInfo(UserLoanManagementVO userLoanManagementVO, String userIp) {

        int userLoanId = userLoanManagementVO.getId();
        int userId = userLoanManagementVO.getId();
//        LoanInfoDTO loanInfoDTO = sysLoanInfoServiceImpl.loadById(userLoanId);
        LoanInfoDTO loanInfoDTO = new LoanInfoDTO();

        loanInfoDTO.setId(userLoanId);
        loanInfoDTO.setUserId(userLoanManagementVO.getUserId());
        loanInfoDTO.setStatus(userLoanManagementVO.getStatus());
        loanInfoDTO.setUpdateTime(new Date());
        sysLoanInfoService.update(loanInfoDTO);

//        cacheManageService.syncUserModel(userLoanId);
        this.memberService.asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin verify user enterprise auth.", UserInfoUpdateLogTypeEnum.PROFILE_CONFIG.getType(), null, userIp);

    }



    /**
     * 获取借据到期时间
     * @return
     */
    private Date getDbtExpTime(){
        int loanBorrowingTime = Integer.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_LOAN,LoanInterfaceConfig.LOAN_BORROWING_TIME));
        return DateUtil.addDay(new Date(),loanBorrowingTime);
    }


    /**
     * 放款通知提醒
     * @param loanInfoDTO
     */
    private void sendMsgOfPayPre(LoanInfoDTO loanInfoDTO){

        String template = "【九创金服】尊敬的客户，您的订单{0}中的货柜{1}已发货，该货柜资金服务申请已审批通过，资金预计将在2小时左右发放，请留意您的银行卡到账通知，如有疑问，请致电4008-265-128。";

        if(loanInfoDTO!=null && loanInfoDTO.getStatus()== LoanInfoStatusEnum.PENDING_AUDIT.getStatus()) {

            int userId = loanInfoDTO.getUserId();

            ContainerDTO container = containerService.loadByTransactionNo(loanInfoDTO.getLoanTransNo());

            //根据userId查询实名认证信息
            LoanUserAuthInfoDTO loanUserAuthInfoDTO = loanUserAuthInfoService.loadByUserId(userId);

            String content = MessageFormat.format(template, container.getOrderNo(), container.getNo() + container.getProductName());

            loanMessageService.sendSms(loanInfoDTO.getUserId(),loanUserAuthInfoDTO.getMobile(), content);
        }

    }
}