package com.base.redis;

import java.util.List;

import com.base.data.ChangedObject;

/**
 * 缓存处理公共接口
 * 
 * @author dream
 *
 */
public abstract class AbstractCache
{
    /** 保存数据的条数 */
    protected static final int SAVE_COUNT = 100;

    /**
     * 取得所有客户端
     * 
     * @return
     */
    protected List<RedisClient> getAllClient()
    {
        return RedisClientComponent.getClientList();
    }

    /**
     * 没有玩家ID，默认0缓存客户端
     * 
     * @return
     */
    protected RedisClient getRedisClient()
    {
        return getRedisClient(0);
    }

    /**
     * 根据redisID取得客户端
     * 
     * @param redisID
     * @return
     */
    protected RedisClient getRedisClient(int redisID)
    {
        return RedisClientComponent.getClient(redisID);
    }

    /**
     * 根据玩家的ID取得缓存客户端，取余算法求缓存客户端
     * 
     * @param userID
     * @return
     */
    protected RedisClient getRedisClient(long userID)
    {
        int id = RedisClientComponent.getRedisID(userID);
        return RedisClientComponent.getClient(id);
    }

    /**
     * 重置玩家信息的标志位false
     * 
     * @param list
     */
    protected void resetChanged(List<? extends ChangedObject> list)
    {
        if (list != null)
        {
            for (ChangedObject c : list)
            {
                c.setChanged(false);
            }
        }
    }

    /**
     * 重置玩家信息的标志位位false
     * 
     * @param list
     */
    protected void resetChanged(ChangedObject obj)
    {
        if (obj != null)
            obj.setChanged(false);
    }

    public boolean reload(long userID)
    {
        return true;
    }

    public abstract boolean save();

    public abstract boolean saveAll();

}
