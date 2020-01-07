package com.base.net.coder;

import java.nio.ByteBuffer;

import com.base.net.CommonMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CommonMessageEncoder extends MessageToByteEncoder<CommonMessage>
{
    public CommonMessageEncoder()
    {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CommonMessage msg, ByteBuf out) throws Exception
    {
        ByteBuffer buffer = msg.toByteBuffer();
        out.writeBytes(buffer.array());
    }
}
