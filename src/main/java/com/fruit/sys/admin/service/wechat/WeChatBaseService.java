package com.fruit.sys.admin.service.wechat;/*
 * Create Author  : ivan
 * Create  Time   :
 * Project        :
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

import com.alibaba.fastjson.JSON;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.EnvService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.utils.WechatConstants;
import com.ovfintech.cache.client.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeChatBaseService implements InitializingBean
{

    private static final Logger logger = LoggerFactory.getLogger(WeChatBaseService.class);

    private static final ObjectMapper mapper = new  ObjectMapper() ;

    @Autowired
    protected EnvService envService;

    @Resource
    private CacheClient cacheClient;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    private String appId;

    private String appSecret;

    /**
     * 获取用户验证CODE
     * @param redictUrl
     * @return
     */
    public String getCodeUrl(String redictUrl){
        return String.format(WechatConstants.get_Code_Url, appId, URLEncoder.encode(redictUrl));

    }

    /**
     *获取用户token
     * @throws Exception
     */
    public AjaxResult getUserTokenInfo(String code){
        String  url = String.format(WechatConstants.get_UserToken_Url,this.appId,this.appSecret,code);

        String openid ;
             try {

            logger.info("request accessToken from url: {}", url);

            Map object = getJsonObjectByGet(url);
            logger.info("request accessToken success. [result={}]", object);
            if(object.keySet().contains("errcode")){
                 String errmsg = object.get("errmsg").toString().replaceAll("\"", "");
                 if("40029".equals(object.get("errcode"))){
                     url = String.format(WechatConstants.get_UserToken_Url,this.appId,this.appSecret,code);
                     object = getJsonObjectByGet(url);

                     openid = object.get("openid").toString().replaceAll("\"", "");
                     String access_token = object.get("access_token").toString().replaceAll("\"", "");
                     String refresh_token =  object.get("access_token").toString().replaceAll("\"", "");

                     cacheClient.setex(WechatConstants.user_access_token + openid , 6000, access_token);
                     cacheClient.setex(WechatConstants.user_refresh_token + openid , 10800, refresh_token);

                 }else {
                     return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), errmsg);
                 }
            }else {
                openid = object.get("openid").toString().replaceAll("\"", "");
                String access_token = object.get("access_token").toString().replaceAll("\"", "");
                String refresh_token =  object.get("access_token").toString().replaceAll("\"", "");

                cacheClient.setex(WechatConstants.user_access_token + openid, 6000, access_token);
                cacheClient.setex(WechatConstants.user_refresh_token + openid, 10800, refresh_token);
            }

        } catch (Exception ex) {
            logger.error("fail to request wechat access token. [error={}]", ex);

            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部系统异常");
        }

        return  new AjaxResult(openid);

    }

    /**
     *获取用户token
     * @throws Exception
     */
    public AjaxResult getUserTokenInfoByOpenid(String openid){

        String access_token = cacheClient.get(WechatConstants.user_access_token + openid );
        String refresh_token = cacheClient.get(WechatConstants.user_refresh_token + openid );

        try {

            if(StringUtils.isBlank(access_token)){
                //token失效
                if(StringUtils.isBlank(refresh_token)){
                    //refresh_token也失效
                    return new AjaxResult(AjaxResultCode.USER_UNLOGIN.getCode(),"用户未登陆");
                }else {
                    String url = String.format(WechatConstants.refresh_UserToken_Url, this.appId, refresh_token);
                    Map object = getJsonObjectByGet(url);
                    logger.info("request refresh accessToken success. [result={}]", object);
                    if (object.entrySet().contains("errcode")) {
                        String errmsg = object.get("errmsg").toString().replaceAll("\"", "");
                        return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), errmsg);
                    } else {
                        access_token =  object.get("access_token").toString().replaceAll("\"", "");

                        cacheClient.setex(WechatConstants.user_access_token + openid , 6000, access_token);

                    }
                }
            }

        } catch (Exception ex) {
            logger.error("fail to request wechat access token. [error={}]", ex);

            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),"内部系统异常");
        }

        return  new AjaxResult(access_token);

    }

    /**
     *获取平台token
     * @throws Exception
     */
    public String getPlatformTokenInfo(){
        String  url = String.format(WechatConstants.get_PlatformToken_Url,this.appId,this.appSecret);

        String access_token = cacheClient.get(WechatConstants.platform_access_token);

        try {

            logger.info("request platform_accessToken from url: {}", WechatConstants.get_PlatformToken_Url);

           if(StringUtils.isBlank(access_token)){
                Map object = getJsonObjectByGet(url);
                logger.info("request platform_accessToken success. [result={}]", object);

                if(object.entrySet().contains("errcode")){
                    String errmsg = object.get("errmsg").toString().replaceAll("\"", "");
                    logger.info("request platform_accessToken faile. [result={}]", errmsg);
                    access_token = null;
                }else {
                    access_token = object.get("access_token").toString().replaceAll("\"", "");
                    cacheClient.setex(WechatConstants.platform_access_token, 6000, access_token);
                }
            }

        } catch (Exception ex) {
            logger.error("fail to request wechat access token. [error={}]", ex);

            access_token = null;
        }

        return  access_token;

    }

    /**
     *刷新平台token
     * @throws Exception
     */
    public void refreshPlatformToken(){
        String  url = String.format(WechatConstants.get_PlatformToken_Url,this.appId,this.appSecret);


        try {

            logger.info("refresh platformToken from url: {}", url);


                Map object = getJsonObjectByGet(url);
                logger.info("request accessToken success. [result={}]", object);

                if(object.entrySet().contains("errcode")){
                    String errmsg = object.get("errmsg").toString().replaceAll("\"", "");
                    logger.info("request accessToken failed. [result={}]", errmsg);
                }else {
                    String access_token = object.get("access_token").toString().replaceAll("\"", "");

                    cacheClient.setex(WechatConstants.platform_access_token, 3000, access_token);
                }


        } catch (Exception ex) {
            logger.error("fail to request wechat access token. [error={}]", ex);

        }


    }


    /**
     * 验证token有效型
     * @param access_token
     * @param openid
     * @return
     */
    public boolean authUserToken(String access_token ,String openid  ){

        String url = String.format(WechatConstants.auth_UserToken_Url,access_token,openid);

        Map object = null;
        try {
            object = getJsonObjectByGet(url);

            String code = object.get("errcode").toString().replaceAll("\"", "");

            Validate.isTrue(code.equals("0")|| Integer.parseInt(code) == 0,"验证失败.");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();

            return false;
        }

        return  true;
    }

    /**
     * 获取公众号所属行业
     */
    public AjaxResult setIndustry(List<String> industryIds){

        String url = String.format(WechatConstants.set_Industry_Url,this.getPlatformTokenInfo());

        Map jsonResult= null;
        try {
            jsonResult = getJsonObjectByPost(url,JSON.toJSONString(industryIds));

            if(jsonResult!=null){
                int errorCode= Integer.parseInt(String.valueOf(jsonResult.get("errcode")));
                String errorMessage= String.valueOf(jsonResult.get("errmsg"));
                if(errorCode != 0){
                    logger.error("设置失败:"+errorCode+","+errorMessage);
                    return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(),errorMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("设置失败:"+e);
            return new AjaxResult(AjaxResultCode.REQUEST_INVALID.getCode(),"内部系统异常");
        }

        return  new AjaxResult();

    }

    /**
     * 微信消息通知
     * @return
     */
    public boolean sendMessage(TemplateVO template){

    boolean flag=false;
    //获取授权

    String url = String.format(WechatConstants.send_TemplateSMS_Url,this.getPlatformTokenInfo());


        Map jsonResult= null;
        try {
            jsonResult = getJsonObjectByPost(url,template.toJSON());

            if(jsonResult!=null){
                String errorCode= String.valueOf(jsonResult.get("errcode"));
                String errorMessage= String.valueOf(jsonResult.get("errmsg"));
                if("0".equals(errorCode)){
                    flag=true;
                }else{
//                    if("40001".equals(errorCode)){
//                        refreshPlatformToken();
//                        return  sendMessage(template);
//                    }
                    logger.error("模板消息发送失败:"+errorCode+","+errorMessage);
                    flag=false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("模板消息发送失败:"+e);
        }
        return flag;

    }










    private Map getJsonObjectByGet(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String tokens = EntityUtils.toString(httpEntity, "utf-8");

        return mapper.readValue(tokens,Map.class);
    }

    private Map getJsonObjectByPost (String url,String template) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type","application/json; charset=utf-8");

        httpPost.setEntity(new StringEntity(template, Charset.forName("UTF-8")));

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String tokens = EntityUtils.toString(httpEntity, "utf-8");
//        Gson datagson = new Gson();
        return mapper.readValue(tokens,Map.class);
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        String appid = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.appId);
        String appSecret = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.appSecret);

        this.appId = appid;
        this.appSecret = appSecret;

    }
}
