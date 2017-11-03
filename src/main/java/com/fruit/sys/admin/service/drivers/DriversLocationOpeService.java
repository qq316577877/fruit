/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.drivers;


import com.fruit.drivers.biz.common.DriversStatusEnum;
import com.fruit.drivers.biz.dto.DriversAccountDTO;
import com.fruit.drivers.biz.dto.DriversLocationDTO;
import com.fruit.drivers.biz.service.DriversAccountService;
import com.fruit.drivers.biz.service.DriversLocationService;
import com.fruit.order.biz.dto.ContainerDTO;
import com.fruit.order.biz.dto.LogisticsDetailDTO;
import com.fruit.order.biz.service.ContainerService;
import com.fruit.order.biz.service.LogisticsDetailService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.utils.DateUtil;
import com.fruit.sys.admin.utils.MathUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-07-26
 * Project        : fruit
 * File Name      : DriversLocationService.java
 */
@Service
public class DriversLocationOpeService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DriversLocationOpeService.class);

    private static final int ADD_CHECK_MINUTES = 10;//检查10分钟以内该司机插入条数

    private static final int ADD_TIMES_IN_TIMES = 3;//10分钟内只允许插入3条

    private static final int KEEP_DECIMAL_PLACE = 6;//坐标保留的位数

    @Autowired
    private DriversAccountService driversAccountService;

    @Autowired
    private DriversLocationService driversLocationService;

    @Autowired
    private LogisticsDetailService logisticsDetailService;

    @Autowired
    private ContainerService containerService;


    /**
     * 添加司机的坐标
     * @param mobile
     * @param latitudeCoordinates 纬度
     * @param longitudeCoordinate 经度
     * @return
     */
    public int addDriversLocation(String mobile,String latitudeCoordinates,String longitudeCoordinate){
        Validate.notEmpty(mobile);
        Validate.notEmpty(latitudeCoordinates);
        Validate.notEmpty(longitudeCoordinate);
        int addId = 0;

        //先查询此手机号是否注册
        DriversAccountDTO selDriversAccountDTO = this.driversAccountService.loadByMobile(mobile);
        Validate.isTrue(selDriversAccountDTO != null, "没有此司机账号!");
        Validate.isTrue(selDriversAccountDTO.getStatus() != DriversStatusEnum.FROZEN.getStatus(), "此司机的账号已被冻结！");

        //再查看此手机号规定时间内插入了多少条数据
        boolean canAdd = ifDriverCanAddLocation(selDriversAccountDTO);
        if(canAdd){//插入司机坐标
            DriversLocationDTO driversLocationDTO = new DriversLocationDTO();
            BigDecimal latitude = MathUtil.stringToBigDecima(latitudeCoordinates,KEEP_DECIMAL_PLACE);
            BigDecimal longitude = MathUtil.stringToBigDecima(longitudeCoordinate,KEEP_DECIMAL_PLACE);
            driversLocationDTO.setDriverId(selDriversAccountDTO.getId());
            driversLocationDTO.setLatitudeCoordinates(latitude);
            driversLocationDTO.setLongitudeCoordinate(longitude);
            addId = save(driversLocationDTO);
            LOGGER.info(mobile+"新增司机坐标：纬度" + latitudeCoordinates+"，经度"+longitudeCoordinate);
        }

        return addId;
    }


    /**
     * 新建司机坐标
     * @param driversLocationDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int save(DriversLocationDTO driversLocationDTO)
    {

        int id = this.driversLocationService.create(driversLocationDTO);
        return id;
    }


    /**
     * 确认该driversId是否有插入权限
     * @return
     */
    private boolean ifDriverCanAddLocation(DriversAccountDTO driversAccountDTO){
        //1.查询
        Date currrentDate = new Date();
        Date checkTime = DateUtil.addMinutes(currrentDate,-ADD_CHECK_MINUTES);
        List<DriversLocationDTO> driversLocationDTOs = driversLocationService.loadByDriverIdAndTime(driversAccountDTO.getId(),checkTime,currrentDate);
        if (CollectionUtils.isNotEmpty(driversLocationDTOs))
        {
            if(driversLocationDTOs.size()>ADD_TIMES_IN_TIMES){
                LOGGER.warn("手机号："+driversAccountDTO.getMobile()+",该司机位置更新频率超过系统上限，异常！");
                return false;
            }
        }
        return true;
    }


    /**
     * 根据货柜号查询 货柜的物流信息（老司机坐标号）
     * @param containerNo
     * @return
     */
    public List<DriversLocationDTO> getLogisticsLocationByContainerNo(String containerNo){
        List<DriversLocationDTO> driversLocationDTOs = null;

        List<LogisticsDetailDTO> logisticsDetais = logisticsDetailService.loadByContainerNo(containerNo);

        List<Integer> driverIds = null;
        if(CollectionUtils.isNotEmpty(logisticsDetais)){
            driverIds = new ArrayList<Integer>(logisticsDetais.size());
            for (LogisticsDetailDTO logisticsDetailDTO : logisticsDetais) {
                if(logisticsDetailDTO.getDriverId()>0){
                    driverIds.add(logisticsDetailDTO.getDriverId());
                }
            }
        }

        if(CollectionUtils.isNotEmpty(driverIds)){
            ContainerDTO containerDTO = containerService.loadByContainerId(containerNo);
            Validate.isTrue(containerDTO != null, "没有此货柜信息!");
            Date beginTime = containerDTO.getClearanceTime();//清关时间
            Date endTime = containerDTO.getPreReceiveTime();//预计到货时间 如果没有，则当前时间
            //根据driverIds查询最近N次的物流信息
            driversLocationDTOs = driversLocationService.loadByDriverIdsAndTime(driverIds,beginTime,endTime);
        }

        return driversLocationDTOs;
    }







}
