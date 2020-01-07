package com.data.bag;

import com.data.bean.ItemBean;
import com.data.info.ItemInfo;

/**
 * 物品统一信息
 * 
 * @author dream
 *
 */
public class BaseItem
{
    private ItemInfo itemInfo;

    private ItemBean itemBean;

    public BaseItem()
    {

    }

    public BaseItem(ItemInfo info, ItemBean bean)
    {
        this.itemInfo = info;
        this.itemBean = bean;
    }

    public boolean getIsBind()
    {
        return itemInfo.getIsBind();
    }

    public int getKeyID()
    {
        return itemInfo.getKeyID();
    }

    /**
     * 创建物品
     * 
     * @param itemID
     * @param count
     * @return
     */
    public static BaseItem createItem(int itemID, int count)
    {
        // ItemBean itemBean = SystemConfigComponent.getItemBean(itemID);
        // if (itemBean == null)
        // return null;
        //
        // ItemInfo info = new ItemInfo();
        // info.setItemCount(count);
        // info.setItemID(itemID);
        // info.setCreateTime(new Date());
        // info.setIsExist(true);
        //
        // BaseItem item = new BaseItem(info, itemBean);
        // return item;

        return null;
    }

    /**
     * 创建物品
     * 
     * @param info
     * @param bean
     * @return
     */
    public static BaseItem createItem(ItemInfo info, ItemBean bean)
    {
        BaseItem item = new BaseItem(info, bean);
        return item;
    }

    /**
     * 道具信息
     * 
     * @return
     */
    public ItemBean getBean()
    {
        return itemBean;
    }

    /**
     * 物品信息
     * 
     * @return
     */
    public ItemInfo getInfo()
    {
        return itemInfo;
    }

    public int getItemID()
    {
        return itemBean.getItemID();
    }

    public int getItemCount()
    {
        return itemInfo.getItemCount();
    }

    /**
     * 获取物品类型
     * 
     * @return
     */
    public ItemType getItemType()
    {
        return ItemType.ITEM_EVIL;
        // return ItemType.parse(itemBean.getItemType());
    }

    /**
     * 设置物品存在
     */
    public void setExist(boolean exist)
    {
        itemInfo.setIsExist(exist);
    }

    /**
     * 设置用户id
     * 
     * @param userID
     */
    public void setUserID(long userID)
    {
        itemInfo.setUserID(userID);
    }
}
