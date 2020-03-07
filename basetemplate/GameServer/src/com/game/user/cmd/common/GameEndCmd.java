package com.game.user.cmd.common;

import java.util.List;

import com.base.code.ErrorCodeType;
import com.base.command.ICode;
import com.base.net.CommonMessage;
import com.data.bag.ItemAddType;
import com.data.bean.UnlockBean;
import com.data.bean.factory.UnlockBeanFactory;
import com.data.type.ResourceType;
import com.game.object.player.GamePlayer;
import com.game.user.cmd.AbstractUserCmd;
import com.proto.command.UserCmdType.UserCmdInType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.proto.user.gen.UserInMsg.GameOverProtoIn;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 单机游戏结果
 * 
 * @author dream
 *
 */
@ICode(code = UserCmdInType.USER_GAME_END_VALUE)
public class GameEndCmd extends AbstractUserCmd
{
    @Override
    public void execute(GamePlayer player, CommonMessage packet)
    {
        try
        {
            GameOverProtoIn proto = GameOverProtoIn.parseFrom(packet.getBody());
            ModeType type = proto.getGameType();
            int value = (int) proto.getEndValue();
            int count = proto.getDiamondCount();

            RankType rankType = RankType.Endless;
            switch (type)
            {
            case EndLess:
                rankType = RankType.Endless;
                break;
            case Room:
            case Room2:
            case Room3:
                rankType = RankType.RoomMatch;
                break;
            case RandomMatch:
            case RandomMatch2:
            case RandomMatch3:
                rankType = RankType.Net;
                break;
            case RAC:
                rankType = RankType.RACE;
                break;
            case TimeLimit:
                rankType = RankType.Timelimit;
                break;

            default:
                break;
            }

            boolean result = player.getDataModule().endGame(type, value, count);
            if (result == false)
            {
                player.sendErrorCode(ErrorCodeType.GAME_ERROR, "");
                return;
            }

            player.getDataModule().addRank(rankType, value);
            player.addResource(ResourceType.COIN.getValue(), count, ItemAddType.GAME.getValue() * 10000 + type.getNumber());

            player.onGameOver(type, value, 0, 0);

            player.getSenderModule().sendRes();

            // 保存全局最长距离
            if (type == ModeType.NormalMode || type == ModeType.EndLess)
            {
                int playerLength = player.getPlayerInfo().getTopLength();
                // 如果最长距离有更新,判断是否可以解锁其他模式
                if (value > playerLength)
                {
                    player.getPlayerInfo().setTopLength(value);

                    List<UnlockBean> unlockBeans = UnlockBeanFactory.getAll();
                    for (UnlockBean bean : unlockBeans)
                    {
                        if (value >= bean.getTargetLength())
                        {
                            player.addGameModeType(bean.getModeType());
                            String attachs = bean.getAttachModes();
                            if (attachs != null && !attachs.isEmpty())
                            {
                                List<Integer> list = StringUtil.splitIntToList(attachs, "\\,");
                                list.forEach(p -> {
                                    player.addGameModeType(p);
                                });
                            }
                        }
                    }

                    player.getSenderModule().sendRes();
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }
}
