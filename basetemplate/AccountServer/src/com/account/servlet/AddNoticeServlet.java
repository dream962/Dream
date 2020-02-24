/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.data.CommonData;
import com.util.JsonUtil;

/**
 * 添加公告
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/addNotice", description = "公告")
public class AddNoticeServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        CommonData req = JsonUtil.parseStringToObject(jsonString, CommonData.class);
        String content = "fail";
        if (req != null)
        {
            content = ServerComponent.addNotice(req);
        }

        return content;
    }
}
