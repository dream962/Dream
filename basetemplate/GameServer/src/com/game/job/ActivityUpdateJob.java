package com.game.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 活动更新 
 * @date 2016年7月18日 上午10:32:50 
 * @author dansen 
 * @desc 
 */


public class ActivityUpdateJob implements Job
{
    private static final Logger logger=LoggerFactory.getLogger(ActivityUpdateJob.class.getName());
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        try
        {
            
        }
        catch (Exception e)
        {
            logger.error("活动更新异常:",e);
        }
    }
}



