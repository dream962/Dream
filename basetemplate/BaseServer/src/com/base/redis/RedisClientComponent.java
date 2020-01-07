package com.base.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.config.CacheServerConfig;
import com.util.print.LogFactory;

/**
 * redis客户端组件
 * 
 * @author dream
 *
 */
public class RedisClientComponent extends AbstractComponent
{
    /** 数据过期时间（三天） */
    private static int expiredTime = 72;

    /** <配置的ID,client> redis实例的集合 */
    private static Map<Integer, RedisClient> clientMap = new HashMap<>();

    private static int count = 1;

    public static int getRedisID(long userID)
    {
        return (int) (userID % count);
    }

    @Override
    public boolean initialize()
    {
        CacheServerConfig cache = GlobalConfigComponent.getConfig().cacheServer;
        count = GlobalConfigComponent.getConfig().cacheServer.dbCount;
        count = count < 0 ? 1 : count;
        count = count > 16 ? 16 : count;

        for (int i = 0; i < count; i++)
        {
            try
            {
                RedisClient client = new RedisClient();
                if (cache.expiredTime <= 0)
                    cache.expiredTime = expiredTime;

                if (client.init(cache, i))
                {
                    clientMap.put(i, client);
                    LogFactory.error("add redis client : " + i + ", " + cache.host + ":" + cache.port + ",db" + i);
                }
                else
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                LogFactory.error("Redis Client Init Exception:", e);
            }
        }

        return true;
    }

    @Override
    public void stop()
    {
        for (RedisClient client : clientMap.values())
            client.quit();

        clientMap.clear();
    }

    /**
     * 根据类型取得缓存信息
     * 
     * @param redisDB
     * @return
     */
    public static RedisClient getClient(int redisDB)
    {
        RedisClient client = clientMap.get(redisDB);
        if (client != null)
            return client;

        return clientMap.get(0);
    }

    public static List<RedisClient> getClientList()
    {
        List<RedisClient> list = new ArrayList<>();
        list.addAll(clientMap.values());

        return list;
    }
}
