package com.data.bean;


/**
 * 全局参数配置表.xls : float参数配置 - t_s_config_float
 * @author = auto generate code.
 */
public final class ConfigFloatBean
{
	/** 值 */
	private float value;

	/** 类型 */
	private String keyID;

	/** 名称 */
	private String name;


	/** 值 */
	public float getValue()
	{
		return value;
	}

	/** 值 */
	public void setValue(float value)
	{
		this.value = value;
	}

	/** 类型 */
	public String getKeyID()
	{
		return keyID;
	}

	/** 类型 */
	public void setKeyID(String keyID)
	{
		if(keyID==null)
			this.keyID = "";
		else
			this.keyID = keyID;
	}

	/** 名称 */
	public String getName()
	{
		return name;
	}

	/** 名称 */
	public void setName(String name)
	{
		if(name==null)
			this.name = "";
		else
			this.name = name;
	}


	/** x.clone() != x */
	public ConfigFloatBean clone()
	{
		ConfigFloatBean clone = new ConfigFloatBean();
		clone.setValue(this.getValue());
		clone.setKeyID(this.getKeyID());
		clone.setName(this.getName());
		return clone;
	}

}