/**
 * 
 */
package com.game.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

import com.google.gson.Gson;
import com.util.TokenUtil;
import com.util.TokenUtil.TokenD;

public class GMAuthFilter implements Filter
{
    public static String USERNAME = "root";
    public static String PASSWORD = "123456";
    public static int STATID = 1503526193;

    @Override
    public void destroy()
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        Request req = (Request) request;
        String params = req.getParameter("params");
        Gson gson = new Gson();

        Map map = (Map) gson.fromJson(params, Object.class);

        String token = (String) map.get("token");

        try
        {
            TokenD td = TokenUtil.decrypt(token);

            if (td != null && td.id == STATID)
            {
                chain.doFilter(request, response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Response res = (Response) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
    }

    @Override
    public void init(FilterConfig config) throws ServletException
    {

    }

}
