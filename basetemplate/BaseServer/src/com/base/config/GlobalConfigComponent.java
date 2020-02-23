package com.base.config;

import java.io.IOException;

import com.base.component.AbstractComponent;
import com.util.FileUtil;
import com.util.XmlUtil;
import com.util.print.LogFactory;

/**
 * 全局配置组件
 * 
 * @author dream
 *
 */
public class GlobalConfigComponent extends AbstractComponent
{
    private static AllConfigList Config;

    public static AllConfigList getConfig()
    {
        return Config;
    }

    public static boolean init(String path)
    {
        try
        {
            String xmlStr = FileUtil.readTxt(path, "UTF-8");

            Config = XmlUtil.toObject(xmlStr, AllConfigList.class);
            if (Config == null)
            {
                LogFactory.error("Server Config[{}] Load Failed.", path);
                return false;
            }

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean initialize()
    {
        return true;
    }

    @Override
    public void stop()
    {
        Config = null;
    }
}
