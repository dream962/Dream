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
import com.data.bean.HeaderBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class HeaderBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《头像ID，数据》 */
    private static Map<Integer, HeaderBean> headerBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<HeaderBean> list = SystemDataComponent.getBeanList(HeaderBean.class);
        headerBeanMap.clear();
        for (HeaderBean bean : list)
        {
            headerBeanMap.put(bean.getHeadID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<HeaderBean> list = SystemDataComponent.getBeanList(HeaderBean.class);
			for (HeaderBean bean : list)
			{
				if (headerBeanMap.containsKey(bean.getHeadID()))
				{
					headerBeanMap.get(bean.getHeadID()).reset(bean);
				}
				else
				{
					headerBeanMap.put(bean.getHeadID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<HeaderBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<HeaderBean> list = new ArrayList<>();
			list.addAll(headerBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static HeaderBean getHeaderBean(int headID)
	{
		lock.readLock().lock();
		try
		{
			HeaderBean bean = headerBeanMap.get(headID);
			if (bean == null)
				LogFactory.error("HeaderBean is Null.headID:{}", headID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

