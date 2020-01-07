package com.data.bean;


/**
 * 充值配置表.xls : 兑换配置表 - t_s_exchange
 * @author = auto generate code.
 */
public final class ExchangeBean
{
	/** 配置的ID */
	private int configID;

	/** 兑换的目标物体ID */
	private int targetItemID;

	/** 兑换的目标物体个数 */
	private int targetItemCount;

	/** 消耗的物品ID */
	private int consumeItemID;

	/** 消耗的物品个数 */
	private int consumeItemCount;

	/** 显示的图标资源 */
	private String iconResource;

	/** 优惠力度 */
	private int freePercent;

	/** 商品特性 */
	private int itemPeculiarity;


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

	/** 兑换的目标物体ID */
	public int getTargetItemID()
	{
		return targetItemID;
	}

	/** 兑换的目标物体ID */
	public void setTargetItemID(int targetItemID)
	{
		this.targetItemID = targetItemID;
	}

	/** 兑换的目标物体个数 */
	public int getTargetItemCount()
	{
		return targetItemCount;
	}

	/** 兑换的目标物体个数 */
	public void setTargetItemCount(int targetItemCount)
	{
		this.targetItemCount = targetItemCount;
	}

	/** 消耗的物品ID */
	public int getConsumeItemID()
	{
		return consumeItemID;
	}

	/** 消耗的物品ID */
	public void setConsumeItemID(int consumeItemID)
	{
		this.consumeItemID = consumeItemID;
	}

	/** 消耗的物品个数 */
	public int getConsumeItemCount()
	{
		return consumeItemCount;
	}

	/** 消耗的物品个数 */
	public void setConsumeItemCount(int consumeItemCount)
	{
		this.consumeItemCount = consumeItemCount;
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

	/** 商品特性 */
	public int getItemPeculiarity()
	{
		return itemPeculiarity;
	}

	/** 商品特性 */
	public void setItemPeculiarity(int itemPeculiarity)
	{
		this.itemPeculiarity = itemPeculiarity;
	}


	/** x.clone() != x */
	public ExchangeBean clone()
	{
		ExchangeBean clone = new ExchangeBean();
		clone.setConfigID(this.getConfigID());
		clone.setTargetItemID(this.getTargetItemID());
		clone.setTargetItemCount(this.getTargetItemCount());
		clone.setConsumeItemID(this.getConsumeItemID());
		clone.setConsumeItemCount(this.getConsumeItemCount());
		clone.setIconResource(this.getIconResource());
		clone.setFreePercent(this.getFreePercent());
		clone.setItemPeculiarity(this.getItemPeculiarity());
		return clone;
	}

	/** 重置信息 */
	public void reset(ExchangeBean bean)
	{
		this.setConfigID(bean.getConfigID());
		this.setTargetItemID(bean.getTargetItemID());
		this.setTargetItemCount(bean.getTargetItemCount());
		this.setConsumeItemID(bean.getConsumeItemID());
		this.setConsumeItemCount(bean.getConsumeItemCount());
		this.setIconResource(bean.getIconResource());
		this.setFreePercent(bean.getFreePercent());
		this.setItemPeculiarity(bean.getItemPeculiarity());
	}

}