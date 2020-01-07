package com.upload.web.servlet.serverlist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.business.ServerListBusiness;
import com.upload.data.data.ServerListData;
import com.upload.util.StringUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-28 11:31:03
 * @description
 *		根据ids获取服务器列表
 */
@WebHandleAnnotation(cmdName = "/getServers", description = "根据ids获取服务器列表")
public class GetServersServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -4775739369795748004L;

    static class Req
    {
        String keyIDs;
    }
    
    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);
        if (req != null && !StringUtil.isNullOrEmpty(req.keyIDs))
        {
            String[] ids = req.keyIDs.split(",");
            List<ServerListData> list = new ArrayList<>();
            for (String id : ids)
            {
                ServerListData data = ServerListBusiness.getServerByKeyID(Integer.parseInt(id));
                list.add(data);
            }
            return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, list));
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
    }

}
