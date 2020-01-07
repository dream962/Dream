package com.logic.actions;

import com.logic.game.AbstractGame;

/**
 * 一定分开两次执行action，指定延时后执行，移除时执行
 */
public abstract class TwiceRunAction implements IAction
{
    private long firstRunTime;
    private int finishDelay;
    private long secondRunTime;

    public TwiceRunAction(int delay, int finishDelay)
    {
        this.firstRunTime = System.currentTimeMillis() + delay;
        this.finishDelay = finishDelay;
        this.secondRunTime = Long.MAX_VALUE;
    }

    @Override
    public void execute(AbstractGame game, long tick)
    {
        if (this.secondRunTime == Long.MAX_VALUE && this.firstRunTime <= tick)
        {
            this.secondRunTime = tick + this.finishDelay;
            run1(game, tick);
        }

        if (isFinished(game, tick))
        {
            run2(game, tick);
        }
    }

    /**
     * @param game
     * @param tick
     */
    protected abstract void run1(AbstractGame game, long tick);

    /**
     * @param game
     * @param tick
     */
    protected abstract void run2(AbstractGame game, long tick);

    @Override
    public boolean isFinished(AbstractGame game, long tick)
    {
        return this.secondRunTime <= tick;
    }

}
