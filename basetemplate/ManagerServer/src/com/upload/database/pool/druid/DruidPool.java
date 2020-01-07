package com.upload.database.pool.druid;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.upload.config.DatabaseConfig.DruidPoolConfig;
import com.upload.database.pool.IDBPool;

/**
 * DruidPool
 * 
 * @author dream
 *
 */
public class DruidPool implements IDBPool
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidPool.class);
    private DruidDataSource source;
    private DruidPoolConfig config;

    public DruidPool(DruidPoolConfig config)
    {
        this.config = config;
    }

    @Override
    public Connection getConnection()
    {
        try
        {
            return source.getConnection();
        }
        catch (SQLException e)
        {
            LOGGER.error("Exception:",e);
        }
        
        return null;
    }

    @Override
    public boolean startup()
    {
        source = new DruidDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUsername(config.username);
        source.setPassword(config.password);
        source.setUrl(config.url);
        source.setInitialSize(config.initialSize);
        source.setMinIdle(config.minIdle);
        source.setMaxActive(config.maxActive);
        source.setMaxWait(config.maxWait);
        
        if (config.filters != null && config.filters != "")
        {
            try
            {
                source.setFilters(config.filters);
            }
            catch (SQLException e)
            {
                LOGGER.error("Exception:",e);
            }
        }

        return true;
    }

    @Override
    public void shutdown()
    {
        source.close();
    }

    @Override
    public boolean validConn()
    {
        if (source == null || getCurConns() <= 0)
            return false;
        return true;
    }

    @Override
    public String getState()
    {
       return "";
    }

    @Override
    public int getCurConns()
    {
        return source.getActiveCount();
    }
}
