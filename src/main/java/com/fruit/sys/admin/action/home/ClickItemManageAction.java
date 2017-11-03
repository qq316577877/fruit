/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.home;

import com.fruit.base.biz.common.DBStatusEnum;
import com.fruit.loan.biz.socket.util.DateStyle;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.utils.BizConstants;
import com.fruit.sys.admin.utils.DateUtil;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import com.fruit.portal.biz.dto.ClickItemDTO;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResultDTO;
import com.fruit.sys.admin.model.home.ClickItemModel;
import com.fruit.sys.admin.service.home.ClickItemManageService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 点击新闻、资讯、banner图管理
 */
@Component
@UriMapping("/home/clickitem")
public class ClickItemManageAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickItemManageAction.class);

    private static final String CLICK_ITEM_ENCODE = "utf-8";

    @Autowired
    private ClickItemManageService clickItemManageService;

    @UriMapping("/list" )
    public String showList()
    {
        int type = super.getIntParameter("type", -1);
        int size = BizConstants.DEFAULT_PAGE_SIZE;

        List<ClickItemModel> result = this.clickItemManageService.loadList(type,size);

        HttpServletRequest request = WebContext.getRequest();
        request.setAttribute("records", result);
        request.setAttribute("potalDomain", super.getPortalDomain());

        List<IdValueVO> typeList =  new ArrayList<IdValueVO>(ClickItemManageService.POSITIONS.size()+1);
        typeList.add(new IdValueVO(-1, "全部"));
        for (IdValueVO idValueVO :  ClickItemManageService.POSITIONS)
        {
            if(type==idValueVO.getId()){
                idValueVO.setSelected(1);
            }else{
                idValueVO.setSelected(0);
            }
            typeList.add(idValueVO);
        }
        request.setAttribute("positionItems",typeList);

        return "/home/clickitem/list";
    }

    @UriMapping(value = "/detail")
    public String detail()
    {
        HttpServletRequest request = WebContext.getRequest();

        int id = super.getIntParameter("id");
        ClickItemDTO clickItemDTO = null;
        if (id > 0)
        {
            clickItemDTO = this.clickItemManageService.loadById(id);
        }
        else
        {
            clickItemDTO = new ClickItemDTO();
        }

        int type = clickItemDTO.getType();
        List<IdValueVO> typeList =  new ArrayList<IdValueVO>(ClickItemManageService.POSITIONS.size()+1);
        for (IdValueVO idValueVO :  ClickItemManageService.POSITIONS)
        {
            if(type==idValueVO.getId()){
                idValueVO.setSelected(1);
            }else{
                idValueVO.setSelected(0);
            }
            typeList.add(idValueVO);
        }

        request.setAttribute("positionItems", typeList);
        ClickItemModel clickItemModel = null;
        if(clickItemDTO!=null){
            clickItemModel = this.clickItemManageService.transfer(clickItemDTO);
        }

        request.setAttribute("data", clickItemModel);

        return "/home/clickitem/detail";
    }

    @UriMapping(value = "/save_ajax", interceptors = "logInterceptor")
    public JsonResultDTO save()
    {
        JsonResultDTO<Boolean> jsonResult = new JsonResultDTO<Boolean>();
        try
        {
            int id = super.getIntParameter("id");
            int priority = super.getIntParameter("priority");
            String title = super.getStringParameter("title");
            String subtitle = super.getStringParameter("subtitle");
            String link = super.getStringParameter("link");
            String time = super.getStringParameter("time");
            String imgUrl = super.getStringParameter("imgUrl");
            int type = super.getIntParameter("type", -1);
            String description = super.getStringParameter("description");

            String encodeTitle = StringEscapeUtils.unescapeHtml(title);
            String encodeSubTitle = StringEscapeUtils.unescapeHtml(subtitle);
            String encodeDescription = StringEscapeUtils.unescapeHtml(description);

            ClickItemDTO itemDTO = new ClickItemDTO();
            itemDTO.setId(id);
            itemDTO.setStatus(DBStatusEnum.NORMAL.getStatus());
            itemDTO.setTitle(encodeTitle);
            itemDTO.setSubtitle(encodeSubTitle);
            itemDTO.setImgUrl(imgUrl);
            itemDTO.setType(type);
            itemDTO.setLink(link);
            itemDTO.setPriority(priority);
            itemDTO.setDescription(encodeDescription);

            if(StringUtils.isNotEmpty(time)){
                Date newsTime = DateUtil.StringToDateAndSetTime(time);
                itemDTO.setTime(newsTime);
            }

            this.clickItemManageService.save(itemDTO);

            jsonResult.setResult(true);
            jsonResult.setMsg("success");
        }
        catch (Exception e)
        {
            LOGGER.error("[system][/home/clickitem/save_ajax].Exception:{}",e);
            jsonResult.setResult(false);
            jsonResult.setMsg("失败:" + e.getMessage());
        }
        return jsonResult;
    }

}
