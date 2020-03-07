/**
 * 
 */
package com.account.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.data.ServerData;

/**
 * 取得服务器信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getServerList", description = "")
public class GetServerListServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        List<ServerData> list = ServerComponent.getServerList();
        return gson.toJson(list);
    }
}
