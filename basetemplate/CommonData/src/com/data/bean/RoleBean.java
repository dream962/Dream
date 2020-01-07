package com.data.bean;


/**
 * 角色配置表.xls : 角色基础表 - t_s_role
 * @author = auto generate code.
 */
public final class RoleBean
{
	/** 角色ID */
	private int objectID;

	/** 对象名称 */
	private String objectName;

	/** 头像Icon */
	private String icon;

	/** 描述说明 */
	private String detail;

	/** 解锁消耗的道具ID(小于0就是货币) */
	private int itemID;

	/** 解锁消耗甜甜圈数量 */
	private int count;


	/** 角色ID */
	public int getObjectID()
	{
		return objectID;
	}

	/** 角色ID */
	public void setObjectID(int objectID)
	{
		this.objectID = objectID;
	}

	/** 对象名称 */
	public String getObjectName()
	{
		return objectName;
	}

	/** 对象名称 */
	public void setObjectName(String objectName)
	{
		if(objectName==null)
			this.objectName = "";
		else
			this.objectName = objectName;
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
	public RoleBean clone()
	{
		RoleBean clone = new RoleBean();
		clone.setObjectID(this.getObjectID());
		clone.setObjectName(this.getObjectName());
		clone.setIcon(this.getIcon());
		clone.setDetail(this.getDetail());
		clone.setItemID(this.getItemID());
		clone.setCount(this.getCount());
		return clone;
	}

	/** 重置信息 */
	public void reset(RoleBean bean)
	{
		this.setObjectID(bean.getObjectID());
		this.setObjectName(bean.getObjectName());
		this.setIcon(bean.getIcon());
		this.setDetail(bean.getDetail());
		this.setItemID(bean.getItemID());
		this.setCount(bean.getCount());
	}

}