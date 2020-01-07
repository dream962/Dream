package com.upload.web.servlet.sql;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.util.FileUtil;
import com.upload.util.JDBCUtils;
import com.upload.util.JDBCUtils.DataTable;
import com.upload.util.JDBCUtils.TableCell;
import com.upload.util.JDBCUtils.TableColumn;
import com.upload.util.TimeUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 查询sql
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/querySql", description = "")
public class QuerySqlServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    static class Req
    {
        String keyIDs;
        boolean isSave;
        int db;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        String sql = request.getParameter("sql");
        Req req = gson.fromJson(json, Req.class);
        int dbType = req.db;
        boolean isSave = req.isSave;
        isSave = true;

        try
        {
            if (req != null)
            {
                String[] ids = req.keyIDs.split(",");
                if (ids.length <= 0)
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
                }

                String path = System.getProperty("user.dir") + File.separator + "save" + File.separator
                        + TimeUtil.getDateFormat(new Date(), "yyyyMMddHHmmss") + ".csv";
                if (isSave)
                {
                    FileUtil.createFile(path, "查询结果:\n");
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

                    logger.error("当前正在执行SQL的服务器: " + data.getServerName() + " ,ID:" + data.getServerID() + " ,DB:" + dbName);

                    List<DataTable> tables = JDBCUtils.querySql(data.getDbIp(), data.getDbPort(), data.getDbUsername(),
                            data.getDbPassword(), dbName, sql);

                    // 保存
                    if (isSave)
                    {
                        for (DataTable table : tables)
                        {
                            StringBuilder builder = new StringBuilder();
                            builder.append("当前服务器:"+data.getServerID()+" -- "+data.getServerName()).append("\n");
                            for (TableColumn c : table.columns)
                            {
                                builder.append(c.name).append(",");
                            }
                            builder.append("\n");

                            for (TableCell cell : table.cells)
                            {
                                for (Object o : cell.value)
                                {
                                    builder.append(o).append(",");
                                }
                                builder.append("\n");
                            }
                            FileUtil.writeFileContent(path, builder.toString());
                        }
                    }

                    logger.error("区服执行完毕...数据保存:" + path);
                }

                logger.error("查询执行完毕...数据保存:" + path);
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
