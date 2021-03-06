package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.print.LogFactory;

public class HttpUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class.getName());

    /**
     * 连接超时
     */
    private static int connectTimeOut = 8000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 5000;

    /**
     * 请求编码
     */
    private static String requestEncoding = "UTF-8";

    /**
     * @return 连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static int getConnectTimeOut()
    {
        return HttpUtil.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static int getReadTimeOut()
    {
        return HttpUtil.readTimeOut;
    }

    /**
     * @return 请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static String getRequestEncoding()
    {
        return requestEncoding;
    }

    /**
     * @param connectTimeOut
     *            连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static void setConnectTimeOut(int connectTimeOut)
    {
        HttpUtil.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut
     *            读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static void setReadTimeOut(int readTimeOut)
    {
        HttpUtil.readTimeOut = readTimeOut;
    }

    /**
     * @param requestEncoding
     *            请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static void setRequestEncoding(String requestEncoding)
    {
        HttpUtil.requestEncoding = requestEncoding;
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map<?, ?> parameters,
            String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
            {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        HttpUtil.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout",
                    String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            // url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            // url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            // String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            // LOGGER.error("当前请求地址错误：", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map<?, ?> parameters,
            String recvEncoding, String requestEncodString)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
            {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        requestEncodString));
                params.append("&");
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout",
                    String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            // url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            // url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            // String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            // LOGGER.error("当前请求地址错误：", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0)
            {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1,
                        reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++)
                {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0)
                    {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1,
                                string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value,
                                HttpUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(50000);// （单位：毫秒）jdk
            url_con.setReadTimeout(50000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 下载文件信息到指定路径
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding, String path)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0)
            {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1,
                        reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++)
                {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0)
                    {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1,
                                string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value,
                                HttpUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout",
                    String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            byte[] result = new byte[1024];
            in.read(result);

            File myFilePath = new File(path);
            myFilePath.delete();
            myFilePath.createNewFile();

            FileOutputStream writer = new FileOutputStream(myFilePath);
            writer.write(result);
            writer.close();
            in.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map<String, String> parameters,
            String recvEncoding)
    {
        HttpURLConnection url_con = null;
        try
        {
            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpUtil.requestEncoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            // http正文内，因此需要设为true，默认情况下是false
            url_con.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true
            url_con.setDoInput(true);
            // Post请求不能使用缓存
            url_con.setUseCaches(false);
            url_con.setRequestMethod("POST");
            url_con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            url_con.setRequestProperty("Accept", "*/*");
            url_con.setConnectTimeout(10000);// 连接主机的超时时间（单位：毫秒）
            url_con.setReadTimeout(10000);// 从主机读取数据的超时时间（单位：毫秒）

            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            url_con.connect();

            InputStream inStrm = url_con.getInputStream();// 注意，实际发送请求的代码段就在这里
            InputStreamReader isReader = new InputStreamReader(inStrm, "utf-8");
            BufferedReader bReader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str = bReader.readLine();
            while (str != null)
            {
                sb.append(str);
                str = bReader.readLine();
            }
            inStrm.close();
            isReader.close();
            bReader.close();
            return sb.toString();
        }
        catch (Exception e)
        {
            LogFactory.error("HttpUtil:doPost", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }
        return "";
    }

    /**
     * 通过HTTP下载文档，保存
     * 
     * @param xmlUrl
     * @param path
     */
    public static void getUrlXmlFile(String xmlUrl, String path)
    {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024 * 10];
        int size = 0;
        try
        {
            url = new URL(xmlUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            // 连接指定的资源
            httpUrl.connect();
            // 获取网络输入流
            InputStream i = httpUrl.getInputStream();
            bis = new BufferedInputStream(i);
            // 建立文件
            fos = new FileOutputStream(path);

            // 保存文件
            while ((size = bis.read(buf)) != -1)
            {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            httpUrl.disconnect();

        }
        catch (MalformedURLException e)
        {
            LOGGER.error("HttpUtil:getUrlXmlFile", e);
        }
        catch (IOException e)
        {
            LOGGER.error("HttpUtil:getUrlXmlFile", e);
        }
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTPS请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGetByHttps(String reqUrl, Map<?, ?> parameters,
            String recvEncoding)
    {
        HttpsURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
            {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        HttpUtil.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpsURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout",
                    String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            // url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            // url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            // String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            // LOGGER.error("当前请求地址错误：", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTPS请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPostByHttps(String reqUrl, Map<String, String> parameters, String recvEncoding)
    {
        HttpsURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpUtil.requestEncoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpsURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(10000);// （单位：毫秒）jdk
            url_con.setReadTimeout(10000);// （单位：毫秒）
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null)
            {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            LOGGER.error("HttpUtil:doPostByHttps", e);
        }
        catch (Exception e)
        {
            LOGGER.error("HttpUtil:doPostByHttps", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    public static String doPosts(String reqUrl, Map<String, String> parameters,
            String recvEncoding)
    {
        HttpsURLConnection url_con = null;
        String responseContent = "0";
        try
        {
            //
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType)
                {
                }

            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            //
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {

                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            // 进行url编码

            //
            String params = sortParam(parameters);

            URL url = new URL(
                    reqUrl);
            url_con = (HttpsURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();

            while (tempLine != null)
            {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }

            responseContent = tempStr.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    // 对参数进行编码
    public static String sortParam(Map<String, String> parameters)
            throws UnsupportedEncodingException
    {
        StringBuilder listParam = new StringBuilder("");
        List<String> keys = new ArrayList<String>();

        for (String key : parameters.keySet())
        {
            keys.add(key);
        }

        Collections.sort(keys);

        for (String key : keys)
        {
            if (!key.equals("sig"))
            {
                // 出sig以外都需要进行url编码
                String value = URLEncoder.encode(parameters.get(key), "utf-8");
                listParam.append(key).append("=").append(value).append('&');
            }
            else
            {
                listParam.append(key).append("=").append(parameters.get(key)).append('&');
            }
        }
        return listParam.substring(0, listParam.length() - 1);
    }

    public static void main(String a[])
    {
        String reqUrl = "http://192.168.1.47:9002/loginAccount";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", "test1234");
        parameters.put("openID", "sasffdf");
        parameters.put("machinecode", "tessfdsfsdf");
        String ss = doPostForm(reqUrl, parameters, "UTF-8");
        System.err.println(ss);
    }

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet1(String reqUrl, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0)
            {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1,
                        reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++)
                {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0)
                    {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1,
                                string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value,
                                HttpUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");

            url_con.setRequestProperty("X-Forwarded-For", "101.23.141.125,103.122.4.113");
            url_con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
            url_con.setRequestProperty("cache-control", "no-store, no-cache, must-revalidate");
            // url_con.setRequestProperty("accept-encoding","gzip, deflate, sdch");
            url_con.setRequestProperty("accept-language", "zh-CN,zh;q=0.8");
            url_con.setRequestProperty("cookie", "PHPSESSID=20r0c3id7r5sudl3vmgta5mj75");
            url_con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     *
     * @param httpUrl
     *            请求的url
     * @param param
     *            form表单的参数（key,value形式）
     * @return
     */
    public static String doPostForm(String httpUrl, Map<String, String> param, String code)
    {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try
        {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");

            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            // connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
            os.write(createLinkString(param).getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200)
            {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null)
                {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
            else
            {
                LogFactory.error("Code:{},httpUrl:{}", connection.getResponseCode(), httpUrl);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            // 关闭资源
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (null != os)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (null != is)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * 
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> parameters)
    {
        StringBuffer params = new StringBuffer();
        try
        {

            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpUtil.requestEncoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
                return params.toString();
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        return "";
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param string
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> parameters, String encoder)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try
        {
            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(),
                            HttpUtil.requestEncoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        }
        catch (Exception e)
        {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
