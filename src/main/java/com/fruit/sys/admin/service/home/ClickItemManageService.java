/*
 *
 * Copyright (c) 2017-2022 by wuhan Information Co., Ltd.
 * All rights reserved.
 *
 */


package com.fruit.sys.admin.service.home;

import com.fruit.portal.biz.common.ClickItemTypeEnum;
import com.fruit.portal.biz.dto.ClickItemDTO;
import com.fruit.portal.biz.service.ClickItemService;
import com.fruit.sys.admin.model.IdValueVO;
import com.fruit.sys.admin.model.home.ClickItemModel;
import com.fruit.sys.admin.service.common.FileUploadService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClickItemManageService
{
    public static final List<IdValueVO> POSITIONS = new ArrayList<IdValueVO>();

    static
    {
        ClickItemTypeEnum[] values = ClickItemTypeEnum.values();
        if (ArrayUtils.isNotEmpty(values))
        {
            for (ClickItemTypeEnum type : values)
            {
                POSITIONS.add(new IdValueVO(type.getType(), type.getMessage()));
            }
        }
    }


    @Autowired
    private ClickItemService clickItemService;

    @Autowired
    private FileUploadService fileUploadService;

    public List<ClickItemModel> loadList(int type,int size)
    {
        List<ClickItemDTO> models = null;
        if (type>0)
        {
            models = this.clickItemService.listByType(type, size);
        }
        else
        {
            models = this.clickItemService.listAll();
        }
        return this.transferClickItems(models);
    }

    private List<ClickItemModel> transferClickItems(List<ClickItemDTO> models)
    {
        List<ClickItemModel> results = new ArrayList<ClickItemModel>();
        if (CollectionUtils.isNotEmpty(models))
        {
            for (ClickItemDTO model : models)
            {
                ClickItemModel result = transfer(model);
                results.add(result);
            }
        }
        return results;
    }

    public ClickItemModel transfer(ClickItemDTO dto)
    {
        ClickItemModel clickItemModel = null;
        try
        {
            if(dto!=null && dto.getId()>0){
                clickItemModel = new ClickItemModel();
                BeanUtils.copyProperties(clickItemModel, dto);
                clickItemModel.setImgUrl(fileUploadService.buildDiskUrl(dto.getImgUrl()));
                clickItemModel.setImgUrlPath(dto.getImgUrl());
                clickItemModel.setTypeDesc(ClickItemTypeEnum.get(clickItemModel.getType()).getMessage());
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return clickItemModel;
    }

    public ClickItemDTO loadById(int id)
    {
        ClickItemDTO clickItemDTO = this.clickItemService.loadById(id);
        return clickItemDTO;
    }

    public void save(ClickItemDTO itemDTO)
    {
        int id = itemDTO.getId();
        if(id > 0)
        {
            this.clickItemService.update(itemDTO);
        }
        else
        {
            this.clickItemService.create(itemDTO);
        }


    }
}
