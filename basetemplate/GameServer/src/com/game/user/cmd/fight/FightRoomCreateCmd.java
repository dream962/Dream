package com.game.user.cmd.fight;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.FightRoomComponent;
import com.logic.room.FightRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.CreateRoomProtoIn;
import com.proto.user.gen.UserOutMsg.RoomInfoProtoOut;
import com.util.print.LogFactory;

/**
 * 创建房间
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.CREATE_ROOM_VALUE)
public class FightRoomCreateCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            CreateRoomProtoIn proto = CreateRoomProtoIn.parseFrom(packet.getBody());
            String roomName = proto.getRoomName();
            int length = proto.getLength();
            RoleType roleType = proto.getRoleType();

            FightRoom room = FightRoomComponent.getRoomByName(roomName);
            if (room != null)
            {
                player.sendErrorCode(ErrorCodeType.Room_Name_Exist, "房间名字已经存在。");
                return;
            }

            if (player.getRoomModule().getCurrentRoom() != null)
                player.getRoomModule().exitRoom(0);

            FightRoom fightRoom = FightRoomComponent.createRoom(player, roomName, length, roleType);
            if (fightRoom != null)
            {
                RoomInfoProtoOut.Builder builder = RoomInfoProtoOut.newBuilder();
                builder.setLength(length);
                builder.setRoomID(fightRoom.getRoomId());
                builder.setRoomName(fightRoom.getRoomName());

                player.sendMessage(UserCmdOutType.CREATE_ROOM_RETURN_VALUE, builder);
            }
            else
            {
                player.sendErrorCode(ErrorCodeType.Room_Start_Error, "房间创建异常。");
                return;
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
