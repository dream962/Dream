package com.data.bean;


/**
 * 通用字符串配置表.xls : 男性角色姓库 - t_s_male_xing
 * @author = auto generate code.
 */
public final class MaleXingBean
{
	/** 姓 */
	private String surName;

	/** 字符序号 */
	private int charOrder;


	/** 姓 */
	public String getSurName()
	{
		return surName;
	}

	/** 姓 */
	public void setSurName(String surName)
	{
		if(surName==null)
			this.surName = "";
		else
			this.surName = surName;
	}

	/** 字符序号 */
	public int getCharOrder()
	{
		return charOrder;
	}

	/** 字符序号 */
	public void setCharOrder(int charOrder)
	{
		this.charOrder = charOrder;
	}


	/** x.clone() != x */
	public MaleXingBean clone()
	{
		MaleXingBean clone = new MaleXingBean();
		clone.setSurName(this.getSurName());
		clone.setCharOrder(this.getCharOrder());
		return clone;
	}

}