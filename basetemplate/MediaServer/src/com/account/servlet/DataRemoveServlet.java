/**
 * 
 */
package com.account.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;

/**
 * 删除数据
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/removeData", description = "删除数据")
public class DataRemoveServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 7873228658039843205L;

    public static class Req
    {
        public List<Long> keyList;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        Req requestInfo = gson.fromJson(jsonString, Req.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "参数异常."));

        boolean result = false;

        if (requestInfo.keyList != null)
        {
            result = ServerComponent.removeDataByKeys(requestInfo.keyList);
        }

        if (result)
            return gson.toJson(new RetInfo(ErrorCodeType.Success, "删除成功."));
        else
            return gson.toJson(new RetInfo(ErrorCodeType.Error, "删除失败."));
    }
}
