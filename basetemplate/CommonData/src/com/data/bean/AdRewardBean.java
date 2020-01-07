package com.data.bean;


/**
 * 广告配置表.xls : 视频奖励广告配置 - t_s_ad_reward
 * @author = auto generate code.
 */
public final class AdRewardBean
{
	/** 配置的ID */
	private int configID;

	/** 间隔时长(分钟) */
	private int durationCount;

	/** 奖励物品ID */
	private int rewardItemID;

	/** 奖励物品数量 */
	private int rewardItemCount;


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

	/** 间隔时长(分钟) */
	public int getDurationCount()
	{
		return durationCount;
	}

	/** 间隔时长(分钟) */
	public void setDurationCount(int durationCount)
	{
		this.durationCount = durationCount;
	}

	/** 奖励物品ID */
	public int getRewardItemID()
	{
		return rewardItemID;
	}

	/** 奖励物品ID */
	public void setRewardItemID(int rewardItemID)
	{
		this.rewardItemID = rewardItemID;
	}

	/** 奖励物品数量 */
	public int getRewardItemCount()
	{
		return rewardItemCount;
	}

	/** 奖励物品数量 */
	public void setRewardItemCount(int rewardItemCount)
	{
		this.rewardItemCount = rewardItemCount;
	}


	/** x.clone() != x */
	public AdRewardBean clone()
	{
		AdRewardBean clone = new AdRewardBean();
		clone.setConfigID(this.getConfigID());
		clone.setDurationCount(this.getDurationCount());
		clone.setRewardItemID(this.getRewardItemID());
		clone.setRewardItemCount(this.getRewardItemCount());
		return clone;
	}

	/** 重置信息 */
	public void reset(AdRewardBean bean)
	{
		this.setConfigID(bean.getConfigID());
		this.setDurationCount(bean.getDurationCount());
		this.setRewardItemID(bean.getRewardItemID());
		this.setRewardItemCount(bean.getRewardItemCount());
	}

}