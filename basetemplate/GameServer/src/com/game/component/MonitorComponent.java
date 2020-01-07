package com.game.component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.base.component.AbstractComponent;
import com.util.NamedThreadFactory;

/**
 * 监听组件
 * 
 * @author dream
 *
 */
public class MonitorComponent extends AbstractComponent
{
    private static ScheduledExecutorService schedule;

    public static void begin()
    {
        if (schedule != null)
        {
            schedule = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("monitor-thread"));
            schedule.scheduleWithFixedDelay(() -> update(), 10, 10, TimeUnit.SECONDS);
        }
    }

    public static void end()
    {
        if (schedule != null)
        {
            schedule.shutdownNow();
            schedule = null;
        }
    }

    @Override
    public boolean initialize()
    {
        return true;
    }

    private static void update()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("------------------monitor------------------").append("\n");
        int count = GamePlayerComponent.getOnlineCount();
        int all = GamePlayerComponent.getPlayerCount();
        builder.append("online player:").append(count).append("  -- all player:").append(all).append("\n");

        System.err.println(builder.toString());
    }

    @Override
    public void stop()
    {
        schedule.shutdownNow();
    }
}
