/**
 * 
 */
package com.account.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.account.entity.RetInfo;
import com.base.code.ErrorCodeType;
import com.base.component.SimpleScheduleComponent;
import com.base.web.PlayerHandlerServlet;
import com.base.web.WebHandleAnnotation;
import com.data.account.business.SeqType;
import com.data.account.cache.AccountCacheComponent;
import com.data.account.data.UserData;
import com.data.account.factory.UserDataFactory;
import com.util.MD5Util;

/**
 * 创建注册账号
 * 
 * @author dream
 *
 */
@WebHandleAnnotation(cmdName = "/register", description = "注册账户")
public class RegisterServlet extends PlayerHandlerServlet
{
    private static final long serialVersionUID = 4744757610406809052L;

    public static class RequestInfo
    {
        public String phone;
        public String code;
        public String password1;
        public String password2;
    }

    static class Res
    {
        public boolean isSuccess;
        public long userID;
        public String phone;
    }

    @Override
    public String execute(String jsonString, HttpServletRequest request, HttpServletResponse response)
    {
        RequestInfo requestInfo = gson.fromJson(jsonString, RequestInfo.class);
        if (requestInfo == null)
            return gson.toJson(new RetInfo(ErrorCodeType.Http_Parameter_Null, "登录参数异常."));

        UserData userInfo = AccountCacheComponent.getCacheUser().getUserInfoByName(requestInfo.phone);
        if (userInfo != null)
            return gson.toJson(new RetInfo(ErrorCodeType.User_Name_Exist, "玩家已经注册."));

        String password = MD5Util.md5(requestInfo.password1);

        userInfo = new UserData();
        long userID = AccountCacheComponent.getSystem().getTableMaxID(SeqType.T_U_USER.getValue());
        userInfo.setUserID(userID);
        userInfo.setLastLoginDate(new Date());
        userInfo.setRegisterDate(new Date());
        userInfo.setUserName(requestInfo.phone);
        userInfo.setOpenID(requestInfo.phone);
        userInfo.setGold(100);
        userInfo.setLevel(10);
        userInfo.setMoney(100);
        userInfo.setPassword(password);

        AccountCacheComponent.getCacheUser().updateUserData(userInfo);
        UserData userInfo1 = userInfo;
        SimpleScheduleComponent.schedule((job) -> {
            UserDataFactory.getDao().addOrUpdate(userInfo1);
        });

        Res res = new Res();
        res.userID = userInfo.getUserID();
        res.phone = requestInfo.phone;
        res.isSuccess = true;

        System.err.println("注册 -- account:" + requestInfo.phone + ",userID:" + userInfo.getUserID());

        return gson.toJson(res);
    }
}
