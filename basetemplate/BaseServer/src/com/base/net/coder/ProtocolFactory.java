package com.base.net.coder;

import com.base.net.CommonMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public interface ProtocolFactory
{
    public ByteToMessageDecoder getDecoder();
    
    public MessageToByteEncoder<CommonMessage> getEncoder();
    
    public ChannelHandlerAdapter getHandler();
}
