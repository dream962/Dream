package com.game.user.cmd.common;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.RechargeProtoIn;
import com.util.print.LogFactory;

/**
 * 充值
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.RECHARGE_VALUE)
public class RechargeCreateCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RechargeProtoIn proto = RechargeProtoIn.parseFrom(packet.getBody());
            int configID = proto.getRechargeConfigID();
            player.getDataModule().charge(configID);
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
