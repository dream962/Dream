package com.base.redis;

import java.util.List;

import com.base.data.ChangedObject;

/**
 * 缓存处理公共接口
 * 
 * @author dream
 *
 */
public class CacheCommon
{
    /** 修改的key保存记录(删除的数据直接在数据库删除) */
    protected RedisSaveHandler<?> handler;

    protected CacheCommon()
    {

    }

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
     * 添加修改的key key-value数据类型
     * 
     * @param type
     * @param userID
     *            玩家ID，如果没有的，填0；
     * @param key
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void addKey(long userID, String key, Class clazz)
    {
        handler.addKey(userID, key, clazz);
    }

    /**
     * 添加修改的key key-hash散列字段的数据类型
     * 
     * @param type
     * @param userID
     *            玩家ID，如果没有的，填0；
     * @param key
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void addHashKey(long userID, String key, Class clazz)
    {
        handler.addHashKey(userID, key, clazz);
    }

    /**
     * 添加修改的key key-subKey-value 数据类型
     * 
     * @param type
     * @param userID
     *            玩家ID，如果没有的，填0；
     * @param key
     * @param subKey
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void addSubKey(long userID, String key, String subKey, Class clazz)
    {
        handler.addSubKey(userID, key, subKey, clazz);
    }

    /**
     * 添加修改的key key-subKey-value 数据类型
     * 
     * 
     * @param type
     * @param userID
     *            玩家ID，如果没有的，填0；
     * @param key
     * @param subKey
     */
    @SuppressWarnings({ "rawtypes" })
    protected void addSubKey(long userID, String key, int subKey, Class clazz)
    {
        addSubKey(userID, key, String.valueOf(subKey), clazz);
    }

    /**
     * 添加修改的key key-subKey-value 数据类型
     * 
     * 
     * @param type
     * @param userID
     *            玩家ID，如果没有的，填0；
     * @param key
     * @param subKey
     */
    @SuppressWarnings({ "rawtypes" })
    protected void addSubKey(long userID, String key, long subKey, Class clazz)
    {
        addSubKey(userID, key, String.valueOf(subKey), clazz);
    }

    /**
     * 定时保存数据
     */
    public void save()
    {
        handler.save();
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
}
