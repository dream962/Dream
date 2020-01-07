package com.logic.cmd;

import com.base.command.GameCmdAnnotation;
import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.PlayerBeKillProtoIn;

/**
 * 玩家死亡
 * 
 * @author dream
 *
 */
@GameCmdAnnotation(type = UserCmdInType.USER_KILL_VALUE)
public class UserKillCmd implements ICommandHandler
{
    @Override
    public void handleCommand(AbstractGame game, Player player, CommonMessage packet)
    {
        try
        {
            PlayerBeKillProtoIn proto = PlayerBeKillProtoIn.parseFrom(packet.getBody());
            int platformIndex = proto.getPlatformIndex();

            player.jumpError(platformIndex);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
