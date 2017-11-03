/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.Key;

/**
 * 默认的AES加密工具类
 *
 * @author terry
 */
public class AESHelper
{

    /**
     * 默认密钥算法
     */
    private static final String DEFAULT_KEY_ALGORITHM = "AES";

    /**
     * 默认秘钥长度
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 默认密文算法:加密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 默认秘钥:<code>cdc1dbfff4ece85f66cf8aeda578fcc4</code>
     */
    private static Key DEFAULT_KEY = null;

    static
    {
        setSecretKey("cdc1dbfff4ece85f66cf8aeda578fcc4");
    }

    /**
     * 设置秘钥
     *
     * @param key 必须是32位16进制字符串
     */
    public static synchronized void setSecretKey(String key)
    {
        try
        {
            DEFAULT_KEY = AESUtil.toKey(Hex.decodeHex(key.toCharArray()), DEFAULT_KEY_ALGORITHM);
        }
        catch (DecoderException e)
        {
            e.printStackTrace();
            if (null == DEFAULT_KEY)
            {
                DEFAULT_KEY = AESUtil.generateSecretKey(DEFAULT_KEY_ALGORITHM, DEFAULT_KEY_SIZE);
            }
        }
    }

    /**
     * 加密
     *
     * @param data 需要加密的字符串
     * @return 加密后经过过<code>Hex</code>转换的字符串
     */
    public static String encryptHexString(String data)
    {
        if (null == data)
        {
            return data;
        }
        try
        {
            byte[] encrypt = AESUtil.encrypt(data.getBytes("UTF-8"), DEFAULT_KEY, DEFAULT_CIPHER_ALGORITHM);
            return Hex.encodeHexString(encrypt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param encryptedHexString 需要解密的字符串(经AES加密并通过Hex转码的字符串)
     * @return 解密后字符串
     */
    public static String decrypt(String encryptedHexString)
    {
        if (null == encryptedHexString)
        {
            return encryptedHexString;
        }
        try
        {
            byte[] decodeHex = Hex.decodeHex(encryptedHexString.toCharArray());
            byte[] encrypt = AESUtil.decrypt(decodeHex, DEFAULT_KEY, DEFAULT_CIPHER_ALGORITHM);
            return new String(encrypt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
