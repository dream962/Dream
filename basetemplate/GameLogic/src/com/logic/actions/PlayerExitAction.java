package com.logic.actions;

import com.logic.game.AbstractGame;

/**
 * 玩家离开战斗场景
 * 
 * @author dream
 *
 */
public class PlayerExitAction implements IAction
{
    private long userID;

    /** 是否保存玩家的场景信息 */
    private int exitType;

    public PlayerExitAction(long userID, int exitType)
    {
        this.userID = userID;
        this.exitType = exitType;
    }

    @Override
    public void execute(AbstractGame game, long tick)
    {
        game.removePlayer(userID);
    }

    @Override
    public boolean isFinished(AbstractGame game, long tick)
    {
        return true;
    }
}
