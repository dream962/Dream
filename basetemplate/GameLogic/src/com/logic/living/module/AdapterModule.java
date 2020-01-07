package com.logic.living.module;

import com.logic.living.Living;

/**
 * 适配器
 * 
 * @author dream
 *
 */
public class AdapterModule extends AbstractLivingModule
{
    public AdapterModule(Living living)
    {
        super(living);
    }

    @Override
    public boolean init()
    {
        return true;
    }

    @Override
    public void update(long time, int interval)
    {

    }

    @Override
    public void destroy()
    {

    }
}
