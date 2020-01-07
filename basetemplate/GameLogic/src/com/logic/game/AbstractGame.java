package com.logic.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.event.EventSource;
import com.base.net.CommonMessage;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.actions.IAction;
import com.logic.component.GameComponent;
import com.logic.living.Player;
import com.logic.type.GameStateType;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.util.print.LogFactory;

/**
 * 抽象游戏类
 * 
 * @author dream
 *
 */
public abstract class AbstractGame implements Runnable
{
    protected EventSource eventSource;

    protected int gameID;

    protected int gameType;

    protected int missionID;

    private int currentActionCount;

    private long executeTime;

    /** 当前游戏时间 */
    private long tick;

    /** 游戏总执行的帧数 */
    protected int frameTime;

    /** 游戏开始时间 */
    protected long startTime;

    /** 上次执行时间 */
    private long lastExcTime;

    /** 暂停时间 */
    private long pauseTime;

    /** 游戏状态 */
    protected GameStateType gameState;

    /** 加载开始时间 */
    protected long loadingStartTime;

    /** 《PlayerID,Player》 */
    protected HashMap<Integer, Player> players;

    /** 游戏action */
    private List<IAction> actions;

    /** 地图信息 */
    protected Map<Integer, PlatformGroupInfoProtoOut> map = new HashMap<>();

    /** 已经获得的钻石《level,userID》 */
    private Map<Integer, Integer> getDiamondMap = new HashMap<Integer, Integer>();

    public AbstractGame(int id, int gameType)
    {
        this.gameID = id;
        this.eventSource = new EventSource();
        this.gameType = gameType;
        this.gameState = GameStateType.Inited;
        this.players = new HashMap<>(0);
        this.actions = new ArrayList<>(0);
        this.currentActionCount = 0;
        this.frameTime = 0;
        this.executeTime = 0;
        this.pauseTime = 0;
    }

    public Map<Integer, Integer> getDiamondMap()
    {
        return getDiamondMap;
    }

    public Map<Integer, PlatformGroupInfoProtoOut> getMap()
    {
        return map;
    }

    public abstract void checkState(int delay);

    public abstract boolean checkGameOver();

    public abstract boolean isAllComplete();

    public abstract void processData(CommonMessage packet);

    /**************************************************************************************************************/

    public abstract void loading();

    public abstract void prepare();

    public abstract void start();

    public abstract void gameover();

    public abstract void stop();

    public abstract void exception();

    /**************************************************************************************************************/

    public EventSource getEvent()
    {
        return this.eventSource;
    }

    public int getActionCount()
    {
        return currentActionCount;
    }

    public GameStateType getGameState()
    {
        return gameState;
    }

    public long getStartTime()
    {
        return startTime;
    }

    /**
     * 游戏类型
     * 
     * @return
     */
    public int getGameType()
    {
        return this.gameType;
    }

    /**
     * 帧时间
     * 
     * @return
     */
    public int getFrameTime()
    {
        return frameTime;
    }

    /**
     * 游戏时间
     * 
     * @return
     */
    public long getTick()
    {
        return tick;
    }

    /**
     * 游戏总执行时间(从开始0到此刻的毫秒数)
     * 
     * @return
     */
    public long getGameTime()
    {
        return tick - startTime;
    }

    /**
     * 计算时间转帧率(秒)
     * 
     * @param time
     * @return
     */
    public int calTimeToFrame(float time)
    {
        return (int) (time * 1000 / GameComponent.GAME_UPDATE_INTERVAL);
    }

    /**
     * 计算帧率转时间(毫秒)
     * 
     * @param frame
     * @return
     */
    public float calFrameToTime(int frame)
    {
        return frame * GameComponent.GAME_UPDATE_INTERVAL;
    }

    public int getGameID()
    {
        return gameID;
    }

    public void addAction(IAction action)
    {
        if (action == null)
            return;

        synchronized (this.actions)
        {
            this.actions.add(action);
        }
    }

    public void addAction(List<IAction> actions)
    {
        if (actions == null || actions.size() < 1)
            return;

        synchronized (this.actions)
        {
            this.actions.addAll(actions);
        }
    }

    /*********************************************************************************************************/

    public Player getPlayerByUserID(long userID)
    {
        for (Player p : players.values())
        {
            if (p.getUserID() == userID)
                return p;
        }

        return null;
    }

    public List<Player> getAllPlayers()
    {
        List<Player> list = new ArrayList<Player>();
        list.addAll(this.players.values());
        return list;
    }

    public void addPlayer(Player player)
    {
        players.put(player.getPlayerID(), player);
    }

    public Player removePlayer(long userID)
    {
        for (Player p : players.values())
        {
            if (p.getUserID() == userID)
            {
                return players.remove(p.getPlayerID());
            }
        }

        return null;
    }

    /*********************************************************************************************************/

    public void resume()
    {
        pauseTime = 0;
    }

    public void pause(int delay)
    {
        pauseTime = Math.max(pauseTime, System.currentTimeMillis() + delay);
    }

    /**
     * 判断是否可以执行
     * 
     * @param current
     * @return 0：无效 ---1：执行 ------2：等待
     */
    public int canExecute()
    {
        if (gameState == GameStateType.Stopped)
            return 0;

        if (executeTime - System.currentTimeMillis() <= 3)
            return 1;
        else
            return 2;
    }

    @Override
    public void run()
    {
        if (gameState == GameStateType.Stopped)
            return;

        tick = System.currentTimeMillis();

        if (lastExcTime == 0)
            lastExcTime = tick;

        this.currentActionCount = 0;

        frameTime++;

        try
        {
            if (pauseTime < tick)
            {
                List<IAction> tempAction = new ArrayList<>();

                synchronized (this.actions)
                {
                    if (this.actions.size() > 0)
                    {
                        tempAction.addAll(this.actions);
                        this.actions.clear();
                    }
                }

                if (tempAction.size() > 0)
                {
                    currentActionCount = tempAction.size();
                    ArrayList<IAction> left = new ArrayList<IAction>();

                    for (IAction action : tempAction)
                    {
                        try
                        {
                            action.execute(this, tick);
                            if (action.isFinished(this, tick) == false)
                                left.add(action);
                        }
                        catch (Exception ex)
                        {
                            LogFactory.error("game update error:", ex);
                        }
                    }

                    if (left.size() > 0)
                        addAction(left);

                    tempAction.clear();
                    left.clear();
                }
                else
                {
                    checkState(0);
                }

                update(tick, (int) (tick - lastExcTime));
            }
        }
        catch (Exception e)
        {
            LogFactory.error("game update fail:", e);
        }
        finally
        {
            long tempTime = tick - lastExcTime;
            if (tempTime > 200)
                LogFactory.warn(String.format("Game Run Too Much Time.  GameID-%s  time-%s", gameID, tempTime));

            // 一帧的工作全部完成，设置时间
            lastExcTime = tick;
            long end = System.currentTimeMillis();

            int time = (GameComponent.GAME_UPDATE_INTERVAL - ((int) (end - tick)));

            if (time > 1)
            {
                executeTime = Math.max(executeTime, tick + GameComponent.GAME_UPDATE_INTERVAL);
                GameComponent.submitDelay(this);
            }
            else
            {
                GameComponent.submit(this);
            }
        }
    }

    protected void update(long tick, int interval)
    {

    }

    /**************************************************************************************************************/

    public void sendToAll(CommonMessage pkg, Player except)
    {
        List<Player> all = getAllPlayers();
        for (Player player : all)
        {
            if (player != except)
            {
                player.sendMessage(pkg);
            }
        }
    }

    public void sendToAll(Builder<?> builder, int code, Player except)
    {
        CommonMessage msg = new CommonMessage(code);
        if (builder != null)
            msg.setBody(builder.build().toByteArray());
        sendToAll(msg, except);
    }

    public void sendToAll(Builder<?> builder, int code)
    {
        sendToAll(builder, code, null);
    }

}
