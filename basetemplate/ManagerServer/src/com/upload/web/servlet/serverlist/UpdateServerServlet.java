package com.upload.web.servlet.serverlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upload.adapter.IntegerDefault0Adapter;
import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerListData;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 更新server数据
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/updateServer", description = "更新server数据")
public class UpdateServerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -6439653831066106333L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        // 注册适配器，解决Gson无法转换空字符串为Integer(int)类型
        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerDefault0Adapter()).registerTypeAdapter(int.class,
                new IntegerDefault0Adapter()).setDateFormat("yyyy-MM-dd HH:mm").create();
        ServerListData data = gson.fromJson(json, ServerListData.class);

        boolean result = ServerListComponent.addUpdateServerList(data);
        if (result)
        {
            return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, "success"));
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
    }
}
