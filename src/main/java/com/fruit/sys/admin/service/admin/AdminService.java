/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.admin;

import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.sys.admin.model.KeyValueModel;
import com.fruit.sys.admin.model.RoleVO;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.common.RedBankService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.CookieUtil;
import com.fruit.sys.biz.common.AdminType;
import com.fruit.sys.biz.common.AdminUserStatus;
import com.fruit.sys.biz.dto.AdminRoleDTO;
import com.fruit.sys.biz.dto.AdminRoleUserDTO;
import com.fruit.sys.biz.dto.AdminUserDTO;
import com.fruit.sys.biz.service.AdminRoleService;
import com.fruit.sys.biz.service.AdminRoleUserService;
import com.fruit.sys.biz.service.AdminUserService;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : fruit
 * File Name      : AdminService.java
 */
@Service
public class AdminService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    private static final int ADMIN_DEFAUTLE_ROLE_ID = 5;

    private static final int SALE_ROLE_ID = 7;

    private static final String MD5_SALT = "jophOJOHEWIOy47ahKJ~floPORdafTI";

    @Autowired
    private RedBankService redBankService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleUserService adminRoleUserService;

    @Autowired
    private AdminRoleService adminRoleService;

    public UserInfo loadUserInfo(int sysId)
    {
        AdminUserDTO adminUserDTO = this.adminUserService.loadById(sysId);
        UserInfo userInfo = null;
        if (adminUserDTO.getStatus() == AdminUserStatus.ENABLED.getStatus())
        {
            userInfo = this.dto2vo(adminUserDTO);
        }
        return userInfo;
    }

    public List<UserInfo> loadAll()
    {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        List<AdminUserDTO> adminUserDTOs = this.adminUserService.loadAll();
        for(AdminUserDTO adminUserDTO: adminUserDTOs)
        {
            userInfos.add(dto2vo(adminUserDTO));
        }
        return userInfos;
    }

    public UserInfo loadById(int sysId)
    {
        return dto2vo(this.adminUserService.loadById(sysId));
    }

    public UserInfo loadByUserName(String username)
    {
        Validate.notEmpty(username);
        AdminUserDTO adminUserDTO = this.adminUserService.loadByUserName(username);
        return this.dto2vo(adminUserDTO);
    }

    public UserInfo adminUserLogin(String username, String password)
    {
        Validate.notEmpty(username);
        Validate.notEmpty(password);
        AdminUserDTO adminUserDTO = this.adminUserService.loadByUserName(username);
        Validate.isTrue(null != adminUserDTO, "账号密码错误!");
        String passwordMd5 = this.getPasswordString(password);
        Validate.isTrue(StringUtils.equals(adminUserDTO.getPassword(), passwordMd5), "账号密码错误!");
        Validate.isTrue(adminUserDTO.getStatus() == AdminUserStatus.ENABLED.getStatus(), "账号不可用!");
        return this.dto2vo(adminUserDTO);
    }

    public UserInfo save(int id, String userName, int type, String cnNane, String enName, String mail, String mobile, String depart, String info, String operator)
    {
        Validate.isTrue(id < 1 && null == adminUserService.loadByUserName(userName), "该账号已存在!");
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setId(id);
        adminUserDTO.setUserName(userName);
        adminUserDTO.setType(type);
        adminUserDTO.setCnName(cnNane);
        adminUserDTO.setEnName(enName);
        adminUserDTO.setMail(mail);
        adminUserDTO.setMobile(mobile);
        adminUserDTO.setDepart(depart);
        adminUserDTO.setInfo(info);
        if (id > 0)
        {
            adminUserService.update(adminUserDTO);
        }
        else
        {
            String password = "123456";//默认密码123456
//                    BizUtils.getRandomPwd(8);
            String passwordMd5 = getPasswordString(password);
            adminUserDTO.setPassword(passwordMd5);
            adminUserService.create(adminUserDTO);
            specifyDefaultRole(adminUserDTO);
//            sendNewcomeMessage(adminUserDTO, password, operator);//发消息
        }
        return this.dto2vo(adminUserDTO);
    }

    private void specifyDefaultRole(AdminUserDTO adminUserDTO)
    {
        AdminRoleUserDTO adminRoleUserDTO = new AdminRoleUserDTO();
        adminRoleUserDTO.setSysId(adminUserDTO.getId());
        adminRoleUserDTO.setUserName(adminUserDTO.getUserName());
        adminRoleUserDTO.setRoleId(ADMIN_DEFAUTLE_ROLE_ID);
        adminRoleUserService.create(adminRoleUserDTO);
    }

    private void sendNewcomeMessage(AdminUserDTO adminUserDTO, String password, String operator)
    {
        Map<String, String> extraData = new HashMap<String, String>();
        extraData.put("password", password);
        this.redBankService.syncFireEvent(adminUserDTO.getId(), MessageTypeEnum.NEW_ADMIN_USER, extraData, operator);
    }

    protected static String getPasswordString(String password)
    {
        String passwordMd5 = "";
        try
        {
            String source = password + MD5_SALT;
            passwordMd5 = DigestUtils.md5Hex(source.getBytes(BizConstants.UTF_8));
        }
        catch (UnsupportedEncodingException e)
        {
        }
        return passwordMd5;
    }

    public void changePassword(int sysId, String password, String newPassword)
    {
        AdminUserDTO adminUserDTO = this.adminUserService.loadById(sysId);
        Validate.notNull(adminUserDTO, "用户不存在");
        String passwordMd5 = this.getPasswordString(password);
        Validate.isTrue(StringUtils.equals(adminUserDTO.getPassword(), passwordMd5), "账号密码错误!");
        String newMd5 = this.getPasswordString(newPassword);
        this.adminUserService.updatePassword(sysId, newMd5);
    }

    public boolean checkPassword(int sysId, String password)
    {
        AdminUserDTO adminUserDTO = this.adminUserService.loadById(sysId);
        Validate.notNull(adminUserDTO, "用户不存在");
        String passwordMd5 = this.getPasswordString(password);
        return StringUtils.equals(adminUserDTO.getPassword(), passwordMd5);
    }

    public List<KeyValueModel> getAllSales()
    {
        List<KeyValueModel> result = new ArrayList<KeyValueModel>();
        result.add(new KeyValueModel("", "请选择销售", 0)); // 默认增加一个空选项
        List<Integer> saleIds = adminRoleUserService.loadSysIdsByRoleId(SALE_ROLE_ID);
        if (CollectionUtils.isNotEmpty(saleIds))
        {
            List<AdminUserDTO> adminUserDTOs = adminUserService.loadByIds(saleIds);
            if (CollectionUtils.isNotEmpty(adminUserDTOs))
            {
                for (AdminUserDTO adminUserDTO : adminUserDTOs)
                {
                    String userName = adminUserDTO.getUserName();
                    String cnName = StringUtils.isBlank(adminUserDTO.getCnName()) ? userName : adminUserDTO.getCnName();
                    KeyValueModel idValueVO = new KeyValueModel(userName, cnName, 0);
                    result.add(idValueVO);
                }
            }
        }
        return result;
    }

    private UserInfo dto2vo(AdminUserDTO adminUserDTO)
    {
        if (null == adminUserDTO)
        {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(adminUserDTO.getUserName());
        userInfo.setSysId(adminUserDTO.getId());
        userInfo.setDepartName(adminUserDTO.getDepart());
        userInfo.setMobile(adminUserDTO.getMobile());
        userInfo.setAdmin(adminUserDTO.getType() == AdminType.SYS_ADMIN.getType());
        userInfo.setMail(adminUserDTO.getMail());
        userInfo.setStatus(adminUserDTO.getStatus());
        userInfo.setAddTime(adminUserDTO.getAddTime());
        userInfo.setUpdateTime(adminUserDTO.getUpdateTime());
        userInfo.setCnName(adminUserDTO.getCnName());
        userInfo.setEnName(adminUserDTO.getEnName());
        userInfo.setType(adminUserDTO.getType());
        return userInfo;
    }

    public static void main(String[] args)
    {
        System.out.println(getPasswordString("123456"));
    }

    public List<AdminRoleDTO> loadRoleList ()
    {
        List<AdminRoleDTO> roleList = adminRoleService.loadAll();

        return roleList;
    }


    private RoleVO transfer(AdminRoleDTO adminRoleDTO)
    {
        RoleVO result = new RoleVO();

        BeanUtils.copyProperties(adminRoleDTO, result);

        return result;
    }

    /**
     * 根据父角色ID查询role
     *
     * @return
     */
    public List<AdminRoleDTO> loadByParentId()
    {
        List<AdminRoleDTO> roleDTOList = new ArrayList<AdminRoleDTO>();

        int currentUserId =  getUserId( WebContext.getRequest());
        //查询子角色 20170811
        roleDTOList = this.adminRoleService.loadByParentUserId(currentUserId);

        return  roleDTOList;
    }

    private int getUserId(HttpServletRequest httpServletRequest)
    {
        int sysId = 0;
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie userCookie = null; // 正式登陆用户cookie
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (BizConstants.COOKIE_USER.equals(cookie.getName())) //已登录用户
                {
                    userCookie = cookie;
                    break;
                }
            }
        }

        if (userCookie != null)
        {
            String value = userCookie.getValue();
            String idValue = CookieUtil.getIdFromPassport(value);
            httpServletRequest.setAttribute(BizConstants.COOKIE_USER, value);
            sysId = NumberUtils.toInt(idValue);
            httpServletRequest.setAttribute(BizConstants.ATTR_USER, sysId);
        }
        return sysId;
    }
}
