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
import com.data.bean.AdvertiseBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class AdvertiseBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《配置的ID，数据》 */
    private static Map<Integer, AdvertiseBean> advertiseBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<AdvertiseBean> list = SystemDataComponent.getBeanList(AdvertiseBean.class);
        advertiseBeanMap.clear();
        for (AdvertiseBean bean : list)
        {
            advertiseBeanMap.put(bean.getConfigID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<AdvertiseBean> list = SystemDataComponent.getBeanList(AdvertiseBean.class);
			for (AdvertiseBean bean : list)
			{
				if (advertiseBeanMap.containsKey(bean.getConfigID()))
				{
					advertiseBeanMap.get(bean.getConfigID()).reset(bean);
				}
				else
				{
					advertiseBeanMap.put(bean.getConfigID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<AdvertiseBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<AdvertiseBean> list = new ArrayList<>();
			list.addAll(advertiseBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static AdvertiseBean getAdvertiseBean(int configID)
	{
		lock.readLock().lock();
		try
		{
			AdvertiseBean bean = advertiseBeanMap.get(configID);
			if (bean == null)
				LogFactory.error("AdvertiseBean is Null.configID:{}", configID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

