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
import com.data.account.data.VersionData;

/**
 * 取得版本信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getVersionList", description = "版本")
public class GetVersionServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        List<VersionData> list = ServerComponent.getVersionList();
        return gson.toJson(list);
    }
}
