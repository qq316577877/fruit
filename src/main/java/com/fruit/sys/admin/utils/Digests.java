package com.fruit.sys.admin.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Description:
 * 支持SHA-1/MD5消息摘要的工具类.
 * <p/>
 * 返回ByteSource，可进一步被编码为Hex, Base64或UrlSafeBase64
 * Create Author  : terry
 * Create Date    : 2017-05-1511-23
 * Project        : message-biz
 * File Name      : Digests.java
 */
public class Digests
{

    private static final String SHA1 = "SHA-1";

    private static final String MD5 = "MD5";

    private static final String DEFAULT_PWD = "123456";

    private static SecureRandom random = new SecureRandom();

    /**
     * 对输入字符串进行sha1散列.
     */
    public static byte[] sha1(byte[] input)
    {
        return digest(input, SHA1, null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt)
    {
        return digest(input, SHA1, salt, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations)
    {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (salt != null)
            {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            for (int i = 1; i < iterations; i++)
            {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        }
        catch (GeneralSecurityException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对文件进行md5散列.
     */
    public static byte[] md5(InputStream input) throws IOException
    {
        return digest(input, MD5);
    }

    /**
     * 对文件进行sha1散列.
     */
    public static byte[] sha1(InputStream input) throws IOException
    {
        return digest(input, SHA1);
    }

    private static byte[] digest(InputStream input, String algorithm) throws IOException
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);
            while (read > -1)
            {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }
            return messageDigest.digest();
        }
        catch (GeneralSecurityException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成新密码
     * 不同于 <code>DigestUtils.md5Hex(String data)</code>, 此方法返回全部小写的hash码
     */
    public static String md5Hex(String data)
    {
        return new String(Hex.encodeHex(DigestUtils.md5(data), true));
    }

    /**
     * 设置系统默认密码
     */
    public static String md5Hex()
    {
        return new String(Hex.encodeHex(DigestUtils.md5(DEFAULT_PWD), false));
    }

}
