package com.upload.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.upload.data.data.NoticeData;
import com.upload.util.HttpUtil;
import com.upload.util.JsonUtil;
import com.upload.util.LogFactory;

public class DataComponent extends AbstractComponent
{
    private static List<NoticeData> noticeList = new ArrayList<>();

    private static ScheduledExecutorService scheduler;

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public boolean initialize()
    {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(() -> refreshServerList(), 0, 60, TimeUnit.SECONDS);
        return true;
    }

    public static List<NoticeData> getAllNoticeList()
    {
        List<NoticeData> list = new ArrayList<>();
        list.addAll(noticeList);
        return list;
    }

    public static String addOrUpdateNotice(NoticeData data)
    {
        boolean isContain = false;
        for (NoticeData temp : noticeList)
        {
            if (data.getNoticeType() == temp.getNoticeType() && data.getLanguageType().equalsIgnoreCase(temp.getLanguageType()))
            {
                temp.setLanguageType(data.getLanguageType());
                temp.setNoticeMessage(data.getNoticeMessage());
                temp.setNoticeType(data.getNoticeType());

                isContain = true;
                break;
            }
        }

        if (isContain == false)
        {
            noticeList.add(data);
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;

        String json = JsonUtil.parseObjectToString(data);
        String reqUrl = url + "/addNotice?params=" + json;
        String result = HttpUtil.doGet(reqUrl, 120000);
        return result;
    }

    public static String removeNotice(int noticeType, String languageType)
    {
        for (NoticeData data : noticeList)
        {
            if (data.getNoticeType() == noticeType && data.getLanguageType().equalsIgnoreCase(languageType))
            {
                noticeList.remove(data);
                break;
            }
        }

        String url = GlobalConfigComponent.getConfig().server.serverUrl;

        String reqUrl = url + "/removeNotice?params={'noticeType':" + noticeType + ",'languageType':'" + languageType + "'}";
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
            url = url + "/getNotice";
            String json = HttpUtil.doGet(url);
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
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        LogFactory.error("定时任务:刷新服务器信息结束");

        return true;
    }
}
