package com.base.net.client;

import com.base.type.CommonNettyConst;
import com.util.print.LogFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientIOHandler extends ChannelInboundHandlerAdapter
{
    protected AbstractClientPacketHandler handler = null;

    public ClientIOHandler(AbstractClientPacketHandler h)
    {
        handler = h;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        IClientConnection connection = new ClientNettyConnection(handler, ctx.channel());
        ctx.channel().attr(CommonNettyConst.CLIENT_CONNECTION).set(connection);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);
        IClientConnection conn = (IClientConnection) ctx.channel().attr(CommonNettyConst.CLIENT_CONNECTION).get();
        conn.onDisconnect();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        super.channelRead(ctx, msg);
        IClientConnection conn = (IClientConnection) ctx.channel().attr(CommonNettyConst.CLIENT_CONNECTION).get();
        this.handler.process(conn, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LogFactory.warn("Exception:", cause);
    }
}
