/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.service.drivers;

import com.fruit.drivers.biz.common.DriversStatusEnum;
import com.fruit.drivers.biz.service.DriversAccountService;
import com.fruit.sys.admin.model.drivers.DriversInfoVO;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.drivers.biz.dto.DriversAccountDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-07-26
 * Project        : fruit
 * File Name      : DriversMemberService.java
 */
@Service
public class DriversMemberService extends BaseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DriversMemberService.class);

    @Autowired
    private DriversAccountService driversAccountService;

    public DriversAccountDTO loadDriverInfoByMobile(String mobile)
    {
        DriversAccountDTO driversAccountDTO = this.driversAccountService.loadByMobile(mobile);

        return driversAccountDTO;
    }


    public DriversAccountDTO registerAndLogin(DriversAccountDTO driversAccountDTO){
        String mobile = driversAccountDTO.getMobile();
        Validate.notEmpty(mobile);

        DriversAccountDTO selDriversAccountDTO = loadDriverInfoByMobile(mobile);
        if(selDriversAccountDTO!=null){
            LOGGER.info("老司机注册登录，已有账号，直接登录。");
            Validate.isTrue(selDriversAccountDTO.getStatus() != DriversStatusEnum.FROZEN.getStatus(), "您的账号已被冻结，请联系客服!");
        }else{
            LOGGER.info("老司机注册登录，没有账号，注册账号。");
            selDriversAccountDTO = this.save(driversAccountDTO);
        }

        return selDriversAccountDTO;
    }


    /**
     * 新建司机
     * @param driversAccountDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DriversAccountDTO save(DriversAccountDTO driversAccountDTO)
    {

        int id = this.driversAccountService.create(driversAccountDTO);

        DriversAccountDTO returnDriversAccountDTO = this.driversAccountService.loadById(id);

        return returnDriversAccountDTO;
    }



    public DriversInfoVO getDriverInfoByMobile(String mobile){
        Validate.notEmpty(mobile);

        DriversInfoVO driversAccountVO = null;
        DriversAccountDTO selDriversAccountDTO = loadDriverInfoByMobile(mobile);
        if(selDriversAccountDTO!=null){
           //将查询到信息格式化后，传回前台的
            driversAccountVO = loadDriversInfo(selDriversAccountDTO);
        }

        return driversAccountVO;
    }

    /**
     * 将查询到信息格式化后，传回前台的
     * @param driversAccountDTO DriversInfoVO
     * @return
     */
    private DriversInfoVO loadDriversInfo(DriversAccountDTO driversAccountDTO){
        DriversInfoVO driversInfoVO = null;
        if (driversAccountDTO != null)
        {
            driversInfoVO = new DriversInfoVO();

            BeanUtils.copyProperties(driversAccountDTO, driversInfoVO);

            DriversStatusEnum statusEnum = DriversStatusEnum.get(driversAccountDTO.getStatus());
            if (null != statusEnum)
            {
                driversInfoVO.setStatusDesc(statusEnum.getMessage());
            }

        }
        return driversInfoVO;
    }


    public void updateDriverInfo(DriversAccountDTO driversAccountDTO){
        String mobile = driversAccountDTO.getMobile();
        Validate.notEmpty(mobile);

        DriversAccountDTO selDriversAccountDTO = loadDriverInfoByMobile(mobile);

        Validate.isTrue(selDriversAccountDTO!=null,"该手机号未注册!");

        if(StringUtils.isNotEmpty(driversAccountDTO.getName())){
            LOGGER.info("老司机修改信息-姓名。");
            selDriversAccountDTO.setName(driversAccountDTO.getName());
        }
        if(StringUtils.isNotEmpty(driversAccountDTO.getLicensePlate())){
            LOGGER.info("老司机修改信息-车牌号。");
            selDriversAccountDTO.setLicensePlate(driversAccountDTO.getLicensePlate());
        }

        selDriversAccountDTO.setLastEditor(mobile);

        this.driversAccountService.update(selDriversAccountDTO);
        LOGGER.info("老司机修改信息成功。");
    }


    public void updateMobile(String oldMobile,String newMobile){
        Validate.notEmpty(oldMobile);
        Validate.notEmpty(newMobile);

        DriversAccountDTO oldDriversAccountDTO = loadDriverInfoByMobile(oldMobile);
        Validate.isTrue(oldDriversAccountDTO!=null,"旧手机号账号异常！");
        DriversAccountDTO newDriversAccountDTO = loadDriverInfoByMobile(newMobile);
        Validate.isTrue(newDriversAccountDTO==null,"新手机号已被注册！");

        this.driversAccountService.updateMobile(oldDriversAccountDTO.getId(),newMobile,oldMobile);
        LOGGER.info("老司机修改信息-注册手机号。");
    }


    /**
     * 根据手机号获取老司机ID
     * @param mobile
     * @return
     */
    public int getDriverIdByMobile(String mobile){
        Validate.notEmpty(mobile);

        DriversInfoVO driversAccountVO = null;
        DriversAccountDTO selDriversAccountDTO = loadDriverInfoByMobile(mobile);
        Validate.isTrue(selDriversAccountDTO!=null,"该手机号未注册老司机app，请检查确认后重新填写！");

        int driverId = selDriversAccountDTO.getId();
        return driverId;
    }





}
