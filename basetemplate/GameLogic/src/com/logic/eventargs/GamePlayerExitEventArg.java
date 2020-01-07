package com.logic.eventargs;

import com.base.event.EventArg;

/**
 * 游戏内玩家离开事件
 * 
 * @author dream
 *
 */
public class GamePlayerExitEventArg extends EventArg
{
    /** 阵营战斗结果 */
    public int userID;

    public GamePlayerExitEventArg(int eventType, int userID)
    {
        super(eventType);
        this.userID = userID;
    }
}
