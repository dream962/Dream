package com.base.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.util.print.LogFactory;

/**
 * 保存处理handler
 * 
 * @author dream
 *
 * @param <E>
 */
public class RedisSaveHandler<E>
{
    private static class SaveEntity<E>
    {
        public int redisID;
        public String key;
        public Set<String> subKey = null;
        public Class<E> clazz;
        /** 是否实体用hash散列形式保存 */
        public boolean isHashEntity;

        @Override
        public int hashCode()
        {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object a)
        {
            SaveEntity<?> entity = (SaveEntity<?>) a;
            if (entity.key.equals(key))
                return true;

            return false;
        }
    }

    /** 更新key */
    private Set<SaveEntity<E>> keySet = new HashSet<>();

    /** 更新key */
    private HashMap<String, SaveEntity<E>> subKeySet = new HashMap<>();

    public interface Hanlder<E>
    {
        public boolean exec(List<E> list);
    }

    private Hanlder<E> handler;

    public RedisSaveHandler(Hanlder<E> handler)
    {
        this.handler = handler;
    }

    public void addKey(long userID, String key, Class<E> clazz)
    {
        SaveEntity<E> entity = new SaveEntity<E>();
        entity.key = key;
        entity.redisID = RedisClientComponent.getRedisID(userID);
        entity.clazz = clazz;
        entity.isHashEntity = false;

        synchronized (keySet)
        {
            keySet.add(entity);
        }
    }

    public void addHashKey(long userID, String key, Class<E> clazz)
    {
        SaveEntity<E> entity = new SaveEntity<E>();
        entity.key = key;
        entity.redisID = RedisClientComponent.getRedisID(userID);
        entity.clazz = clazz;
        entity.isHashEntity = true;

        synchronized (keySet)
        {
            keySet.add(entity);
        }
    }

    public void addSubKey(long userID, String key, String subKey, Class<E> clazz)
    {
        synchronized (subKeySet)
        {
            SaveEntity<E> set = subKeySet.get(key);
            if (set == null)
            {
                set = new SaveEntity<E>();
                set.redisID = RedisClientComponent.getRedisID(userID);
                set.subKey = new HashSet<>();
                set.clazz = clazz;
                subKeySet.put(key, set);
            }

            set.subKey.add(subKey);
        }
    }

    public void save()
    {
        // 保证数据的保存,在异常情况下，数据也不会丢失。
        if (!keySet.isEmpty())
        {
            List<SaveEntity<E>> keys = new ArrayList<>();
            synchronized (keySet)
            {
                keys.addAll(keySet);
                keySet.clear();
            }

            try
            {
                List<E> list = new ArrayList<>();
                for (SaveEntity<E> entity : keys)
                {
                    RedisClient client = RedisClientComponent.getClient(entity.redisID);
                    E info = null;
                    if (entity.isHashEntity)
                        info = client.hgetall(entity.key, entity.clazz);
                    else
                        info = client.get(entity.key, entity.clazz);

                    if (info != null)
                        list.add(info);
                    else
                        LogFactory.error("udpate:redis list data is not exist.key:{},{}", entity.redisID, entity.key);
                }

                if (!handler.exec(list))
                {
                    synchronized (keySet)
                    {
                        keySet.addAll(keys);
                    }

                    LogFactory.error("RedisSaveHandler Save list error - rollback.");
                }
            }
            catch (Exception e)
            {
                synchronized (keySet)
                {
                    keySet.addAll(keys);
                }
                LogFactory.error("RedisSaveHandler Save list Exception:", e);
            }
            finally
            {
                keys.clear();
            }
        }

        if (!subKeySet.isEmpty())
        {
            HashMap<String, SaveEntity<E>> keys = new HashMap<>();
            synchronized (subKeySet)
            {
                keys.putAll(subKeySet);
                subKeySet.clear();
            }

            try
            {
                List<E> list = new ArrayList<>();
                for (Entry<String, SaveEntity<E>> entry : keys.entrySet())
                {
                    for (String subKey : entry.getValue().subKey)
                    {
                        RedisClient client = RedisClientComponent.getClient(entry.getValue().redisID);
                        E temp = client.hget(entry.getKey(), subKey, entry.getValue().clazz);
                        if (temp != null)
                            list.add(temp);
                        else
                        {
                            // 在更新之前该记录已经被删除
                            LogFactory.error("udpate:redis map data is not exist.redis:{},key:{},{}", entry.getValue().redisID, entry.getKey(),
                                    subKey);
                        }
                    }
                }

                if (!handler.exec(list))
                {
                    synchronized (subKeySet)
                    {
                        for (Entry<String, SaveEntity<E>> entry : keys.entrySet())
                        {
                            if (subKeySet.containsKey(entry.getKey()))
                                subKeySet.get(entry.getKey()).subKey.addAll(entry.getValue().subKey);
                            else
                                subKeySet.put(entry.getKey(), entry.getValue());
                        }
                    }

                    LogFactory.error("RedisSaveHandler Save map Exception - rollback.");
                }
            }
            catch (Exception e)
            {
                synchronized (subKeySet)
                {
                    for (Entry<String, SaveEntity<E>> entry : keys.entrySet())
                    {
                        if (subKeySet.containsKey(entry.getKey()))
                            subKeySet.get(entry.getKey()).subKey.addAll(entry.getValue().subKey);
                        else
                            subKeySet.put(entry.getKey(), entry.getValue());
                    }
                }

                LogFactory.error("RedisSaveHandler Save map Exception:", e);
            }
            finally
            {
                keys.clear();
            }
        }
    }
}
