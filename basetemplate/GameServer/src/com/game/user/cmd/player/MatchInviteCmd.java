package com.game.user.cmd.player;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.RoomComponent;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.GetInviteAgainProtoOut;
import com.util.print.LogFactory;

/**
 * 邀请再开一局
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.INVITE_AGAIN_VALUE)
public class MatchInviteCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            AbstractRoom room = player.getRoomModule().getCurrentRoom();
            if (room == null)
            {
                player.sendErrorCode(ErrorCodeType.Room_Not_Exist, "玩家不在房间内.");
                return;
            }

            AbstractGamePlayer player2 = null;
            List<AbstractGamePlayer> players = room.getAllPlayer();
            for (AbstractGamePlayer p : players)
            {
                if (p.getUserID() != player.getUserID())
                {
                    player2 = p;
                    break;
                }
            }

            if (player2 != null && player2.getNetworkModule().isConnect())
            {
                // 如果对方机器人,邀请就再开一局
                if (player2.getIsRobot())
                {
                    boolean result = RoomComponent.startRoom(player.getRoomModule().getCurrentRoom());
                    if (result == false)
                    {
                        for (AbstractGamePlayer p1 : players)
                        {
                            p1.sendErrorCode(ErrorCodeType.Room_Start_Error, "房间开始异常.");
                            LogFactory.error("房间再来一局,开始异常.");
                            p1.getRoomModule().exitRoom(1);
                        }
                    }
                }
                else
                {
                    GetInviteAgainProtoOut.Builder builder = GetInviteAgainProtoOut.newBuilder();
                    builder.setEnemyName(player.getNickName());
                    player2.sendMessage(UserCmdOutType.GET_INVITE_AGAIN_VALUE, builder);
                }
            }
            else
            {
                player.sendErrorCode(ErrorCodeType.Room_Count_Error, "房员已经离开,无法开始");
                return;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
