package com.data.bean;


/**
 * 配置表.xls : 商城 - t_s_shop
 * @author = auto generate code.
 */
public final class ShopBean
{
	/** 商品ID */
	private int shopID;

	/** 商品名称 */
	private String shopName;

	/** 商品类型 */
	private int shopType;

	/** 商品说明 */
	private String shopDesc;

	/** 购买类型 */
	private int buyType;

	/** 物品1 */
	private int buyCount1;

	/** 物品2 */
	private int buyCount2;


	/** 商品ID */
	public int getShopID()
	{
		return shopID;
	}

	/** 商品ID */
	public void setShopID(int shopID)
	{
		this.shopID = shopID;
	}

	/** 商品名称 */
	public String getShopName()
	{
		return shopName;
	}

	/** 商品名称 */
	public void setShopName(String shopName)
	{
		if(shopName==null)
			this.shopName = "";
		else
			this.shopName = shopName;
	}

	/** 商品类型 */
	public int getShopType()
	{
		return shopType;
	}

	/** 商品类型 */
	public void setShopType(int shopType)
	{
		this.shopType = shopType;
	}

	/** 商品说明 */
	public String getShopDesc()
	{
		return shopDesc;
	}

	/** 商品说明 */
	public void setShopDesc(String shopDesc)
	{
		if(shopDesc==null)
			this.shopDesc = "";
		else
			this.shopDesc = shopDesc;
	}

	/** 购买类型 */
	public int getBuyType()
	{
		return buyType;
	}

	/** 购买类型 */
	public void setBuyType(int buyType)
	{
		this.buyType = buyType;
	}

	/** 物品1 */
	public int getBuyCount1()
	{
		return buyCount1;
	}

	/** 物品1 */
	public void setBuyCount1(int buyCount1)
	{
		this.buyCount1 = buyCount1;
	}

	/** 物品2 */
	public int getBuyCount2()
	{
		return buyCount2;
	}

	/** 物品2 */
	public void setBuyCount2(int buyCount2)
	{
		this.buyCount2 = buyCount2;
	}


	/** x.clone() != x */
	public ShopBean clone()
	{
		ShopBean clone = new ShopBean();
		clone.setShopID(this.getShopID());
		clone.setShopName(this.getShopName());
		clone.setShopType(this.getShopType());
		clone.setShopDesc(this.getShopDesc());
		clone.setBuyType(this.getBuyType());
		clone.setBuyCount1(this.getBuyCount1());
		clone.setBuyCount2(this.getBuyCount2());
		return clone;
	}

	/** 重置信息 */
	public void reset(ShopBean bean)
	{
		this.setShopID(bean.getShopID());
		this.setShopName(bean.getShopName());
		this.setShopType(bean.getShopType());
		this.setShopDesc(bean.getShopDesc());
		this.setBuyType(bean.getBuyType());
		this.setBuyCount1(bean.getBuyCount1());
		this.setBuyCount2(bean.getBuyCount2());
	}

}