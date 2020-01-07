package com.logic.room;

import java.util.ArrayList;
import java.util.List;

import com.base.event.EventArg;
import com.logic.actions.PlayerExitAction;
import com.logic.component.FightRoomComponent;
import com.logic.component.GameComponent;
import com.logic.map.MapBuilder;
import com.logic.object.AbstractGamePlayer;
import com.logic.type.GameEventType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.PlatformTheme;
import com.proto.user.gen.UserOutMsg.MatchMapProtoOut;
import com.proto.user.gen.UserOutMsg.MatchPlayerDetail;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.proto.user.gen.UserOutMsg.PlatformInfoProtoOut;
import com.proto.user.gen.UserOutMsg.RandomMatchProtoOut;
import com.util.ThreadSafeRandom;
import com.util.print.LogFactory;

/**
 * 对战房间
 * 
 * @author admin
 *
 */
public class FightRoom extends AbstractRoom
{
    public final static int ROOM_CAPACITY = 2;

    /** 房间内玩家列表 */
    private List<AbstractGamePlayer> players;

    /** 房间拥有者 */
    private AbstractGamePlayer owner;

    /** 房间名称 */
    private String roomName;

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public FightRoom(int roomId, int gameType, ModeType modeType)
    {
        super(roomId, gameType, modeType);
        gameOverListener = (e) -> onGameOver(e);
        players = new ArrayList<>();
    }

    /**
     * 开始使用房间
     */
    @Override
    public void start()
    {
        try
        {
            roomState = RoomStateType.Playing;

            RandomMatchProtoOut.Builder builder = RandomMatchProtoOut.newBuilder();

            // 匹配的长度
            int length = getMissionType();
            // 匹配的主题
            PlatformTheme theme = PlatformTheme.valueOf((ThreadSafeRandom.next(1, 8)));
            // 生成的地块
            List<PlatformGroupInfoProtoOut> map = MapBuilder.build(length, theme);

            MatchMapProtoOut.Builder builder2 = MatchMapProtoOut.newBuilder();
            builder2.setTotalLength(length);
            builder2.setBegin(0);
            if (map.size() <= 200)
            {
                builder2.setEnd(map.size() - 1);
                builder2.addAllPlatformList(map);
                sendToAll(builder2, UserCmdOutType.ROOM_RESULT_MAP_VALUE);
            }
            else
            {
                for (int i = 0; i < map.size(); i++)
                {
                    builder2.addPlatformList(map.get(i));
                    if (builder2.getPlatformListCount() == 200)
                    {
                        builder2.setEnd(i);
                        sendToAll(builder2, UserCmdOutType.ROOM_RESULT_MAP_VALUE);

                        if (i + 1 >= map.size())
                        {
                            builder2 = null;
                        }
                        else
                        {
                            builder2 = MatchMapProtoOut.newBuilder();
                            builder2.setTotalLength(length);
                            builder2.setBegin(i + 1);
                        }
                    }
                }
                if (builder2 != null)
                {
                    builder2.setEnd(map.size() - 1);
                    sendToAll(builder2, UserCmdOutType.ROOM_RESULT_MAP_VALUE);
                }
            }

            builder.setPlatformType(theme);
            List<AbstractGamePlayer> players = getAllPlayer();
            int index = 0;
            for (AbstractGamePlayer player : players)
            {
                MatchPlayerDetail.Builder detail = MatchPlayerDetail.newBuilder();
                detail.setUserID(player.getUserID());
                detail.setUserName(player.getNickName());
                detail.setRoleType(player.getRoomModule().getRoleType());
                detail.setHeadID(player.getPlayerInfo().getHeaderID());
                if (getOwner() != null && getOwner() == player)
                    detail.setIsRoomOwner(true);
                else
                    detail.setIsRoomOwner(false);

                PlatformGroupInfoProtoOut platformGroupInfoProtoOut = map.get(0);
                index++;
                if (index % 2 == 0)// Red
                {
                    PlatformInfoProtoOut proto = platformGroupInfoProtoOut.getPlatformInfo();
                    detail.setPosX(proto.getPosX());
                    detail.setPosY(proto.getPosY());
                    detail.setPosZ(proto.getPosZ());
                }
                else// Blue
                {
                    PlatformInfoProtoOut proto = platformGroupInfoProtoOut.getPlatformInfoExtral();
                    detail.setPosX(proto.getPosX());
                    detail.setPosY(proto.getPosY());
                    detail.setPosZ(proto.getPosZ());
                }

                builder.addPlayers(detail);
            }

            sendToAll(builder, UserCmdOutType.ROOM_RESULT_VALUE);

            if (game != null)
                game.stop();

            game = GameComponent.startPvPGame(missionType, modeType, players, map);

            if (gameOverListener != null)
                game.getEvent().addListener(GameEventType.GameOver.getValue(), gameOverListener);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
    }

    private void onGameOver(EventArg e)
    {
        if (game != null)
        {
            game.getEvent().removeListener(GameEventType.GameOver.getValue(), gameOverListener);
        }

        roomState = RoomStateType.Ready;
        game = null;
    }

    /**
     * 停用房间
     */
    @Override
    public void stop()
    {
        players.clear();
        game = null;

        FightRoomComponent.removeRoom(getRoomId());
    }

    public void setOwner(AbstractGamePlayer player)
    {
        this.owner = player;
    }

    public AbstractGamePlayer getOwner()
    {
        return owner;
    }

    /**
     * 添加玩家
     * 
     * @param player
     *            玩家对象
     */
    @Override
    public boolean addPlayer(AbstractGamePlayer player)
    {
        synchronized (players)
        {
            if (players.size() > ROOM_CAPACITY)
                return false;

            players.add(player);
            playerCount++;
            player.getRoomModule().setCurrentRoom(this);
        }

        return true;
    }

    /**
     * /**
     * 移除玩家:1：主动；4：房主解散；3：踢除
     */
    @Override
    public void removePlayer(AbstractGamePlayer player, int type)
    {
        AbstractGamePlayer robot = null;
        synchronized (players)
        {
            for (int i = 0; i < players.size(); ++i)
            {
                if (player.getUserID() == players.get(i).getUserID())
                {
                    players.remove(i);
                    playerCount--;
                    break;
                }
            }

            // 玩家移除,更新状态
            if (roomState == RoomStateType.Ready)
            {
                roomState = RoomStateType.Using;
            }

            // 如果只剩下一个robot,移除
            for (int i = 0; i < players.size(); ++i)
            {
                if (players.get(i).getIsRobot())
                {
                    robot = players.get(i);
                    break;
                }
            }
        }

        if (robot != null)
            robot.getRoomModule().exitRoom(1);

        player.getRoomModule().setCurrentRoom(null);
        if (game != null)
        {
            // 移除玩家
            game.addAction(new PlayerExitAction(player.getUserID(), type));
        }

        if (players.size() <= 0)
        {
            stop();
        }
    }

    /**
     * 销毁房间
     */
    public void distroy()
    {
        List<AbstractGamePlayer> list = getAllPlayer();

        for (AbstractGamePlayer p : list)
            p.sendMessage(UserCmdOutType.DISMISS_ROOM_RETURN_VALUE, null);

        for (AbstractGamePlayer player : list)
        {
            removePlayer(player, 0);
        }
    }

    public List<AbstractGamePlayer> getAllPlayer()
    {
        List<AbstractGamePlayer> temp = new ArrayList<AbstractGamePlayer>();
        synchronized (players)
        {
            for (AbstractGamePlayer p : players)
                temp.add(p);
        }
        return temp;
    }
}
