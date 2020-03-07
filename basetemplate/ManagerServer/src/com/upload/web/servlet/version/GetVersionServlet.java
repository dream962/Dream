package com.upload.web.servlet.version;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.VersionData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 版本列表
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getVersion", description = "")
public class GetVersionServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<VersionData> list = DataComponent.getAllVersionList();
            return gson.toJson(list);
        }
        catch (Exception e)
        {
            logger.error("", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
    }
}
