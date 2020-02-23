package com.game.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 玩家定时保存作业
 * 
 * @author dream.wang
 */
@DisallowConcurrentExecution
public class RefreshTokenJob implements Job
{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
    }
}
