package com.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ThreadSafeRandom
{
    // 防止连续两次随机一样
    public static Random random = new Random(System.currentTimeMillis());

    public static float nextFloat()
    {
        float val = random.nextFloat();
        return val;
    }

    public static int next()
    {
        return random.nextInt();
    }

    public static int next(int maxValue)
    {
        if (maxValue <= 0)
            return 0;

        return random.nextInt(maxValue);
    }

    /**
     * 随机区间值，如 min=1 maxValue=5 随机，其结果值不包括5
     * 
     * @param minValue
     *            开始值
     * @param maxValue
     *            结束值
     * @return
     */
    public static int next(int minValue, int maxValue)
    {
        if (minValue < maxValue)
        {
            return random.nextInt(maxValue - minValue) + minValue;
        }
        return minValue;
    }

    /**
     * 随机区间值，如 min=1 maxValue=5 随机，其结果值包括5
     * 
     * @param minValue
     *            开始值
     * @param maxValue
     *            结束值
     * @return
     */
    public static int nextWithMax(int minValue, int maxValue)
    {
        int val = next(minValue, maxValue + 1);
        return val;
    }

    /**
     * 比较随机的数值与给定的概率
     * 
     * @param max
     * @param percent
     * @return random>=percent true, or false
     */
    public static boolean compareRandom(int max, float percent)
    {
        float index = next(max) / (max * 1.0f);

        if (index <= percent)
            return true;
        else
            return false;
    }

    /**
     * 比较随机的数值与给定的参数
     * 
     * @param max
     * @param percent
     * @return random>=percent true, or false
     */
    public static boolean compareRandom(int max, int percent)
    {
        if (next(max) >= percent)
            return true;
        else
            return false;
    }

    /**
     * 随机区间值，如 min=1f maxValue=5f 随机，其结果值不包括5f
     * 
     * @param minValue
     *            开始值
     * @param maxValue
     *            结束值
     * @return
     */
    public static float next(float minValue, float maxValue)
    {
        if (minValue < maxValue)
        {
            return random.nextFloat() * (maxValue - minValue) + minValue;
        }
        return minValue;
    }

    /**
     * generate no duplicate numbers.
     * 
     * @param min
     * @param max
     * @param count
     * @return
     */
    public static int[] next(int min, int max, int count)
    {
        if (count < 0 || (max - min) < count)
        {
            int[] temp = { min };
            return temp;
        }

        Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        while (map.size() < count)
        {
            int value = next(min, max);
            if (!map.containsKey(value))
            {
                map.put(value, value);
            }
        }

        int[] temp = new int[map.keySet().size()];
        int index = 0;
        for (Integer i : map.keySet())
        {
            temp[index] = i;
            index = index + 1;
        }
        return temp;
    }

    public static float percent(int base)
    {
        return next(base) / base * 1.0f;
    }

    public static float percent()
    {
        return percent(10000);
    }

    public static void main(String[] args)
    {
        int[] aa = next(0, 6, 6);
        for (int a : aa)
        {
            System.err.println(a);
        }
    }
}
