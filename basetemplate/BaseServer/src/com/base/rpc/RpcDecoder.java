package com.base.rpc;

import java.util.List;

import com.base.rpc.util.RpcUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Rpc数据解码
 * 
 * @author dream
 *
 */
public class RpcDecoder extends ByteToMessageDecoder
{
    private Class<?> clazz;

    public RpcDecoder(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        // 长度不足
        if (in.readableBytes() < 4)
            return;

        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength)
        {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = RpcUtil.deserialize(data, clazz);
        out.add(obj);
    }
}
