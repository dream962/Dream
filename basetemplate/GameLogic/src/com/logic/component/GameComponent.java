package com.logic.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.base.component.AbstractComponent;
import com.logic.game.AbstractGame;
import com.logic.game.PVPGame;
import com.logic.object.AbstractGamePlayer;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.util.print.LogFactory;

/**
 * 游戏逻辑组件
 * 
 * @author dream.wang
 * 
 */
public class GameComponent extends AbstractComponent
{
    /** 游戏帧时间 */
    public static final int GAME_UPDATE_INTERVAL = 1000 / 25;

    /** game检查时间间隔 */
    public static final int CHECK_DELAY_INTERVAL = 2;

    private static class GameThread extends Thread
    {
        private Runnable runnable;

        public GameThread(String name)
        {
            super(name);
        }

        public Runnable getRunnable()
        {
            return GameComponent.getReadyGame();
        }

        @Override
        public void run()
        {
            while (GameComponent.isRunning())
            {
                runnable = getRunnable();

                try
                {
                    if (this.runnable != null)
                        this.runnable.run();
                    else
                        Thread.sleep(1);
                }
                catch (Exception e)
                {
                    LogFactory.error("", e);
                }
            }
        }
    }

    private static ConcurrentHashMap<Integer, AbstractGame> games;

    /** 执行时间未到的列表 */
    private static List<AbstractGame> delayList = new ArrayList<>();
    /** 执行时间未到的列表Temp */
    private static List<AbstractGame> tempList = new ArrayList<>();
    /** 执行列表 */
    private static ConcurrentLinkedQueue<AbstractGame> runQueue = new ConcurrentLinkedQueue<AbstractGame>();

    private static Thread bossExecutor;

    private static Thread[] workerExecutor;

    private static volatile boolean isRunning = true;

    private static AtomicInteger gameId;

    public static boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public boolean initialize()
    {
        // 游戏驱动专用线程池
        int workerSize = Runtime.getRuntime().availableProcessors() * 4 + 1;
        bossExecutor = new Thread(() -> checkGameDelay(), "game-boss-pool");
        workerExecutor = new Thread[workerSize];

        games = new ConcurrentHashMap<Integer, AbstractGame>();
        gameId = new AtomicInteger(0);

        return true;
    }

    public boolean start()
    {
        for (int i = 0; i < workerExecutor.length; i++)
        {
            workerExecutor[i] = new GameThread("game-worker-pool");
            workerExecutor[i].start();
        }

        bossExecutor.start();

        return true;
    }

    public void stop()
    {
        isRunning = false;
        workerExecutor = null;
    }

    /**
     * 添加到执行列表
     * 
     * @param game
     */
    public static void submit(AbstractGame game)
    {
        runQueue.offer(game);
    }

    /**
     * 添加到执行列表
     * 
     * @param game
     */
    private static void submit(List<AbstractGame> list)
    {
        runQueue.addAll(list);
    }

    public static AbstractGame getReadyGame()
    {
        return runQueue.poll();
    }

    public static void submitDelay(AbstractGame game)
    {
        synchronized (delayList)
        {
            delayList.add(game);
        }
    }

    private static void checkGameDelay()
    {
        while (isRunning)
        {
            try
            {
                long time = System.currentTimeMillis();
                int count = 0;

                if (delayList.size() > 0)
                {
                    synchronized (delayList)
                    {
                        AbstractGame game = null;
                        int result = 0;
                        count = delayList.size();

                        for (int i = delayList.size() - 1; i >= 0; i--)
                        {
                            game = delayList.get(i);
                            result = game.canExecute();
                            if (result == 1)
                            {
                                tempList.add(game);
                                delayList.remove(i);
                            }

                            if (result == 0)
                                delayList.remove(i);
                        }
                    }

                    if (tempList.size() > 0)
                    {
                        submit(tempList);
                        tempList.clear();
                    }
                }

                time = System.currentTimeMillis() - time;

                if (time < CHECK_DELAY_INTERVAL)
                {
                    Thread.sleep(CHECK_DELAY_INTERVAL - time);
                }
                else
                {
                    if (time > 20)
                    {
                        LogFactory.warn("******************GameComponent Check Game Delay Spend Too Much Time --- time:{},count:{}", time, count);
                    }
                }
            }
            catch (Exception e)
            {
                LogFactory.error("GameComponent Exception:", e);
            }
        }
    }

    /**
     * 获取自增id，使用原子数据类型，无需同步
     * 
     * @return id
     */
    public static int getGameId()
    {
        return gameId.getAndIncrement();
    }

    /**
     * 获取所有游戏对象
     * 
     * @return
     */
    public static List<AbstractGame> getAllGame()
    {
        List<AbstractGame> list = new ArrayList<AbstractGame>(games.values());
        return list;
    }

    /**
     * 添加游戏
     * 
     * @param game
     */
    public static void addGame(AbstractGame game)
    {
        if (game != null)
        {
            games.put(game.getGameID(), game);
            submit(game);
        }
    }

    /**
     * 移除游戏
     * 
     * @param game
     *            游戏对象
     */
    public static void removeGame(AbstractGame game)
    {
        if (game != null)
        {
            games.remove(game.getGameID());
        }
    }

    /**
     * 开启多人副本游戏
     * 
     * @param gameType
     * @param red
     * @param missionID
     * @return
     */
    public static PVPGame startPvPGame(int missionType, ModeType modeType, List<AbstractGamePlayer> players, List<PlatformGroupInfoProtoOut> map)
    {
        try
        {
            PVPGame game = new PVPGame(getGameId(), missionType, modeType);
            if (game.init(map, players))
                addGame(game);
            else
                return null;

            return game;
        }
        catch (Exception e)
        {
            LogFactory.error("create game error:", e);
        }

        return null;
    }

}
