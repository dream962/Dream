package com.game.user.cmd.common;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bean.RoleBean;
import com.data.bean.factory.RoleBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserInMsg.UnlockRoleProtoIn;
import com.util.print.LogFactory;

/**
 * 角色解锁
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.UNLOCKROLE_VALUE)
public class UnlockRoleCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            UnlockRoleProtoIn proto = UnlockRoleProtoIn.parseFrom(packet.getBody());
            RoleType type = proto.getRoleType();

            RoleBean bean = RoleBeanFactory.getRoleBean(type.getNumber());
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "解锁角色配置无效");
                return;
            }

            if (!player.checkResource(bean.getItemID(), bean.getCount()))
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(bean.getItemID(), bean.getCount());

            player.addRoleType(type.getNumber());

            player.getSenderModule().sendRes();

            player.sendMessage(UserCmdOutType.UNLOCK_SUCCESS_VALUE, null);
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
