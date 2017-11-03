package com.fruit.sys.admin.action;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.utils.CollectionTools;
import com.fruit.sys.biz.common.Page;
import com.fruit.sys.biz.dto.AdminMenuDTO;
import com.fruit.sys.biz.service.AdminMenuService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Component
@UriMapping("/menu")
public class MenuAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuAction.class);

    private static final String CLAZZ_TYPE = MenuAction.class.getSimpleName();

    private static final int DEF_PAGE_SIZE = 7;

    @Autowired
    private AdminMenuService adminMenuService;

    @UriMapping(value = "/list")
    public String list()
    {
        String name = this.getStringParameter("name");
        int pageNo = this.getIntParameter("pageNo", 1);
        int pageSize = this.getIntParameter("pageSize", DEF_PAGE_SIZE);

        Page<AdminMenuDTO> page = adminMenuService.queryByPage(StringUtils.trimToEmpty(name), pageNo, pageSize);
        Map<String, Object> fixParams = this.fixParams();

        this.setAttribute("pageDto", page);
        this.setAttribute("params", fixParams);

        return "/menu/menu_list";
    }

    @UriMapping(value = "/detail")
    public String detail()
    {
        AdminMenuDTO menuDTO = null;
        int id = this.getIntParameter("id");
        if(id > 0)
        {
             menuDTO = adminMenuService.loadById(id);
        }
        this.setAttribute("data", menuDTO);
        return "/menu/menu_detail";
    }

    @ResponseBody
    @UriMapping(value = "/save", interceptors = "logInterceptor")
    public JsonResult save()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            AdminMenuDTO obj = new AdminMenuDTO();
            obj.setName(getStringParameter("name"));
            obj.setPId(getIntParameter("PId"));
            obj.setUrl(getStringParameter("url"));
            obj.setStatus(getByteParameter("status"));
            obj.setType(getByteParameter("type"));
            obj.setNamespace(getStringParameter("namespace"));

            int id = 0;
            int id_ = getIntParameter("id");
            if (id_ > 0)
            {
                obj.setId(id_);
            }

            if(id_ > 0)
            {
                adminMenuService.update(obj);
            }
            else
            {
                id = adminMenuService.create(obj);
            }
            dto.setT(id);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("save error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }

    @UriMapping(value = "/delete")
    public JsonResult delete()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            String ids = getRequiredStringParamter("ids");
            List<Integer> idList = CollectionTools.createIntList(ids);
            int count = adminMenuService.delete(idList);
            dto.setT(count);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("delete error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }
}


