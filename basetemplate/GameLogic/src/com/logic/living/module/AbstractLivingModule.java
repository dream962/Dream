package com.logic.living.module;

import com.logic.living.Living;

/**
 * 抽象模块类
 * 
 * @author dream
 *
 */
public abstract class AbstractLivingModule
{
    protected Living owner;

    public AbstractLivingModule(Living living)
    {
        this.owner = living;
    }

    public Living getOwner()
    {
        return this.owner;
    }

    public abstract boolean init();

    public abstract void update(long time, int interval);

    public void destroy()
    {
        owner = null;
    }
}
