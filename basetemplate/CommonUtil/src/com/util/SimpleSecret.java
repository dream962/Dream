/**
 * 
 */
package com.util;

import java.math.BigInteger;

/**
 * @date 2018年6月7日 下午5:12:58
 * @author TIME
 * @desc 一个简单的加密解密小工具
 */
public class SimpleSecret
{
    private final static String secretStr = "19920609";

    public static final String encrypt(String s)
    {
        if (s == null)
            return "";
        if (s.length() == 0)
        {
            return "";
        }
        else
        {
            BigInteger biginteger = new BigInteger(s.getBytes());
            BigInteger biginteger1 = new BigInteger(secretStr);
            BigInteger biginteger2 = biginteger1.xor(biginteger);
            return biginteger2.toString(16);
        }
    }

    public static final String decrypt(String s)
    {
        if (s == null)
            return "";
        if (s.length() == 0)
            return "";
        BigInteger biginteger = new BigInteger(secretStr);
        try
        {
            BigInteger biginteger1 = new BigInteger(s, 16);
            BigInteger biginteger2 = biginteger1.xor(biginteger);
            return new String(biginteger2.toByteArray());
        }
        catch (Exception exception)
        {
            return "";
        }
    }

    public static void main(String[] args)
    {
        String baseStr = "123654123";
        String targetStr = encrypt(baseStr);
        System.out.println(targetStr);
        System.out.println(decrypt(targetStr));
    }
}
