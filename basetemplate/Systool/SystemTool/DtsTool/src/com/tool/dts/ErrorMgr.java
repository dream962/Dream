package com.tool.dts;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ErrorMgr
{
    public static void error(String userMsg, Exception e)
    {
        Writer w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        System.err.println(String.format("出错信息：%s，程序提示：%s，Trace：%s",
                                         e.getMessage(), userMsg, w.toString()));
    }

    public static void info(String userMsg)
    {
        System.err.println(String.format("提示信息：%s", userMsg));
    }
}
