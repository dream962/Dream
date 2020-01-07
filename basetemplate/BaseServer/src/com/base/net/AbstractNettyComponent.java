package com.base.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.util.ThreadPoolUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty网络组件抽象基类。
 * 
 */
public abstract class AbstractNettyComponent extends AbstractComponent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyComponent.class);

    protected ServerBootstrap bootstrap = null;

    protected ExecutorService parentExecutor;

    protected int parentCount;

    protected ExecutorService childExecutor;

    protected int childCount;

    /**
     * mina初始化配置。
     * 
     * @param serverAcceptor
     */
    protected abstract void acceptorInit(ChannelPipeline pipeline);

    /**
     * 获取监听的端口。
     * 
     * @return
     */
    protected int getPort()
    {
        return -1;
    }

    public void initGroup()
    {
        int count = Runtime.getRuntime().availableProcessors();
        parentCount = count;
        parentExecutor = ThreadPoolUtil.fixedServicePool(parentCount, "netty-parent-pool");
        childCount = count * 2;
        childExecutor = ThreadPoolUtil.fixedServicePool(childCount, "netty-child-pool");
    }

    @Override
    public boolean initialize()
    {
        initGroup();

        bootstrap = new ServerBootstrap();

        bootstrap.group(new NioEventLoopGroup(parentCount), new NioEventLoopGroup(childCount)).channel(
                NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        acceptorInit(ch.pipeline());
                    }
                });

        bootstrap.option(ChannelOption.SO_BACKLOG, 256) // 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
            .option(ChannelOption.SO_RCVBUF, 1024 * 256) // 接受缓存区
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childOption(ChannelOption.ALLOCATOR,
                    PooledByteBufAllocator.DEFAULT).childOption(ChannelOption.SO_SNDBUF, 1024 * 256) // 发送缓冲区
            .childOption(ChannelOption.TCP_NODELAY, true);// 保证高实时性，有数据发送时就马上发送

        return true;
    }

    @Override
    public boolean start()
    {
        try
        {
            int port = getPort();
            bootstrap.bind(port);
            LOGGER.info("Listening at port " + port);
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
            return false;
        }

        return true;
    }

    protected InetSocketAddress[] getPorts()
    {
        String[] portStr = GlobalConfigComponent.getConfig().server.ports.split("\\,");
        if (portStr.length > 0)
        {
            InetSocketAddress[] ports = new InetSocketAddress[portStr.length];
            for (int i = 0; i < ports.length; i++)
                ports[i] = new InetSocketAddress(Integer.valueOf(portStr[i]));

            return ports;
        }

        return null;
    }

    @Override
    public void stop()
    {
        if (parentExecutor != null)
            parentExecutor.shutdownNow();

        if (childExecutor != null)
            childExecutor.shutdownNow();
    }

    @Override
    public boolean reload()
    {
        return true;
    }
}
