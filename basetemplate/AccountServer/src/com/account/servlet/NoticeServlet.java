/**
 * 
 */
package com.account.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.data.CommonData;
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
        public int noticeType;// 类型 2:系统公告1:首页公告
        public String languageType;
    }

    static class ResDetail
    {
        public String content;
        public String title;
        public int id;
    }

    static class Res
    {
        public int type;// 类型 2:系统公告1:首页公告
        public List<ResDetail> info = new ArrayList<>();
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        Res res = new Res();

        Req req = JsonUtil.parseStringToObject(jsonString, Req.class);
        if (req != null)
        {
            List<CommonData> list = ServerComponent.getContent(req.noticeType, req.languageType);
            for (CommonData d : list)
            {
                ResDetail detail = new ResDetail();
                detail.id = d.getID();
                detail.content = d.getNoticeMessage();
                detail.title = d.getTitle();
                res.info.add(detail);
            }

            res.type = req.noticeType;
        }

        String msg = gson.toJson(res);
        return gson.toJson(new RetInfo(ErrorCodeType.Success, "", msg));
    }
}
