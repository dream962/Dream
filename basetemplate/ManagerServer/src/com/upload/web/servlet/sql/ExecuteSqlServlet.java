package com.upload.web.servlet.sql;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.util.JDBCUtils;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-05-24 14:56:44
 * @description
 *              连接相应数据库并执行sql语句
 */
@WebHandleAnnotation(cmdName = "/executeSql", description = "连接相应数据库并执行sql语句")
public class ExecuteSqlServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    static class Req
    {
        String keyIDs;
        boolean isSave;
        int db;
    }

    @Override
    public String  execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        String sql = request.getParameter("sql");
        Req req = gson.fromJson(json, Req.class);
        int dbType = req.db;

        try
        {
            if (req != null)
            {
                String[] ids = req.keyIDs.split(",");
                if (ids.length <= 0)
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
                }

                for (String id : ids)
                {
                    int serverID = Integer.parseInt(id);
                    ServerListData data = ServerListComponent.getServerByServerID(serverID);

                    String dbName = data.getDbGameName();
                    switch (dbType)
                    {
                    case 1:// game
                        dbName = data.getDbGameName();
                        break;

                    case 2:// log
                        dbName = data.getDbLogName();
                        break;

                    case 3:// mart
                        dbName = data.getDbMartName();
                        break;

                    default:
                        dbName = data.getDbGameName();
                        break;
                    }

                    logger.error("当前正在执行SQL的服务器:" + data.getServerName() + ",ID:" + data.getServerID());
                    JDBCUtils.excuteSql(data.getDbIp(), data.getDbPort(), data.getDbUsername(), data.getDbPassword(), dbName, sql);
                    logger.error("执行完毕...");
                }
            }

            return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, "success"));
        }
        catch (Exception e)
        {
            logger.error("执行sql错误..", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
        finally
        {
            JDBCUtils.close();
        }
    }
}
