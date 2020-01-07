package com.upload.util;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-27 17:39:47
 * @description
 *              用于查看jsch执行连接时状态的Logger
 */
public class SettleLogger implements com.jcraft.jsch.Logger
{
    public boolean isEnabled(int level)
    {
        return true;
    }

    public void log(int level, String msg)
    {
        System.out.println(msg);
    }
}
