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
@WebHandleAnnotation(cmdName = "/refreshServerList", description = "")
public class RefreshServerListServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        ServerListComponent.refreshServerList();
        List<ServerListData> list = ServerListComponent.getAllServerList();
        return gson.toJson(list);
    }
}
