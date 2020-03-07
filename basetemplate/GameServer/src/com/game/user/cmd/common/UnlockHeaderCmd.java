package com.game.user.cmd.common;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bag.ItemRemoveType;
import com.data.bean.HeaderBean;
import com.data.bean.factory.HeaderBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.user.gen.UserInMsg.HeadIDProtoIn;
import com.util.print.LogFactory;

/**
 * 头像Header解锁
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.UNLOCKHEADID_VALUE)
public class UnlockHeaderCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            HeadIDProtoIn proto = HeadIDProtoIn.parseFrom(packet.getBody());
            int headerID = proto.getHeadID();

            HeaderBean bean = HeaderBeanFactory.getHeaderBean(headerID);
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "解锁配置无效");
                return;
            }

            if (!player.checkResource(bean.getConsumeItemID(), bean.getConsumeItemCount()))
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(bean.getConsumeItemID(), bean.getConsumeItemCount(), ItemRemoveType.UNLOCKHEADID);

            player.addHeader(headerID);

            player.getSenderModule().sendRes();
        }
        catch (Exception e)
        {
            LogFactory.error("异常", e);
        }
    }
}
