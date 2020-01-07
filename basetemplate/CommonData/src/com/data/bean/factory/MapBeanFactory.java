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
import com.data.bean.MapBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class MapBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《地块类型ID，数据》 */
    private static Map<Integer, MapBean> mapBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<MapBean> list = SystemDataComponent.getBeanList(MapBean.class);
        mapBeanMap.clear();
        for (MapBean bean : list)
        {
            mapBeanMap.put(bean.getObjectID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<MapBean> list = SystemDataComponent.getBeanList(MapBean.class);
			for (MapBean bean : list)
			{
				if (mapBeanMap.containsKey(bean.getObjectID()))
				{
					mapBeanMap.get(bean.getObjectID()).reset(bean);
				}
				else
				{
					mapBeanMap.put(bean.getObjectID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<MapBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<MapBean> list = new ArrayList<>();
			list.addAll(mapBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static MapBean getMapBean(int objectID)
	{
		lock.readLock().lock();
		try
		{
			MapBean bean = mapBeanMap.get(objectID);
			if (bean == null)
				LogFactory.error("MapBean is Null.objectID:{}", objectID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

