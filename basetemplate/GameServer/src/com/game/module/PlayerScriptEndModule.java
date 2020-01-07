package com.game.module;

import java.util.List;

import com.base.component.ScriptComponent;
import com.game.object.player.GamePlayer;
import com.logic.object.module.AbstractPlayerModule;
import com.util.print.LogFactory;

import groovy.lang.GroovyObject;

/**
 * 玩家脚本模块
 * 
 * @author dream
 *
 */
public class PlayerScriptEndModule extends AbstractPlayerModule<GamePlayer>
{
    public PlayerScriptEndModule(GamePlayer player)
    {
        super(player);
    }
    
    @Override
    public boolean load()
    {
        List<GroovyObject> list = ScriptComponent.getCacheScript();
        if (!list.isEmpty())
        {
            try
            {
                for (GroovyObject script : list)
                {
                    Object result = script.invokeMethod("load", player);
                    if (!result.toString().equalsIgnoreCase("ok"))
                    {
                        LogFactory.error("PlayerScriptModule load Exception:{},{}", script.getClass().getSimpleName(), result.toString());
                    }
                }
            }
            catch (Exception e)
            {
                LogFactory.error("PlayerScriptModule load Exception:", e);
            }
        }
        return true;
    }

    public boolean relogin()
    {
        List<GroovyObject> list = ScriptComponent.getCacheScript();
        if (!list.isEmpty())
        {
            try
            {
                for (GroovyObject script : list)
                {
                    Object result = script.invokeMethod("relogin", player);
                    if (!result.toString().equalsIgnoreCase("ok"))
                    {
                        LogFactory.error("PlayerScriptModule relogin Exception:{},{}", script.getClass().getSimpleName(), result.toString());
                    }
                }
            }
            catch (Exception e)
            {
                LogFactory.error("PlayerScriptModule relogin Exception:", e);
            }
        }

        return true;
    }

}
