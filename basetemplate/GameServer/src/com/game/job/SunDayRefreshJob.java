package com.game.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.component.RankComponent;

/**
 * 每周日数据刷新
 * 
 * @author dream
 *
 */
@DisallowConcurrentExecution
public class SunDayRefreshJob implements Job
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SunDayRefreshJob.class.getName());

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        try
        {
            RankComponent.refreshWeek();
        }
        catch (Exception e)
        {
            LOGGER.error("SunDayRefreshJob Exception:", e);
        }
    }
}
