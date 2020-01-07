package com.data.bag;

/**
 * 所有能放进背包的物品的抽象
 * @author dream
 *
 */
public interface IBagItem
{
    /**
     * 取得物品数据库ID
     * 
     * @return
     */
    int getKeyID();
    
    /**
     * 取得物品归属者ID
     * 
     * @return
     */
    int getUserID();

    /**
     * 设置数量
     * 
     * @param count
     */
    void setCount(int count);

    /**
     * 返回物品数量
     * 
     * @return
     */
    int getCount();

    /**
     *  设置当前物品所在位置
     * 
     * @param mask 
     * @return
     */
    void setPlace(int pos);

    /**
     * 返回当前物品在背包中的位置
     * 
     * @return
     */
    int getPlace();

    /**
     * 取得当前物品所能放置的背包类型
     * 
     * @return
     */
    BagType getBagType();

    /**
     * 设置当前物品的背包类型
     * 
     * @param type
     *            背包类型
     */
    void setBagType(BagType type);

    /**
     * 是否是新增物品
     * 
     * @return
     */
    boolean isNewItem();

    /**
     * 设置是否是新增物品
     * 
     * @param isNew
     */
    void setIsNewItem(boolean isNew);

    /**
     * 获取物品的模板Id
     * 
     * @return
     */
    int getBeanId();

    /**
     * 获取的物品是否是绑定的
     * 
     * @return
     */
    boolean getIsBind();

}
