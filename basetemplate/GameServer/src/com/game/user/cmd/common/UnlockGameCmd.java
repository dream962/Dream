package com.game.user.cmd.common;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bag.ItemRemoveType;
import com.data.bean.UnlockBean;
import com.data.bean.factory.UnlockBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.user.gen.UserInMsg.GameUnlockProtoIn;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 关卡解锁
 *
 * @author dream
 *
 */
@ICode(code = UserCmdInType.GAME_UNLOCK_VALUE)
public class UnlockGameCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            GameUnlockProtoIn proto = GameUnlockProtoIn.parseFrom(packet.getBody());
            ModeType type = proto.getGameMode();
            boolean unlockByAD = proto.getUnLockByAD();

            UnlockBean bean = UnlockBeanFactory.getUnlockBean(type.getNumber());
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "解锁关卡配置无效");
                return;
            }

            // 非金币模式
            if (type != ModeType.Coin)
            {
                if (!player.checkResource(bean.getItemID(), bean.getItemCount()))
                {
                    player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                    return;
                }

                player.removeResource(bean.getItemID(), bean.getItemCount(), ItemRemoveType.GAME_UNLOCK);
            }
            else
            {
                // 金币模式可以通过广告解锁,false非广告
                if (unlockByAD == false)
                {
                    if (!player.checkResource(bean.getItemID(), bean.getItemCount()))
                    {
                        player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                        return;
                    }

                    player.removeResource(bean.getItemID(), bean.getItemCount(), ItemRemoveType.GAME_UNLOCK);
                }
            }

            player.addGameModeType(type.getNumber());
            String attachs = bean.getAttachModes();
            if (attachs != null && !attachs.isEmpty())
            {
                List<Integer> list = StringUtil.splitIntToList(attachs, "\\,");
                list.forEach(p -> {
                    player.addGameModeType(p);
                });
            }

            player.getSenderModule().sendRes();
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
