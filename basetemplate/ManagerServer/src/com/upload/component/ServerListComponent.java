package com.upload.component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.upload.data.ServerType;
import com.upload.data.business.ServerListBusiness;
import com.upload.data.data.ServerData;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.LogFactory;

public class ServerListComponent extends AbstractComponent
{
    public static int Wait_Time = 3 * 60 * 1000;

    /** 《服务器逻辑ID，服务器数据》 */
    private static Map<Integer, ServerListData> serverMap = new ConcurrentHashMap<>();

    private static ScheduledExecutorService scheduler;

    @Override
    public boolean initialize()
    {
        List<ServerListData> serverList = ServerListBusiness.listAll();
        serverList.forEach(p -> serverMap.put(p.getServerID(), p));

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(() -> refreshServerList(), 60, 60, TimeUnit.SECONDS);

        // 刷新服务器信息
        refreshServerList();

        return true;
    }

    /**
     * 刷新服务器信息(如果已经删除/被合服,删除;如果新加,添加;如果修改,修改)
     * 
     * @return
     */
    public static boolean refreshServerList()
    {
        LogFactory.error("定时任务:刷新服务器信息开始");
        try
        {
            // 取得所有信息
            List<ServerListData> serverList = getAllServerList();

            String url = GlobalConfigComponent.getConfig().server.url;
            String userName = GlobalConfigComponent.getConfig().server.username;
            String password = GlobalConfigComponent.getConfig().server.password;

            // 账号服取得的服务器列表
            List<ServerData> list = getServerList(url, userName, password);
            if (list == null || list.isEmpty())
            {
                LogFactory.error("********刷新服务器列表异常.**************");
                return false;
            }

            // 合服删除的
            List<ServerListData> delList = new ArrayList<>();

            // 判断需要删除的服务器信息
            for (ServerListData serverListData : serverList)
            {
                if (serverListData.getType() == ServerType.GAME_SERVER)
                {
                    boolean isExist = false;
                    for (ServerData data : list)
                    {
                        if (data.getServerID() == serverListData.getServerID())
                        {
                            isExist = true;
                            break;
                        }
                    }

                    if (isExist == false)
                        delList.add(serverListData);
                }
            }

            // 删除被合服的信息
            for (ServerListData p : delList)
            {
                serverMap.remove(p.getServerID());
                ServerListBusiness.deleteData(p.getServerID());
            }

            // 添加或者更新服务器
            for (ServerData data : list)
            {
                if (!serverMap.containsKey(data.getServerID()))
                {
                    ServerListData listData = new ServerListData();
                    listData.setServerID(data.getServerID());
                    listData.setServerIp(data.getHost());
                    listData.setServerName(data.getServerName());
                    listData.setGamePort(data.getGamePort());
                    listData.setGatePort(data.getWebPort());
                    listData.setType(ServerType.GAME_SERVER);
                    listData.setOutState(data.getState());
                    listData.setServerGroup(0);

                    String serverPath = String.format("%04d", data.getServerID());

                    listData.setDbAddress("/data/mysql_3306");
                    listData.setDbIp(data.getHost());
                    listData.setDbPassword("jump_admin_!#$@1980");
                    listData.setDbPort("12000");
                    listData.setDbStartAddress("sh /usr/local/shell/mysql.sh");
                    listData.setDbUsername("daomu2_admin");
                    listData.setDbGameName("db_game_tlcs_" + serverPath);
                    listData.setDbLogName("db_game_tlcs_log_" + serverPath);

                    listData.setLastOpenTime(new Date());
                    listData.setLastUpdateTime(new Date());

                    listData.setServerAddress("/data/server/gameserver" + serverPath);
                    listData.setGateAddress("/data/server/gatewayserver" + serverPath);

                    listData.setSSHKeyPassword("db_jump_2019");
                    listData.setSSHKeyPath(GlobalConfigComponent.getConfig().server.keyName);
                    listData.setSSHPassword("123456");
                    listData.setSSHPort(5188);
                    listData.setSSHUsername("admin");

                    serverMap.put(data.getServerID(), listData);

                    ServerListBusiness.addOrUpdate(listData);
                }
                else
                {
                    ServerListData listData = serverMap.get(data.getServerID());
                    if (listData != null)
                    {
                        listData.setServerID(data.getServerID());
                        listData.setServerIp(data.getHost());
                        listData.setServerName(data.getServerName());
                        listData.setGamePort(data.getGamePort());
                        listData.setGatePort(data.getWebPort());
                        listData.setType(ServerType.GAME_SERVER);
                        listData.setOutState(data.getState());
                        listData.setLastOpenTime(new Date());
                        listData.setDbIp(data.getHost());
                        listData.setServerGroup(0);
                        ServerListBusiness.addOrUpdate(listData);
                    }
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

    /**
     * 刷新单个服务器的游戏/网关状态
     * 
     * @param serverID
     * @return
     */
    public static ServerListData refreshSingleServerState(int serverID)
    {
        ServerListData data = getServerByServerID(serverID);
        if (data == null)
            return null;

        int port1 = data.getGamePort();
        int port2 = data.getGatePort();

        JSchUtil jsSchUtils = new JSchUtil();
        // 检测游戏端口
        boolean result1 = false;
        boolean result2 = false;

        if (jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                data.getSSHKeyPassword(), data.getSSHPassword()))
        {
            if (port1 > 0)
            {
                String command1 = "netstat -ntulp|grep " + port1;
                result1 = jsSchUtils.execStatusCmd(command1);
            }

            if (port2 > 0)
            {
                String command2 = "netstat -ntulp|grep " + port2;
                result2 = jsSchUtils.execStatusCmd(command2);
            }
        }

        jsSchUtils.close();

        if (result1)
            data.setStatus(0);
        else
            data.setStatus(1);

        if (result2)
            data.setGateStatus(0);
        else
            data.setGateStatus(1);

        return data;
    }

    @Override
    public void stop()
    {
        serverMap.clear();
        scheduler.shutdownNow();
    }

    private static List<ServerData> getServerList(String url, String user, String password)
    {
        // 声明Connection对象
        Connection con;
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";

        List<ServerData> list = new ArrayList<>();
        try
        {
            // 加载驱动程序
            Class.forName(driver);
            // 1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM t_p_server;";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                ServerData data = new ServerData();
                data.setGamePort(rs.getInt("GamePort"));
                data.setHost(rs.getString("Host"));
                data.setRegisterCount(rs.getInt("RegisterCount"));
                data.setServerID(rs.getInt("ServerID"));
                data.setServerName(rs.getString("ServerName"));
                data.setState(rs.getInt("State"));
                data.setWebPort(rs.getInt("WebPort"));

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

    public static List<ServerListData> getServerListByServerID(int serverID, int type)
    {
        List<ServerListData> list = new ArrayList<>();

        for (Entry<Integer, ServerListData> data : serverMap.entrySet())
        {
            if (data.getKey().toString().indexOf(serverID + "") >= 0 && (type == 0 || type == data.getValue().getType()))
                list.add(data.getValue());
        }

        return list;
    }

    /**
     * 根据参数取得服务器列表
     * 
     * @param type:账号服,游戏服
     *            0:所有
     * @param state
     *            1:已开服;0:未开服
     * @param group
     *            -1:所有
     * @return
     */
    public static List<ServerListData> getServerListByServerType(int type, int state, int group)
    {
        List<ServerListData> list = new ArrayList<>();

        for (Entry<Integer, ServerListData> data : serverMap.entrySet())
        {
            if (group == -1 || data.getValue().getServerGroup() == group)
            {
                if ((type == 0 || data.getValue().getType() == type)
                        && (state == -1 || (state == 1 && data.getValue().getOutState() > 1)
                                || (state == 0 && data.getValue().getOutState() <= 1)))
                    list.add(data.getValue());
            }
        }

        return list;
    }

    public static ServerListData getServerByServerID(int serverID)
    {
        return serverMap.get(serverID);
    }

    public static List<ServerListData> getAllServerList()
    {
        List<ServerListData> list = new ArrayList<>();
        list.addAll(serverMap.values());
        return list;
    }

    public static boolean addUpdateServerList(ServerListData data)
    {
        serverMap.put(data.getServerID(), data);
        return ServerListBusiness.addOrUpdate(data);
    }

    /**
     * 删除信息
     * 
     * @param ids
     */
    public static void deleteServerList(String ids)
    {
        String[] results = ids.split("\\,");

        for (String str : results)
        {
            if (str.isEmpty() || str == null)
                continue;

            ServerListData data = serverMap.remove(Integer.valueOf(str));
            if (data != null)
            {
                ServerListBusiness.deleteData(data.getServerID());
            }
        }
    }

}
