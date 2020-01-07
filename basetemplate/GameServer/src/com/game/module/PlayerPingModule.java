package com.game.module;

import com.data.component.GamePropertiesComponent;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.PingProtoOut;

/**
 * ping模块
 * 
 * @author dream
 *
 */
public class PlayerPingModule extends AbstractPlayerModule<GamePlayer>
{
    /** 延迟时间 */
    private int latency;

    /** 返回的ping的时间 */
    private long pingTime;

    public void setPingTime(long time)
    {
        this.pingTime = time;
    }

    public int getLatency()
    {
        return latency;
    }

    public void setLatency(int latency)
    {
        this.latency = latency;
    }

    public PlayerPingModule(GamePlayer player)
    {
        super(player);
    }

    /**
     * 发送ping
     */
    public void sendPing()
    {
        PingProtoOut.Builder builder = PingProtoOut.newBuilder();
        builder.setServerTime(System.currentTimeMillis());

        player.sendMessage(UserCmdOutType.PING_RESULT_VALUE, builder);
    }

    /**
     * 定时ping逻辑
     */
    public void ping()
    {
        sendPing();

        if (System.currentTimeMillis() - pingTime > GamePropertiesComponent.PING_MAX_TIME * 1000)
        {
            if (player.getNetworkModule().isConnect() == false)
            {
                player.disconnect(player.getNetworkModule().getClientConnection(), true);
            }
        }
    }

    @Override
    public void send()
    {
        sendPing();
        setPingTime(System.currentTimeMillis());
    }
}
