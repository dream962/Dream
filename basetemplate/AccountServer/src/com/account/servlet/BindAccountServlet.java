/**
 *
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.component.SimpleScheduleComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.cache.AccountCacheComponent;
import com.data.account.data.UserData;
import com.data.account.factory.UserDataFactory;
import com.util.print.LogFactory;

/**
 * 绑定账号
 *
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/bindAccount", description = "绑定账户")
public class BindAccountServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 6159342458932645296L;

    public static class RequestInfo
    {
        public String name;
        public String machineCode;
        public String gName;
        public String openID;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        RequestInfo requestInfo = gson.fromJson(jsonString, RequestInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "参数不对"));

        UserData userInfo = AccountCacheComponent.getCacheUser().getUserInfoByMachineCode(requestInfo.name, requestInfo.machineCode);

        if (userInfo != null)
        {
            userInfo.setOpenID(requestInfo.openID);
            userInfo.setGName(requestInfo.gName);

            AccountCacheComponent.getCacheUser().updateUserData(userInfo);
            AccountCacheComponent.getCacheUser().updateUserGName(requestInfo.gName, requestInfo.openID, userInfo.getUserID());

            SimpleScheduleComponent.schedule((job) -> {
                UserDataFactory.getDao().addOrUpdate(userInfo);
            });

            return gson.toJson(new RetInfo(ErrorCodeType.Success, "", "ok"));
        }
        else
        {
            LogFactory.error("绑定账号失败.{},{}", requestInfo.name, requestInfo.machineCode);
        }

        return gson.toJson(new RetInfo(ErrorCodeType.Success, "", "fail"));
    }
}
