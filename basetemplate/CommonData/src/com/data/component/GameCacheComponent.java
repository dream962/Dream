package com.data.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.redis.CacheCommon;
import com.base.rmi.IRemoteCode;
import com.util.ClassUtil;
import com.util.ThreadPoolUtil;
import com.util.print.LogFactory;

/**
 * 缓存管理组件
 * 
 * @author dream
 */
public class GameCacheComponent extends AbstractComponent
{
    protected static volatile boolean isStop = false;

    /** 缓存处理 */
    private static Map<String, CacheCommon> remotes = new HashMap<>();

    /** 定时保存线程池 */
    private static ScheduledExecutorService threadSyncMysql = ThreadPoolUtil.singleScheduledExecutor("redis-cache");

    @Override
    public boolean initialize()
    {
        try
        {
            List<Class<?>> allClasses = ClassUtil.getClasses("com.game.cache");

            for (Class<?> clazz : allClasses)
            {
                IRemoteCode cmd = clazz.getAnnotation(IRemoteCode.class);
                if (cmd != null)
                {
                    remotes.put(cmd.code(), (CacheCommon) clazz.newInstance());
                }
            }

            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("缓存加载异常", e);
        }

        return false;
    }

    @Override
    public boolean start()
    {
        int interval = GlobalConfigComponent.getConfig().cacheServer.syncInterval;
        if (interval <= 0)
            interval = 180;

        // 初始化自增ID,通过账号服设置添加，全局唯一
        // List<TablesInfo> list = SystemBusiness.getTableList();
        // for (TablesInfo info : list)
        // {
        // getRemoteSystem().resetTableMaxID(info.getTableID(), info.getValue());
        // }

        threadSyncMysql.scheduleWithFixedDelay(() -> save(), 1, interval, TimeUnit.SECONDS);

        return true;
    }

    @Override
    public void stop()
    {
        save();
        isStop = true;
        threadSyncMysql.shutdownNow();
        remotes.clear();
    }

    public static void save()
    {
        try
        {
            long time = System.currentTimeMillis();

            List<CacheCommon> list = new ArrayList<>(remotes.values());
            for (CacheCommon common : list)
            {
                if (isStop)
                {
                    break;
                }
                common.save();
            }

            long dt = System.currentTimeMillis() - time;

            if (dt > 3000)
            {
                LogFactory.info("Cache data save to database too much time：" + dt);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("Cache Synchronize DB Exception:", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getModule(String type)
    {
        return (T) remotes.get(type);
    }
}
