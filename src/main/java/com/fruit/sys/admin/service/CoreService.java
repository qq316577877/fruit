/*
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : beauty-admin-web
 * File Name      : AdminCoreService.java
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service;

import com.fruit.sys.admin.model.RoleMenuDto;
import com.fruit.sys.admin.tree.Tree;
import com.fruit.sys.admin.tree.TreeCreateParam;
import com.fruit.sys.biz.common.AdminMenuType;
import com.fruit.sys.biz.dto.AdminLogDTO;
import com.fruit.sys.biz.dto.AdminMenuDTO;
import com.fruit.sys.biz.service.AdminLogService;
import com.fruit.sys.biz.service.AdminMenuService;
import com.fruit.sys.biz.service.AdminRoleMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能描述:  <p>
 *
 * @author : terry <p>
 * @version 1.0 2015-06-12
 * @since beauty-admin-web 1.0
 */
@Service
public class CoreService
{
    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private AdminRoleMenuService adminRoleMenuService;

    @Autowired
    private AdminLogService adminLogService;

    public Tree<AdminMenuDTO> getMenuTree()
    {
        List<AdminMenuDTO> list = adminMenuService.loadAll();
        Tree<AdminMenuDTO> tree = Tree.createTree(list, getTreeCreateParam());
        return tree;
    }

    public Tree<AdminMenuDTO> getMenuTree(int dperId)
    {
        List<AdminMenuDTO> list = adminMenuService.loadBySysId(dperId);
        Tree<AdminMenuDTO> tree = Tree.createTree(list, getTreeCreateParam());
        return tree;
    }

    public String adminMenus()
    {
        List<AdminMenuDTO> list = adminMenuService.loadAll();
        return menus2String(list);
    }

    public String userMenus(int dperId)
    {
        List<AdminMenuDTO> list = adminMenuService.loadBySysId(dperId);
        return menus2String(list);
    }

    public Set<String> userNamespace(int dperId)
    {
        List<AdminMenuDTO> list = adminMenuService.loadBySysId(dperId);
        Set<String> namespaceSet = new HashSet<String>(list.size());
        for (AdminMenuDTO dto : list)
        {
            String namespaceArray = dto.getNamespace();
            if (StringUtils.isNotBlank(namespaceArray))
            {
                for (String namespace : namespaceArray.split(","))
                {
                    namespaceSet.add(namespace.trim());
                }
            }
        }
        return namespaceSet;
    }

    private String menus2String(List<AdminMenuDTO> list)
    {
        if (CollectionUtils.isEmpty(list))
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (AdminMenuDTO menu : list)
        {
            if (StringUtils.isNotBlank(menu.getUrl()))
            {
                if (menu.getType().equals(AdminMenuType.remote.getVal()))
                {
                    if (menu.getUrl().indexOf("dp/") != -1)
                    {
                        sb.append(menu.getUrl().split("dp/")[1] + ";");
                    }
                    else
                    {
                        sb.append(menu.getUrl() + ";");
                    }
                }
                else
                {
                    sb.append(menu.getUrl() + ";");
                }
            }
        }

        return sb.toString();
    }

    public Tree<RoleMenuDto> getRoleMenu(int roleId)
    {
        List<AdminMenuDTO> list = adminMenuService.loadAll();
        List<Integer> ids = adminRoleMenuService.loadMenuIdsByRoleId(roleId);

        List<RoleMenuDto> res = new ArrayList<RoleMenuDto>(list.size());

        for (AdminMenuDTO menu : list)
        {
            RoleMenuDto dto = new RoleMenuDto();
            dto.setId(menu.getId());
            dto.setName(menu.getName());
            dto.setPId(menu.getPId());
            dto.setChecked("false");
            if (ids.contains(menu.getId()))
            {
                dto.setChecked("true");
            }

            res.add(dto);
        }

        TreeCreateParam param = getTreeCreateParam();
        param.setFieldName_checked("checked");

        Tree<RoleMenuDto> tree = Tree.createTree(res, param);
        return tree;
    }

    private TreeCreateParam getTreeCreateParam()
    {
        TreeCreateParam param = new TreeCreateParam();
        param.setFieldName_id("id");
        param.setFieldName_value("id");
        param.setFieldName_name("name");
        param.setFieldName_pid("PId");
        param.setRoot_id("0");
        param.setRoot_name("ROOT");
        param.setRoot_value("0");
        param.setSeparator("/");
        return param;
    }

    public void log(String ip, int dper, String content)
    {
        AdminLogDTO log = new AdminLogDTO();
        log.setIP(ip);
        log.setSysId(dper);
        log.setContent(content);
        adminLogService.create(log);
    }

}
