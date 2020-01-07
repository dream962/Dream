package com.base.web;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.config.WebServerConfig;
import com.base.config.WebServerConfig.ServletConfig;
import com.util.ClassUtil;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 嵌入式http调用使用的连接组件
 * 
 */
public class WebComponent extends AbstractComponent
{
    /**
     * jetty自带的server
     */
    private Server server;

    private WebAppContext webAppContext;
    private ServletContextHandler servletContextHandler;

    /**
     * 添加过滤器
     * 
     * @param filterClass
     *            过滤器实现类
     * @param pathSpec
     *            过滤路径
     * @param dispatches
     *            过滤类型
     */
    public void addFilter(Class<? extends Filter> filterClass, String pathSpec, EnumSet<DispatcherType> dispatches)
    {
        if (servletContextHandler != null)
            servletContextHandler.addFilter(filterClass, pathSpec, dispatches);
    }

    public ServletContextHandler getContext()
    {
        return servletContextHandler;
    }

    @Override
    public boolean start()
    {
        super.start();
        try
        {
            server.start();
        }
        catch (Exception e)
        {
            LogFactory.error("WebComponent Start Exception:", e);
            return false;
        }

        return true;
    }

    public boolean initialize()
    {
        try
        {
            WebServerConfig web = GlobalConfigComponent.getConfig().web;

            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setMaxThreads(web.serverThreadCount);
            server = new Server(threadPool);

            webAppContext = new WebAppContext();
            servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

            // 从相应的包加载WebComponent的不同接口
            List<Class<?>> activityClass = ClassUtil.getClasses(web.packages);
            for (Class<?> class1 : activityClass)
            {
                WebHandleAnnotation annotation = class1.getAnnotation(WebHandleAnnotation.class);

                if (annotation != null)
                {
                    try
                    {
                        String path = annotation.cmdName();
                        Servlet servlet = (Servlet) class1.newInstance();
                        servletContextHandler.addServlet(new ServletHolder(servlet), path);
                    }
                    catch (InstantiationException e)
                    {
                        LogFactory.error("WebComponent Exception:", e);
                        continue;
                    }
                    catch (IllegalAccessException e)
                    {
                        LogFactory.error("WebComponent Exception:", e);
                        continue;
                    }
                }
            }

            // 第三方或者系统的配置
            if (web.mapping != null && !web.mapping.isEmpty())
            {
                for (ServletConfig config : web.mapping)
                {
                    Servlet servlet = (Servlet) ClassUtil.getClass(config.classPath).newInstance();
                    servletContextHandler.addServlet(new ServletHolder(servlet), config.url);

                    LogFactory.info("web服务器添加Servlet:" + config.classPath);
                }
            }

            // resource 和 context 的添加顺序必须如此
            HandlerList handlerList = new HandlerList();

            // 资源处理器
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(web.isShowDirectory);
            resourceHandler.setWelcomeFiles(new String[] { "index.html", web.welcomeFile });
            // 资源根目录
            if (StringUtil.isNullOrEmpty(web.resourcePath))
            {
                resourceHandler.setResourceBase(".");
                webAppContext.setResourceBase(".");
            }
            else
            {
                resourceHandler.setResourceBase(web.resourcePath);
                webAppContext.setResourceBase(web.resourcePath);
            }

            handlerList.addHandler(resourceHandler);
            handlerList.addHandler(servletContextHandler);
            handlerList.addHandler(webAppContext);

            server.setHandler(handlerList);

            // HTTP Configuration
            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setOutputBufferSize(32768);
            http_config.setRequestHeaderSize(8192);
            http_config.setResponseHeaderSize(8192);
            http_config.setSendServerVersion(true);
            http_config.setSendDateHeader(false);

            // HTTP 连接器连接器
            ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
            http.setPort(web.port);
            http.setIdleTimeout(30000);
            server.addConnector(http);

            // HTTPS 连接器配置
            if(web.https.isOpen)
                initHttps(http_config);

            // 额外配置
            server.setDumpAfterStart(false);
            server.setDumpBeforeStop(false);
            server.setStopAtShutdown(true);

            LogFactory.info("web服务器启动--端口" + web.port);
        }
        catch (Exception e)
        {
            LogFactory.error("WebComponent Initialize Exception:", e);
            return false;
        }

        return true;
    }

    private ServerConnector initHttps(HttpConfiguration http_config)
    {
        WebServerConfig web = GlobalConfigComponent.getConfig().web;
        // === jetty-https.xml ===
        // SSL Context Factory
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(web.https.jks);
        sslContextFactory.setKeyStorePassword(web.https.password);
        sslContextFactory.setKeyManagerPassword(web.https.password);
        sslContextFactory.setTrustStorePath(web.https.jks);
        sslContextFactory.setTrustStorePassword(web.https.password);

        sslContextFactory.setExcludeCipherSuites(
                "SSL_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_DSS_WITH_DES_CBC_SHA",
                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");

        // SSL HTTP Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(https_config));
        sslConnector.setPort(web.https.port);

        server.addConnector(sslConnector);

        return sslConnector;
    }

    @Override
    public void stop()
    {
        try
        {
            server.stop();
        }
        catch (Exception e)
        {
            LogFactory.error("Exception:", e);
        }
    }

}
