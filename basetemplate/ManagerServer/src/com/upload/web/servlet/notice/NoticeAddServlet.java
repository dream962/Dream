package com.upload.web.servlet.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.NoticeData;
import com.upload.util.JsonUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 添加信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/addNotice", description = "")
public class NoticeAddServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            NoticeData req = JsonUtil.parseStringToObject(json, NoticeData.class);

            String result = DataComponent.addOrUpdateNotice(req);
            if (result.equalsIgnoreCase("success"))
                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, result));
            else
                return gson.toJson(new ResponseInfo(ResponseCode.ERROR, result));
        }
        catch (Exception e)
        {
            logger.error("", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
    }
}
