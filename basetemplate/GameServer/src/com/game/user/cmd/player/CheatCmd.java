package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.CheatAddItemProtoIn;
import com.util.print.LogFactory;

/**
 * 作弊添加物品
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.CHEAT_ADD_ITEM_VALUE)
public class CheatCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            CheatAddItemProtoIn proto = CheatAddItemProtoIn.parseFrom(packet.getBody());
            int itemID = proto.getItemID();
            int itemCount = proto.getItemCount();

            player.addResource(itemID, itemCount);

            player.getSenderModule().sendRes();
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
