package com.upload.web.servlet.version;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.VersionData;
import com.upload.util.JsonUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 添加版本
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/addVersion", description = "")
public class VersionAddServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            VersionData req = JsonUtil.parseStringToObject(json, VersionData.class);

            String result = DataComponent.addOrUpdateVersion(req);
            if (result.equalsIgnoreCase("ok"))
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
