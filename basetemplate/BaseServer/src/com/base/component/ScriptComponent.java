package com.base.component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.util.FileUtil;
import com.util.print.LogFactory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

/**
 * 脚本组件
 * 
 * @author dream
 *
 */
public class ScriptComponent extends AbstractComponent
{
    /** 执行简单脚本（GM后台刷新） */
    private static GroovyShell shell;

    /** 缓存脚本 */
    private static Map<String, GroovyObject> scriptCache = new HashMap<>();

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    /** 缓存的脚本是玩家登陆后执行的脚本，所以必须有load和relogin方法。 */
    private static final String SCRIPT_PATH = "config/script/";

    public static List<GroovyObject> getCacheScript()
    {
        List<GroovyObject> list = new ArrayList<>();

        lock.readLock().lock();
        try
        {
            list.addAll(scriptCache.values());
        }
        finally
        {
            lock.readLock().unlock();
        }

        return list;
    }

    public static void clearScript()
    {
        lock.writeLock().lock();
        try
        {
            scriptCache.clear();
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean initialize()
    {
        if (shell != null)
        {
            shell.resetLoadedClasses();
            shell = null;
        }

        shell = new GroovyShell();

        // 加载脚本
        Map<String, GroovyObject> map = new HashMap<>();
        List<File> list = FileUtil.getAllFile(SCRIPT_PATH, ".groovy");
        for (File file : list)
        {
            GroovyClassLoader groovyLoader = new GroovyClassLoader();
            try
            {
                // 每次执行groovyLoader.parseClass(groovyScript)， 为了保证每次执行的都是新的脚本内容，会每次生成一个新名字的Class文件.
                // 每个 script 都 new 一个 GroovyClassLoader 来装载
                Class<?> script = groovyLoader.parseClass(file);
                GroovyObject groovyObject = (GroovyObject) script.newInstance();
                map.put(file.getName(), groovyObject);
            }
            catch (IOException | InstantiationException | IllegalAccessException e)
            {
                LogFactory.error("", e);
            }
            finally
            {
                try
                {
                    groovyLoader.clearCache();
                    groovyLoader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        lock.writeLock().lock();
        try
        {
            scriptCache.clear();
            scriptCache.putAll(map);
        }
        finally
        {
            lock.writeLock().unlock();
        }

        return true;
    }

    /**
     * 单独脚本文件执行
     * 
     * @param str
     * @return
     */
    public static String evaluate(String str)
    {
        try
        {
            Object obj = null;
            if (shell != null)
                obj = shell.evaluate(str);

            if (obj != null)
                return String.valueOf(obj);
            else
                return "void method return.";
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
            return e.getMessage();
        }
        finally
        {
            shell.getClassLoader().clearCache();
        }
    }

    @Override
    public void stop()
    {
        for (GroovyObject obj : scriptCache.values())
        {
            if (obj.getClass().getClassLoader() instanceof GroovyClassLoader)
            {
                GroovyClassLoader loader = (GroovyClassLoader) obj.getClass().getClassLoader();
                loader.clearCache();
            }
        }
        scriptCache.clear();

        shell.resetLoadedClasses();
        shell = null;
    }
}
