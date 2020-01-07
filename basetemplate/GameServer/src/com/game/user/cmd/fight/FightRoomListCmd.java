package com.game.user.cmd.fight;

import java.util.List;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.component.FightRoomComponent;
import com.logic.room.FightRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserInMsg.GetRoomListProtoIn;
import com.proto.user.gen.UserOutMsg.RoomInfoProtoOut;
import com.proto.user.gen.UserOutMsg.RoomListInfoProtoOut;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 房间列表
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.GET_ROOM_LIST_VALUE)
public class FightRoomListCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            GetRoomListProtoIn proto = GetRoomListProtoIn.parseFrom(packet.getBody());
            String roomFlag = proto.getRoomIdOrRoomName();
            int count = 0;

            RoomListInfoProtoOut.Builder builder = RoomListInfoProtoOut.newBuilder();
            // 为空--返回全部列表
            if (roomFlag == null || roomFlag.isEmpty())
            {
                List<FightRoom> list = FightRoomComponent.getAllRoom();
                for (FightRoom room : list)
                {
                    RoomInfoProtoOut.Builder detail = RoomInfoProtoOut.newBuilder();
                    detail.setRoomID(room.getRoomId());
                    detail.setRoomName(room.getRoomName());
                    detail.setLength(room.getMissionType());

                    builder.addRoomInfoList(detail);
                }
                count = list.size();
            }
            else
            {
                FightRoom room = null;
                if (StringUtil.isNumber(roomFlag))
                {
                    int roomID = Integer.valueOf(roomFlag);
                    room = FightRoomComponent.getRoomByID(roomID);
                }
                else
                {
                    room = FightRoomComponent.getRoomByName(roomFlag);
                }

                if (room != null)
                {
                    RoomInfoProtoOut.Builder detail = RoomInfoProtoOut.newBuilder();
                    detail.setRoomID(room.getRoomId());
                    detail.setRoomName(room.getRoomName());
                    detail.setLength(room.getMissionType());

                    builder.addRoomInfoList(detail);
                    count = 1;
                }
            }
            builder.setRoomCount(count);

            player.sendMessage(UserCmdOutType.GET_ROOM_LIST_RETURN_VALUE, builder);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
