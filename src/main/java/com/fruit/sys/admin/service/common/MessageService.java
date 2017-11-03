/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.common;

import com.dianping.cat.Cat;
import com.fruit.base.biz.common.SmsStatusEnum;
import com.fruit.base.biz.dto.SmsCaptchaDTO;
import com.fruit.base.biz.dto.SmsLogDTO;
import com.fruit.base.biz.service.SmsCaptchaService;
import com.fruit.base.biz.service.SmsLogService;
import com.fruit.sys.admin.model.ContextObject;
import com.fruit.sys.admin.service.ContextManger;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.BizUtils;
import com.fruit.sys.admin.utils.TaskProcessUtil;
import com.ovfintech.arch.message.common.mail.MailMessageClient;
import com.ovfintech.arch.message.common.mail.MailServerInfo;
import com.ovfintech.arch.message.common.sms.MobileMessageClient;

import com.ovfintech.concurrent.TaskAction;
import com.ovft.notice.api.bean.MessageNoticeBean;
import com.ovft.notice.api.service.MessageNoticeService;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Description:   消息服务
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-1511-16
 * Project        : fruit
 * File Name      : PortalMessageService.java
 */
@Service
public class MessageService implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private static final String SMS_CONTENT = "【九创金服】您的手机验证码为{0}，请于30分钟内输入，超时请重新获取。";

    @Autowired
    private EnvService envService;

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private MessageNoticeService messageNoticeService;

    private MailMessageClient mailMessageClient;

    private boolean isProduct()
    {
        return EnvService.getEnv().equals("product");
    }

    public boolean sendMobileMessage(int userId, String mobile, String content)
    {
        int result = 1;
        if(!this.isProduct())
        {
            LOGGER.warn("[message service] disable sms && mail under non-product. user: {}, to: {}, content: {}", new Object[]{userId, mobile, content});
        }
        else
        {
            result = MobileMessageClient.sendMobileMessage(mobile, content);
        }
        addSmsLog(userId, mobile, content, result);
        Cat.logEvent("SMS.SendMobileMessage", String.valueOf(result));
        return result == 0;
    }

    /**
     * 向指定的用户、手机发送短信验证码
     *
     * @param userId
     * @param mobile
     * @param type
     * @return
     */
    public boolean sendSmsCaptcha(int userId, String mobile, int type)
    {
        // 查询是否已经发过短信
        SmsCaptchaDTO sms = smsCaptchaService.loadLastest(mobile, type);
        if (null != sms && sms.getStatus() == SmsStatusEnum.NOT_VERIFY.getValue()
                && !isCaptchaExpired(sms.getAddTime()))  // 已发短信为验证、未过期，则重新更新过期时间，重复发送
        {
            smsCaptchaService.updateAddTime(sms.getId(), new Date());
        }
        else
        {
            // 创建数据库记录
            String captcha = String.valueOf(BizUtils.random6Int());
            sms = new SmsCaptchaDTO();
            sms.setUserId(userId);
            sms.setMobile(mobile);
            sms.setCaptcha(captcha);
            sms.setAddTime(new Date());
            sms.setType(type);
            ContextObject context = ContextManger.getContext();
            if (null != context)
            {
                sms.setUserIp(context.getUserIp());
            }
            sms.setStatus(SmsStatusEnum.NOT_VERIFY.getValue());
            smsCaptchaService.create(sms);
        }
        return sendSmsCaptcha(userId, mobile, sms.getCaptcha());
    }

    /**
     * 检测指定的验证码是否可用
     *
     * @param mobile
     * @param captcha
     * @param type
     * @param updateTime 是否更新UpdateTime字段
     */
    public void checkCaptchaAlive(String mobile, String captcha, int type, boolean updateTime)
    {
        SmsCaptchaDTO smsCaptchaDTO = smsCaptchaService.loadByCaptcha(mobile, captcha);
        Validate.isTrue(null != smsCaptchaDTO, "验证码错误!");
        Validate.isTrue(smsCaptchaDTO.getType() == type, "验证码类型错误!");
        Validate.isTrue(smsCaptchaDTO.getStatus() == SmsStatusEnum.NOT_VERIFY.getValue(), "验证码错误!");
        boolean expired = MessageService.isCaptchaExpired(smsCaptchaDTO.getAddTime());
        if (expired) // 如果加载到过期验证码，则更新为已过期
        {
            smsCaptchaService.updateStatus(smsCaptchaDTO.getId(), SmsStatusEnum.EXPIRED.getValue());
            throw new IllegalArgumentException("验证码已过期!");
        }
        if (updateTime)
        {
            smsCaptchaService.updateAddTime(smsCaptchaDTO.getId(), new Date()); // 更新时间
        }
    }

    /**
     * 消费当前处于存活状态的验证码，如果加载到已过期的验证码则更新其状态为已过期
     *
     * @param mobile
     * @param captcha
     * @param type
     */
    public void expendCaptcha(String mobile, String captcha, int type)
    {
        SmsCaptchaDTO smsCaptchaDTO = smsCaptchaService.loadByCaptcha(mobile, captcha);
        Validate.isTrue(null != smsCaptchaDTO, "验证码错误!");
        Validate.isTrue(smsCaptchaDTO.getType() == type, "验证码类型错误!");
        Validate.isTrue(smsCaptchaDTO.getStatus() == SmsStatusEnum.NOT_VERIFY.getValue(), "验证码错误!");
        boolean expired = MessageService.isCaptchaExpired(smsCaptchaDTO.getAddTime());
        if (expired) // 如果加载到过期验证码，则更新为已过期
        {
            smsCaptchaService.updateStatus(smsCaptchaDTO.getId(), SmsStatusEnum.EXPIRED.getValue());
            throw new IllegalArgumentException("验证码已过期!");
        }
        smsCaptchaService.updateStatus(smsCaptchaDTO.getId(), SmsStatusEnum.VERIFIED.getValue()); // 消耗短信验证码
    }

    /**
     * 判断短信验证码是否已过期 (过期时间为<code>BizConstants.SMS_CAPTCHA_EXPIRATION_TIME</code>所定义的时间)
     *
     * @param date
     * @return 验证码已过期则返回true，否则返回false
     */
    public static boolean isCaptchaExpired(Date date)
    {
        if (null != date && (date.getTime() + BizConstants.SMS_CAPTCHA_EXPIRATION_TIME) < System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }

    private boolean sendSmsCaptcha(int userId, String mobile, String captcha)
    {
        int result = 0;
        String content = MessageFormat.format(SMS_CONTENT, captcha);
        if (!this.isProduct())
        {
            LOGGER.warn("[message service] disable sms && mail under non-product. user: {}, to: {}, content: {}", new Object[]{userId, mobile, content});
        }
        else
        {
            result = MobileMessageClient.sendMobileMessage(mobile, content);
        }
        addSmsLog(userId, mobile, content, result);
        Cat.logEvent("SMS.SendCaptchaResult", String.valueOf(result));
        return result == 0;
    }

    public boolean sendSms(int userId, String mobile, String content)
    {
        int result = 0;
        if (!this.isProduct())
        {
            LOGGER.warn("[message service] disable sms && mail under non-product. user: {}, to: {}, content: {}", new Object[]{userId, mobile, content});
        }
        else
        {
            result = MobileMessageClient.sendMobileMessage(mobile, content);
        }
        addSmsLog(userId, mobile, content, result);
        Cat.logEvent("SMS.SendContentResult", String.valueOf(result));
        return result == 0;
    }

    private void addSmsLog(int userId, String mobile, String content, int result)
    {
        try
        {
            SmsLogDTO smsLogDTO = new SmsLogDTO();
            smsLogDTO.setUserId(userId);
            smsLogDTO.setMobile(mobile);
            smsLogDTO.setContent(content);
            smsLogDTO.setResponseCode(result);
            smsLogDTO.setStatus(result == 0 ? 1 : 0);
            smsLogDTO.setAddTime(new Date());
            this.smsLogService.create(smsLogDTO);
        }
        catch (RuntimeException e)
        {
            LOGGER.error("[sendMail] failed to insert sms log: " + mobile + "-" + content, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.mailMessageClient = new MailMessageClient();
        MailServerInfo server = new MailServerInfo();
        String mailSetting = this.envService.getConfig("mail.notification.settings");
        String[] settingArray = mailSetting.split(",");
        server.setHost(settingArray[0]);
        server.setUser(settingArray[1]);
        server.setPassword(settingArray[2]);
        server.setConnectTimeout(1000);
        mailMessageClient.setMailServerInfo(server);
    }

    /**
     * 发送短信，使用短信微服务。
     * 短信微服务中使用了kafka
     * @param userId
     * @param mobile
     * @param content
     * @return
     */
    public boolean sendSmsByKafka(int userId, String mobile, String content)
    {
        int result = 0;
        if (!this.isProduct())
        {
            LOGGER.warn("[message service] disable sms && mail under non-product. user: {}, to: {}, content: {}", new Object[]{userId, mobile, content});
        }
        else
        {
            final MessageNoticeBean message = new MessageNoticeBean();
            message.setMobile(mobile);
            message.setContent(content);
            message.setSource("www.fruit.com");
            TaskProcessUtil.getMtTaskProcess().asyncExecuteTask(new TaskAction<Object>() {
                @Override
                public Object doInAction() throws Exception {
                    messageNoticeService.sendSmsMessage(message);//此dubbo服务中使用了kafka
                    return null;
                };
            });
        }

        addSmsLog(userId, mobile, content, result);
        Cat.logEvent("SMS.SendKafkaContentResult", String.valueOf(result));
        return result == 0;
    }
}
