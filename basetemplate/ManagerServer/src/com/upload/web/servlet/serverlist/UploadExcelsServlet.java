package com.upload.web.servlet.serverlist;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.ServerPath;
import com.upload.data.business.ServerListBusiness;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.UploadUtils;
import com.upload.web.servlet.FileHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-27 14:35:42
 * @description 上传配置表到一个或多个服务器
 */
@WebHandleAnnotation(cmdName = "/uploadExcels", description = "上传配置表到一个或多个服务器")
public class UploadExcelsServlet extends FileHandlerServlet
{
    private static final long serialVersionUID = -7519554899416697120L;

    static class Req
    {
        String keyIDs;
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
                // 获取文件列表
                List<FileItem> list = UploadUtils.getRequestFiles(request);
                for (String id : ids)
                {
                    int keyID = Integer.parseInt(id);
                    ServerListData data = ServerListBusiness.getServerByKeyID(keyID);

                    String dest = data.getServerAddress() + ServerPath.Excel_Path;
                    JSchUtil jsSchUtils = new JSchUtil();
                    jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),data.getSSHKeyPassword(),data.getSSHPassword());
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
                            String shell = "cd " + data.getServerAddress() + ServerPath.Excel_Path + ";" + "unzip -o "
                                    + fileName;
                            jsSchUtils.execSh(shell);
                        }
                        // JSchUtils.execSh("cd " + data.getServerAddress() +
                        // ServerPath.Excel_Path + ";" + "rm -f " + fileName);
                    }
                    jsSchUtils.close();
                    data.setLastUpdateTime(new Date());
                    ServerListBusiness.addOrUpdate(data);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("上传配置表出错....", e);
        }

        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器错误"));
    }

}
