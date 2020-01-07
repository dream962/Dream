package com.game.component;

import com.base.component.AbstractComponent;
import com.base.timer.QuartzComponent;
import com.data.component.GamePropertiesComponent;
import com.game.job.GamePlayerAutoSaveJob;
import com.game.job.GamePlayerPingJob;
import com.game.job.MidnightRefreshJob;
import com.game.job.MonthRefreshJob;
import com.game.job.SunDayRefreshJob;

/**
 * 游戏服定时器
 * 
 * @author dream.wang
 *
 */
public class GameJobComponent extends AbstractComponent
{
    @Override
    public boolean initialize()
    {
        return true;
    }

    @Override
    public boolean start()
    {
        // 每日凌晨刷新作业
        QuartzComponent.deleteJob("job_midnight_refresh");
        QuartzComponent.addCornJob("job_midnight_refresh", MidnightRefreshJob.class, GamePropertiesComponent.JOB_CORN_REFRESH);

        // 每周日数据清空
        QuartzComponent.deleteJob("sunday_refresh");
        QuartzComponent.addCornJob("sunday_refresh", SunDayRefreshJob.class, GamePropertiesComponent.SUNDAY_REFRESH);

        // 每月数据清空
        QuartzComponent.deleteJob("month_refresh");
        QuartzComponent.addCornJob("month_refresh", MonthRefreshJob.class, GamePropertiesComponent.SUNDAY_REFRESH);

        // ping玩家信息
        QuartzComponent.deleteJob("job_ping_player");
        QuartzComponent.addForEverDelayJob("job_ping_player", GamePlayerPingJob.class, GamePropertiesComponent.JOB_PING_PLAYER);

        // 玩家保存
        QuartzComponent.deleteJob("job_save_player");
        QuartzComponent.addForEverDelayJob("job_save_player", GamePlayerAutoSaveJob.class, GamePropertiesComponent.JOB_SAVE_PLAYER);

        // 刷新支付
        QuartzComponent.deleteJob("refresh_token");
        QuartzComponent.addForEverDelayJob("refresh_token", GamePlayerAutoSaveJob.class, GamePropertiesComponent.REFRESH_TOKEN);

        return true;
    }

    @Override
    public void stop()
    {

    }
}
