/**
 * 
 */
package com.account.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.info.DataInfo;
import com.util.StringUtil;

/**
 * 取得数据
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/getData", description = "取得数据")
public class DataGetServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    public static class RequestInfo
    {
        public long userID;
        public String groupID;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        RequestInfo requestInfo = gson.fromJson(jsonString, RequestInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "登录参数异常."));

        List<DataInfo> list = new ArrayList<>();

        if (requestInfo.userID > 0 && !StringUtil.isNullOrEmpty(requestInfo.groupID))
        {
            List<DataInfo> groupList = ServerComponent.getDataByGroup(requestInfo.userID, requestInfo.groupID);
            list.addAll(groupList);
            return gson.toJson(list);
        }

        if (requestInfo.userID > 0 && StringUtil.isNullOrEmpty(requestInfo.groupID))
        {
            List<DataInfo> groupList = ServerComponent.getDataByUser(requestInfo.userID);
            list.addAll(groupList);
            return gson.toJson(list);
        }

        return gson.toJson(list);
    }
}
