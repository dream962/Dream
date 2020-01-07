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
import com.data.bean.ExchangeBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class ExchangeBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《配置的ID，数据》 */
    private static Map<Integer, ExchangeBean> exchangeBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<ExchangeBean> list = SystemDataComponent.getBeanList(ExchangeBean.class);
        exchangeBeanMap.clear();
        for (ExchangeBean bean : list)
        {
            exchangeBeanMap.put(bean.getConfigID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<ExchangeBean> list = SystemDataComponent.getBeanList(ExchangeBean.class);
			for (ExchangeBean bean : list)
			{
				if (exchangeBeanMap.containsKey(bean.getConfigID()))
				{
					exchangeBeanMap.get(bean.getConfigID()).reset(bean);
				}
				else
				{
					exchangeBeanMap.put(bean.getConfigID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<ExchangeBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<ExchangeBean> list = new ArrayList<>();
			list.addAll(exchangeBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static ExchangeBean getExchangeBean(int configID)
	{
		lock.readLock().lock();
		try
		{
			ExchangeBean bean = exchangeBeanMap.get(configID);
			if (bean == null)
				LogFactory.error("ExchangeBean is Null.configID:{}", configID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

