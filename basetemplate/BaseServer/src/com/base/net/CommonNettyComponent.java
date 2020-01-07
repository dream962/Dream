package com.base.net;

import com.base.component.AbstractCommandComponent;
import com.base.component.ComponentManager;
import com.base.component.GlobalConfigComponent;
import com.base.net.client.AbstractClientPacketHandler;
import com.base.net.client.ClientIOHandler;
import com.base.net.coder.CommonMessageDecoder;
import com.base.net.coder.CommonMessageEncoder;
import com.util.ThreadPoolUtil;

import io.netty.channel.ChannelPipeline;

/**
 * @date 2015年10月15日 下午3:00:48
 * @author dansen
 * @desc netty组件默认实现
 */
public class CommonNettyComponent extends AbstractNettyComponent
{
    public class DefaultClientHandler extends AbstractClientPacketHandler
    {
        @Override
        public AbstractCommandComponent getComponent()
        {
            DefaultCommandComponent cm = ComponentManager.getInstance().getComponent(DefaultCommandComponent.class);
            return cm;
        }
    }

    @Override
    public void initGroup()
    {
        int count = Runtime.getRuntime().availableProcessors();
        parentCount = count * 2;
        parentExecutor = ThreadPoolUtil.fixedServicePool(parentCount, "netty-default-pool");
        childCount = count * 4;
        childExecutor = ThreadPoolUtil.fixedServicePool(childCount, "netty-default-pool");
    }

    @Override
    protected void acceptorInit(ChannelPipeline pipeline)
    {
        pipeline.addLast("codec-d", new CommonMessageDecoder());
        pipeline.addLast("codec-e", new CommonMessageEncoder());
        pipeline.addLast("hander", new ClientIOHandler(new DefaultClientHandler()));
    }

    @Override
    protected int getPort()
    {
        return Integer.valueOf(GlobalConfigComponent.getConfig().server.ports.split(",")[0]);
    }
}
