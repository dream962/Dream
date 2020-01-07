package com.logic.actions;

import com.base.callback.ICallBack;
import com.logic.game.AbstractGame;
import com.logic.living.Living;

/**
 * 回调Action
 * @author dream
 *
 */
public class CallFunctionAction extends DelayAction
{
    private Living living;

    private ICallBack func;

    public CallFunctionAction(Living living, ICallBack func, int delay)
    {
        super(delay);
        this.living = living;
        this.func = func;
    }

    @Override
    protected void run(AbstractGame game, long tick)
    {
        if (func != null)
            func.callback(living);
    }
}
