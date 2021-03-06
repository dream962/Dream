package com.base.timer;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.base.component.AbstractComponent;
import com.util.print.LogFactory;

/**
 * 时间调度管理
 * CronTrigger配置格式: [秒] [分] [小时] [日] [月] [周] [年]
 * 
 * * 表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。
 * ? 表示不指定值。只能用在日和周域上，但是不能在这两个域上同时使用。使用的场景为不需要关心当前设置这个字段的值。例如:要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为"?" 具体设置为 0 0 0 10 *
 * ?
 * - 表示区间。例如 在小时上设置 "10-12",表示 10,11,12点都会触发。
 * , 表示指定多个值，例如在周字段上设置 "MON,WED,FRI" 表示周一，周三和周五触发
 * / 用于递增触发。如在秒上面设置"5/15" 表示从5秒开始，每增15秒触发(5,20,35,50)。 在月字段上设置'1/3'所示每月1号开始，每隔三天触发一次。
 * L 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]),
 * 在周字段上表示星期六，相当于"7"或"SAT"。如果在"L"前加上数字，则表示该数据的最后一个。例如在周字段上设置"6L"这样的格式,则表示“本月最后一个星期五"
 * W 表示离指定日期的最近那个工作日(周一至周五). 例如在日字段上设置"15W"，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发,
 * 如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为
 * "1W",它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，"W"前只能设置具体的数字,不允许区间"-").
 * 
 * 
 * 
 * "@DisallowConcurrentExecution"
 * 此标记用在实现Job的类上面,意思是不允许并发执行,Job(任务)的执行时间[比如需要10秒]大于任务的时间间隔[Interval（5秒)],那么默认情况下,调度框架为了能让
 * 任务按照我们预定的时间间隔执行,会马上启用新的线程执行任务。添加标记后,会等待任务执行完毕以后 再重新执行,保证线性执行！
 * 
 * @author dream 2014-12-14 上午1:04:16
 */
public class QuartzComponent extends AbstractComponent
{
    private static final String QUARTZ_GROUP = "quartzGroup";

    public static final String PARAM_NAME = "quartzParam";

    private static volatile Scheduler scheduler = null;

    public static void main(String[] args)
    {
        QuartzComponent component = new QuartzComponent();
        component.initialize();
        component.start();

        QuartzComponent.addForEverDelayJob("test", TestJob.class, 3);
        QuartzComponent.addForEverDelayJob("test2", TestJob2.class, 2);
        // QuartzComponent.addStartTimeRepeatJob("ddd", TestJob.class, TimeUtil.formartDate("2010-01-01 00:00:00",
        // "yyyy-MM-dd HH:mm:ss"), 60, null);
    }

    /**
     * @param jobName
     *            名称
     * @param job
     *            作业
     * @param cornException
     *            corn表达式
     */
    public static void addCornJob(String jobName, Class<? extends Job> clazz, String cronExpression)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, QUARTZ_GROUP).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常，", e);
        }
        catch (Exception e)
        {
            LogFactory.error("添加定时器任务异常，", e);
        }
    }

    /**
     * 添加无限重复的作业
     * 
     * @param jobName
     *            名称
     * @param job
     *            作业
     * @param secondInterval
     *            重复间隔 秒
     */
    public static void addForEverDelayJob(String jobName, Class<? extends Job> clazz, int secondInterval)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(jobName, QUARTZ_GROUP).withSchedule(
                    SimpleScheduleBuilder.repeatSecondlyForever(secondInterval)).build();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常", e);
        }
    }

    /**
     * 一次性延时作业
     * 
     * @param jobName
     * @param job
     * @param delay
     *            ms
     */
    public static void addOneTimeDelayJob(String jobName, Class<? extends Job> clazz, long millisecondsDelay)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().startAt(new Date(new Date().getTime() + millisecondsDelay)).withIdentity(jobName, QUARTZ_GROUP).build();

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常", e);
        }
    }

    /**
     * 添加一个在某段时间重复执行的任务
     * 
     * @param jobName
     * @param job
     * @param startTime
     * @param endTime
     * @param repeatInterval
     *            秒为单位
     */
    public static void addDelayRepeatJob(String jobName, Class<? extends Job> clazz, Date startTime, Date endTime,
            int secondInterval, Object param)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();

            Trigger trigger = TriggerBuilder.newTrigger().startAt(startTime).endAt(endTime).withSchedule(
                    SimpleScheduleBuilder.repeatSecondlyForever(secondInterval)).withIdentity(jobName, QUARTZ_GROUP).build();

            jobDetail.getJobDataMap().put(PARAM_NAME, param);

            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常", e);
        }
    }

    /**
     * 添加一个从开始时间执行一次的任务
     * 
     * @param jobName
     * @param job
     * @param startTime
     */
    public static void addDelayJob(String jobName, Class<? extends Job> clazz, Date startTime, Object param)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().startAt(startTime).withIdentity(jobName, QUARTZ_GROUP).build();
            jobDetail.getJobDataMap().put(PARAM_NAME, param);
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常", e);
        }
    }

    /**
     * 从固定时间开始，间隔一定时间，永久重复
     * 
     * @param jobName
     * @param clazz
     * @param startTime
     *            ：job开始时间
     * @param repeatTime
     *            ：重复时间秒
     * @param param
     */
    public static void addStartTimeRepeatJob(String jobName, Class<? extends Job> clazz, Date startTime, int repeatSecondTime, Object param)
    {
        try
        {
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, QUARTZ_GROUP).build();
            Trigger trigger = TriggerBuilder.newTrigger().startAt(startTime).withIdentity(jobName, QUARTZ_GROUP).withSchedule(
                    SimpleScheduleBuilder.repeatSecondlyForever(repeatSecondTime)).build();
            jobDetail.getJobDataMap().put(PARAM_NAME, param);
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("添加定时器任务异常", e);
        }
    }

    /**
     * 删除job
     * 
     * @param jobName
     */
    public static void deleteJob(String jobName)
    {
        try
        {
            JobDetail detail = scheduler.getJobDetail(new JobKey(jobName, QUARTZ_GROUP));
            if (detail == null)
            {
                return;
            }

            scheduler.pauseTrigger(new TriggerKey(jobName, QUARTZ_GROUP));
            scheduler.unscheduleJob(new TriggerKey(jobName, QUARTZ_GROUP));
            scheduler.deleteJob(new JobKey(jobName, QUARTZ_GROUP));
        }
        catch (SchedulerException e)
        {
            LogFactory.error("定时器删除任务异常，", e);
        }
    }

    @Override
    public boolean initialize()
    {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try
        {
            scheduler = schedulerFactory.getScheduler();
        }
        catch (SchedulerException e)
        {
            LogFactory.error("初始化定时器异常，", e);
            return false;
        }
        return true;
    }

    @Override
    public void stop()
    {
        try
        {
            scheduler.shutdown(true);
        }
        catch (SchedulerException e)
        {
            LogFactory.error("定时器关闭异常，", e);
        }
    }

}
