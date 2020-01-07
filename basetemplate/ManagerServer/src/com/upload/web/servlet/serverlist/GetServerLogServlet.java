package com.upload.web.servlet.serverlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.ServerPath;
import com.upload.data.ServerType;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.LogFactory;
import com.upload.util.StringUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-06-05 10:17:14
 * @description
 *              查看服务器日志
 */
@WebHandleAnnotation(cmdName = "/getServerLog", description = "查看服务器日志")
public class GetServerLogServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -1457174989605261940L;

    static class Req
    {
        String keyIDs;
    }

    static class Resp
    {
        String serverIp;
        String serverName;
        int serverID;
        List<String> log = new ArrayList<>();
        List<String> logGate = new ArrayList<>();
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<Resp> list = new ArrayList<>();
            Req req = gson.fromJson(json, Req.class);
            if (req != null && !StringUtil.isNullOrEmpty(req.keyIDs))
            {
                String[] ids = req.keyIDs.split(",");
                for (String id : ids)
                {
                    Resp resp = new Resp();
                    int serverID = Integer.parseInt(id);
                    ServerListData data = ServerListComponent.getServerByServerID(serverID);

                    resp.serverIp = data.getServerIp();
                    resp.serverName = data.getServerName();
                    resp.serverID = data.getServerID();

                    JSchUtil jsSchUtils = new JSchUtil();
                    try
                    {
                        // 查询log
                        jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                                data.getSSHKeyPassword(), data.getSSHPassword());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String str = format.format(new Date());
                        String command = "tail -n 100 " + data.getServerAddress() + ServerPath.Log_Path + "error." + str + ".log";
                        String result = jsSchUtils.execCmd(command);
                        String[] split = result.split("\n");
                        for (String s : split)
                        {
                            resp.log.add(s);
                        }

                        // 如果是游戏服,同时显示网关服
                        if (data.getType() == ServerType.GAME_SERVER)
                        {
                            String str1 = format.format(new Date());
                            String command1 = "tail -n 100 " + data.getGateAddress() + ServerPath.Log_Path + "error." + str1 + ".log";
                            String result1 = jsSchUtils.execCmd(command1);
                            String[] split1 = result1.split("\n");
                            for (String s : split1)
                            {
                                resp.logGate.add(s);
                            }
                        }

                        list.add(resp);
                    }
                    catch (Exception e)
                    {
                        LogFactory.error("", e);
                    }
                    finally
                    {
                        jsSchUtils.close();
                    }
                }
                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, list));
            }
        }
        catch (Exception e)
        {
            logger.error("查看服务器日志异常...", e);
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
    }

}
