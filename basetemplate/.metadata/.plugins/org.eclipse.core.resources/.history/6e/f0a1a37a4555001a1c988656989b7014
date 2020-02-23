package com.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upload.component.ComponentManager;
import com.upload.component.DataComponent;
import com.upload.component.GlobalConfigComponent;
import com.upload.component.WebComponent;
import com.upload.database.DBPoolComponent;

public class ManagerServer
{
    private static final Logger logger = LoggerFactory.getLogger(ManagerServer.class);

    public static void main(String[] args)
    {
        long time = System.currentTimeMillis();

        logger.info("ManagerServer is starting...");

        String path = "";
        if (args.length <= 0)
        {
            logger.error("Please input the config path.");
            path = "config/config.xml";
        }
        else
        {
            path = args[0];
        }

        // initialize the configure
        if (!GlobalConfigComponent.init(path))
        {
            logger.error("Configuration initialization error");
            return;
        }

        if (!ComponentManager.getInstance().addComponent(DBPoolComponent.class.getName()))
        {
            logger.error("DBPoolComponent 启动失败.");
            return;
        }

        if (!ComponentManager.getInstance().addComponent(DataComponent.class.getName()))
        {
            logger.error("数据失败.");
            return;
        }

        // if (!ComponentManager.getInstance().addComponent(ServerListComponent.class.getName()))
        // {
        // logger.error("服务器列表模块失败");
        // return;
        // }

        if (!ComponentManager.getInstance().addComponent(WebComponent.class.getName()))
        {
            logger.error("jetty启动失败");
            return;
        }

        if (!ComponentManager.getInstance().start())
        {
            logger.error("服务器启动异常");
            return;
        }

        logger.error(String.format("ManagerServer has started successfully, taken %d millis.",
                System.currentTimeMillis() - time));
    }
}
