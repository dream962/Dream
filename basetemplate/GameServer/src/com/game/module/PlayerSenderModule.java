package com.game.module;

import java.util.Date;
import java.util.List;

import com.data.bean.AdRewardBean;
import com.data.bean.factory.AdRewardBeanFactory;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.PlatformTheme;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.proto.user.gen.UserOutMsg.LoginInfoProtoOut;
import com.proto.user.gen.UserOutMsg.ResourceRefresh;

/**
 * 玩家网络数据代理
 * 
 * @author dream.wang
 * 
 */
public class PlayerSenderModule extends AbstractPlayerModule<GamePlayer>
{
    public PlayerSenderModule(GamePlayer player)
    {
        super(player);
    }

    /**
     * 发送登陆成功
     */
    public void sendLoginSuccess()
    {
        LoginInfoProtoOut.Builder builder = LoginInfoProtoOut.newBuilder();
        builder.setUserID(player.getUserID());
        builder.setFailCount(player.getPlayerInfo().getFailCount());
        builder.setWinCount(player.getPlayerInfo().getWinCount());
        builder.setDiamondCount(player.getPlayerInfo().getGold());
        builder.setEndlessTopScore(player.getPlayerInfo().getEndTopScore());
        builder.setHeadID(player.getPlayerInfo().getHeaderID());
        boolean isAd = true;
        if (player.getPlayerInfo().getAdExpireTime() == null || player.getPlayerInfo().getAdExpireTime().getTime() < new Date().getTime())
            isAd = false;
        builder.setIsRemoveAd(isAd);
        builder.setDonutCount(player.getPlayerInfo().getMoney());
        builder.setTimelimitTopScore(player.getPlayerInfo().getTimeTopScore());
        builder.setRaceTopScore(player.getPlayerInfo().getRaceTopScore());
        builder.setTodayTriggerADCount(player.getPlayerInfo().getAdTriggerCount());
        builder.setGlobalTopScore(player.getPlayerInfo().getTopLength());

        AdRewardBean bean = AdRewardBeanFactory.getAdRewardBean(player.getPlayerInfo().getAdTriggerCount());
        if (bean != null)
        {
            int time = (int) ((System.currentTimeMillis() - player.getPlayerInfo().getLastAdTime().getTime()) / 1000);
            int leftTime = bean.getDurationCount() * 60 - time;
            leftTime = leftTime < 0 ? 0 : leftTime;
            builder.setTriggerADLeftTime(leftTime);
        }
        else
        {
            builder.setTriggerADLeftTime(0);
        }

        List<Integer> roles = player.getRoleTypes();
        for (int role : roles)
        {
            if (RoleType.valueOf(role) != null)
                builder.addUnlockRoleType(RoleType.valueOf(role));
        }

        List<Integer> themes = player.getThemeTypes();
        for (int t : themes)
        {
            if (PlatformTheme.valueOf(t) != null)
                builder.addUnlockTheme(PlatformTheme.valueOf(t));
        }

        List<Integer> modes = player.getGameModeTypes();
        for (int m : modes)
        {
            if (ModeType.valueOf(m) != null)
                builder.addUnlockGameMode(ModeType.valueOf(m));
        }

        List<Integer> headers = player.getHeaders();
        for (int m : headers)
            builder.addUnlockHeadID(m);

        builder.setIsCanUnlockCoinModeByAD(player.getPlayerInfo().getIsCanUnlockCoinModeByAD());

        player.sendMessage(UserCmdOutType.USER_LOGIN_RESULT_VALUE, builder);
    }

    public void sendRes()
    {
        ResourceRefresh.Builder builder = ResourceRefresh.newBuilder();
        builder.setFailCount(player.getPlayerInfo().getFailCount());
        builder.setWinCount(player.getPlayerInfo().getWinCount());
        builder.setDiamondCount((int) player.getPlayerInfo().getGold());
        builder.setEndlessTopScore(player.getPlayerInfo().getEndTopScore());
        boolean isAd = true;
        if (player.getPlayerInfo().getAdExpireTime() == null || player.getPlayerInfo().getAdExpireTime().getTime() < new Date().getTime())
            isAd = false;
        builder.setIsRemoveAd(isAd);
        builder.setDonutCount(player.getPlayerInfo().getMoney());
        builder.setTimelimitTopScore(player.getPlayerInfo().getTimeTopScore());
        builder.setRaceTopScore(player.getPlayerInfo().getRaceTopScore());
        builder.setGlobalTopScore(player.getPlayerInfo().getTopLength());

        List<Integer> roles = player.getRoleTypes();
        for (int role : roles)
        {
            if (RoleType.valueOf(role) != null)
                builder.addUnlockRoleType(RoleType.valueOf(role));
        }

        List<Integer> themes = player.getThemeTypes();
        for (int t : themes)
        {
            if (PlatformTheme.valueOf(t) != null)
                builder.addUnlockTheme(PlatformTheme.valueOf(t));
        }

        List<Integer> modes = player.getGameModeTypes();
        for (int m : modes)
        {
            if (ModeType.valueOf(m) != null)
                builder.addUnlockGameMode(ModeType.valueOf(m));
        }

        List<Integer> headers = player.getHeaders();
        for (int m : headers)
            builder.addUnlockHeadID(m);

        builder.setIsCanUnlockCoinModeByAD(player.getPlayerInfo().getIsCanUnlockCoinModeByAD());
        builder.setHeadID(player.getPlayerInfo().getHeaderID());

        player.sendMessage(UserCmdOutType.UPDATE_RESOURCE_VALUE, builder);
    }

    public void sendKickPlayer()
    {
        player.sendMessage(UserCmdOutType.KICK_USER_VALUE, null);
    }
}
