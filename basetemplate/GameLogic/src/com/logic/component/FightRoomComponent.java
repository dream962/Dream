package com.logic.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.base.component.AbstractComponent;
import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.logic.room.BaseRoom;
import com.logic.room.FightRoom;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.RoleType;

/**
 * 战斗房间管理组件
 * 
 * @author dream
 * @date 2013-3-10
 * @version
 */
public class FightRoomComponent extends AbstractComponent
{
    /** 本服务器的所有战斗房间对象 */
    private static Map<Integer, FightRoom> roomMap = new ConcurrentHashMap<>();

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
        return true;
    }

    @Override
    public void stop()
    {
        roomMap.clear();
    }

    /**
     * 获取指定房间号的房间
     * 
     * @param roomId
     *            房间ID
     * @return id合法时返回对应房间的房间对象，否则返回null
     */
    public static FightRoom getRoomByID(int roomId)
    {
        return roomMap.get(roomId);
    }

    /**
     * 获取指定名称的房间
     * 
     * @param roomName
     *            房间名称
     * @return
     */
    public static FightRoom getRoomByName(String roomName)
    {
        for (FightRoom room : roomMap.values())
        {
            if (roomName.equalsIgnoreCase(room.getRoomName()))
            {
                return room;
            }
        }

        return null;
    }

    /**
     * 创建对战房间
     * 
     * @param player
     * @param roomName
     * @param length
     * @return
     */
    public static FightRoom createRoom(AbstractGamePlayer player, String roomName, int length, RoleType type)
    {
        ModeType modeType = ModeType.Room;
        if (length == 500)
            modeType = ModeType.Room2;
        else if (length == 1000)
            modeType = ModeType.Room3;

        FightRoom room = new FightRoom(createRoomID(), length, modeType);
        room.setRoomName(roomName);
        player.getRoomModule().setRoleType(type);
        room.setOwner(player);

        if (room.addPlayer(player))
        {
            roomMap.put(room.getRoomId(), room);
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

        return false;
    }

    public static void removeRoom(int roomId)
    {
        roomMap.remove(roomId);
    }

    public static List<FightRoom> getAllRoom()
    {
        List<FightRoom> list = new ArrayList<>();
        list.addAll(roomMap.values());
        return list;
    }
}
