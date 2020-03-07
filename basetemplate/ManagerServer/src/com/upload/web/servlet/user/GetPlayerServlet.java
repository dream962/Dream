package com.upload.web.servlet.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.PlayerInfo;
import com.upload.data.data.ServerData;
import com.upload.util.HttpUtil;
import com.upload.util.JsonUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 玩家信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getPlayerInfo", description = "")
public class GetPlayerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    public static final class Req
    {
        public String name;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Req req = JsonUtil.parseStringToObject(json, Req.class);

            List<PlayerInfo> returnList = new ArrayList<PlayerInfo>();

            List<ServerData> list = DataComponent.getServerData();
            for (ServerData data : list)
            {
                String url = String.format("http://%s:%s/gm/getPlayerList?params={'name':'%s'}",
                        data.getHost(), data.getWebPort(), req.name);

                String result = HttpUtil.doGet(url, 120000);

                List<PlayerInfo> playerInfos = JsonUtil.parseJsonToListObject(result, PlayerInfo.class);
                if (playerInfos != null)
                {
                    for (PlayerInfo playerInfo : playerInfos)
                    {
                        playerInfo.serverData = data;
                        returnList.add(playerInfo);
                    }
                }
            }

            String result = gson.toJson(returnList);
            return result;
        }
        catch (Exception e)
        {
            logger.error("", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
    }
}
