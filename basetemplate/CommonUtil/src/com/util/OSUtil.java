package com.util;

/**
 * 操作系统工具类
 * 
 * @author dream
 *
 */
public class OSUtil
{
    public static boolean isWindows()
    {
        String os = System.getProperties().getProperty("os.name");

        if (os.indexOf("Windows") >= 0)
            return true;

        return false;
    }

    public static String vmConfig()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("最大内存：Xmx=" + Runtime.getRuntime().maxMemory() / 1024.0 / 1024 + "M").append("\n");     // 系统的最大空间
        builder.append("空闲内存：free mem=" + Runtime.getRuntime().freeMemory() / 1024.0 / 1024 + "M").append("\n");   // 系统的空闲空间
        builder.append("可用总内存：total mem=" + Runtime.getRuntime().totalMemory() / 1024.0 / 1024 + "M").append("\n");   // 当前可用的总空间

        return builder.toString();
    }

    public static void main(String[] args)
    {
        byte[] b = new byte[1345 * 1024 * 1024];
        System.err.println(vmConfig());
        b = null;
        System.gc();
        System.err.println(vmConfig());
    }
}
