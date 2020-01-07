package com.game.user.cmd.player;

import java.util.Date;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bean.AdRewardBean;
import com.data.bean.factory.AdRewardBeanFactory;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.RewardADProtoOut;
import com.util.print.LogFactory;

/**
 * 触发广告
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.TRIGGER_REWARD_AD_VALUE)
public class AdTriggerCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            if (player.getPlayerInfo().getAdTriggerCount() == 0)
            {
                player.getPlayerInfo().setAdTriggerCount(1);
                player.getPlayerInfo().setLastAdTime(new Date());

                AdRewardBean bean = AdRewardBeanFactory.getAdRewardBean(player.getPlayerInfo().getAdTriggerCount());

                RewardADProtoOut.Builder builder = RewardADProtoOut.newBuilder();
                int leftTime = bean.getDurationCount() * 60;
                builder.setTriggerADLeftTime(leftTime);
                builder.setTodayTriggerADCount(player.getPlayerInfo().getAdTriggerCount());

                player.sendMessage(UserCmdOutType.REWARD_AD_RETURN_VALUE, builder);
                return;
            }

            int count = player.getPlayerInfo().getAdTriggerCount() + 1;
            AdRewardBean bean = AdRewardBeanFactory.getAdRewardBean(count);
            if (bean == null)
            {
                player.sendErrorCode(ErrorCodeType.Config_Error, "广告次数配置无效");
                return;
            }

            player.getPlayerInfo().setAdTriggerCount(count);
            player.getPlayerInfo().setLastAdTime(new Date());

            player.addResource(bean.getRewardItemID(), bean.getRewardItemCount());

            RewardADProtoOut.Builder builder = RewardADProtoOut.newBuilder();

            int leftTime = bean.getDurationCount() * 60;
            builder.setTriggerADLeftTime(leftTime);
            builder.setTodayTriggerADCount(player.getPlayerInfo().getAdTriggerCount());

            player.sendMessage(UserCmdOutType.REWARD_AD_RETURN_VALUE, builder);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
