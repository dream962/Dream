package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.RoomComponent;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.RandomMatchProtoIn;
import com.util.print.LogFactory;

@ICode(code = UserCmdInType.RANDOMMATCH_VALUE)
public class MatchBeginCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            if (player.getRoomModule().getCurrentRoom() != null)
            {
                player.getRoomModule().exitRoom(1);
            }

            RandomMatchProtoIn proto = RandomMatchProtoIn.parseFrom(packet.getBody());
            int length = proto.getPlatformCount();
            RoleType roleType = proto.getRoleType();
            player.getRoomModule().setRoleType(roleType);

            RoomComponent.addMatchPlayer(length, player);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
