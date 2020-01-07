/**
 * 
 */
package com.account.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.cache.AccountCacheComponent;
import com.data.account.data.UserData;
import com.util.MD5Util;

/**
 * 登录账号
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/login", description = "登录账户")
public class LoginServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    public static class RequestInfo
    {
        public String userName;
        public String password;
    }

    static class Res
    {
        public long userID;
        public String userName;
        public int gold;
        public int level;
        public int money;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        RequestInfo requestInfo = gson.fromJson(jsonString, RequestInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "登录参数异常."));

        UserData userInfo = AccountCacheComponent.getCacheUser().getUserInfoByName(requestInfo.userName);
        if (userInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.User_Not_Exist, "玩家不存在."));

        String password = MD5Util.md5(requestInfo.password);
        if (!userInfo.getPassword().equalsIgnoreCase(password))
            return gson.toJson(new RetInfo(ErrorCodeType.Password_Error, "密码错误."));

        Res res = new Res();
        res.userID = userInfo.getUserID();
        res.gold = userInfo.getGold();
        res.level = userInfo.getLevel();
        res.money = userInfo.getMoney();
        res.userName = userInfo.getUserName();

        System.err.println("account:" + requestInfo.userName + ",userID:" + userInfo.getUserID());

        return gson.toJson(res);
    }
}
