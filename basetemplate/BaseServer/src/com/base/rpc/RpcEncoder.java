package com.base.rpc;

import com.base.rpc.util.RpcUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC 编码器
 * 
 * @author dream
 *
 */
public class RpcEncoder extends MessageToByteEncoder<Object>
{
    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception
    {
        byte[] data = RpcUtil.serialize(in);
        // 长度前4位
        out.writeInt(data.length);
        // 数据内容
        out.writeBytes(data);
    }
}
