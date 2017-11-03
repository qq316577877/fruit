/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action.drivers;

import com.fruit.base.biz.common.SmsTypeEnum;
import com.fruit.drivers.biz.dto.DriversAccountDTO;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.drivers.DriversInfoVO;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.drivers.DriversMemberService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Component
@UriMapping("/drivers")
public class DriversLoginAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DriversLoginAction.class);

    private static final String DRIVER_ENCODE = "utf-8";

    @Autowired
    private DriversMemberService driversMemberService;

    @Autowired
    private MessageService messageService;

    @UriMapping(value = "/register_and_login_ajax" , interceptors = "validationInterceptor")
    public AjaxResult loginAjax()
    {
        String mobile = StringUtils.trimToEmpty(super.getStringParameter("mobile"));
        String mobileCaptcha = StringUtils.trimToEmpty(super.getStringParameter("mobileCaptcha"));
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(mobile), "手机号不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(mobileCaptcha), "短信验证码不能为空!");
            //1.先验证短信验证码
            messageService.expendCaptcha(mobile, mobileCaptcha, SmsTypeEnum.LSG_USER_REGISTER.getValue());

            DriversAccountDTO driversAccountDTO = new DriversAccountDTO();
            driversAccountDTO.setMobile(mobile);
            DriversAccountDTO loginDriversAccountDTO = driversMemberService.registerAndLogin(driversAccountDTO);
            Validate.isTrue(loginDriversAccountDTO!=null,"注册登录异常！");

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(mobile+"登录失败:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }



    @UriMapping(value = "/captcha/send_sms_drivers_ajax" , interceptors = "validationInterceptor")
    public AjaxResult sendSmsDirect()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String mobile = StringUtils.trimToEmpty((String) validationResults.get("mobile"));
        int type = (Integer) validationResults.get("type");

        try
        {
            messageService.sendSmsCaptcha(0, mobile, type);
            return new AjaxResult();
        }
        catch (Exception e)
        {
            LOGGER.error("[/drivers/captcha/send_sms_drivers_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(), "老司机验证码发送失败!");
        }
    }



    @UriMapping(value = "/get_driver_info_by_mobile_ajax" , interceptors = "validationInterceptor")
    public AjaxResult getDriverInfoAjax()
    {
        String mobile = StringUtils.trimToEmpty(super.getStringParameter("mobile"));
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(mobile), "手机号不能为空!");
            DriversInfoVO driversInfoVO = driversMemberService.getDriverInfoByMobile(mobile);
            Validate.isTrue(driversInfoVO!=null,"此手机号未注册！");

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(driversInfoVO);
            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(mobile+"查询司机信息失败:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }



    @UriMapping(value = "/update_driver_info_ajax" , interceptors = "validationInterceptor")
    public AjaxResult updateDriverInfoAjax()
    {
        String mobile = StringUtils.trimToEmpty(super.getStringParameter("mobile"));
        String name = StringUtils.trimToEmpty(super.getStringParameter("name"));
        String licensePlate = StringUtils.trimToEmpty(super.getStringParameter("licensePlate"));
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(mobile), "手机号不能为空!");
            String encodeName = URLDecoder.decode(name,DRIVER_ENCODE);
            String encodeLicensePlate = URLDecoder.decode(licensePlate,DRIVER_ENCODE);
            DriversAccountDTO driversAccountDTO = new DriversAccountDTO();
            driversAccountDTO.setMobile(mobile);
            driversAccountDTO.setName(encodeName);
            driversAccountDTO.setLicensePlate(encodeLicensePlate);
            driversMemberService.updateDriverInfo(driversAccountDTO);

            return new AjaxResult();
        }
        catch (UnsupportedEncodingException uee)
        {
            LOGGER.error(mobile+"修改司机信息转码失败:" + uee.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),uee.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(mobile+"修改司机信息失败:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }



    @UriMapping(value = "/update_register_mobile_ajax" , interceptors = "validationInterceptor")
    public AjaxResult updateRegisterMobileAjax()
    {
        String oldMobile = StringUtils.trimToEmpty(super.getStringParameter("oldMobile"));
        String newMobile = StringUtils.trimToEmpty(super.getStringParameter("newMobile"));
        String mobileCaptcha = StringUtils.trimToEmpty(super.getStringParameter("mobileCaptcha"));
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(oldMobile), "原手机号不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(newMobile), "新手机号不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(mobileCaptcha), "短信验证码不能为空!");
            //1.先验证短信验证码
            messageService.expendCaptcha(newMobile, mobileCaptcha, SmsTypeEnum.LSG_USER_REGISTER.getValue());

            driversMemberService.updateMobile(oldMobile,newMobile);

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(oldMobile+"修改手机号异常:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }

}


