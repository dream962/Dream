package com.game.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.base.component.AbstractComponent;
import com.base.net.CommonMessage;
import com.data.business.PlayerBusiness;
import com.data.info.PlayerInfo;
import com.data.log.ResourceLog;
import com.data.log.factory.ResourceLogFactory;
import com.game.object.player.GamePlayer;
import com.google.protobuf.GeneratedMessage.Builder;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 玩家管理组件
 * 
 * @author dream
 *
 */
public class GamePlayerComponent extends AbstractComponent
{
    private static Map<Long, GamePlayer> gamePlayerMap = new HashMap<>();

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static List<ResourceLog> resourceList = new ArrayList<>();

    public static void addPlayer(long userID, GamePlayer player)
    {
        lock.writeLock().lock();
        try
        {
            gamePlayerMap.put(userID, player);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    public static void removePlayer(long userID)
    {
        lock.writeLock().lock();
        try
        {
            gamePlayerMap.remove(userID);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
     * 判断玩家是否在线
     * 
     * @param userID
     * @return
     */
    public static boolean isOnline(long userID)
    {
        GamePlayer player = getPlayerByUserID(userID);

        if (player != null)
        {
            return player.getNetworkModule().isConnect();
        }

        return false;
    }

    /**
     * 获取所有玩家数量
     * 
     * @return
     */
    public static int getPlayerCount()
    {
        return gamePlayerMap.size();
    }

    public static List<GamePlayer> getAllPlayer()
    {
        lock.readLock().lock();
        try
        {
            return new ArrayList<>(gamePlayerMap.values());
        }
        finally
        {
            lock.readLock().unlock();
        }
    }

    public static GamePlayer getPlayerByUserID(long userID)
    {
        GamePlayer player = null;

        lock.readLock().lock();
        try
        {
            player = gamePlayerMap.get(userID);
        }
        finally
        {
            lock.readLock().unlock();
        }
        return player;
    }

    @Override
    public boolean initialize()
    {
        return true;
    }

    @Override
    public void stop()
    {
        List<GamePlayer> list = new ArrayList<>();
        list.addAll(gamePlayerMap.values());

        // 踢除玩家
        for (GamePlayer p : list)
        {
            p.getSenderModule().sendKickPlayer();
        }

        list.forEach(p -> {
            p.disconnect(p.getNetworkModule().getClientConnection(), true);
            p.save();
        });

        LogFactory.info("gameplayer disconnect. count" + list.size());

        gamePlayerMap.clear();
    }

    /**
     * 定时保存
     */
    public static void save()
    {
        try
        {
            List<GamePlayer> list = getAllPlayer();
            long time = System.currentTimeMillis();

            for (GamePlayer player : list)
            {
                player.jobSave();
            }

            List<ResourceLog> resourceLogs = new ArrayList<>();
            synchronized (resourceList)
            {
                resourceLogs.addAll(resourceList);
                resourceList.clear();
            }

            ResourceLogFactory.getDao().addOrUpdateBatch(resourceLogs);

            if (System.currentTimeMillis() - time > 5000)
            {
                LogFactory.warn(String.format("GamePlayerComponent : save player spend too much time -- %d", System.currentTimeMillis() - time));
            }
        }
        catch (Exception e)
        {
            LogFactory.error("GamePlayerComponent Exception:", e);
        }
    }

    public static void sendToAll(int code, Builder<?> builder, GamePlayer except)
    {
        List<GamePlayer> list = getAllPlayer();
        for (GamePlayer player : list)
        {
            if (player != except && player.getNetworkModule().isConnect())
                player.sendMessage(code, builder);
        }
    }

    public static void sendToAll(CommonMessage msg, GamePlayer except)
    {
        List<GamePlayer> list = getAllPlayer();
        for (GamePlayer player : list)
        {
            if (player != except)
                player.sendMessage(msg);
        }
    }

    public static void addResourceLog(ResourceLog log)
    {
        synchronized (resourceList)
        {
            resourceList.add(log);
        }
    }

    public static boolean deletePlayer(int userID)
    {
        try
        {
            GamePlayer player = GamePlayerComponent.getPlayerByUserID(userID);
            if (player != null)
            {
                removePlayer(userID);
                player.disconnect(player.getNetworkModule().getClientConnection(), true);
            }

            RankComponent.removePlauer(userID);

            PlayerBusiness.removePlayer(userID);
            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
            return false;
        }
    }

    public static List<PlayerInfo> getPlayerByName(String name)
    {
        List<PlayerInfo> list = new ArrayList<>();

        lock.readLock().lock();
        try
        {
            for (GamePlayer player2 : gamePlayerMap.values())
            {
                if (StringUtil.isNumber(name))
                {
                    if (player2.getUserID() == Integer.valueOf(name))
                    {
                        list.add(player2.getPlayerInfo());
                    }
                }
                else
                {
                    if (player2.getNickName().equalsIgnoreCase(name) ||
                            (player2.getPlayerInfo().getAccountName() != null && player2.getPlayerInfo().getAccountName().equalsIgnoreCase(name)) ||
                            (player2.getPlayerInfo().getAccuntGName() != null && player2.getPlayerInfo().getAccuntGName().equalsIgnoreCase(name)))
                    {
                        list.add(player2.getPlayerInfo());
                    }
                }
            }
        }
        finally
        {
            lock.readLock().unlock();
        }

        List<PlayerInfo> playerInfos = PlayerBusiness.getPlayerInfoByName(name);
        if (playerInfos != null)
        {
            list.addAll(playerInfos);
        }
        return list;
    }
}
