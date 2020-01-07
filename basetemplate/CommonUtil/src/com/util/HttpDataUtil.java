/**
 * Date: 2014-2-18
 *
 * Copyright (C) 2013-2015 7Road. All rights reserved.
 */

package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * HTTP请求
 * 
 * @author saly.bao
 */
public class HttpDataUtil
{

    public static String getHttpURL(String path) throws MalformedURLException, ProtocolException, IOException
    {
        // String path =
        // "http://127.0.0.1:8088/Rank/PlayerLevel?CorpID=123&LoginName=qqq&name="+URLEncoder.encode("汉字","utf-8");
        URL url = new URL(path);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true，默认情况下是false
        urlConn.setDoOutput(true);
        // 设置是否从httpUrlConnection读入，默认情况下是true
        urlConn.setDoInput(true);
        // Post请求不能使用缓存
        urlConn.setUseCaches(false);
        // 设定传送的内容类型是可序列化的java对象
        // （如果不设此项，在传送序列化对象时，当WEB服务默认的不是这种类型时可能抛java.io.EOFException）
        // urlConn.setRequestProperty("Content-type", "application/x-java-serialize-object");
        urlConn.setRequestProperty("Content-type", "text/plain;charset=UTF-8");
        // 设定请求的方法为"POST"，默认是GET
        urlConn.setRequestMethod("POST");
        // 连接，上面对urlConn的所有配置必须要在connect之前完成
        urlConn.setConnectTimeout(10000);// 连接主机的超时时间（单位：毫秒）
        urlConn.setReadTimeout(10000);// 从主机读取数据的超时时间（单位：毫秒）
        urlConn.connect();
        // 调用HttpURLConnection连接对象的getInputStream()函数，
        // 将内存缓冲区中封装好的完整的HTTP请求电文发送服务端
        InputStream inStrm = urlConn.getInputStream();// 注意，实际发送请求的代码段就在这里
        InputStreamReader isReader = new InputStreamReader(inStrm, "utf-8");
        BufferedReader bReader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str = bReader.readLine();
        while (str != null)
        {
            sb.append(str);
            str = bReader.readLine();
        }
        return sb.toString();
    }

    public static void main(String[] args)
    {

    }
}
