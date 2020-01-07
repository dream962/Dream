package com.util.loader;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.StringUtil;

/**
 * 游戏脚本类加载器，用于副本脚本的热替换
 * @author dream
 *
 */
public class ScriptClassLoader extends ClassLoader
{
    // 脚本文件跟目录
    private static final String scriptDir = "scripts";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptClassLoader.class.getName());

    public ScriptClassLoader()
    {
        super(null);
        File file = new File(scriptDir);

        if (file.exists())
            loadAllScript(file, "");
    }

    /**
     * 获取脚本加载路径
     * 
     * @return
     */
    public static String getScriptDir()
    {
        return scriptDir;
    }

    /**
     * 加载所有脚本
     */
    private void loadAllScript(File file, String parentName)
    {
        try
        {
            // 类名基地址，文件夹需按包目录组织
            String baseName = StringUtil.isNullOrEmpty(parentName) ? ""
                    : parentName + ".";

            File[] allFiles = file.listFiles();

            for (File cFile : allFiles)
            {
                if (cFile.isDirectory())
                {
                    // 当前为目录，递归加载
                    loadAllScript(cFile, baseName + cFile.getName());
                }
                else
                {
                    // 当前为文件，加载指定类
                    FileInputStream inputStream = new FileInputStream(cFile);
                    byte[] rawData = new byte[(int) cFile.length()];
                    inputStream.read(rawData);
                    inputStream.close();

                    // 去掉 .class 后缀
                    String className = baseName
                            + cFile.getName().replaceAll("\\.class", "");
                    defineClass(className, rawData, 0, rawData.length);
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("loadAllScript error:", ex);
        }
    }

    /**
     * 加载指定类
     */
    protected synchronized Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        Class<?> cls = null;

        // 查找本加载器是否已加载该类
        cls = findLoadedClass(name);

        if (cls == null)
            // 委托系统加载
            cls = getSystemClassLoader().loadClass(name);

        if (cls == null)
            // 加载失败
            throw new ClassNotFoundException(name);

        if (resolve)
            resolveClass(cls);

        return cls;
    }
}
