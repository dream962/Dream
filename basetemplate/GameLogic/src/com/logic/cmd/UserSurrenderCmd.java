package com.logic.cmd;

import com.base.command.GameCmdAnnotation;
import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;
import com.proto.command.UserCmdType.UserCmdInType;

/**
 * 玩家投降
 * 
 * @author dream
 *
 */
@GameCmdAnnotation(type = UserCmdInType.USER_SURRENDER_VALUE)
public class UserSurrenderCmd implements ICommandHandler
{
    @Override
    public void handleCommand(AbstractGame game, Player player, CommonMessage packet)
    {
        try
        {
            player.surrender();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
