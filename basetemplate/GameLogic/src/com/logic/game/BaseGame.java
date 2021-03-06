package com.logic.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.base.net.CommonMessage;
import com.logic.actions.ProcessPacketAction;
import com.logic.component.GameComponent;
import com.logic.eventargs.GameOverEventArg;
import com.logic.living.Living;
import com.logic.living.Physics;
import com.logic.living.Player;
import com.logic.object.AbstractGamePlayer;
import com.logic.type.GameEventType;
import com.logic.type.GameStateType;
import com.util.MathsUtil;
import com.util.print.LogFactory;
import com.util.wrapper.WrapPoint;

/**
 * 抽象的游戏对象，所有游戏对象的基类
 */
public abstract class BaseGame extends AbstractGame
{
    public BaseGame(int id, int gameType)
    {
        super(id, gameType);
    }

    public boolean init(int missionID)
    {
        return true;
    }

    public boolean init(int missionID, List<AbstractGamePlayer> gamePlayers)
    {
        if (init(missionID))
        {
            return true;
        }

        return false;
    }

    public boolean checkPos(float x, float y)
    {
        return true;
    }

    /**
     * 数据包同步两种方案：
     * 1.找到leader玩家
     * 2.客户端发包param设置livingID
     */
    @Override
    public void processData(CommonMessage packet)
    {
        long userID = packet.getParam();
        Player player = getPlayerByUserID(userID);

        if (player != null)
        {
            addAction(new ProcessPacketAction(player, packet));
        }
    }

    @Override
    public boolean isAllComplete()
    {
        return true;
    }

    /*********************************************************************************************/

    @Override
    public void prepare()
    {
        if (gameState == GameStateType.Inited)
        {
            sendCreateGame();
            checkState(0);

            gameState = GameStateType.Prepare;
        }
    }

    @Override
    public void loading()
    {
        if (gameState == GameStateType.Prepare)
        {
            sendLoading();
            gameState = GameStateType.Loading;
        }
    }

    @Override
    public void start()
    {
        if (gameState == GameStateType.Loading)
        {
            resume();
            startTime = System.currentTimeMillis();
            frameTime = 0;

            sendStart();

            gameState = GameStateType.Playing;
            eventSource.notifyListeners(GameEventType.GameStart.getValue());
        }
    }

    @Override
    public void gameover()
    {
        gameState = GameStateType.GameOver;
        eventSource.notifyListeners(new GameOverEventArg(GameEventType.GameOver.getValue()));
    }

    @Override
    public void stop()
    {
        try
        {
            gameState = GameStateType.Stopped;
            GameComponent.removeGame(this);

            eventSource.notifyListeners(GameEventType.GameStop.getValue());
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

            eventSource.notifyListeners(GameEventType.GameOver.getValue());
        }
        catch (Exception exception)
        {
            LogFactory.error("Game Stop Error:", exception);
        }
    }

    /*********************************************************************************************/

    /**
     * 检查一个旋转矩形内包含的living
     *
     * @param livings
     * @param w
     * @param h
     * @param angle
     * @param x
     * @param y
     */
    public List<Living> calLivingsInRectangle(List<Living> livings, int w, int h, float angle, int x, int y)
    {
        WrapPoint point1 = MathsUtil.pointAngleDistance(x, y, angle, w);

        int cx = (x + point1.x) / 2;
        int cy = (y + point1.y) / 2;

        WrapPoint point3 = MathsUtil.pointAngleDistance(cx, cy, angle + 90, h);

        List<Living> list = new ArrayList<>();

        for (Living living : livings)
        {
            int distanceH = MathsUtil.pointToLine(point1.x, point1.y, x, y, living.getX(), living.getY());
            int distanceW = MathsUtil.pointToLine(cx, cy, point3.x, point3.y, living.getX(), living.getY());

            if (distanceH > h / 2 || distanceW > w / 2)
                continue;

            list.add(living);
        }

        return list;
    }

    /**
     * 计算扇形内的living
     *
     * @param livings
     *            所有其他living
     * @param sectorX
     *            位置
     * @param sectorY
     *            位置
     * @param radius
     *            半径
     * @param sectorAngle
     *            角度
     * @param dir
     *            方向
     * @return
     */
    public List<Living> calLivingsInSector(List<Living> livings, int sectorX, int sectorY, int radius, int sectorAngle, int dir)
    {
        List<Living> list = new ArrayList<>();

        for (Living living : livings)
        {
            if (sectorAngle >= 360)
            {
                if (MathsUtil.pointInCircle(living.getX(), living.getY(), sectorX, sectorY, radius))
                    list.add(living);
            }
            else
            {
                if (MathsUtil.pointInSector(living.getX(), living.getY(), sectorX, sectorY, radius, sectorAngle, dir))
                    list.add(living);
            }
        }

        return list;
    }

    //    /**
    //     * 计算扇形内的living
    //     *
    //     * @param x
    //     * @param y
    //     * @param dir
    //     * @param radius
    //     * @param sectorAngle
    //     * @return
    //     */
    //    public List<Living> calLivingsInSector(int x, int y, int dir, int radius, int sectorAngle)
    //    {
    //        List<Living> list = new ArrayList<>();
    //
    //        for (Living living : livings)
    //        {
    //            int distance = (x - living.getX()) * (x - living.getX()) + (y - living.getY()) * (y - living.getY());
    //            if (distance <= radius * radius)
    //            {
    //                if (MathsUtil.pointInSector(living.getX(), living.getY(), x, y, radius, sectorAngle, dir))
    //                    list.add(living);
    //            }
    //        }
    //
    //        return list;
    //    }

    /**
     * 计算圆内的living
     *
     * @param livings
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public List<Living> calLivingsCircle(List<Living> livings, int x, int y, float radius)
    {
        List<Living> list = livings.stream().filter(
                p -> (x - p.getX()) * (x - p.getX()) + (y - p.getY()) * (y - p.getY()) <= radius * radius).collect(Collectors.toList());
        return list;
    }

    //    /**
    //     * 计算在圆内的玩家
    //     *
    //     * @param x
    //     * @param y
    //     * @param radius
    //     * @return
    //     */
    //    public List<Player> calPlayersCircle(int x, int y, float radius)
    //    {
    //        List<Player> list = new ArrayList<>();
    //
    //        players.forEach((k, p) -> {
    //            int d = (int) ((x - p.getX()) * (x - p.getX()) + (y - p.getY()) * (y - p.getY()) - radius * radius);
    //            if (d <= 0)
    //                list.add(p);
    //        });
    //
    //        return list;
    //    }

    /*********************************************************************************************/

    public void sendCreateGame()
    {
    }

    public void sendLoading()
    {
    }

    public void sendStart()
    {
    }

    public void sendCreateNpc(Living npc)
    {
    }

    /**
     * 发送测试移动包
     *
     * @param physics
     */
    public void sendTestMove(Physics physics, long time)
    {
    }

}
