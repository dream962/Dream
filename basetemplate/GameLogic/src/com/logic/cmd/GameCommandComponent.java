package com.logic.cmd;

import java.util.HashMap;
import java.util.Set;

import com.base.command.GameCmdAnnotation;
import com.base.component.AbstractComponent;
import com.util.ClassUtil;
import com.util.print.LogFactory;

/**
 * 游戏命令管理类
 * 
 * @author dream.wang
 *
 */
public class GameCommandComponent extends AbstractComponent
{
    private static HashMap<Short, ICommandHandler> handles = new HashMap<Short, ICommandHandler>();

    public static ICommandHandler loadCommandHandler(short code)
    {
        return handles.get(code);
    }

    @Override
    public boolean initialize()
    {
        handles.clear();
        int count = 0;
        Package pack = Package.getPackage("com.logic.cmd");
        Set<Class<?>> allClasses = ClassUtil.getClasses(pack);
        for (Class<?> class1 : allClasses)
        {
            GameCmdAnnotation annotation = class1.getAnnotation(GameCmdAnnotation.class);

            if (annotation != null)
            {
                try
                {
                    handles.put(annotation.type(), (ICommandHandler) class1.newInstance());
                    count++;
                }
                catch (InstantiationException e)
                {
                    LogFactory.error("GameCommandMgr:SearchCommandHandlers", e);
                    return false;
                }
                catch (IllegalAccessException e)
                {
                    LogFactory.error("GameCommandMgr:SearchCommandHandlers", e);
                    return false;
                }
            }
        }

        LogFactory.info(String.format("total load %d cmds.", count));
        return true;
    }

    @Override
    public void stop()
    {
        handles.clear();
    }
}
