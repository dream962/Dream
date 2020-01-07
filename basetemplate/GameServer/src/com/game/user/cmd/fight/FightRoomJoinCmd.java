package com.game.user.cmd.fight;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.FightRoomComponent;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.FightRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.RoomIDProtoIn;
import com.proto.user.gen.UserOutMsg.RoomPlayerInfoProtoOut;
import com.util.print.LogFactory;

/**
 * 加入房间
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.JOIN_ROOM_VALUE)
public class FightRoomJoinCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RoomIDProtoIn proto = RoomIDProtoIn.parseFrom(packet.getBody());
            int roomID = proto.getRoomID();
            RoleType roleType = proto.getRoleType();

            FightRoom fightRoom = FightRoomComponent.getRoomByID(roomID);
            if (fightRoom != null)
            {
                if (fightRoom.getPlayerCount() >= 2)
                {
                    player.sendErrorCode(ErrorCodeType.Room_Count_Max, "房间人数已满。");
                    return;
                }

                // 加入房间
                if (fightRoom.addPlayer(player))
                {
                    player.getRoomModule().setRoleType(roleType);

                    AbstractGamePlayer owner = fightRoom.getOwner();
                    if (owner != null)
                    {
                        RoomPlayerInfoProtoOut.Builder builder = RoomPlayerInfoProtoOut.newBuilder();
                        builder.setRoomOwnerRoleType(owner.getRoomModule().getRoleType());
                        builder.setRoomerRoleType(roleType);
                        builder.setRoomerStatus(1);
                        builder.setRoomID(fightRoom.getRoomId());
                        builder.setLength(fightRoom.getMissionType());
                        builder.setRoomName(fightRoom.getRoomName());

                        List<AbstractGamePlayer> list = fightRoom.getAllPlayer();
                        for (AbstractGamePlayer p : list)
                        {
                            p.sendMessage(UserCmdOutType.ROOM_PLAYER_INFO_RETURN_VALUE, builder);
                        }
                    }
                    else
                    {
                        player.sendErrorCode(ErrorCodeType.Room_Destroy, "房间不存在。");
                    }
                }
                else
                {
                    player.sendErrorCode(ErrorCodeType.Room_Count_Max, "房间人数已满。");
                    return;
                }
            }
            else
            {
                player.sendErrorCode(ErrorCodeType.Room_Destroy, "房间不存在。");
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
