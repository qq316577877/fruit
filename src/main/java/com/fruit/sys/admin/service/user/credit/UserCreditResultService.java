/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.credit;


import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.sys.SysLoanUserApplyCreditService;
import com.fruit.loan.biz.service.sys.SysLoanUserCreditInfoService;
import com.fruit.sys.admin.model.user.credit.UserCreditVO;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.user.loan.UserLoanListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserCreditResultService extends BaseService
{
    @Autowired
    private SysLoanUserCreditInfoService sysLoanUserCreditInfoService;

    @Autowired
    private UserCreditListService userCreditListService;

    @Autowired
    private SysLoanUserApplyCreditService sysLoanUserApplyCreditService;

    @Autowired
    private UserLoanListService userLoanListService;

    @Autowired
    private LoanMessageService loanMessageService;

    public void updateUserCreditInfo(UserCreditVO userCreditVO, String userIp) {

        int userCreditId = userCreditVO.getId();
        LoanUserCreditInfoDTO loanUserCreditInfoDTO = sysLoanUserCreditInfoService.loadById(userCreditId);
        if (loanUserCreditInfoDTO != null)
        {
            loanUserCreditInfoDTO.setId(userCreditId);
            loanUserCreditInfoDTO.setStatus(userCreditVO.getStatus());
            loanUserCreditInfoDTO.setRejectNote(userCreditVO.getRejectNote());
            loanUserCreditInfoDTO.setDescription(userCreditVO.getDescription());
            loanUserCreditInfoDTO.setUpdateTime(new Date());
            sysLoanUserCreditInfoService.update(loanUserCreditInfoDTO);

        }

    }

    @Transactional(rollbackFor = Exception.class)
    public LoanUserCreditInfoDTO updateContractCreditInfo(UserCreditVO userCreditVO, String userIp) {

        int userCreditId = userCreditVO.getId();
        LoanUserCreditInfoDTO loanUserCreditInfoDTO = sysLoanUserCreditInfoService.loadById(userCreditId);
        if (loanUserCreditInfoDTO != null)
        {
            loanUserCreditInfoDTO.setId(userCreditId);
            loanUserCreditInfoDTO.setCtrBankNo(userCreditVO.getCtrBankNo());
            loanUserCreditInfoDTO.setInsureCtrNo(userCreditVO.getInsureCtrNo());
            loanUserCreditInfoDTO.setUpdateTime(new Date());
            sysLoanUserCreditInfoService.update(loanUserCreditInfoDTO);

            //短信通知
//            loanMessageService.sendSms(loanUserCreditInfoDTO.getUserId(), loanUserCreditInfoDTO.getMobile(), "【九创金服】尊敬的客户，您在九创金服的资金服务贷款账户(存款账户)已更新为:"+userCreditVO.getCtrBankNo() + "   " +
//                    ",保证合同编号更新为:"+userCreditVO.getInsureCtrNo() +"。如有疑问，请咨询您的专属客户经理。");

        }



        return loanUserCreditInfoDTO;
    }



}
