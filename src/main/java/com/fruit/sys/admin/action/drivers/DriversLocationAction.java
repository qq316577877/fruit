/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.action.drivers;

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


@Component
@UriMapping("/drivers")
public class DriversLocationAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DriversLocationAction.class);

    @Autowired
    private DriversLocationOpeService driversLocationOpeService;

    @UriMapping(value = "/add_driver_coordinate_location" , interceptors = "validationInterceptor")
    public AjaxResult addDriverLocationAjax()
    {
        String mobile = StringUtils.trimToEmpty(super.getStringParameter("mobile"));
        String latitudeCoordinates = StringUtils.trimToEmpty(super.getStringParameter("latitudeCoordinates"));//纬度
        String longitudeCoordinate = StringUtils.trimToEmpty(super.getStringParameter("longitudeCoordinate"));//经度
        try
        {
            Validate.isTrue(StringUtils.isNotBlank(mobile), "手机号不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(latitudeCoordinates), "纬度坐标不能为空!");
            Validate.isTrue(StringUtils.isNotBlank(longitudeCoordinate), "经度坐标不能为空!");

            int addId = driversLocationOpeService.addDriversLocation(mobile,latitudeCoordinates,longitudeCoordinate);

//            Validate.isTrue(addId>0,"司机坐标位置更新异常");
            return new AjaxResult();
//            return new AjaxResult(200, SUCCESS);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(mobile+"新增定位失败:" + e.getMessage());
            return new AjaxResult(AjaxResultCode.SERVER_ERROR.getCode(),e.getMessage());
        }
    }


}


