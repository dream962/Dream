package com.base.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.hooker.BaseShutdownHooker;
import com.base.hooker.IStopHooker;

/**
 * 服务器基础类
 * @author dream.wang
 *
 */
public abstract class BaseServer implements IStopHooker
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServer.class);
    
    public boolean start()
    {
        if(loadComponent())
        {
            Runtime.getRuntime().addShutdownHook(new BaseShutdownHooker(this));
            return true;
        }
        
        return false;
    }
    
    protected abstract boolean loadComponent();
    
    public abstract void stop();
    
    @Override
    public void callbackHooker()
    {
        LOGGER.error("BaseServer callbackHooker is Running.");
        stop();
    }
}
