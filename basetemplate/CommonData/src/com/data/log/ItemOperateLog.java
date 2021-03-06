package com.data.log;

import java.util.Date;

import com.base.data.ChangedObject;

public final class ItemOperateLog extends ChangedObject
{
    /**
     * 道具出售记录ID
     */
    private int itemOperateID;

    /**
     * 玩家ID
     */
    private long userID;

    /**
     * 角色名称
     */
    private String nickName;

    /**
     * 道具ID
     */
    private int itemID;

    /**
     * 道具名称
     */
    private String itemName;

    /**
     * 操作数量
     */
    private int opCount;

    /**
     * 操作类型（来源去处）
     */
    private int opType;

    /**
     * 操作时间
     */
    private Date opTime;

    /**
     * 是否是收益类型（true：收益；false：消耗）
     */
    private boolean isProfit;

    public ItemOperateLog()
    {
        super();
    }

    /**
     * 道具出售记录ID
     */
    public int getItemOperateID()
    {
        return itemOperateID;
    }

    /**
     * 道具出售记录ID
     */
    public void setItemOperateID(int itemOperateID)
    {
        if (itemOperateID != this.itemOperateID)
        {
            this.itemOperateID = itemOperateID;
            setChanged(true);
        }
    }

    /**
     * 玩家ID
     */
    public long getUserID()
    {
        return userID;
    }

    /**
     * 玩家ID
     */
    public void setUserID(long userID)
    {
        if (userID != this.userID)
        {
            this.userID = userID;
            setChanged(true);
        }
    }

    /**
     * 角色名称
     */
    public String getNickName()
    {
        return nickName;
    }

    /**
     * 角色名称
     */
    public void setNickName(String nickName)
    {
        if (nickName != null)
        {
            if (!nickName.equals(this.nickName))
            {
                this.nickName = nickName;
                setChanged(true);
            }
        }
        else
        {
            if (nickName != this.nickName)
            {
                this.nickName = nickName;
                setChanged(true);
            }
        }
    }

    /**
     * 道具ID
     */
    public int getItemID()
    {
        return itemID;
    }

    /**
     * 道具ID
     */
    public void setItemID(int itemID)
    {
        if (itemID != this.itemID)
        {
            this.itemID = itemID;
            setChanged(true);
        }
    }

    /**
     * 道具名称
     */
    public String getItemName()
    {
        return itemName;
    }

    /**
     * 道具名称
     */
    public void setItemName(String itemName)
    {
        if (itemName != null)
        {
            if (!itemName.equals(this.itemName))
            {
                this.itemName = itemName;
                setChanged(true);
            }
        }
        else
        {
            if (itemName != this.itemName)
            {
                this.itemName = itemName;
                setChanged(true);
            }
        }
    }

    /**
     * 操作数量
     */
    public int getOpCount()
    {
        return opCount;
    }

    /**
     * 操作数量
     */
    public void setOpCount(int opCount)
    {
        if (opCount != this.opCount)
        {
            this.opCount = opCount;
            setChanged(true);
        }
    }

    /**
     * 操作类型（来源去处）
     */
    public int getOpType()
    {
        return opType;
    }

    /**
     * 操作类型（来源去处）
     */
    public void setOpType(int opType)
    {
        if (opType != this.opType)
        {
            this.opType = opType;
            setChanged(true);
        }
    }

    /**
     * 操作时间
     */
    public Date getOpTime()
    {
        return opTime;
    }

    /**
     * 操作时间
     */
    public void setOpTime(Date opTime)
    {
        if (opTime != null)
        {
            if (!opTime.equals(this.opTime))
            {
                this.opTime = opTime;
                setChanged(true);
            }
        }
        else
        {
            if (opTime != this.opTime)
            {
                this.opTime = opTime;
                setChanged(true);
            }
        }
    }

    /**
     * 是否是收益类型（true：收益；false：消耗）
     */
    public boolean getIsProfit()
    {
        return isProfit;
    }

    /**
     * 是否是收益类型（true：收益；false：消耗）
     */
    public void setIsProfit(boolean isProfit)
    {
        if (isProfit != this.isProfit)
        {
            this.isProfit = isProfit;
            setChanged(true);
        }
    }

    /**
     * x.clone() != x
     */
    public ItemOperateLog clone()
    {
        ItemOperateLog clone = new ItemOperateLog();
        clone.setItemOperateID(this.getItemOperateID());
        clone.setUserID(this.getUserID());
        clone.setNickName(this.getNickName());
        clone.setItemID(this.getItemID());
        clone.setItemName(this.getItemName());
        clone.setOpCount(this.getOpCount());
        clone.setOpType(this.getOpType());
        clone.setOpTime(this.getOpTime());
        clone.setIsProfit(this.getIsProfit());
        return clone;
    }

    /**
     * 重置信息
     */
    public void reset(ItemOperateLog info)
    {
        this.setItemOperateID(info.getItemOperateID());
        this.setUserID(info.getUserID());
        this.setNickName(info.getNickName());
        this.setItemID(info.getItemID());
        this.setItemName(info.getItemName());
        this.setOpCount(info.getOpCount());
        this.setOpType(info.getOpType());
        this.setOpTime(info.getOpTime());
        this.setIsProfit(info.getIsProfit());
    }

}
