package com.game.user.cmd.common;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.user.gen.UserInMsg.GameBeginProtoIn;
import com.util.print.LogFactory;

/**
 * 单机游戏开始
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.USER_GAME_BEGIN_VALUE)
public class GameBeginCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            GameBeginProtoIn proto = GameBeginProtoIn.parseFrom(packet.getBody());
            ModeType type = proto.getGameType();

            player.getDataModule().beginGame(type);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
