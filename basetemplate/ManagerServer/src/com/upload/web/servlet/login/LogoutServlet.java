package com.upload.web.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @date 2018-04-24 20:23:35
 * @description
 *              一键发布后台-退出帐户
 */
@WebHandleAnnotation(cmdName = "/logout", description = "一键发布后台-退出帐户")
public class LogoutServlet extends BaseHandlerServlet
{
    private static final long serialVersionUID = 4863016554325389875L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, ""));
    }

}
