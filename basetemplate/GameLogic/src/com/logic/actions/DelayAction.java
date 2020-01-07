package com.logic.actions;

import com.logic.game.AbstractGame;

/**
 * 延时执行action
 */
public abstract class DelayAction implements IAction
{
    private long tick;

    public DelayAction(int delay)
    {
        this.tick = System.currentTimeMillis() + delay;
    }

    @Override
    public void execute(AbstractGame game, long tick)
    {
        if (this.tick <= tick)
        {
            run(game, tick);
        }
    }

    /**
     * @param game
     * @param tick
     */
    protected abstract void run(AbstractGame game, long tick);

    @Override
    public boolean isFinished(AbstractGame game, long tick)
    {
        return this.tick <= tick;
    }
}
