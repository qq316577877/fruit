/*
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All right reserved.
 */

package com.fruit.sys.admin.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * AES加密工具
 *
 * @author terry
 */
public class AESUtil
{

    /**
     * 根据指定的算法和秘钥长度生成秘钥
     *
     * @param algorithm 采用的算法(AES/ECB/PKCS5Padding)
     * @param keysize   (128/192/256)注意默认情况下JRE支持128位加密，192/256位加密是受限的。
     * @return
     */
    public static Key generateSecretKey(String algorithm, int keysize)
    {
        // 生成指定算法的密钥生成器
        KeyGenerator kg = null;
        try
        {
            kg = KeyGenerator.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
        // 初始化密钥生成器，使其具有确定的密钥大小，AES要求密钥长度为 128/192/256中的任一个
        kg.init(keysize);
        // 生成一个密钥
        return kg.generateKey();
    }

    /**
     * 转换密钥
     *
     * @param key 密钥
     * @return 二进制密钥
     */
    public static byte[] toByte(Key key)
    {
        return key.getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key, String algorithm)
    {
        return new SecretKeySpec(key, algorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception
    {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception
    {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

}