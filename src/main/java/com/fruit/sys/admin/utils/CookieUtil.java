/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

import com.fruit.sys.admin.model.UserModel;
import com.fruit.sys.admin.model.UserInfo;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : partal-main-web
 * File Name      : CookieUtil.java
 */
public class CookieUtil
{

    /**
     * 根据用户信息生成passport cookie
     *
     * @param userInfo
     * @return
     */
    public static String generatePassportByUserInfo(UserInfo userInfo)
    {
        if (null == userInfo)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(userInfo.getSysId()).append('|'); // 用户ID
        sb.append(userInfo.getUserName()).append('|'); // 用户名
        sb.append(System.currentTimeMillis());
        return AESHelper.encryptHexString(sb.toString());
    }

    /**
     * 根据用户信息生成passport cookie
     *
     * @param userModel
     * @return
     */
    public static String generatePassportByUserModel(UserModel userModel)
    {
        if (null == userModel)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(userModel.getUserId()).append('|'); // 用户ID
        sb.append(userModel.getUserName()).append('|'); // 用户名
        sb.append(System.currentTimeMillis());
        return AESHelper.encryptHexString(sb.toString());
    }

    /**
     * 根据passport cookie解析出用户ID
     *
     * @param passport
     * @return
     */
    public static String getIdFromPassport(String passport)
    {
        String decrypted = AESHelper.decrypt(passport);
        if (null == decrypted)
        {
            return null;
        }
        int index = decrypted.indexOf('|');
        if (index > 0)
        {
            return decrypted.substring(0, index);
        }
        return null;
    }

}
