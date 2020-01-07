package com.upload.web.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.upload.data.ResponseCode;
import com.upload.data.ResponseInfo;
import com.upload.data.data.StaffBean;
import com.upload.web.servlet.BaseHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-05-07 10:16:06
 * @description
 *              ajax获取昵称，用于登录后展示
 */
@WebHandleAnnotation(cmdName = "/getNickName", description = "用于获取用户的昵称")
public class GetNickNameServlet extends BaseHandlerServlet
{

    private static final long serialVersionUID = -6919429097271838817L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        StaffBean bean = (StaffBean) session.getAttribute("user");
        if (bean == null)
        {
            return gson.toJson(new ResponseInfo(ResponseCode.ERROR, "用户错误"));
        }
        return gson.toJson(new ResponseInfo(ResponseCode.SUCCESS, bean.getAccountName()));
    }

}
