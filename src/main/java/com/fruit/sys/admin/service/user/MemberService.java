package com.fruit.sys.admin.service.user;

import com.fruit.account.biz.common.*;
import com.fruit.account.biz.dto.*;
import com.fruit.account.biz.request.UserAccountQueryRequest;
import com.fruit.account.biz.request.sys.SysUserListRequest;
import com.fruit.account.biz.service.*;
import com.fruit.account.biz.service.sys.SysUserAccountService;
import com.fruit.account.biz.service.sys.SysUserEnterpriseService;
import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.message.biz.common.MessageTypeEnum;
import com.fruit.sys.admin.event.ITask;
import com.fruit.sys.admin.event.TaskEvent;
import com.fruit.sys.admin.model.PageResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.UserInfoQueryRequest;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.EnterpriseVO;
import com.fruit.sys.admin.model.wechat.TemplateParamVO;
import com.fruit.sys.admin.model.wechat.TemplateVO;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.CacheManageService;
import com.fruit.sys.admin.service.common.MessageService;
import com.fruit.sys.admin.service.common.RedBankService;
import com.fruit.sys.admin.service.common.RuntimeConfigurationService;
import com.fruit.sys.admin.service.wechat.WeChatBaseService;
import com.fruit.sys.admin.utils.BizUtils;
import com.fruit.sys.admin.utils.WechatConstants;
import com.ovfintech.arch.common.event.EventChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@Service
public class MemberService extends BaseService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private static final String VERIFIED_SMS_CONTENT = "【九创金服】尊敬的客户，您的会员认证信息已通过审核。";

    private static final String REJECTED_SMS_CONTENT = "【九创金服】您的会员认证信息未通过审核，请登录平台查看详情。";

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SysUserAccountService sysUserAccountService;

    @Autowired
    private UserEnterpriseService userEnterpriseService;

    @Autowired
    private SysUserEnterpriseService sysUserEnterpriseService;

    @Autowired
    private CacheManageService cacheManageService;

    @Autowired
    private UserInfoUpdateLogService infoUpdateLogService;

    @Autowired
    private RedBankService redBankService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private  UserListService userListService;

    @Autowired
    private RuntimeConfigurationService runtimeConfigurationService;

    @Autowired
    private WeChatBaseService weChatBaseService;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    public int userCount(UserStatusEnum status, UserTypeEnum type,UserEnterpriseStatusEnum enterpriseStatus)
    {
        return sysUserAccountService.count(status, type,enterpriseStatus, super.getUserBacklistIds());
    }

    /**
     * 计算各注册方式的用户个数
     * @param status
     * @param type
     * @param source
     * @return
     */
    public int userCount(UserStatusEnum status, UserTypeEnum type,UserEnterpriseStatusEnum enterpriseStatus, UserAccountSourceEnum source)
    {
        return sysUserAccountService.count(status, type,enterpriseStatus, source, super.getUserBacklistIds());
    }

    /**
     * 计算各个条件下的用户个数
     * @param queryRequest
     * @return
     */
    public int userCount(UserInfoQueryRequest queryRequest)
    {
        SysUserListRequest sysUserListRequest = userListService.transfer(queryRequest);
        return sysUserAccountService.count(sysUserListRequest);
    }

    public List<UserModel> loadByEnterpriseStatus(UserEnterpriseStatusEnum status, String sortKey, boolean desc, int pageNo, int pageSize)
    {
        List<UserAccountDTO> userAccountDTOs = sysUserAccountService.loadByEnterpriseStatus(status, super.getUserBacklistIds(), sortKey, desc, pageNo, pageSize);
        if (CollectionUtils.isEmpty(userAccountDTOs))
        {
            return Collections.EMPTY_LIST;
        }
        List<UserModel> userModelList = new ArrayList<UserModel>(userAccountDTOs.size());
        for (UserAccountDTO user : userAccountDTOs)
        {
            UserModel userModel = userListService.loadUserModel(user, true);
            if (null != userModel.getEnterprise())
            {
                userModelList.add(userModel);
            }
        }
        return userModelList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void enterpriseVerify(int userId, UserEnterpriseStatusEnum status, UserTypeEnum type, String rejectNote, String lastEditor, String userIp)
    {
        Validate.isTrue(userId > 0);
        UserModel userModel = userListService.loadUserModel(userId, true);
        Validate.isTrue(null != userModel, "账号信息异常!");
        EnterpriseVO enterprise = userModel.getEnterprise();
        Validate.isTrue(null != enterprise, "账号信息异常!");
        rejectNote = StringUtils.abbreviate(rejectNote, 64);
        sysUserAccountService.updateVerifyStatusAndType(userId, status, type, lastEditor);
        sysUserEnterpriseService.updateStatus(userId, status, type, rejectNote, lastEditor);
        cacheManageService.syncUserModel(userId);
        if (UserEnterpriseStatusEnum.VERIFIED == status)
        {
            this.redBankService.syncFireEvent(userId, MessageTypeEnum.ENTERPRISE_VERIFY_PASS, lastEditor);
        }
        else if (UserEnterpriseStatusEnum.REJECTED == status)
        {
            this.redBankService.syncFireEvent(userId, MessageTypeEnum.ENTERPRISE_VERIFY_REJECTED, lastEditor);
        }
        asyncSaveUpdateLog(userModel, "update enterprise verify status", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), lastEditor, userIp);
    }

    public int countEnterprise(UserEnterpriseStatusEnum status)
    {
        Validate.isTrue(null != status, "参数错误!");
        return sysUserEnterpriseService.countByStatus(status.getStatus(), super.getUserBacklistIds());
    }



    @Transactional(rollbackFor = Exception.class)
    public void update(int userId,String mobile, String mail, String QQ, String lastEditor, String userIp)
    {
        Validate.isTrue(userId > 0, "参数错误!");
        UserAccountDTO mobileDTO = userAccountService.loadByMobile(mobile);
        Validate.isTrue(null == mobileDTO || userId == mobileDTO.getId(), "该手机号已注册!");
        sysUserAccountService.updateBaseInfo(userId, mobile, mail, QQ, lastEditor);
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin update user base info.", UserInfoUpdateLogTypeEnum.BASE_INFO_UPDATE.getType(), lastEditor, userIp);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEnterprise(EnterpriseVO enterpriseVO, String userIp)
    {
        int userId = enterpriseVO.getUserId();
        UserEnterpriseDTO userEnterpriseDTO = userEnterpriseService.loadByUserId(userId);
        if (userEnterpriseDTO != null)
        {
            userEnterpriseDTO.setName(enterpriseVO.getName());
            userEnterpriseDTO.setProvinceId(enterpriseVO.getProvinceId());
            userEnterpriseDTO.setCityId(enterpriseVO.getCityId());
            userEnterpriseDTO.setAddress(enterpriseVO.getAddress());
            userEnterpriseDTO.setPhoneNum(enterpriseVO.getPhoneNum());
            userEnterpriseDTO.setLicence(enterpriseVO.getLicence());
            userEnterpriseDTO.setLastEditor(enterpriseVO.getLastEditor());
            userEnterpriseDTO.setUpdateTime(new Date());
            userEnterpriseService.update(userEnterpriseDTO);
            cacheManageService.syncUserModel(userId);
            asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin update user enterprise info.", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), enterpriseVO.getLastEditor(), userIp);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateEnterpriseVerify(EnterpriseVO enterpriseVO, String userIp)
    {
        int userId = enterpriseVO.getUserId();
        UserEnterpriseDTO userEnterpriseDTO = userEnterpriseService.loadByUserId(userId);
        if (userEnterpriseDTO != null)
        {
//            userEnterpriseDTO.setId(enterpriseVO.getId());
            userEnterpriseDTO.setUserId(enterpriseVO.getUserId());
            userEnterpriseDTO.setMemberIdentification(enterpriseVO.getMemberIdentification());
            userEnterpriseDTO.setRejectNote(enterpriseVO.getRejectNote());
            userEnterpriseDTO.setDescription(enterpriseVO.getDescription());
            userEnterpriseDTO.setStatus(enterpriseVO.getStatus());
            userEnterpriseDTO.setLastEditor(enterpriseVO.getLastEditor());
            userEnterpriseDTO.setUpdateTime(new Date());
            userEnterpriseService.update(userEnterpriseDTO);

            //同时修改user_accout表中的状态
            userAccountService.updateEnterpriseVerifyStatusAndType(enterpriseVO.getUserId(), enterpriseVO.getStatus(),enterpriseVO.getType(), enterpriseVO.getLastEditor());

            cacheManageService.syncUserModel(userId);
            asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin verify user enterprise auth.", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), enterpriseVO.getLastEditor(), userIp);

            String wechatSend = "尊敬的客户，您的会员认证信息已通过审核！";
            //发送短信
            UserAccountDTO userAccountDTO = userAccountService.loadById(userId);
            String mobile = userAccountDTO.getMobile();
            if(enterpriseVO.getStatus()==UserEnterpriseStatusEnum.REJECTED.getStatus()){//认证未通过
                messageService.sendMobileMessage(userId, mobile, REJECTED_SMS_CONTENT);
                wechatSend = "尊敬的客户，您的会员认证信息未通过审核！";
            }else if(enterpriseVO.getStatus()==UserEnterpriseStatusEnum.VERIFIED.getStatus()){//认证通过
                messageService.sendMobileMessage(userId, mobile, VERIFIED_SMS_CONTENT);
            }
            //微信提示
            String openid = userAccountDTO.getOpenid();
            if(StringUtils.isNotBlank(openid)) {
                TemplateVO template = new TemplateVO();
                template.setToUser(openid);
                String templateId = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.template_1);
                String urlEnter = runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, WechatConstants.doamin);
                template.setTemplateId(templateId);
                template.setUrl(urlEnter);

                template.setTopColor("#000000");
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                List<TemplateParamVO> dataListP = new ArrayList<TemplateParamVO>();
                dataListP.add(new TemplateParamVO("first", wechatSend, "#000000"));
                dataListP.add(new TemplateParamVO("keyword1", "会员认证", "#000000"));
                dataListP.add(new TemplateParamVO("keyword2", format.format(new Date()), "#000000"));
                dataListP.add(new TemplateParamVO("keyword3", UserEnterpriseStatusEnum.get(enterpriseVO.getStatus()).getMessage(), "#000000"));
                dataListP.add(new TemplateParamVO("remark", "欢迎随时前往公众号查看详情", "#000000"));


                template.setTemplateParamList(dataListP);

                weChatBaseService.sendMessage(template);
            }

        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void asyncSaveUpdateLog(final UserModel userModel, final String field, final int type, final String operator, final String userIp)
    {
        this.taskEventChannel.publish(new TaskEvent(new ITask()
        {
            @Override
            public void doTask()
            {
                if (null != userModel)
                {
                    UserInfoUpdateLogDTO infoUpdateLogDTO = new UserInfoUpdateLogDTO();
                    infoUpdateLogDTO.setUserId(userModel.getUserId());
                    infoUpdateLogDTO.setEnterpriseName(null != userModel.getEnterprise() ? userModel.getEnterprise().getName() : "");
                    infoUpdateLogDTO.setOperator(operator);
                    infoUpdateLogDTO.setUserIp(userIp);
                    infoUpdateLogDTO.setType(type);
                    infoUpdateLogDTO.setInfo(field);
                    infoUpdateLogService.create(infoUpdateLogDTO);
                }
            }
        }));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserDecription(UserInfo admin, int userId, String userIp, String decription, int userType)
    {
        UserAccountDTO userAccountDTO = userAccountService.loadById(userId);
        Validate.isTrue(null != userAccountDTO, "用户信息异常!");
        if (StringUtils.isNotEmpty(decription))
        {
            userAccountDTO.setDescription(decription);
        }
        userAccountDTO.setType(userType);
        userAccountDTO.setLastEditor(admin.getUserName());
        userAccountService.update(userAccountDTO);
        UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userId);
        Validate.isTrue(null != userEnterpriseDTO, "账号信息异常!");
        userEnterpriseDTO.setType(userType);
        userEnterpriseDTO.setLastEditor(admin.getUserName());
        userEnterpriseService.update(userEnterpriseDTO);
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Freeze user account", UserInfoUpdateLogTypeEnum.BASE_INFO_UPDATE.getType(), admin.getUserName(), userIp);
    }

    /**
     * 检查手机号是否已注册
     *
     * @param mobile
     * @return 手机号已注册则返回true，否则返回false
     */
    public boolean isMobileRegistered(String mobile)
    {
        UserAccountDTO userAccountDTO = userAccountService.loadByMobile(mobile);
        return null != userAccountDTO;
    }


    @Transactional(rollbackFor = Exception.class)
    public void enterpriseSaveVerify(int userId, int type, String enterpriseName, int provinceId, int cityId, String address, String phone, String lastEditor, String userIp)
    {
        Validate.isTrue(userId > 0);
        UserModel userModel = userListService.loadUserModel(userId, false);
        Validate.isTrue(null != userModel, "账号信息异常!");
        UserEnterpriseDTO userEnterpriseDTO = this.userEnterpriseService.loadByUserId(userId);
        Validate.isTrue(null != userEnterpriseDTO, "账号信息异常!");
        UserAccountDTO userAccountDTO = userAccountService.loadById(userId);
        userAccountDTO.setId(userId);
        userAccountDTO.setEnterpriseVerifyStatus(UserEnterpriseStatusEnum.VERIFIED.getStatus());
        userAccountDTO.setType(type);
        userAccountDTO.setLastEditor(lastEditor);
        userAccountDTO.setStatus(UserStatusEnum.NORMAL.getStatus());
        userAccountService.update(userAccountDTO);
        userEnterpriseDTO.setPhoneNum(phone);
        userEnterpriseDTO.setName(enterpriseName);
        userEnterpriseDTO.setProvinceId(provinceId);
        userEnterpriseDTO.setCityId(cityId);
        userEnterpriseDTO.setAddress(address);
        userEnterpriseDTO.setLastEditor(lastEditor);
        userEnterpriseDTO.setStatus(UserEnterpriseStatusEnum.VERIFIED.getStatus());
        userEnterpriseDTO.setType(type);
        userEnterpriseService.update(userEnterpriseDTO);
        cacheManageService.syncUserModel(userId);
        this.redBankService.syncFireEvent(userId, MessageTypeEnum.ENTERPRISE_VERIFY_PASS, lastEditor);
        asyncSaveUpdateLog(userModel, "update enterprise verify status", UserInfoUpdateLogTypeEnum.ENTERPRISE_AUTH.getType(), lastEditor, userIp);
    }

    /**
     * 根据mobile、企业类型加载用户
     * @param mobile
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageResult<UserModel> loadUserModels(String mobile,int type, int pageNo, int pageSize)
    {
        List<Integer> unionIds = Collections.EMPTY_LIST;
        if (StringUtils.isNotBlank(mobile))
        {
            unionIds = this.userListService.searchUserIds(mobile);
        }
        PageResult<UserModel> pageResult = new PageResult<UserModel>(pageSize, pageNo);
        UserAccountQueryRequest userAccountQueryRequest = new UserAccountQueryRequest();
        userAccountQueryRequest.setUserIds(unionIds);
        userAccountQueryRequest.setType(type);
        com.fruit.account.biz.common.PageModel<UserAccountDTO> pageModel = userAccountService.query(userAccountQueryRequest, pageNo, pageSize);
        pageResult.setTotalCount(pageModel.getTotalCount());
        pageResult.setTotalPage((int) Math.ceil(pageModel.getTotalCount() * 1.0 / pageModel.getPageSize()));
        pageResult.setRecords(this.loadUserModel(pageModel.getRecords()));
        return pageResult;
    }

    private List<UserModel> loadUserModel(List<UserAccountDTO> userAccountDTOs)
    {
        List<UserModel> userModels = new ArrayList<UserModel>();
        if (CollectionUtils.isNotEmpty(userAccountDTOs))
        {
            for (UserAccountDTO userAccountDTO : userAccountDTOs)
            {
                userModels.add(this.userListService.loadUserModel(userAccountDTO, true));
            }
        }
        return userModels;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(int userId, UserStatusEnum status, String lastEditor, String userIp)
    {
        userAccountService.updateStatus(userId, status.getStatus(), lastEditor);
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Freeze user account", UserInfoUpdateLogTypeEnum.STATUS_UPDATE.getType(), lastEditor, userIp);
    }


    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(int userId,String password, String lastEditor, String userIp)
    {
        Validate.isTrue(userId > 0, "参数错误!");
        String md5Password = BizUtils.md5Password(password);
        userAccountService.updatePassword(userId, md5Password,lastEditor);
        cacheManageService.syncUserModel(userId);
        asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin update user password.", UserInfoUpdateLogTypeEnum.PASSWORD_UPDATE.getType(), lastEditor, userIp);
    }


    public int userAuthCount(UserStatusEnum status, UserTypeEnum type,UserEnterpriseStatusEnum enterpriseStatus)
    {
        List<Integer> notIncludeStatus = new ArrayList<Integer>(3);
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.DELETED.getStatus()));
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.VERIFIED.getStatus()));

        return sysUserAccountService.authcount(status, type,enterpriseStatus,notIncludeStatus, super.getUserBacklistIds());
    }

    public int userAuthCount(UserInfoQueryRequest queryRequest)
    {
        List<Integer> notIncludeStatus = new ArrayList<Integer>(3);
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.DELETED.getStatus()));
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.VERIFIED.getStatus()));
        notIncludeStatus.add(Integer.valueOf(UserEnterpriseStatusEnum.NOT_YET.getStatus()));//20170807 未认证的排除显示

        //用户类型排除 20170807
        UserTypeEnum type = queryRequest.getType();
        List<Integer>  types = new ArrayList<Integer>();
        if(type != null){
        types.add(type.getType());
        }

        List<Integer> userIds = sysUserEnterpriseService.searchIdsByTypesStatus(types,null);
        queryRequest.setIncludeIds(userIds);


        SysUserListRequest request = userListService.transfer(queryRequest);

        return sysUserAccountService.authcount(request);
    }


}
