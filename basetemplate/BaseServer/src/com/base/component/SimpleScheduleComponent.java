package com.base.component;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.ThreadPoolUtil;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

/**
 * 简单定时器处理
 * 
 * @author dream
 *
 */
public class SimpleScheduleComponent extends AbstractComponent
{
    private static Logger logger = LoggerFactory.getLogger(SimpleScheduleComponent.class);
    /** 线程数量 */
    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    /** 一个线程池 */
    private static ExecutorService service = null;
    /** 任务队列 */
    private volatile static Queue<Job> taskQueue = null;
    /** 退出标识 */
    private static volatile boolean isRunning = true;
    /** 当前最近的任务时间 */
    private static volatile long leftTime = 0;
    /** 任务ID */
    private static AtomicInteger jobID = new AtomicInteger(0);
    /** 数量 */
    public static AtomicInteger count = new AtomicInteger(0);

    public static abstract class Job implements Runnable
    {
        public boolean isStop = false;
        public long interval;
        public long delay;
        public long nextTime = 0;
        public long jobID = 0;
    }

    @Override
    public boolean initialize()
    {
        service = ThreadPoolUtil.fixedServicePool(DEFAULT_INITIAL_CAPACITY, "SimpleScheduleComponent");
        taskQueue = new PriorityBlockingQueue<>(8, new Comparator<Job>()
        {
            @Override
            public int compare(Job o1, Job o2)
            {
                return (int) (o1.nextTime - o2.nextTime);
            }
        });

        service.submit(() -> check());
        return true;
    }

    /**
     * 结束定时器
     */
    public void stop()
    {
        isRunning = false;
        service.shutdown();
    }

    private static void check()
    {
        long time = System.currentTimeMillis();

        while (isRunning && !taskQueue.isEmpty())
        {
            Job job = taskQueue.peek();

            // 取出所有到时的任务
            if (time >= job.nextTime)
            {
                service.submit(job);
                taskQueue.poll();

                // TODO:这里添加，是固定频率延时FixRate
                // if (job.interval > 0)
                // {
                // job.nextTime = time + job.interval;
                // taskQueue.add(job);
                // }
            }
            else
            {
                break;
            }
        }

        if (taskQueue.isEmpty())
        {
            leftTime = 0;
        }
        else
        {
            leftTime = taskQueue.peek().nextTime - time;
        }

        try
        {
            synchronized (taskQueue)
            {
                taskQueue.wait(leftTime);
            }

            if (!service.isShutdown())
            {
                service.submit(() -> check());
            }
        }
        catch (Exception e)
        {
            logger.info("", e);
        }
    }

    /**
     * 添加一个job
     * 
     * @param delay
     *            延迟多长时间执行(ms)
     * @param interval
     *            间隔多长时间执行，如果是0则表示不重复执行(ms)
     * @param job
     *            任务对象
     * @return
     */
    private static long add(int delay, int interval, Job job)
    {
        if (job == null)
            return 0;

        if (delay < 0 || interval < 0)
            return 0;

        try
        {
            job.interval = interval;
            job.jobID = jobID.incrementAndGet();
            job.delay = delay;
            job.nextTime = System.currentTimeMillis() + delay;

            taskQueue.add(job);
            // 通知所有线程
            synchronized (taskQueue)
            {
                taskQueue.notifyAll();
            }

            return job.jobID;
        }
        catch (Exception e)
        {
            LogFactory.error("SimpleScheduleComponent Exception:", e);
        }

        return 0;
    }

    /**
     * 对外接口
     * 
     * @param delay
     * @param interval
     * @param consumer
     * @return jobID 返回一个任务ID，便于删除该任务
     */
    public static long schedule(int delay, int interval, Consumer<Job> consumer)
    {
        long j = add(delay, interval, new Job()
        {
            @Override
            public void run()
            {
                try
                {
                    long time = System.currentTimeMillis();
                    consumer.accept(this);
                    long current = System.currentTimeMillis();

                    if (current - time > 200)
                    {
                        logger.info(String.format("SimpleScheduleComponent (%s) spend too much time %d", consumer,
                                current - time));
                    }
                }
                catch (Exception e)
                {
                    logger.info("", e);
                }
                finally
                {
                    // TODO:这里添加，是固定间隔延时FixDelayTime
                    if (interval > 0)
                    {
                        nextTime = System.currentTimeMillis() + interval;
                        taskQueue.add(this);
                        // 通知所有线程
                        synchronized (taskQueue)
                        {
                            taskQueue.notifyAll();
                        }
                    }
                }
            }
        });

        return j;
    }

    /**
     * 对外接口
     * 
     * @param consumer
     * @return
     */
    public static long schedule(Consumer<Job> consumer)
    {
        return schedule(0, 0, consumer);
    }

    /**
     * 停止定时任务
     * 
     * @param jobID
     */
    public static void unschedule(long jobID)
    {
        taskQueue.removeIf(p -> {
            return p.jobID == jobID;
        });
    }

    public static void main(String[] args)
    {
        SimpleScheduleComponent schedule = new SimpleScheduleComponent();
        schedule.initialize();
        SimpleScheduleComponent.schedule(0, 100, (job) -> {
            try
            {
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            PrintFactory.error("ddddd");
        });
        SimpleScheduleComponent.schedule(0, 0, (job) -> System.err.println("dddddd2"));
        SimpleScheduleComponent.schedule(20, 0, (job) -> System.err.println("dddddd3"));
    }
}
