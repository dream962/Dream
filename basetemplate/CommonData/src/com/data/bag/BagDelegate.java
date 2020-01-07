package com.data.bag;

/**
 * 背包代理
 * @author dream
 *
 */
public interface BagDelegate
{
    void onItemAdd(BaseItem item);

    void onItemChanged(BaseItem currentItem);

    void onItemDelete(BaseItem currentItem);
}
