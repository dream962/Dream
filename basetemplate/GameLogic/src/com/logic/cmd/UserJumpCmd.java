package com.logic.cmd;

import com.base.command.GameCmdAnnotation;
import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.PlayerJumpProtoIn;

/**
 * 玩家上传步数
 * 
 * @author dream
 *
 */
@GameCmdAnnotation(type = UserCmdInType.USER_JUMP_VALUE)
public class UserJumpCmd implements ICommandHandler
{
    @Override
    public void handleCommand(AbstractGame game, Player player, CommonMessage packet)
    {
        try
        {
            PlayerJumpProtoIn proto = PlayerJumpProtoIn.parseFrom(packet.getBody());
            boolean isLeftJump = proto.getIsLeftJump();
            int index = proto.getJumpIndex();
            float time = proto.getMultiple();

            player.jump(isLeftJump, index, time);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
