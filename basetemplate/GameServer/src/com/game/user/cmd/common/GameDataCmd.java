package com.game.user.cmd.common;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.room.AbstractRoom;
import com.proto.command.UserCmdType.GameCmdType;

/***
 * 游戏二级协议**@author dream.wang
 */
@ICode(code = GameCmdType.TWO_MAX_VALUE, desc = "二级协议处理")
public class GameDataCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        AbstractRoom room = player.getRoomModule().getCurrentRoom();
        if (room == null)
            return;

        packet.setParam(player.getUserID());
        room.processGameData(packet);
    }
}
