/**
 * 
 */
package com.account.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.base.component.AbstractComponent;
import com.data.account.data.CommonData;
import com.data.account.data.ServerData;
import com.data.account.data.VersionData;
import com.data.account.factory.CommonDataFactory;
import com.data.account.factory.ServerDataFactory;
import com.data.account.factory.VersionDataFactory;
import com.util.ThreadPoolUtil;
import com.util.print.LogFactory;

/**
 * 服务器管理组件
 * 
 * @author dream
 *
 */
public class ServerComponent extends AbstractComponent
{
    private static ScheduledExecutorService schedule;

    /** 服务器列表 */
    private static Map<Integer, ServerData> serverMap = new ConcurrentHashMap<>();

    /** 公告 */
    private static List<CommonData> commonDataList = new ArrayList<>();

    /** 版本 */
    private static List<VersionData> versionList = new ArrayList<>();

    private static ReadWriteLock noticeLock = new ReentrantReadWriteLock();

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean initialize()
    {
        schedule = ThreadPoolUtil.singleScheduledExecutor("account-server");
        schedule.scheduleAtFixedRate(() -> updateServer(), 30, 30, TimeUnit.SECONDS);

        List<VersionData> versionTemps = VersionDataFactory.getDao().listAll();
        if (versionTemps != null)
            versionList.addAll(versionTemps);

        // 初始化服务器列表
        updateServer();

        return true;
    }

    public static List<VersionData> getVersionList()
    {
        lock.readLock().lock();
        try
        {
            return new ArrayList<VersionData>(versionList);
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public static boolean getVersionForce(int version)
    {
        lock.readLock().lock();
        try
        {
            for (VersionData data : versionList)
            {
                if (data.getVersionID() > version)
                {
                    if (data.getIsForce())
                        return true;
                }
            }

            return false;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public static int getMaxVersion()
    {
        int max = 0;
        lock.readLock().lock();
        try
        {
            for (VersionData data : versionList)
            {
                if (data.getVersionID() > max)
                {
                    max = data.getVersionID();
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return max;
    }

    public static List<CommonData> getContent(int type, String languageType)
    {
        List<CommonData> list = new ArrayList<>();
        noticeLock.readLock().lock();
        try
        {
            for (CommonData data : commonDataList)
            {
                if (data.getNoticeType() == type && data.getLanguageType().equalsIgnoreCase(languageType))
                {
                    list.add(data);
                }
            }
        }
        finally
        {
            noticeLock.readLock().unlock();
        }
        return list;
    }

    public static List<CommonData> getCommonDataList()
    {
        noticeLock.readLock().lock();
        try
        {
            return new ArrayList<>(commonDataList);
        }
        finally
        {
            noticeLock.readLock().unlock();
        }
    }

    public static String addNotice(CommonData data)
    {
        noticeLock.writeLock().lock();
        try
        {
            boolean isContain = false;
            for (CommonData temp : commonDataList)
            {
                if (data.getNoticeType() == temp.getNoticeType() && data.getLanguageType() == temp.getLanguageType()
                        && data.getID() == temp.getID())
                {
                    temp.setNoticeMessage(data.getNoticeMessage());
                    CommonDataFactory.getDao().addOrUpdate(temp);
                    isContain = true;
                }
            }

            if (isContain == false)
            {
                CommonDataFactory.getDao().addOrUpdate(data);
                commonDataList.add(data);
            }

            return "success";
        }
        finally
        {
            noticeLock.writeLock().unlock();
        }
    }

    public static String removeNotice(int id)
    {
        noticeLock.writeLock().lock();
        try
        {
            for (CommonData temp : commonDataList)
            {
                if (id == temp.getID())
                {
                    commonDataList.remove(temp);
                    CommonDataFactory.getDao().delete(temp);
                    return "success";
                }
            }

            return "fail";
        }
        finally
        {
            noticeLock.writeLock().unlock();
        }
    }

    private static void updateServer()
    {
        try
        {
            List<ServerData> list = ServerDataFactory.getDao().listAll();
            list.forEach(p -> {
                serverMap.put(p.getServerID(), p);
            });

            List<VersionData> versionTemps = VersionDataFactory.getDao().listAll();
            lock.writeLock().lock();
            try
            {
                if (versionTemps != null)
                {
                    versionList.clear();
                    versionList.addAll(versionTemps);
                }
            }
            finally
            {
                lock.writeLock().unlock();
            }

            List<CommonData> dataList = CommonDataFactory.getDao().queryList("select * from t_p_common");
            if (dataList != null)
            {
                noticeLock.writeLock().lock();
                try
                {
                    commonDataList.clear();
                    commonDataList.addAll(dataList);
                }
                finally
                {
                    noticeLock.writeLock().unlock();
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }

    /**
     * 选择人数最少的服务器
     * 
     * @param isTest:true-测试环境；false：正式环境
     * @return
     */
    public static ServerData choiceServer(boolean isTest)
    {
        ServerData data1 = null;

        // 如果是测试数据
        if (isTest)
        {
            for (ServerData data : serverMap.values())
            {
                if (data.getState() == 3)
                    return data;
            }
        }
        else
        {
            for (ServerData data : serverMap.values())
            {
                if (data.getState() == 1)
                {
                    if (data1 == null || data.getRegisterCount() < data1.getRegisterCount())
                        data1 = data;
                }
            }
        }

        return data1;
    }

    public static List<ServerData> getServerList()
    {
        List<ServerData> list = new ArrayList<>();
        list.addAll(serverMap.values());
        return list;
    }

    @Override
    public void stop()
    {
        serverMap.clear();
    }

    public static boolean addOrUpdateVersion(VersionData data)
    {
        lock.writeLock().lock();
        try
        {
            for (VersionData v : versionList)
            {
                if (v.getVersionID() == data.getVersionID())
                {
                    v.setIsForce(data.getIsForce());
                    v.setVersionDesc(data.getVersionDesc());
                    VersionDataFactory.getDao().addOrUpdate(v);
                    return true;
                }
            }

            versionList.add(data);

            return VersionDataFactory.getDao().addOrUpdate(data);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public static boolean deleteVersion(int version)
    {
        lock.writeLock().lock();
        try
        {
            for (VersionData v : versionList)
            {
                if (v.getVersionID() == version)
                {
                    versionList.remove(v);
                    return VersionDataFactory.getDao().deleteByKey(version);
                }
            }

            return VersionDataFactory.getDao().deleteByKey(version);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
}
