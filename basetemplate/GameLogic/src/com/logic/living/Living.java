package com.logic.living;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.net.CommonMessage;
import com.google.protobuf.GeneratedMessage.Builder;
import com.logic.game.BaseGame;
import com.logic.living.module.AbstractLivingModule;
import com.logic.living.module.LivingModuleType;
import com.logic.move.LivingMoveModule;

/**
 * living信息
 * 
 * @author dream
 *
 */
public class Living extends Physics
{
    private static Logger logger = LoggerFactory.getLogger(Living.class.getName());

    protected Living(int id, BaseGame game)
    {
        super(id, game);
    }

    /************************************************** 模块 ************************************************************/

    /**
     * 每帧更新
     */
    @Override
    public void update(long tick, int interval)
    {
        for (AbstractLivingModule module : moduleMap.values())
        {
            if (isLiving)
            {
                module.update(tick, interval);
            }
        }

        if (isLiving)
            super.update(tick, interval);
    }

    public boolean init()
    {
        try
        {
            moduleMap.put(LivingModuleType.MoveModule.getValue(), new LivingMoveModule(this));
            for (Entry<Integer, AbstractLivingModule> entry : moduleMap.entrySet())
            {
                entry.getValue().init();
            }
        }
        catch (Exception e)
        {
            logger.error("Living Init Exception:", e);
        }

        return true;
    }

    public boolean addModule(LivingModuleType type, AbstractLivingModule module)
    {
        moduleMap.put(type.getValue(), module);
        if (module.init())
            return true;

        return false;
    }

    public boolean removeModule(LivingModuleType type)
    {
        AbstractLivingModule module = moduleMap.remove(type.getValue());
        if (module != null)
            module.destroy();

        return true;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractLivingModule> T getModule(int type)
    {
        return (T) moduleMap.get(type);
    }

    public LivingMoveModule getMoveModule()
    {
        return getModule(LivingModuleType.MoveModule.getValue());
    }

    /*********************************************** 属性Get/Set ******************************************************/

    public BaseGame getGame()
    {
        return this.game;
    }

    /**
     * 属性资源增加（所有资源类属性通过此方法添加）
     * 
     * @param value
     * @param type
     * @return
     */
    public float addProperty(int type, float value)
    {
        return value;
    }

    /**
     * 属性资源减少（所有资源类属性通过此方法减少）
     * 
     * @param attacker
     *            攻击者ID
     * @param type
     *            减少的属性类型
     * @param value
     *            减少的属性数量
     * @return
     */
    public float removeProperty(int attacker, int type, float value)
    {
        return value;
    }

    /**
     * 取得特定属性信息
     * 
     * @param type
     * @return
     */
    public float getProperty(int type)
    {
        return 0;
    }

    public void setProperty(int type, float value)
    {
    }

    /***************************************************** 状态 *********************************************************/

    public void die(int atackerID)
    {
        super.die(atackerID);

//        Living attacker = game.getAllLivingByID(atackerID);
//
//        if (game != null)
//        {
//            game.removeLiving(livingID);
//
//            if (game.getEvent() != null)
//            {
//                game.getEvent().notifyListeners(new LivingAndGameDieEventArg(GameEventType.LivingDie.getValue(), this, attacker));
//            }
//        }
    }

    /***************************************************** 同步 *********************************************************/

    public void sendMessage(Builder<?> builder, int code)
    {

    }

    public void sendMessage(CommonMessage pkg)
    {

    }
}
