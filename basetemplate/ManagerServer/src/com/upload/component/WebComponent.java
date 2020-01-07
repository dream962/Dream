package com.upload.component;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upload.config.WebServerConfig;
import com.upload.config.WebServerConfig.ServletConfig;
import com.upload.util.ClassUtil;
import com.upload.util.StringUtil;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 嵌入式http调用使用的连接组件
 * 
 */
public class WebComponent extends AbstractComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebComponent.class);

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
            e.printStackTrace();
            LOGGER.error("WebComponent Start Exception:", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean initialize()
    {
        WebServerConfig web = GlobalConfigComponent.getConfig().web;
        server = new Server(web.port);

        try
        {
            webAppContext = new WebAppContext();
            servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

            // 如果是windows系统，不需要进行文件map优化，防止锁定文件
            if (System.getProperty("os.name").toLowerCase().contains("win"))
            {
                webAppContext.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            // 从相应的包加载WebComponent的不同接口
            String servletPackage = web.packages;
            List<Class<?>> activityClass = ClassUtil.getClasses(servletPackage);
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
                        LOGGER.error("WebComponent Exception:", e);
                        continue;
                    }
                    catch (IllegalAccessException e)
                    {
                        LOGGER.error("WebComponent Exception:", e);
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

                    LOGGER.info("web服务器添加Servlet:" + config.classPath);
                }
            }

            ResourceHandler resourceHandler = new ResourceHandler();

            resourceHandler.setDirectoriesListed(web.isShowDirectory);

            resourceHandler.setWelcomeFiles(
                    new String[] { "index.html", web.welcomeFile });

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

            // resource 和 context 的添加顺序必须如此
            HandlerList handlerList = new HandlerList();
            handlerList.addHandler(resourceHandler);
            handlerList.addHandler(servletContextHandler);
            handlerList.addHandler(webAppContext);

            server.setHandler(handlerList);

            LOGGER.info("web服务器启动--端口" + web.port);
        }
        catch (Exception e)
        {
            LOGGER.error("WebComponent Initialize Exception:", e);
            return false;
        }

        return true;
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

        }
    }

}
