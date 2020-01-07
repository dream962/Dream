/**
 * 
 */
package com.game.component;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.base.component.AbstractComponent;
import com.base.timer.QuartzComponent;
import com.game.GameServer;
import com.util.print.LogFactory;

/**
 * @date 2018年1月24日 上午11:49:43
 * @author TIME
 * @desc gm管理
 */
public class GmManagerComponent extends AbstractComponent
{
    private static Date serverStopDate = null;
    private static String serverStopClientShow = "";
    private static final String STOP_SERVER = "stop_server";

    private static final class StopServerJob implements Job
    {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException
        {
            GameServer.getInstance().stop();
        }
    }

    @Override
    public boolean initialize()
    {
        return true;
    }

    /**
     * 添加服务器定时停服定时器
     * 
     * @param entity
     */
    public static boolean addServerStopTimer(Date stopTime, String clientDoc)
    {
        try
        {
            serverStopDate = stopTime;
            serverStopClientShow = clientDoc;
            long delay = stopTime.getTime() - System.currentTimeMillis();
            QuartzComponent.deleteJob(STOP_SERVER);
            QuartzComponent.addOneTimeDelayJob(STOP_SERVER, StopServerJob.class, delay);
            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return false;
    }

    /**
     * 玩家登录获取停服倒计时时间
     * 
     * @param
     */
    public static long getServerStopRestPeriod()
    {
        if (serverStopDate == null || new Date().getTime() >= serverStopDate.getTime())
        {
            return -1;
        }
        else
        {
            return serverStopDate.getTime() - new Date().getTime();
        }
    }

    /**
     * 玩家登录获取停服倒计时显示文案
     * 
     * @param
     */
    public static String getServerStopClientShow()
    {
        return serverStopClientShow;
    }

    @Override
    public void stop()
    {

    }

}
