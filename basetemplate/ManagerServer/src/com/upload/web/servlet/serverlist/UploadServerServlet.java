package com.upload.web.servlet.serverlist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.upload.component.ServerListComponent;
import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.ServerPath;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.ServerLogUtil;
import com.upload.util.ShellUtils;
import com.upload.util.UploadUtils;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 服务器版本发布更新，资源、jar包等
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/uploadServer", description = "")
public class UploadServerServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2831007871413830916L;

    static class Req
    {
        /** 服务器列表 */
        String keyIDs;

        /** 1:游戏服；2：网关 */
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
                int type = req.type;

                if (ids.length <= 0)
                {
                    return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "error"));
                }

                // 先将服务器关闭
                for (String id : ids)
                {
                    try
                    {
                        int serverID = Integer.parseInt(id);
                        ServerListData data = ServerListComponent.getServerByServerID(serverID);

                        String addressPath = data.getServerAddress();
                        if (type == 2)
                            addressPath = data.getGateAddress();

                        String shell = ShellUtils.getShell(data.getType(), type);
                        if (shell == null || shell.isEmpty())
                        {
                            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器类型错误..."));
                        }
                        JSchUtil jsSchUtils = new JSchUtil();
                        jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                                data.getSSHKeyPassword(),data.getSSHPassword());
                        ServerLogUtil.stoppingLog(data.getServerID(), data.getServerName());
                        String command = "cd " + addressPath + ";sh " + shell + " stop";

                        jsSchUtils.execSh(command);
                        jsSchUtils.close();
                        ServerLogUtil.stoppedLog(data.getServerID(), data.getServerName());

                        if (type == 1)
                            data.setStatus(1);
                        if (type == 2)
                            data.setGateStatus(1);
                        
                    }
                    catch (Exception e)
                    {
                        logger.error("更新服务器出错....", e);
                    }
                }

                // 线程等待，确保服务器完全关闭
                Thread.sleep(1000);

                // 更新服务器资源
                List<FileItem> list = UploadUtils.getRequestFiles(request);
                for (String id : ids)
                {
                    int serverID = Integer.parseInt(id);
                    ServerListData data = ServerListComponent.getServerByServerID(serverID);

                    String addressPath = data.getServerAddress();
                    if (type == 2)
                        addressPath = data.getGateAddress();

                    String dest = addressPath + ServerPath.Jar_Path;
                    JSchUtil jsSchUtils = new JSchUtil();
                    jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                            data.getSSHKeyPassword(),data.getSSHPassword());
                    ServerLogUtil.updatingLog(data.getServerID(), data.getServerName());
                    String fileName = "";
                    for (FileItem file : list)
                    {
                        if (file.getName() == null)
                        {
                            continue;
                        }
                        jsSchUtils.upload(dest, file);
                        fileName = file.getName();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if ("zip".equals(suffix))
                        {
                            String shell = "cd " + addressPath + ";unzip -o " + fileName;
                            jsSchUtils.execSh(shell);
                        }
                        jsSchUtils.execSh("cd " + addressPath + ";rm -f " + fileName);
                    }
                    jsSchUtils.close();
                    ServerLogUtil.updatedLog(data.getServerID(), data.getServerName());

                    ServerListComponent.addUpdateServerList(data);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("更新服务器出错....", e);
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器错误"));
    }

}
