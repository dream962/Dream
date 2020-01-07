package com.data.bean;


/**
 * 通用字符串配置表.xls : 男性角色名库 - t_s_male_ming
 * @author = auto generate code.
 */
public final class MaleMingBean
{
	/** 字符序号 */
	private int charOrder;

	/** 名 */
	private String name;


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

	/** 名 */
	public String getName()
	{
		return name;
	}

	/** 名 */
	public void setName(String name)
	{
		if(name==null)
			this.name = "";
		else
			this.name = name;
	}


	/** x.clone() != x */
	public MaleMingBean clone()
	{
		MaleMingBean clone = new MaleMingBean();
		clone.setCharOrder(this.getCharOrder());
		clone.setName(this.getName());
		return clone;
	}

}