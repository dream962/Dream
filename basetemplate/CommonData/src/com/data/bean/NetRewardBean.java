package com.data.bean;


/**
 * 关卡解锁配置表.xls : 联机奖励表 - t_s_net_reward
 * @author = auto generate code.
 */
public final class NetRewardBean
{
	/** 长度 */
	private int length;

	/** 奖励ID1 */
	private int itemID1;

	/** 奖励数量 */
	private int itemCount1;

	/** 奖励ID2 */
	private int itemID2;

	/** 奖励数量2 */
	private int itemCount2;


	/** 长度 */
	public int getLength()
	{
		return length;
	}

	/** 长度 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/** 奖励ID1 */
	public int getItemID1()
	{
		return itemID1;
	}

	/** 奖励ID1 */
	public void setItemID1(int itemID1)
	{
		this.itemID1 = itemID1;
	}

	/** 奖励数量 */
	public int getItemCount1()
	{
		return itemCount1;
	}

	/** 奖励数量 */
	public void setItemCount1(int itemCount1)
	{
		this.itemCount1 = itemCount1;
	}

	/** 奖励ID2 */
	public int getItemID2()
	{
		return itemID2;
	}

	/** 奖励ID2 */
	public void setItemID2(int itemID2)
	{
		this.itemID2 = itemID2;
	}

	/** 奖励数量2 */
	public int getItemCount2()
	{
		return itemCount2;
	}

	/** 奖励数量2 */
	public void setItemCount2(int itemCount2)
	{
		this.itemCount2 = itemCount2;
	}


	/** x.clone() != x */
	public NetRewardBean clone()
	{
		NetRewardBean clone = new NetRewardBean();
		clone.setLength(this.getLength());
		clone.setItemID1(this.getItemID1());
		clone.setItemCount1(this.getItemCount1());
		clone.setItemID2(this.getItemID2());
		clone.setItemCount2(this.getItemCount2());
		return clone;
	}

	/** 重置信息 */
	public void reset(NetRewardBean bean)
	{
		this.setLength(bean.getLength());
		this.setItemID1(bean.getItemID1());
		this.setItemCount1(bean.getItemCount1());
		this.setItemID2(bean.getItemID2());
		this.setItemCount2(bean.getItemCount2());
	}

}