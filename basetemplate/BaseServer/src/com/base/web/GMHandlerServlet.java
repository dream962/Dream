package com.base.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.util.ServletUtil;

/**
 * 处理参数（params）为json格式字符串的Servlet基类。
 * 
 */
public abstract class GMHandlerServlet extends HttpServlet
{
    private static final long serialVersionUID = 8719792486954264323L;

    protected static final Logger logger = LoggerFactory.getLogger(GMHandlerServlet.class);

    protected String keyword;

    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    /** 请求客户端的IP */
    protected String clientIP = null;

    /** 非法的客户端IP */
    protected static final byte INVALID_IP = 5;

    public GMHandlerServlet()
    {
        super();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
    }

    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        return "";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        this.request = request;
        this.response = response;

        keyword = this.getClass().getAnnotation(WebHandleAnnotation.class).cmdName();
        keyword = keyword.replace("/", "");

        String result = null;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

        // IP 检查
        clientIP = ServletUtil.getRequestIP(request);
        String host = ServletUtil.getRequestHost(request);

        if (checkRequestIP(clientIP) == false && checkRequestIP(host) == false)
        {
            result = gson.toJson(INVALID_IP);
        }
        else
        {
            String json = request.getParameter("params");
            try
            {
                result = execute(json, request, response);
            }
            catch (Exception e)
            {
                String msg = String.format("%s--请求异常:action(%s) params(%s)", clientIP, keyword, json);
                logger.error(msg, e);
            }
        }

        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 检查客户端IP或者域名是否合法。<br>
     * 子类可通过Override来改变检查范围。
     * 
     * @param ip
     *            发出请求的客户端IP
     * @return
     */
    protected boolean checkRequestIP(String ip)
    {
        return true;
    }

    /**
     * 获取请求客户端的IP
     * 
     * @return
     */
    protected String getRequstIP()
    {
        return clientIP;
    }

    /**
     * 转换utf-8编码
     * 
     * @param value
     * @return
     */
    public String getUTF8(String value)
    {
        if (value != null)
        {
            try
            {
                if (!checkGBK(value))
                {
                    return new String(value.getBytes("ISO-8859-1"), "UTF-8");
                }
                else
                {
                    return value;
                }
            }
            catch (UnsupportedEncodingException e)
            {
                logger.error("获取字符转换成utf-8出错", e);
            }
        }
        return "";
    }

    /**
     * 检查能否用GBK解码
     * 
     * @param value
     * @return
     */
    public boolean checkGBK(String value)
    {
        return java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(value);
    }
}
