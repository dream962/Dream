package com.util;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

public class UuidUtil
{
    private static UUID uid = null;
    private static final Random RANDOM = new Random();

    public static String getUUID()
    {
        uid = UUID.randomUUID();
        String idstr = uid.toString().replace("-", "");
        return idstr.toUpperCase();
    }

    public static String generateGUID()
    {
        return new BigInteger(165, RANDOM).toString(36).toUpperCase();
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        int cycles = 10;

        long start = System.currentTimeMillis();
        for (int i = 0; i < cycles; i++)
        {
            // System.out.println(getUUID());
            getUUID();
        }
        long end = System.currentTimeMillis();
        String msg = "Time(ms) of getUUID() " + cycles + " times: ";
        System.out.println(msg + (end - start));

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < cycles; i++)
        {
            // System.out.println(generateGUID());
            generateGUID();
        }
        long end1 = System.currentTimeMillis();
        msg = "Time(ms) of generateGUID() " + cycles + " times: ";
        System.out.println(msg + (end1 - start1));
    }
}
