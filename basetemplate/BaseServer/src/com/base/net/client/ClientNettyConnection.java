package com.base.net.client;

import java.net.InetSocketAddress;

import com.base.type.CommonConst;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 基于Netty的IClientConnection实现。
 * 
 */
public class ClientNettyConnection extends AbstractClientConnection
{
    protected Channel channel = null;

    /** 连接的平台ID */
    protected int platformID;
    /** 连接的区域 */
    protected int areaID;
    /** 连接的频道ID */
    protected int channelID;

    public ClientNettyConnection(AbstractClientPacketHandler packetHandler, Channel session)
    {
        super(packetHandler);
        this.channel = session;
    }

    @Override
    public void send(Object packet)
    {
        if (channel != null && channel.isWritable())
        {
            channel.writeAndFlush(packet);
        }
    }

    @Override
    public void setKeys(int[] encryptKeys, int[] decryptKeys)
    {
        AttributeKey<int[]> val1 = AttributeKey.valueOf(CommonConst.ENCRYPTION_KEY);
        channel.attr(val1).set(encryptKeys);

        AttributeKey<int[]> val2 = AttributeKey.valueOf(CommonConst.DECRYPTION_KEY);
        channel.attr(val2).set(decryptKeys);
    }

    @Override
    public boolean isConn()
    {
        if (channel != null && channel.isActive())
            return true;

        return false;
    }

    @Override
    public void onDisconnect()
    {
        super.onDisconnect();
        if (channel != null)
        {
            channel.close();
            channel = null;
        }
    }

    @Override
    public void closeConnection(boolean immediately)
    {
        if (this.channel != null && channel.isOpen())
        {
            channel.close();
        }
    }

    @Override
    public String getClientIP()
    {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public int getPlatformID()
    {
        return platformID;
    }

    @Override
    public int getAreaID()
    {
        return areaID;
    }

    @Override
    public void setPlatformID(int id)
    {
        this.platformID = id;
    }

    @Override
    public void setAreaID(int id)
    {
        this.areaID = id;
    }

    public int getChannelID()
    {
        return channelID;
    }

    public void setChannelID(int channelID)
    {
        this.channelID = channelID;
    }

}
