package com.upload.web.servlet.login;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.business.StaffBusiness;
import com.upload.data.data.StaffBean;
import com.upload.util.MD5Util;
import com.upload.util.StringUtil;
import com.upload.util.TimeUtil;
import com.upload.util.TokenUtil;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-24 10:46:59
 * @description
 *              一键发布后台登录
 */
@WebHandleAnnotation(cmdName = "/login", description = "一键发布后台登录")
public class LoginServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 2363870483776561675L;

    protected static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);

    static class Req
    {
        String accountName;
        String password;
    }

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        Req req = gson.fromJson(json, Req.class);
        ResponseInfo res;
        if (req == null)
        {
            res = new ResponseInfo(ResponseCode.ERROR, "帐户错误");
            return gson.toJson(res);
        }

        if (StringUtil.isNullOrEmpty(req.accountName) || StringUtil.isNullOrEmpty(req.password))
        {
            res = new ResponseInfo(ResponseCode.ERROR, "帐户为空");
            return gson.toJson(res);
        }

        StaffBean bean = StaffBusiness.getStaffBean(req.accountName);

        if (bean == null)
        {
            res = new ResponseInfo(ResponseCode.ACCOUNT_NOT_EXIST, "帐户不存在");
            return gson.toJson(res);
        }

        // 次日重置校验次数
        if (bean.getCheckTime() == null || !TimeUtil.isSameDay(new Date(), bean.getCheckTime()))
        {
            bean.setCheckCount(0);
            bean.setCheckTime(new Date());
            StaffBusiness.updateStaffBean(bean);
        }

        if (bean.getCheckCount() >= 5)
        {
            res = new ResponseInfo(ResponseCode.ACCOUNT_NOT_EXIST, "密码错误次数达到今日最大尝试次数!");
            return gson.toJson(res);
        }

        String code = MD5Util.md5(req.password);
        if (!bean.getPassword().equalsIgnoreCase(code))
        {
            bean.setCheckCount(bean.getCheckCount() + 1);
            bean.setCheckTime(new Date());
            StaffBusiness.updateStaffBean(bean);
            res = new ResponseInfo(ResponseCode.ACCOUNT_NOT_EXIST, "密码错误!还有" + (5 - bean.getCheckCount()) + "次机会.");
            return gson.toJson(res);
        }

        bean = StaffBusiness.getStaffBean(req.accountName, req.password);
        if (bean == null)
        {
            res = new ResponseInfo(ResponseCode.ACCOUNT_NOT_EXIST, "帐户不存在");
            return gson.toJson(res);
        }
        if (bean.getStatus() == ResponseCode.ACCOUNT_STATUS_ERROR)
        {
            res = new ResponseInfo(ResponseCode.ACCOUNT_STATUS_ERROR, "帐户状态错误");
            return gson.toJson(res);
        }

        if (bean.getCheckCount() > 0)
        {
            bean.setCheckCount(bean.getCheckCount() / 2);
            StaffBusiness.updateStaffBean(bean);
        }

        String token = TokenUtil.encrypt(bean.getUserID(), System.currentTimeMillis());

        // 将用户id存入session
        HttpSession session = request.getSession();
        session.setAttribute("user", bean);
        // 生成的token下发给客户端，用于保持会话
        res = new ResponseInfo(ResponseCode.SUCCESS, token);
        return gson.toJson(res);
    }

}
