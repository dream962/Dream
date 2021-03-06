package com.game.user.cmd.player;

import java.util.List;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.ExitBoutProtoOut;
import com.util.print.LogFactory;

/**
 * 匹配退出
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.EXIT_BOUT_VALUE)
public class MatchExitCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            AbstractRoom room = player.getRoomModule().getCurrentRoom();
            if (room != null)
            {
                player.getRoomModule().exitRoom(0);
                List<AbstractGamePlayer> list = room.getAllPlayer();
                if (list != null)
                {
                    ExitBoutProtoOut.Builder builder = ExitBoutProtoOut.newBuilder();
                    builder.setUserID(player.getUserID());
                    room.sendToAll(builder, UserCmdOutType.EXIT_BOUT_RETURN_VALUE, player);
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
