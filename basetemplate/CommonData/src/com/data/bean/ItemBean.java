package com.data.bean;


/**
 * 物品配置表.xls : 物品配置基础表 - t_s_item
 * @author = auto generate code.
 */
public final class ItemBean
{
	/** 物品ID */
	private int itemID;

	/** 名称 */
	private String itemName;

	/** 描述 */
	private String itemDesc;

	/** 图标 */
	private String icon;


	/** 物品ID */
	public int getItemID()
	{
		return itemID;
	}

	/** 物品ID */
	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	/** 名称 */
	public String getItemName()
	{
		return itemName;
	}

	/** 名称 */
	public void setItemName(String itemName)
	{
		if(itemName==null)
			this.itemName = "";
		else
			this.itemName = itemName;
	}

	/** 描述 */
	public String getItemDesc()
	{
		return itemDesc;
	}

	/** 描述 */
	public void setItemDesc(String itemDesc)
	{
		if(itemDesc==null)
			this.itemDesc = "";
		else
			this.itemDesc = itemDesc;
	}

	/** 图标 */
	public String getIcon()
	{
		return icon;
	}

	/** 图标 */
	public void setIcon(String icon)
	{
		if(icon==null)
			this.icon = "";
		else
			this.icon = icon;
	}


	/** x.clone() != x */
	public ItemBean clone()
	{
		ItemBean clone = new ItemBean();
		clone.setItemID(this.getItemID());
		clone.setItemName(this.getItemName());
		clone.setItemDesc(this.getItemDesc());
		clone.setIcon(this.getIcon());
		return clone;
	}

	/** 重置信息 */
	public void reset(ItemBean bean)
	{
		this.setItemID(bean.getItemID());
		this.setItemName(bean.getItemName());
		this.setItemDesc(bean.getItemDesc());
		this.setIcon(bean.getIcon());
	}

}