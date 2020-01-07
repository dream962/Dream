package com.upload.web.servlet.serverlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 查询出服务器信息并跳转到修改页面
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/editServer", description = "")
public class EditServerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -341466288537316619L;

    static class Req
    {
        int serverID;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);
        try
        {
            if (req != null)
            {
                ServerListData data = ServerListComponent.getServerByServerID(req.serverID);
                if (data == null)
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "找不到对应的服务器配置..."));
                }
                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, data));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器错误..."));
    }

}
