package com.logic.cmd;

import com.base.command.GameCmdAnnotation;
import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;
import com.proto.command.UserCmdType.UserCmdInType;

/**
 * 玩家退出回合
 * 
 * @author dream
 *
 */
@GameCmdAnnotation(type = UserCmdInType.EXIT_BOUT_VALUE)
public class UserExitBoutCmd implements ICommandHandler
{
    @Override
    public void handleCommand(AbstractGame game, Player player, CommonMessage packet)
    {
        try
        {
            player.exitBout();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
