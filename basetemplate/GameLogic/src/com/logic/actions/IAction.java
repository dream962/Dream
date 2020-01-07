package com.logic.actions;

import com.logic.game.AbstractGame;

/**
 * @version 地图内物体的动作
 */
public interface IAction
{
    public void execute(AbstractGame game, long tick);

    public boolean isFinished(AbstractGame game, long tick);
}
