package com.base.net.websocket;

import java.util.ArrayList;
import java.util.List;

import com.base.net.CommonMessage;
import com.base.net.client.AbstractClientPacketHandler;
import com.base.net.client.ClientNettyConnection;
import com.util.print.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 基于Netty的WebSocket客户端实现。
 * 
 */
public class WebSocketNettyConnection extends ClientNettyConnection
{
    private ByteBuf buffer;

    public WebSocketNettyConnection(AbstractClientPacketHandler packetHandler, Channel session)
    {
        super(packetHandler, session);
        buffer = Unpooled.buffer(Short.MAX_VALUE);
    }

    @Override
    public void send(Object packet)
    {
        if (channel != null && channel.isWritable())
        {
            CommonMessage msg = (CommonMessage) packet;
            ByteBuf buffer = msg.toByteBuf();
            channel.writeAndFlush(new BinaryWebSocketFrame(buffer));
        }
    }

    public List<CommonMessage> onMessage(ByteBuf byteBuf)
    {
        // 读取数据到缓存
        buffer.writeBytes(byteBuf, 0, byteBuf.readableBytes());

        List<CommonMessage> list = new ArrayList<>();

        while (buffer.isReadable(CommonMessage.HDR_SIZE))
        {
            try
            {
                // 标记读指针
                buffer.markReaderIndex();

                // 包头验证
                short headerFlag = buffer.readShort();
                if (CommonMessage.HEADER != headerFlag)
                    break;

                // 数据长度验证
                short length = buffer.readShort();
                if (length <= 0 || length >= Short.MAX_VALUE)
                {
                    LogFactory.error("Message Length Invalid Length = " + length + ", drop this Message.");
                    break;
                }

                // 判断包数据是否完整
                if (!buffer.isReadable(length - 4))
                {
                    // 数据还不够读取,等待下一次读取
                    buffer.resetReaderIndex(); // 复位
                    break;
                }

                buffer.resetReaderIndex();

                // 封装数据
                byte[] pktBytes = new byte[length];
                buffer.readBytes(pktBytes);

                CommonMessage message = CommonMessage.build(pktBytes);
                list.add(message);
            }
            catch (Exception e)
            {
                LogFactory.error("Binary Data Resolve Exception...", e);
            }
        }

        return list;
    }
}
