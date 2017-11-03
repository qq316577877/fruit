package com.fruit.sys.admin.action;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.tree.Tree;
import com.fruit.sys.biz.dto.AdminMenuDTO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@UriMapping("/tree")
public class TreeAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeAction.class);

    private static final String CLAZZ_TYPE = TreeAction.class.getSimpleName();

    @Autowired
    private CoreService coreService;

    @UriMapping(value = "/template")
    public String template()
    {
        return "/components/tree_open";
    }

    @ResponseBody
    @UriMapping(value = "/menu")
    public JsonResult menu()
    {
        JsonResult<Tree<AdminMenuDTO>> dto = new JsonResult<Tree<AdminMenuDTO>>();
        try
        {
            dto.setT(coreService.getMenuTree());
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("menu error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }
}


