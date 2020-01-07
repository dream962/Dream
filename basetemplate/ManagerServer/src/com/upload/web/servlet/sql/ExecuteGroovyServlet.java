package com.upload.web.servlet.sql;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.util.HttpUtil;
import com.upload.util.JDBCUtils;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 执行groovy脚本
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/executeGroovy", description = "")
public class ExecuteGroovyServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    static class Req
    {
        String keyIDs;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        String groovy = request.getParameter("groovy");
        Req req = gson.fromJson(json, Req.class);

        try
        {
            StringBuilder builder = new StringBuilder();

            if (req != null)
            {
                String[] ids = req.keyIDs.split(",");
                if (ids.length <= 0 || groovy == null || groovy.isEmpty())
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
                }

                groovy = URLEncoder.encode(groovy, "UTF-8");
                for (String id : ids)
                {
                    int serverID = Integer.parseInt(id);
                    ServerListData data = ServerListComponent.getServerByServerID(serverID);

                    String url = String.format("http://%s:%s/gm/script?params={'script':'%s'}", data.getServerIp(), data.getGamePort(),
                            groovy);
                    logger.error("当前正在执行Groovy的服务器:" + data.getServerName() + ",ID:" + data.getServerID());
                    String result = HttpUtil.doGet(url, 60000);

                    builder.append("服务器:" + data.getServerGroup() + "-" + data.getServerName()).append(",IP:" + data.getServerIp()).append(
                            "\n");
                    builder.append("执行结果:" + result).append("\n\n");

                    logger.error("执行结果:" + result);
                }
            }

            return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, builder.toString()));
        }
        catch (Exception e)
        {
            logger.error("执行Groovy错误..", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
        finally
        {
            JDBCUtils.close();
        }
    }
}
