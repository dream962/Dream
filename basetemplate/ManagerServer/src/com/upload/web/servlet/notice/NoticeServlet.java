package com.upload.web.servlet.notice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.NoticeData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 公告列表
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/noticeServer", description = "")
public class NoticeServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<NoticeData> list = DataComponent.getAllNoticeList();
            return gson.toJson(list);
        }
        catch (Exception e)
        {
            logger.error("", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
    }
}
