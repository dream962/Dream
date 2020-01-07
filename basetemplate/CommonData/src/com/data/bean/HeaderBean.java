package com.data.bean;


/**
 * 头像配置表.xls : 装备配置表 - t_s_header
 * @author = auto generate code.
 */
public final class HeaderBean
{
	/** 头像ID */
	private int headID;

	/** 头像资源 */
	private String headIcon;

	/** 解锁消耗物品ID */
	private int consumeItemID;

	/** 解锁消耗物品个数 */
	private int consumeItemCount;


	/** 头像ID */
	public int getHeadID()
	{
		return headID;
	}

	/** 头像ID */
	public void setHeadID(int headID)
	{
		this.headID = headID;
	}

	/** 头像资源 */
	public String getHeadIcon()
	{
		return headIcon;
	}

	/** 头像资源 */
	public void setHeadIcon(String headIcon)
	{
		if(headIcon==null)
			this.headIcon = "";
		else
			this.headIcon = headIcon;
	}

	/** 解锁消耗物品ID */
	public int getConsumeItemID()
	{
		return consumeItemID;
	}

	/** 解锁消耗物品ID */
	public void setConsumeItemID(int consumeItemID)
	{
		this.consumeItemID = consumeItemID;
	}

	/** 解锁消耗物品个数 */
	public int getConsumeItemCount()
	{
		return consumeItemCount;
	}

	/** 解锁消耗物品个数 */
	public void setConsumeItemCount(int consumeItemCount)
	{
		this.consumeItemCount = consumeItemCount;
	}


	/** x.clone() != x */
	public HeaderBean clone()
	{
		HeaderBean clone = new HeaderBean();
		clone.setHeadID(this.getHeadID());
		clone.setHeadIcon(this.getHeadIcon());
		clone.setConsumeItemID(this.getConsumeItemID());
		clone.setConsumeItemCount(this.getConsumeItemCount());
		return clone;
	}

	/** 重置信息 */
	public void reset(HeaderBean bean)
	{
		this.setHeadID(bean.getHeadID());
		this.setHeadIcon(bean.getHeadIcon());
		this.setConsumeItemID(bean.getConsumeItemID());
		this.setConsumeItemCount(bean.getConsumeItemCount());
	}

}