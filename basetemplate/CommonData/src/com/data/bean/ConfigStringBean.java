package com.data.bean;


/**
 * 全局参数配置表.xls : string参数配置 - t_s_config_string
 * @author = auto generate code.
 */
public final class ConfigStringBean
{
	/** 值 */
	private String value;

	/** 类型 */
	private String keyID;

	/** 名称 */
	private String name;


	/** 值 */
	public String getValue()
	{
		return value;
	}

	/** 值 */
	public void setValue(String value)
	{
		if(value==null)
			this.value = "";
		else
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
	public ConfigStringBean clone()
	{
		ConfigStringBean clone = new ConfigStringBean();
		clone.setValue(this.getValue());
		clone.setKeyID(this.getKeyID());
		clone.setName(this.getName());
		return clone;
	}

}