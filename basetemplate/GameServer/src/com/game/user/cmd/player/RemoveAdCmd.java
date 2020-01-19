package com.game.user.cmd.player;

import java.util.Calendar;
import java.util.Date;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bean.AdvertiseBean;
import com.data.bean.factory.AdvertiseBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserInMsg.RemoveAdProtoIn;
import com.proto.user.gen.UserOutMsg.RemoveAdProtoOut;
import com.util.TimeUtil;
import com.util.print.LogFactory;

/**
 * 移除广告
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.REMOVE_AD_VALUE)
public class RemoveAdCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            RemoveAdProtoIn proto = RemoveAdProtoIn.parseFrom(packet.getBody());
            int configID = proto.getRemoveAdConfigID();

            AdvertiseBean bean = AdvertiseBeanFactory.getAdvertiseBean(configID);
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "配置无效");
                return;
            }

            if (player.checkResource(bean.getConsumeID(), bean.getConsumeCount()) == false)
            {
                player.sendErrorCode(ErrorCodeType.Not_Enough_Resource, "资源不足.");
                return;
            }

            player.removeResource(bean.getConsumeID(), bean.getConsumeCount());

            Date expireTime = player.getPlayerInfo().getAdExpireTime();
            if (expireTime == null || expireTime.getTime() < new Date().getTime())
            {
                player.getPlayerInfo().setBuyAdTime(new Date());
                expireTime = TimeUtil.getDateByDay(new Date());
                expireTime = TimeUtil.addOrRemoveDate(expireTime, Calendar.MONTH, bean.getMonthCount());
            }
            else if (expireTime.getTime() > new Date().getTime())
            {
                expireTime = TimeUtil.addOrRemoveDate(expireTime, Calendar.MONTH, bean.getMonthCount());
            }

            player.getPlayerInfo().setAdExpireTime(expireTime);

            player.getSenderModule().sendRes();

            RemoveAdProtoOut.Builder builder = RemoveAdProtoOut.newBuilder();
            builder.setRemoveAdConfigID(configID);
            player.sendMessage(UserCmdOutType.REMOVE_AD_RETURN_VALUE, builder);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
