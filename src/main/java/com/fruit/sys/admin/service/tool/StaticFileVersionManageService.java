/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.tool;

import com.fruit.base.biz.dto.StaticFileVersionDTO;
import com.fruit.base.biz.service.StaticFileVersionService;
import com.fruit.sys.admin.model.tool.StaticFileVersionModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : 0kuProject1
 * File Name      : StaticFileVersionManageService.java
 */
@Service
public class StaticFileVersionManageService
{
    @Autowired
    private StaticFileVersionService staticFileVersionService;

    public List<StaticFileVersionModel> loadList()
    {
        List<StaticFileVersionDTO> dtoList = this.staticFileVersionService.loadAll();
        return this.transfer(dtoList);
    }

    public StaticFileVersionModel loadById(int id)
    {
        StaticFileVersionDTO dto = this.staticFileVersionService.loadById(id);
        return this.transfer(dto);
    }

    public void save(StaticFileVersionDTO dto)
    {
        int id = dto.getId();
        if (id > 0)
        {
            StaticFileVersionDTO staticFileVersionDTO = this.staticFileVersionService.loadById(id);
            if (staticFileVersionDTO != null) {

                staticFileVersionDTO.setId(dto.getId());
                staticFileVersionDTO.setProject(dto.getProject());
                staticFileVersionDTO.setVersion(dto.getVersion());
                staticFileVersionDTO.setDescription(dto.getDescription());

                this.staticFileVersionService.update(staticFileVersionDTO);
            }
        }
        else
        {
            this.staticFileVersionService.create(dto);
        }
    }

    private List<StaticFileVersionModel> transfer(List<StaticFileVersionDTO> dtoList)
    {
        List<StaticFileVersionModel> modelList = new ArrayList<StaticFileVersionModel>();
        for (StaticFileVersionDTO dto : dtoList)
        {
            modelList.add(this.transfer(dto));
        }
        return modelList;
    }

    private StaticFileVersionModel transfer(StaticFileVersionDTO dto)
    {
        StaticFileVersionModel model = new StaticFileVersionModel();
        try
        {
            BeanUtils.copyProperties(model, dto);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return model;
    }
}
