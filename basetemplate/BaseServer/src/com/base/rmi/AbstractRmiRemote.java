package com.base.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import com.base.redis.RedisClient;
import com.base.redis.RedisClientComponent;
import com.base.redis.RedisSaveHandler;

/**
 * RMI抽象接口公共类，绑定一个redis client，缓存
 * 
 * @author dansen
 *         2015年8月31日 下午2:43:27
 */
public abstract class AbstractRmiRemote extends UnicastRemoteObject
{
    private static final long serialVersionUID = 7855795243543228565L;

    protected RedisClient client;

    /** 修改的key保存记录 */
    protected HashMap<Byte, RedisSaveHandler<?>> handlerMap = new HashMap<>();

    protected AbstractRmiRemote() throws RemoteException
    {
        this(0);
    }

    protected AbstractRmiRemote(int redisServerID) throws RemoteException
    {
        super();
        client = RedisClientComponent.getClient(redisServerID);
    }

    protected AbstractRmiRemote(int redisServerID, int expiredTime) throws RemoteException
    {
        super();
        client = RedisClientComponent.getClient(redisServerID);
    }

    /**
     * 添加修改的key
     * 
     * @param type
     * @param key
     */
    protected void addKey(byte type, long userID, String key, Class clazz)
    {
        RedisSaveHandler<?> handler = handlerMap.get(type);
        if (handler != null)
        {
            handler.addKey(userID, key, clazz);
        }
    }

    /**
     * 添加key
     * 
     * @param type
     * @param key
     * @param subKey
     */
    protected void addKey(byte type, long userID, String key, int subKey, Class clazz)
    {
        RedisSaveHandler<?> handler = handlerMap.get(type);
        if (handler != null)
        {
            handler.addSubKey(userID, key, String.valueOf(subKey), clazz);
        }
    }

    // /**
    // * 添加删除的key
    // *
    // * @param type
    // * @param key
    // * @param subKey
    // */
    // protected void addDelKey(byte type, String key, int subKey)
    // {
    // RedisSaveHandler<?> handler = handlerMap.get(type);
    // if (handler != null)
    // {
    // handler.addDelMapKey(key, subKey);
    // }
    // }

    /**
     * 定时保存数据
     */
    public void save()
    {
        for (RedisSaveHandler<?> handler : handlerMap.values())
            handler.save();
    }
}
