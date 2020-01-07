/**
 * 
 */
package com;

import com.account.component.ServerComponent;
import com.base.component.ComponentManager;
import com.base.component.GlobalConfigComponent;
import com.base.component.SimpleScheduleComponent;
import com.base.database.DBPoolComponent;
import com.base.redis.RedisClientComponent;
import com.base.server.BaseServer;
import com.base.timer.QuartzComponent;
import com.base.web.WebComponent;
import com.data.account.cache.AccountCacheComponent;
import com.util.print.LogFactory;

/**
 * 账号登录服
 * 
 * @author dream
 *
 */
public class AccountServer extends BaseServer
{
    private static final AccountServer INSTANCE = new AccountServer();

    public static AccountServer getInstance()
    {
        return INSTANCE;
    }

    public static void main(String[] args)
    {
        long time = System.currentTimeMillis();

        try
        {
            String path = "";

            if (args.length > 0)
                path = args[0];
            else
            {
                path = "media.xml";
                LogFactory.error("path set default value.");
            }

            if (!GlobalConfigComponent.init(path))
            {
                LogFactory.error("加载配置出错，路径不正确");
                System.exit(1);
                return;
            }

            if (!AccountServer.getInstance().start())
            {
                LogFactory.error("GameServer has started failed.");
                System.exit(1);
            }

            LogFactory.error(String.format("AccountServer has started successfully, taken %d millis.", System.currentTimeMillis() - time));
        }
        catch (Exception e)
        {
            LogFactory.error("账户服务器启动异常", e);
        }
    }

    @Override
    protected boolean loadComponent()
    {
        try
        {
            if (!ComponentManager.getInstance().addComponent(DBPoolComponent.class.getName()))
                return false;// 数据库组件

            if (!ComponentManager.getInstance().addComponent(RedisClientComponent.class.getName()))
                return false;// redis组件

            if (!ComponentManager.getInstance().addComponent(AccountCacheComponent.class.getName()))
                return false;// 缓存处理组件

            if (!ComponentManager.getInstance().addComponent(QuartzComponent.class.getName()))
                return false;// 定时器组件

            if (!ComponentManager.getInstance().addComponent(SimpleScheduleComponent.class.getName()))
                return false;// 定时器组件

            if (!ComponentManager.getInstance().addComponent(ServerComponent.class.getName()))
                return false;// 服务器组件

            if (!ComponentManager.getInstance().addComponent(WebComponent.class.getName()))
                return false;// web组件

            if (ComponentManager.getInstance().start())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
            return false;
        }
    }

    @Override
    public void stop()
    {
        ComponentManager.getInstance().stop();
        Runtime.getRuntime().halt(0);
    }
}
