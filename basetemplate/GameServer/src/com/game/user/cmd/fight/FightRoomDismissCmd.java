package com.game.user.cmd.fight;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.room.AbstractRoom;
import com.logic.room.FightRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.util.print.LogFactory;

/**
 * 解散房间
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.DISMISS_ROOM_VALUE)
public class FightRoomDismissCmd extends AbstractUserCmd
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

            if (room instanceof FightRoom)
            {
                FightRoom fightRoom = (FightRoom) room;
                fightRoom.distroy();
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
