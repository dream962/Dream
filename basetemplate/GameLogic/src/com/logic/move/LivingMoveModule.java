package com.logic.move;

import com.logic.living.Living;
import com.logic.living.module.AbstractLivingModule;

/**
 * 移动模块
 * 
 * @author dream
 *
 */
public class LivingMoveModule extends AbstractLivingModule
{
    public LivingMoveModule(Living living)
    {
        super(living);
    }

    @Override
    public boolean init()
    {
        return false;
    }

    @Override
    public void update(long time, int interval)
    {

    }
}
