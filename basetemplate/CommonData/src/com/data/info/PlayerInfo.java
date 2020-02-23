package com.data.info;

import java.util.Date;

import com.base.data.ChangedObject;

/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public final class PlayerInfo extends ChangedObject 
{
	/**
	 * 玩家ID
	 */
	private int userID;

	/**
	 * 玩家名称
	 */
	private String playerName;

	/**
	 * 账号ID
	 */
	private long accountID;

	/**
	 * 账号名称
	 */
	private String accountName;

	/**
	 * 账号服G名
	 */
	private String accuntGName;

	/**
	 * 账号OpenID
	 */
	private String openID;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 上次登录时间
	 */
	private Date lastLoginTime;

	/**
	 * 金币
	 */
	private int gold;

	/**
	 * 钻石
	 */
	private int diamond;

	/**
	 * 金钱
	 */
	private int money;

	/**
	 * 当前形象
	 */
	private int headerID;

	/**
	 * 广告过期时间
	 */
	private Date adExpireTime;

	/**
	 * 胜利次数
	 */
	private int winCount;

	/**
	 * 失败次数
	 */
	private int failCount;

	/**
	 * 解锁角色数
	 */
	private String roleTypes;

	/**
	 * 解锁游戏类型
	 */
	private String modes;

	/**
	 * 解锁主题
	 */
	private String theme;

	/**
	 * 头像
	 */
	private String headers;

	/**
	 * 限时模式最佳
	 */
	private int timeTopScore;

	/**
	 * 竞技模式最佳
	 */
	private int raceTopScore;

	/**
	 * 无尽模式最佳
	 */
	private int endTopScore;

	/**
	 * 全局最佳长度
	 */
	private int topLength;

	/**
	 * 捐献数量
	 */
	private int donateValue;

	/**
	 * 是否广告解锁金币模式
	 */
	private boolean isCanUnlockCoinModeByAD;

	/**
	 * 上次广告时间
	 */
	private Date lastAdTime;

	/**
	 * 广告触发次数
	 */
	private int adTriggerCount;

	/**
	 * 购买广告时间
	 */
	private Date buyAdTime;


    public PlayerInfo()
    {
       super();
    }

	/**
	 * 玩家ID
	 */
	public int getUserID()
	{
		return userID;
	}

	/**
	 * 玩家ID
	 */
	public void setUserID(int userID)
	{
		if(userID != this.userID)
		{
			this.userID = userID;
			setChanged(true);
		}
	}

	/**
	 * 玩家名称
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * 玩家名称
	 */
	public void setPlayerName(String playerName)
	{
		if(playerName != null)
		{
			if(!playerName.equals(this.playerName))
			{
				this.playerName = playerName;
				setChanged(true);
			}
		}
		else
		{
			if(playerName != this.playerName)
			{
				this.playerName = playerName;
				setChanged(true);
			}
		}
	}

	/**
	 * 账号ID
	 */
	public long getAccountID()
	{
		return accountID;
	}

	/**
	 * 账号ID
	 */
	public void setAccountID(long accountID)
	{
		if(accountID != this.accountID)
		{
			this.accountID = accountID;
			setChanged(true);
		}
	}

	/**
	 * 账号名称
	 */
	public String getAccountName()
	{
		return accountName;
	}

	/**
	 * 账号名称
	 */
	public void setAccountName(String accountName)
	{
		if(accountName != null)
		{
			if(!accountName.equals(this.accountName))
			{
				this.accountName = accountName;
				setChanged(true);
			}
		}
		else
		{
			if(accountName != this.accountName)
			{
				this.accountName = accountName;
				setChanged(true);
			}
		}
	}

	/**
	 * 账号服G名
	 */
	public String getAccuntGName()
	{
		return accuntGName;
	}

	/**
	 * 账号服G名
	 */
	public void setAccuntGName(String accuntGName)
	{
		if(accuntGName != null)
		{
			if(!accuntGName.equals(this.accuntGName))
			{
				this.accuntGName = accuntGName;
				setChanged(true);
			}
		}
		else
		{
			if(accuntGName != this.accuntGName)
			{
				this.accuntGName = accuntGName;
				setChanged(true);
			}
		}
	}

	/**
	 * 账号OpenID
	 */
	public String getOpenID()
	{
		return openID;
	}

	/**
	 * 账号OpenID
	 */
	public void setOpenID(String openID)
	{
		if(openID != null)
		{
			if(!openID.equals(this.openID))
			{
				this.openID = openID;
				setChanged(true);
			}
		}
		else
		{
			if(openID != this.openID)
			{
				this.openID = openID;
				setChanged(true);
			}
		}
	}

	/**
	 * 创建时间
	 */
	public Date getCreateTime()
	{
		return createTime;
	}

	/**
	 * 创建时间
	 */
	public void setCreateTime(Date createTime)
	{
		if(createTime != null)
		{
			if(!createTime.equals(this.createTime))
			{
				this.createTime = createTime;
				setChanged(true);
			}
		}
		else
		{
			if(createTime != this.createTime)
			{
				this.createTime = createTime;
				setChanged(true);
			}
		}
	}

	/**
	 * 上次登录时间
	 */
	public Date getLastLoginTime()
	{
		return lastLoginTime;
	}

	/**
	 * 上次登录时间
	 */
	public void setLastLoginTime(Date lastLoginTime)
	{
		if(lastLoginTime != null)
		{
			if(!lastLoginTime.equals(this.lastLoginTime))
			{
				this.lastLoginTime = lastLoginTime;
				setChanged(true);
			}
		}
		else
		{
			if(lastLoginTime != this.lastLoginTime)
			{
				this.lastLoginTime = lastLoginTime;
				setChanged(true);
			}
		}
	}

	/**
	 * 金币
	 */
	public int getGold()
	{
		return gold;
	}

	/**
	 * 金币
	 */
	public void setGold(int gold)
	{
		if(gold != this.gold)
		{
			this.gold = gold;
			setChanged(true);
		}
	}

	/**
	 * 钻石
	 */
	public int getDiamond()
	{
		return diamond;
	}

	/**
	 * 钻石
	 */
	public void setDiamond(int diamond)
	{
		if(diamond != this.diamond)
		{
			this.diamond = diamond;
			setChanged(true);
		}
	}

	/**
	 * 金钱
	 */
	public int getMoney()
	{
		return money;
	}

	/**
	 * 金钱
	 */
	public void setMoney(int money)
	{
		if(money != this.money)
		{
			this.money = money;
			setChanged(true);
		}
	}

	/**
	 * 当前形象
	 */
	public int getHeaderID()
	{
		return headerID;
	}

	/**
	 * 当前形象
	 */
	public void setHeaderID(int headerID)
	{
		if(headerID != this.headerID)
		{
			this.headerID = headerID;
			setChanged(true);
		}
	}

	/**
	 * 广告过期时间
	 */
	public Date getAdExpireTime()
	{
		return adExpireTime;
	}

	/**
	 * 广告过期时间
	 */
	public void setAdExpireTime(Date adExpireTime)
	{
		if(adExpireTime != null)
		{
			if(!adExpireTime.equals(this.adExpireTime))
			{
				this.adExpireTime = adExpireTime;
				setChanged(true);
			}
		}
		else
		{
			if(adExpireTime != this.adExpireTime)
			{
				this.adExpireTime = adExpireTime;
				setChanged(true);
			}
		}
	}

	/**
	 * 胜利次数
	 */
	public int getWinCount()
	{
		return winCount;
	}

	/**
	 * 胜利次数
	 */
	public void setWinCount(int winCount)
	{
		if(winCount != this.winCount)
		{
			this.winCount = winCount;
			setChanged(true);
		}
	}

	/**
	 * 失败次数
	 */
	public int getFailCount()
	{
		return failCount;
	}

	/**
	 * 失败次数
	 */
	public void setFailCount(int failCount)
	{
		if(failCount != this.failCount)
		{
			this.failCount = failCount;
			setChanged(true);
		}
	}

	/**
	 * 解锁角色数
	 */
	public String getRoleTypes()
	{
		return roleTypes;
	}

	/**
	 * 解锁角色数
	 */
	public void setRoleTypes(String roleTypes)
	{
		if(roleTypes != null)
		{
			if(!roleTypes.equals(this.roleTypes))
			{
				this.roleTypes = roleTypes;
				setChanged(true);
			}
		}
		else
		{
			if(roleTypes != this.roleTypes)
			{
				this.roleTypes = roleTypes;
				setChanged(true);
			}
		}
	}

	/**
	 * 解锁游戏类型
	 */
	public String getModes()
	{
		return modes;
	}

	/**
	 * 解锁游戏类型
	 */
	public void setModes(String modes)
	{
		if(modes != null)
		{
			if(!modes.equals(this.modes))
			{
				this.modes = modes;
				setChanged(true);
			}
		}
		else
		{
			if(modes != this.modes)
			{
				this.modes = modes;
				setChanged(true);
			}
		}
	}

	/**
	 * 解锁主题
	 */
	public String getTheme()
	{
		return theme;
	}

	/**
	 * 解锁主题
	 */
	public void setTheme(String theme)
	{
		if(theme != null)
		{
			if(!theme.equals(this.theme))
			{
				this.theme = theme;
				setChanged(true);
			}
		}
		else
		{
			if(theme != this.theme)
			{
				this.theme = theme;
				setChanged(true);
			}
		}
	}

	/**
	 * 头像
	 */
	public String getHeaders()
	{
		return headers;
	}

	/**
	 * 头像
	 */
	public void setHeaders(String headers)
	{
		if(headers != null)
		{
			if(!headers.equals(this.headers))
			{
				this.headers = headers;
				setChanged(true);
			}
		}
		else
		{
			if(headers != this.headers)
			{
				this.headers = headers;
				setChanged(true);
			}
		}
	}

	/**
	 * 限时模式最佳
	 */
	public int getTimeTopScore()
	{
		return timeTopScore;
	}

	/**
	 * 限时模式最佳
	 */
	public void setTimeTopScore(int timeTopScore)
	{
		if(timeTopScore != this.timeTopScore)
		{
			this.timeTopScore = timeTopScore;
			setChanged(true);
		}
	}

	/**
	 * 竞技模式最佳
	 */
	public int getRaceTopScore()
	{
		return raceTopScore;
	}

	/**
	 * 竞技模式最佳
	 */
	public void setRaceTopScore(int raceTopScore)
	{
		if(raceTopScore != this.raceTopScore)
		{
			this.raceTopScore = raceTopScore;
			setChanged(true);
		}
	}

	/**
	 * 无尽模式最佳
	 */
	public int getEndTopScore()
	{
		return endTopScore;
	}

	/**
	 * 无尽模式最佳
	 */
	public void setEndTopScore(int endTopScore)
	{
		if(endTopScore != this.endTopScore)
		{
			this.endTopScore = endTopScore;
			setChanged(true);
		}
	}

	/**
	 * 全局最佳长度
	 */
	public int getTopLength()
	{
		return topLength;
	}

	/**
	 * 全局最佳长度
	 */
	public void setTopLength(int topLength)
	{
		if(topLength != this.topLength)
		{
			this.topLength = topLength;
			setChanged(true);
		}
	}

	/**
	 * 捐献数量
	 */
	public int getDonateValue()
	{
		return donateValue;
	}

	/**
	 * 捐献数量
	 */
	public void setDonateValue(int donateValue)
	{
		if(donateValue != this.donateValue)
		{
			this.donateValue = donateValue;
			setChanged(true);
		}
	}

	/**
	 * 是否广告解锁金币模式
	 */
	public boolean getIsCanUnlockCoinModeByAD()
	{
		return isCanUnlockCoinModeByAD;
	}

	/**
	 * 是否广告解锁金币模式
	 */
	public void setIsCanUnlockCoinModeByAD(boolean isCanUnlockCoinModeByAD)
	{
		if(isCanUnlockCoinModeByAD != this.isCanUnlockCoinModeByAD)
		{
			this.isCanUnlockCoinModeByAD = isCanUnlockCoinModeByAD;
			setChanged(true);
		}
	}

	/**
	 * 上次广告时间
	 */
	public Date getLastAdTime()
	{
		return lastAdTime;
	}

	/**
	 * 上次广告时间
	 */
	public void setLastAdTime(Date lastAdTime)
	{
		if(lastAdTime != null)
		{
			if(!lastAdTime.equals(this.lastAdTime))
			{
				this.lastAdTime = lastAdTime;
				setChanged(true);
			}
		}
		else
		{
			if(lastAdTime != this.lastAdTime)
			{
				this.lastAdTime = lastAdTime;
				setChanged(true);
			}
		}
	}

	/**
	 * 广告触发次数
	 */
	public int getAdTriggerCount()
	{
		return adTriggerCount;
	}

	/**
	 * 广告触发次数
	 */
	public void setAdTriggerCount(int adTriggerCount)
	{
		if(adTriggerCount != this.adTriggerCount)
		{
			this.adTriggerCount = adTriggerCount;
			setChanged(true);
		}
	}

	/**
	 * 购买广告时间
	 */
	public Date getBuyAdTime()
	{
		return buyAdTime;
	}

	/**
	 * 购买广告时间
	 */
	public void setBuyAdTime(Date buyAdTime)
	{
		if(buyAdTime != null)
		{
			if(!buyAdTime.equals(this.buyAdTime))
			{
				this.buyAdTime = buyAdTime;
				setChanged(true);
			}
		}
		else
		{
			if(buyAdTime != this.buyAdTime)
			{
				this.buyAdTime = buyAdTime;
				setChanged(true);
			}
		}
	}


	/**
	 * x.clone() != x
	 */
	public PlayerInfo clone()
	{
		PlayerInfo clone = new PlayerInfo();
		clone.setUserID(this.getUserID());
		clone.setPlayerName(this.getPlayerName());
		clone.setAccountID(this.getAccountID());
		clone.setAccountName(this.getAccountName());
		clone.setAccuntGName(this.getAccuntGName());
		clone.setOpenID(this.getOpenID());
		clone.setCreateTime(this.getCreateTime());
		clone.setLastLoginTime(this.getLastLoginTime());
		clone.setGold(this.getGold());
		clone.setDiamond(this.getDiamond());
		clone.setMoney(this.getMoney());
		clone.setHeaderID(this.getHeaderID());
		clone.setAdExpireTime(this.getAdExpireTime());
		clone.setWinCount(this.getWinCount());
		clone.setFailCount(this.getFailCount());
		clone.setRoleTypes(this.getRoleTypes());
		clone.setModes(this.getModes());
		clone.setTheme(this.getTheme());
		clone.setHeaders(this.getHeaders());
		clone.setTimeTopScore(this.getTimeTopScore());
		clone.setRaceTopScore(this.getRaceTopScore());
		clone.setEndTopScore(this.getEndTopScore());
		clone.setTopLength(this.getTopLength());
		clone.setDonateValue(this.getDonateValue());
		clone.setIsCanUnlockCoinModeByAD(this.getIsCanUnlockCoinModeByAD());
		clone.setLastAdTime(this.getLastAdTime());
		clone.setAdTriggerCount(this.getAdTriggerCount());
		clone.setBuyAdTime(this.getBuyAdTime());
		return clone;
	}

	/**
	 * 重置信息
	 */
	public void reset(PlayerInfo info)
	{
		this.setUserID(info.getUserID());
		this.setPlayerName(info.getPlayerName());
		this.setAccountID(info.getAccountID());
		this.setAccountName(info.getAccountName());
		this.setAccuntGName(info.getAccuntGName());
		this.setOpenID(info.getOpenID());
		this.setCreateTime(info.getCreateTime());
		this.setLastLoginTime(info.getLastLoginTime());
		this.setGold(info.getGold());
		this.setDiamond(info.getDiamond());
		this.setMoney(info.getMoney());
		this.setHeaderID(info.getHeaderID());
		this.setAdExpireTime(info.getAdExpireTime());
		this.setWinCount(info.getWinCount());
		this.setFailCount(info.getFailCount());
		this.setRoleTypes(info.getRoleTypes());
		this.setModes(info.getModes());
		this.setTheme(info.getTheme());
		this.setHeaders(info.getHeaders());
		this.setTimeTopScore(info.getTimeTopScore());
		this.setRaceTopScore(info.getRaceTopScore());
		this.setEndTopScore(info.getEndTopScore());
		this.setTopLength(info.getTopLength());
		this.setDonateValue(info.getDonateValue());
		this.setIsCanUnlockCoinModeByAD(info.getIsCanUnlockCoinModeByAD());
		this.setLastAdTime(info.getLastAdTime());
		this.setAdTriggerCount(info.getAdTriggerCount());
		this.setBuyAdTime(info.getBuyAdTime());
	}

}