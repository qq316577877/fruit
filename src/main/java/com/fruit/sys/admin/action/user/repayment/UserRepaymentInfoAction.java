/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.repayment;


import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.request.UserLoanManagementQueryRequest;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
import com.fruit.sys.admin.model.user.loanManagement.RepaymentCollectModel;
import com.fruit.sys.admin.service.user.MemberService;
import com.fruit.sys.admin.service.user.loanManagement.UserLoanManagementListService;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.biz.common.Page;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:贷后管理页面
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserRepaymentInfoAction.java
 */
@Component
@UriMapping("/user/repayment")
public class UserRepaymentInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepaymentInfoAction.class);

    private static final List<IdValueVO> LOANMANAGEMENT_STATUS_LIST = new ArrayList<IdValueVO>(8);

    static
    {
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        //前端页面，供查询的7个状态：已放款、已还款、待还款、还款失败、保证金还款成功、保证金还款失败、对账-待确认
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.SECURED_LOAN.getStatus(), LoanInfoStatusEnum.SECURED_LOAN.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.REPAYMENTS.getStatus(), LoanInfoStatusEnum.REPAYMENTS.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.REPAYMENT.getStatus(), LoanInfoStatusEnum.REPAYMENT.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus(), LoanInfoStatusEnum.REPAYMENT_FAILURE.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus(), LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus(), LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus(), LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getMessage()));
    }


    @Autowired
    private UserLoanManagementListService userLoanManagementListService;


    //贷后管理包含状态--包含 已放款、已还款、待还款、还款失败、保证金还款成功、保证金还款失败、对账-待确认
    private static final List<Integer> LOAD_REPAYMENT_STATUS_LIST = new ArrayList<Integer>(7);
    static
    {
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.SECURED_LOAN.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENTS.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.REPAYMENT_FAILURE.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_SUCCESS.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.MARGIN_REPAYMENT_FAILURE.getStatus());
        LOAD_REPAYMENT_STATUS_LIST.add(LoanInfoStatusEnum.RECONCILIATION_CONFIRMED.getStatus());
    }


    @UriMapping("/list")
    public String userLoanInfoList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = 10;
        String keyword = super.getStringParameter("keyword", "");
        int type = super.getIntParameter("type", -1);
        int status = super.getIntParameter("status", -1);
        int sortby = super.getIntParameter("sortby", 0);

        try
        {
            UserLoanManagementQueryRequest queryRequest = new UserLoanManagementQueryRequest();

            List<Integer> statusList = new ArrayList<Integer>();
            if(status==-1)
            {
                statusList = LOAD_REPAYMENT_STATUS_LIST;
            } else
            {
                statusList.add(status);
            }

            queryRequest.setStatusList(statusList);
            queryRequest.setKeyword(keyword);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<LoanManagementModel> loanManagementModelList = userLoanManagementListService.loadUserLoanManagementList(queryRequest, pageNo, pageSize);
            RepaymentCollectModel repaymentCollectModel = userLoanManagementListService.loadRepaymentCollectModel();
            Page<LoanManagementModel> pageDto = new Page<LoanManagementModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(loanManagementModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            request.setAttribute("sortby", sortby);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("repayment_collect_model", repaymentCollectModel);//总额、笔数
            request.setAttribute("user_loanMangement_list", loanManagementModelList);
            request.setAttribute("loanManagement_status_list", LOANMANAGEMENT_STATUS_LIST);
            request.setAttribute("user_repayment_list_url", UrlUtils.getUserRepaymentListUrl(super.getDomain()));
            request.setAttribute("user_repayment_info_detail_url", UrlUtils.getUserRepaymentDetailInfoUrl(super.getDomain()));
            request.setAttribute("update_user_repayment_info_ajax", UrlUtils.getUserRepaymentInfoUpdateUrl(super.getDomain()));

            return "/user/repayment/user_repayment_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/repayment].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }




}
