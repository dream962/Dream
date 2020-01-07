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
import com.data.bean.UnlockBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class UnlockBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《关卡类型，数据》 */
    private static Map<Integer, UnlockBean> unlockBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<UnlockBean> list = SystemDataComponent.getBeanList(UnlockBean.class);
        unlockBeanMap.clear();
        for (UnlockBean bean : list)
        {
            unlockBeanMap.put(bean.getModeType(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<UnlockBean> list = SystemDataComponent.getBeanList(UnlockBean.class);
			for (UnlockBean bean : list)
			{
				if (unlockBeanMap.containsKey(bean.getModeType()))
				{
					unlockBeanMap.get(bean.getModeType()).reset(bean);
				}
				else
				{
					unlockBeanMap.put(bean.getModeType(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<UnlockBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<UnlockBean> list = new ArrayList<>();
			list.addAll(unlockBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static UnlockBean getUnlockBean(int modeType)
	{
		lock.readLock().lock();
		try
		{
			UnlockBean bean = unlockBeanMap.get(modeType);
			if (bean == null)
				LogFactory.error("UnlockBean is Null.modeType:{}", modeType);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

