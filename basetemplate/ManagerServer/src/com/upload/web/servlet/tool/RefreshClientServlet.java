package com.upload.web.servlet.tool;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.upload.component.ServerListComponent;
import com.upload.data.ServerType;
import com.upload.data.data.ServerListData;
import com.upload.util.HttpUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 刷新客户端资源
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/refreshClient", description = "")
public class RefreshClientServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    static class Req
    {
        String id;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);
        String[] ids = req.id.split("\\,");
        int serverID = Integer.parseInt(ids[0]);
        List<ServerListData> list = ServerListComponent.getServerListByServerID(serverID, ServerType.RESOURCE_SERVER);
        String resultStr = "";
        for (ServerListData data : list)
        {
            String reqUrl = String.format("http://%s:%s/reloadTemplate", data.getServerIp(), data.getGamePort());
            String result = HttpUtil.doGet(reqUrl, 120000);

            JSONObject jsonResult = (JSONObject) JSONObject.parse(result);
            resultStr += "请求URL:" + reqUrl + " ,结果:" + jsonResult.getString("result") + "," + jsonResult.getString("msg") + "\n";
        }

        JSONObject returnStr = new JSONObject();
        returnStr.put("result", resultStr);
        return returnStr.toJSONString();
    }
}
