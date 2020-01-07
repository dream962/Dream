package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.BindAccountProtoIn;
import com.util.print.LogFactory;

/**
 * 账号绑定
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.BIND_ACCOUNT_VALUE)
public class AccountBindCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            BindAccountProtoIn proto = BindAccountProtoIn.parseFrom(packet.getBody());
            String name = proto.getName();
            String machineCode = proto.getMachinecode();
            String gName = proto.getGoogleName();
            String openID = proto.getOpenID();

        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
