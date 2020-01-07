package com.upload.web.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Hoan.Zou
 * @date 2018-04-27 14:23:23
 * @description
 *              用于处理文件上传的Handler
 */
public abstract class FileHandlerServlet extends HttpServlet
{
    private static final long serialVersionUID = 2579210974155856996L;

    protected static final Logger logger = LoggerFactory.getLogger(FileHandlerServlet.class);
    
    protected static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();

    protected String keyword;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }
    
    public abstract String execute(String json, HttpServletRequest request, HttpServletResponse response);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        keyword = this.getClass().getAnnotation(WebHandleAnnotation.class).cmdName();
        keyword = keyword.replace("/", "");

        String result = null;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        
        String json = request.getParameter("params");
        try
        {
            String execute = execute(json, request, response);
            if ("exit".equals(execute))
            {
                return;
            }
        }
        catch (Exception e)
        {
            String msg = String.format("%s--请求异常 ", keyword);
            logger.error(msg, e);
        }
        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }
}
