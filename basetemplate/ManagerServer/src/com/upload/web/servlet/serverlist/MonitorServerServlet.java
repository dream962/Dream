package com.upload.web.servlet.serverlist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.util.StringUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 实时查询服务器状态
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/monitorServer", description = "")
public class MonitorServerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 4382980168166272342L;

    static class Req
    {
        String keyIDs;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<ServerListData> list = new ArrayList<>();
            Req req = gson.fromJson(json, Req.class);
            if (req != null && !StringUtil.isNullOrEmpty(req.keyIDs))
            {
                String[] ids = req.keyIDs.split(",");
                // 逐个检测服务器状态
                for (String id : ids)
                {
                    if (StringUtil.isNumber(id))
                    {
                        ServerListData data = ServerListComponent.refreshSingleServerState(Integer.valueOf(id));
                        list.add(data);
                    }
                }

                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, list));
            }
        }
        catch (Exception e)
        {
            logger.error("实时查询服务器状态异常...", e);
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
    }
}
