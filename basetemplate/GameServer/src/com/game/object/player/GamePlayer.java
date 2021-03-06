package com.game.object.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.base.net.client.IClientConnection;
import com.data.bag.ItemAddType;
import com.data.bag.ItemRemoveType;
import com.data.business.PlayerBusiness;
import com.data.component.GamePropertiesComponent;
import com.data.log.ResourceLog;
import com.data.type.ResourceType;
import com.game.component.GamePlayerComponent;
import com.game.module.PlayerCmdQueueModule;
import com.game.module.PlayerDataModule;
import com.game.module.PlayerEventModule;
import com.game.module.PlayerPingModule;
import com.game.module.PlayerScriptEndModule;
import com.game.module.PlayerSenderModule;
import com.logic.component.RoomComponent;
import com.logic.object.AbstractGamePlayer;
import com.logic.object.AbstractPlayerModuleType;
import com.logic.object.module.AbstractPlayerModule;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.common.gen.CommonOutMsg.RankType;
import com.util.StringUtil;
import com.util.print.LogFactory;

/**
 * 游戏玩家主类
 *
 * @author dream
 *
 */
public class GamePlayer extends AbstractGamePlayer
{
    /** 保存时间，每个玩家的不同，每隔一定时间同步一次 */
    private long saveTime = 0;

    /** 离线保存时间间隔 */
    private long offlineSaveTime = 0;

    /*********************************************** 模块 ************************************************************/

    /**
     * 初始化模块
     */
    @Override
    protected void initModule()
    {
        super.initModule();

        moduleMap.put(AbstractPlayerModuleType.CMD_QUEUE.getValue(), new PlayerCmdQueueModule(this));
        moduleMap.put(AbstractPlayerModuleType.PROXY_SENDER.getValue(), new PlayerSenderModule(this));
        moduleMap.put(AbstractPlayerModuleType.EVENT.getValue(), new PlayerEventModule(this));

        moduleMap.put(AbstractPlayerModuleType.PING.getValue(), new PlayerPingModule(this));
        moduleMap.put(AbstractPlayerModuleType.DATA.getValue(), new PlayerDataModule(this));

        // 最后加载
        moduleMap.put(AbstractPlayerModuleType.SCRIPT.getValue(), new PlayerScriptEndModule(this));
    }

    public PlayerEventModule getEventModule()
    {
        return getModule(AbstractPlayerModuleType.EVENT);
    }

    public PlayerCmdQueueModule getCmdQueueModule()
    {
        return getModule(AbstractPlayerModuleType.CMD_QUEUE);
    }

    public PlayerSenderModule getSenderModule()
    {
        return getModule(AbstractPlayerModuleType.PROXY_SENDER);
    }

    public PlayerPingModule getPingModule()
    {
        return getModule(AbstractPlayerModuleType.PING);
    }

    public PlayerDataModule getDataModule()
    {
        return getModule(AbstractPlayerModuleType.DATA);
    }

    /*********************************************** 系统 **********************************************************/

    /**
     * 定时保存，区分在线和离线，而且每个玩家的保存时间不同,保存到数据库
     */
    public void jobSave()
    {
        long time = System.currentTimeMillis();
        if (getNetworkModule().isConnect())
        {
            if (time >= saveTime)
                saveTime = time + GamePropertiesComponent.SAVE_PLAYER_TIME * 1000;
            else
                return;
        }
        else
        {
            if (time >= offlineSaveTime)
                offlineSaveTime = time + GamePropertiesComponent.SAVE_PLAYER_TIME * 5000;
            else
                return;
        }

        save();
    }

    public boolean save()
    {
        try
        {
            for (AbstractPlayerModule<?> module : moduleMap.values())
            {
                try
                {
                    if (!module.save())
                    {
                        LogFactory.error("GamePlayer:saveModuleData() error. module:" + module.getClass().getName());
                        return false;
                    }
                }
                catch (Exception e)
                {
                    LogFactory.error("", e);
                }
            }

            PlayerBusiness.addOrUpdatePlayer(playerInfo);
        }
        catch (Exception e)
        {
            LogFactory.error("保存玩家数据失败", e);
            return false;
        }

        return true;
    }

    /**
     * 午夜刷新重置玩家模块信息
     * 强制刷新
     */
    public void refresh()
    {
        try
        {
            for (AbstractPlayerModule<?> module : moduleMap.values())
            {
                module.refresh(true);
            }

            playerInfo.setLastLoginTime(new Date());
            save();
        }
        catch (Exception e)
        {
            LogFactory.error("GamePlayer Refresh Exception.userID:" + getUserID(), e);
        }
    }

    /**
     * 是否是强制断线
     *
     * @param isForceKick
     */
    @Override
    public void disconnect(IClientConnection conn, boolean isForceKick)
    {
        try
        {
            IClientConnection client = getNetworkModule().getClientConnection();
            if (client != null)
            {
                client.setHolder(null);
                client.setClientID(0);
                client.closeConnection(true);
                client = null;
            }

            // 玩家离线后处理
            for (AbstractPlayerModule<?> module : moduleMap.values())
            {
                module.disconnect();
            }

            // 移除匹配
            RoomComponent.removeMatchPlayer(this);
            // 退出房间
            getRoomModule().disconnectRoom();

            save();
        }
        catch (Exception e)
        {
            LogFactory.error("catch error when user quit:", e);
        }
        finally
        {
            GamePlayerComponent.removePlayer(getUserID());

            System.err.println("玩家退出." + getUserID() + "," + getNickName());
        }
    }

    private boolean load()
    {
        for (AbstractPlayerModule<?> module : moduleMap.values())
        {
            try
            {
                if (!module.load())
                {
                    LogFactory.error(module.getClass().getName() + " module load data from db error.");
                    return false;
                }
            }
            catch (Exception e)
            {
                LogFactory.error(module.getClass().getName() + " module load data from db error:", e);
                return false;
            }
        }
        return true;
    }

    public boolean login()
    {
        if (load())
        {
            relogin();
            return true;
        }

        return false;
    }

    public void relogin()
    {
        for (AbstractPlayerModule<?> module : moduleMap.values())
        {
            try
            {
                module.relogin();
            }
            catch (Exception e)
            {
                LogFactory.error("Module Relogin Exception:" + module.getClass().getName(), e);
            }
        }

        playerInfo.setLastLoginTime(new Date());

        getPingModule().send();
        getSenderModule().sendLoginSuccess();

        for (AbstractPlayerModule<?> module : moduleMap.values())
        {
            try
            {
                module.send();
            }
            catch (Exception e)
            {
                LogFactory.error("Module Relogin Exception:" + module.getClass().getName(), e);
            }
        }

        LogFactory.error("玩家登陆完成:{},{}", getUserID(), getNickName());

        // 保存时间初始化
        saveTime = System.currentTimeMillis() + GamePropertiesComponent.SAVE_PLAYER_TIME;
    }

    public int getResource(int type)
    {
        if (type == ResourceType.COIN.getValue())
        {
            return getPlayerInfo().getGold();

        }

        if (type == ResourceType.TTQ.getValue())
        {
            return getPlayerInfo().getMoney();
        }

        return 0;
    }

    public boolean checkResource(int type, int count)
    {
        if (type == ResourceType.COIN.getValue())
        {
            if (getPlayerInfo().getGold() >= count)
                return true;

            return false;
        }

        if (type == ResourceType.TTQ.getValue())
        {
            if (getPlayerInfo().getMoney() >= count)
                return true;

            return false;
        }

        return false;
    }

    public void addResource(int itemID, int count, ItemAddType type)
    {
        if (itemID == ResourceType.COIN.getValue())
        {
            getPlayerInfo().setGold(getPlayerInfo().getGold() + count);
        }

        if (itemID == ResourceType.TTQ.getValue())
        {
            getPlayerInfo().setMoney(getPlayerInfo().getMoney() + count);
        }

        ResourceLog log = new ResourceLog();
        log.setAddTime(new Date());
        log.setItemCount(count);
        log.setUserID(getUserID());
        log.setItemID(itemID);
        log.setAddType(type.getValue());

        GamePlayerComponent.addResourceLog(log);
    }

    public void addResource(int itemID, int count, int type)
    {
        if (itemID == ResourceType.COIN.getValue())
        {
            getPlayerInfo().setGold(getPlayerInfo().getGold() + count);
        }

        if (itemID == ResourceType.TTQ.getValue())
        {
            getPlayerInfo().setMoney(getPlayerInfo().getMoney() + count);
        }

        ResourceLog log = new ResourceLog();
        log.setAddTime(new Date());
        log.setItemCount(count);
        log.setUserID(getUserID());
        log.setItemID(itemID);
        log.setAddType(type);

        GamePlayerComponent.addResourceLog(log);
    }

    public void removeResource(int itemID, int count, ItemRemoveType type)
    {
        if (itemID == ResourceType.COIN.getValue())
        {
            int gold = getPlayerInfo().getGold() - count;
            gold = gold < 0 ? 0 : gold;
            getPlayerInfo().setGold(gold);
        }

        if (itemID == ResourceType.TTQ.getValue())
        {
            int money = getPlayerInfo().getMoney() - count;
            money = money < 0 ? 0 : money;
            getPlayerInfo().setMoney(money);
        }

        ResourceLog log = new ResourceLog();
        log.setAddTime(new Date());
        log.setItemCount(count);
        log.setUserID(getUserID());
        log.setItemID(itemID);
        log.setAddType(-type.getValue());

        GamePlayerComponent.addResourceLog(log);
    }

    public void addGameModeType(int type)
    {
        Set<Integer> set = StringUtil.splitInt(getPlayerInfo().getModes(), "\\,");
        if (set.contains(type))
            return;

        String modes = getPlayerInfo().getModes() + type + ",";
        getPlayerInfo().setModes(modes);
    }

    public List<Integer> getGameModeTypes()
    {
        List<Integer> list = new ArrayList<>();
        list.addAll(StringUtil.splitInt(getPlayerInfo().getModes(), "\\,"));
        return list;
    }

    public void addRoleType(int type)
    {
        Set<Integer> set = StringUtil.splitInt(getPlayerInfo().getRoleTypes(), "\\,");
        if (set.contains(type))
            return;

        String modes = getPlayerInfo().getRoleTypes() + type + ",";
        getPlayerInfo().setRoleTypes(modes);
    }

    public List<Integer> getRoleTypes()
    {
        List<Integer> list = new ArrayList<>();
        list.addAll(StringUtil.splitInt(getPlayerInfo().getRoleTypes(), "\\,"));
        return list;
    }

    public void addThemeType(int type)
    {
        Set<Integer> set = StringUtil.splitInt(getPlayerInfo().getTheme(), "\\,");
        if (set.contains(type))
            return;

        String modes = getPlayerInfo().getTheme() + type + ",";
        getPlayerInfo().setTheme(modes);
    }

    public List<Integer> getThemeTypes()
    {
        List<Integer> list = new ArrayList<>();
        list.addAll(StringUtil.splitInt(getPlayerInfo().getTheme(), "\\,"));
        return list;
    }

    public void addHeader(int headerID)
    {
        Set<Integer> set = StringUtil.splitInt(getPlayerInfo().getHeaders(), "\\,");
        if (set.contains(headerID))
            return;

        String modes = getPlayerInfo().getHeaders() + headerID + ",";
        getPlayerInfo().setHeaders(modes);
    }

    public List<Integer> getHeaders()
    {
        List<Integer> list = new ArrayList<>();
        list.addAll(StringUtil.splitInt(getPlayerInfo().getHeaders(), "\\,"));
        return list;
    }

    @Override
    public void onGameOver(ModeType type, int value, int diamondCount, int ttqCount)
    {
        switch (type)
        {
        case RAC:
            if (value > 0 && (value < playerInfo.getRaceTopScore() || playerInfo.getRaceTopScore() <= 0))
            {
                playerInfo.setRaceTopScore(value);
                getSenderModule().sendRes();
            }
            break;
        case TimeLimit:
            if (value > playerInfo.getTimeTopScore())
            {
                playerInfo.setTimeTopScore(value);
                getSenderModule().sendRes();
            }
            break;
        case EndLess:
            if (value > playerInfo.getEndTopScore())
            {
                playerInfo.setEndTopScore(value);
                getSenderModule().sendRes();
            }
            break;
        case Room:
        case Room2:
        case Room3:
            // 成功加奖励
            if (value == 1)
            {
                getRoomModule().setWinCount(getRoomModule().getWinCount() + 1);

                getPlayerInfo().setWinCount(getPlayerInfo().getWinCount() + 1);

                int diamond = getPlayerInfo().getGold() + diamondCount;
                getPlayerInfo().setGold(diamond);

                int ttq = getPlayerInfo().getMoney() + ttqCount;
                getPlayerInfo().setMoney(ttq);

                // 排行数据
                getDataModule().addRank(RankType.Net, 1);
            }
            else
            {
                getPlayerInfo().setFailCount(getPlayerInfo().getFailCount() + 1);
            }
            break;
        case RandomMatch:
        case RandomMatch3:
        case RandomMatch2:
            // 成功加奖励
            if (value == 1)
            {
                getRoomModule().setWinCount(getRoomModule().getWinCount() + 1);

                getPlayerInfo().setWinCount(getPlayerInfo().getWinCount() + 1);

                int diamond = getPlayerInfo().getGold() + diamondCount;
                getPlayerInfo().setGold(diamond);

                int ttq = getPlayerInfo().getMoney() + ttqCount;
                getPlayerInfo().setMoney(ttq);

                // 排行数据
                getDataModule().addRank(RankType.Net, 1);
            }
            else
            {
                getPlayerInfo().setFailCount(getPlayerInfo().getFailCount() + 1);
            }
            break;
        default:
            break;
        }

        getSenderModule().sendRes();
    }
}
