/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.info.DataInfo;

/**
 * 添加更新数据
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/addData", description = "添加更新数据")
public class DataAddServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 7873228658039843105L;

    public static class Resp
    {
        public long keyID;
        public int code;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        DataInfo requestInfo = gson.fromJson(jsonString, DataInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "参数异常."));

        boolean result = ServerComponent.addData(requestInfo);
        Resp resp = new Resp();
        resp.code = result ? 1 : 0;
        resp.keyID = requestInfo.getKeyID();
        return gson.toJson(resp);
    }
}
