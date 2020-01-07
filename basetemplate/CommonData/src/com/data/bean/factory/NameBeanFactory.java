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
import com.data.bean.NameBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class NameBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《序号，数据》 */
    private static Map<Integer, NameBean> nameBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<NameBean> list = SystemDataComponent.getBeanList(NameBean.class);
        nameBeanMap.clear();
        for (NameBean bean : list)
        {
            nameBeanMap.put(bean.getID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<NameBean> list = SystemDataComponent.getBeanList(NameBean.class);
			for (NameBean bean : list)
			{
				if (nameBeanMap.containsKey(bean.getID()))
				{
					nameBeanMap.get(bean.getID()).reset(bean);
				}
				else
				{
					nameBeanMap.put(bean.getID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<NameBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<NameBean> list = new ArrayList<>();
			list.addAll(nameBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static NameBean getNameBean(int iD)
	{
		lock.readLock().lock();
		try
		{
			NameBean bean = nameBeanMap.get(iD);
			if (bean == null)
				LogFactory.error("NameBean is Null.iD:{}", iD);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

