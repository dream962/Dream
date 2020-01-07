package com.base.rpc.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.base.rpc.RpcDecoder;
import com.base.rpc.RpcEncoder;
import com.base.rpc.RpcRequest;
import com.util.NamedThreadFactory;
import com.util.print.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * RPC 服务组件
 * gRPC-java:https://github.com/grpc/grpc-java
 * 参照修改,支持双工模式.
 * 
 * @author dream
 *
 */
public class RpcServer
{
    private int port;

    /** RPC服务列表《类名，服务handler实例》 */
    private Map<String, Object> handlerMap = new HashMap<>();

    /** 连接的客户端 */
    private Map<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>();

    private static ExecutorService threadPoolExecutor;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    public RpcServer(int port)
    {
        threadPoolExecutor = Executors.newFixedThreadPool(8, new NamedThreadFactory("RPC-Service-Pool"));
        this.port = port;
    }

    public void stop()
    {
        if (bossGroup != null)
        {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null)
        {
            workerGroup.shutdownGracefully();
        }

        if (!threadPoolExecutor.isShutdown())
            threadPoolExecutor.shutdownNow();
    }

    public static void submit(Runnable task)
    {
        threadPoolExecutor.submit(task);
    }

    public RpcServer addService(String interfaceName, Object serviceBean)
    {
        if (!handlerMap.containsKey(interfaceName))
        {
            handlerMap.put(interfaceName, serviceBean);
        }

        return this;
    }

    public void start() throws Exception
    {
        if (bossGroup == null && workerGroup == null)
        {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel channel) throws Exception
                {
                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0)).addLast(new RpcDecoder(RpcRequest.class)).addLast(new RpcEncoder()).addLast(
                            new RpcServerHandler(handlerMap));
                }
            }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            LogFactory.info("RPC Server started on port {}", port);

            future.channel().closeFuture().sync();
        }
    }

}
