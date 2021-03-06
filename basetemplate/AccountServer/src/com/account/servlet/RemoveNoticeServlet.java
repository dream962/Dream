/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.util.JsonUtil;

/**
 * 删除公告
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/removeNotice", description = "公告")
public class RemoveNoticeServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    static class Req
    {
        public int id;
        public int type;
        public String language;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = JsonUtil.parseStringToObject(jsonString, Req.class);
        String content = "fail";
        if (req != null)
        {
            content = ServerComponent.removeNotice(req.id, req.type, req.language);
        }

        return content;
    }
}
