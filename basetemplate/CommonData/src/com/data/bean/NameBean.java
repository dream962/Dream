package com.data.bean;


/**
 * 名称.xls : 昵称 - t_s_name
 * @author = auto generate code.
 */
public final class NameBean
{
	/** 序号 */
	private int iD;

	/** 姓 */
	private String xing;

	/** 男名 */
	private String boy;

	/** 女名 */
	private String girl;


	/** 序号 */
	public int getID()
	{
		return iD;
	}

	/** 序号 */
	public void setID(int iD)
	{
		this.iD = iD;
	}

	/** 姓 */
	public String getXing()
	{
		return xing;
	}

	/** 姓 */
	public void setXing(String xing)
	{
		if(xing==null)
			this.xing = "";
		else
			this.xing = xing;
	}

	/** 男名 */
	public String getBoy()
	{
		return boy;
	}

	/** 男名 */
	public void setBoy(String boy)
	{
		if(boy==null)
			this.boy = "";
		else
			this.boy = boy;
	}

	/** 女名 */
	public String getGirl()
	{
		return girl;
	}

	/** 女名 */
	public void setGirl(String girl)
	{
		if(girl==null)
			this.girl = "";
		else
			this.girl = girl;
	}


	/** x.clone() != x */
	public NameBean clone()
	{
		NameBean clone = new NameBean();
		clone.setID(this.getID());
		clone.setXing(this.getXing());
		clone.setBoy(this.getBoy());
		clone.setGirl(this.getGirl());
		return clone;
	}

	/** 重置信息 */
	public void reset(NameBean bean)
	{
		this.setID(bean.getID());
		this.setXing(bean.getXing());
		this.setBoy(bean.getBoy());
		this.setGirl(bean.getGirl());
	}

}