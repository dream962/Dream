package com.data.bean;


/**
 * 关卡解锁配置表.xls : 关卡解锁 - t_s_unlock
 * @author = auto generate code.
 */
public final class UnlockBean
{
	/** 关卡类型 */
	private int modeType;

	/** 模式名称 */
	private String modeName;

	/** 需要达到的最远距离 */
	private int targetLength;

	/** 解锁需要消耗的物品ID */
	private int itemID;

	/** 解锁需要消耗的物品数量 */
	private int itemCount;

	/** 永久开启(0为永久开启，1为1次性开启) */
	private boolean openForever;

	/** 附带解锁类型（多个用逗号隔开） */
	private String attachModes;


	/** 关卡类型 */
	public int getModeType()
	{
		return modeType;
	}

	/** 关卡类型 */
	public void setModeType(int modeType)
	{
		this.modeType = modeType;
	}

	/** 模式名称 */
	public String getModeName()
	{
		return modeName;
	}

	/** 模式名称 */
	public void setModeName(String modeName)
	{
		if(modeName==null)
			this.modeName = "";
		else
			this.modeName = modeName;
	}

	/** 需要达到的最远距离 */
	public int getTargetLength()
	{
		return targetLength;
	}

	/** 需要达到的最远距离 */
	public void setTargetLength(int targetLength)
	{
		this.targetLength = targetLength;
	}

	/** 解锁需要消耗的物品ID */
	public int getItemID()
	{
		return itemID;
	}

	/** 解锁需要消耗的物品ID */
	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	/** 解锁需要消耗的物品数量 */
	public int getItemCount()
	{
		return itemCount;
	}

	/** 解锁需要消耗的物品数量 */
	public void setItemCount(int itemCount)
	{
		this.itemCount = itemCount;
	}

	/** 永久开启(0为永久开启，1为1次性开启) */
	public boolean getOpenForever()
	{
		return openForever;
	}

	/** 永久开启(0为永久开启，1为1次性开启) */
	public void setOpenForever(boolean openForever)
	{
		this.openForever = openForever;
	}

	/** 附带解锁类型（多个用逗号隔开） */
	public String getAttachModes()
	{
		return attachModes;
	}

	/** 附带解锁类型（多个用逗号隔开） */
	public void setAttachModes(String attachModes)
	{
		if(attachModes==null)
			this.attachModes = "";
		else
			this.attachModes = attachModes;
	}


	/** x.clone() != x */
	public UnlockBean clone()
	{
		UnlockBean clone = new UnlockBean();
		clone.setModeType(this.getModeType());
		clone.setModeName(this.getModeName());
		clone.setTargetLength(this.getTargetLength());
		clone.setItemID(this.getItemID());
		clone.setItemCount(this.getItemCount());
		clone.setOpenForever(this.getOpenForever());
		clone.setAttachModes(this.getAttachModes());
		return clone;
	}

	/** 重置信息 */
	public void reset(UnlockBean bean)
	{
		this.setModeType(bean.getModeType());
		this.setModeName(bean.getModeName());
		this.setTargetLength(bean.getTargetLength());
		this.setItemID(bean.getItemID());
		this.setItemCount(bean.getItemCount());
		this.setOpenForever(bean.getOpenForever());
		this.setAttachModes(bean.getAttachModes());
	}

}