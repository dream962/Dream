package com.base.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 缓存服务器代理配置
 * 
 * @author dream
 *
 */
public class CacheServerConfig
{
    public static class RedisConfig
    {
        @XmlAttribute
        public int key;

        @XmlAttribute
        public String desc;
    }

    public String host;

    public int port;

    public String password;

    public int expiredTime;

    public int syncInterval;

    public boolean isCompress = false;

    public int dbCount = 10;

    /** 控制一个pool最多有多少个状态为idle的jedis实例 */
    public int maxIdle = 20;

    /** 控制一个pool最少有多少个状态为idle的jedis实例 */
    public int minIdle = 10;

    /** 连接最大的等待时间，如果超时，抛出异常 */
    public int maxWaitMillis = 1000 * 10;

    /** 连接池最大连接个数 */
    public int maxTotal = 1000;

    @XmlElementWrapper(name = "redis")
    @XmlElement(name = "detail")
    public List<RedisConfig> redisList;
}
