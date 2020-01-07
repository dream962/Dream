package com.game.component;

import com.base.component.AbstractCommandComponent;
import com.base.component.ComponentManager;
import com.base.component.GlobalConfigComponent;
import com.base.net.AbstractNettyComponent;
import com.base.net.client.AbstractClientPacketHandler;
import com.base.net.client.ClientIOHandler;
import com.base.net.coder.StrictMessageDecoder;
import com.base.net.coder.StrictMessageEncoder;

import io.netty.channel.ChannelPipeline;

/**
 * 游戏服网络组件
 * 
 * @author dream
 *
 */
public class GameServerNettyComponent extends AbstractNettyComponent
{
    public class GameServerClientHandler extends AbstractClientPacketHandler
    {
        @Override
        public AbstractCommandComponent getComponent()
        {
            UserCommandComponent cm = ComponentManager.getInstance().getComponent(UserCommandComponent.class);
            return cm;
        }
    }

    @Override
    public void initGroup()
    {
        int count = Runtime.getRuntime().availableProcessors();
        parentCount = count * 1;
        childCount = count * 3 + 1;
    }

    @Override
    protected void acceptorInit(ChannelPipeline pipeline)
    {
        pipeline.addLast("codec-d", new StrictMessageDecoder());
        pipeline.addLast("codec-e", new StrictMessageEncoder());

        // pipeline.addLast("codec-d", new CommonMessageDecoder());
        // pipeline.addLast("codec-e", new CommonMessageEncoder());

        pipeline.addLast("handler", new ClientIOHandler(new GameServerClientHandler()));
    }

    @Override
    protected int getPort()
    {
        return Integer.valueOf(GlobalConfigComponent.getConfig().server.ports.split(",")[0]);
    }
}
