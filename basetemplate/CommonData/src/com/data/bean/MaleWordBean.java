package com.data.bean;


/**
 * 通用字符串配置表.xls : 男性角色字库 - t_s_male_word
 * @author = auto generate code.
 */
public final class MaleWordBean
{
	/** 字 */
	private String word;

	/** 字符序号 */
	private int charOrder;


	/** 字 */
	public String getWord()
	{
		return word;
	}

	/** 字 */
	public void setWord(String word)
	{
		if(word==null)
			this.word = "";
		else
			this.word = word;
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
	public MaleWordBean clone()
	{
		MaleWordBean clone = new MaleWordBean();
		clone.setWord(this.getWord());
		clone.setCharOrder(this.getCharOrder());
		return clone;
	}

}