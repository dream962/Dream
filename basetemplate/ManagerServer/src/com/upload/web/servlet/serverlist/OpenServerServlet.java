package com.upload.web.servlet.serverlist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.business.ServerListBusiness;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.ServerLogUtil;
import com.upload.util.ShellUtils;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 一键开启一个或多个服务器
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/openServer", description = "")
public class OpenServerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -6838703582349947045L;

    static class Req
    {
        String keyIDs;
        /** 0:全部；1：游戏；2：网关 */
        int type;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            Req req = gson.fromJson(json, Req.class);
            if (req != null)
            {
                String[] ids = req.keyIDs.split(",");
                if (ids.length <= 0)
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
                }

                List<Integer> types = new ArrayList<Integer>();
                if (req.type == 0)
                {
                    types.add(1);
                    types.add(2);
                }
                else
                    types.add(req.type);

                // 先将服务器关闭
                for (String id : ids)
                {
                    for (int type : types)
                    {
                        int serverID = Integer.parseInt(id);
                        ServerListData data = ServerListComponent.getServerByServerID(serverID);

                        String addressPath = data.getServerAddress();
                        if (type == 2)
                            addressPath = data.getGateAddress();

                        String shell = ShellUtils.getShell(data.getType(), type);
                        if (shell == "")
                        {
                            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器类型错误..."));
                        }
                        JSchUtil jsSchUtils = new JSchUtil();
                        jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                                data.getSSHKeyPassword(), data.getSSHPassword());
                        ServerLogUtil.stoppingLog(data.getServerID(), data.getServerName());
                        String command = "cd " + addressPath + ";sh " + shell + " stop";

                        jsSchUtils.execSh(command);
                        jsSchUtils.close();
                        ServerLogUtil.stoppedLog(data.getServerID(), data.getServerName());

                        if (type == 1)
                            data.setStatus(1);
                        if (type == 2)
                            data.setGateStatus(1);
                        ServerListComponent.addUpdateServerList(data);
                    }
                }

                // 线程等待，确保服务器完全关闭
                Thread.sleep(8000);

                // 再开启服务器
                for (String id : ids)
                {
                    for (int type : types)
                    {
                        int keyID = Integer.parseInt(id);
                        ServerListData data = ServerListBusiness.getServerByKeyID(keyID);

                        String addressPath = data.getServerAddress();
                        if (type == 2)
                            addressPath = data.getGateAddress();

                        String shell = ShellUtils.getShell(data.getType(), type);
                        if (shell == "")
                        {
                            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器类型错误..."));
                        }
                        JSchUtil jsSchUtils = new JSchUtil();
                        jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                                data.getSSHKeyPassword(), data.getSSHPassword());
                        ServerLogUtil.startingLog(data.getServerID(), data.getServerName());
                        String command = "cd " + addressPath + ";sh " + shell + " start";

                        jsSchUtils.execSh(command);
                        jsSchUtils.close();
                        ServerLogUtil.startedLog(data.getServerID(), data.getServerName());

                        if (type == 1)
                            data.setStatus(0);
                        if (type == 2)
                            data.setGateStatus(0);

                        ServerListComponent.addUpdateServerList(data);
                    }
                }

                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, "服务器已开启"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器错误..."));

    }

}
