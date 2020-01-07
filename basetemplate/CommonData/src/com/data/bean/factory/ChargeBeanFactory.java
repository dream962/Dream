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
import com.data.bean.ChargeBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class ChargeBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《配置的ID，唯一ID，数据》 */
    private static Map<Integer, ChargeBean> chargeBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<ChargeBean> list = SystemDataComponent.getBeanList(ChargeBean.class);
        chargeBeanMap.clear();
        for (ChargeBean bean : list)
        {
            chargeBeanMap.put(bean.getConfigID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<ChargeBean> list = SystemDataComponent.getBeanList(ChargeBean.class);
			for (ChargeBean bean : list)
			{
				if (chargeBeanMap.containsKey(bean.getConfigID()))
				{
					chargeBeanMap.get(bean.getConfigID()).reset(bean);
				}
				else
				{
					chargeBeanMap.put(bean.getConfigID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<ChargeBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<ChargeBean> list = new ArrayList<>();
			list.addAll(chargeBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static ChargeBean getChargeBean(int configID)
	{
		lock.readLock().lock();
		try
		{
			ChargeBean bean = chargeBeanMap.get(configID);
			if (bean == null)
				LogFactory.error("ChargeBean is Null.configID:{}", configID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

