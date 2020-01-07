package com.data.bean;


/**
 * 角色配置表.xls : 地块配置表 - t_s_map
 * @author = auto generate code.
 */
public final class MapBean
{
	/** 地块类型ID */
	private int objectID;

	/** 类型名称 */
	private String typeName;

	/** 头像Icon */
	private String icon;

	/** 描述说明 */
	private String detail;

	/** 解锁消耗的道具ID(小于0就是货币) */
	private int itemID;

	/** 解锁消耗甜甜圈数量 */
	private int count;


	/** 地块类型ID */
	public int getObjectID()
	{
		return objectID;
	}

	/** 地块类型ID */
	public void setObjectID(int objectID)
	{
		this.objectID = objectID;
	}

	/** 类型名称 */
	public String getTypeName()
	{
		return typeName;
	}

	/** 类型名称 */
	public void setTypeName(String typeName)
	{
		if(typeName==null)
			this.typeName = "";
		else
			this.typeName = typeName;
	}

	/** 头像Icon */
	public String getIcon()
	{
		return icon;
	}

	/** 头像Icon */
	public void setIcon(String icon)
	{
		if(icon==null)
			this.icon = "";
		else
			this.icon = icon;
	}

	/** 描述说明 */
	public String getDetail()
	{
		return detail;
	}

	/** 描述说明 */
	public void setDetail(String detail)
	{
		if(detail==null)
			this.detail = "";
		else
			this.detail = detail;
	}

	/** 解锁消耗的道具ID(小于0就是货币) */
	public int getItemID()
	{
		return itemID;
	}

	/** 解锁消耗的道具ID(小于0就是货币) */
	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	/** 解锁消耗甜甜圈数量 */
	public int getCount()
	{
		return count;
	}

	/** 解锁消耗甜甜圈数量 */
	public void setCount(int count)
	{
		this.count = count;
	}


	/** x.clone() != x */
	public MapBean clone()
	{
		MapBean clone = new MapBean();
		clone.setObjectID(this.getObjectID());
		clone.setTypeName(this.getTypeName());
		clone.setIcon(this.getIcon());
		clone.setDetail(this.getDetail());
		clone.setItemID(this.getItemID());
		clone.setCount(this.getCount());
		return clone;
	}

	/** 重置信息 */
	public void reset(MapBean bean)
	{
		this.setObjectID(bean.getObjectID());
		this.setTypeName(bean.getTypeName());
		this.setIcon(bean.getIcon());
		this.setDetail(bean.getDetail());
		this.setItemID(bean.getItemID());
		this.setCount(bean.getCount());
	}

}