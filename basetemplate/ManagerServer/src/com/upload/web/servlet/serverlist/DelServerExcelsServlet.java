package com.upload.web.servlet.serverlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.business.ServerListBusiness;
import com.upload.data.data.ServerListData;
import com.upload.util.JSchUtil;
import com.upload.util.LogFactory;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-06-16 03:01:55
 * @description
 *              临时：删除配置表
 */
@WebHandleAnnotation(cmdName = "/delServerExcels", description = "临时：删除配置表")
public class DelServerExcelsServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = -5227812489463847416L;

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

                for (String id : ids)
                {
                    int keyID = Integer.parseInt(id);
                    ServerListData data = ServerListBusiness.getServerByKeyID(keyID);
                    String shell = "cd " + data.getServerAddress() + ";rm -rf excel";

                    JSchUtil jsSchUtils = new JSchUtil();
                    jsSchUtils.connect(data.getSSHUsername(), data.getSSHKeyPath(), data.getServerIp(), data.getSSHPort(),
                            data.getSSHKeyPassword(), data.getSSHPassword());
                    String result = jsSchUtils.execCmd(shell);
                    LogFactory.error(result);
                    jsSchUtils.close();
                }
                return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, "删除成功"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "服务器错误..."));
    }
}
