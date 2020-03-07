package com.game.user.cmd.common;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bag.ItemRemoveType;
import com.data.bean.MapBean;
import com.data.bean.factory.MapBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.PlatformTheme;
import com.proto.user.gen.UserInMsg.UnlockPlatformProtoIn;
import com.util.print.LogFactory;

/**
 * 地块主体解锁
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.UNLOCKPLATFORM_VALUE)
public class UnlockThemeCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            UnlockPlatformProtoIn proto = UnlockPlatformProtoIn.parseFrom(packet.getBody());
            PlatformTheme type = proto.getPlatformTheme();

            MapBean bean = MapBeanFactory.getMapBean(type.getNumber());
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "解锁关卡配置无效");
                return;
            }

            if (!player.checkResource(bean.getItemID(), bean.getCount()))
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(bean.getItemID(), bean.getCount(), ItemRemoveType.UNLOCKPLATFORM);

            player.addThemeType(type.getNumber());

            player.getSenderModule().sendRes();

            player.sendMessage(UserCmdOutType.UNLOCK_SUCCESS_VALUE, null);
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
