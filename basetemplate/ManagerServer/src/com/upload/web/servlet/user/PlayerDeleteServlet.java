package com.upload.web.servlet.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.DataComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.ServerData;
import com.upload.util.HttpUtil;
import com.upload.util.JsonUtil;
import com.upload.util.StringSplitUtil;
import com.upload.util.StringUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 删除玩家
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/deletePlayer", description = "")
public class PlayerDeleteServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2610082594754872065L;

    public static final class Req
    {
        public String ids;
    }

    public static final class Resp
    {
        public int userID;
        public boolean isDelete;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<Resp> respList = new ArrayList<>();
            Req req = JsonUtil.parseStringToObject(json, Req.class);
            if (req != null)
            {
                List<String> list = StringSplitUtil.splitToStr(req.ids, "\\|");
                for (String str : list)
                {
                    List<Integer> temp = StringUtil.splitIntToList(str, "\\,");
                    if (temp.size() >= 2)
                    {
                        ServerData serverData = DataComponent.getServerData(temp.get(1));

                        String url = String.format("http://%s:%s/gm/delPlayer?params={'userID':'%s'}",
                                serverData.getHost(), serverData.getWebPort(), temp.get(0));

                        String result = HttpUtil.doGet(url, 120000);

                        Resp resp = new Resp();
                        resp.userID = temp.get(0);

                        if (result.equalsIgnoreCase("ok"))
                            resp.isDelete = true;
                        else
                            resp.isDelete = false;

                        respList.add(resp);
                    }
                }
            }

            return gson.toJson(respList);
        }
        catch (Exception e)
        {
            logger.error("", e);
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, e.getMessage()));
        }
    }
}
