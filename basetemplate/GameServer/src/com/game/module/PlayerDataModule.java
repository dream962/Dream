package com.game.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.base.code.ErrorCodeType;
import com.data.bean.ChargeBean;
import com.data.bean.factory.ChargeBeanFactory;
import com.data.business.PlayerBusiness;
import com.data.info.ChargeInfo;
import com.data.info.FriendInfo;
import com.data.info.RankInfo;
import com.data.type.ResourceType;
import com.game.component.ChargeComponent;
import com.game.component.RankComponent;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RankTimeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.proto.user.gen.UserOutMsg.RechargeProtoOut;
import com.proto.user.gen.UserOutMsg.RechargeVerifyProtoOut;
import com.util.TimeUtil;
import com.util.UuidUtil;

/**
 * 玩家数据模块
 * 
 * @author dream
 *
 */
public class PlayerDataModule extends AbstractPlayerModule<GamePlayer>
{
    /** 玩家的排行信息<时间类型-排行类型,数据> */
    private Map<String, RankInfo> rankInfoMap = new ConcurrentHashMap<String, RankInfo>();

    private Map<Integer, FriendInfo> friendMap = new ConcurrentHashMap<>();

    public PlayerDataModule(GamePlayer player)
    {
        super(player);
    }

    public boolean load()
    {
        List<RankInfo> list = PlayerBusiness.queryPlayerRank(player.getUserID());
        list.forEach(p -> {
            rankInfoMap.put(p.getTimeType() + "-" + p.getRankType(), p);
        });

        return true;
    }

    public boolean relogin()
    {
        // 不是同一天登录,刷新
        if (!TimeUtil.isSameDay(new Date(), player.getPlayerInfo().getLastLoginTime()))
            refresh(false);

        return true;
    }

    public void refresh(boolean isSend)
    {
        player.getPlayerInfo().setIsCanUnlockCoinModeByAD(true);
        player.getPlayerInfo().setLastAdTime(null);
        player.getPlayerInfo().setAdTriggerCount(0);
        if (isSend)
            player.getSenderModule().sendRes();
    }

    /**
     * 周,月刷新排行数据
     * 
     * @param timeType
     */
    public void refreshRank(RankTimeType timeType)
    {
        RankType[] rankTypes = RankType.values();
        for (RankType rankType : rankTypes)
        {
            RankInfo info = rankInfoMap.get(timeType.getNumber() + "-" + rankType.getNumber());
            if (info != null)
            {
                info.setRankValue(0);
            }
        }

        save();
    }

    public void send()
    {

    }

    public boolean save()
    {
        List<RankInfo> list = new ArrayList<RankInfo>();
        list.addAll(rankInfoMap.values());

        for (RankInfo info : list)
        {
            if (info.isChanged())
                PlayerBusiness.addOrUpdateRank(info);
        }
        return true;
    }

    public void addRank(RankType rankType, int value)
    {
        if (value <= 0)
            return;

        RankTimeType[] types = RankTimeType.values();
        for (RankTimeType timeType : types)
        {
            RankInfo info = rankInfoMap.get(timeType.getNumber() + "-" + rankType.getNumber());
            if (info == null)
            {
                info = new RankInfo();
                info.setUserID(player.getUserID());
                info.setRankType(rankType.getNumber());

                if (rankType == RankType.GoodPersion || rankType == RankType.Net)
                {
                    info.setRankValue(value + info.getRankValue());
                }
                else
                    info.setRankValue(value);

                info.setHeader(player.getPlayerInfo().getHeaderID());
                info.setNickName(player.getNickName());
                info.setTimeType(timeType.getNumber());
                info.setUpdateTime(new Date());

                rankInfoMap.put(timeType.getNumber() + "-" + rankType.getNumber(), info);

                RankComponent.addRank(info);
            }
            else
            {
                if (info.getRankType() == RankType.RACE_VALUE)
                {
                    if (value < info.getRankValue() || info.getRankValue() > 0)
                    {
                        info.setRankValue(value);
                        info.setHeader(player.getPlayerInfo().getHeaderID());
                        info.setNickName(player.getNickName());

                        RankComponent.addRank(info);
                    }
                }
                else if (info.getRankType() == RankType.GoodPersion_VALUE || info.getRankType() == RankType.Net_VALUE)
                {
                    info.setRankValue(value + info.getRankValue());
                    info.setHeader(player.getPlayerInfo().getHeaderID());
                    info.setNickName(player.getNickName());

                    RankComponent.addRank(info);
                }
                else
                {
                    if (value > info.getRankValue())
                    {
                        info.setRankValue(value);
                        info.setHeader(player.getPlayerInfo().getHeaderID());
                        info.setNickName(player.getNickName());

                        RankComponent.addRank(info);
                    }
                }
            }
        }
    }

    public Map<Integer, FriendInfo> getAllFriends()
    {
        return friendMap;
    }

    public void changeHeader(int header)
    {
        List<RankInfo> list = new ArrayList<RankInfo>();
        list.addAll(rankInfoMap.values());

        for (RankInfo info : list)
        {
            info.setHeader(header);
        }
    }

    public void charge(int configID)
    {
        ChargeBean bean = ChargeBeanFactory.getChargeBean(configID);
        if (bean == null)
        {
            player.sendErrorCode(ErrorCodeType.Config_Error, "配置错误.");
            return;
        }

        String sku = bean.getGoogleSKU();
        String orderID = UuidUtil.generateGUID();

        ChargeInfo chargeInfo = new ChargeInfo();
        chargeInfo.setAccountName(player.getPlayerInfo().getAccountName());
        chargeInfo.setConfigID(configID);
        chargeInfo.setCreateTime(new Date());
        chargeInfo.setOrderId(orderID);
        chargeInfo.setOrderStatus(1);
        chargeInfo.setPayMoney(bean.getChargeValue());
        chargeInfo.setPayTime(new Date());
        chargeInfo.setUserID(player.getUserID());
        chargeInfo.setPayWay("google");

        ChargeComponent.addChargeInfo(chargeInfo);

        RechargeProtoOut.Builder builder = RechargeProtoOut.newBuilder();
        builder.setOrderNum(orderID);
        builder.setProductID(sku);
        player.sendMessage(UserCmdOutType.RECHARGE_RESULT_VALUE, builder);
    }

    public void chargeCheck(String orderID, String purchaseToken)
    {
        ChargeInfo chargeInfo = ChargeComponent.getChargeInfo(orderID);
        if (chargeInfo == null)
        {
            chargeInfo = PlayerBusiness.getChargeInfo(player.getUserID(), orderID);
            ChargeComponent.addChargeInfo(chargeInfo);
        }

        if (chargeInfo == null)
        {
            player.sendErrorCode(ErrorCodeType.Charge_Not_Exist, "");
            return;
        }

        // 已经完成,或者取消
        if (chargeInfo.getOrderStatus() != 1)
        {
            player.sendErrorCode(ErrorCodeType.Charge_Order_Finish, "");
            return;
        }

        ChargeBean bean = ChargeBeanFactory.getChargeBean(chargeInfo.getConfigID());
        if (bean == null)
        {
            player.sendErrorCode(ErrorCodeType.Config_Error, "配置错误.");
            return;
        }

        chargeInfo.setPurchaseToken(purchaseToken);

        int code = 1;
        int result = ChargeComponent.checkPay(bean.getGoogleSKU(), purchaseToken);
        if (result == 0)
        {
            code = 0;
            // 完成
            chargeInfo.setOrderStatus(2);

            // 添加物品
            int ttq = bean.getChargeIngot();
            player.addResource(ResourceType.TTQ.getValue(), ttq);

            if (bean.getChargeRoleID() > 0)
            {
                player.addRoleType(bean.getChargeRoleID());
            }

            player.getSenderModule().sendRes();
        }
        else if (result == 1)
        {
            // 取消
            chargeInfo.setOrderStatus(0);
            player.sendErrorCode(ErrorCodeType.Charge_Order_Check, "");
        }
        else
        {
            player.sendErrorCode(ErrorCodeType.Charge_Order_Check, "");
        }

        // 返回
        RechargeVerifyProtoOut.Builder builder = RechargeVerifyProtoOut.newBuilder();
        builder.setCode(code);
        builder.setRechargeConfigID(chargeInfo.getConfigID());

        player.sendMessage(UserCmdOutType.RECHARGE_VERIFY_RETURN_VALUE, builder);
    }
}
