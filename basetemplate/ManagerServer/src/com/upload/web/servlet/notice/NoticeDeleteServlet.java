package com.upload.web.servlet.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.util.JsonUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 删除信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/deleteNotice", description = "")
public class NoticeDeleteServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    public static final class Req
    {
        public String keys;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = JsonUtil.parseStringToObject(json, Req.class);
        String result = "";
        String[] keys = req.keys.split("\\,");
        for (String key : keys)
        {
            result = DataComponent.removeNotice(key);
        }

        if (result.equalsIgnoreCase("success"))
            return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, result));
        else
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, result));
    }
}
