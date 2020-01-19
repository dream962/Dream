package com.logic.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.event.IEventListener;
import com.base.net.CommonMessage;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.game.AbstractGame;
import com.logic.object.AbstractGamePlayer;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.PlatformTheme;
import com.util.StringSplitUtil;
import com.util.ThreadSafeRandom;
import com.util.print.LogFactory;

/**
 * 抽象游戏房间接口
 * 
 * @author dream
 * @date 2013-3-15
 * @version
 */
public abstract class AbstractRoom
{
    /** 房间ID */
    protected int roomId;

    /** 地图ID */
    protected int missionID;

    /** 房间状态 */
    protected RoomStateType roomState;

    /** 当前游戏类型 */
    protected int missionType;

    /** 游戏对象 */
    protected AbstractGame game;

    /** 玩家人数 */
    protected int playerCount;

    /** 游戏停止监听 */
    protected IEventListener gameOverListener;

    protected ModeType modeType;

    public AbstractRoom(int roomID, int gameType, ModeType modeType)
    {
        roomState = RoomStateType.Using;
        roomId = roomID;
        playerCount = 0;
        this.missionType = gameType;
        this.modeType = modeType;
    }

    public int getMissionID()
    {
        return missionID;
    }

    public void setMissionID(int id)
    {
        this.missionID = id;
    }

    public RoomStateType getRoomStateType()
    {
        return this.roomState;
    }

    public void setRoomStateType(RoomStateType stateType)
    {
        this.roomState = stateType;
    }

    public int getRoomId()
    {
        return this.roomId;
    }

    public int getMissionType()
    {
        return this.missionType;
    }

    public void setMissionType(int type)
    {
        this.missionType = type;
    }

    public AbstractGame getGame()
    {
        return this.game;
    }

    public int getPlayerCount()
    {
        return this.playerCount;
    }

    /**
     * 房间转发数据包
     */
    public void processGameData(CommonMessage packet)
    {
        if (game != null)
            game.processData(packet);
        else
            LogFactory.error(String.format("the game in room is null,but received packet. --- Code:%d,GameKey:%s", packet.getCode(), packet.getParam()));
    }

    public abstract List<AbstractGamePlayer> getAllPlayer();

    public abstract boolean addPlayer(AbstractGamePlayer gamePlayer);

    public abstract void removePlayer(AbstractGamePlayer player, int exitType);

    public abstract void start();

    public abstract void stop();

    /**
     * 取得随机的主题
     * 
     * @return
     */
    public int getRandomTheme()
    {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        List<AbstractGamePlayer> players = getAllPlayer();
        for (AbstractGamePlayer p : players)
        {
            int[] list = StringSplitUtil.splitToInt(p.getPlayerInfo().getTheme(), "\\,");
            for (int i : list)
            {
                if (!map.containsKey(i))
                    map.put(i, 1);

                map.put(i, map.get(i) + 1);
            }
        }

        List<Integer> result = new ArrayList<Integer>();
        map.forEach((k, v) -> {
            if (v >= 2)
                result.add(k);
        });

        if (result.isEmpty())
            return PlatformTheme.Desert_VALUE;
        else
        {
            int index = ThreadSafeRandom.next(0, result.size());
            return result.get(index);
        }
    }

    public void sendToAll(CommonMessage msg, AbstractGamePlayer except)
    {
        List<AbstractGamePlayer> temp = getAllPlayer();

        if (temp != null && !temp.isEmpty())
        {
            for (AbstractGamePlayer player : temp)
            {
                if (player != except)
                    player.sendMessage(msg);
            }
        }
    }

    public void sendToAll(Builder<?> builder, int code, AbstractGamePlayer except)
    {
        List<AbstractGamePlayer> temp = getAllPlayer();

        if (temp != null && !temp.isEmpty())
        {
            for (AbstractGamePlayer player : temp)
            {
                if (player.getNetworkModule().getClientConnection() == null)
                    continue;

                CommonMessage msg = new CommonMessage(code);
                if (builder != null)
                    msg.setBody(builder.build().toByteArray());

                if (player != except)
                    player.sendMessage(msg);
            }
        }
    }

    public void sendToAll(Builder<?> builder, int code)
    {
        sendToAll(builder, code, null);
    }

    public void sendCodeToAll(int code, AbstractGamePlayer except)
    {
        List<AbstractGamePlayer> temp = getAllPlayer();

        if (temp != null && !temp.isEmpty())
        {
            for (AbstractGamePlayer player : temp)
            {
                CommonMessage msg = new CommonMessage(code);
                if (player != except)
                    player.sendMessage(msg);
            }
        }
    }
}
