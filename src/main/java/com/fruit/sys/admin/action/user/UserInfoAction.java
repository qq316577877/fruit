/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.user;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserProductLoanInfoDTO;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.*;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.service.user.*;
import com.fruit.sys.admin.service.user.product.UserProductLoanResultService;
import com.fruit.sys.admin.utils.*;
import com.fruit.sys.biz.common.Page;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserInfoAction.java
 */
@Component
@UriMapping("/user")
public class UserInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoAction.class);

    private static final List<IdValueVO> USER_TYPE_LIST = new ArrayList<IdValueVO>();

    static
    {
        USER_TYPE_LIST.add(new IdValueVO(-1, "全部"));
        UserTypeEnum[] values = UserTypeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (UserTypeEnum type : values)
            {
                USER_TYPE_LIST.add(new IdValueVO(type.getType(), type.getMessage()));

            }
        }
    }

    private static final List<IdValueVO> USER_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        USER_STATUS_LIST.add(new IdValueVO(-1, "全部"));
        UserStatusEnum[] values = UserStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (UserStatusEnum status : values)
            {
                USER_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
            }
        }
    }


    private static final List<IdValueVO> USER_ENTERPRISE_STATUS_LIST = new ArrayList<IdValueVO>();

    static
    {
        USER_ENTERPRISE_STATUS_LIST.add(new IdValueVO(-1, "全部"));
        UserEnterpriseStatusEnum[] values = UserEnterpriseStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (UserEnterpriseStatusEnum status : values)
            {
                if(status.getStatus() == UserEnterpriseStatusEnum.DELETED.getStatus()){
                    continue;
                }else{
                    USER_ENTERPRISE_STATUS_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
                }

            }
        }
    }

    private static final List<IdValueVO> SORT_BY_LIST = new ArrayList<IdValueVO>();

    static
    {
        SORT_BY_LIST.add(new IdValueVO(0, "注册时间从晚到早"));
        SORT_BY_LIST.add(new IdValueVO(1, "注册时间从早到晚"));
    }



    //去掉已认证的
    private static final List<IdValueVO> USER_ENTER_STATUS_OUT_VERIFIED_LIST = new ArrayList<IdValueVO>();

    static
    {
        USER_ENTER_STATUS_OUT_VERIFIED_LIST.add(new IdValueVO(-1, "全部"));
        UserEnterpriseStatusEnum[] values = UserEnterpriseStatusEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (UserEnterpriseStatusEnum status : values)
            {
                if((status.getStatus() == UserEnterpriseStatusEnum.DELETED.getStatus())
                        || (status.getStatus() == UserEnterpriseStatusEnum.VERIFIED.getStatus()
                        ||status.getStatus() == UserEnterpriseStatusEnum.NOT_YET.getStatus()
                )
                        ){
                    continue;
                }else{
                    USER_ENTER_STATUS_OUT_VERIFIED_LIST.add(new IdValueVO(status.getStatus(), status.getMessage()));
                }

            }
        }
    }


    @Autowired
    private MemberService memberService;

    @Autowired
    private UserListService userListService;

    @Autowired
    private UserProductLoanResultService userProductLoanResultService;

    private static File uploadItemsFolder;

    @PostConstruct
    private void initUploadDir()
    {
        if (null == uploadItemsFolder)
        {
            String tempDir = "/data/appdatas/update_items/";
            uploadItemsFolder = new File(tempDir);
            uploadItemsFolder.mkdirs();
            if (!uploadItemsFolder.exists())
            {
                throw new RuntimeException("Export temp dir init failed.");
            }
        }
    }

    @UriMapping("/list")
    public String userInfoList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = BizConstants.DEFAULT_PAGE_SIZE;
        String keyword = super.getStringParameter("keyword", "");
        int type = super.getIntParameter("type", -1);
        int status = super.getIntParameter("status", 1);

        int enterpriseStatus = super.getIntParameter("enterpriseStatus", -1);

        int sortby = super.getIntParameter("sortby", 0);

        try
        {
            UserTypeEnum typeEnum = -1 == type ? null : UserTypeEnum.get(type);
            UserStatusEnum statusEnum = UserStatusEnum.get(status);
            UserEnterpriseStatusEnum enterpriseStatusEnum = -1 == enterpriseStatus ? null : UserEnterpriseStatusEnum.get(enterpriseStatus);

            UserInfoQueryRequest queryRequest = new UserInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setType(typeEnum);
            queryRequest.setStatus(statusEnum);
            queryRequest.setEnterpriseStatusEnum(enterpriseStatusEnum);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<UserModel> userModelList = userListService.loadUserList(queryRequest, pageNo, BizConstants.DEFAULT_PAGE_SIZE);

            Page<UserModel> pageDto = new Page<UserModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(userModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
            request.setAttribute("type", type);
            request.setAttribute("enterpriseStatus", enterpriseStatus);
            request.setAttribute("status", status);
            request.setAttribute("sortby", sortby);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("user_list", userModelList);
            request.setAttribute("user_type_list", USER_TYPE_LIST);
            request.setAttribute("user_status_list", USER_STATUS_LIST);
            request.setAttribute("user_enterprise_status_list", USER_ENTERPRISE_STATUS_LIST);
            request.setAttribute("sort_by_list", SORT_BY_LIST);
            request.setAttribute("user_list_url", UrlUtils.getUserInfoListUrl(super.getDomain()));
            request.setAttribute("user_info_detail_url", UrlUtils.getUserInfoDetailUrl(super.getDomain()));
            setUserCounts(request,queryRequest);
            return "/user/user_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    private void setUserCounts(HttpServletRequest request,UserInfoQueryRequest queryRequest)
    {
        queryRequest.setSortKey(null);//统计数量不需要排序
        queryRequest.setSource(null);//不需要区分注册渠道
        queryRequest.setType(null);
        queryRequest.setStatus(null);
        queryRequest.setEnterpriseStatusEnum(null);

        int total = memberService.userCount(queryRequest);

        Map<String, Integer> typCountMap = new HashMap<String, Integer>();
        typCountMap.put("全部", total);
        queryRequest.setType(UserTypeEnum.NON);
        typCountMap.put(UserTypeEnum.NON.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setType(UserTypeEnum.PERSONAL);
        typCountMap.put(UserTypeEnum.PERSONAL.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setType(UserTypeEnum.ENTERPRISE);
        typCountMap.put(UserTypeEnum.ENTERPRISE.getMessage(), memberService.userCount(queryRequest));
        request.setAttribute("type_count_map", typCountMap);

        Map<String, Integer> statusCountMap = new HashMap<String, Integer>();
        statusCountMap.put("全部", total);
        queryRequest.setType(null);
        queryRequest.setStatus(UserStatusEnum.DELETED);
        statusCountMap.put(UserStatusEnum.DELETED.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setStatus(UserStatusEnum.NORMAL);
        statusCountMap.put(UserStatusEnum.NORMAL.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setStatus(UserStatusEnum.FROZEN);
        statusCountMap.put(UserStatusEnum.FROZEN.getMessage(), memberService.userCount(queryRequest));
//        statusCountMap.put(UserStatusEnum.UNCLIAMED.getMessage(), memberService.userCount(UserStatusEnum.UNCLIAMED, null));
//        statusCountMap.put(UserStatusEnum.NOT_ACTIVATED.getMessage(), memberService.userCount(UserStatusEnum.NOT_ACTIVATED, null));
        request.setAttribute("status_count_map", statusCountMap);

        Map<String, Integer> enterpriseStatusCountMap = new HashMap<String, Integer>();
        enterpriseStatusCountMap.put("全部", total);
//        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.DELETED.getMessage(), memberService.userCount(null,null,UserEnterpriseStatusEnum.DELETED));
        queryRequest.setStatus(null);
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.VERIFIED);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.VERIFIED.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.NOT_YET);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.NOT_YET.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.VERIFYING);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.VERIFYING.getMessage(), memberService.userCount(queryRequest));
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.REJECTED);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.REJECTED.getMessage(), memberService.userCount(queryRequest));
        request.setAttribute("enterprise_status_count_map", enterpriseStatusCountMap);
    }

    @UriMapping(value = "/login_as_user")
    public String loginAsUser()
    {
        try
        {
            int userId = super.getIntParameter("id");
            Validate.isTrue(userId > 0, "参数错误!");
            UserModel userModel = userListService.loadUserModel(userId, false);
            Validate.isTrue(null != userModel, "系统错误：用户账号异常!");
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "系统错误：管理员账号异常!");
            String sign = userInfo.getUserName() + "|" + userId + "|" + System.currentTimeMillis();
            sign = AESHelper.encryptHexString(sign);
            return "redirect:" + UrlUtils.getPortalDashboardUrl(super.getPortalDomain(), sign);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/login_as_user].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_message", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/freeze_user_ajax")
    public JsonResultDTO freezeUserAccount()
    {
        try
        {
            UserInfo userInfo = super.getUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");
            memberService.updateUserStatus(userId, UserStatusEnum.FROZEN, userInfo.getUserName(), super.getUserIp());
            return new JsonResultDTO();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/freeze_user_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }

    @UriMapping(value = "/unfreeze_user_ajax")
    public JsonResultDTO unFreezeUserAccount()
    {
        try
        {
            UserInfo userInfo = super.getUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            int userId = super.getIntParameter("id", 0);
            Validate.isTrue(userId > 0, "参数错误!");
            memberService.updateUserStatus(userId, UserStatusEnum.NORMAL, userInfo.getUserName(), super.getUserIp());
            return new JsonResultDTO();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/unfreeze_user_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    @UriMapping(value = "/save_base_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveBaseInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        String mobile = (String) validationResults.get("mobile");
        String mail = (String) validationResults.get("mail");
        String QQ = (String) validationResults.get("QQ");
        try
        {
            if(StringUtils.isEmpty(mail)){
                mail = "";
            }
            if(StringUtils.isEmpty(QQ)){
                QQ = "";
            }

            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            memberService.update(userId, mobile, mail, QQ, userInfo.getUserName(), getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, false);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/save_base_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    @UriMapping(value = "/update_user_password_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO updateUserPassword()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("id");
        String newPassword = (String) validationResults.get("newPassword");
        String confirmPassword = (String) validationResults.get("confirmPassword");

        try
        {
            newPassword = newPassword.trim();
            confirmPassword = confirmPassword.trim();

            // check 两次 pwd
            if (!StringUtils.equals(newPassword,confirmPassword))
            {
                return new JsonResultDTO( "两次密码不一样!");
            }


            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            memberService.updatePassword(userId,newPassword, userInfo.getUserName(), getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, false);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/update_user_password_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }

    @UriMapping(value = "/product/save_product_loan_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveProductLoan()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("userId");
        int productId = (Integer) validationResults.get("productId");
        BigDecimal productLoan = new BigDecimal((String) validationResults.get("productLoan"));

        UserProductLoanInfoDTO userProductLoanInfoDTO = new UserProductLoanInfoDTO();
        userProductLoanInfoDTO.setUserId(userId);
        userProductLoanInfoDTO.setProductId(productId);
        userProductLoanInfoDTO.setProductLoan(productLoan);
        try
        {
            userProductLoanResultService.addProductLoanInfo(userProductLoanInfoDTO);

            return new JsonResultDTO();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/product/save_product_loan_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }

    @UriMapping(value = "/product/update_product_loan_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO updateProductLoan()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int productId = (Integer) validationResults.get("productId");
        BigDecimal productLoan = new BigDecimal((String) validationResults.get("productLoan"));

        int userId = 1;

        try
        {
            userProductLoanResultService.updateProductLoanInfo(productId, userId, productLoan, getUserIp());

            return new JsonResultDTO();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/product/update_product_loan_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }



    @UriMapping(value = "/save_enterprise_info_ajax", interceptors = "validationInterceptor")
    public JsonResultDTO saveEnterpirseInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int userId = (Integer) validationResults.get("userId");
        String name = (String) validationResults.get("name");
        int provinceId = (Integer) validationResults.get("provinceId");
        int cityId = (Integer) validationResults.get("cityId");
        String address = (String) validationResults.get("address");
        String phoneNum = (String) validationResults.get("phone");
        String licence = (String) validationResults.get("licensePath");
        String sealPic = (String) validationResults.get("sealPath");
        String logoPic = (String) validationResults.get("logoPath");
        String contact = (String) validationResults.get("contact");

        try
        {
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.isTrue(null != userInfo, "请先登录!");
            EnterpriseVO enterpriseVO = new EnterpriseVO();
            enterpriseVO.setUserId(userId);
            enterpriseVO.setName(name);
            enterpriseVO.setProvinceId(provinceId);
            enterpriseVO.setCityId(cityId);
            enterpriseVO.setAddress(address);
            enterpriseVO.setPhoneNum(phoneNum);
            enterpriseVO.setLicence(licence);
            enterpriseVO.setLastEditor(userInfo.getUserName());
            memberService.updateEnterprise(enterpriseVO,super.getUserIp());
            UserModel userModel = userListService.loadUserModel(userId, true);
            Validate.isTrue(null != userModel, "用户账号异常!");
            return new JsonResultDTO(userModel);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/save_enterprise_info_ajax].Exception:{}",e);
            return new JsonResultDTO(e.getMessage());
        }
    }


    private void doLoginAsUser(UserModel userModel)
    {
        HttpServletResponse response = WebContext.getResponse();
        // 种用户登录详细信息cookie，保存用户的常用信息(加密)
        Cookie user = new Cookie(BizConstants.COOKIE_USER_PORTAL, CookieUtil.generatePassportByUserModel(userModel));
        user.setDomain(super.getPortalDomain());
        user.setPath("0ku-alpha.com");
        user.setMaxAge(-1); // 关闭浏览器退出
        response.addCookie(user);
        this.clearCookie(BizConstants.COOKIE_GUEST_PORTAL);
    }

    @UriMapping("/update_user_type")
    public String updateUserType()
    {
        try{
            int userId = super.getIntParameter("userId",0);
            int userType  = super.getIntParameter("userType");
            HttpServletRequest request  = WebContext.getRequest();
            request.setAttribute("userTypes", USER_TYPE_LIST);
            request.setAttribute("userId", userId);
            request.setAttribute("userType",userType);
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/update_user_type].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
        }

        return "/user/user_update_type";
    }

    @UriMapping("/save_update_usertype")
    public JsonResultDTO saveUpdateUserYpe()
    {
        JsonResultDTO jsonResult = new JsonResultDTO();
        try
        {
            UserInfo admin = super.getLoginUserInfo();
            Validate.isTrue(null != admin, "请先登录!");
            int userId = super.getIntParameter("userId");
            int userType = super.getIntParameter("type");
            Validate.isTrue(userId>0,"参数错误！");
            String decription = super.getStringParameter("description");
            this.memberService.updateUserDecription(admin,userId,super.getUserIp(),decription,userType);
            jsonResult.setResult(true);
        }
        catch (IllegalArgumentException e)
        {   LOGGER.error("[system][/user/save_update_usertype].Exception:{}",e);
            jsonResult.setResult(false);
            jsonResult.setMsg("失败:" + e.getMessage());
        }
        return jsonResult;
    }



    /**
     * 去掉 已认证状态的 所有会员列表
     * @return
     */
    @UriMapping("/authlist")
    public String userInfoAuthList()
    {
        int pageNo = super.getIntParameter("pageNo", 1);
        int pageSize = BizConstants.DEFAULT_PAGE_SIZE;
        String keyword = super.getStringParameter("keyword", "");
        int type = super.getIntParameter("type", -1);
        int status = super.getIntParameter("status", 1);

        int enterpriseStatus = super.getIntParameter("enterpriseStatus", -1);

        int sortby = super.getIntParameter("sortby", 0);

        try
        {
            UserTypeEnum typeEnum = -1 == type ? null : UserTypeEnum.get(type);
            UserStatusEnum statusEnum = UserStatusEnum.get(status);
            UserEnterpriseStatusEnum enterpriseStatusEnum = -1 == enterpriseStatus ? null : UserEnterpriseStatusEnum.get(enterpriseStatus);

            UserInfoQueryRequest queryRequest = new UserInfoQueryRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setType(typeEnum);
            queryRequest.setStatus(statusEnum);
            queryRequest.setEnterpriseStatusEnum(enterpriseStatusEnum);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(sortby == 0);

            List<UserModel> userModelList = userListService.loadUserAuthList(queryRequest, pageNo, BizConstants.DEFAULT_PAGE_SIZE);

            Page<UserModel> pageDto = new Page<UserModel>();//用于公共分页
            pageDto.setCurrentPageNo(pageNo);
            pageDto.setCurrentPageSize(pageSize);
            pageDto.setResult(userModelList);

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("keyword", keyword);
            request.setAttribute("type", type);
            request.setAttribute("enterpriseStatus", enterpriseStatus);
            request.setAttribute("status", status);
            request.setAttribute("sortby", sortby);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("pageDto", pageDto);//用公共分页必须用到此属性
            request.setAttribute("user_list", userModelList);
            request.setAttribute("user_type_list", USER_TYPE_LIST);
            request.setAttribute("user_status_list", USER_STATUS_LIST);
            request.setAttribute("user_enterprise_status_list", USER_ENTER_STATUS_OUT_VERIFIED_LIST);
            request.setAttribute("sort_by_list", SORT_BY_LIST);
            request.setAttribute("user_auth_list_url", UrlUtils.getUserAuthInfoListUrl(super.getDomain()));
            request.setAttribute("user_info_auth_detail_url", UrlUtils.getUserInfoAuthDetailUrl(super.getDomain()));
            request.setAttribute("user_info_auth_verify_url", UrlUtils.getUserInfoAuthVerifyUrl(super.getDomain()));
            setUserAuthCounts(request,queryRequest);
            return "/user/user_auth_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/user/authlist].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }


    private void setUserAuthCounts(HttpServletRequest request,UserInfoQueryRequest queryRequest)
    {
        queryRequest.setSortKey(null);//统计数量不需要排序
        queryRequest.setSource(null);//不需要区分注册渠道
        queryRequest.setType(null);
        queryRequest.setStatus(null);
        queryRequest.setEnterpriseStatusEnum(null);

        int total = memberService.userAuthCount(queryRequest);

        Map<String, Integer> typCountMap = new HashMap<String, Integer>();
        typCountMap.put("全部", total);

        queryRequest.setType(UserTypeEnum.PERSONAL);
        typCountMap.put(UserTypeEnum.PERSONAL.getMessage(), memberService.userAuthCount(queryRequest));

        queryRequest.setType(UserTypeEnum.ENTERPRISE);
        typCountMap.put(UserTypeEnum.ENTERPRISE.getMessage(), memberService.userAuthCount(queryRequest));
        request.setAttribute("type_count_map", typCountMap);

        Map<String, Integer> statusCountMap = new HashMap<String, Integer>();
        statusCountMap.put("全部", total);
        queryRequest.setType(null);
        queryRequest.setStatus(UserStatusEnum.DELETED);
        statusCountMap.put(UserStatusEnum.DELETED.getMessage(), memberService.userAuthCount(queryRequest));
        queryRequest.setStatus(UserStatusEnum.NORMAL);
        statusCountMap.put(UserStatusEnum.NORMAL.getMessage(), memberService.userAuthCount(queryRequest));
        queryRequest.setStatus(UserStatusEnum.FROZEN);
        statusCountMap.put(UserStatusEnum.FROZEN.getMessage(), memberService.userAuthCount(queryRequest));
        request.setAttribute("status_count_map", statusCountMap);

        Map<String, Integer> enterpriseStatusCountMap = new HashMap<String, Integer>();
        enterpriseStatusCountMap.put("全部", total);
        queryRequest.setStatus(null);
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.VERIFYING);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.VERIFYING.getMessage(), memberService.userAuthCount(queryRequest));
        queryRequest.setEnterpriseStatusEnum(UserEnterpriseStatusEnum.REJECTED);
        enterpriseStatusCountMap.put(UserEnterpriseStatusEnum.REJECTED.getMessage(), memberService.userAuthCount(queryRequest));
        request.setAttribute("enterprise_status_count_map", enterpriseStatusCountMap);
    }

}
