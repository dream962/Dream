package com.base.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.util.ServletUtil;
import com.util.print.LogFactory;

/**
 * 玩家请求的servlet基类，和GM的分开，GM的会做IP限制
 * 
 * @author dream
 *
 */
public abstract class PlayerHandlerServlet extends HttpServlet
{
    private static final long serialVersionUID = -7337936262053988938L;

    protected String keyword = null;

    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();

    /** 请求客户端的IP */
    protected String clientIP = null;

    public PlayerHandlerServlet()
    {
        super();
        keyword = this.getClass().getAnnotation(WebHandleAnnotation.class).cmdName();
        keyword = keyword.replace("/", "");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        String result = null;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

        clientIP = ServletUtil.getRequestIP(request);

        String json = request.getParameter("params");
        try
        {
            result = execute(json, request, response);
        }
        catch (Exception e)
        {
            String msg = String.format(" %s--请求异常:action(%s) params(%s)", clientIP, keyword, json);
            LogFactory.error(msg, e);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    protected abstract String execute(String json, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取请求客户端的IP
     * 
     * @return
     */
    protected String getRequstIP()
    {
        return clientIP;
    }
}
