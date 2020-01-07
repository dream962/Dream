
package com.base.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.util.print.PrintFactory;

/**
 * 监控任务
 * 
 * @author dream.wang
 *
 */
public class TestJob2 implements Job
{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        PrintFactory.error("TestJob2.");
    }
}
