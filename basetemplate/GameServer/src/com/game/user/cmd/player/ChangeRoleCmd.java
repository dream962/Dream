package com.game.user.cmd.player;

import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.ChangeRoleTypeProtoIn;
import com.proto.user.gen.UserOutMsg.ChangeRoleTypeProtoOut;
import com.util.print.LogFactory;

/**
 * 修改角色类型
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.CHANGE_ROLE_TYPE_VALUE)
public class ChangeRoleCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            ChangeRoleTypeProtoIn proto = ChangeRoleTypeProtoIn.parseFrom(packet.getBody());
            RoleType type = proto.getRoleType();

            ChangeRoleTypeProtoOut.Builder builder = ChangeRoleTypeProtoOut.newBuilder();
            builder.setUserID(player.getUserID());
            builder.setRoleType(type);

            player.getRoomModule().setRoleType(type);

            if (player.getRoomModule().getCurrentRoom() != null)
            {
                player.getRoomModule().getCurrentRoom().sendToAll(builder, UserCmdOutType.CHANGE_ROLETYPE_RETURN_VALUE);
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
