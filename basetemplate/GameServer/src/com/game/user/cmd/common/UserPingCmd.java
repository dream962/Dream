package com.game.user.cmd.common;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.module.PlayerPingModule;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.PingProtoIn;
import com.util.print.LogFactory;

@ICode(code = UserCmdInType.USER_PING_VALUE)
public class UserPingCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            long ct = System.currentTimeMillis();
            PingProtoIn proto = PingProtoIn.parseFrom(packet.getBody());
            // 延迟：玩家登陆时服务器会发一个ping给客户端，客户端再ping服务器，这个回环中记录了起始时间
            int latency = (int) (ct - proto.getServerTime()) / 2;
            // ping处理的所有延时：RTT/2+一帧的时间
            int delay = latency;
            PlayerPingModule module = player.getPingModule();
            module.setLatency(delay);
            module.setPingTime(ct);
        }
        catch (Exception e)
        {
            LogFactory.error("用户ping异常", e);
        }
    }
}
