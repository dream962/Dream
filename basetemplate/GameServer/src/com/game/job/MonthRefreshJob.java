package com.game.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.component.RankComponent;

/**
 * 每月数据刷新
 * 
 * @author dream
 *
 */
@DisallowConcurrentExecution
public class MonthRefreshJob implements Job
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonthRefreshJob.class.getName());

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        try
        {
            RankComponent.refreshMonth();
        }
        catch (Exception e)
        {
            LOGGER.error("MonthRefreshJob Exception:", e);
        }
    }
}
