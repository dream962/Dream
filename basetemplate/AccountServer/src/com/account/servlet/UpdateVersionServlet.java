/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.data.VersionData;

/**
 * 更新版本信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/updateVersion", description = "版本")
public class UpdateVersionServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        VersionData requestInfo = gson.fromJson(jsonString, VersionData.class);
        if (requestInfo != null)
        {
            boolean result = ServerComponent.addOrUpdateVersion(requestInfo);
            if (result)
                return "ok";

            return "fail";
        }

        return "fail";
    }
}
