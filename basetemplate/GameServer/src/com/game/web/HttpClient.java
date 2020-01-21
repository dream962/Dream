/**
 *
 */
package com.game.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.base.component.GlobalConfigComponent;
import com.util.print.LogFactory;

/**
 * Http请求服务器
 *
 * @author dream
 *
 */
public class HttpClient
{
    public static int TIME_OUT = 30;

    /**
     * 请求账号服
     *
     * @param action
     * @param params
     * @return
     */
    public static String getFromAccount(String action, String params)
    {
        String url = "";
        try
        {
            String ip = GlobalConfigComponent.getConfig().server.accountServerIp;
            int port = GlobalConfigComponent.getConfig().server.accountServerPort;

            url = String.format("http://%s:%d/%s?params=%s", ip, port, action, params);
            Document document = Jsoup.connect(url).timeout(TIME_OUT * 1000).get();
            if (document == null)
            {
                LogFactory.error("HttpClient.getFromAccount Exception.URL:{}", url);
                return "";
            }

            String result = document.text();
            if (result == null || result.isEmpty() || result == "")
            {
                LogFactory.error("HttpClient.getFromAccount Return Empty.URL:{}", url);
            }

            return result;
        }
        catch (Exception e)
        {
            LogFactory.error("HttpClient：" + url, e);
        }

        return null;
    }

}
