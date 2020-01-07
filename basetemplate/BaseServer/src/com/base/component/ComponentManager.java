package com.base.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组件管理器。使用单例模式。
 */
public class ComponentManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentManager.class);

    public static final ComponentManager INSTANCE = new ComponentManager();

    /** 类加载器 */
    private ClassLoader loader = null;

    /** 组件集合 (添加的顺序保持不变) */
    private static Map<String, IComponent> components = new LinkedHashMap<>();

    private ComponentManager()
    {
        loader = Thread.currentThread().getContextClassLoader();
    }

    public static ComponentManager getInstance()
    {
        return INSTANCE;
    }

    public List<IComponent> getAllComponent()
    {
        List<IComponent> list = new ArrayList<>();
        list.addAll(components.values());
        return list;
    }

    public boolean addComponent(String className)
    {
        try
        {
            Class<?> cls = loader.loadClass(className);
            IComponent component = (IComponent) cls.newInstance();

            long spendTime = System.currentTimeMillis();

            if (component != null && component.initialize())
            {
                components.put(className, component);

                if ((spendTime = System.currentTimeMillis() - spendTime) > 1000)
                {
                    LOGGER.warn(String.format("Component %s spend time %d 毫秒!!", className, spendTime));
                }

                return true;
            }
            else
            {
                LOGGER.error("{} getName() is null or empty。", className);
                return false;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> T getComponent(Class<T> t)
    {
        return (T) components.get(t.getName());
    }

    /**
     * 启动组件管理器。
     * 
     * @return
     */
    public boolean start()
    {
        for (Entry<String, IComponent> entry : components.entrySet())
        {
            IComponent module = entry.getValue();

            if (!module.start())
            {
                LOGGER.error("****** Component:[{}] has started failed.******************", entry.getKey());
                return false;
            }
            else
            {
                LOGGER.error("****** Component:[{}] has started successfully.******************", entry.getKey());
            }
        }

        LOGGER.error("****** All Component has started successfully.******************");

        return true;
    }

    /**
     * 关闭组件管理器。
     */
    public void stop()
    {
        List<IComponent> iComponents = new ArrayList<>(components.values());
        Collections.reverse(iComponents);

        iComponents.forEach(p -> {
            LOGGER.error("****** Component:[{}] is ready to stop.******************", p.getClass().getName());
            p.stop();
            LOGGER.error("****** Component:[{}] has stoped.******************", p.getClass().getName());
        });

        components.clear();

        LOGGER.error("All component has stoped.");
    }

    /**
     * 重新加载所有组件
     * 
     * @param excludes
     *            排除的组件
     * @return
     */
    public boolean reload(List<String> excludes)
    {
        for (Entry<String, IComponent> entry : components.entrySet())
        {
            if (excludes != null && excludes.contains(entry.getKey()))
                continue;

            IComponent module = entry.getValue();
            if (!module.reload())
                return false;
        }
        return true;
    }

    /**
     * 加载指定组件
     * 
     * @param componentName
     * @return
     */
    public boolean reloadSingle(String componentName)
    {
        IComponent module = components.get(componentName);
        if (module != null)
            module.reload();

        return true;
    }
}
