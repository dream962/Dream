package com.upload.data.data;

import java.util.Date;

import com.upload.data.cache.ObjectClone;
public class StaffBean extends ObjectClone 
{
    private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private int userID;

	/**
	 * 帐户名
	 */
	private String accountName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 账号状态
	 */
	private int status;

	/**
	 * 检查次数
	 */
	private int checkCount;

	/**
	 * 检查时间
	 */
	private Date checkTime;


    public StaffBean()
    {
       super();
    }

	/**
	 * 用户id
	 */
	public int getUserID()
	{
		return userID;
	}

	/**
	 * 用户id
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
	 * 帐户名
	 */
	public String getAccountName()
	{
		return accountName;
	}

	/**
	 * 帐户名
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
	 * 密码
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * 密码
	 */
	public void setPassword(String password)
	{
		if(password != null)
		{
			if(!password.equals(this.password))
			{
				this.password = password;
				setChanged(true);
			}
		}
		else
		{
			if(password != this.password)
			{
				this.password = password;
				setChanged(true);
			}
		}
	}

	/**
	 * 昵称
	 */
	public String getNickName()
	{
		return nickName;
	}

	/**
	 * 昵称
	 */
	public void setNickName(String nickName)
	{
		if(nickName != null)
		{
			if(!nickName.equals(this.nickName))
			{
				this.nickName = nickName;
				setChanged(true);
			}
		}
		else
		{
			if(nickName != this.nickName)
			{
				this.nickName = nickName;
				setChanged(true);
			}
		}
	}

	/**
	 * 备注
	 */
	public String getRemark()
	{
		return remark;
	}

	/**
	 * 备注
	 */
	public void setRemark(String remark)
	{
		if(remark != null)
		{
			if(!remark.equals(this.remark))
			{
				this.remark = remark;
				setChanged(true);
			}
		}
		else
		{
			if(remark != this.remark)
			{
				this.remark = remark;
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
	 * 账号状态
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * 账号状态
	 */
	public void setStatus(int status)
	{
		if(status != this.status)
		{
			this.status = status;
			setChanged(true);
		}
	}

	/**
	 * 检查次数
	 */
	public int getCheckCount()
	{
		return checkCount;
	}

	/**
	 * 检查次数
	 */
	public void setCheckCount(int checkCount)
	{
		if(checkCount != this.checkCount)
		{
			this.checkCount = checkCount;
			setChanged(true);
		}
	}

	/**
	 * 检查时间
	 */
	public Date getCheckTime()
	{
		return checkTime;
	}

	/**
	 * 检查时间
	 */
	public void setCheckTime(Date checkTime)
	{
		if(checkTime != null)
		{
			if(!checkTime.equals(this.checkTime))
			{
				this.checkTime = checkTime;
				setChanged(true);
			}
		}
		else
		{
			if(checkTime != this.checkTime)
			{
				this.checkTime = checkTime;
				setChanged(true);
			}
		}
	}


	/**
	 * x.clone() != x
	 */
	public StaffBean clone()
	{
		StaffBean clone = new StaffBean();
		clone.setUserID(this.getUserID());
		clone.setAccountName(this.getAccountName());
		clone.setPassword(this.getPassword());
		clone.setNickName(this.getNickName());
		clone.setRemark(this.getRemark());
		clone.setCreateTime(this.getCreateTime());
		clone.setStatus(this.getStatus());
		clone.setCheckCount(this.getCheckCount());
		clone.setCheckTime(this.getCheckTime());
		return clone;
	}

	/**
	 * 重置信息
	 */
	public void reset(StaffBean info)
	{
		this.setUserID(info.getUserID());
		this.setAccountName(info.getAccountName());
		this.setPassword(info.getPassword());
		this.setNickName(info.getNickName());
		this.setRemark(info.getRemark());
		this.setCreateTime(info.getCreateTime());
		this.setStatus(info.getStatus());
		this.setCheckCount(info.getCheckCount());
		this.setCheckTime(info.getCheckTime());
	}

}