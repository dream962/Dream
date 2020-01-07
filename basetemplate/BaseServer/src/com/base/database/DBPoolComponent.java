package com.base.database;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.config.DatabaseConfig;
import com.base.config.DatabaseConfig.DruidPoolConfig;
import com.base.database.pool.DBHelper;
import com.base.database.pool.IDBPool;
import com.base.database.pool.druid.DruidPool;
import com.util.print.LogFactory;

/**
 * 数据库连接池管理类
 * 
 */
public final class DBPoolComponent extends AbstractComponent
{
    protected static volatile boolean isStop = false;

    /**
     * pools保存所有的连接池的信息 key对应是这个连接池的名字
     */
    private static Map<String, IDBPool> pools = new ConcurrentHashMap<String, IDBPool>();

    private static Map<String, DBHelper> dbHelpers = new ConcurrentHashMap<String, DBHelper>();

    /**
     * 启动连接池
     * 
     * @param pool
     */
    private boolean startupPool()
    {
        synchronized (pools)
        {
            for (Entry<String, IDBPool> entry : pools.entrySet())
            {
                if (!entry.getValue().startup())
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 重新启动连接池
     */
    public void restartup()
    {
        synchronized (pools)
        {
            Set<Entry<String, IDBPool>> entries = pools.entrySet();
            for (Entry<String, IDBPool> entry : entries)
            {
                if (!entry.getValue().validConn())
                {
                    entry.getValue().startup();
                }
            }
        }
    }

    /**
     * 添加连接池，并启动连接池
     * 
     * @param dbName
     * @param pool
     */
    private void addDBPool(String dbName, IDBPool pool)
    {
        synchronized (pools)
        {
            IDBPool temp = pools.get(dbName);
            if (temp != null)
            {
                throw new RuntimeException("Already exit the dbPool!");
            }
            else
            {
                pools.put(dbName, pool);
            }
        }
    }

    /**
     * 添加Hepler
     * 
     * @param dbName
     * @param dbHelper
     */
    private void addDBHelper(String dbName, DBHelper dbHelper)
    {
        synchronized (dbHelpers)
        {
            DBHelper temp = dbHelpers.get(dbName);
            if (temp != null)
            {
                throw new RuntimeException("Already exit the dbHelper!");
            }
            else
            {
                dbHelpers.put(dbName, dbHelper);
            }
        }
    }

    /**
     * 根据XML解析数据库连接池
     * 
     * @param element
     *            传进来xml的database节点
     * 
     *            @xml文件的格式应该如下：<br>
     */
    private boolean initWithDbXML(DatabaseConfig element)
    {
        try
        {
            if (element.druidPools != null && !element.druidPools.isEmpty())
            {
                for (DruidPoolConfig object : element.druidPools)
                {
                    DruidPool pool = new DruidPool(object);
                    DBHelper dbHelper = new DBHelper(pool);
                    addDBPool(object.name, pool);
                    addDBHelper(object.name, dbHelper);

                    LogFactory.info("初始化 druid db pool：" + object.name);
                }
            }

            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("初始化数据库配置文件出错", e);
        }
        return false;

    }

    /**
     * 取得数据库帮助类
     * 
     * @param key
     * @return
     */
    public static DBHelper getDBHelper(String key)
    {
        return dbHelpers.get(key);
    }

    @Override
    public boolean initialize()
    {
        boolean result = initWithDbXML(GlobalConfigComponent.getConfig().database);
        startupPool();
        return result;
    }

    @Override
    public boolean start()
    {
        return true;
    }

    @Override
    public void stop()
    {
        isStop = true;

        synchronized (pools)
        {
            Set<Entry<String, IDBPool>> entries = pools.entrySet();
            for (Entry<String, IDBPool> entry : entries)
            {
                entry.getValue().shutdown();
            }
        }
    }

}
