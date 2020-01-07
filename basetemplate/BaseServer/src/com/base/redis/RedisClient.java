package com.base.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.base.config.CacheServerConfig;
import com.util.EntityHashUtil;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.params.SetParams;

/**
 * redis缓存代理
 * 
 * @author dansen
 * @date 2015年8月28日 下午6:53:59
 */
public class RedisClient
{
    private JedisPool pool = null;

    /** 数据过期时间（三天） */
    private int expiredTime = 3600 * 72;

    private int redisID;

    public int getExpireTime()
    {
        return expiredTime;
    }

    public int getRedisID()
    {
        return redisID;
    }

    /**
     * 初始化redis连接
     * 
     * @param host
     * @param port
     * @param expiredTime
     *            过期时间（小时）
     * @param password
     *            密码
     * @param dbID
     *            子库ID（16个 0-15）
     * @return
     */
    public boolean init(CacheServerConfig data, int dbID)
    {
        JedisPoolConfig config = new JedisPoolConfig();
        this.expiredTime = data.expiredTime * 3600;
        this.redisID = dbID;

        config.setMaxIdle(data.maxIdle);
        config.setMaxWaitMillis(data.maxWaitMillis);
        config.setMinIdle(data.minIdle);
        config.setMaxTotal(data.maxTotal);

        pool = new JedisPool(config, data.host, data.port < 0 ? 6379 : data.port, expiredTime, data.password, dbID);

        Jedis jedis = null;

        try
        {
            jedis = pool.getResource();
        }
        catch (Exception e)
        {
            LogFactory.error("redis client init Exception:" + data.host + ":" + data.port, e);
            return false;
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return true;
    }

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     * 
     * @param key
     * @return
     */
    public int ttl(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            long val = jedis.ttl(key);
            return (int) val;
        }
        catch (Exception e)
        {
            LogFactory.error("redis error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return -1;
    }

    /**
     * 设置失效时间
     * 对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
     * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
     * 
     * @param key
     * @param second
     */
    public boolean expire(String key, int second)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            long val = jedis.expire(key, second);
            return val == 1;
        }
        catch (Exception e)
        {
            LogFactory.error("redis error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**************************************** set *******************************************/

    /**
     * hash表 设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, String field, Object value)
    {
        if (value == null)
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            // 0 表示更新，1表示新增
            byte[] data = RedisSerializeUtil.serialize(value);
            if (data != null)
            {
                long r = jedis.hset(key.getBytes(), field.getBytes(), data);
                jedis.expire(key, this.expiredTime);
                return r == 0 || r == 1;
            }
            else
            {
                LogFactory.error("redis set error:" + key + "-" + field + ",Serialize Exception Null.");
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis set error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * hash表 设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, int field, Object value)
    {
        return hset(key, String.valueOf(field), value);
    }

    /**
     * hash表 设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, long field, Object value)
    {
        return hset(key, String.valueOf(field), value);
    }

    /**
     * hash表 批量设置值
     * 
     * @param key
     * @param map
     * @return
     */
    public boolean hset(String key, Map<String, Object> map)
    {
        if (map == null || map.isEmpty())
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            for (Entry<String, Object> entry : map.entrySet())
            {
                // 0 表示更新，1表示新增
                byte[] data = RedisSerializeUtil.serialize(entry.getValue());
                if (data != null)
                {
                    long r = jedis.hset(key.getBytes(), entry.getKey().getBytes(), data);
                    if (r != 0 && r != 1)
                    {
                        LogFactory.error("redis hset error:" + key + "-" + entry.getKey() + ",jedis failed.");
                        return false;
                    }
                }
                else
                {
                    LogFactory.error("redis set error:" + key + "-" + entry.getKey() + ",Serialize Exception Null.");
                    return false;
                }
            }

            jedis.expire(key, this.expiredTime);
        }
        catch (Exception e)
        {
            LogFactory.error("redis set error:" + key, e);
            return false;
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return true;
    }

    /**
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * 若域 field 已经存在，该操作无效。
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hsetex(String key, String field, Object value)
    {
        if (value == null)
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            // 0 表示更新，1表示新增
            byte[] data = RedisSerializeUtil.serialize(value);
            if (data != null)
            {
                long r = jedis.hsetnx(key.getBytes(), field.getBytes(), data);
                jedis.expire(key, this.expiredTime);
                return r == 0 || r == 1;
            }
            else
            {
                LogFactory.error("redis set error:" + key + "-" + field + ",Serialize Exception Null.");
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis set error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 对象散列化设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hmset(String key, String field, String value)
    {
        if (value == null)
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            Map<byte[], byte[]> hash = new HashMap<>();
            hash.put(field.getBytes(), value.getBytes());
            jedis.hmset(key.getBytes(), hash);
            jedis.expire(key, this.expiredTime);
            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("redis hmset error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 对象散列化设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hmset(String key, Map<String, String> hash)
    {
        if (hash == null || hash.isEmpty())
            return true;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.hmset(key, hash);
            jedis.expire(key, this.expiredTime);
            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("redis hmset error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 对象散列化设置值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hmset(String key, Object obj)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            Map<String, String> map = EntityHashUtil.objectToHash(obj);
            jedis.hmset(key, map);
            jedis.expire(key, this.expiredTime);
            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("redis hmset error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 缓存的是对象，所以通过命令行查看是数据会有部分类说明的乱码
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value)
    {
        if (value == null)
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            String r = jedis.set(key.getBytes(), RedisSerializeUtil.serialize(value));
            jedis.expire(key, this.expiredTime);

            return r.equals("OK");
        }
        catch (Exception e)
        {
            LogFactory.error("redis set error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 设置带单独失效时间的key值
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setex(String key, Object value)
    {
        return setex(key, value, this.expiredTime);
    }

    /**
     * 设置带单独失效时间的key值
     * 
     * @param key
     * @param value
     * @param expiredTime
     *            失效时间（秒）
     * @return
     */
    public boolean setex(String key, Object value, int expiredTime)
    {
        if (value == null)
            return false;

        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            String r = jedis.setex(key.getBytes(), expiredTime, RedisSerializeUtil.serialize(value));
            return r.equals("OK");
        }
        catch (Exception e)
        {
            LogFactory.error("redis set error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * 
     * @param key
     * @param value
     * @return
     */
    public int setnx(String key, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            long val = jedis.setnx(key, "" + value);
            jedis.expire(key, this.expiredTime);
            return (int) val;
        }
        catch (Exception e)
        {
            LogFactory.error("redis error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return -1;
    }

    /************************************* lock-unlock ****************************************/

    /**
     * 数据加锁（默认1秒生存时间）
     * 
     * @param key
     * @return
     */
    public long lock(String key)
    {
        return lock(key, 1000);
    }

    /**
     * Hash数据加锁
     * 
     * @param key
     * @param subKey
     * @param expireTime
     * @return
     */
    public long lock(String key, String subKey, int expireTime)
    {
        return lock(key + ":" + subKey, expireTime);
    }

    /**
     * 数据加锁
     * 
     * @param key
     *            加锁的Key
     * @param value
     *            加锁的各自独有value值
     * @param expireTime
     *            锁超时时间 毫秒
     * @return
     */
    public long lock(String key, int expireTime)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            boolean isWile = true;
            long v = jedis.incr("auto:" + key);
            String value = v + "";
            if (v >= Integer.MAX_VALUE)
                jedis.getSet(key, "1");

            int index = 1;
            do
            {
                String str = jedis.set("lock:" + key, value, new SetParams().px(expireTime).nx());
                if (str != null && str.equalsIgnoreCase("ok"))
                    isWile = false;
                else
                {
                    index++;
                    Thread.sleep(1);
                    if (index > 3000)
                    {
                        // 只打印3次
                        if (index <= 3003)
                            LogFactory.error("Redis缓存加锁超时时间。key:{},time:{}", key, index);

                        if (index > 5000)
                        {
                            jedis.getSet(key, value);
                            jedis.pexpire(key, (long) expireTime);
                            LogFactory.error("Redis缓存加锁异常。key:{},time:{}", key, index);
                            break;
                        }
                    }
                }
            }
            while (isWile);

            return v;
        }
        catch (Exception e)
        {
            LogFactory.error("redis lock error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return 0;
    }

    /**
     * Hash数据解锁
     * 
     * @param key
     * @param subKey
     * @param value
     */
    public void unlock(String key, String subKey, long value)
    {
        unlock(key + ":" + subKey, value);
    }

    /**
     * 数据解锁
     * 
     * @param key
     * @param value
     */
    public void unlock(String key, long value)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            String data = jedis.get("lock:" + key);
            if (data != null)
            {
                // 防止删了其他人加的锁
                if (data.equalsIgnoreCase(value + ""))
                    jedis.del("lock:" + key);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis get error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**************************************** get *******************************************/

    /**
     * 取得一个key的值
     * 
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> clazz)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            byte[] data = jedis.get(key.getBytes());
            if (data != null && data.length > 0)
            {
                T object = RedisSerializeUtil.unserialize(data, clazz);
                jedis.expire(key, this.expiredTime);
                return object;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis get error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return null;
    }

    /**
     * 获取值 hash表
     * 
     * @param key
     * @param field
     * @return
     */
    public <T> T hget(String key, String field, Class<T> clazz)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            // 0 表示更新，1表示新增
            byte[] val = jedis.hget(key.getBytes(), field.getBytes());
            if (val != null)
            {
                T object = RedisSerializeUtil.unserialize(val, clazz);
                jedis.expire(key, this.expiredTime);
                return object;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis hget error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取值 hash表
     * 
     * @param key
     * @param field
     * @return
     */
    public <T> T hget(String key, int field, Class<T> clazz)
    {
        return hget(key, String.valueOf(field), clazz);
    }

    /**
     * 取得一个key的值
     * 
     * @param key
     * @return
     */
    public String hmget(String key, String field)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            List<String> data = jedis.hmget(key, field);
            if (data != null && data.size() > 0)
            {
                return data.get(0);
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis get error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return null;
    }

    /**
     * 取得散列的所有数据
     * 
     * @param key
     * @return
     */
    public Map<String, String> hgetall(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            Map<String, String> data = jedis.hgetAll(key);
            if (data != null && data.size() > 0)
            {
                return data;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis hgetall error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return null;
    }

    /**
     * 散列数据转对象
     * 
     * @param key
     * @return
     */
    public <T> T hgetall(String key, Class<T> clazz)
    {
        Map<String, String> map = hgetall(key);
        if (map != null)
        {
            T object = EntityHashUtil.hashToObject(map, clazz);
            return object;
        }

        return null;
    }

    /**
     * 返回指定key的所有 hash value。
     * 
     * @param key
     * @return
     */
    public <T> List<T> hValues(String key, Class<T> clazz)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            List<byte[]> val = jedis.hvals(key.getBytes());
            if (val != null && !val.isEmpty())
            {
                jedis.expire(key, this.expiredTime);
                List<T> all = new ArrayList<>();

                val.forEach(p -> {
                    T object = RedisSerializeUtil.unserialize(p, clazz);
                    if (object != null)
                        all.add(object);
                });

                return all;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("redis hvals error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    /**************************************** del *******************************************/

    /**
     * 删除单个Hash
     * 
     * @param key
     * @param field
     */
    public void hdel(String key, String field)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.hdel(key, field);
        }
        catch (Exception e)
        {
            LogFactory.error("redis hdel error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 删除单个Hash
     * 
     * @param key
     * @param field
     */
    public void hdel(String key, int field)
    {
        hdel(key, String.valueOf(field));
    }

    /**
     * 删除单个key
     * 
     * @param key
     */
    public long delete(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.del(key);
        }
        catch (Exception e)
        {
            LogFactory.error("redis delete error:" + key, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return 0;
    }

    /**
     * 删除单个key
     * 
     * @param key
     */
    public long delete(int key)
    {
        return delete(key + "");
    }

    /**
     * 删除多个key
     * 
     * @param members
     * @return
     */
    public long del(String... members)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.del(members);
        }
        catch (Exception e)
        {
            LogFactory.error("redis mult delete error:" + members, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return 0;
    }

    /************************************** 列表set *****************************************/

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合
     * 
     * @param key
     * @param members
     */
    public void sadd(String key, String... members)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.sadd(key, members);
            jedis.expire(key, this.expiredTime);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * 
     * @param key
     * @param members
     */
    public void srem(String key, String... members)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.srem(key, members);
        }
        catch (Exception e)
        {
            LogFactory.error("redis remove keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 判断集合 key是否包含member元素成员。
     * 
     * @param key
     * @param member
     * @return
     */
    public boolean scontain(String key, String member)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return false;
    }

    /**
     * 返回集合 key 中的所有成员。不存在的 key 被视为空集合。
     * 
     * @param key
     * @return
     */
    public List<String> smembers(String key)
    {
        Jedis jedis = null;
        try
        {
            List<String> list = new ArrayList<>();
            jedis = pool.getResource();
            Set<String> set = jedis.smembers(key);
            list.addAll(set);
            if (set != null && !set.isEmpty())
                jedis.expire(key, this.expiredTime);

            return list;
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return null;
    }

    /************************************** global *****************************************/

    /**
     * 将 key 中储存的数字值增一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * key对应的value值必须是数字类型。本操作的值限制在 64 位(bit)有符号数字表示之内。
     * 
     * @param key
     * @return
     */
    public long incr(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.expire(key, this.expiredTime);
            return jedis.incr(key);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return -1;
    }

    /**
     * 重置原子操作的值
     * 
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.expire(key, this.expiredTime);
            String result = jedis.getSet(key, value);
            return result;
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return "";
    }

    /**
     * 将 key 所储存的值加上增量 increment 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     * 
     * @param key
     * @param val
     * @return
     */
    public long incrBy(String key, int val)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.expire(key, this.expiredTime);
            return jedis.incrBy(key, val);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return -1;
    }

    /**************************************** scan *******************************************/

    /**
     * 基于游标的迭代器</br>
     * SCAN命令每次被调用之后， 都会向用户返回一个新的游标， 用户在下次迭代时需要使用这个新游标作为 SCAN命令的游标参数， 以此来延续之前的迭代过程。</br>
     * 当 SCAN 命令的游标参数被设置为 0 时， 服务器将开始一次新的迭代， 而当服务器向用户返回值为 0 的游标时， 表示迭代已结束
     * 
     * @param cursor
     *            游标
     * @param params
     *            参数
     * @return
     */
    public ScanResult<String> scan(String cursor, ScanParams params)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.scan(cursor, params);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return null;
    }

    /**
     * 基于游标的迭代器</br>
     * SCAN命令每次被调用之后， 都会向用户返回一个新的游标， 用户在下次迭代时需要使用这个新游标作为 SCAN命令的游标参数， 以此来延续之前的迭代过程。</br>
     * 当 SCAN 命令的游标参数被设置为 0 时， 服务器将开始一次新的迭代， 而当服务器向用户返回值为 0 的游标时， 表示迭代已结束
     * 
     * @param cursor
     *            游标
     * @param count
     *            查询数量
     * @param pattern
     *            正则表达式
     * @return
     */
    public ScanResult<String> scan(String cursor, int count, final String pattern)
    {
        ScanParams params = new ScanParams();
        params.count(count);
        params.match(pattern);

        return scan(cursor, params);
    }

    /************************************** function *****************************************/

    /**
     * 清除所有数据库的数据
     */
    public void flushAll()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.flushAll();
        }
        catch (Exception e)
        {
            LogFactory.error("redis flush all error.", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 清除当前数据库的数据
     */
    public void flushDB()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.flushDB();
        }
        catch (Exception e)
        {
            LogFactory.error("redis flush db error.", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    public void save()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.save();
        }
        catch (Exception e)
        {
            LogFactory.error("redis save error.", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    public void saveAsync()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.bgsave();
        }
        catch (Exception e)
        {
            LogFactory.error("redis save async error.", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 切换到指定的数据库，数据库索引号 index 用数字值指定，以 0 作为起始索引值。默认使用 0 号数据库。
     * 
     * @param id
     */
    public void select(int id)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.select(id);
        }
        catch (Exception e)
        {
            LogFactory.error("redis select db error:" + id, e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 请求服务器关闭与当前客户端的连接。
     * 一旦所有等待中的回复(如果有的话)顺利写入到客户端，连接就会被关闭。
     */
    public void quit()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.quit();
        }
        catch (Exception e)
        {
            LogFactory.error("redis quit error:", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * @return
     */
    public String getRedisInfo()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.keys("");
            return jedis.info();
        }
        catch (Exception e)
        {
            LogFactory.error("redis get info error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 根据字符串取得匹配的key（慎用）
     * 
     * @param pattern
     * @return
     */
    public List<String> keys(String pattern)
    {
        List<String> list = new ArrayList<String>();
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            Set<String> sets = jedis.keys(pattern);
            list.addAll(sets);
        }
        catch (Exception e)
        {
            LogFactory.error("redis get keys error", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return list;
    }

    /**
     * 订阅
     * 
     * @param sub
     * @param channels
     */
    public void subscribe(JedisPubSub sub, String... channels)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.subscribe(sub, channels);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 发布
     * 
     * @param channel
     * @param message
     */
    public void publish(String channel, String message)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.publish(channel, message);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }
    }

    /**
     * 配置
     */
    public String configSet(String parameter, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            return jedis.configSet(parameter, value);
        }
        catch (Exception e)
        {
            LogFactory.error("redis flush all error.", e);
        }
        finally
        {
            if (jedis != null)
            {
                jedis.close();
            }
        }

        return "";
    }

    public static void main(String[] args)
    {
        RedisClient client = new RedisClient();
        CacheServerConfig config = new CacheServerConfig();
        config.host = "192.168.1.97";
        config.port = 6382;
        config.expiredTime = 72 * 3600;
        config.password = "daomu2";
        client.init(config, 0);

        for (int i = 0; i < 100; i++)
        {
            long string = client.lock("test111");
            PrintFactory.error(string + "");
        }

        ScanParams params = new ScanParams();
        params.count(999);
        params.match("*");
        ScanResult<String> result = client.scan("0", params);
        System.err.println(result.getCursor());
        System.err.println(result.getResult().size());

        // for(int i=0;i<200;i++)
        // {
        // client.set(i+"a",
        // i+"abcsssssss阿斯顿发送到发送到发送到发的发送到发送到发送到发送到发斯蒂芬ssasf水电费水电费是多付多所付多所付所多付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付阿道夫付所多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多水电费的说法abcsssssss阿斯顿发送到发送到发送到发的发送到发送到发送到发送到发斯蒂芬ssasf水电费水电费是多付多所付多所付所多付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付阿道夫付所多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多水电费的说法abcsssssss阿斯顿发送到发送到发送到发的发送到发送到发送到发送到发斯蒂芬ssasf水电费水电费是多付多所付多所付所多付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付阿道夫付所多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多水电费的说法abcsssssss阿斯顿发送到发送到发送到发的发送到发送到发送到发送到发斯蒂芬ssasf水电费水电费是多付多所付多所付所多付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付付阿道夫付所多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多多水电费的说法");
        // }
    }
}
