
package com.game.web;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * HttpClient Post请求
     *
     * @param reqUrl
     * @param parameters
     * @return
     */
    // public static String doPost(String reqUrl, Map<String, String> parameters)
    // {
    // // 获得Http客户端
    // CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    //
    // // 参数
    // StringBuffer params = new StringBuffer();
    // try
    // {
    // if (parameters != null)
    // {
    // for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
    // {
    // Entry<?, ?> element = (Entry<?, ?>) iter.next();
    // params.append(element.getKey().toString());
    // params.append("=");
    // params.append(URLEncoder.encode(element.getValue().toString(), "UTF-8"));
    // params.append("&");
    // }
    // }
    //
    // if (params.length() > 0)
    // {
    // params = params.deleteCharAt(params.length() - 1);
    // }
    // }
    // catch (UnsupportedEncodingException e1)
    // {
    // LogFactory.error("", e1);
    // }
    //
    // // 创建Post请求
    // String url = "";
    // HttpPost httpPost = new HttpPost(reqUrl + "?" + params);
    // httpPost.setHeader("User-Agent", "PostmanRuntime");
    // httpPost.setHeader("Accept", "*/*");
    // httpPost.setHeader("Host", "accounts.google.com");
    // httpPost.setHeader("Accept-Encoding", "gzip,deflate");
    //
    // // 响应模型
    // CloseableHttpResponse response = null;
    // try
    // {
    // // 由客户端执行(发送)Post请求
    // response = httpClient.execute(httpPost);
    // // 从响应模型中获取响应实体
    // HttpEntity responseEntity = response.getEntity();
    //
    // System.out.println("响应状态为:" + response.getStatusLine());
    // if (responseEntity != null)
    // {
    // String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
    // return responseStr;
    // }
    // }
    // catch (Exception e)
    // {
    // LogFactory.error("", e);
    // }
    // finally
    // {
    // try
    // {
    // // 释放资源
    // if (httpClient != null)
    // {
    // httpClient.close();
    // }
    // if (response != null)
    // {
    // response.close();
    // }
    // }
    // catch (IOException e)
    // {
    // LogFactory.error("", e);
    // }
    // }
    //
    // return "";
    // }

    public static void main(String[] args)
    {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "test112");
        parameters.put("openID", "test112");
        parameters.put("machinecode", "test112");

        // String result = doPost("http://192.168.1.47:9002/loginAccount", parameters);
    }
}
