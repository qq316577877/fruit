/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action.admin;

import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.utils.BizUtils;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.biz.dto.AdminRoleDTO;
import com.fruit.sys.biz.service.AdminUserService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *   系统管理员处理
 */
@Component
@UriMapping("/admin/user")
public class AdminAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAction.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminUserService adminUserService;

    @UriMapping("/list")
    public String showList()
    {
        List<UserInfo> userInfos = this.adminService.loadAll();
        List<AdminRoleDTO> roleDTOList = this.adminService.loadRoleList();
        WebContext.getRequest().setAttribute("records", userInfos);
        WebContext.getRequest().setAttribute("role_list", roleDTOList);
        WebContext.getRequest().setAttribute("admin_user_detail", UrlUtils.getAdminUserDetailUrl(super.getDomain()));
        return "/admin/user/list";
    }

    @UriMapping("/changepwd")
    public String showChangePwd()
    {
        return "/admin/user/password";
    }

    @UriMapping(value = "/changepwd_ajax", interceptors = "logInterceptor")
    public JsonResult reject()
    {
        JsonResult<Boolean> jsonResult = new JsonResult<Boolean>();
        try
        {
            String password = super.getStringParameter("password");
            String newPassword = super.getStringParameter("newPassword");
            String confirmPassword = super.getStringParameter("confirmPassword");

            Validate.notEmpty(password, "请填写原始密码");
            Validate.notEmpty(newPassword, "请填写新密码");
            Validate.notEmpty(confirmPassword, "请确认新密码");
            Validate.isTrue(confirmPassword.equals(newPassword), "两次输入不一致");
            Validate.isTrue(!confirmPassword.equals(password), "新密码不能与旧密码相同");

            UserInfo userInfo = super.loadUserInfo();
            int sysId = userInfo.getSysId();
            this.adminService.changePassword(sysId, password, newPassword);

            jsonResult.setResult(true);
            jsonResult.setMsg("");
        }
        catch (Exception e)
        {
            LOGGER.error("save error", e);
            jsonResult.setResult(false);
            jsonResult.setMsg("失败:" + e.getMessage());
        }
        return jsonResult;
    }

    @UriMapping(value = "/detail")
    public String detail()
    {
        int id = this.getIntParameter("id");
        UserInfo userInfo = null;
        if (id > 0)
        {
            userInfo = adminService.loadById(id);
        }
      //  List<AdminRoleDTO> roleDTOList = this.adminService.loadRoleList();  取消加载所有角色
        //
        //查询子角色 20170811
        List<AdminRoleDTO> roleDTOList = this.adminService.loadByParentId();

        this.setAttribute("data", userInfo);
        this.setAttribute("role_list", roleDTOList);
        this.setAttribute("save_admin_user_ajax", UrlUtils.getSaveAdminUserAjaxUrl(super.getDomain()));
        return "/admin/user/user_detail";
    }

    @UriMapping(value = "/save_ajax", interceptors = "validationInterceptor")
    public JsonResult save()
    {
        try
        {
            UserInfo loginUserInfo = super.getLoginUserInfo();
            Validate.isTrue(null != loginUserInfo, "请先登录!");
            Map<String, Object> validationResults = super.getParamsValidationResults();
            int id = (Integer) validationResults.get("id");
            String userName = (String) validationResults.get("userName");
            int type = (Integer) validationResults.get("type");
            String cnName = (String) validationResults.get("cnName");
            String enName = (String) validationResults.get("enName");
            String mail = (String) validationResults.get("mail");
            String mobile = (String) validationResults.get("mobile");
            String depart = (String) validationResults.get("depart");
            String info = (String) validationResults.get("info");
            Validate.isTrue(BizUtils.emailValidate(mail), "邮箱格式错误!");
            UserInfo userInfo = adminService.save(id, userName, type, cnName, enName, mail, mobile, depart, info, loginUserInfo.getUserName());
            return new JsonResult(userInfo.getSysId());
        }
        catch (Exception e)
        {
            LOGGER.error("新增/修改管理员信息失败", e);
            return new JsonResult(e.getMessage());
        }
    }

    @ResponseBody
    @UriMapping(value = "/delete")
    public JsonResult delete()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            int id = Integer.parseInt(getRequiredStringParamter("id"));

            int count = adminUserService.delete(id);
            dto.setT(count);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("delete error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }
}
