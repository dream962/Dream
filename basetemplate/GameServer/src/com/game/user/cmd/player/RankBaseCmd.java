package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.component.RankComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.util.print.LogFactory;

/**
 * 请求基础排行数据
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.USER_LEADERBOARD_BASE_VALUE)
public class RankBaseCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RankComponent.sendBaseRank(player);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
