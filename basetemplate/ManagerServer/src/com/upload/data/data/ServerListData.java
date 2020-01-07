package com.upload.data.data;

import java.util.Date;

import com.upload.data.cache.ObjectClone;

public class ServerListData extends ObjectClone
{
    private static final long serialVersionUID = 1L;

    /**
     * 服务器逻辑ID
     */
    private int serverID;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器组
     */
    private int serverGroup;

    /**
     * 外网状态 1：待开启；2：维护；3：新服；4：流畅；5：火爆；
     */
    private int outState;

    /**
     * 服务器逻辑地址
     */
    private String serverIp;

    /**
     * 服务器类型（1-Game，2-Account，3-SDK,4-Cross，5-活动）
     */
    private int type;

    /**
     * 游戏端口
     */
    private int gamePort;

    /**
     * 网关端口
     */
    private int gatePort;

    /**
     * 服务器状态（0-开启，1-关闭，2-异常）
     */
    private int status;

    /**
     * 网关服状态（0-开启，1-关闭，2-异常）
     */
    private int gateStatus;

    /**
     * 服务器部署路径（游戏服、账号服、跨服、充值服等不同）
     */
    private String serverAddress;

    /**
     * 网关部署路径
     */
    private String gateAddress;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

    /**
     * 开服时间
     */
    private Date lastOpenTime;

    /**
     * 数据库账号
     */
    private String dbUsername;

    /**
     * 数据库密码
     */
    private String dbPassword;

    /**
     * 数据库IP地址
     */
    private String dbIp;

    /**
     * 数据库对应端口
     */
    private String dbPort;

    /**
     * 游戏库名称
     */
    private String dbGameName;

    /**
     * 日志库名称
     */
    private String dbLogName;

    /**
     * 统计库名称
     */
    private String dbMartName;

    /**
     * 数据库部署路径
     */
    private String dbAddress;

    /**
     * 数据库启动脚本路径
     */
    private String dbStartAddress;

    /**
     * SSH帐户名
     */
    private String sSHUsername;

    /**
     * SSH密码
     */
    private String sSHPassword;

    /**
     * SSH密码
     */
    private int sSHPort;

    /**
     * SSH秘钥路径
     */
    private String sSHKeyPath;

    /**
     * SSH秘钥密码
     */
    private String sSHKeyPassword;

    /**
     * tmux
     */
    private String tmux;

    /**
     * 备注
     */
    private String remarks;

    /**
     * redis缓存IP地址
     */
    private String redisIp;

    /**
     * redis缓存对应端口
     */
    private String redisPort;

    /**
     * redis缓存部署路径
     */
    private String redisAddress;

    /**
     * redis启动脚本路径
     */
    private String redisStartAddress;

    public ServerListData()
    {
        super();
    }

    /**
     * 服务器逻辑ID
     */
    public int getServerID()
    {
        return serverID;
    }

    /**
     * 服务器逻辑ID
     */
    public void setServerID(int serverID)
    {
        if (serverID != this.serverID)
        {
            this.serverID = serverID;
            setChanged(true);
        }
    }

    /**
     * 服务器名称
     */
    public String getServerName()
    {
        return serverName;
    }

    /**
     * 服务器名称
     */
    public void setServerName(String serverName)
    {
        if (serverName != null)
        {
            if (!serverName.equals(this.serverName))
            {
                this.serverName = serverName;
                setChanged(true);
            }
        }
        else
        {
            if (serverName != this.serverName)
            {
                this.serverName = serverName;
                setChanged(true);
            }
        }
    }

    /**
     * 服务器组
     */
    public int getServerGroup()
    {
        return serverGroup;
    }

    /**
     * 服务器组
     */
    public void setServerGroup(int serverGroup)
    {
        if (serverGroup != this.serverGroup)
        {
            this.serverGroup = serverGroup;
            setChanged(true);
        }
    }

    /**
     * 外网状态 1：待开启；2：维护；3：新服；4：流畅；5：火爆；
     */
    public int getOutState()
    {
        return outState;
    }

    /**
     * 外网状态
     */
    public void setOutState(int outState)
    {
        if (outState != this.outState)
        {
            this.outState = outState;
            setChanged(true);
        }
    }

    /**
     * 服务器逻辑地址
     */
    public String getServerIp()
    {
        return serverIp;
    }

    /**
     * 服务器逻辑地址
     */
    public void setServerIp(String serverIp)
    {
        if (serverIp != null)
        {
            if (!serverIp.equals(this.serverIp))
            {
                this.serverIp = serverIp;
                setChanged(true);
            }
        }
        else
        {
            if (serverIp != this.serverIp)
            {
                this.serverIp = serverIp;
                setChanged(true);
            }
        }
    }

    /**
     * 服务器类型（1-Game，2-Account，3-SDK,4-Cross，5-活动）
     */
    public int getType()
    {
        return type;
    }

    /**
     * 服务器类型（1-Game，2-Account，3-SDK,4-Cross，5-活动）
     */
    public void setType(int type)
    {
        if (type != this.type)
        {
            this.type = type;
            setChanged(true);
        }
    }

    /**
     * 游戏端口
     */
    public int getGamePort()
    {
        return gamePort;
    }

    /**
     * 游戏端口
     */
    public void setGamePort(int gamePort)
    {
        if (gamePort != this.gamePort)
        {
            this.gamePort = gamePort;
            setChanged(true);
        }
    }

    /**
     * 网关端口
     */
    public int getGatePort()
    {
        return gatePort;
    }

    /**
     * 网关端口
     */
    public void setGatePort(int gatePort)
    {
        if (gatePort != this.gatePort)
        {
            this.gatePort = gatePort;
            setChanged(true);
        }
    }

    /**
     * 服务器状态（0-开启，1-关闭，2-异常）
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * 服务器状态（0-开启，1-关闭，2-异常）
     */
    public void setStatus(int status)
    {
        if (status != this.status)
        {
            this.status = status;
            setChanged(true);
        }
    }

    /**
     * 网关服状态（0-开启，1-关闭，2-异常）
     */
    public int getGateStatus()
    {
        return gateStatus;
    }

    /**
     * 网关服状态（0-开启，1-关闭，2-异常）
     */
    public void setGateStatus(int gateStatus)
    {
        if (gateStatus != this.gateStatus)
        {
            this.gateStatus = gateStatus;
            setChanged(true);
        }
    }

    /**
     * 服务器部署路径（游戏服、账号服、跨服、充值服等不同）
     */
    public String getServerAddress()
    {
        return serverAddress;
    }

    /**
     * 服务器部署路径（游戏服、账号服、跨服、充值服等不同）
     */
    public void setServerAddress(String serverAddress)
    {
        if (serverAddress != null)
        {
            if (!serverAddress.equals(this.serverAddress))
            {
                this.serverAddress = serverAddress;
                setChanged(true);
            }
        }
        else
        {
            if (serverAddress != this.serverAddress)
            {
                this.serverAddress = serverAddress;
                setChanged(true);
            }
        }
    }

    /**
     * 网关部署路径
     */
    public String getGateAddress()
    {
        return gateAddress;
    }

    /**
     * 网关部署路径
     */
    public void setGateAddress(String gateAddress)
    {
        if (gateAddress != null)
        {
            if (!gateAddress.equals(this.gateAddress))
            {
                this.gateAddress = gateAddress;
                setChanged(true);
            }
        }
        else
        {
            if (gateAddress != this.gateAddress)
            {
                this.gateAddress = gateAddress;
                setChanged(true);
            }
        }
    }

    /**
     * 上次更新时间
     */
    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    /**
     * 上次更新时间
     */
    public void setLastUpdateTime(Date lastUpdateTime)
    {
        if (lastUpdateTime != null)
        {
            if (!lastUpdateTime.equals(this.lastUpdateTime))
            {
                this.lastUpdateTime = lastUpdateTime;
                setChanged(true);
            }
        }
        else
        {
            if (lastUpdateTime != this.lastUpdateTime)
            {
                this.lastUpdateTime = lastUpdateTime;
                setChanged(true);
            }
        }
    }

    /**
     * 开服时间
     */
    public Date getLastOpenTime()
    {
        return lastOpenTime;
    }

    /**
     * 开服时间
     */
    public void setLastOpenTime(Date lastOpenTime)
    {
        if (lastOpenTime != null)
        {
            if (!lastOpenTime.equals(this.lastOpenTime))
            {
                this.lastOpenTime = lastOpenTime;
                setChanged(true);
            }
        }
        else
        {
            if (lastOpenTime != this.lastOpenTime)
            {
                this.lastOpenTime = lastOpenTime;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库账号
     */
    public String getDbUsername()
    {
        return dbUsername;
    }

    /**
     * 数据库账号
     */
    public void setDbUsername(String dbUsername)
    {
        if (dbUsername != null)
        {
            if (!dbUsername.equals(this.dbUsername))
            {
                this.dbUsername = dbUsername;
                setChanged(true);
            }
        }
        else
        {
            if (dbUsername != this.dbUsername)
            {
                this.dbUsername = dbUsername;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库密码
     */
    public String getDbPassword()
    {
        return dbPassword;
    }

    /**
     * 数据库密码
     */
    public void setDbPassword(String dbPassword)
    {
        if (dbPassword != null)
        {
            if (!dbPassword.equals(this.dbPassword))
            {
                this.dbPassword = dbPassword;
                setChanged(true);
            }
        }
        else
        {
            if (dbPassword != this.dbPassword)
            {
                this.dbPassword = dbPassword;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库IP地址
     */
    public String getDbIp()
    {
        return dbIp;
    }

    /**
     * 数据库IP地址
     */
    public void setDbIp(String dbIp)
    {
        if (dbIp != null)
        {
            if (!dbIp.equals(this.dbIp))
            {
                this.dbIp = dbIp;
                setChanged(true);
            }
        }
        else
        {
            if (dbIp != this.dbIp)
            {
                this.dbIp = dbIp;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库对应端口
     */
    public String getDbPort()
    {
        return dbPort;
    }

    /**
     * 数据库对应端口
     */
    public void setDbPort(String dbPort)
    {
        if (dbPort != null)
        {
            if (!dbPort.equals(this.dbPort))
            {
                this.dbPort = dbPort;
                setChanged(true);
            }
        }
        else
        {
            if (dbPort != this.dbPort)
            {
                this.dbPort = dbPort;
                setChanged(true);
            }
        }
    }

    /**
     * 游戏库名称
     */
    public String getDbGameName()
    {
        return dbGameName;
    }

    /**
     * 游戏库名称
     */
    public void setDbGameName(String dbGameName)
    {
        if (dbGameName != null)
        {
            if (!dbGameName.equals(this.dbGameName))
            {
                this.dbGameName = dbGameName;
                setChanged(true);
            }
        }
        else
        {
            if (dbGameName != this.dbGameName)
            {
                this.dbGameName = dbGameName;
                setChanged(true);
            }
        }
    }

    /**
     * 日志库名称
     */
    public String getDbLogName()
    {
        return dbLogName;
    }

    /**
     * 日志库名称
     */
    public void setDbLogName(String dbLogName)
    {
        if (dbLogName != null)
        {
            if (!dbLogName.equals(this.dbLogName))
            {
                this.dbLogName = dbLogName;
                setChanged(true);
            }
        }
        else
        {
            if (dbLogName != this.dbLogName)
            {
                this.dbLogName = dbLogName;
                setChanged(true);
            }
        }
    }

    /**
     * 统计库名称
     */
    public String getDbMartName()
    {
        return dbMartName;
    }

    /**
     * 统计库名称
     */
    public void setDbMartName(String dbMartName)
    {
        if (dbMartName != null)
        {
            if (!dbMartName.equals(this.dbMartName))
            {
                this.dbMartName = dbMartName;
                setChanged(true);
            }
        }
        else
        {
            if (dbMartName != this.dbMartName)
            {
                this.dbMartName = dbMartName;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库部署路径
     */
    public String getDbAddress()
    {
        return dbAddress;
    }

    /**
     * 数据库部署路径
     */
    public void setDbAddress(String dbAddress)
    {
        if (dbAddress != null)
        {
            if (!dbAddress.equals(this.dbAddress))
            {
                this.dbAddress = dbAddress;
                setChanged(true);
            }
        }
        else
        {
            if (dbAddress != this.dbAddress)
            {
                this.dbAddress = dbAddress;
                setChanged(true);
            }
        }
    }

    /**
     * 数据库启动脚本路径
     */
    public String getDbStartAddress()
    {
        return dbStartAddress;
    }

    /**
     * 数据库启动脚本路径
     */
    public void setDbStartAddress(String dbStartAddress)
    {
        if (dbStartAddress != null)
        {
            if (!dbStartAddress.equals(this.dbStartAddress))
            {
                this.dbStartAddress = dbStartAddress;
                setChanged(true);
            }
        }
        else
        {
            if (dbStartAddress != this.dbStartAddress)
            {
                this.dbStartAddress = dbStartAddress;
                setChanged(true);
            }
        }
    }

    /**
     * SSH帐户名
     */
    public String getSSHUsername()
    {
        return sSHUsername;
    }

    /**
     * SSH帐户名
     */
    public void setSSHUsername(String sSHUsername)
    {
        if (sSHUsername != null)
        {
            if (!sSHUsername.equals(this.sSHUsername))
            {
                this.sSHUsername = sSHUsername;
                setChanged(true);
            }
        }
        else
        {
            if (sSHUsername != this.sSHUsername)
            {
                this.sSHUsername = sSHUsername;
                setChanged(true);
            }
        }
    }

    /**
     * SSH密码
     */
    public String getSSHPassword()
    {
        return sSHPassword;
    }

    /**
     * SSH密码
     */
    public void setSSHPassword(String sSHPassword)
    {
        if (sSHPassword != null)
        {
            if (!sSHPassword.equals(this.sSHPassword))
            {
                this.sSHPassword = sSHPassword;
                setChanged(true);
            }
        }
        else
        {
            if (sSHPassword != this.sSHPassword)
            {
                this.sSHPassword = sSHPassword;
                setChanged(true);
            }
        }
    }

    /**
     * SSH密码
     */
    public int getSSHPort()
    {
        return sSHPort;
    }

    /**
     * SSH密码
     */
    public void setSSHPort(int sSHPort)
    {
        if (sSHPort != this.sSHPort)
        {
            this.sSHPort = sSHPort;
            setChanged(true);
        }
    }

    /**
     * SSH秘钥路径
     */
    public String getSSHKeyPath()
    {
        return sSHKeyPath;
    }

    /**
     * SSH秘钥路径
     */
    public void setSSHKeyPath(String sSHKeyPath)
    {
        if (sSHKeyPath != null)
        {
            if (!sSHKeyPath.equals(this.sSHKeyPath))
            {
                this.sSHKeyPath = sSHKeyPath;
                setChanged(true);
            }
        }
        else
        {
            if (sSHKeyPath != this.sSHKeyPath)
            {
                this.sSHKeyPath = sSHKeyPath;
                setChanged(true);
            }
        }
    }

    /**
     * SSH秘钥密码
     */
    public String getSSHKeyPassword()
    {
        return sSHKeyPassword;
    }

    /**
     * SSH秘钥密码
     */
    public void setSSHKeyPassword(String sSHKeyPassword)
    {
        if (sSHKeyPassword != null)
        {
            if (!sSHKeyPassword.equals(this.sSHKeyPassword))
            {
                this.sSHKeyPassword = sSHKeyPassword;
                setChanged(true);
            }
        }
        else
        {
            if (sSHKeyPassword != this.sSHKeyPassword)
            {
                this.sSHKeyPassword = sSHKeyPassword;
                setChanged(true);
            }
        }
    }

    /**
     * tmux
     */
    public String getTmux()
    {
        return tmux;
    }

    /**
     * tmux
     */
    public void setTmux(String tmux)
    {
        if (tmux != null)
        {
            if (!tmux.equals(this.tmux))
            {
                this.tmux = tmux;
                setChanged(true);
            }
        }
        else
        {
            if (tmux != this.tmux)
            {
                this.tmux = tmux;
                setChanged(true);
            }
        }
    }

    /**
     * 备注
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * 备注
     */
    public void setRemarks(String remarks)
    {
        if (remarks != null)
        {
            if (!remarks.equals(this.remarks))
            {
                this.remarks = remarks;
                setChanged(true);
            }
        }
        else
        {
            if (remarks != this.remarks)
            {
                this.remarks = remarks;
                setChanged(true);
            }
        }
    }

    /**
     * redis缓存IP地址
     */
    public String getRedisIp()
    {
        return redisIp;
    }

    /**
     * redis缓存IP地址
     */
    public void setRedisIp(String redisIp)
    {
        if (redisIp != null)
        {
            if (!redisIp.equals(this.redisIp))
            {
                this.redisIp = redisIp;
                setChanged(true);
            }
        }
        else
        {
            if (redisIp != this.redisIp)
            {
                this.redisIp = redisIp;
                setChanged(true);
            }
        }
    }

    /**
     * redis缓存对应端口
     */
    public String getRedisPort()
    {
        return redisPort;
    }

    /**
     * redis缓存对应端口
     */
    public void setRedisPort(String redisPort)
    {
        if (redisPort != null)
        {
            if (!redisPort.equals(this.redisPort))
            {
                this.redisPort = redisPort;
                setChanged(true);
            }
        }
        else
        {
            if (redisPort != this.redisPort)
            {
                this.redisPort = redisPort;
                setChanged(true);
            }
        }
    }

    /**
     * redis缓存部署路径
     */
    public String getRedisAddress()
    {
        return redisAddress;
    }

    /**
     * redis缓存部署路径
     */
    public void setRedisAddress(String redisAddress)
    {
        if (redisAddress != null)
        {
            if (!redisAddress.equals(this.redisAddress))
            {
                this.redisAddress = redisAddress;
                setChanged(true);
            }
        }
        else
        {
            if (redisAddress != this.redisAddress)
            {
                this.redisAddress = redisAddress;
                setChanged(true);
            }
        }
    }

    /**
     * redis启动脚本路径
     */
    public String getRedisStartAddress()
    {
        return redisStartAddress;
    }

    /**
     * redis启动脚本路径
     */
    public void setRedisStartAddress(String redisStartAddress)
    {
        if (redisStartAddress != null)
        {
            if (!redisStartAddress.equals(this.redisStartAddress))
            {
                this.redisStartAddress = redisStartAddress;
                setChanged(true);
            }
        }
        else
        {
            if (redisStartAddress != this.redisStartAddress)
            {
                this.redisStartAddress = redisStartAddress;
                setChanged(true);
            }
        }
    }

    /**
     * x.clone() != x
     */
    public ServerListData clone()
    {
        ServerListData clone = new ServerListData();
        clone.setServerID(this.getServerID());
        clone.setServerName(this.getServerName());
        clone.setServerGroup(this.getServerGroup());
        clone.setOutState(this.getOutState());
        clone.setServerIp(this.getServerIp());
        clone.setType(this.getType());
        clone.setGamePort(this.getGamePort());
        clone.setGatePort(this.getGatePort());
        clone.setStatus(this.getStatus());
        clone.setGateStatus(this.getGateStatus());
        clone.setServerAddress(this.getServerAddress());
        clone.setGateAddress(this.getGateAddress());
        clone.setLastUpdateTime(this.getLastUpdateTime());
        clone.setLastOpenTime(this.getLastOpenTime());
        clone.setDbUsername(this.getDbUsername());
        clone.setDbPassword(this.getDbPassword());
        clone.setDbIp(this.getDbIp());
        clone.setDbPort(this.getDbPort());
        clone.setDbGameName(this.getDbGameName());
        clone.setDbLogName(this.getDbLogName());
        clone.setDbMartName(this.getDbMartName());
        clone.setDbAddress(this.getDbAddress());
        clone.setDbStartAddress(this.getDbStartAddress());
        clone.setSSHUsername(this.getSSHUsername());
        clone.setSSHPassword(this.getSSHPassword());
        clone.setSSHPort(this.getSSHPort());
        clone.setSSHKeyPath(this.getSSHKeyPath());
        clone.setSSHKeyPassword(this.getSSHKeyPassword());
        clone.setTmux(this.getTmux());
        clone.setRemarks(this.getRemarks());
        clone.setRedisIp(this.getRedisIp());
        clone.setRedisPort(this.getRedisPort());
        clone.setRedisAddress(this.getRedisAddress());
        clone.setRedisStartAddress(this.getRedisStartAddress());
        return clone;
    }

    /**
     * 重置信息
     */
    public void reset(ServerListData info)
    {
        this.setServerID(info.getServerID());
        this.setServerName(info.getServerName());
        this.setServerGroup(info.getServerGroup());
        this.setOutState(info.getOutState());
        this.setServerIp(info.getServerIp());
        this.setType(info.getType());
        this.setGamePort(info.getGamePort());
        this.setGatePort(info.getGatePort());
        this.setStatus(info.getStatus());
        this.setGateStatus(info.getGateStatus());
        this.setServerAddress(info.getServerAddress());
        this.setGateAddress(info.getGateAddress());
        this.setLastUpdateTime(info.getLastUpdateTime());
        this.setLastOpenTime(info.getLastOpenTime());
        this.setDbUsername(info.getDbUsername());
        this.setDbPassword(info.getDbPassword());
        this.setDbIp(info.getDbIp());
        this.setDbPort(info.getDbPort());
        this.setDbGameName(info.getDbGameName());
        this.setDbLogName(info.getDbLogName());
        this.setDbMartName(info.getDbMartName());
        this.setDbAddress(info.getDbAddress());
        this.setDbStartAddress(info.getDbStartAddress());
        this.setSSHUsername(info.getSSHUsername());
        this.setSSHPassword(info.getSSHPassword());
        this.setSSHPort(info.getSSHPort());
        this.setSSHKeyPath(info.getSSHKeyPath());
        this.setSSHKeyPassword(info.getSSHKeyPassword());
        this.setTmux(info.getTmux());
        this.setRemarks(info.getRemarks());
        this.setRedisIp(info.getRedisIp());
        this.setRedisPort(info.getRedisPort());
        this.setRedisAddress(info.getRedisAddress());
        this.setRedisStartAddress(info.getRedisStartAddress());
    }

}
