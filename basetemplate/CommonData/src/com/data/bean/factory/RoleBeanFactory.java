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
import com.data.bean.RoleBean;

/**
 * auto generate,don't modify.
 * @author system
*/
public class RoleBeanFactory implements IBeanFactory
{
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /**《角色ID，数据》 */
    private static Map<Integer, RoleBean> roleBeanMap = new HashMap<>();

    @Override
    public boolean load()
    {
        List<RoleBean> list = SystemDataComponent.getBeanList(RoleBean.class);
        roleBeanMap.clear();
        for (RoleBean bean : list)
        {
            roleBeanMap.put(bean.getObjectID(), bean);
        }
        return true;
    }

	@Override
	public boolean reload()
	{
		lock.writeLock().lock();
		try
		{
			List<RoleBean> list = SystemDataComponent.getBeanList(RoleBean.class);
			for (RoleBean bean : list)
			{
				if (roleBeanMap.containsKey(bean.getObjectID()))
				{
					roleBeanMap.get(bean.getObjectID()).reset(bean);
				}
				else
				{
					roleBeanMap.put(bean.getObjectID(), bean);
				}
			}

			return true;
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	public static List<RoleBean> getAll()
	{
		lock.readLock().lock();
		try
		{
			List<RoleBean> list = new ArrayList<>();
			list.addAll(roleBeanMap.values());
			return list;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	public static RoleBean getRoleBean(int objectID)
	{
		lock.readLock().lock();
		try
		{
			RoleBean bean = roleBeanMap.get(objectID);
			if (bean == null)
				LogFactory.error("RoleBean is Null.objectID:{}", objectID);
			return bean;
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
 }

