package com.game;

import java.util.Date;
import java.util.Random;

import com.util.ThreadSafeRandom;
import com.util.TimeUtil;

public class Test
{
    public static void main(String args[])
    {
        try
        {
            Date date = TimeUtil.getDate("2019-11-28 17:07:00");
            ThreadSafeRandom.random = new Random(date.getTime());
            int[] list = ThreadSafeRandom.next(1, 36, 5);
            for (int i : list)
                System.err.print(i + ",");

            date = TimeUtil.getDate("1983-11-24 00:00:00");
            ThreadSafeRandom.random = new Random(date.getTime());
            list = ThreadSafeRandom.next(1, 13, 2);
            for (int i : list)
                System.err.print(i + ",");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
