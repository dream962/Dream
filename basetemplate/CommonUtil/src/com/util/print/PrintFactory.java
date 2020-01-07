package com.util.print;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import com.util.TimeUtil;

/**
 * 输出测试打印工厂
 * 
 * @author dream
 *
 */
public class PrintFactory
{
    private static int base = 0;

    private static boolean flag = true;

    public static void out(String str, Object... objects)
    {
        if (flag)
            System.out.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS") + " " + String.format(str, objects));
    }

    public static void outLevel(String str, int level, Object... objects)
    {
        if (level > base)
            System.out.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS") + " " + String.format(str, objects));
    }

    public static void error(String str, Object...objects)
    {
        if (flag)
            System.err.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS") + " " + String.format(str, objects));
    }

    public static void errorLevel(String str, int level, Object... objects)
    {
        if (level > base)
            System.err.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS") + " " + String.format(str, objects));
    }

    public static void trace(String str, Exception e)
    {
        if (flag)
        {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            System.err.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS") + " "
                    + String.format("出错信息：%s，程序提示：%s，Trace：%s", e.getMessage(), str, writer.toString()));
        }
    }

    public static void traceLevel(String str, int level, Exception e)
    {
        if (level > base)
        {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            System.err.println("Time:" + TimeUtil.getTimeFormat(new Date(), "HH:mm:ss:SSS")
                    + String.format("出错信息：%s，程序提示：%s，Trace：%s", e.getMessage(), str, writer.toString()));
        }
    }
}
