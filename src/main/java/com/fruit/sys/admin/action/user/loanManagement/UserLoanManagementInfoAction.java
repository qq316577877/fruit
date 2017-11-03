/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.loanManagement;

import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.loan.biz.common.LoanInfoStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.request.UserLoanManagementQueryRequest;
import com.fruit.sys.admin.model.user.loanManagement.LoanCollectModel;
import com.fruit.sys.admin.model.user.loanManagement.LoanManagementModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 贷款管理页面
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserLoanManagementInfoAction.java
 */
@Component
@UriMapping("/user/loanManagement")
public class UserLoanManagementInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoanManagementInfoAction.class);

    private static final List<IdValueVO> LOANMANAGEMENT_STATUS_LIST = new ArrayList<IdValueVO>(5);

    static
    {
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        //前端页面，供查询的4个状态：待审核 待放款 审核不通过 放款待确认
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.PENDING_AUDIT.getStatus(), LoanInfoStatusEnum.PENDING_AUDIT.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.LOAN_PENDING.getStatus(), LoanInfoStatusEnum.LOAN_PENDING.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.NOT_THROUGH.getStatus(), LoanInfoStatusEnum.NOT_THROUGH.getMessage()));
        LOANMANAGEMENT_STATUS_LIST.add(new IdValueVO(LoanInfoStatusEnum.TO_CONFIRMED.getStatus(), LoanInfoStatusEnum.TO_CONFIRMED.getMessage()));
    }

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserLoanManagementListService userLoanManagementListService;


    //贷款管理包含状态为：待审核 待放款 审核不通过 放款待确认
    private static final List<Integer> LOAD_MANAGEMENT_STATUS_LIST = new ArrayList<Integer>(4);
    static
    {
        LOAD_MANAGEMENT_STATUS_LIST.add(LoanInfoStatusEnum.PENDING_AUDIT.getStatus());//待审核
        LOAD_MANAGEMENT_STATUS_LIST.add(LoanInfoStatusEnum.LOAN_PENDING.getStatus());//待放款
        LOAD_MANAGEMENT_STATUS_LIST.add(LoanInfoStatusEnum.NOT_THROUGH.getStatus());//审核不通过
        LOAD_MANAGEMENT_STATUS_LIST.add(LoanInfoStatusEnum.TO_CONFIRMED.getStatus());//放款待确认
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
                statusList = LOAD_MANAGEMENT_STATUS_LIST;
            } else
            {
                statusList.add(status);
            }

            queryRequest.setStatusList(statusList);
            queryRequest.setKeyword(keyword);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<LoanManagementModel> loanManagementModelList = userLoanManagementListService.loadUserLoanManagementList(queryRequest, pageNo, pageSize);
            LoanCollectModel  loanCollectModel = userLoanManagementListService.loadLoanCollectModel();
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
            request.setAttribute("loan_collect_model", loanCollectModel);//总额、服务费、笔数
            request.setAttribute("user_loanMangement_list", loanManagementModelList);
            request.setAttribute("loanManagement_status_list", LOANMANAGEMENT_STATUS_LIST);
            request.setAttribute("user_loanManagement_list_url", UrlUtils.getUserLoanManagementInfoListUrl(super.getDomain()));
            request.setAttribute("user_loanManagement_info_detail_url", UrlUtils.getUserLoanManagementDetailInfoUrl(super.getDomain()));
            request.setAttribute("update_user_loanMangement_info_ajax", UrlUtils.getUserLoanManagementDetailInfoUrl(super.getDomain()));
            setLoanCounts(request);
            return "/user/loanManagement/user_loanManagement_list";

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    private void setLoanCounts(HttpServletRequest request)
    {
        int total = memberService.userCount(null, null,null);

        Map<String, Integer> statusCountMap = new HashMap<String, Integer>();
        statusCountMap.put("全部", total);
        statusCountMap.put(UserStatusEnum.DELETED.getMessage(), memberService.userCount(UserStatusEnum.DELETED, null,null));
        statusCountMap.put(UserStatusEnum.NORMAL.getMessage(), memberService.userCount(UserStatusEnum.NORMAL, null,null));
        statusCountMap.put(UserStatusEnum.FROZEN.getMessage(), memberService.userCount(UserStatusEnum.FROZEN, null,null));
        request.setAttribute("status_count_map", statusCountMap);
    }



}
