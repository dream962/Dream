package com.game.user.cmd.player;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.ConsumeItemReviveProtoIn;
import com.util.print.LogFactory;

/**
 * 复活
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.CONSUME_ITEM_REVIVE_VALUE)
public class ReliveCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            ConsumeItemReviveProtoIn proto = ConsumeItemReviveProtoIn.parseFrom(packet.getBody());
            int count = proto.getItemCount();
            int itemID = proto.getItemID();

            if (!player.checkResource(itemID, count))
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(itemID, count);

            player.getSenderModule().sendRes();
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
