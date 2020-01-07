package com.game.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.game.component.GamePlayerComponent;
import com.game.object.player.GamePlayer;
import com.util.print.LogFactory;

@DisallowConcurrentExecution
public class GamePlayerPingJob implements Job
{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        try
        {
            List<GamePlayer> list = GamePlayerComponent.getAllPlayer();
            for (GamePlayer player : list)
                player.getPingModule().ping();
        }
        catch (Exception e)
        {
            LogFactory.error("GamePlayerPingJob Exception:", e);
        }
    }
}
