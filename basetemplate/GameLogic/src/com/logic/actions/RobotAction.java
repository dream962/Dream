package com.logic.actions;

import com.logic.game.AbstractGame;

/**
 * 机器人Action
 * 
 * @author dream
 *
 */
public class RobotAction implements IAction
{
    @Override
    public void execute(AbstractGame game, long tick)
    {

    }

    @Override
    public boolean isFinished(AbstractGame game, long tick)
    {
        return true;
    }
}
