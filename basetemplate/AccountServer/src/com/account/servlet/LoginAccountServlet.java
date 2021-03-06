/**
 *
 */
package com.account.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.component.ServerComponent;
import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.component.SimpleScheduleComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.business.SeqType;
import com.data.account.cache.AccountCacheComponent;
import com.data.account.data.ServerData;
import com.data.account.data.UserData;
import com.data.account.factory.UserDataFactory;
import com.util.StringUtil;
import com.util.TokenUtil;

/**
 * 创建注册账号
 *
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/loginAccount", description = "登录账户")
public class LoginAccountServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    public static class RequestInfo
    {
        public String name;// openID->gName;machineCode->UserName
        public String openID;
        public String machinecode;
        public int version; // 当前版本
    }

    static class Res
    {
        public String token;
        public String ip;
        public int gamePort;
        public int webPort;
        public String name;
        public String error;
        public boolean isForce; // 是否强制更新
        public int maxVersion;  // 当前最高版本
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        RequestInfo requestInfo = gson.fromJson(jsonString, RequestInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, keyword));

        UserData userInfo = null;

        // 测试登录,免密
        if (requestInfo.name.indexOf("@jump") >= 0)
        {
            requestInfo.name = requestInfo.name.replace("@jump", "");
            userInfo = AccountCacheComponent.getCacheUser().getUserInfoByNameTest(requestInfo.name);
        }
        else
        {
            // 如果openID为空
            if (StringUtil.isNullOrEmpty(requestInfo.openID))
            {
                userInfo = AccountCacheComponent.getCacheUser().getUserInfoByMachineCode(requestInfo.name, requestInfo.machinecode);
            }
            else
            {
                userInfo = AccountCacheComponent.getCacheUser().getUserInfoByName(requestInfo.name, requestInfo.openID);
            }
        }

        // 自定义名字注册的判断名字是否重复
        if (userInfo == null)
        {
            if (StringUtil.isNullOrEmpty(requestInfo.openID))
            {
                boolean isUserNameUsed = AccountCacheComponent.getCacheUser().checkUserName(requestInfo.name);
                if (isUserNameUsed)
                {
                    Res res = new Res();
                    res.name = requestInfo.name;
                    res.error = "error";
                    String msg = gson.toJson(res);
                    return gson.toJson(new RetInfo(ErrorCodeType.Success, "", msg));
                }
            }

            userInfo = new UserData();
            long userID = AccountCacheComponent.getSystem().getTableMaxID(SeqType.T_U_USER.getValue());
            userInfo.setUserID(userID);
            userInfo.setLastLoginDate(new Date());
            userInfo.setRegisterDate(new Date());
            // openID->gName;machineCode->UserName
            if (!StringUtil.isNullOrEmpty(requestInfo.openID))
                userInfo.setGName(requestInfo.name);
            else
            {
                if (!StringUtil.isNullOrEmpty(requestInfo.machinecode))
                    userInfo.setUserName(requestInfo.name);
            }

            userInfo.setOpenID(requestInfo.openID);
            userInfo.setMachineCode(requestInfo.machinecode);

            AccountCacheComponent.getCacheUser().addUserData(userInfo);
            UserData userInfo1 = userInfo;
            SimpleScheduleComponent.schedule((job) -> {
                UserDataFactory.getDao().addOrUpdate(userInfo1);
            });
        }

        Res res = new Res();
        res.token = TokenUtil.encrypt(userInfo.getUserID(), System.currentTimeMillis());

        boolean isTest = false;
        if (requestInfo.name.indexOf("@d") >= 0)
        {
            isTest = true;
        }

        ServerData serverData = ServerComponent.choiceServer(isTest);
        res.gamePort = serverData.getGamePort();
        res.ip = serverData.getHost();
        res.webPort = serverData.getWebPort();
        res.name = requestInfo.name;
        res.error = "success";

        res.isForce = ServerComponent.getVersionForce(requestInfo.version);
        res.maxVersion = ServerComponent.getMaxVersion();

        // 强制更新,下发空
        if (res.isForce && res.maxVersion > 0)
        {
            res.gamePort = 0;
            res.ip = "";
        }

        System.err.println("account:" + res.name + "," + requestInfo.openID + ",userID:" + userInfo.getUserID() + ",machineCode:"
                + userInfo.getMachineCode() + ",force:" + res.isForce + ",ip:" + res.ip);

        String msg = gson.toJson(res);
        return gson.toJson(new RetInfo(ErrorCodeType.Success, "", msg));
    }
}
