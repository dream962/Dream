package com.upload.web.servlet.serverlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.data.ServerListData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-05-07 11:27:10
 * @description
 *              根据条件获取服务器列表
 */
@WebHandleAnnotation(cmdName = "/getServerList", description = "根据条件获取服务器列表")
public class GetServerListServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -4216308403198718082L;

    static class Req
    {
        int type;// 服务器类型
        int status;// 外网状态
        int serverID;
        int group; // 服务器组
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);

        List<ServerListData> list = null;
        if (req == null)
        {
            list = ServerListComponent.getServerListByServerType(0, -1,-1);
        }
        else
        {
            if (req.serverID > 0)
            {
                list = ServerListComponent.getServerListByServerID(req.serverID, req.type);

            }
            else
            {
                list = ServerListComponent.getServerListByServerType(req.type, req.status,req.group);
            }
        }

        return gson.toJson(list);
    }
}
