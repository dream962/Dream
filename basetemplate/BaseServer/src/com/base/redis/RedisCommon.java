package com.base.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.base.data.ChangedObject;
import com.util.print.LogFactory;

/**
 * 缓存处理公共接口
 * 
 * @author dream
 *
 */
public abstract class RedisCommon
{
    /** 修改的key保存记录(删除的数据直接在数据库删除) */
    protected HashMap<Byte, RedisSaveHandler<?>> handlerMap = new HashMap<>();

    protected RedisCommon()
    {

    }

    /**
     * 没有玩家ID，默认0数据库
     * 
     * @return
     */
    protected RedisClient getRedisClient()
    {
        return getRedisClient(0);
    }

    /**
     * 根据玩家的ID取得数据库，取余算法求数据库
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
    protected void addKey(byte type, long userID, String key, Class clazz)
    {
        RedisSaveHandler<?> handler = handlerMap.get(type);
        if (handler != null)
        {
            handler.addKey(userID, key, clazz);
        }
        else
        {
            LogFactory.error("addKey Error.type:{},userID:{},class:{}", type, userID, clazz.getName());
        }
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
    protected void addHashKey(byte type, long userID, String key, Class clazz)
    {
        RedisSaveHandler<?> handler = handlerMap.get(type);
        if (handler != null)
        {
            handler.addHashKey(userID, key, clazz);
        }
        else
        {
            LogFactory.error("addHashKey Error.type:{},userID:{},class:{}", type, userID, clazz.getName());
        }
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
    protected void addSubKey(byte type, long userID, String key, String subKey, Class clazz)
    {
        RedisSaveHandler<?> handler = handlerMap.get(type);
        if (handler != null)
        {
            handler.addSubKey(userID, key, subKey, clazz);
        }
        else
        {
            LogFactory.error("addSubKey Error.type:{},userID:{},class:{}", type, userID, clazz.getName());
        }
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
    protected void addSubKey(byte type, long userID, String key, int subKey, Class clazz)
    {
        addSubKey(type, userID, key, String.valueOf(subKey), clazz);
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
    protected void addSubKey(byte type, long userID, String key, long subKey, Class clazz)
    {
        addSubKey(type, userID, key, String.valueOf(subKey), clazz);
    }

    /**
     * 定时保存数据
     */
    public void save()
    {
        Collection<RedisSaveHandler<?>> vals = handlerMap.values();

        for (RedisSaveHandler<?> handler : vals)
        {
            handler.save();
        }
    }

    /**
     * 重置玩家信息的标志位位false
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
