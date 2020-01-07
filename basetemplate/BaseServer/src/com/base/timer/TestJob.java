
package com.base.timer;

import org.quartz.DisallowConcurrentExecution;
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
@DisallowConcurrentExecution
public class TestJob implements Job
{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        PrintFactory.error("TestJob.");
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
