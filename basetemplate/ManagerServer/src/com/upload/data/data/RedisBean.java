package com.upload.data.data;

import java.util.Date;

import java.io.Serializable;
public class RedisBean implements Serializable 
{
    private static final long serialVersionUID = 1L;

	/**
	 *  监控时间
	 */
	private Date recordDate;

	/**
	 * 服务器ID
	 */
	private int serverID;

	/**
	 * IP
	 */
	private String iP;

	/**
	 * 端口
	 */
	private int port;

	/**
	 * 实时内存(以人类可读的格式返回 Redis 分配的内存总量)
	 */
	private float usedMemoryHuman;

	/**
	 * 实时最高内存(占用内存的峰值)
	 */
	private float usedMemoryPeakHuman;

	/**
	 * 配置最大内存(从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）)
	 */
	private String usedMemoryRss;

	/**
	 * RDB模式频率（save）
	 */
	private String rdbChangesSinceLastSave;

	/**
	 * RDB文件目录
	 */
	private String rdbDbFilename;

	/**
	 * AOF模式
	 */
	private String aofEnabled;

	/**
	 * AOF文件目录
	 */
	private String aofDbFilename;

	/**
	 * 已连接客户端的数量（不包括通过从属服务器连接的客户端)
	 */
	private String connectedClients;

	/**
	 * redis内部调度（进行关闭timeout的客户端，删除过期key等等）频率，程序规定serverCron每秒运行10次。
	 */
	private String hz;

	/**
	 * 的key的数量,以及带有生存期的key的数,平均存活时间
	 */
	private String keyspace;

	/**
	 * redis服务器启动总时间，单位是天
	 */
	private String uptimeInDays;

	/**
	 * redis服务器版本
	 */
	private String redisVersion;

	/**
	 *  Redis 服务器耗费的系统 CPU
	 */
	private String userdCpuSys;


	/**
	 *  监控时间
	 */
	public Date getRecordDate()
	{
		return recordDate;
	}

	/**
	 *  监控时间
	 */
	public void setRecordDate(Date recordDate)
	{
		this.recordDate = recordDate;
	}

	/**
	 * 服务器ID
	 */
	public int getServerID()
	{
		return serverID;
	}

	/**
	 * 服务器ID
	 */
	public void setServerID(int serverID)
	{
		this.serverID = serverID;
	}

	/**
	 * IP
	 */
	public String getIP()
	{
		return iP;
	}

	/**
	 * IP
	 */
	public void setIP(String iP)
	{
		this.iP = iP;
	}

	/**
	 * 端口
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * 端口
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * 实时内存(以人类可读的格式返回 Redis 分配的内存总量)
	 */
	public float getUsedMemoryHuman()
	{
		return usedMemoryHuman;
	}

	/**
	 * 实时内存(以人类可读的格式返回 Redis 分配的内存总量)
	 */
	public void setUsedMemoryHuman(float usedMemoryHuman)
	{
		this.usedMemoryHuman = usedMemoryHuman;
	}

	/**
	 * 实时最高内存(占用内存的峰值)
	 */
	public float getUsedMemoryPeakHuman()
	{
		return usedMemoryPeakHuman;
	}

	/**
	 * 实时最高内存(占用内存的峰值)
	 */
	public void setUsedMemoryPeakHuman(float usedMemoryPeakHuman)
	{
		this.usedMemoryPeakHuman = usedMemoryPeakHuman;
	}

	/**
	 * 配置最大内存(从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）)
	 */
	public String getUsedMemoryRss()
	{
		return usedMemoryRss;
	}

	/**
	 * 配置最大内存(从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）)
	 */
	public void setUsedMemoryRss(String usedMemoryRss)
	{
		this.usedMemoryRss = usedMemoryRss;
	}

	/**
	 * RDB模式频率（save）
	 */
	public String getRdbChangesSinceLastSave()
	{
		return rdbChangesSinceLastSave;
	}

	/**
	 * RDB模式频率（save）
	 */
	public void setRdbChangesSinceLastSave(String rdbChangesSinceLastSave)
	{
		this.rdbChangesSinceLastSave = rdbChangesSinceLastSave;
	}

	/**
	 * RDB文件目录
	 */
	public String getRdbDbFilename()
	{
		return rdbDbFilename;
	}

	/**
	 * RDB文件目录
	 */
	public void setRdbDbFilename(String rdbDbFilename)
	{
		this.rdbDbFilename = rdbDbFilename;
	}

	/**
	 * AOF模式
	 */
	public String getAofEnabled()
	{
		return aofEnabled;
	}

	/**
	 * AOF模式
	 */
	public void setAofEnabled(String aofEnabled)
	{
		this.aofEnabled = aofEnabled;
	}

	/**
	 * AOF文件目录
	 */
	public String getAofDbFilename()
	{
		return aofDbFilename;
	}

	/**
	 * AOF文件目录
	 */
	public void setAofDbFilename(String aofDbFilename)
	{
		this.aofDbFilename = aofDbFilename;
	}

	/**
	 * 已连接客户端的数量（不包括通过从属服务器连接的客户端)
	 */
	public String getConnectedClients()
	{
		return connectedClients;
	}

	/**
	 * 已连接客户端的数量（不包括通过从属服务器连接的客户端)
	 */
	public void setConnectedClients(String connectedClients)
	{
		this.connectedClients = connectedClients;
	}

	/**
	 * redis内部调度（进行关闭timeout的客户端，删除过期key等等）频率，程序规定serverCron每秒运行10次。
	 */
	public String getHz()
	{
		return hz;
	}

	/**
	 * redis内部调度（进行关闭timeout的客户端，删除过期key等等）频率，程序规定serverCron每秒运行10次。
	 */
	public void setHz(String hz)
	{
		this.hz = hz;
	}

	/**
	 * 的key的数量,以及带有生存期的key的数,平均存活时间
	 */
	public String getKeyspace()
	{
		return keyspace;
	}

	/**
	 * 的key的数量,以及带有生存期的key的数,平均存活时间
	 */
	public void setKeyspace(String keyspace)
	{
		this.keyspace = keyspace;
	}

	/**
	 * redis服务器启动总时间，单位是天
	 */
	public String getUptimeInDays()
	{
		return uptimeInDays;
	}

	/**
	 * redis服务器启动总时间，单位是天
	 */
	public void setUptimeInDays(String uptimeInDays)
	{
		this.uptimeInDays = uptimeInDays;
	}

	/**
	 * redis服务器版本
	 */
	public String getRedisVersion()
	{
		return redisVersion;
	}

	/**
	 * redis服务器版本
	 */
	public void setRedisVersion(String redisVersion)
	{
		this.redisVersion = redisVersion;
	}

	/**
	 *  Redis 服务器耗费的系统 CPU
	 */
	public String getUserdCpuSys()
	{
		return userdCpuSys;
	}

	/**
	 *  Redis 服务器耗费的系统 CPU
	 */
	public void setUserdCpuSys(String userdCpuSys)
	{
		this.userdCpuSys = userdCpuSys;
	}


	/**
	 * x.clone() != x
	 */
	public RedisBean clone()
	{
		RedisBean clone = new RedisBean();
		clone.setRecordDate(this.getRecordDate());
		clone.setServerID(this.getServerID());
		clone.setIP(this.getIP());
		clone.setPort(this.getPort());
		clone.setUsedMemoryHuman(this.getUsedMemoryHuman());
		clone.setUsedMemoryPeakHuman(this.getUsedMemoryPeakHuman());
		clone.setUsedMemoryRss(this.getUsedMemoryRss());
		clone.setRdbChangesSinceLastSave(this.getRdbChangesSinceLastSave());
		clone.setRdbDbFilename(this.getRdbDbFilename());
		clone.setAofEnabled(this.getAofEnabled());
		clone.setAofDbFilename(this.getAofDbFilename());
		clone.setConnectedClients(this.getConnectedClients());
		clone.setHz(this.getHz());
		clone.setKeyspace(this.getKeyspace());
		clone.setUptimeInDays(this.getUptimeInDays());
		clone.setRedisVersion(this.getRedisVersion());
		clone.setUserdCpuSys(this.getUserdCpuSys());
		return clone;
	}

	/**
	 * 重置信息
	 */
	public void reset(RedisBean info)
	{
		this.setRecordDate(info.getRecordDate());
		this.setServerID(info.getServerID());
		this.setIP(info.getIP());
		this.setPort(info.getPort());
		this.setUsedMemoryHuman(info.getUsedMemoryHuman());
		this.setUsedMemoryPeakHuman(info.getUsedMemoryPeakHuman());
		this.setUsedMemoryRss(info.getUsedMemoryRss());
		this.setRdbChangesSinceLastSave(info.getRdbChangesSinceLastSave());
		this.setRdbDbFilename(info.getRdbDbFilename());
		this.setAofEnabled(info.getAofEnabled());
		this.setAofDbFilename(info.getAofDbFilename());
		this.setConnectedClients(info.getConnectedClients());
		this.setHz(info.getHz());
		this.setKeyspace(info.getKeyspace());
		this.setUptimeInDays(info.getUptimeInDays());
		this.setRedisVersion(info.getRedisVersion());
		this.setUserdCpuSys(info.getUserdCpuSys());
	}

}