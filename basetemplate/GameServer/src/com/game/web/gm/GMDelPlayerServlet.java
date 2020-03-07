package com.game.web.gm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.game.component.GamePlayerComponent;
import com.google.gson.JsonObject;
import com.util.print.LogFactory;

/**
 * 删除玩家信息
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/gm/delPlayer", description = "")
public class GMDelPlayerServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 1;

    public static class Req
    {
        public int userID;
    }

    @Override
    protected String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            int userID = jsonObject.get("userID").getAsInt();
            boolean result = GamePlayerComponent.deletePlayer(userID);
            if (result)
                return "ok";
            else
                return "fail";
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }

        return "";
    }
}
