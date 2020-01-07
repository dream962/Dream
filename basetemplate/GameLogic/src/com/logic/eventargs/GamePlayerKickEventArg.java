package com.logic.eventargs;

import com.base.event.EventArg;

/**
 * 游戏内玩家被踢出事件
 * 
 * @author dream
 *
 */
public class GamePlayerKickEventArg extends EventArg
{
    /** 阵营战斗结果 */
    public int userID;

    public GamePlayerKickEventArg(int eventType, int userID)
    {
        super(eventType);
        this.userID = userID;
    }
}
