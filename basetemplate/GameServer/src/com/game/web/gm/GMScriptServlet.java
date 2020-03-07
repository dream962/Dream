package com.game.web.gm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.component.ScriptComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.game.web.RetInfo;
import com.google.gson.JsonObject;
import com.util.print.LogFactory;

/**
 * 修改脚本
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/gm/script", description = "")
public class GMScriptServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 1;

    public static class Req
    {
        public String script;
    }

    @Override
    protected String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            String script = jsonObject.get("script").getAsString();

            String result = "";
            if (result != null)
                result = ScriptComponent.evaluate(script);

            return gson.toJson(new RetInfo(RetCode.OK, "脚本完成:" + result));
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
