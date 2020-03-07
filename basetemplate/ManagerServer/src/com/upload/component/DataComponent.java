package com.upload.component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.upload.data.data.NoticeData;
import com.upload.data.data.ServerData;
import com.upload.data.data.VersionData;
import com.upload.util.HttpUtil;
import com.upload.util.JsonUtil;
import com.upload.util.LogFactory;

public class DataComponent extends AbstractComponent
{
    private static List<NoticeData> noticeList = new ArrayList<>();

    private static List<VersionData> versionList = new ArrayList<>();

    private static List<ServerData> serverList = new ArrayList<>();

    private static ScheduledExecutorService scheduler;

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean initialize()
    {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(() -> refreshServerList(), 0, 60, TimeUnit.SECONDS);
        return true;
    }

    public static List<ServerData> getServerData()
    {
        List<ServerData> list = new ArrayList<>();

        lock.readLock().lock();
        try
        {
            for (ServerData data : serverList)
            {
                if (data.getState() == 1)
                {
                    list.add(data);
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }

        return list;
    }

    public static ServerData getServerData(int serverID)
    {
        lock.readLock().lock();
        try
        {
            for (ServerData data : serverList)
            {
                if (data.getServerID() == serverID)
                {
                    return data;
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }

        return null;
    }

    public static List<NoticeData> getAllNoticeList()
    {
        lock.readLock().lock();
        try
        {
            List<NoticeData> list = new ArrayList<>();
            list.addAll(noticeList);
            return list;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public static String addOrUpdateNotice(NoticeData data)
    {
        lock.writeLock().lock();
        try
        {
            boolean isContain = false;
            for (NoticeData temp : noticeList)
            {
                if (data.getNoticeType() == temp.getNoticeType() && data.getLanguageType().equalsIgnoreCase(temp.getLanguageType())
                        && data.getID() == temp.getID())
                {
                    temp.setLanguageType(data.getLanguageType());
                    temp.setNoticeMessage(data.getNoticeMessage());
                    temp.setNoticeType(data.getNoticeType());
                    temp.setTitle(data.getTitle());

                    isContain = true;
                    break;
                }
            }

            if (isContain == false)
            {
                noticeList.add(data);
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;

        String json = JsonUtil.parseObjectToString(data);
        try
        {
            json = URLEncoder.encode(json, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LogFactory.error("", e);
        }
        String reqUrl = url + "/addNotice?params=" + json;
        String result = HttpUtil.doGet(reqUrl, 120000);
        return result;
    }

    public static String removeNotice(int ID)
    {
        lock.writeLock().lock();
        try
        {
            for (NoticeData data : noticeList)
            {
                if (data.getID() == ID)
                {
                    noticeList.remove(data);
                    break;
                }
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;

        String reqUrl = url + "/removeNotice?params={'id':" + ID + "}";
        String result = HttpUtil.doGet(reqUrl, 120000);
        return result;
    }

    public static List<VersionData> getAllVersionList()
    {
        lock.readLock().lock();
        try
        {
            List<VersionData> list = new ArrayList<>();
            list.addAll(versionList);
            return list;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public static String addOrUpdateVersion(VersionData data)
    {
        lock.writeLock().lock();
        try
        {
            boolean isContain = false;
            for (VersionData temp : versionList)
            {
                if (data.getVersionID() == temp.getVersionID())
                {
                    temp.setVersionDesc(data.getVersionDesc());
                    temp.setIsForce(data.getIsForce());
                    isContain = true;
                    break;
                }
            }

            if (isContain == false)
            {
                versionList.add(data);
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;
        String json = JsonUtil.parseObjectToString(data);
        String reqUrl = url + "/updateVersion?params=" + json;
        String result = HttpUtil.doGet(reqUrl, 120000);
        return result;
    }

    public static String removeVersion(int version)
    {
        lock.writeLock().lock();
        try
        {
            for (VersionData data : versionList)
            {
                if (data.getVersionID() == version)
                {
                    versionList.remove(data);
                    break;
                }
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;
        String reqUrl = url + "/delVersion?params={'versionID':" + version + "}";
        String result = HttpUtil.doGet(reqUrl, 120000);
        return result;
    }

    @Override
    public void stop()
    {
        noticeList.clear();
        scheduler.shutdownNow();
    }

    /**
     * 刷新信息
     * 
     * @return
     */
    public static boolean refreshServerList()
    {
        LogFactory.error("定时任务:刷新信息开始");
        try
        {
            String url = GlobalConfigComponent.getConfig().server.serverUrl;
            String noticeUrl = url + "/getNotice";
            String json = HttpUtil.doGet(noticeUrl);
            // 公告列表
            List<NoticeData> list = JsonUtil.parseJsonToListObject(json, NoticeData.class);
            if (list != null && !list.isEmpty())
            {
                lock.writeLock().lock();
                try
                {
                    noticeList.clear();
                    noticeList.addAll(list);
                }
                finally
                {
                    lock.writeLock().unlock();
                }
            }

            String versionUrl = url + "/getVersionList";
            String versionJson = HttpUtil.doGet(versionUrl);
            // 版本列表
            List<VersionData> versionListTemp = JsonUtil.parseJsonToListObject(versionJson, VersionData.class);
            if (versionListTemp != null && !versionListTemp.isEmpty())
            {
                lock.writeLock().lock();
                try
                {
                    versionList.clear();
                    versionList.addAll(versionListTemp);
                }
                finally
                {
                    lock.writeLock().unlock();
                }
            }

            String serverUrl = url + "/getServerList";
            String serverJson = HttpUtil.doGet(serverUrl);
            // 服务器列表
            List<ServerData> serverListTemp = JsonUtil.parseJsonToListObject(serverJson, ServerData.class);
            if (serverListTemp != null && !serverListTemp.isEmpty())
            {
                lock.writeLock().lock();
                try
                {
                    serverList.clear();
                    serverList.addAll(serverListTemp);
                }
                finally
                {
                    lock.writeLock().unlock();
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        LogFactory.error("定时任务:刷新服务器信息结束");

        return true;
    }
}
