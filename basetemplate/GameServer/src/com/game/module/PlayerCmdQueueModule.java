package com.game.module;

import com.base.executor.ExecutorPool;
import com.base.executor.SelfDrivenTaskQueue;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;

/**
 * 玩家命令队列模块
 * 
 * @author dream
 *
 */
public class PlayerCmdQueueModule extends AbstractPlayerModule<GamePlayer>
{
    /** 命令队列 */
    private SelfDrivenTaskQueue cmdQueue;

    /** 玩家命令处理线程池 */
    private static ExecutorPool userCmdpool = new ExecutorPool(Runtime.getRuntime().availableProcessors() * 3 + 1, "GamePlayer-Worker");

    public PlayerCmdQueueModule(GamePlayer player)
    {
        super(player);
        cmdQueue = new SelfDrivenTaskQueue(userCmdpool);
    }

    public static ExecutorPool getPool()
    {
        return userCmdpool;
    }

    public SelfDrivenTaskQueue getQueue()
    {
        return cmdQueue;
    }
}
