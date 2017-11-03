/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.action.notice;

import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.admin.AdminNoticeService;
import com.fruit.sys.biz.dto.SysNotifyDTO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : points-biz-api
 * File Name      : NoticeListAction.java
 */
@Component
@UriMapping("/notice/center")
public class NoticeListAction extends BaseAction
{

    @Autowired
    private AdminNoticeService adminNoticeService;

//    @UriMapping("/list")
//    public String list()
//    {
//        try
//        {
//            UserInfo userInfo = super.getLoginUserInfo();
//            Validate.notNull(userInfo, "请先登录");
//            List<SysNotifyVO> notices = transfer(adminNoticeService.listUnchecked(userInfo.getSysId()));
//            WebContext.getRequest().setAttribute("admin_notices", notices);
//            return "/admin/notice/list";
//        }
//        catch (IllegalArgumentException e)
//        {
//            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
//            return FTL_ERROR_400;
//        }
//    }

    @UriMapping("/inquire")
    public JsonResult inquire()
    {
        try
        {
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.notNull(userInfo, "请先登录");
            List<SysNotifyDTO> sysNotifyDTOs = adminNoticeService.listUnchecked(userInfo.getSysId());
            int noticeCount = 0;
            Map<String, Object> data = new HashMap<String, Object>();
            if (CollectionUtils.isNotEmpty(sysNotifyDTOs))
            {
                noticeCount = sysNotifyDTOs.size();
                data.put("notice_list", sysNotifyDTOs);
            }
            data.put("notice_count", Integer.toString(noticeCount));
            return new JsonResult(data);
        }
        catch (IllegalArgumentException e)
        {
            return new JsonResult(e.getMessage());
        }
    }

    @UriMapping("/check_ajax")
    public JsonResult check()
    {
        try
        {
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.notNull(userInfo, "请先登录");
            int id = super.getIntParameter("id", 0);
            Validate.isTrue(id > 0, "参数错误");
            adminNoticeService.sysCheck(userInfo.getSysId(), id);
            return new JsonResult();
        }
        catch (IllegalArgumentException e)
        {
            return new JsonResult(e.getMessage());
        }
    }

    @UriMapping("/check_all_ajax")
    public JsonResult checkAll()
    {
        try
        {
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.notNull(userInfo, "请先登录");
            int type = super.getIntParameter("type", 0);
            adminNoticeService.sysbatchCheck(userInfo.getSysId(), type);
            return new JsonResult();
        }
        catch (IllegalArgumentException e)
        {
            return new JsonResult(e.getMessage());
        }
    }

//    private static List<SysNotifyVO> transfer(List<SysNotifyDTO> notices)
//    {
//        List<SysNotifyVO> result = Collections.EMPTY_LIST;
//        if (CollectionUtils.isNotEmpty(notices))
//        {
//            result = new ArrayList<SysNotifyVO>(notices.size());
//            for (SysNotifyDTO notice : notices)
//            {
//                SysNotifyVO vo = new SysNotifyVO();
//                result.add(vo);
//                BeanUtils.copyProperties(notice, vo);
//                AdminNotifyType type = AdminNotifyType.get(notice.getType());
//                vo.setTypeDesc(null == type ? "未知" : type.getMessage());
//            }
//        }
//        return result;
//    }
}
