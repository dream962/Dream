package com.game.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.game.component.GamePlayerComponent;
import com.game.object.player.GamePlayer;
import com.util.print.LogFactory;

/**
 * 午夜刷新重置玩家信息
 * 
 * @author dream.wang
 */
@DisallowConcurrentExecution
public class MidnightRefreshJob implements Job
{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        try
        {
            List<GamePlayer> list = GamePlayerComponent.getAllPlayer();
            long time = System.currentTimeMillis();

            // 玩家信息刷新
            for (GamePlayer player : list)
            {
                player.refresh();
            }

            if (System.currentTimeMillis() - time > 5000)
            {
                LogFactory.warn(String.format("MidnightRefreshJob : refresh spend too much time -- %d",
                        System.currentTimeMillis() - time));
            }
        }
        catch (Exception e)
        {
            LogFactory.error("MidnightRefreshJob Exception:", e);
        }
    }
}
