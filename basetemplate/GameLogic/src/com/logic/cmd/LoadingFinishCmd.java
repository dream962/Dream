package com.logic.cmd;

import com.base.command.GameCmdAnnotation;
import com.base.net.CommonMessage;
import com.logic.game.AbstractGame;
import com.logic.living.Player;
import com.proto.command.UserCmdType.UserCmdInType;

/**
 * 游戏开始
 * 
 * @author dream
 *
 */
@GameCmdAnnotation(type = UserCmdInType.LOAD_FINISHI_VALUE)
public class LoadingFinishCmd implements ICommandHandler
{
    @Override
    public void handleCommand(AbstractGame game, Player player, CommonMessage packet)
    {
        try
        {
            player.setProgress(100);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
