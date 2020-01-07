package com.upload.web.servlet.serverlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.data.ServerListData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 刷新服务器列表
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/deleteServerList", description = "")
public class DeleteServerListServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;
    
    public static final class Req
    {
        public String ids;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);
        ServerListComponent.deleteServerList(req.ids);
        List<ServerListData> list = ServerListComponent.getServerListByServerType(0, -1,-1);
        return gson.toJson(list);
    }

}
