package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.util.print.LogFactory;

/**
 * 广告解锁
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.UNLOCK_COIN_BY_AD_VALUE)
public class UnlockCoinByADCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            player.getPlayerInfo().setIsCanUnlockCoinModeByAD(true);
            player.getSenderModule().sendRes();
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
