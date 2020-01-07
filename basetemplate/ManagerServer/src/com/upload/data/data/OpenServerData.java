package com.upload.data.data;

import java.util.Date;

import com.upload.data.cache.ObjectClone;

public class OpenServerData extends ObjectClone
{
    private static final long serialVersionUID = 1L;

    /**
     * 服务器ID
     */
    private int serverID;

    /**
     * 开服时间
     */
    private Date openTime;

    /**
     * 监控服务器ID
     */
    private int monitorServerID;

    /**
     * 监控条件1
     */
    private String monitorCondition1;

    /**
     * 监控条件2
     */
    private String monitorCondition2;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建者
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 启动状态(0:创建;1:成功;2:失败)
     */
    private int openState;

    /**
     * 消息
     */
    private String message;

    public OpenServerData()
    {
        super();
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
        if (serverID != this.serverID)
        {
            this.serverID = serverID;
            setChanged(true);
        }
    }

    /**
     * 开服时间
     */
    public Date getOpenTime()
    {
        return openTime;
    }

    /**
     * 开服时间
     */
    public void setOpenTime(Date openTime)
    {
        if (openTime != null)
        {
            if (!openTime.equals(this.openTime))
            {
                this.openTime = openTime;
                setChanged(true);
            }
        }
        else
        {
            if (openTime != this.openTime)
            {
                this.openTime = openTime;
                setChanged(true);
            }
        }
    }

    /**
     * 监控服务器ID
     */
    public int getMonitorServerID()
    {
        return monitorServerID;
    }

    /**
     * 监控服务器ID
     */
    public void setMonitorServerID(int monitorServerID)
    {
        if (monitorServerID != this.monitorServerID)
        {
            this.monitorServerID = monitorServerID;
            setChanged(true);
        }
    }

    /**
     * 监控条件1
     */
    public String getMonitorCondition1()
    {
        return monitorCondition1;
    }

    /**
     * 监控条件1
     */
    public void setMonitorCondition1(String monitorCondition1)
    {
        if (monitorCondition1 != null)
        {
            if (!monitorCondition1.equals(this.monitorCondition1))
            {
                this.monitorCondition1 = monitorCondition1;
                setChanged(true);
            }
        }
        else
        {
            if (monitorCondition1 != this.monitorCondition1)
            {
                this.monitorCondition1 = monitorCondition1;
                setChanged(true);
            }
        }
    }

    /**
     * 监控条件2
     */
    public String getMonitorCondition2()
    {
        return monitorCondition2;
    }

    /**
     * 监控条件2
     */
    public void setMonitorCondition2(String monitorCondition2)
    {
        if (monitorCondition2 != null)
        {
            if (!monitorCondition2.equals(this.monitorCondition2))
            {
                this.monitorCondition2 = monitorCondition2;
                setChanged(true);
            }
        }
        else
        {
            if (monitorCondition2 != this.monitorCondition2)
            {
                this.monitorCondition2 = monitorCondition2;
                setChanged(true);
            }
        }
    }

    /**
     * 开始时间
     */
    public String getBeginTime()
    {
        return beginTime;
    }

    /**
     * 开始时间
     */
    public void setBeginTime(String beginTime)
    {
        if (beginTime != null)
        {
            if (!beginTime.equals(this.beginTime))
            {
                this.beginTime = beginTime;
                setChanged(true);
            }
        }
        else
        {
            if (beginTime != this.beginTime)
            {
                this.beginTime = beginTime;
                setChanged(true);
            }
        }
    }

    /**
     * 结束时间
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * 结束时间
     */
    public void setEndTime(String endTime)
    {
        if (endTime != null)
        {
            if (!endTime.equals(this.endTime))
            {
                this.endTime = endTime;
                setChanged(true);
            }
        }
        else
        {
            if (endTime != this.endTime)
            {
                this.endTime = endTime;
                setChanged(true);
            }
        }
    }

    /**
     * 创建者
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * 创建者
     */
    public void setOperator(String operator)
    {
        if (operator != null)
        {
            if (!operator.equals(this.operator))
            {
                this.operator = operator;
                setChanged(true);
            }
        }
        else
        {
            if (operator != this.operator)
            {
                this.operator = operator;
                setChanged(true);
            }
        }
    }

    /**
     * 创建时间
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime)
    {
        if (createTime != null)
        {
            if (!createTime.equals(this.createTime))
            {
                this.createTime = createTime;
                setChanged(true);
            }
        }
        else
        {
            if (createTime != this.createTime)
            {
                this.createTime = createTime;
                setChanged(true);
            }
        }
    }

    /**
     * 启动状态(0:创建;1:成功;2:失败)
     */
    public int getOpenState()
    {
        return openState;
    }

    /**
     * 启动状态(0:创建;1:成功;2:失败)
     */
    public void setOpenState(int openState)
    {
        if (openState != this.openState)
        {
            this.openState = openState;
            setChanged(true);
        }
    }

    /**
     * 消息
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * 消息
     */
    public void setMessage(String message)
    {
        if (message != null)
        {
            if (!message.equals(this.message))
            {
                this.message = message;
                setChanged(true);
            }
        }
        else
        {
            if (message != this.message)
            {
                this.message = message;
                setChanged(true);
            }
        }
    }

    /**
     * x.clone() != x
     */
    public OpenServerData clone()
    {
        OpenServerData clone = new OpenServerData();
        clone.setServerID(this.getServerID());
        clone.setOpenTime(this.getOpenTime());
        clone.setMonitorServerID(this.getMonitorServerID());
        clone.setMonitorCondition1(this.getMonitorCondition1());
        clone.setMonitorCondition2(this.getMonitorCondition2());
        clone.setBeginTime(this.getBeginTime());
        clone.setEndTime(this.getEndTime());
        clone.setOperator(this.getOperator());
        clone.setCreateTime(this.getCreateTime());
        clone.setOpenState(this.getOpenState());
        clone.setMessage(this.getMessage());
        return clone;
    }

    /**
     * 重置信息
     */
    public void reset(OpenServerData info)
    {
        this.setServerID(info.getServerID());
        this.setOpenTime(info.getOpenTime());
        this.setMonitorServerID(info.getMonitorServerID());
        this.setMonitorCondition1(info.getMonitorCondition1());
        this.setMonitorCondition2(info.getMonitorCondition2());
        this.setBeginTime(info.getBeginTime());
        this.setEndTime(info.getEndTime());
        this.setOperator(info.getOperator());
        this.setCreateTime(info.getCreateTime());
        this.setOpenState(info.getOpenState());
        this.setMessage(info.getMessage());
    }

    private Date nextDate;

    public void setNextDate(Date date)
    {
        this.nextDate = date;
    }

    public Date getNextDate()
    {
        return nextDate;
    }

}
