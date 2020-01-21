package com.logic.game;

import java.util.List;

import com.base.net.CommonMessage;
import com.data.bean.NetRewardBean;
import com.data.bean.factory.NetRewardBeanFactory;
import com.data.type.ResourceType;
import com.logic.actions.CheckPVPGameStateAction;
import com.logic.actions.ProcessPacketAction;
import com.logic.component.GameComponent;
import com.logic.eventargs.GameOverEventArg;
import com.logic.living.Player;
import com.logic.object.AbstractGamePlayer;
import com.logic.type.CampColorType;
import com.logic.type.GameEventType;
import com.logic.type.GameStateType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.common.gen.CommonOutMsg.ModeType;
import com.proto.user.gen.UserOutMsg.GameOverProtoOut;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

/**
 * PVP游戏逻辑
 */
public class PVPGame extends AbstractGame
{
    private static final int FINISH_TIME = 20 * 60 * 1000;

    private static final long ROBOT_TIME = 100L;

    private boolean isRobot = false;

    private long lastRobotTime = System.currentTimeMillis();

    private ModeType modeType;

    public PVPGame(int id, int gameType, ModeType modeType)
    {
        super(id, gameType);
        this.modeType = modeType;
    }

    public boolean init(List<PlatformGroupInfoProtoOut> mapList, List<AbstractGamePlayer> players)
    {
        mapList.forEach(p -> {
            map.put(p.getLevel(), p);
        });

        int index = 0;
        for (AbstractGamePlayer gamePlayer : players)
        {
            if (gamePlayer.getIsRobot())
                isRobot = true;

            index++;
            if (index % 2 == 0)
            {
                Player player = new Player(index, CampColorType.RED, this, gamePlayer);
                addPlayer(player);
            }
            else
            {
                Player player = new Player(index, CampColorType.BLUE, this, gamePlayer);
                addPlayer(player);
            }
        }

        return true;
    }

    @Override
    public void checkState(int delay)
    {
        addAction(new CheckPVPGameStateAction(delay));
    }

    @Override
    public boolean checkGameOver()
    {
        List<Player> players = getAllPlayers();

        // 玩家都离开,结束
        if (players.size() <= 0)
            return true;

        // 有玩家完成,结束
        for (Player p : players)
        {
            if (false == p.getGamePlayer().getIsRobot())
            {
                if (p.getGamePlayer().getNetworkModule().isConnect())
                {
                }
            }

            // 如果有玩家投降
            if (p.isSurrender() == true)
            {
                // 当前玩家失败
                p.setOverTime(System.currentTimeMillis());
                p.setSuccess(false);

                // 另一个玩家胜利
                for (Player blue : players)
                {
                    if (p != blue)
                    {
                        blue.setOverTime(System.currentTimeMillis());
                        blue.setSuccess(true);
                        return true;
                    }
                }

                return true;
            }

            if (p.isOver() == true)
            {
                p.setOverTime(System.currentTimeMillis());
                p.setSuccess(true);
                return true;
            }
        }

        // 如果玩家掉线,结束游戏
        // if (isPlayerConnect == false)
        // return true;

        // 时间到
        if (System.currentTimeMillis() - startTime >= FINISH_TIME)
            return true;

        return false;
    }

    /**
     * 数据包同步
     */
    @Override
    public void processData(CommonMessage packet)
    {
        Player player = getPlayerByUserID(packet.getParam());
        if (player != null)
        {
            addAction(new ProcessPacketAction(player, packet));
        }
        else
        {
            LogFactory.error("Player is Exist. Cmd:" + packet.getCode());
        }
    }

    @Override
    public boolean isAllComplete()
    {
        boolean result = true;
        if (gameState == GameStateType.Loading)
        {
            List<Player> list = getAllPlayers();
            for (Player player : list)
            {
                if (!player.getGamePlayer().getIsRobot() && player.getProgress() < 100)
                {
                    result = false;
                    break;
                }
            }

            // 时间超过1分钟开始
            if (loadingStartTime > 0 && System.currentTimeMillis() - loadingStartTime > 60000)
            {
                result = true;
            }
        }
        return result;
    }

    @Override
    public void prepare()
    {
        if (gameState == GameStateType.Inited)
        {
            gameState = GameStateType.Prepare;
            PrintFactory.error("Game Prepare.ID:%s", getGameID());
        }
    }

    @Override
    public void loading()
    {
        if (gameState == GameStateType.Prepare)
        {
            gameState = GameStateType.Loading;
            loadingStartTime = System.currentTimeMillis();
            PrintFactory.error("Game Loading.ID:%s", getGameID());
        }
    }

    @Override
    public void start()
    {
        if (gameState == GameStateType.Loading)
        {
            resume();
            startTime = System.currentTimeMillis();
            frameTime = 1;

            gameState = GameStateType.Playing;
            // AI延迟1秒执行 + 正式开始前等待3秒
            lastRobotTime = System.currentTimeMillis() + 4000;

            sendToAll(null, UserCmdOutType.START_GAME_VALUE);
            PrintFactory.error("Game Start.ID:%s", getGameID());
        }
    }

    @Override
    public void gameover()
    {
        gameState = GameStateType.GameOver;

        List<Player> players = getAllPlayers();

        PrintFactory.error("**************游戏结束.*********************");

        // 计算金币数量
        int diamond = getDiamondMap().size();
        int donut = 0;

        Player winner = null;
        Player loser = null;

        boolean isSurrender = false;
        for (Player player : players)
        {
            if (player.isSuccess())
                winner = player;
            else
                loser = player;

            if (player.isSurrender())
            {
                isSurrender = true;
            }
        }

        // 如果没有投降的玩家，计算系统奖励
        if (isSurrender == false)
        {
            NetRewardBean rewardBean = NetRewardBeanFactory.getNetRewardBean(gameType);
            if (rewardBean != null)
            {
                if (rewardBean.getItemID1() == ResourceType.COIN.getValue())
                {
                    diamond += rewardBean.getItemCount1();
                }
                else if (rewardBean.getItemID1() == ResourceType.TTQ.getValue())
                {
                    donut += rewardBean.getItemCount1();
                }
                if (rewardBean.getItemID2() == ResourceType.COIN.getValue())
                {
                    diamond += rewardBean.getItemCount2();
                }
                else if (rewardBean.getItemID2() == ResourceType.TTQ.getValue())
                {
                    donut += rewardBean.getItemCount2();
                }
            }
        }

        // 胜利者更新数据
        if (winner != null)
            winner.getGamePlayer().onGameOver(modeType, 1, diamond, donut);
        if (loser != null)
            loser.getGamePlayer().onGameOver(modeType, 0, 0, 0);

        // 同步消息
        for (Player player : players)
        {
            GameOverProtoOut.Builder builder = GameOverProtoOut.newBuilder();
            builder.setPlayerWinCount(player.getGamePlayer().getRoomModule().getWinCount());
            builder.setWinnerGetDiamondCount(diamond);
            builder.setWinnerGetDonutCount(donut);

            if (player == winner)
            {
                builder.setIsWin(true);
                if (loser != null)
                    builder.setEnemyWinCount(loser.getGamePlayer().getRoomModule().getWinCount());
                else
                    builder.setEnemyWinCount(0);
            }

            if (player == loser)
            {
                builder.setIsWin(false);

                if (winner != null)
                    builder.setEnemyWinCount(winner.getGamePlayer().getRoomModule().getWinCount());
                else
                    builder.setEnemyWinCount(0);
            }

            player.sendMessage(builder, UserCmdOutType.GAME_OVER_VALUE);
        }

        getEvent().notifyListeners(new GameOverEventArg(GameEventType.GameOver.getValue()));
    }

    @Override
    public void stop()
    {
        try
        {
            gameState = GameStateType.Stopped;
            getEvent().notifyListeners(new GameOverEventArg(GameEventType.GameStop.getValue()));
            GameComponent.removeGame(this);
        }
        catch (Exception exception)
        {
            LogFactory.error("Game Stop Error:", exception);
        }
    }

    @Override
    public void exception()
    {
        try
        {
            gameState = GameStateType.Stopped;
            GameComponent.removeGame(this);
        }
        catch (Exception exception)
        {
            LogFactory.error("Game Stop Error:", exception);
        }
    }

    @Override
    protected void update(long tick, int interval)
    {
        super.update(tick, interval);

        if (gameState == GameStateType.Playing)
        {
            if (isRobot && tick - lastRobotTime >= ROBOT_TIME)
            {
                lastRobotTime = tick;
                List<Player> players = getAllPlayers();
                for (Player p : players)
                {
                    if (p.getGamePlayer().getIsRobot())
                        p.jump(tick);
                }
            }
        }
    }
}
