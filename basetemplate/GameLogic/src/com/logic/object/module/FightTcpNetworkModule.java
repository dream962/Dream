package com.logic.object.module;

import com.base.net.CommonMessage;
import com.base.net.client.IClientConnection;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.object.AbstractGamePlayer;

/**
 * 战斗服内网连接模块
 * 
 * @author dream
 *
 */
public class FightTcpNetworkModule extends AbstractPlayerModule<AbstractGamePlayer>
{
    /** TCP客户端 */
    private IClientConnection tcpClient;

    public FightTcpNetworkModule(AbstractGamePlayer player)
    {
        super(player);
    }

    public void setTcpClient(IClientConnection kcpClient)
    {
        this.tcpClient = kcpClient;
    }

    public IClientConnection getTcpClient()
    {
        return this.tcpClient;
    }

    /**
     * 发送tcp消息
     * 
     * @param code
     * @param builder
     */
    public void sendFightMessage(int code, Builder<?> builder)
    {
        if (tcpClient != null)
        {
            CommonMessage packet = new CommonMessage(code);
            packet.setParam(player.getUserID());
            if (builder != null)
                packet.setBody(builder.build().toByteArray());

            tcpClient.send(packet);
        }
    }
}
