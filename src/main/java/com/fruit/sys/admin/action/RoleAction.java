package com.fruit.sys.admin.action;

import com.fruit.sys.admin.model.JsonResult;
import com.fruit.sys.admin.model.RoleMenuDto;
import com.fruit.sys.admin.model.UserInfo;
import com.fruit.sys.admin.service.CoreService;
import com.fruit.sys.admin.service.admin.AdminService;
import com.fruit.sys.admin.tree.Tree;
import com.fruit.sys.admin.utils.CollectionTools;
import com.fruit.sys.biz.common.Page;
import com.fruit.sys.biz.dto.AdminRoleDTO;
import com.fruit.sys.biz.dto.AdminRoleUserDTO;
import com.fruit.sys.biz.service.AdminRoleMenuService;
import com.fruit.sys.biz.service.AdminRoleService;
import com.fruit.sys.biz.service.AdminRoleUserService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Component
@UriMapping("/role")
public class RoleAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleAction.class);

    private static final String CLAZZ_TYPE = RoleAction.class.getSimpleName();

    private static final int DEF_PAGE_SIZE = 7;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private CoreService coreService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRoleMenuService adminRoleMenuService;

    @Autowired
    private AdminRoleUserService adminRoleDperService;

    @UriMapping(value = "/list")
    public String list()
    {
        String name = this.getStringParameter("name");
        int pageNo = this.getIntParameter("pageNo", 1);
        int pageSize = this.getIntParameter("pageSize", DEF_PAGE_SIZE);

        this.setAttribute("pageDto", adminRoleService.queryByPage(StringUtils.trimToEmpty(name), pageNo, pageSize));
        this.setAttribute("params", this.fixParams());

        return "/role/role_list";
    }

    @ResponseBody
    @UriMapping(value = "/delete")
    public JsonResult delete()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            String ids = getRequiredStringParamter("ids");
            List<Integer> idList = CollectionTools.createIntList(ids);

            int count = adminRoleService.delete(idList);
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

    @UriMapping(value = "/detail")
    public String detail()
    {
        int id = this.getIntParameter("id");
        if (id > 0)
        {
            this.setAttribute("id", id);
        }
        return "/role/role_detail";
    }

    @UriMapping(value = "/info")
    public String info()
    {
        AdminRoleDTO roleDTO = null;
        int id = this.getIntParameter("id");
        if(id > 0)
        {
            roleDTO = adminRoleService.loadById(id);
        }
        //查询子角色 20170811
        List<AdminRoleDTO> roleDTOList = this.adminService.loadByParentId();

        this.setAttribute("role_list", roleDTOList);
        this.setAttribute("data", roleDTO);
        return "/role/role_info";
    }

    @ResponseBody
    @UriMapping(value = "/save")
    public JsonResult save()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            AdminRoleDTO obj = new AdminRoleDTO();

            int id_ = this.getIntParameter("id");
            obj.setName(getStringParameter("name"));
            obj.setStatus(getByteParameter("status"));

            int id = 0;
            if (id_ > 0)
            {
                obj.setId(id_);
                id = adminRoleService.update(obj);
            }else{
                id =  adminRoleService.create(obj);
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

    /**
     * Menu      *
     */

    @UriMapping(value = "/menu")
    public String menu()
    {
        this.setAttribute("roleId", this.getIntParameter("roleId"));
        return "/role/role_menu";
    }

    @ResponseBody
    @UriMapping(value = "/menuTree")
    public JsonResult<Tree<RoleMenuDto>> menuTree()
    {
        JsonResult<Tree<RoleMenuDto>> dto = new JsonResult<Tree<RoleMenuDto>>();
        try
        {
            int roleId = getRequiredIntParamter("roleId");
            dto.setT(coreService.getRoleMenu(roleId));
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("menuTree error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }

        return dto;
    }

    @ResponseBody
    @UriMapping(value = "/saveMenu")
    public JsonResult<String> saveMenu()
    {
        JsonResult<String> dto = new JsonResult<String>();
        try
        {
            int roleId = getRequiredIntParamter("roleId");
            String[] menuIds = getArrayParameter("menuIds[]");

            List<Integer> menuList = new ArrayList<Integer>();
            if (menuIds != null)
            {
                for (String s : menuIds)
                {
                    menuList.add(NumberUtils.toInt(s));
                }
            }

            adminRoleMenuService.updateAdminRoleMenu(menuList, roleId);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("saveMenu error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }

    /**
     * Dper      *
     */
    @UriMapping(value = "/dper")
    public String dper()
    {
        int roleId = getRequiredIntParamter("roleId");
        int name = this.getIntParameter("name");

        int pageNo = this.getIntParameter("pageNo", 1);
        int pageSize = this.getIntParameter("pageSize", DEF_PAGE_SIZE);

        Page<AdminRoleUserDTO> page = adminRoleDperService.queryByPage(roleId, name, pageNo, pageSize);

        List<UserInfo> userInfoList = this.adminService.loadAll();

        this.setAttribute("pageDto", page);
        this.setAttribute("user_info_list", userInfoList);
        this.setAttribute("params", this.fixParams());

        return "/role/role_dper";
    }

    @ResponseBody
    @UriMapping(value = "/saveDper", interceptors = "logInterceptor")
    public JsonResult saveDper()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            String username = super.getStringParameter("name");
            UserInfo userInfo = this.adminService.loadByUserName(username);
            if(userInfo == null)
            {
                throw new RuntimeException("用户不存在");
            }

            AdminRoleUserDTO obj = new AdminRoleUserDTO();
            obj.setRoleId(getIntParameter("roleId"));
            obj.setUserName(username);
            obj.setSysId(userInfo != null ? userInfo.getSysId() : 0);

            adminRoleDperService.create(obj);
            dto.setResult(true);
            dto.setMsg("成功!");
        }
        catch (Exception e)
        {
            LOGGER.error("saveDper error", e);
            dto.setResult(false);
            dto.setMsg("失败:" + e.getMessage());
        }
        return dto;
    }

    @ResponseBody
    @UriMapping(value = "/deleteDper")
    public JsonResult deleteDper()
    {
        JsonResult<Integer> dto = new JsonResult<Integer>();
        try
        {
            String ids = getRequiredStringParamter("ids");
            List<Integer> idList = CollectionTools.createIntList(ids);

            int count = adminRoleDperService.delete(idList);
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


