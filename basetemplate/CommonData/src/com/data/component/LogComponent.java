package com.data.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.base.component.AbstractComponent;
import com.data.log.factory.ILogFactory;
import com.util.ClassUtil;
import com.util.ThreadPoolUtil;
import com.util.print.LogFactory;

/**
 * 日志组件
 * 
 * @author dream
 *
 */
public class LogComponent extends AbstractComponent
{
    private static Map<String, ILogFactory> factoryMap = new HashMap<String, ILogFactory>();

    private static ScheduledExecutorService schedule = ThreadPoolUtil.singleScheduledExecutor("log-pool");

    @Override
    public boolean initialize()
    {
        try
        {
            List<Class<?>> allClasses = ClassUtil.getClasses("com.data.log.factory");
            for (Class<?> clazz : allClasses)
            {
                try
                {
                    Object newObject = clazz.newInstance();
                    if (newObject instanceof ILogFactory)
                    {
                        ILogFactory factory = (ILogFactory) newObject;
                        factoryMap.put(factory.getClass().getSimpleName(), factory);
                    }
                }
                catch (Exception e)
                {
                    LogFactory.error("load command fail, bean factory name : " + clazz.getName(), e);
                }
            }

            schedule.scheduleWithFixedDelay(() -> save(), 10, GamePropertiesComponent.LOG_SAVE_INTERVAL, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            LogFactory.error("命令管理器解析错误", e);
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getLogFactory(Class<T> className)
    {
        ILogFactory data = factoryMap.get(className.getSimpleName());
        if (data != null)
        {
            return (T) data;
        }

        return null;
    }

    private static void save()
    {
        try
        {
            long time = System.currentTimeMillis();

            factoryMap.forEach((k, v) -> {
                v.save();
            });

            time = System.currentTimeMillis() - time;
            LogFactory.info("LogComponent -- Save Log Speed Time:{}", time);
        }
        catch (Exception e)
        {
            LogFactory.error("LogComponent Exception:", e);
        }
    }

    @Override
    public void stop()
    {
        save();
        schedule.shutdownNow();
    }
}
