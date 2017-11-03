package com.fruit.sys.admin.utils;

/**
 * 微信端常量
 *
 * @author ivan
 */
public class WechatConstants
{

   public static final String get_Code_Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=0_status#wechat_redirect";

   public static final String get_PlatformToken_Url  ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

   public static final String get_UserToken_Url  = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

   public static final String refresh_UserToken_Url  = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

   public static final String auth_UserToken_Url  ="https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s";

   public static final String send_TemplateSMS_Url  = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

   public static final String set_Industry_Url  = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=%s";

   public static final String get_Industry_Url  = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=%s";

   public static final String add_Template_Url  = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=%s";

   public static final String get_All_Template_Url  = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";

   public static final String del_Template_Url  = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=%s";

   public static final String user_access_token = "USER_ACCESS_TOKEN_";

   public static final String user_refresh_token = "USER_REFRESH_TOKEN_";

   public static final String platform_access_token = "PLATFORM_ACCESS_TOKEN";

   public static final String enter1 = "WECHAT_ENTER1";

   public static final String enter2 = "WECHAT_ENTER2";

   public static final String enter3 = "WECHAT_ENTER3";

   public static final String template_1 = "WECHAT_TEMPLATE1";

   public static final String template_2 = "WECHAT_TEMPLATE2";

   public static final String template_3 = "WECHAT_TEMPLATE3";

   public static final String template_4 = "WECHAT_TEMPLATE4";

   public static final String appId = "WECHAT_APPID";

   public static final String appSecret = "WECHAT_APPSECRET";

   public static final String doamin = "WECHAT_DOMAIN";


}