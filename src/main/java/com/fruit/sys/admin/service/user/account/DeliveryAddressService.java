/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.account;

import com.fruit.account.biz.common.DBIsDefaultStatusEnum;
import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.service.UserDeliveryAddressService;
import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.sys.admin.meta.MetaArea;
import com.fruit.sys.admin.meta.MetaCity;
import com.fruit.sys.admin.meta.MetaCountry;
import com.fruit.sys.admin.meta.MetaProvince;
import com.fruit.sys.admin.meta.MetadataProvider;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.user.account.AddressVo;
import com.fruit.sys.admin.model.user.account.UserDeliveryAddressRequest;
import com.fruit.sys.admin.service.ContextManger;
import com.fruit.sys.admin.service.user.MemberService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description:
 * 买家相关
 * Create Author  : paul
 * Create Date    : 2017-05-18
 * Project        : partal-main-web
 * File Name      : DeliveryAddressService.java
 */
@Service
public class DeliveryAddressService
{
    @Autowired
    private UserDeliveryAddressService userDeliveryAddressService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    private static final Object LOCKER_OBJ = new Object();

    private static final Map<Integer, Object> DB_LOCKER_MAP = new HashMap<Integer, Object>(100);

    private static final int MAX_ADD_NUMBER = 20;//上限

    /**
     * 加载用户所有收货地址
     *
     * @param userId
     * @return
     */
    public List<UserDeliveryAddressDTO> loadAllAddress(int userId)
    {
        List<UserDeliveryAddressDTO> addressDtos = userDeliveryAddressService.listByUserId(userId);
        if (null == addressDtos)
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            Collections.sort(addressDtos, new Comparator<UserDeliveryAddressDTO>()
            {
                @Override
                public int compare(UserDeliveryAddressDTO o1, UserDeliveryAddressDTO o2)
                {
                    int selected = o2.getSelected() - o1.getSelected();
                    return selected == DBIsDefaultStatusEnum.ISNOTDEFAULT.getValue() ? o1.getId() - o2.getId() : selected; // 默认地址排第一，其他地址按添加时间顺序排列(因为id是自增的)
                }
            });
        }
        return addressDtos;
    }

    /**
     * 加载指定的收货地址
     *
     * @param id
     * @return
     */
    public UserDeliveryAddressDTO loadAddressById(int id)
    {
        return userDeliveryAddressService.loadById(id);
    }

    /**
     * 加载用户的默认收货地址(如果用户没有设置默认收货地址，则返回用户的第一个收货地址，若无收货地址则返回null)
     *
     * @param userId
     * @return
     */
    public UserDeliveryAddressDTO loadDefaultAddress(int userId)
    {
        UserDeliveryAddressDTO result = userDeliveryAddressService.loadDefaultAddress(userId);
        if (null == result)
        {
            List<UserDeliveryAddressDTO> addresses = loadAllAddress(userId);
            if (CollectionUtils.isNotEmpty(addresses))
            {
                result = addresses.get(0);
            }
        }
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(UserModel userModel, UserDeliveryAddressDTO address)
    {
        if (null != address && address.getId() > 0)
        {
            if (address.getSelected() == DBIsDefaultStatusEnum.ISDEFAULT.getValue())
            {
                cleanDefaultAddress(address.getUserId());
            }
            userDeliveryAddressService.update(address);
            this.memberService.asyncSaveUpdateLog(userModel, "Update delivery address", UserInfoUpdateLogTypeEnum.DELIVERY_ADDRESS_UPDATE.getType(), null,ContextManger.getContext().getUserIp());
        }
    }


    /**
     * 通过userId查询此客户已经存在的收货地址数量，与设定的上限数进行对比，如果超过上限，则不允许新增
     * @param userId
     * @return
     */
    private boolean canAddByUpperLimit(int userId){
        int count = userDeliveryAddressService.countByUserId(userId);
        if(count>=MAX_ADD_NUMBER){
            return false;
        }else{
            return true;
        }
    }


    /**
     * 将指定用户的默认地址(如果有)设置为非默认地址
     *
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private void cleanDefaultAddress(int userId)
    {
        UserDeliveryAddressDTO deliveryAddressDTO = userDeliveryAddressService.loadDefaultAddress(userId);
        if (null != deliveryAddressDTO)
        {
            int stlected = DBIsDefaultStatusEnum.ISNOTDEFAULT.getValue(); // 0表示未选中
            deliveryAddressDTO.setSelected(stlected);
            userDeliveryAddressService.update(deliveryAddressDTO);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(UserModel userModel,int userId, int id)
    {
        Object dbLocker = null;
        synchronized (LOCKER_OBJ)
        {
            dbLocker = DB_LOCKER_MAP.get(userId);
            if (null == dbLocker)
            {
                if (DB_LOCKER_MAP.size() > 75)
                {
                    DB_LOCKER_MAP.clear();
                }
                dbLocker = new Object();
                DB_LOCKER_MAP.put(userId, dbLocker);
            }
        }
        synchronized (dbLocker)
        {
            cleanDefaultAddress(userId);
            UserDeliveryAddressDTO deliveryAddressDTO = userDeliveryAddressService.loadById(id);
            if (null != deliveryAddressDTO)
            {
                deliveryAddressDTO.setSelected(DBIsDefaultStatusEnum.ISDEFAULT.getValue());
                userDeliveryAddressService.update(deliveryAddressDTO);
                this.memberService.asyncSaveUpdateLog(userModel, "Set default address", UserInfoUpdateLogTypeEnum.DELIVERY_ADDRESS_UPDATE.getType(), null ,ContextManger.getContext().getUserIp());
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(UserModel userModel,int id)
    {
        userDeliveryAddressService.delete(id);
        this.memberService.asyncSaveUpdateLog(userModel, "Delete delivery address", UserInfoUpdateLogTypeEnum.DELIVERY_ADDRESS_UPDATE.getType(),null,ContextManger.getContext().getUserIp());
    }

    public AddressVo wrapVO(UserDeliveryAddressDTO addressDTO)
    {
        AddressVo addressVo = new AddressVo();

        BeanUtils.copyProperties(addressDTO, addressVo);//将addressDTOcopy到addressVo

        //设置国家、省市区
        MetaCountry country = metadataProvider.getCountry(addressDTO.getCountryId());
        MetaProvince province = metadataProvider.getProvince(addressDTO.getProvinceId());
        MetaCity city = metadataProvider.getCity(addressDTO.getCityId());
        MetaArea area = metadataProvider.getArea(addressDTO.getDistrictId());
        addressVo.setCountryName(country.getName());
        addressVo.setProvinceName(province.getName());
        addressVo.setCityName(city.getName());
        addressVo.setDistrictName(area.getName());

        return addressVo;
    }

    public List<AddressVo> wrapVOs(List<UserDeliveryAddressDTO> addressDTOs)
    {
        List<AddressVo> result = null;
        if (CollectionUtils.isNotEmpty(addressDTOs))
        {
            result = new ArrayList<AddressVo>(addressDTOs.size());
            for (UserDeliveryAddressDTO addressDTO : addressDTOs)
            {
                AddressVo addressVo = this.wrapVO(addressDTO);
                result.add(addressVo);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDeliveryAddressDTO addAddress(int sysId,UserModel userModel,UserDeliveryAddressRequest userDeliveryAddressRequest)
    {

        //查询该客户已新增的收货地址数量，已达最高数量不允许新增
        Validate.isTrue(canAddByUpperLimit(userModel.getUserId()),"您已有收货地址已达上限："+MAX_ADD_NUMBER+"个！");

        UserDeliveryAddressDTO addressDTO = new UserDeliveryAddressDTO();

        BeanUtils.copyProperties(userDeliveryAddressRequest, addressDTO);//将传入的参数copy到addressDTO

        if (DBIsDefaultStatusEnum.ISDEFAULT.getValue() == addressDTO.getSelected())
        {
            cleanDefaultAddress(addressDTO.getUserId());
        }
        userDeliveryAddressService.create(addressDTO);
        this.memberService.asyncSaveUpdateLog(userModel, "Add delivery address", UserInfoUpdateLogTypeEnum.DELIVERY_ADDRESS_UPDATE.getType(),"sys:"+sysId, ContextManger.getContext().getUserIp());
        return addressDTO;
    }
}
