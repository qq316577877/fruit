/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user.credit;


import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.request.UserCreditInfoQueryRequest;
import com.fruit.sys.admin.model.user.credit.CreditCollectModel;
import com.fruit.sys.admin.model.user.credit.UserCreditModel;
import com.fruit.sys.admin.service.user.credit.UserCreditListService;
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
 * Description:授信申请
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserCreditInfoAction.java
 */
@Component
@UriMapping("/user/credit")
public class UserCreditInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreditInfoAction.class);

    private static final List<IdValueVO> LOAN_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        LOAN_STATUS_LIST.add(new IdValueVO(-1, "全部"));

        LoanUserCreditStatusEnum[] values = LoanUserCreditStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (LoanUserCreditStatusEnum status : values)
            {
                LOAN_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }


    private static final List<IdValueVO> SORT_BY_LIST = new ArrayList<IdValueVO>();

    static
    {
        SORT_BY_LIST.add(new IdValueVO(0, "授信时间从晚到早"));
        SORT_BY_LIST.add(new IdValueVO(1, "授信时间从早到晚"));
    }

    @Autowired
    private UserCreditListService userCreditListService;


    @UriMapping("/list")
    public String userLoanInfoList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = 10;
        String keyword = super.getStringParameter("keyword", "");
//        int type = super.getIntParameter("type", -1);//多余的字段
//        int status = super.getIntParameter("status", -1);
        int sortby = super.getIntParameter("sortby", 0);

        try
        {
            List<Integer> statusList = new ArrayList<Integer>();
             //查询范围
            statusList.add(LoanUserCreditStatusEnum.PASS.getStatus());//已授信
            statusList.add(LoanUserCreditStatusEnum.UNAUTHORIZED.getStatus());//已授信(未激活)
            statusList.add(LoanUserCreditStatusEnum.TO_CONFIRM.getStatus());//已激活(待确认)

            UserCreditInfoQueryRequest queryRequest = new UserCreditInfoQueryRequest();
            queryRequest.setKeyword(keyword);

            queryRequest.setStatusList(statusList);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<UserCreditModel> userCreditModelList = userCreditListService.loadUserCreditList(queryRequest, pageNo, pageSize);
            CreditCollectModel creditCollectModel = userCreditListService.loadLoanCollectModel(statusList);
            Page<UserCreditModel> pageDto = new Page<UserCreditModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(userCreditModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
//            request.setAttribute("status", status);
            request.setAttribute("sortby", sortby);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("user_credit_list", userCreditModelList);
            request.setAttribute("credit_collect_list", creditCollectModel);
            request.setAttribute("loan_status_list", LOAN_STATUS_LIST);
            request.setAttribute("sort_by_list", SORT_BY_LIST);
            request.setAttribute("user_credit_list_url", UrlUtils.getUserCreditInfoListUrl(super.getDomain()));
            request.setAttribute("user_credit_detail_info_url", UrlUtils.getUserCreditDetailInfoUrl(super.getDomain()));
            request.setAttribute("contract_SmsSend_Ajax_url", UrlUtils.getContractCaptchaSendUrl(super.getDomain()));
            request.setAttribute("contract_SignBorrow_Ajax_url", UrlUtils.getContractSignBorrowUrl(super.getDomain()));

            return "/user/credit/user_credit_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping("/list/apply")
    public String userLoanApplyList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = 10;
        String keyword = super.getStringParameter("keyword", "");
//        int type = super.getIntParameter("type", -1);
        int status = super.getIntParameter("status", -1);
        int sortby = super.getIntParameter("sortby", 0);

        try
        {
            LoanUserCreditStatusEnum statusEnum = LoanUserCreditStatusEnum.get(status);
            List<Integer>  statusList = new ArrayList<Integer>();
            if(statusEnum == null){
                //查询全部
                for(IdValueVO vo:LOAN_STATUS_LIST){
                    if(LoanUserCreditStatusEnum.DELETED.getStatus() != vo.getId()
                            && -1 != vo.getId() ){
                        //既不是全部，也不是删除状态
                        statusList.add(vo.getId());
                    }
                }

            }else{
                statusList.add(status);
            }

            UserCreditInfoQueryRequest queryRequest = new UserCreditInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setStatusList(statusList);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<UserCreditModel> userCreditModelList = userCreditListService.loadUserCreditList(queryRequest, pageNo, pageSize);
            Page<UserCreditModel> pageDto = new Page<UserCreditModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(userCreditModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            request.setAttribute("sortby", sortby);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("user_loan_list", userCreditModelList);
            request.setAttribute("loan_status_list", LOAN_STATUS_LIST);
            request.setAttribute("sort_by_list", SORT_BY_LIST);
            request.setAttribute("user_loan_list_url", UrlUtils.getUserLoanInfoListUrl(super.getDomain()));
            request.setAttribute("user_loan_info_detail_url", UrlUtils.getUserLoanInfoDetailUrl(super.getDomain()));

            return "/user/loan/user_loan_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }


}
