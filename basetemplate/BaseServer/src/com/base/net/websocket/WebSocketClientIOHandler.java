package com.base.net.websocket;

import java.util.List;

import com.base.net.CommonMessage;
import com.base.net.client.AbstractClientPacketHandler;
import com.base.net.client.IClientConnection;
import com.base.type.CommonNettyConst;
import com.util.print.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class WebSocketClientIOHandler extends ChannelInboundHandlerAdapter
{
    private WebSocketServerHandshaker handshaker;

    protected AbstractClientPacketHandler handler = null;

    public WebSocketClientIOHandler(AbstractClientPacketHandler h)
    {
        handler = h;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        IClientConnection connection = new WebSocketNettyConnection(handler, ctx.channel());
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
        try
        {
            // HTTP接入，WebSocket第一次连接使用HTTP连接,用于握手
            if (msg instanceof FullHttpRequest)
                handleHttpRequest(ctx, (FullHttpRequest) msg);

            // 数据处理
            if (msg instanceof WebSocketFrame)
                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LogFactory.error("Exception:", cause);
    }

    /**
     * WS处理消息
     * 
     * @param ctx
     * @param frame
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame)
    {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame)
        {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame)
        {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 二进制大数据处理，分片处理
        if (frame instanceof ContinuationWebSocketFrame)
        {
            ByteBuf buf = frame.content();
            WebSocketNettyConnection conn = (WebSocketNettyConnection) ctx.channel().attr(CommonNettyConst.CLIENT_CONNECTION).get();
            List<CommonMessage> messages = conn.onMessage(buf);
            for (CommonMessage message : messages)
            {
                this.handler.process(conn, message);
            }
        }

        // 二进制数据处理
        if (frame instanceof BinaryWebSocketFrame)
        {
            ByteBuf buf = frame.content();
            WebSocketNettyConnection conn = (WebSocketNettyConnection) ctx.channel().attr(CommonNettyConst.CLIENT_CONNECTION).get();
            List<CommonMessage> messages = conn.onMessage(buf);
            for (CommonMessage message : messages)
            {
                this.handler.process(conn, message);
            }
        }

        // 文本数据处理
        if (frame instanceof TextWebSocketFrame)
        {
            // 返回应答消息
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().write(new TextWebSocketFrame("服务器收到并返回：" + request));
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req)
    {
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade"))))
        {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("", null, false, 65536);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null)
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        else
            handshaker.handshake(ctx.channel(), req);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res)
    {
        // 返回应答给客户端
        if (res.status().code() != 200)
        {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200)
        {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req)
    {
        return false;
    }
}
