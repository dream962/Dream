package com.data.component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.data.IBeanFactory;
import com.util.ClassUtil;
import com.util.JsonUtil;
import com.util.print.LogFactory;
import com.util.print.PrintFactory;

/**
 * 系统配置数据组件
 * 
 * @author dream
 *
 */
public class SystemDataComponent extends AbstractComponent
{
    /** 系统配置的所有数据-临时保存 */
    private static Map<String, List<Object>> dataMap = new HashMap<>();

    /** bean工厂 */
    private static Map<String, IBeanFactory> beanFactoryMap = new HashMap<>();

    @Override
    public boolean initialize()
    {
        String path = GlobalConfigComponent.getConfig().server.configPath;
        boolean result = reloadJsonFiles(path);

        if (result)
        {
            initBeanFactory();
        }

        return result;
    }

    private void initBeanFactory()
    {
        try
        {
            List<Class<?>> allClasses = ClassUtil.getClasses("com.data.bean.factory");
            for (Class<?> clazz : allClasses)
            {
                try
                {
                    Object newObject = clazz.newInstance();
                    if (newObject instanceof IBeanFactory)
                    {
                        IBeanFactory beanFactory = (IBeanFactory) newObject;
                        if (beanFactory.load())
                        {
                            beanFactoryMap.put(beanFactory.getClass().getSimpleName(), beanFactory);
                        }
                        else
                        {
                            LogFactory.error("load command fail, bean factory name : " + clazz.getName());
                        }
                    }
                }
                catch (Exception e)
                {
                    LogFactory.error("load command fail, bean factory name : " + clazz.getName(), e);
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("命令管理器解析错误", e);
        }
    }

    /**
     * 重新加载BeanFactory
     * 
     * @param beanName
     * @return
     */
    public static boolean reloadBeanFactory(String beanName)
    {
        IBeanFactory beanFactory = beanFactoryMap.get(beanName);
        if (beanFactory != null)
            return beanFactory.reload();

        return false;
    }

    /**
     * 加载json文件夹
     * 
     * @param path
     * @return
     */
    private static boolean reloadJsonFiles(String path)
    {
        try
        {
            File directory = new File(path);
            if (directory.isDirectory())
            {
                String[] fileList = directory.list();
                for (int i = 0; i < fileList.length; i++)
                {
                    String file = path + File.separator + fileList[i];
                    if (fileList[i].endsWith(".json"))
                    {
                        String clazz = fileList[i].replace(".json", "");
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        try
                        {
                            Class<?> cls = loader.loadClass(clazz);

                            @SuppressWarnings("unchecked")
                            List<Object> list = (List<Object>) JsonUtil.parseFileToListObject(file, cls);
                            if (list != null)
                            {
                                dataMap.put(clazz, list);
                            }
                        }
                        catch (Exception e)
                        {
                            LogFactory.error("SystemDataComponent.reloadJsonFiles() Exception.clazz" + clazz, e);
                            return false;
                        }
                    }
                }
            }

            return true;
        }
        catch (Exception e)
        {
            LogFactory.error("SystemDataComponent.reloadJsonFiles() Exception.", e);
        }

        return false;
    }

    @Override
    public boolean start()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeanList(Class<T> className)
    {
        List<Object> data = dataMap.get(className.getName());
        if (data == null)
            data = new ArrayList<>();
        return (List<T>) data;
    }

    @Override
    public void stop()
    {
        dataMap.clear();
    }

    /**
     * 加载单个json文件
     * 
     * @param fileName
     * @return
     */
    public static boolean reloadSingleFile(String fileName)
    {
        try
        {
            String file = GlobalConfigComponent.getConfig().server.configPath + File.separator + fileName;
            if (fileName.endsWith(".json"))
            {
                String clazz = fileName.replace(".json", "");
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class<?> cls = loader.loadClass(clazz);

                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) JsonUtil.parseFileToListObject(file, cls);
                if (list != null)
                {
                    dataMap.put(clazz, list);
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            PrintFactory.error("", e);
        }
        return true;
    }

    /**
     * 加载多个json文件
     * 
     * @param fileNameList
     * @return
     */
    public static boolean reloadFileList(List<String> fileNameList)
    {
        boolean result = true;
        for (String str : fileNameList)
        {
            if (reloadSingleFile(str) == false)
                result = false;
        }
        return result;
    }

    /**
     * 加载单个文件
     * 
     * @param fileName
     * @return
     */
    public static boolean reloadSingleFileAndBean(String fileName)
    {
        try
        {
            String file = GlobalConfigComponent.getConfig().server.configPath + File.separator + fileName;
            if (fileName.endsWith(".json"))
            {
                String clazz = fileName.replace(".json", "");
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class<?> cls = loader.loadClass(clazz);

                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) JsonUtil.parseFileToListObject(file, cls);
                if (list != null)
                {
                    dataMap.put(clazz, list);
                    return reloadBeanFactory(fileName);
                }
            }
        }
        catch (Exception e)
        {
            PrintFactory.error("", e);
        }
        return true;
    }

    public static boolean reloadFileAndBeanList(List<String> fileNameList)
    {
        boolean result = true;

        for (String str : fileNameList)
        {
            if (reloadSingleFile(str))
            {
                result = reloadBeanFactory(str);
                if (result == false)
                    break;
            }
            else
            {
                result = false;
                break;
            }
        }

        dataMap.clear();

        return result;
    }

}
