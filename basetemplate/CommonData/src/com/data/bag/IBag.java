package com.data.bag;

import java.util.List;

/**
 * 背包接口
 * 
 * @author dream
 *
 * @param <T>
 */
public interface IBag<T>
{
    /**
     * 加载道具
     * 
     * @param item
     * @return
     */
    boolean loadItem(T item);

    /**
     * 通过keyId获取对应的物品
     * 
     * @param keyId
     * @return
     */
    T getItem(int keyId);

    /**
     * 通过itemID获取对应的物品
     * 
     * @param itemID
     * @return
     */
    List<T> getItemByID(int itemID);

    /**
     * 添加多个物品,能叠加就叠加，不能叠加就找合适位置放下
     * 
     * @param item
     *            物品
     * @param count
     *            数量
     * @return 是否添加成功
     */
    boolean addItem(T item, ItemAddType addType);

    /**
     * 移除某件物品count个
     * 
     * @param item
     * @param count
     * @param itemRemoveType
     *            移除原因
     * @return
     */
    boolean removeItem(int itemID, int count, ItemRemoveType itemRemoveType);

    /**
     * 取得背包类型
     * 
     * @return
     */
    BagType getBagType();

    /** 获取所有物品 */
    public List<T> getItemList();
}
