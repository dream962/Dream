package com.data.bean;

/**
 * 充值配置表.xls : 基本配置 - t_s_charge
 * 
 * @author = auto generate code.
 */
public final class ChargeBean
{
    /** 配置的ID，唯一ID */
    private int configID;

    /** 显示的图标资源 */
    private String iconResource;

    /** 需要充值的金额 */
    private float chargeValue;

    /** 充值可以获得的甜甜数数量 */
    private int chargeIngot;

    /** 优惠力度 */
    private int freePercent;

    /** 商品特性 */
    private int itemPeculiarity;

    /** 谷歌后台ID */
    private String googleSKU;

    /** 角色ID */
    private int chargeRoleID;

    public int getChargeRoleID()
    {
        return chargeRoleID;
    }

    public void setChargeRoleID(int chargeRoleID)
    {
        this.chargeRoleID = chargeRoleID;
    }

    /** 配置的ID，唯一ID */
    public int getConfigID()
    {
        return configID;
    }

    /** 配置的ID，唯一ID */
    public void setConfigID(int configID)
    {
        this.configID = configID;
    }

    /** 显示的图标资源 */
    public String getIconResource()
    {
        return iconResource;
    }

    /** 显示的图标资源 */
    public void setIconResource(String iconResource)
    {
        if (iconResource == null)
            this.iconResource = "";
        else
            this.iconResource = iconResource;
    }

    /** 需要充值的金额 */
    public float getChargeValue()
    {
        return chargeValue;
    }

    /** 需要充值的金额 */
    public void setChargeValue(float chargeValue)
    {
        this.chargeValue = chargeValue;
    }

    /** 充值可以获得的甜甜数数量 */
    public int getChargeIngot()
    {
        return chargeIngot;
    }

    /** 充值可以获得的甜甜数数量 */
    public void setChargeIngot(int chargeIngot)
    {
        this.chargeIngot = chargeIngot;
    }

    /** 优惠力度 */
    public int getFreePercent()
    {
        return freePercent;
    }

    /** 优惠力度 */
    public void setFreePercent(int freePercent)
    {
        this.freePercent = freePercent;
    }

    /** 商品特性 */
    public int getItemPeculiarity()
    {
        return itemPeculiarity;
    }

    /** 商品特性 */
    public void setItemPeculiarity(int itemPeculiarity)
    {
        this.itemPeculiarity = itemPeculiarity;
    }

    /** 谷歌后台ID */
    public String getGoogleSKU()
    {
        return googleSKU;
    }

    /** 谷歌后台ID */
    public void setGoogleSKU(String googleSKU)
    {
        if (googleSKU == null)
            this.googleSKU = "";
        else
            this.googleSKU = googleSKU;
    }

    /** x.clone() != x */
    public ChargeBean clone()
    {
        ChargeBean clone = new ChargeBean();
        clone.setConfigID(this.getConfigID());
        clone.setIconResource(this.getIconResource());
        clone.setChargeValue(this.getChargeValue());
        clone.setChargeIngot(this.getChargeIngot());
        clone.setFreePercent(this.getFreePercent());
        clone.setItemPeculiarity(this.getItemPeculiarity());
        clone.setGoogleSKU(this.getGoogleSKU());
        return clone;
    }

    /** 重置信息 */
    public void reset(ChargeBean bean)
    {
        this.setConfigID(bean.getConfigID());
        this.setIconResource(bean.getIconResource());
        this.setChargeValue(bean.getChargeValue());
        this.setChargeIngot(bean.getChargeIngot());
        this.setFreePercent(bean.getFreePercent());
        this.setItemPeculiarity(bean.getItemPeculiarity());
        this.setGoogleSKU(bean.getGoogleSKU());
    }

}
