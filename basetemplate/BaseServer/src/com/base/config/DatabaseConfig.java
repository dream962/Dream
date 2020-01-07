package com.base.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 数据库连接配置
 * 
 * @author dream
 *
 */
public class DatabaseConfig
{
    /** RSA private exponent */
    public String privateExponent;

    /** RSA private modulus */
    public String privateModulus;

    /** BoneCP连接池配置 */
    public static class BonePoolConfig
    {
        /** 名称 */
        @XmlAttribute(name = "name")
        public String name;

        /** 区域 */
        @XmlAttribute(name = "area")
        public String area;

        public String url;
        public String username;
        public String password;

        /** 设置分区个数。这个参数默认为1，建议3-4 */
        public int partitionCount = 4;
        /** 设置每个分区含有connection最大个数。这个参数默认为2。如果小于2，BoneCP将设置为50。 */
        public int maxConnectionsPerPartition = 2;
        /** 设置每个分区含有connection最大小个数。这个参数默认为0。 */
        public int minConnectionsPerPartition = 1;
        /** 设置分区中的connection增长数量。这个参数默认为1。 */
        public int acquireIncrement = 1;
        /** 设置连接池阀值。这个参数默认为20。如果小于0或是大于100，BoneCP将设置为20。 */
        public int poolAvailabilityThreshold = 20;
    }

    /** Druid连接池配置 */
    public static class DruidPoolConfig
    {
        /** 数据库名称 */
        @XmlAttribute(name = "name")
        public String name;

        public String url;
        public String username;
        public String password;

        /** 最大并发连接数 */
        public int maxActive = 4;
        /** 初始化时建立物理连接的个数 */
        public int initialSize = 2;
        /** 最小连接池数量 */
        public int minIdle = 2;
        /** Druid内置提供一个StatFilter，用于统计监控信息 */
        public String filters = "";
        /** 获取连接时最大等待时间，单位毫秒 */
        public int maxWait = 600000;
        /** 连接属性 */
        public String connectionProperties;
    }

    @XmlElementWrapper(name = "db-pool")
    @XmlElement(name = "db")
    public List<BonePoolConfig> bonePools;

    @XmlElementWrapper(name = "druid")
    @XmlElement(name = "db")
    public List<DruidPoolConfig> druidPools;

    @XmlElementWrapper(name = "orm")
    @XmlElement(name = "db")
    public List<BonePoolConfig> orm;

}
