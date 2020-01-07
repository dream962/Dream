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
import com.data.bean.AdRewardBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class AdRewardBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《配置的ID，数据》 */
    private static Map<Integer, AdRewardBean> adRewardBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<AdRewardBean> list = SystemDataComponent.getBeanList(AdRewardBean.class);
        adRewardBeanMap.clear();
        for (AdRewardBean bean : list)
        {
            adRewardBeanMap.put(bean.getConfigID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<AdRewardBean> list = SystemDataComponent.getBeanList(AdRewardBean.class);
			for (AdRewardBean bean : list)
			{
				if (adRewardBeanMap.containsKey(bean.getConfigID()))
				{
					adRewardBeanMap.get(bean.getConfigID()).reset(bean);
				}
				else
				{
					adRewardBeanMap.put(bean.getConfigID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<AdRewardBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<AdRewardBean> list = new ArrayList<>();
			list.addAll(adRewardBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static AdRewardBean getAdRewardBean(int configID)
	{
		lock.readLock().lock();
		try
		{
			AdRewardBean bean = adRewardBeanMap.get(configID);
			if (bean == null)
				LogFactory.error("AdRewardBean is Null.configID:{}", configID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

