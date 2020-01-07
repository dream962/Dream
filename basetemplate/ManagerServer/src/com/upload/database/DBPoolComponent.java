package com.upload.database;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upload.component.AbstractComponent;
import com.upload.component.GlobalConfigComponent;
import com.upload.config.DatabaseConfig;
import com.upload.config.DatabaseConfig.DruidPoolConfig;
import com.upload.database.pool.DBHelper;
import com.upload.database.pool.IDBPool;
import com.upload.database.pool.druid.DruidPool;
import com.upload.util.StringUtil;

/**
 * 数据库连接池管理类
 * 
 */
public final class DBPoolComponent extends AbstractComponent
{
    protected static volatile boolean isStop = false;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DBPoolComponent.class);

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
            Set<Entry<String, IDBPool>> entries = pools.entrySet();
            
            for (Entry<String, IDBPool> entry : entries)
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
    public void addDBPool(String dbName, IDBPool pool)
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
    public void addDBHelper(String dbName, DBHelper dbHelper)
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
    public boolean initWithDbXML(DatabaseConfig element)
    {
        try
        {
            /*if (element.bonePools != null && !element.bonePools.isEmpty())
            {
                for (BonePoolConfig object : element.bonePools)
                {
                    BoneCPConfiguration configuration = new BoneCPConfiguration(object);
                    BoneCPDBPool boneCPDBPool = new BoneCPDBPool(configuration);
                    DBHelper dbHelper = new DBHelper(boneCPDBPool);
                    if (StringUtil.isNullOrEmpty(object.area))
                    {
                        addDBPool(object.name, boneCPDBPool);
                        addDBHelper(object.name, dbHelper);
                    }
                    else
                    {
                        addDBPool(object.area + "_" + object.name, boneCPDBPool);
                        addDBHelper(object.area + "_" + object.name, dbHelper);
                    }

                    LOGGER.info("初始化 bonecp db pool：" + object.name);
                }
            }*/

            if (element.druidPools != null && !element.druidPools.isEmpty())
            {
                for (DruidPoolConfig object : element.druidPools)
                {
                    DruidPool pool = new DruidPool(object);
                    DBHelper dbHelper = new DBHelper(pool);
                    if (StringUtil.isNullOrEmpty(object.area))
                    {
                        addDBPool(object.name, pool);
                        addDBHelper(object.name, dbHelper);
                    }
                    else
                    {
                        addDBPool(object.area + "_" + object.name, pool);
                        addDBHelper(object.area + "_" + object.name, dbHelper);
                    }

                    LOGGER.info("初始化 druid db pool：" + object.name);
                }
            }

            return true;
        }
        catch (Exception e)
        {
            LOGGER.error("初始化数据库配置文件出错", e);
        }
        return false;

    }

    /**
     * 区域_名称的组合key
     * 
     * @param key
     * @return
     */
    public static DBHelper getDBHelper(String key)
    {
        return dbHelpers.get(key);
    }

    /**
     * 区域 和名称组合的DBHelper
     * 
     * @param area
     * @param dbName
     * @return
     */
    public static DBHelper getDBHelper(String area, String dbName)
    {
        return dbHelpers.get(area + "_" + dbName);
    }

    public static Map<String, DBHelper> getAllHelper()
    {
        return dbHelpers;
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
