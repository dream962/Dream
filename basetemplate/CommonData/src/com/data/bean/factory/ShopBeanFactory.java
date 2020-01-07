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
import com.data.bean.ShopBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class ShopBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《商品ID，数据》 */
    private static Map<Integer, ShopBean> shopBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<ShopBean> list = SystemDataComponent.getBeanList(ShopBean.class);
        shopBeanMap.clear();
        for (ShopBean bean : list)
        {
            shopBeanMap.put(bean.getShopID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<ShopBean> list = SystemDataComponent.getBeanList(ShopBean.class);
			for (ShopBean bean : list)
			{
				if (shopBeanMap.containsKey(bean.getShopID()))
				{
					shopBeanMap.get(bean.getShopID()).reset(bean);
				}
				else
				{
					shopBeanMap.put(bean.getShopID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<ShopBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<ShopBean> list = new ArrayList<>();
			list.addAll(shopBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static ShopBean getShopBean(int shopID)
	{
		lock.readLock().lock();
		try
		{
			ShopBean bean = shopBeanMap.get(shopID);
			if (bean == null)
				LogFactory.error("ShopBean is Null.shopID:{}", shopID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

