package com.upload.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil
{
    /**
     * 连接超时
     */
    public static int CONNECT_TIMEOUT = 5000;

    /**
     * 读取数据超时
     */
    public static int READ_TIMEOUT = 15000;

    /**
     * 请求编码
     */
    public static String ENCODING = "UTF-8";

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
    public static String doGet(String reqUrl, Map<?, ?> parameters, String encoding, int connectTieout,
            int readTimeout)
    {
        String responseContent = null;
        HttpURLConnection urlConn = null;
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
                    params.append(URLEncoder.encode(element.getValue().toString(), encoding));
                    params.append("&");
                }
            }
            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
                reqUrl += (reqUrl.indexOf("?") != -1 ? "&" : "?") + params;
            }
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(connectTieout);
            urlConn.setReadTimeout(readTimeout);
            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
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
            LogFactory.error("reqUrl:" + reqUrl, e);
        }
        finally
        {
            if (urlConn != null)
            {
                urlConn.disconnect();
            }
        }
        return responseContent;
    }

    public static String doGet(String reqUrl, Map<String, String> parameters)
    {
        return doGet(reqUrl, parameters, ENCODING, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String doGet(String reqUrl)
    {
        return doGet(reqUrl, null);
    }

    public static String doGet(String reqUrl, int timeOut)
    {
        return doGet(reqUrl, null, ENCODING, timeOut, timeOut);
    }

    /**
     * Post请求，参数格式：params={json}
     * 
     * @param reqUrl
     * @param json
     * @return
     */
    public static String doPostByParams(String reqUrl, String json)
    {
        String responseContent = null;
        HttpURLConnection urlConn = null;
        try
        {
            StringBuffer params = new StringBuffer();
            if (json != null && !json.isEmpty())
                params.append("params=").append(URLEncoder.encode(json, ENCODING));

            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(CONNECT_TIMEOUT);
            urlConn.setReadTimeout(READ_TIMEOUT);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] b = params.toString().getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();

            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, ENCODING));
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
            if (urlConn != null)
            {
                urlConn.disconnect();
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
    public static String doPost(String reqUrl, Map<String, String> parameters, String encoding, int connectTimeout,
            int readTimeout)
    {
        String responseContent = null;
        HttpURLConnection urlConn = null;
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
                    params.append(URLEncoder.encode(element.getValue().toString(), encoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] b = params.toString().getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();

            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
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
            if (urlConn != null)
            {
                urlConn.disconnect();
            }
        }

        return responseContent;
    }

    public static String doPost(String reqUrl, Map<String, String> parameters)
    {
        return doPost(reqUrl, parameters, ENCODING, CONNECT_TIMEOUT, READ_TIMEOUT);
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
    public static String doGetByHttps(String reqUrl, Map<?, ?> parameters, String encoding, int connectTimeout,
            int readTimeout)
    {
        HttpsURLConnection urlConn = null;
        String responseContent = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType)
                {
                }

            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(), encoding));
                    params.append("&");
                }
            }
            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
                reqUrl += (reqUrl.indexOf("?") != -1 ? "&" : "?") + params;
            }
            URL url = new URL(reqUrl);
            urlConn = (HttpsURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
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
            e.printStackTrace();
        }
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConn != null)
            {
                urlConn.disconnect();
            }
        }

        return responseContent;
    }

    public static String doGetByHttps(String reqUrl, Map<String, String> parameters)
    {
        return doGetByHttps(reqUrl, parameters, ENCODING, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String doGetByHttps(String reqUrl)
    {
        return doGetByHttps(reqUrl, null);
    }

    /**
     * <pre>
     * https POST请求
     * </pre>
     *
     * @param reqUrl
     * @param parameters
     * @param recvEncoding
     * @return
     */
    public static String doPostByHttps(String reqUrl, Map<String, String> parameters, String encoding,
            int connectTimeout, int readTimeout)
    {
        HttpsURLConnection urlConn = null;
        String responseContent = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType)
                {
                }

            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    params.append(URLEncoder.encode(element.getValue().toString(), encoding));
                    params.append("&");
                }
            }
            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            urlConn = (HttpsURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setDoOutput(true);
            urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] b = params.toString().getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();
            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
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
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConn != null)
            {
                urlConn.disconnect();
            }
        }
        return responseContent;
    }

    public static String doPostByHttps(String reqUrl, Map<String, String> parameters)
    {
        return doPostByHttps(reqUrl, parameters, ENCODING, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    public static String doPostJsonByHttps(String reqUrl, String json)
    {
        return doPostJsonByHttps(reqUrl, json, ENCODING, CONNECT_TIMEOUT, READ_TIMEOUT);
    }

    /**
     * <pre>
     * HTTPS POST JSON
     * </pre>
     *
     * @param reqUrl
     * @param parameters
     * @param recvEncoding
     * @return
     */
    public static String doPostJsonByHttps(String reqUrl, String json, String encoding, int connectTimeout,
            int readTimeout)
    {
        HttpsURLConnection urlConn = null;
        String responseContent = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType)
                {
                }

            } };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL(reqUrl);
            urlConn = (HttpsURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setDoOutput(true);
            urlConn.addRequestProperty("Content-Type", "application/json;charset=utf-8");

            urlConn.getOutputStream().write(json.getBytes(encoding));
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();

            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
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
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (urlConn != null)
            {
                urlConn.disconnect();
            }
        }
        return responseContent;
    }

}
