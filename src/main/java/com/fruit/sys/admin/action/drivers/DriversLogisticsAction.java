/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action.drivers;

import com.fruit.drivers.biz.dto.DriversLocationDTO;
import com.fruit.drivers.biz.service.DriversAccountService;
import com.fruit.sys.admin.action.BaseAction;
import com.fruit.sys.admin.model.portal.AjaxResult;
import com.fruit.sys.admin.model.portal.AjaxResultCode;
import com.fruit.sys.admin.service.drivers.DriversLocationOpeService;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@UriMapping("/drivers")
public class DriversLogisticsAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DriversLogisticsAction.class);

    @Autowired
    private DriversAccountService driversAccountService;

    @Autowired
    private DriversLocationOpeService driversLocationOpeService;


    /**
     * 根据货柜编号查询货柜物流信息----老司机定位
     * @return
     */
    @UriMapping(value = "/get_driver_location_by_container_ajax" , interceptors = "validationInterceptor")
    public AjaxResult getDriverInfomationAjax()
    {
        String containerNo = StringUtils.trimToEmpty(super.getStringParameter("containerNo"));
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(containerNo), "货柜号不能为空!");

            List<DriversLocationDTO> driversLocationDTOs = driversLocationOpeService.getLogisticsLocationByContainerNo(containerNo);
            Validate.isTrue(driversLocationDTOs!=null,"该货柜暂无老司机定位点！");

            int code = AjaxResultCode.OK.getCode();
            String msg = SUCCESS;
            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(driversLocationDTOs);
            return ajaxResult;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("/drivers/get_driver_location_by_container_ajax 查询货柜老司机物流信息失败:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }



}


