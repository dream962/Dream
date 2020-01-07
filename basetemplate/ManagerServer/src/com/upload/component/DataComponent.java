package com.upload.component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
        scheduler.scheduleWithFixedDelay(() -> refreshServerList(), 0, 20, TimeUnit.SECONDS);
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
            String url = GlobalConfigComponent.getConfig().server.url;
            String userName = GlobalConfigComponent.getConfig().server.username;
            String password = GlobalConfigComponent.getConfig().server.password;

            // 公告列表
            List<NoticeData> list = getNoticeList(url, userName, password);
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

    private static List<NoticeData> getNoticeList(String url, String user, String password)
    {
        // 声明Connection对象
        Connection con;
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";

        List<NoticeData> list = new ArrayList<>();
        try
        {
            // 加载驱动程序
            Class.forName(driver);
            // 1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM t_p_common;";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                NoticeData data = new NoticeData();
                data.setNoticeType(rs.getInt("NoticeType"));
                data.setLanguageType(rs.getString("LanguageType"));
                data.setNoticeMessage(rs.getString("NoticeMessage"));

                list.add(data);
            }
            rs.close();
            con.close();
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
            return null;
        }

        return list;
    }

}
