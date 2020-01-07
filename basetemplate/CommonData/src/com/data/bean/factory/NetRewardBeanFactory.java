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
import com.data.bean.NetRewardBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class NetRewardBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《长度，数据》 */
    private static Map<Integer, NetRewardBean> netRewardBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<NetRewardBean> list = SystemDataComponent.getBeanList(NetRewardBean.class);
        netRewardBeanMap.clear();
        for (NetRewardBean bean : list)
        {
            netRewardBeanMap.put(bean.getLength(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<NetRewardBean> list = SystemDataComponent.getBeanList(NetRewardBean.class);
			for (NetRewardBean bean : list)
			{
				if (netRewardBeanMap.containsKey(bean.getLength()))
				{
					netRewardBeanMap.get(bean.getLength()).reset(bean);
				}
				else
				{
					netRewardBeanMap.put(bean.getLength(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<NetRewardBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<NetRewardBean> list = new ArrayList<>();
			list.addAll(netRewardBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static NetRewardBean getNetRewardBean(int length)
	{
		lock.readLock().lock();
		try
		{
			NetRewardBean bean = netRewardBeanMap.get(length);
			if (bean == null)
				LogFactory.error("NetRewardBean is Null.length:{}", length);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

