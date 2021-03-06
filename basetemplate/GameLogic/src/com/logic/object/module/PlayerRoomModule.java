package com.logic.object.module;

import java.util.List;

import com.logic.object.AbstractGamePlayer;
import com.logic.room.AbstractRoom;
import com.logic.room.BaseRoom;
import com.logic.room.FightRoom;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.RoleType;
import com.util.print.LogFactory;

/**
 * 房间组件
 * 
 * @author dream
 *
 */
public class PlayerRoomModule extends AbstractPlayerModule<AbstractGamePlayer>
{
    protected AbstractRoom currentRoom;

    private long beginMatchTime = 0;

    private RoleType type;

    private int winCount;

    public PlayerRoomModule(AbstractGamePlayer player)
    {
        super(player);
    }

    public int getWinCount()
    {
        return this.winCount;
    }

    public void setWinCount(int count)
    {
        this.winCount = count;
    }

    public void setRoleType(RoleType type)
    {
        if (type == null)
            type = RoleType.Panda;

        this.type = type;
    }

    public RoleType getRoleType()
    {
        return this.type;
    }

    public void setBeginMatchTime(long time)
    {
        this.beginMatchTime = time;
    }

    public long getBeginMatchTime()
    {
        return this.beginMatchTime;
    }

    public AbstractRoom getCurrentRoom()
    {
        return currentRoom;
    }

    public void setCurrentRoom(AbstractRoom room)
    {
        this.currentRoom = room;
    }

    /**
     * 玩家离开场景
     * 
     * @param type
     *            1-主动退出；2-跳转退出，保存位置；3-强制退出
     */
    public void exitRoom(int type)
    {
        this.winCount = 0;
        this.beginMatchTime = 0;
        this.type = null;

        if (currentRoom != null)
        {
            if (currentRoom instanceof FightRoom)
            {
                if (((FightRoom) currentRoom).getOwner() == player)
                {
                    ((FightRoom) currentRoom).distroy();
                }
                else
                {
                    currentRoom.removePlayer(player, type);
                }

                currentRoom = null;
            }

            if (currentRoom instanceof BaseRoom)
            {
                currentRoom.removePlayer(player, type);
                currentRoom = null;
            }
        }

        LogFactory.error("Exit Room.{},UserID:{}", type, player.getUserID());
    }

    public void disconnectRoom()
    {
        this.winCount = 0;
        this.beginMatchTime = 0;
        this.type = null;

        if (currentRoom != null)
        {
            if (currentRoom instanceof FightRoom)
            {
                if (((FightRoom) currentRoom).getOwner() == player)
                {
                    ((FightRoom) currentRoom).distroy();
                }
                else
                {
                    List<AbstractGamePlayer> players = currentRoom.getAllPlayer();
                    for (AbstractGamePlayer p : players)
                        p.sendMessage(UserCmdOutType.EXIT_ROOM_RETURN_VALUE, null);

                    currentRoom.removePlayer(player, 0);
                }

                currentRoom = null;
            }

            if (currentRoom instanceof BaseRoom)
            {
                currentRoom.removePlayer(player, 0);
                currentRoom = null;
            }
        }

        LogFactory.error("Disconnect Exit Room.{},UserID:{}", type, player.getUserID());
    }

}
