/*
 *
 * Copyright (c) 2015-2018 by Shanghai 0Ku Information Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.supplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.account.biz.service.UserSupplierService;
import com.fruit.sys.admin.meta.MetaArea;
import com.fruit.sys.admin.meta.MetaCity;
import com.fruit.sys.admin.meta.MetaCountry;
import com.fruit.sys.admin.meta.MetaProvince;
import com.fruit.sys.admin.meta.MetadataProvider;
import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.order.SupplierVo;
import com.fruit.sys.admin.model.order.UserSupplierRequest;
import com.fruit.sys.admin.service.ContextManger;
import com.fruit.sys.admin.service.user.MemberService;

/**
 * Description: 用户供应商相关 Create Author : paul Create Date : 2017-05-18 Project :
 * partal-main-web File Name : SupplierService.java
 */
@Service
public class SupplierService {
	@Autowired
	private UserSupplierService userSupplierService;

	@Autowired
	private MetadataProvider metadataProvider;

	@Autowired
	private MemberService memberService;

	private static final Object LOCKER_OBJ = new Object();

	private static final Map<Integer, Object> DB_LOCKER_MAP = new HashMap<Integer, Object>(100);

	/**
	 * 加载用户所有收货供应商
	 *
	 * @param userId
	 * @return
	 */
	public List<UserSupplierDTO> loadAllSupplier(int userId) {
		List<UserSupplierDTO> supplierDtos = userSupplierService.listByUserId(userId);
		if (null == supplierDtos) {
			return Collections.EMPTY_LIST;
		} else {
			return supplierDtos;
		}
	}

	/**
	 * 加载指定的收货供应商
	 *
	 * @param id
	 * @return
	 */
	public UserSupplierDTO loadSupplierById(int id) {
		return userSupplierService.loadById(id);
	}

	public void updateSupplier(UserModel userModel, UserSupplierDTO supplier) {
		if (null != supplier && supplier.getId() > 0) {
			userSupplierService.update(supplier);
			this.memberService.asyncSaveUpdateLog(userModel, "Update supplier",
					UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), null, ContextManger.getContext().getUserIp());
		}
	}

	public UserSupplierDTO addSupplier(UserModel userModel, UserSupplierRequest userSupplierRequest) {
		UserSupplierDTO supplierDTO = new UserSupplierDTO();

		BeanUtils.copyProperties(userSupplierRequest, supplierDTO);// 将传入的参数copy到supplierDTO

		userSupplierService.create(supplierDTO);
		this.memberService.asyncSaveUpdateLog(userModel, "Add supplier",
				UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), null, ContextManger.getContext().getUserIp());
		return supplierDTO;
	}

	public void deleteSupplier(UserModel userModel, int id) {
		userSupplierService.delete(id);
		this.memberService.asyncSaveUpdateLog(userModel, "Delete supplier",
				UserInfoUpdateLogTypeEnum.SUPPLIER_UPDATE.getType(), null, ContextManger.getContext().getUserIp());
	}

	public SupplierVo wrapVO(UserSupplierDTO supplierDTO) {
		SupplierVo supplierVo = new SupplierVo();

		BeanUtils.copyProperties(supplierDTO, supplierVo);// 将supplierDTOcopy到supplierVo

		// 设置国省市区
		MetaCountry country = metadataProvider.getCountry(supplierDTO.getCountryId());
		MetaProvince province = metadataProvider.getProvince(supplierDTO.getProvinceId());
		MetaCity city = metadataProvider.getCity(supplierDTO.getCityId());
		MetaArea area = metadataProvider.getArea(supplierDTO.getDistrictId());
		supplierVo.setCountryName(country.getName());
		supplierVo.setProvinceName(province.getName());
		supplierVo.setCityName(city.getName());
		supplierVo.setDistrictName(area.getName());

		return supplierVo;
	}

	public List<SupplierVo> wrapVOs(List<UserSupplierDTO> supplierDTOs) {
		List<SupplierVo> result = null;
		if (CollectionUtils.isNotEmpty(supplierDTOs)) {
			result = new ArrayList<SupplierVo>(supplierDTOs.size());
			for (UserSupplierDTO supplierDTO : supplierDTOs) {
				SupplierVo supplierVo = this.wrapVO(supplierDTO);
				result.add(supplierVo);
			}
		}
		return result;
	}
}
