package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.component.RankComponent;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.RankSectionType;
import com.proto.common.gen.CommonOutMsg.RankTimeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.proto.user.gen.UserInMsg.RankInfoProtoIn;
import com.util.print.LogFactory;

/**
 * 请求排行榜
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.USER_LEADERBOARD_VALUE)
public class RankTypeCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RankInfoProtoIn proto = RankInfoProtoIn.parseFrom(packet.getBody());
            RankType rankType = proto.getRankType();
            RankTimeType timeType = proto.getRankTimeType();
            RankSectionType sectionType = proto.getRankSectionType();

            RankComponent.sendRank(player, rankType, timeType, sectionType);
        }
        catch (Exception e)
        {
            LogFactory.error(" ", e);
        }
    }
}
