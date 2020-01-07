package com.game.module;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;

/**
 * 通用适配器类
 * 
 * @author dream
 *
 */
public abstract class LockPlayerModule extends AbstractPlayerModule<GamePlayer>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LockPlayerModule.class);

    private ReentrantLock moduleLock = new ReentrantLock();

    private AtomicInteger lockCount = new AtomicInteger(0);

    public LockPlayerModule(GamePlayer player)
    {
        super(player);
    }

    /** 开始改变 */
    public void beginChanges()
    {
        try
        {
            if (moduleLock.tryLock(10, TimeUnit.SECONDS))
                lockCount.incrementAndGet();
            else
                LOGGER.error("PlayerModule beginChanges Error: Lock wait too long time.holdCount:" + moduleLock.getHoldCount()
                        + ",QueueLength:" + moduleLock.getQueueLength());
        }
        catch (Exception e)
        {
            LOGGER.error("PlayerModule beginChanges Error:", e);
        }
    }

    /** 结束改变 */
    public int commitChanges()
    {
        try
        {
            moduleLock.unlock();
            if (lockCount.get() <= 0)
            {
                LOGGER.error("PlayerModule lock Error:lockCount is 0.");
            }
            else
            {
                lockCount.decrementAndGet();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("PlayerModule commitChanges Error:", e);
        }

        return lockCount.get();
    }
}
