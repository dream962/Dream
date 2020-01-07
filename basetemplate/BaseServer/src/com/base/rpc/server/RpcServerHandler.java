package com.base.rpc.server;

import java.util.HashMap;
import java.util.Map;

import com.base.rpc.RpcRequest;
import com.base.rpc.RpcResponse;
import com.base.rpc.util.RpcUtil;
import com.util.print.LogFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * RPC服务端处理Handler
 * 
 * @author dream
 *
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>
{
    private Map<String, Object> handlerMap = new HashMap<>();

    public RpcServerHandler(Map<String, Object> handlerMap)
    {
        this.handlerMap = handlerMap;
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        System.err.println("channelRegistered");
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        System.err.println("channelActive");
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
        System.err.println("channelUnregistered");
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        System.err.println("channelInactive");
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception
    {
        // 多线程处理
        RpcServer.submit(() -> {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try
            {
                String className = request.getClassName();
                Object serviceBean = handlerMap.get(className);
                Class<?> serviceClass = serviceBean.getClass();
                String methodName = request.getMethodName();
                Class<?>[] parameterTypes = request.getParameterTypes();
                Object[] parameters = request.getParameters();

                Object result = RpcUtil.methodInvoke(serviceClass, serviceBean, methodName, parameterTypes, parameters);
                response.setResult(result);
            }
            catch (Throwable t)
            {
                response.setError(t.toString());
                LogFactory.error("RPC Server handle request error", t);
            }

            ctx.writeAndFlush(response).addListener(new ChannelFutureListener()
            {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception
                {
                    LogFactory.debug("Server Send response for request " + request.getRequestId());
                }
            });
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LogFactory.error("server caught exception", cause);
        ctx.close();
    }
}
