package com.logic.living;

import java.util.LinkedHashMap;
import java.util.Map;

import com.base.event.EventSource;
import com.base.event.IEventSource;
import com.logic.game.BaseGame;
import com.logic.living.module.AbstractLivingModule;
import com.logic.type.CampColorType;
import com.util.wrapper.WrapCircle;
import com.util.wrapper.WrapPoint;

/**
 * 游戏内的物理对象，所有游戏内对象的基础
 * 
 * @author dream
 */
public abstract class Physics
{
    protected int livingID;

    /** 坐标（圆：圆心；矩形：左下中点） */
    protected int x;

    /** 坐标（圆：圆心；矩形：左下中点） */
    protected int y;

    /** 方向（角度 0~360） */
    protected float direction;

    /** 事件 */
    protected IEventSource source;

    /** 出生点（只是记录使用） */
    protected WrapPoint birthPoint;

    /** 出生的时间 */
    protected long birthTime;

    /** 碰撞体 */
    protected WrapCircle boundBox;

    /**
     * 是否活着
     */
    protected boolean isLiving;

    /** 游戏对象 */
    protected BaseGame game;

    /** 模块列表 */
    public Map<Integer, AbstractLivingModule> moduleMap;

    /** 阵营类型 */
    protected CampColorType campType;

    public Physics(int id, BaseGame game)
    {
        super();
        this.boundBox = new WrapCircle(50);
        this.game = game;
        this.moduleMap = new LinkedHashMap<>();
        livingID = id;
        isLiving = true;
        source = new EventSource();
        birthTime = System.currentTimeMillis();
    }

    public IEventSource getEventSource()
    {
        return this.source;
    }

    public long getBirthTime()
    {
        return birthTime;
    }

    public int getLivingID()
    {
        return livingID;
    }

    public void setLivingID(int id)
    {
        livingID = id;
    }

    public CampColorType getCampType()
    {
        return this.campType;
    }

    /**
     * 得到包围体
     * 
     * @return
     */
    public WrapCircle getBound()
    {
        return boundBox.clone();
    }

    /**
     * 得到方向角度
     * 
     * @return
     */
    public float getDirection()
    {
        return direction;
    }

    public void setDirection(float dir)
    {
        this.direction = dir;
        if (direction < 0)
            direction = 360 + direction;
        if (direction > 360)
            direction = direction % 360;
    }

    public boolean isLiving()
    {
        return isLiving;
    }

    public void setIsLiving(boolean isLiving)
    {
        this.isLiving = isLiving;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public WrapPoint getBirthPoint()
    {
        return this.birthPoint;
    }

    /**
     * living出生
     * 
     * @param x
     * @param y
     */
    public void birth(int x, int y)
    {
        this.x = x;
        this.y = y;

        if (this.birthPoint == null)
            this.birthPoint = new WrapPoint(x, y);
        else
            this.birthPoint.reset(x, y);
    }

    public void setPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * 直接设置位置，不计算是否合法
     * 
     * @param x
     * @param y
     */
    public void setNoCheckPos(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void die(int attackerID)
    {
        isLiving = false;
    }

    /**
     * 不同游戏对象，不同模块初始化
     * 
     * @return
     */
    public abstract boolean init();

    /**
     * 更新处理
     * 
     * @param tick
     * @param interval
     */
    public void update(long tick, int interval)
    {
        // if (this instanceof Living)
        // {
        // if (((Living) this).isGamePlayer())
        // TODO：测试移动
        // game.sendTestMove(this, tick);
        // }
    }
}
