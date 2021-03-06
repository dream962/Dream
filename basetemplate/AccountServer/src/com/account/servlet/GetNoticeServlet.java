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
import com.data.account.data.CommonData;
import com.util.JsonUtil;

/**
 * 取得公告
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getNotice", description = "公告")
public class GetNoticeServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        List<CommonData> list = ServerComponent.getCommonDataList();
        String json = JsonUtil.parseObjectToString(list);
        return json;
    }
}
