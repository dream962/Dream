package com.base.rpc.client;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.base.rpc.RpcRequest;
import com.base.rpc.RpcResponse;
import com.util.print.LogFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse>
{
    /** 阻塞Future,回调缓存池 */
    private ConcurrentHashMap<String, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

    private volatile Channel channel;

    /** 远程服务器地址 */
    private SocketAddress remotePeer;

    public Channel getChannel()
    {
        return channel;
    }

    public SocketAddress getRemotePeer()
    {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception
    {
        String requestId = response.getRequestId();
        RPCFuture rpcFuture = pendingRPC.get(requestId);
        if (rpcFuture != null)
        {
            pendingRPC.remove(requestId);
            rpcFuture.done(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LogFactory.error("client caught exception", cause);
        ctx.close();
    }

    public void close()
    {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 阻塞式远程方法调用
     * 
     * @param request
     *            方法说明
     * @return
     */
    public RPCFuture sendRequest(RpcRequest request)
    {
        // 使用countdown保证同步调用
        final CountDownLatch latch = new CountDownLatch(1);

        RPCFuture rpcFuture = new RPCFuture(request);
        pendingRPC.put(request.getRequestId(), rpcFuture);

        // 当channel发送完成后，计数减少，如果没有发送成功，会一直等待
        channel.writeAndFlush(request).addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future)
            {
                latch.countDown();
            }
        });
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            LogFactory.error(e.getMessage());
        }

        return rpcFuture;
    }
}
