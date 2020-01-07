package com.logic.actions;

import com.base.callback.ICallBack;
import com.logic.game.AbstractGame;
import com.logic.living.Player;

/**
 *  等待玩家一段时间再执行
 * @author dream
 *
 */
public class WaitAction implements IAction
{
    private Player player;
    private ICallBack timeOutCallBack; // 时间到了回调
    private long finishTick;
    boolean timeout;

    public WaitAction(Player player, long maxwaitTime, ICallBack timeOutCallBack)
    {
        this.finishTick = System.currentTimeMillis() + maxwaitTime;
        this.player = player;

        this.timeOutCallBack = timeOutCallBack;
        this.timeout = false;
    }

    @Override
    public void execute(AbstractGame game, long tick)
    {
        // 时间到了，调用回调函数
        if (tick >= this.finishTick)
        {
            timeout = true;
            timeOutCallBack.callback(player);
        }
    }

    @Override
    public boolean isFinished(AbstractGame game, long tick)
    {
        return timeout;
    }
}
