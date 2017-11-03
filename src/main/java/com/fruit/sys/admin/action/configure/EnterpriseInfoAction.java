package com.fruit.sys.admin.action.configure;

import com.fruit.base.biz.common.DBStatusEnum;
import com.fruit.base.biz.common.EnterpriseTypeEnum;
import com.fruit.base.biz.common.LocationTypeEnum;
import com.fruit.base.biz.dto.EnterpriseInfoDTO;
import com.fruit.base.biz.request.EnterpriseInfoRequest;
import com.fruit.base.biz.request.sys.SysEnterpriseInfoListRequest;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.configure.EnterpriseInfoModel;
import com.fruit.sys.admin.service.configure.EnterpriseInfoListService;
import com.fruit.sys.admin.utils.CollectionTools;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.biz.common.Page;
import com.fruit.sys.biz.service.AdminMenuService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@UriMapping("/configure/enterpriseinfo")
public class EnterpriseInfoAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseInfoAction.class);

    private static final String CLAZZ_TYPE = EnterpriseInfoAction.class.getSimpleName();

    private static final int DEF_PAGE_SIZE = 7;

    /**
     * 代采的物流公司、清关公司
     */
    private static final List<Integer> noIncludeIds = new ArrayList<Integer>(){{
        add(1);
        add(2);
        add(3);
        add(4);
    }};

    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private EnterpriseInfoListService enterpriseInfoListService;



    @UriMapping(value = "/list")
    public String list()
    {
        String keyword = super.getStringParameter("keyword", "");

        int type = super.getIntParameter("type", -1);
        int status = super.getIntParameter("status", 1);
        int locationType = super.getIntParameter("locationType", -1);

        int pageNo = this.getIntParameter("pageNo", 1);
        int pageSize = this.getIntParameter("pageSize", DEF_PAGE_SIZE);

        try{

            EnterpriseTypeEnum typeEnum = -1 == type ? null : EnterpriseTypeEnum.get(type);
            DBStatusEnum statusEnum = DBStatusEnum.get(status);
            LocationTypeEnum locationTypeEnum = -1 == locationType ? null : LocationTypeEnum.get(locationType);

            SysEnterpriseInfoListRequest queryRequest = new SysEnterpriseInfoListRequest();
            queryRequest.setKeyword(keyword);
            queryRequest.setType(typeEnum);
            queryRequest.setStatus(statusEnum);
            queryRequest.setLocationType(locationTypeEnum);
            queryRequest.setSortKey("AddTime");
            queryRequest.setDesc(true);//按时间逆序
            queryRequest.setNotIncludeIds(noIncludeIds);//代采的物流公司、清关公司


            List<EnterpriseInfoModel> enterpriseInfoList = enterpriseInfoListService.loadEnterpriseInfoList(queryRequest, pageNo, pageSize);

            Page<EnterpriseInfoModel> page = new Page<EnterpriseInfoModel>();
            page.setCurrentPageNo(pageNo);
            page.setCurrentPageSize(pageSize);
            page.setResult(enterpriseInfoList);

            Map<String, Object> fixParams = this.fixParams();

            this.setAttribute("pageDto", page);
            this.setAttribute("params", fixParams);

            this.setAttribute("enterprise_list_url", UrlUtils.getEnterpriseInfoListUrl(super.getDomain()));
            this.setAttribute("enterprise_info_add_url", UrlUtils.getEnterpriseInfoAddUrl(super.getDomain()));
            this.setAttribute("enterprise_info_update_url", UrlUtils.getEnterpriseInfoUpdateUrl(super.getDomain()));
            this.setAttribute("enterprise_info_detail_url", UrlUtils.getEnterpriseInfoDetailUrl(super.getDomain()));
            this.setAttribute("enterprise_info_delete_url", UrlUtils.getEnterpriseInfoDeleteUrl(super.getDomain()));

            return "/configure/enterpriseinfo/enterprise_info_list";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/list].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/add")
    public String add()
    {
        try{
            this.setAttribute("enterprise_add_url", UrlUtils.getEnterpriseInfoAddInfoUrl(super.getDomain()));
            return "/configure/enterpriseinfo/enterprise_info_add";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/add].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/update")
    public String update()
    {
        int id = this.getIntParameter("id");

        try{
            EnterpriseInfoDTO enterpriseInfoDTO = null;
            if(id > 0)
            {
                enterpriseInfoDTO = enterpriseInfoListService.loadEnterpriseInfoById(id);
            }
            this.setAttribute("data", enterpriseInfoDTO);
            this.setAttribute("enterprise_update_url", UrlUtils.getEnterpriseInfoUpdateInfoUrl(super.getDomain()));
            return "/configure/enterpriseinfo/enterprise_info_update";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/update].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/detail")
    public String detail()
    {
        int id = this.getIntParameter("id");

        try{
            EnterpriseInfoDTO enterpriseInfoDTO = null;
            if(id > 0)
            {
                enterpriseInfoDTO = enterpriseInfoListService.loadEnterpriseInfoById(id);
            }
            this.setAttribute("data", enterpriseInfoDTO);
            return "/configure/enterpriseinfo/enterprise_info_detail";
        }catch (IllegalArgumentException e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/detail].Exception:{}",e);
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }



    @UriMapping(value = "/delete")
    public JsonResult delete()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            Validate.isTrue(0 > 1, "暂时不支持删除操作，请联系开发运营团队!");

            String ids = getRequiredStringParamter("ids");
            List<Integer> idList = CollectionTools.createIntList(ids);
            int count = enterpriseInfoListService.deleteEnterpriseInfos(idList);
            dto.setT(count);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/delete].Exception:{}",e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }


    /**
     * 获取国内国际物流公司、清关公司list，作为下拉框
     * v_nationalLogistics_info
     * @return
     */
    @UriMapping(value = "/enterprise_selector_list")
    public JsonResult enterpriseSelectorList()
    {
        JsonResult<Map<String, String>> dto = new JsonResult<Map<String, String>>();
        try
        {
            String key = getRequiredStringParamter("key");
            Map<String, String> map = enterpriseInfoListService.getEnterpriseListBySelector(key);
            dto.setT(map);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/enterprise_selector_list].Exception:{}",e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }




    /**
     * 新增方法
     * @return
     */
    @ResponseBody
    @UriMapping(value = "/enterprise_add_ajax", interceptors = {"logInterceptor","validationInterceptor"})
    public JsonResult addEnterpriseInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String name = (String) validationResults.get("name");
        String enName = (String) validationResults.get("enName");
        String credential = (String) validationResults.get("credential");
        String contact = (String) validationResults.get("contact");
        int type = (Integer) validationResults.get("type");
        int locationType = (Integer) validationResults.get("locationType");


        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {

            EnterpriseInfoRequest enterpriseInfoRequest = new EnterpriseInfoRequest();

            enterpriseInfoRequest.setName(name);
            enterpriseInfoRequest.setEnName(enName);
            enterpriseInfoRequest.setCredential(credential);
            enterpriseInfoRequest.setContact(contact);
            enterpriseInfoRequest.setType(type);
            enterpriseInfoRequest.setLocationType(locationType);

            int count =enterpriseInfoListService.countEnterpriseInfo(enterpriseInfoRequest);

            if(count < 1) {
                int id = enterpriseInfoListService.createEnterpriseInfo(enterpriseInfoRequest);

                dto.setT(id);
                dto.setResult(true);
                dto.setMsg("成功!");
            }else{
                dto.setResult(false);
                dto.setMsg("该物流公司已添加!");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/enterprise_add_ajax].Exception:{}",e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }


    /**
     * 修改方法
     * @return
     */
    @ResponseBody
    @UriMapping(value = "/enterprise_update_ajax", interceptors = {"logInterceptor","validationInterceptor"})
    public JsonResult updateEnterpriseInfo()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        int id = (Integer) validationResults.get("id");
        String name = (String) validationResults.get("name");
        String enName = (String) validationResults.get("enName");
        String credential = (String) validationResults.get("credential");
        String contact = (String) validationResults.get("contact");


        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {

            Validate.isTrue(id > 0, "参数错误!");


            EnterpriseInfoRequest enterpriseInfoRequest = new EnterpriseInfoRequest();
            enterpriseInfoRequest.setId(id);
            enterpriseInfoRequest.setName(name);
            enterpriseInfoRequest.setEnName(replaceNullToBlank(enName));
            enterpriseInfoRequest.setCredential(replaceNullToBlank(credential));
            enterpriseInfoRequest.setContact(replaceNullToBlank(contact));

            enterpriseInfoListService.updateEnterpriseInfo(enterpriseInfoRequest);

            dto.setT(id);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("[system][/configure/enterpriseinfo/enterprise_update_ajax].Exception:{}",e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }

    private String replaceNullToBlank(String enName) {

        if(null == enName){
            return "";
        }
        return enName;
    }
}


