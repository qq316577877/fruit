/*
 *
 * Copyright (c) 2017-2020 by wuhan HanTao Information Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.sys.admin.service.user.product;


import com.fruit.account.biz.dto.UserProductLoanInfoDTO;
import com.fruit.account.biz.service.sys.SysUserProductLoanService;
import com.fruit.order.biz.dto.ProductDTO;
import com.fruit.order.biz.service.ProductService;
import com.fruit.sys.admin.model.user.product.UserProductLoanModel;
import com.fruit.sys.admin.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description:
 * <p/>
 * Create Author  : paul
 * Create Date    : 2017-05-24
 * Project        : fruit
 * File Name      : UserListService.java
 */
@Service
public class UserProductInfoService extends BaseService
{

    @Autowired
    private SysUserProductLoanService sysUserProductService;

    @Autowired
    private ProductService productService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public List<UserProductLoanModel> loadProductList(int userId, int pageNo, int pageSize)
    {

        List<UserProductLoanModel> result = Collections.emptyList();

        List<UserProductLoanInfoDTO> userProductInfoDTOs = sysUserProductService.loadProductInfoList(userId, pageNo, pageSize);
        if (CollectionUtils.isNotEmpty(userProductInfoDTOs))
        {
            List<UserProductLoanModel> userProductModelList = this.getUserProductModelLists(userProductInfoDTOs);
            result = userProductModelList;
        }

        return result;
    }



    private List<UserProductLoanModel> getUserProductModelLists(List<UserProductLoanInfoDTO> userProductDTOs)
    {
        List<FutureTask<UserProductLoanModel>> tasks = new ArrayList<FutureTask<UserProductLoanModel>>(userProductDTOs.size());

        List<UserProductLoanModel> userProductModelList = new ArrayList<UserProductLoanModel>(userProductDTOs.size());
        if (CollectionUtils.isNotEmpty(userProductDTOs))
        {
            for (UserProductLoanInfoDTO userProduct : userProductDTOs)
            {
                FutureTask<UserProductLoanModel> futureTask = new FutureTask(new UserProductModelCallable(userProduct));
                executorService.submit(futureTask);
                tasks.add(futureTask);
            }
            for (FutureTask<UserProductLoanModel> task : tasks)
            {
                try
                {
                    UserProductLoanModel userProductModel = task.get();
                    userProductModelList.add(userProductModel);
                }
                catch (InterruptedException e)
                {
                    //
                }
                catch (ExecutionException e)
                {
                    //
                }
            }
        }
        return userProductModelList;
    }


    class UserProductModelCallable implements Callable<UserProductLoanModel>
    {
        private UserProductLoanInfoDTO userProduct;

        public UserProductModelCallable(UserProductLoanInfoDTO userProduct)
        {
            this.userProduct = userProduct;
        }

        @Override
        public UserProductLoanModel call() throws Exception
        {
            try {
                return loadUserProductModel(userProduct, true,true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 加载产品信息
     * @param userProductInfoDTO
     * @param loadUserInfo 是否加载用户信息
     * @param loadEnterprise 是否加载企业信息
     * @return
     */
    protected UserProductLoanModel loadUserProductModel(UserProductLoanInfoDTO userProductInfoDTO, boolean loadUserInfo, boolean loadEnterprise)
    {
        UserProductLoanModel userProductModel = null;

        ProductDTO productDTO  = productService.loadById(userProductInfoDTO.getProductId());

        if (userProductInfoDTO != null)
        {

            userProductModel = new UserProductLoanModel();

            BeanUtils.copyProperties(userProductInfoDTO, userProductModel, new String[]{"productName"});

            userProductModel.setProductName(productDTO.getName());

        }
        return userProductModel;
    }




}
