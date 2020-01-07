package com.data.bean;


/**
 * 广告配置表.xls : 去广告配置表 - t_s_advertise
 * @author = auto generate code.
 */
public final class AdvertiseBean
{
	/** 配置的ID */
	private int configID;

	/** 去除月数 */
	private int monthCount;

	/** 展示文本 */
	private String desc;

	/** 消耗物品ID */
	private int consumeID;

	/** 消耗物品数 */
	private int consumeCount;

	/** 显示的图标资源 */
	private String iconResource;

	/** 优惠力度 */
	private int freePercent;


	/** 配置的ID */
	public int getConfigID()
	{
		return configID;
	}

	/** 配置的ID */
	public void setConfigID(int configID)
	{
		this.configID = configID;
	}

	/** 去除月数 */
	public int getMonthCount()
	{
		return monthCount;
	}

	/** 去除月数 */
	public void setMonthCount(int monthCount)
	{
		this.monthCount = monthCount;
	}

	/** 展示文本 */
	public String getDesc()
	{
		return desc;
	}

	/** 展示文本 */
	public void setDesc(String desc)
	{
		if(desc==null)
			this.desc = "";
		else
			this.desc = desc;
	}

	/** 消耗物品ID */
	public int getConsumeID()
	{
		return consumeID;
	}

	/** 消耗物品ID */
	public void setConsumeID(int consumeID)
	{
		this.consumeID = consumeID;
	}

	/** 消耗物品数 */
	public int getConsumeCount()
	{
		return consumeCount;
	}

	/** 消耗物品数 */
	public void setConsumeCount(int consumeCount)
	{
		this.consumeCount = consumeCount;
	}

	/** 显示的图标资源 */
	public String getIconResource()
	{
		return iconResource;
	}

	/** 显示的图标资源 */
	public void setIconResource(String iconResource)
	{
		if(iconResource==null)
			this.iconResource = "";
		else
			this.iconResource = iconResource;
	}

	/** 优惠力度 */
	public int getFreePercent()
	{
		return freePercent;
	}

	/** 优惠力度 */
	public void setFreePercent(int freePercent)
	{
		this.freePercent = freePercent;
	}


	/** x.clone() != x */
	public AdvertiseBean clone()
	{
		AdvertiseBean clone = new AdvertiseBean();
		clone.setConfigID(this.getConfigID());
		clone.setMonthCount(this.getMonthCount());
		clone.setDesc(this.getDesc());
		clone.setConsumeID(this.getConsumeID());
		clone.setConsumeCount(this.getConsumeCount());
		clone.setIconResource(this.getIconResource());
		clone.setFreePercent(this.getFreePercent());
		return clone;
	}

	/** 重置信息 */
	public void reset(AdvertiseBean bean)
	{
		this.setConfigID(bean.getConfigID());
		this.setMonthCount(bean.getMonthCount());
		this.setDesc(bean.getDesc());
		this.setConsumeID(bean.getConsumeID());
		this.setConsumeCount(bean.getConsumeCount());
		this.setIconResource(bean.getIconResource());
		this.setFreePercent(bean.getFreePercent());
	}

}