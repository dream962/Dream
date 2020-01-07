package com.data.bag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 主背包
 * 
 * @author dream
 *
 */
public class BaseBag implements IBag<BaseItem>
{
    /** 所有的物品 */
    private Map<Integer, BaseItem> itemMap = new HashMap<>();

    /** 读写锁 */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /** 背包类型 */
    private BagType bagType;

    /** 背包代理 */
    private BagDelegate delegate;

    public BaseBag(BagType type, BagDelegate delegate)
    {
        this.bagType = type;
        this.delegate = delegate;
    }

    @Override
    public BaseItem getItem(int keyId)
    {
        lock.readLock().lock();
        try
        {
            BaseItem item = itemMap.get(keyId);
            return item;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<BaseItem> getItemByID(int itemID)
    {
        List<BaseItem> list = new ArrayList<>();

        lock.readLock().lock();
        try
        {
            for (BaseItem item : itemMap.values())
            {
                if (item.getItemID() == itemID)
                {
                    list.add(item);
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }

        return list;
    }

    @Override
    public boolean addItem(BaseItem item, ItemAddType addType)
    {
        lock.writeLock().lock();
        try
        {
            // // 道具ID相同，叠加
            // for (BaseItem baseItem : itemMap.values())
            // {
            // if (baseItem.getBean().getItemID() == item.getBean().getItemID())
            // {
            // int count = baseItem.getInfo().getItemCount() + item.getInfo().getItemCount();
            // baseItem.getInfo().setItemCount(count);
            //
            // delegate.onItemAdd(item);
            // return true;
            // }
            // }
            //
            // // 道具ID不同，添加
            // item.getInfo().setAddType(addType.getValue());
            // itemMap.put(item.getKeyID(), item);
            //
            // delegate.onItemAdd(item);
        }
        finally
        {
            lock.writeLock().unlock();
        }

        return true;
    }

    @Override
    public boolean removeItem(int itemID, int count, ItemRemoveType itemRemoveType)
    {
        lock.writeLock().lock();
        try
        {
            // 道具ID相同
            for (BaseItem baseItem : itemMap.values())
            {
                // if (baseItem.getBean().getItemID() == itemID)
                // {
                // int itemCount = baseItem.getInfo().getItemCount();
                // if (itemCount < count)
                // return false;
                //
                // itemCount = itemCount - count;
                // baseItem.getInfo().setItemCount(itemCount);
                //
                // // 如果物品数量为0
                // if (itemCount <= 0)
                // {
                // itemMap.remove(baseItem.getKeyID());
                // delegate.onItemDelete(baseItem);
                //
                // return true;
                // }
                // else
                // {
                // // 更改
                // delegate.onItemChanged(baseItem);
                // }
                //
                // return true;
                // }
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }

        return false;
    }

    @Override
    public BagType getBagType()
    {
        return bagType;
    }

    @Override
    public List<BaseItem> getItemList()
    {
        List<BaseItem> list = new ArrayList<>();

        lock.writeLock().lock();
        try
        {
            list.addAll(itemMap.values());
        }
        finally
        {
            lock.writeLock().unlock();
        }

        return list;
    }

    @Override
    public boolean loadItem(BaseItem item)
    {
        lock.readLock().lock();
        try
        {
            itemMap.put(item.getKeyID(), item);
        }
        finally
        {
            lock.readLock().unlock();
        }

        return true;
    }
}
