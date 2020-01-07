/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.util.JsonUtil;

/**
 * 公告
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/notice", description = "公告")
public class NoticeServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    static class Req
    {
        public int noticeType;
        public String languageType;
    }

    static class Res
    {
        public String content;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = JsonUtil.parseStringToObject(jsonString, Req.class);
        String content = "";
        if (req != null)
        {
            content = ServerComponent.getContent(req.noticeType, req.languageType);
        }

        Res res = new Res();
        res.content = content;
        String msg = gson.toJson(res);
        return gson.toJson(new RetInfo(ErrorCodeType.Success, "", msg));
    }
}
