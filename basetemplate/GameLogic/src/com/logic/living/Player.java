package com.logic.living;

import java.util.Map;

import com.base.event.IEventSource;
import com.base.net.CommonMessage;
import com.data.component.GamePropertiesComponent;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.game.AbstractGame;
import com.logic.object.AbstractGamePlayer;
import com.logic.type.CampColorType;
import com.proto.command.UserCmdType.UserCmdOutType;
import com.proto.user.gen.UserOutMsg.EnemyBeKillProtoOut;
import com.proto.user.gen.UserOutMsg.EnemyJumpProtoOut;
import com.proto.user.gen.UserOutMsg.PlatformGroupInfoProtoOut;
import com.util.ThreadSafeRandom;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

/**
 * 游戏玩家
 * 
 * @author dream.wang
 *
 */
public class Player
{
    /** 事件 */
    private IEventSource source;

    /** 是否活着 */
    private boolean isLiving;

    /** 游戏对象 */
    private AbstractGame game;

    /** 阵营类型 */
    private CampColorType campType;

    /** 玩家代理 */
    private AbstractGamePlayer gamePlayer;

    /** 玩家ID */
    private int playerID;

    /** 加载进度 */
    private int progress;

    /** 游戏是否结束 */
    private boolean isOver;

    /** 游戏是否结束 */
    private boolean isSuccess;

    /** 玩家是否投降 */
    private boolean isSurrender;

    /** 完成时间 */
    private long finishTime;

    /** AI上次跳跃时间 */
    private long lastJumpTime;

    /** AI间隔时间 */
    private long jumpIntervalTime;

    /** AI成功率 */
    private int successRate;

    /** 当前是否跳成功 */
    private boolean currentJumpIsSuccess;

    /** 当前所在平台 */
    private int currentIndex;

    public Player(int playerID, CampColorType campColorType, AbstractGame game, AbstractGamePlayer gamePlayer)
    {
        this.gamePlayer = gamePlayer;
        this.playerID = playerID;
        this.game = game;
        this.campType = campColorType;
        this.jumpIntervalTime = 300L + gamePlayer.getRobotLevel() * 50L;
        this.jumpIntervalTime = this.jumpIntervalTime > 1000L ? 1000L : jumpIntervalTime;
        this.jumpIntervalTime = this.jumpIntervalTime < 200L ? 200L : jumpIntervalTime;
        this.currentIndex = 0;
        this.currentJumpIsSuccess = true;
        this.isSurrender = false;
        this.successRate = GamePropertiesComponent.BASE_SUCCESS + gamePlayer.getRobotLevel() * GamePropertiesComponent.BASE_SUCCESS_RATE;
    }

    public boolean isOver()
    {
        return isOver;
    }

    public void setOver(boolean isOver)
    {
        this.isOver = isOver;
    }

    public boolean isSurrender()
    {
        return isSurrender;
    }

    public void setIsSurrender(boolean isSurrender)
    {
        this.isSurrender = isSurrender;
    }

    public boolean isSuccess()
    {
        return this.isSuccess;
    }

    public void setSuccess(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }

    public long getOverTime()
    {
        return this.finishTime;
    }

    public void setOverTime(long time)
    {
        this.finishTime = time;
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
        LogFactory.error("player{} progress：{}", getGamePlayer().getNickName(), progress);
    }

    public AbstractGame getGame()
    {
        return this.game;
    }

    public int getPlayerID()
    {
        return playerID;
    }

    public IEventSource getEventSource()
    {
        return this.source;
    }

    public int getUserID()
    {
        return gamePlayer.getUserID();
    }

    public AbstractGamePlayer getGamePlayer()
    {
        return gamePlayer;
    }

    public CampColorType getCampType()
    {
        return this.campType;
    }

    public boolean isLiving()
    {
        return isLiving;
    }

    public void setIsLiving(boolean isLiving)
    {
        this.isLiving = isLiving;
    }

    /**
     * 不同游戏对象，不同模块初始化
     * 
     * @return
     */
    public boolean init()
    {
        return true;
    }

    public void sendMessage(Builder<?> builder, int code)
    {
        gamePlayer.sendMessage(code, builder);
    }

    public void sendMessage(CommonMessage pkg)
    {
        gamePlayer.sendMessage(pkg);
    }

    /**
     * 机器人跳跃
     * 
     * @param tick
     */
    public void jump(long tick)
    {
        long interval = System.currentTimeMillis() - lastJumpTime;
        if (interval >= jumpIntervalTime)
        {
            lastJumpTime = System.currentTimeMillis();

            if (currentJumpIsSuccess == false)
            {
                int nextIndex = this.currentIndex - 5;
                nextIndex = nextIndex < 0 ? 0 : nextIndex;
                this.currentIndex = nextIndex;

                // 失败后,重置
                currentJumpIsSuccess = true;
                // 失败后,回退到指定层级,等待1秒(客户端动作表现时间)
                lastJumpTime += 1500L;

                EnemyBeKillProtoOut.Builder builder = EnemyBeKillProtoOut.newBuilder();
                builder.setPlatformIndex(nextIndex);
                builder.setPlayerID(this.getGamePlayer().getUserID());
                game.sendToAll(builder, UserCmdOutType.ENEMY_KILL_VALUE, this);

                PrintFactory.error("AI-%s:行为出错:%s,层:%s,interval:%s,delay:%s", getUserID(), currentJumpIsSuccess, nextIndex, interval, jumpIntervalTime);
                return;
            }

            int nextIndex = currentIndex + 1;
            int random = ThreadSafeRandom.next(1, 101);
            // 测试:AI智能不会出错
            random = 0;
            // 判断成功概率
            currentJumpIsSuccess = random < this.successRate ? true : false;
            // 前5级一定成功
            currentJumpIsSuccess = nextIndex <= 5 ? true : currentJumpIsSuccess;
            boolean isLeftJump = false;

            Map<Integer, PlatformGroupInfoProtoOut> map = game.getMap();
            PlatformGroupInfoProtoOut currentLevel = map.get(currentIndex);
            PlatformGroupInfoProtoOut nextLevel = map.get(nextIndex);

            // 如果最后一级为空,结束
            if (nextLevel == null)
            {
                isOver = true;
                game.checkState(0);
                return;
            }

            float currentX = currentLevel.getPlatformInfo().getPosX();
            // 第一层两个台阶,特殊处理,红色:platform,蓝色:extra
            if (currentIndex == 0 && getCampType() == CampColorType.BLUE)
            {
                currentX = currentLevel.getPlatformInfoExtral().getPosX();
            }

            float nextX = nextLevel.getPlatformInfo().getPosX();

            // 如果下一级的x位置>当前级x的位置,向右;反之,向左
            if (nextX >= currentX)
            {
                isLeftJump = false;
            }
            else
            {
                isLeftJump = true;
            }

            if (!currentJumpIsSuccess)
                isLeftJump = !isLeftJump;

            // 不管成功与否,都先跳
            this.currentIndex = nextIndex;

            // 添加钻石
            Map<Integer, Integer> daimonds = game.getDiamondMap();

            // 判断是否有钻石
            if (nextLevel.getPlatformInfo() != null && nextLevel.getPlatformInfo().getHasDiamond())
            {
                // 如果没有获取，添加玩家获取
                if (!daimonds.containsKey(this.currentIndex))
                {
                    daimonds.put(this.currentIndex, getUserID());
                }
            }

            EnemyJumpProtoOut.Builder builder = EnemyJumpProtoOut.newBuilder();
            builder.setIsLeftJump(isLeftJump);
            builder.setJumpIndex(nextIndex);
            builder.setMultiple(1.0f);
            builder.setPlayerID(this.getGamePlayer().getUserID());
            game.sendToAll(builder, UserCmdOutType.ENEMY_JUMP_VALUE, this);

            // PrintFactory.error("AI-%s:行为:%s,概率:%s,层:%s,是否左跳:%s,interval:%s,delay:%s",
            // getUserID(), currentJumpIsSuccess, successRate, nextIndex, isLeftJump, interval, jumpIntervalTime);

            // 如果最后一级为空,结束
            PlatformGroupInfoProtoOut finishLevel = map.get(this.currentIndex + 1);
            if (finishLevel == null)
            {
                isOver = true;
                game.checkState(0);
                return;
            }
        }
    }

    /**
     * 玩家跳跃
     * 
     * @param playerIsLeftJump
     * @param nextIndex
     */
    public void jump(boolean playerIsLeftJump, int nextIndex, float time)
    {
        Map<Integer, PlatformGroupInfoProtoOut> map = game.getMap();
        PlatformGroupInfoProtoOut currentLevel = map.get(currentIndex);
        PlatformGroupInfoProtoOut nextLevel = map.get(nextIndex);

        // 如果位置对不上
        if (nextIndex - currentIndex != 1)
        {
            System.err.println("位置异常.client:" + nextIndex + ",server:" + currentIndex);
            EnemyBeKillProtoOut.Builder builder = EnemyBeKillProtoOut.newBuilder();
            builder.setPlatformIndex(currentIndex);
            builder.setPlayerID(this.getGamePlayer().getUserID());
            game.sendToAll(builder, UserCmdOutType.ENEMY_KILL_VALUE, null);
            return;
        }

        // 如果最后一级为空,结束
        if (nextLevel == null)
        {
            isOver = true;
            game.checkState(0);
            return;
        }

        // 当前正确的跳跃
        boolean isLeftJump = false;

        float currentX = currentLevel.getPlatformInfo().getPosX();
        // 第一层两个台阶,特殊处理,红色:platform,蓝色:extra
        if (currentIndex == 0 && getCampType() == CampColorType.BLUE)
        {
            currentX = currentLevel.getPlatformInfoExtral().getPosX();
        }

        float nextX = nextLevel.getPlatformInfo().getPosX();
        // 如果下一级的x位置>当前级x的位置,向右;反之,向左
        if (nextX >= currentX)
            isLeftJump = false;
        else
            isLeftJump = true;

        // 如果玩家的方向和服务器方向相同
        if (playerIsLeftJump == isLeftJump)
        {
            this.currentIndex = nextIndex;
            // 添加钻石
            Map<Integer, Integer> daimonds = game.getDiamondMap();

            // 判断是否有钻石
            if (nextLevel.getPlatformInfo() != null && nextLevel.getPlatformInfo().getHasDiamond())
            {
                // 如果没有获取，添加玩家获取
                if (!daimonds.containsKey(this.currentIndex))
                {
                    daimonds.put(this.currentIndex, getUserID());
                }
            }
        }

        // else
        // {
        // nextIndex = nextIndex - 5;
        // nextIndex = nextIndex < 0 ? 0 : nextIndex;
        // this.currentIndex = nextIndex;
        //
        // EnemyBeKillProtoOut.Builder builder = EnemyBeKillProtoOut.newBuilder();
        // builder.setPlatformIndex(nextIndex);
        // builder.setPlayerID(this.getGamePlayer().getUserID());
        // game.sendToAll(builder, UserCmdOutType.ENEMY_KILL_VALUE, null);
        // }

        EnemyJumpProtoOut.Builder builder = EnemyJumpProtoOut.newBuilder();
        builder.setIsLeftJump(playerIsLeftJump);
        builder.setJumpIndex(this.currentIndex);
        builder.setMultiple(time);
        builder.setPlayerID(this.getGamePlayer().getUserID());
        game.sendToAll(builder, UserCmdOutType.ENEMY_JUMP_VALUE, this);

        PrintFactory.error("玩家:跳跃层:%s,当前层:%s,是否成功:%s,服务器是否左跳:%s,客户端是否左跳:%s",
                nextIndex, this.currentIndex, (playerIsLeftJump == isLeftJump), isLeftJump, playerIsLeftJump);

        // 如果最后一级为空,结束
        PlatformGroupInfoProtoOut finishLevel = map.get(this.currentIndex + 1);
        if (finishLevel == null)
        {
            isOver = true;
            game.checkState(0);
            return;
        }
    }

    /**
     * 玩家跳错
     * 
     * @param platformIndex
     */
    public void jumpError(int targetIndex)
    {
        if (targetIndex < this.currentIndex)
            this.currentIndex = targetIndex;
        else
        {
            System.err.println("跳错异常.client:" + targetIndex + ",server:" + currentIndex);
            this.currentIndex -= 4;
            this.currentIndex = this.currentIndex <= 0 ? 0 : this.currentIndex;
        }

        EnemyBeKillProtoOut.Builder builder = EnemyBeKillProtoOut.newBuilder();
        builder.setPlatformIndex(this.currentIndex);
        builder.setPlayerID(getUserID());
        game.sendToAll(builder, UserCmdOutType.ENEMY_KILL_VALUE, null);
    }

    /**
     * 玩家投降
     */
    public void surrender()
    {
        setIsSurrender(true);
    }
}
