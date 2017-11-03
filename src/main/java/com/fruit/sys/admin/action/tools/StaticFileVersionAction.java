/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.tools;

import com.fruit.base.biz.dto.StaticFileVersionDTO;
import com.fruit.loan.biz.common.LoanUserApplyCreditStatusEnum;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.common.StaticFileVersionEnum;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.tool.StaticFileVersionModel;
import com.fruit.sys.admin.service.tool.StaticFileVersionManageService;
import com.fruit.sys.admin.service.tool.StaticFileVersionRefreshService;
import com.fruit.sys.admin.spy.SpyManageService;
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
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : StaticFileVersionAction.java
 */
@Component
@UriMapping("/staticFileVersion")
public class StaticFileVersionAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticFileVersionAction.class);

    private static final List<IdValueVO> STATIC_FILE_VERSION_LIST = new ArrayList<IdValueVO>();

    static
    {

        StaticFileVersionEnum[] values = StaticFileVersionEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (StaticFileVersionEnum status : values)
            {
                STATIC_FILE_VERSION_LIST.add(new IdValueVO(status.getCode(), status.getMessage()));
            }
        }
    }

    @Autowired
    private StaticFileVersionManageService staticFileVersionManageService;

    @Autowired
    private SpyManageService spyManageService;

    @Autowired
    private StaticFileVersionRefreshService staticFileVersionRefreshService;

    @UriMapping("/list")
    public String list()
    {
        List<StaticFileVersionModel> models = this.staticFileVersionManageService.loadList();
        HttpServletRequest request = WebContext.getRequest();
        request.setAttribute("staticFileVersionModels", models);
        return "/tool/staticFileVersion/list";
    }

    @UriMapping("/detail")
    public String detail()
    {
        int id = super.getIntParameter("id");

        HttpServletRequest request = WebContext.getRequest();
        if (id > 0)
        {
            StaticFileVersionModel model = this.staticFileVersionManageService.loadById(id);
            request.setAttribute("data", model);
        }
        if(id == 0)
        {
            request.setAttribute("static_file_version_list", STATIC_FILE_VERSION_LIST);
        }
        return "/tool/staticFileVersion/detail";
    }

    @UriMapping(value = "/save_ajax", interceptors = {"logInterceptor", "validationInterceptor"})
    public JsonResult saveStaticFileVersion()
    {
        JsonResult<Boolean> jsonResult = new JsonResult<Boolean>();
        Map<String, Object> validationResults = super.getParamsValidationResults();

        int id = (Integer) validationResults.get("id");
        String project = (String) validationResults.get("project");
        String version = (String) validationResults.get("version");
        String description = (String) validationResults.get("description");

        try
        {
            StaticFileVersionDTO dto = new StaticFileVersionDTO();
            dto.setId(id);
            if (id == 0) {
                dto.setProject(project);
            }
            dto.setVersion(version);
            dto.setDescription(description);
            dto.setLastEditor(super.getLoginUserInfo().getUserName());
            this.staticFileVersionManageService.save(dto);
            jsonResult.setResult(true);
        }
        catch (Exception e)
        {
            LOGGER.error("save error", e);
            jsonResult.setResult(false);
            jsonResult.setMsg("失败:" + e.getMessage());
        }

        return jsonResult;
    }

    @UriMapping(value = "/sync_ajax", interceptors = "logInterceptor")
    public JsonResult syncStaticFileVersion()
    {
        JsonResult<Boolean> jsonResult = new JsonResult<Boolean>();
        String type = super.getStringParameter("type");
        try
        {
            if ("portal".equals(type))
            {
//                this.spyManageService.handleSpy("staticVersionService", "refreshStaticServer");
                this.staticFileVersionRefreshService.refreshVersion();

            }
            else if ("h5".equals(type))
            {
                this.spyManageService.handleSpy("staticVersionH5Service", "refreshStaticServer");
            }

            jsonResult.setResult(true);
        }
        catch (Exception e)
        {
            LOGGER.info("同步静态资源版本刷新失败!", e);
            jsonResult.setResult(false);
            jsonResult.setMsg("同步静态资源版本刷新失败:" + e.getMessage());
        }
        return jsonResult;
    }
}
