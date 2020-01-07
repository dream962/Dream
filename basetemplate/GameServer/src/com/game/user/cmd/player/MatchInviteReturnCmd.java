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
import com.proto.user.gen.UserInMsg.InviteBackProtoIn;
import com.proto.user.gen.UserOutMsg.InviteAgainReturnProtoOut;
import com.util.print.LogFactory;

@ICode(code = UserCmdInType.INVITE_AGAIN_BACK_VALUE)
public class MatchInviteReturnCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            InviteBackProtoIn proto = InviteBackProtoIn.parseFrom(packet.getBody());
            boolean isPermit = proto.getIsPermit();

            AbstractRoom room = player.getRoomModule().getCurrentRoom();
            if (room == null)
            {
                player.sendErrorCode(ErrorCodeType.Room_Not_Exist, "玩家不在房间内.");
                return;
            }

            List<AbstractGamePlayer> players = room.getAllPlayer();
            for (AbstractGamePlayer p : players)
            {
                if (p.getUserID() != player.getUserID())
                {
                    InviteAgainReturnProtoOut.Builder builder = InviteAgainReturnProtoOut.newBuilder();
                    builder.setIsPermit(isPermit);
                    builder.setEnemyName(player.getNickName());
                    p.sendMessage(UserCmdOutType.INVITE_AGAIN_RETURN_VALUE, builder);
                }
            }

            // 如果同意,开始新的一局
            if (isPermit)
            {
                boolean result = RoomComponent.startRoom(player.getRoomModule().getCurrentRoom());
                if (result == false)
                {
                    for (AbstractGamePlayer p : players)
                    {
                        p.sendErrorCode(ErrorCodeType.Room_Start_Error, "房间开始异常.");
                        p.getRoomModule().exitRoom(1);
                    }
                }
            }
            else
            {
                // 退出房间
                if (player.getRoomModule().getCurrentRoom() != null)
                {
                    player.getRoomModule().exitRoom(1);
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
