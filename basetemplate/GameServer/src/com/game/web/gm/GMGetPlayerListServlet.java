package com.game.web.gm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.info.PlayerInfo;
import com.game.component.GamePlayerComponent;
import com.google.gson.JsonObject;
import com.util.JsonUtil;
import com.util.print.LogFactory;

/**
 * 查询玩家信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/gm/getPlayerList", description = "")
public class GMGetPlayerListServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 1;

    public static class Req
    {
        public String name;
    }

    @Override
    protected String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            String name = jsonObject.get("name").getAsString();

            List<PlayerInfo> playerInfos = GamePlayerComponent.getPlayerByName(name);
            String result = "";
            if (playerInfos != null)
                result = JsonUtil.parseObjectToString(playerInfos);
            else
                result = "";

            return result;
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return null;
    }

    public interface RetCode
    {
        public static final int OK = 0;
        public static final int ERROR = 1;
        public static final int ITEM_ERROR = 2;
        public static final int USER_ERROR = 3;

        public static final int ERROR_SERVER_INTERNAL = 405;
    }
}
