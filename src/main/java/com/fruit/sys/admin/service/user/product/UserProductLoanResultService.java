/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.product;


import com.fruit.account.biz.common.UserInfoUpdateLogTypeEnum;
import com.fruit.account.biz.dto.UserProductLoanInfoDTO;
import com.fruit.account.biz.service.sys.SysUserProductLoanService;
import com.fruit.loan.biz.service.sys.SysLoanInfoService;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.service.ProductService;
import com.fruit.sys.admin.service.BaseService;
import com.fruit.sys.admin.service.CacheManageService;
import com.fruit.sys.admin.service.user.MemberService;
import com.fruit.sys.admin.service.user.UserListService;
import com.ovfintech.arch.common.event.EventChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserListService.java
 */
@Service
public class UserProductLoanResultService extends BaseService
{

    @Autowired
    private SysUserProductLoanService sysUserProductLoanService;

    @Autowired
    private CacheManageService cacheManageService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserListService userListService;

    @Autowired
    private ProductService productService;

    @Autowired
    @Qualifier("taskTriggerChannel")
    private EventChannel taskEventChannel;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    public void updateProductLoanInfo(int id , int userId, BigDecimal productLoan, String userIp) {

        UserProductLoanInfoDTO userProductInfoDTO = new UserProductLoanInfoDTO();

        userProductInfoDTO.setId(id);
        userProductInfoDTO.setProductLoan(productLoan);
        userProductInfoDTO.setUpdateTime(new Date());
        sysUserProductLoanService.update(userProductInfoDTO);

        cacheManageService.syncUserModel(userId);
        this.memberService.asyncSaveUpdateLog(userListService.loadUserModel(userId, true), "Sys admin verify user enterprise auth.", UserInfoUpdateLogTypeEnum.PROFILE_CONFIG.getType(), null, userIp);

    }

    public void addProductLoanInfo(UserProductLoanInfoDTO userProductLoanInfoDTO) {
        sysUserProductLoanService.insertProductLoanInfo(userProductLoanInfoDTO);
    }


    public List<ProductDTO> loadProductInfo(int userId){
        List<ProductDTO> dtos = productService.listAll();   //获取全部的产品信息
        List<UserProductLoanInfoDTO> reDtos = sysUserProductLoanService.loadProductInfoList(userId,1,100);

        Iterator<ProductDTO> iterator = dtos.iterator();
        while(iterator.hasNext()){
            ProductDTO productDTO = iterator.next();
            for(UserProductLoanInfoDTO userProductLoanInfoDTO : reDtos){
                if(productDTO.getId() == userProductLoanInfoDTO.getProductId()){
                    iterator.remove();
                }
            }
        }

        return dtos;
    }


}