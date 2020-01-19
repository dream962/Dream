package com.game.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.base.component.AbstractComponent;
import com.base.component.SimpleScheduleComponent;
import com.data.business.PlayerBusiness;
import com.data.info.FriendInfo;
import com.data.info.RankInfo;
import com.game.object.player.GamePlayer;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RankSectionType;
import com.proto.common.gen.CommonOutMsg.RankTimeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.proto.user.gen.UserOutMsg.RankBaseInfoListProtoOut;
import com.proto.user.gen.UserOutMsg.RankBaseInfoProtoOut;
import com.proto.user.gen.UserOutMsg.RankInfoListProtoOut;
import com.proto.user.gen.UserOutMsg.RankInfoProtoOut;

/**
 * 排行榜
 * 
 * @author dream
 *
 */
public class RankComponent extends AbstractComponent
{
    private static final int RANK = 100;

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /** 榜单协议<时间类型,<排行类型,proto数据>> */
    private static Map<Integer, Map<Integer, RankInfoListProtoOut.Builder>> rankProtoMap = new ConcurrentHashMap<>();

    /** 榜单<时间类型,<排行类型,排行数据>> */
    private static Map<Integer, Map<Integer, List<RankInfo>>> rankMap = new ConcurrentHashMap<>();

    private static long lastUpdateTime = System.currentTimeMillis();

    @Override
    public boolean initialize()
    {
        RankTimeType[] rankTimeTypes = RankTimeType.values();
        // 遍历时间类型
        for (RankTimeType timeType : rankTimeTypes)
        {
            // 初始化排行类型数据<rankType，list>
            Map<Integer, List<RankInfo>> rankTempMap = new ConcurrentHashMap<>();

            // 查询排行信息
            Map<Integer, List<RankInfo>> queryMap = PlayerBusiness.queryRankTypeListByTime(timeType.getNumber());

            // 遍历排行类型
            RankType[] rankTypes = RankType.values();
            for (RankType rankType : rankTypes)
            {
                // 取得数据库查询的列表
                List<RankInfo> ranks = queryMap.get(rankType.getNumber());
                if (ranks != null)
                {
                    sort(rankType.getNumber(), ranks);
                    rankTempMap.put(rankType.getNumber(), ranks);
                }
                else
                {
                    rankTempMap.put(rankType.getNumber(), new ArrayList<>());
                }
            }

            // 榜单:<时间类型,<排行类型,排行数据>>
            rankMap.put(timeType.getNumber(), rankTempMap);
        }

        genProto();

        return true;
    }

    public static void genProto()
    {
        lock.writeLock().lock();
        try
        {
            for (Entry<Integer, Map<Integer, List<RankInfo>>> time : rankMap.entrySet())
            {
                int timeType = time.getKey();
                Map<Integer, List<RankInfo>> map = time.getValue();

                for (Entry<Integer, List<RankInfo>> rank : map.entrySet())
                {
                    int rankType = rank.getKey();
                    List<RankInfo> list = rank.getValue();

                    RankInfoListProtoOut.Builder builder = RankInfoListProtoOut.newBuilder();
                    builder.setRankTimeType(RankTimeType.valueOf(timeType));
                    builder.setRankType(RankType.valueOf(rankType));
                    builder.setRankSectionType(RankSectionType.World);

                    for (int i = 0; i < list.size(); i++)
                    {
                        if (i >= RANK)
                            break;

                        RankInfo rankInfo = list.get(i);

                        RankInfoProtoOut.Builder proto = RankInfoProtoOut.newBuilder();
                        proto.setHeadID(rankInfo.getHeader());
                        proto.setNickName(rankInfo.getNickName());
                        proto.setRankIndex(i + 1);
                        proto.setResultValue(rankInfo.getRankValue());

                        builder.addRankList(proto);
                    }

                    rankProtoMap.computeIfAbsent(timeType, k -> new ConcurrentHashMap<>()).put(rankType, builder);
                }
            }

            lastUpdateTime = System.currentTimeMillis();
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
     * 修改头像
     * 
     * @param userID
     * @param headerID
     */
    public static void changeHeader(int userID, int headerID)
    {
        for (Entry<Integer, Map<Integer, List<RankInfo>>> e1 : rankMap.entrySet())
        {
            if (e1.getValue() != null)
            {
                for (Entry<Integer, List<RankInfo>> e2 : e1.getValue().entrySet())
                {
                    if (e2.getValue() != null)
                    {
                        for (RankInfo e3 : e2.getValue())
                        {
                            if (e3.getUserID() == userID)
                            {
                                e3.setHeader(headerID);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void sort(int rankType, List<RankInfo> ranks)
    {
        if (rankType == RankType.RACE_VALUE)
        {
            ranks.sort((p1, p2) -> {
                if (p1.getRankValue() != p2.getRankValue())
                    return p1.getRankValue() - p2.getRankValue();
                else
                    return (int) (p1.getUserID() - p2.getUserID());
            });
        }
        else
        {
            ranks.sort((p1, p2) -> {
                if (p1.getRankValue() != p2.getRankValue())
                    return -(p1.getRankValue() - p2.getRankValue());
                else
                    return (int) (p1.getUserID() - p2.getUserID());
            });
        }
    }

    /**
     * 每周刷新
     */
    public static void refreshWeek()
    {
        Map<Integer, List<RankInfo>> map = rankMap.get(RankTimeType.Week_VALUE);
        for (Entry<Integer, List<RankInfo>> m : map.entrySet())
        {
            m.getValue().clear();
        }

        List<GamePlayer> players = GamePlayerComponent.getAllPlayer();
        for (GamePlayer player : players)
        {
            player.getDataModule().refreshRank(RankTimeType.Week);
        }

        genProto();

        PlayerBusiness.updateRank(RankTimeType.Week_VALUE);
    }

    /**
     * 每月刷新
     */
    public static void refreshMonth()
    {
        Map<Integer, List<RankInfo>> map = rankMap.get(RankTimeType.Month_VALUE);
        for (Entry<Integer, List<RankInfo>> m : map.entrySet())
        {
            m.getValue().clear();
        }

        List<GamePlayer> players = GamePlayerComponent.getAllPlayer();
        for (GamePlayer player : players)
        {
            player.getDataModule().refreshRank(RankTimeType.Month);
        }

        genProto();

        PlayerBusiness.updateRank(RankTimeType.Month_VALUE);
    }

    @Override
    public void stop()
    {
        rankMap.clear();
        rankProtoMap.clear();
    }

    /**
     * 查询玩家的排行
     * 
     * @param rankType
     * @param timeType
     * @param playerID
     * @return
     */
    public static RankInfoProtoOut.Builder findPlayerRank(RankType rankType, RankTimeType timeType, GamePlayer player)
    {
        RankInfoProtoOut.Builder playerBuilder = RankInfoProtoOut.newBuilder();
        playerBuilder.setHeadID(player.getPlayerInfo().getHeaderID());
        playerBuilder.setNickName(player.getNickName());

        List<RankInfo> temp = rankMap.getOrDefault(timeType.getNumber(), new ConcurrentHashMap<>()).get(rankType.getNumber());
        if (temp != null)
        {
            for (int i = 0; i < temp.size(); i++)
            {
                if (player.getUserID() == temp.get(i).getUserID())
                {
                    playerBuilder.setRankIndex(i + 1);
                    playerBuilder.setResultValue(temp.get(i).getRankValue());
                    return playerBuilder;
                }
            }
        }

        playerBuilder.setRankIndex(0);
        playerBuilder.setResultValue(0);
        return playerBuilder;
    }

    public static void addRank(RankInfo info)
    {
        SimpleScheduleComponent.schedule((job) -> {
            addRankAction(info);
        });
    }

    private static void addRankAction(RankInfo info)
    {
        List<RankInfo> temp = rankMap.computeIfAbsent(info.getTimeType(), k -> new ConcurrentHashMap<>()).computeIfAbsent(info.getRankType(), k -> new ArrayList<>());
        boolean isContain = false;
        boolean isUpdate = false;
        for (RankInfo data : temp)
        {
            if (data.getUserID() == info.getUserID())
            {
                isContain = true;

                if (info.getRankType() == RankType.RACE_VALUE)
                {
                    if (info.getRankValue() < data.getRankValue())
                    {
                        data.setRankValue(info.getRankValue());
                        data.setUpdateTime(info.getUpdateTime());
                        isUpdate = true;
                    }
                }
                else
                {
                    if (info.getRankValue() > data.getRankValue())
                    {
                        data.setRankValue(info.getRankValue());
                        data.setUpdateTime(info.getUpdateTime());
                        isUpdate = true;
                    }
                }

                data.setHeader(info.getHeader());
                data.setNickName(info.getNickName());
            }
        }

        if (isContain == false)
        {
            RankInfo info2 = info.clone();
            info2.setTimeType(info.getTimeType());
            temp.add(info2);
            isUpdate = true;
        }

        if (isUpdate)
        {
            sort(info.getRankType(), temp);

            if (System.currentTimeMillis() - lastUpdateTime >= 200)
            {
                genProto();
            }
        }
    }

    /**
     * 单个数据
     * 
     * @param player
     * @param rankType
     * @param timeType
     * @param sectionType
     */
    public static void sendRank(GamePlayer player, RankType rankType, RankTimeType timeType, RankSectionType sectionType)
    {
        RankInfoListProtoOut.Builder builder = RankInfoListProtoOut.newBuilder();
        builder.setRankSectionType(sectionType);
        builder.setRankTimeType(timeType);
        builder.setRankType(rankType);

        if (sectionType == RankSectionType.World)
        {
            Map<Integer, RankInfoListProtoOut.Builder> map = rankProtoMap.getOrDefault(timeType.getNumber(), new HashMap<>());
            builder = map.get(rankType.getNumber());
            if (builder == null)
            {
                builder = RankInfoListProtoOut.newBuilder();
                builder.setRankSectionType(sectionType);
                builder.setRankTimeType(timeType);
                builder.setRankType(rankType);
            }

            RankInfoProtoOut.Builder playerBuilder = findPlayerRank(rankType, timeType, player);
            builder.setPlayerRankInfo(playerBuilder);
        }

        if (sectionType == RankSectionType.Friend)
        {
            Map<Integer, FriendInfo> friends = player.getDataModule().getAllFriends();

            List<RankInfo> list = rankMap.getOrDefault(timeType.getNumber(), new HashMap<>()).getOrDefault(rankType.getNumber(), new ArrayList<>());

            List<RankInfo> friendDataList = new ArrayList<>();

            for (RankInfo info : list)
            {
                if (info.getUserID() == player.getUserID())
                {
                    friendDataList.add(info);
                    continue;
                }

                if (friends.containsKey(info.getUserID()))
                {
                    friendDataList.add(info);
                }
            }

            if (!friendDataList.isEmpty())
            {
                sort(rankType.getNumber(), friendDataList);

                for (int i = 0; i < friendDataList.size(); i++)
                {
                    RankInfo rankInfo = friendDataList.get(i);

                    // 朋友排名
                    RankInfoProtoOut.Builder proto = RankInfoProtoOut.newBuilder();
                    proto.setHeadID(rankInfo.getHeader());
                    proto.setNickName(rankInfo.getNickName());
                    proto.setRankIndex(i + 1);
                    proto.setResultValue(rankInfo.getRankValue());

                    builder.addRankList(proto);

                    // 个人排名
                    if (rankInfo.getUserID() == player.getUserID())
                    {
                        builder.setPlayerRankInfo(proto);
                    }
                }
            }
            else
            {
                RankInfoProtoOut.Builder playerBuilder = findPlayerRank(rankType, timeType, player);

                RankInfoProtoOut.Builder proto = RankInfoProtoOut.newBuilder();
                proto.setHeadID(player.getPlayerInfo().getHeaderID());
                proto.setNickName(player.getNickName());
                proto.setRankIndex(1);
                proto.setResultValue(playerBuilder.getResultValue());

                builder.setPlayerRankInfo(proto);
            }

        }

        player.sendMessage(UserCmdOutType.LEADERBOARD_RETURN_VALUE, builder);
    }

    /**
     * 基础数据
     * 
     * @param player
     */
    public static void sendBaseRank(GamePlayer player)
    {
        RankBaseInfoListProtoOut.Builder builder = RankBaseInfoListProtoOut.newBuilder();

        // 初始化排行类型数据
        RankType[] rankTypes = RankType.values();
        for (RankType rankType : rankTypes)
        {
            RankInfoProtoOut.Builder builder2 = findPlayerRank(rankType, RankTimeType.Week, player);

            RankBaseInfoProtoOut.Builder detail = RankBaseInfoProtoOut.newBuilder();
            detail.setRankType(rankType);
            detail.setTotalRankIndex(builder2.getRankIndex());

            builder.addRankBaseInfoList(detail);
        }

        player.sendMessage(UserCmdOutType.LEADERBOARD_BASE_RETURN_VALUE, builder);
    }
}
