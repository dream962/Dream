package com.data.bean.factory;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.base.data.IBeanFactory;
import com.data.component.SystemDataComponent;
import com.util.print.LogFactory;
import com.data.bean.ItemBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class ItemBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《物品ID，数据》 */
    private static Map<Integer, ItemBean> itemBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<ItemBean> list = SystemDataComponent.getBeanList(ItemBean.class);
        itemBeanMap.clear();
        for (ItemBean bean : list)
        {
            itemBeanMap.put(bean.getItemID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<ItemBean> list = SystemDataComponent.getBeanList(ItemBean.class);
			for (ItemBean bean : list)
			{
				if (itemBeanMap.containsKey(bean.getItemID()))
				{
					itemBeanMap.get(bean.getItemID()).reset(bean);
				}
				else
				{
					itemBeanMap.put(bean.getItemID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<ItemBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<ItemBean> list = new ArrayList<>();
			list.addAll(itemBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static ItemBean getItemBean(int itemID)
	{
		lock.readLock().lock();
		try
		{
			ItemBean bean = itemBeanMap.get(itemID);
			if (bean == null)
				LogFactory.error("ItemBean is Null.itemID:{}", itemID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

