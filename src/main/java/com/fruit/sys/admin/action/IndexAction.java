package com.fruit.sys.admin.action;

import com.fruit.sys.admin.exception.ParamInvalidException;
import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.model.notice.SysNotifyVO;
import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.service.admin.AdminNoticeService;
import com.fruit.sys.admin.tree.Tree;
import com.fruit.sys.admin.utils.UrlUtils;
import com.fruit.sys.biz.common.AdminNotifyType;
import com.fruit.sys.biz.dto.AdminMenuDTO;
import com.fruit.sys.biz.dto.SysNotifyDTO;
import com.ovfintech.arch.utils.CookieEncryptTools;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.dispatch.result.JsonpResult;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@UriMapping("/")
public class IndexAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexAction.class);

    private static final String CLAZZ_TYPE = IndexAction.class.getSimpleName();

    @Autowired
    private CoreService coreService;

    @Autowired
    private AdminNoticeService adminNoticeService;

    /**
     * 首页
     */
    @UriMapping(value = "/index")
    public String showIndex()
    {
        UserInfo userInfo = getUserInfo();
        Tree<AdminMenuDTO> menus = null;
        if (userInfo != null)
        {
            menus = userInfo.isAdmin() ? coreService.getMenuTree() : coreService.getMenuTree(userInfo.getSysId());

            this.setAttribute("AdminMenus", menus.getNodeList());
            super.setAttribute("_header_admin_notice_count", adminNoticeService.getNoticeCount(userInfo.getSysId()));
        }
        setNoAuthorUrl();
        return "index";
    }

    private void setNoAuthorUrl()
    {
        HttpServletRequest request = WebContext.getRequest();
        request.setAttribute("logoutUrl", UrlUtils.getLogoutUrl(super.getDomain()));
    }

    /**
     * 首页
     */
    @UriMapping(value = "/main")
    public String main()
    {
        try
        {
            UserInfo userInfo = super.getLoginUserInfo();
            Validate.notNull(userInfo, "请先登录");
            List<SysNotifyVO> notices = transfer(adminNoticeService.listUnchecked(userInfo.getSysId()));
            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("admin_notices", notices);
            return "/main";
        }
        catch (IllegalArgumentException e)
        {
            WebContext.getRequest().setAttribute("error_msg", e.getMessage());
            return FTL_ERROR_400;
        }
    }

    @UriMapping("/dper")
    public JsonpResult dper()
    {
        JsonResult<String> jsonpData = new JsonResult<String>();
        try
        {
            String encryptedDper = getRequiredStringParamter("dper");
            jsonpData.setT(CookieEncryptTools.decryptDper(encryptedDper));
            jsonpData.setResult(true);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            jsonpData.setResult(false);
            jsonpData.setMsg(e.getMessage());
        }
        String jsonp = getStringParameter("jsonp", "callback");
        return new JsonpResult(jsonp, jsonpData);
    }

    @UriMapping("/dperJson")
    public JsonResult dperJson()
    {
        JsonResult<String> jsonpData = new JsonResult<String>();
        try
        {
            String encryptedDper = getRequiredStringParamter("dper");
            jsonpData.setT(CookieEncryptTools.decryptDper(encryptedDper));
            jsonpData.setResult(true);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            jsonpData.setResult(false);
            jsonpData.setMsg(e.getMessage());
        }
        return jsonpData;
    }

    @UriMapping("/remoteMenu")
    public JsonResult remoteMenu()
    {
        JsonResult<String> jsonpData = new JsonResult<String>();
        try
        {
            int dperId = getIntParameter("dperId");

            if (dperId > 0)
            {
                UserInfo userInfo = getUserInfo();
                if (userInfo == null)
                {
                    throw new ParamInvalidException("please login first.");
                }
                dperId = userInfo.getSysId();
            }
            String menus = coreService.userMenus(dperId);
            jsonpData.setT(menus);
            jsonpData.setResult(true);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            jsonpData.setResult(false);
            jsonpData.setMsg(e.getMessage());
        }
        return jsonpData;
    }

    @UriMapping(value = "/noPermission")
    public String noPermission()
    {
        setNoAuthorUrl();
        return "noPermission";
    }

    private static List<SysNotifyVO> transfer(List<SysNotifyDTO> notices)
    {
        List<SysNotifyVO> result = Collections.EMPTY_LIST;
        if (CollectionUtils.isNotEmpty(notices))
        {
            result = new ArrayList<SysNotifyVO>(notices.size());
            for (SysNotifyDTO notice : notices)
            {
                SysNotifyVO vo = new SysNotifyVO();
                result.add(vo);
                BeanUtils.copyProperties(notice, vo);
                AdminNotifyType type = AdminNotifyType.get(notice.getType());
                vo.setTypeDesc(null == type ? "未知" : type.getMessage());
            }
        }
        return result;
    }

}


