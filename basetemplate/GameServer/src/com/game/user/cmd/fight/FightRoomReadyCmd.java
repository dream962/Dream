package com.game.user.cmd.fight;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.logic.room.FightRoom;
import com.logic.room.RoomStateType;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.RoomerReadyStatusProtoOut;
import com.util.print.LogFactory;

/**
 * 房间准备
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.READY_ROOM_VALUE)
public class FightRoomReadyCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            AbstractRoom room = player.getRoomModule().getCurrentRoom();
            if (room == null || !(room instanceof FightRoom))
            {
                player.sendErrorCode(ErrorCodeType.Room_Destroy, "房间已经不存在。");
                return;
            }

            FightRoom fightRoom = (FightRoom) room;
            RoomStateType stateType = fightRoom.getRoomStateType();

            if (stateType == RoomStateType.Playing)
            {
                player.sendErrorCode(ErrorCodeType.Room_Is_Ready, "房间已经开始。");
                return;
            }

            if (stateType == RoomStateType.Using)
                fightRoom.setRoomStateType(RoomStateType.Ready);

            if (stateType == RoomStateType.Ready)
                fightRoom.setRoomStateType(RoomStateType.Using);

            RoomerReadyStatusProtoOut.Builder builder = RoomerReadyStatusProtoOut.newBuilder();
            stateType = fightRoom.getRoomStateType();
            if (stateType == RoomStateType.Using)
                builder.setRoomerStatus(1);
            if (stateType == RoomStateType.Ready)
                builder.setRoomerStatus(2);

            List<AbstractGamePlayer> players = fightRoom.getAllPlayer();
            for (AbstractGamePlayer p : players)
                p.sendMessage(UserCmdOutType.READY_ROOM_RETURN_VALUE, builder);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
