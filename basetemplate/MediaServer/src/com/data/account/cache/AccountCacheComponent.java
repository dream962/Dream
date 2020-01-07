package com.data.account.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.redis.AbstractCache;
import com.base.rmi.IRemoteCode;
import com.data.account.data.TableData;
import com.data.account.factory.TableDataFactory;
import com.util.ClassUtil;
import com.util.ThreadPoolUtil;
import com.util.print.LogFactory;

/**
 * 缓存管理组件
 * 
 * @author dream
 */
public class AccountCacheComponent extends AbstractComponent
{
    protected static volatile boolean isStop = false;

    /** 缓存Public处理 */
    private static Map<String, AbstractCache> publicRemotes = new HashMap<>();

    /** 定时保存线程池 */
    private static ScheduledExecutorService threadSyncMysql = ThreadPoolUtil.singleScheduledExecutor("redis-cache");

    @Override
    public boolean initialize()
    {
        try
        {
            List<Class<?>> allClasses = ClassUtil.getClasses("com.data.account.cache");

            for (Class<?> clazz : allClasses)
            {
                IRemoteCode cmd = clazz.getAnnotation(IRemoteCode.class);
                if (cmd != null)
                {
                    publicRemotes.put(cmd.code(), (AbstractCache) clazz.newInstance());
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
            interval = 300;

        threadSyncMysql.scheduleWithFixedDelay(() -> save(), 10, interval, TimeUnit.SECONDS);

        // 初始化
        List<TableData> tables = TableDataFactory.getDao().listAll();
        CachePublicSystem publicTable = getModule(CachePublicSystem.class);
        for (TableData data : tables)
        {
            publicTable.resetTableMaxID(data.getTableID(), data.getValue());
        }
        return true;
    }

    @Override
    public void stop()
    {
        save();
        isStop = true;
        threadSyncMysql.shutdownNow();
        publicRemotes.clear();
    }

    public static void save()
    {
        try
        {
            if (isStop)
                return;

            List<AbstractCache> list = new ArrayList<>();
            list.addAll(publicRemotes.values());

            long time = System.currentTimeMillis();

            StringBuilder builder = new StringBuilder();
            long detailTime = 0;
            for (AbstractCache common : list)
            {
                detailTime = System.currentTimeMillis();
                common.save();
                builder.append("class:" + common.getClass().getSimpleName()).append(",time:" + (System.currentTimeMillis() - detailTime)).append("\n");
            }

            long dt = System.currentTimeMillis() - time;

            if (dt > 1000)
            {
                LogFactory.warn("Cache data save to database too much time：" + dt);
            }

            if (dt > 3000)
            {
                LogFactory.error("Cache data save to database too much time：" + dt);
                LogFactory.error(builder.toString());
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
        AbstractCache cache = publicRemotes.get(type);
        return (T) cache;
    }

    public static <T> T getModule(Class<T> t)
    {
        return getModule(t.getSimpleName());
    }

    public static CachePublicMediaUser getCacheUser()
    {
        return getModule(CachePublicMediaUser.class);
    }

    public static CachePublicSystem getSystem()
    {
        return getModule(CachePublicSystem.class);
    }
}
