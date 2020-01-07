package com.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程池统一管理模块，便于排查问题（线程池必须命名）
 */
public class ThreadPoolUtil
{
    /**
     * 固定线程数量的线程池
     * 
     * @param num
     * @param threadFactoryName
     * @return
     */
    public static ExecutorService fixedServicePool(int num, String threadFactoryName)
    {
        return Executors.newFixedThreadPool(num, new NamedThreadFactory(threadFactoryName));
    }

    /**
     * 单线程线程池
     * 
     * @param threadFactoryName
     * @return
     */
    public static ExecutorService singleService(String threadFactoryName)
    {
        return Executors.newSingleThreadExecutor(new NamedThreadFactory(threadFactoryName));
    }

    /**
     * 定时器线程池
     * 
     * @param num
     * @return
     */
    public static ScheduledExecutorService scheduledExecutorPool(int num, String threadFactoryName)
    {
        return Executors.newScheduledThreadPool(num, new NamedThreadFactory(threadFactoryName));
    }

    /**
     * 单线程定时器
     * 
     * @param threadFactoryName
     * @return
     */
    public static ScheduledExecutorService singleScheduledExecutor(String threadFactoryName)
    {
        return Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(threadFactoryName));
    }

}
