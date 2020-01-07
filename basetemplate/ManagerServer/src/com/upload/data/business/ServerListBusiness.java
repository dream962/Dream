package com.upload.data.business;

import java.sql.Types;
import java.util.List;

import com.upload.data.data.OpenServerData;
import com.upload.data.data.ServerListData;
import com.upload.data.factory.OpenServerDataFactory;
import com.upload.data.factory.ServerListDataFactory;
import com.upload.database.DBParamWrapper;

/**
 * 
 * @date 2018-04-25 10:51:44
 * @description
 *              获取服务器列表
 */
public class ServerListBusiness
{

    public static List<ServerListData> getByType(int type)
    {
        String sql = "select * from t_p_server_list where `type` = ? order by `ServerID`";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, type);
        return ServerListDataFactory.getDao().queryList(sql, params);
    }

    public static List<ServerListData> listAll()
    {
        String sql = "select * from t_p_server_list order by `ServerID`";
        return ServerListDataFactory.getDao().queryList(sql);
    }

    public static ServerListData getServerByKeyID(int serverID)
    {
        String sql = "select * from t_p_server_list where `ServerID` = ?";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, serverID);
        return ServerListDataFactory.getDao().query(sql, params);
    }

    public static boolean addOrUpdate(ServerListData data)
    {
        return ServerListDataFactory.getDao().addOrUpdate(data);
    }

    public static List<ServerListData> getByStatus(int status)
    {
        String sql = "select * from t_p_server_list where `status` = ? order by `ServerID`";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, status);
        return ServerListDataFactory.getDao().queryList(sql, params);
    }

    public static List<ServerListData> getByStatusAndType(int status, int type)
    {
        String sql = "select * from t_p_server_list where `status` = ? and `type` = ? order by `ServerID`";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, status);
        params.put(Types.INTEGER, type);
        return ServerListDataFactory.getDao().queryList(sql, params);
    }

    public static void deleteData(int serverID)
    {
        String sql = "delete from t_p_server_list where `ServerID` = ?;";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, serverID);
        ServerListDataFactory.getDao().execute(sql, params);
    }

    public static List<OpenServerData> getAllOpenList()
    {
        return OpenServerDataFactory.getDao().listAll();
    }

    public static void addOrUpdateOpenServer(OpenServerData data)
    {
        OpenServerDataFactory.getDao().addOrUpdate(data);
    }
    
    public static void deleteOpen(int serverID)
    {
        String sql = "delete from t_p_open_server where `ServerID` = ?;";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, serverID);
        OpenServerDataFactory.getDao().execute(sql, params);
    }
}
