package com.game.user.cmd.fight;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.room.AbstractRoom;
import com.logic.room.FightRoom;
import com.logic.room.RoomStateType;
import com.proto.command.UserCmdType.UserCmdInType;
import com.util.print.LogFactory;

/**
 * 房间开始
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.START_ROOM_VALUE)
public class FightRoomStartCmd extends AbstractUserCmd
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

            if (fightRoom.getAllPlayer().size() < 2)
            {
                player.sendErrorCode(ErrorCodeType.Room_Count_Error, "人数不足,无法开始");
                return;
            }

            if (fightRoom.getRoomStateType() == RoomStateType.Using)
            {
                player.sendErrorCode(ErrorCodeType.Room_Not_Ready, "房间未准备。");
                return;
            }

            if (fightRoom.getRoomStateType() == RoomStateType.Playing)
            {
                player.sendErrorCode(ErrorCodeType.Room_Is_Ready, "房间已经开始");
                return;
            }

            fightRoom.start();
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
