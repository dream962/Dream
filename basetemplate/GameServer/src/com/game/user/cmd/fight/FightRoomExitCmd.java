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
import com.util.print.LogFactory;

/**
 * 离开房间
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.EXIT_ROOM_VALUE)
public class FightRoomExitCmd extends AbstractUserCmd
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
            List<AbstractGamePlayer> players = fightRoom.getAllPlayer();
            fightRoom.removePlayer(player, 0);
            fightRoom.setRoomStateType(RoomStateType.Using);

            for (AbstractGamePlayer p : players)
                p.sendMessage(UserCmdOutType.EXIT_ROOM_RETURN_VALUE, null);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
