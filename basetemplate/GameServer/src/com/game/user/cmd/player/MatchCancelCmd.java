package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.RoomComponent;
import com.proto.command.UserCmdType.UserCmdInType;
import com.util.print.LogFactory;

@ICode(code = UserCmdInType.MATCHCANCEL_VALUE)
public class MatchCancelCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RoomComponent.removeMatchPlayer(player);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
