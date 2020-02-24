package com.logic.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.base.component.AbstractComponent;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.logic.room.BaseRoom;
import com.logic.room.RoomStateType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.util.ThreadSafeRandom;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

/**
 * 房间管理组件,对房间的所有命令操作
 * 
 * @author dream
 * @date 2013-3-10
 * @version
 */
public class RoomComponent extends AbstractComponent
{
    /** 撮合间隔时间(秒) */
    private static final int PICK_UP_INTERVAL = 3;

    /** 等待最大时间 */
    private static final int WAITING_TIME = 3 * 1000;

    /** 撮合线程 */
    private static ScheduledExecutorService singleExecutor;

    /** 本服务器的所有房间对象 */
    private static Map<Integer, BaseRoom> roomMap = new ConcurrentHashMap<>();

    /** 参与匹配的玩家<<长度,<<玩家ID,玩家>>>> */
    private static Map<Integer, Map<Integer, AbstractGamePlayer>> matchPlayers = new ConcurrentHashMap<>();

    /** 正在使用中的房间数量 */
    private static AtomicInteger roomIndex = new AtomicInteger(0);

    /**
     * 自增创建房间ID
     * 
     * @return
     */
    private static int createRoomID()
    {
        if (roomIndex.get() >= Integer.MAX_VALUE)
            roomIndex.set(0);
        return roomIndex.incrementAndGet();
    }

    @Override
    public boolean initialize()
    {
        singleExecutor = Executors.newSingleThreadScheduledExecutor();
        singleExecutor.scheduleWithFixedDelay(() -> doPickUp(), 3, PICK_UP_INTERVAL, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean start()
    {
        return true;
    }

    @Override
    public void stop()
    {
        roomMap.clear();
        matchPlayers.clear();
    }

    /**
     * 获取指定房间号的房间
     * 
     * @param roomId
     *            房间ID
     * @return id合法时返回对应房间的房间对象，否则返回null
     */
    public static BaseRoom getRoomById(int roomId)
    {
        return roomMap.get(roomId);
    }

    /**
     * 创建游戏房间
     * 
     * @param proxyGamePlayer
     *            玩家对象
     */
    public static BaseRoom createRoom(AbstractGamePlayer red, AbstractGamePlayer blue, int missionType)
    {
        ModeType modeType = ModeType.RandomMatch;
        if (missionType == 300)
            modeType = ModeType.RandomMatch2;
        else if (missionType == 500)
            modeType = ModeType.RandomMatch3;

        BaseRoom room = new BaseRoom(createRoomID(), missionType, modeType);

        if (room.addPlayer(red) && room.addPlayer(blue))
        {
            roomMap.put(room.getRoomId(), room);
            room.start();

            PrintFactory.error("撮合房间开始.red:%s,blue:%s.roomID:%s,gameType:%s", red.getNickName(), blue.getNickName(), room.getRoomId(), missionType);
            return room;
        }

        return null;
    }

    public static boolean startRoom(AbstractRoom room)
    {
        if (room != null && roomMap.containsKey(room.getRoomId()))
        {
            if (room.getAllPlayer().size() >= BaseRoom.ROOM_CAPACITY)
            {
                room.start();
                return true;
            }
        }

        // 异常关闭房间
        removeRoom(room.getRoomId());
        LogFactory.error("Start Room Exception.RoomID:{}", room.getRoomId());
        return false;
    }

    public static void removeRoom(int roomId)
    {
        roomMap.remove(roomId);
    }

    protected static void doPickUp()
    {
        try
        {
            long startTick = System.currentTimeMillis();
            pickPVP();
            long endTick = System.currentTimeMillis();

            if (endTick - startTick > 10000)
                LogFactory.warn("Pickup Room spend too much time:" + (endTick - startTick));
        }
        catch (Exception e)
        {
            LogFactory.error("撮合报错", e);
        }
    }

    public static List<BaseRoom> getAllRoom()
    {
        List<BaseRoom> list = new ArrayList<>();
        list.addAll(roomMap.values());
        return list;
    }

    /**
     * 根据类型取得不同类型房间
     * 
     * @param type
     * @return
     */
    public static List<BaseRoom> getPlayerRoom(int type)
    {
        List<BaseRoom> list = getAllRoom();
        List<BaseRoom> temp = new ArrayList<>();
        for (BaseRoom room : list)
        {
            if (room.getMissionType() == type && room.getRoomStateType() == RoomStateType.Playing && room.getGame() != null)
            {
                temp.add(room);
            }
        }
        return temp;
    }

    private static void pickPVP()
    {
        synchronized (matchPlayers)
        {
            for (Entry<Integer, Map<Integer, AbstractGamePlayer>> players : matchPlayers.entrySet())
            {
                int missionType = players.getKey();
                Map<Integer, AbstractGamePlayer> map = players.getValue();

                if (map.isEmpty())
                    continue;

                try
                {
                    // 匹配的玩家按照等待时间降序排列
                    List<AbstractGamePlayer> matchList = new ArrayList<>(map.values());
                    matchList.sort((p1, p2) -> {
                        if (p1.getRoomModule().getBeginMatchTime() - p2.getRoomModule().getBeginMatchTime() == 0)
                            return (int) (p1.getUserID() - p2.getUserID());
                        else
                            return -(int) (p1.getRoomModule().getBeginMatchTime() - p2.getRoomModule().getBeginMatchTime());
                    });

                    // 双排匹配
                    for (AbstractGamePlayer redPlayer : matchList)
                    {
                        if (redPlayer.getNetworkModule().isConnect() == false)
                        {
                            map.remove(redPlayer);
                            continue;
                        }

                        // 如果玩家当前在房间里，先退出房间
                        if (redPlayer.getRoomModule().getCurrentRoom() != null)
                        {
                            redPlayer.getRoomModule().exitRoom(0);
                        }

                        AbstractGamePlayer matchPlayer = null;

                        for (AbstractGamePlayer bluePlayer : matchList)
                        {
                            if (redPlayer != bluePlayer &&
                                    bluePlayer.getNetworkModule().isConnect() &&
                                    bluePlayer.getRoomModule().getCurrentRoom() == null)
                            {
                                matchPlayer = bluePlayer;
                                break;
                            }
                        }

                        if (matchPlayer != null)
                        {
                            if (createRoom(redPlayer, matchPlayer, missionType) != null)
                            {
                                map.remove(redPlayer.getUserID());
                                map.remove(matchPlayer.getUserID());
                                break;
                            }
                        }
                        else
                        {
                            // 超过等待时间匹配机器人
                            if (System.currentTimeMillis() - redPlayer.getRoomModule().getBeginMatchTime() >= WAITING_TIME)
                            {
                                AbstractGamePlayer blue = RobotComponent.getRobot();
                                if (blue != null)
                                {
                                    RoleType[] roleTypes = RoleType.values();
                                    int index = ThreadSafeRandom.next(0, roleTypes.length);
                                    blue.getRoomModule().setRoleType(roleTypes[index]);
                                    int level = ThreadSafeRandom.next(1, 11);
                                    blue.setRobotLevel(level);

                                    if (createRoom(redPlayer, blue, missionType) != null)
                                    {
                                        map.remove(redPlayer.getUserID());
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    LogFactory.error("", e);
                }
            }
        }
    }

    /**
     * 添加匹配的玩家
     * 
     * @param length
     * @param player
     */
    public static void addMatchPlayer(int length, AbstractGamePlayer player)
    {
        synchronized (matchPlayers)
        {
            player.getRoomModule().setBeginMatchTime(System.currentTimeMillis());
            matchPlayers.computeIfAbsent(length, k -> new ConcurrentHashMap<>()).put(player.getUserID(), player);
        }
    }

    /**
     * 移除匹配的玩家
     * 
     * @param player
     */
    public static void removeMatchPlayer(AbstractGamePlayer player)
    {
        synchronized (matchPlayers)
        {
            matchPlayers.forEach((k, v) -> {
                v.remove(player.getUserID());
            });

            player.getRoomModule().setBeginMatchTime(0);
        }
    }
}
