package com.base.hooker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BaseShutdownHooker extends Thread
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseShutdownHooker.class);
    
    private IStopHooker hooker;
    public BaseShutdownHooker(IStopHooker server)
    {
        this.hooker = server;
    }

    /**
     * 退出回调，停止服务器
     */
    public void run()
    {
        LOGGER.error("BaseShutdownHooker is Running.");
        if (hooker != null)
        {
            hooker.callbackHooker();
        }
    }
}
