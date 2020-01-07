package com.base.rpc.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.rpc.RpcDecoder;
import com.base.rpc.RpcEncoder;
import com.base.rpc.RpcResponse;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 连接管理器
 * 
 * @author dream
 *
 */
public class ConnectManage
{
    private static final Logger logger = LoggerFactory.getLogger(ConnectManage.class);

    private volatile static ConnectManage connectManage;

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));

    /** 连接的节点列表 */
    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private Condition connected = lock.newCondition();

    /** 等待时间 */
    private long connectTimeoutMillis = 6000;

    private AtomicInteger roundRobin = new AtomicInteger(0);

    private volatile boolean isRuning = true;

    private ConnectManage()
    {
    }

    public static ConnectManage getInstance()
    {
        if (connectManage == null)
        {
            synchronized (ConnectManage.class)
            {
                if (connectManage == null)
                {
                    connectManage = new ConnectManage();
                }
            }
        }

        return connectManage;
    }

    /**
     * 创建远程连接客户端
     * 
     * @param allServerAddress
     */
    public void updateConnectedServer(String ip, int port)
    {
        final InetSocketAddress serverNodeAddress = new InetSocketAddress(ip, port);
        connectServerNode(serverNodeAddress);
    }

    public void reconnect(final RpcClientHandler handler, final SocketAddress remotePeer)
    {
        if (handler != null)
        {
            connectedHandlers.remove(handler);
        }
        connectServerNode((InetSocketAddress) remotePeer);
    }

    private void connectServerNode(final InetSocketAddress remotePeer)
    {
        threadPoolExecutor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                Bootstrap b = new Bootstrap();
                b.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception
                    {
                        ChannelPipeline cp = socketChannel.pipeline();
                        cp.addLast(new RpcEncoder());
                        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                        cp.addLast(new RpcDecoder(RpcResponse.class));
                        cp.addLast(new RpcClientHandler());
                    }
                });

                ChannelFuture channelFuture = b.connect(remotePeer);
                channelFuture.addListener(new ChannelFutureListener()
                {
                    @Override
                    public void operationComplete(final ChannelFuture channelFuture) throws Exception
                    {
                        if (channelFuture.isSuccess())
                        {
                            logger.debug("Successfully connect to remote server. remote peer = " + remotePeer);
                            RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                            addHandler(handler);
                        }
                    }
                });
            }
        });
    }

    private void addHandler(RpcClientHandler handler)
    {
        connectedHandlers.add(handler);
        InetSocketAddress remoteAddress = (InetSocketAddress) handler.getChannel().remoteAddress();
        signalAvailableHandler();
    }

    /**
     * 唤醒
     */
    private void signalAvailableHandler()
    {
        lock.lock();
        try
        {
            connected.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 等待
     * 
     * @return
     * @throws InterruptedException
     */
    private boolean waitingForHandler() throws InterruptedException
    {
        lock.lock();
        try
        {
            return connected.await(this.connectTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        finally
        {
            lock.unlock();
        }
    }

    public RpcClientHandler chooseHandler()
    {
        int size = connectedHandlers.size();
        while (isRuning && size <= 0)
        {
            try
            {
                boolean available = waitingForHandler();
                if (available)
                {
                    size = connectedHandlers.size();
                }
            }
            catch (InterruptedException e)
            {
                logger.error("Waiting for available node is interrupted! ", e);
                throw new RuntimeException("Can't connect any servers!", e);
            }
        }
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return connectedHandlers.get(index);
    }

    public void stop()
    {
        isRuning = false;
        for (int i = 0; i < connectedHandlers.size(); ++i)
        {
            RpcClientHandler connectedServerHandler = connectedHandlers.get(i);
            connectedServerHandler.close();
        }
        signalAvailableHandler();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }
}
