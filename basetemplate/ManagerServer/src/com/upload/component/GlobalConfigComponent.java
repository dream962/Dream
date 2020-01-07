package com.upload.component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.upload.config.AllConfigList;
import com.upload.util.FileUtil;
import com.upload.util.XmlUtil;

/**
 * 全局配置组件
 * @author dream
 *
 */
public class GlobalConfigComponent extends AbstractComponent
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalConfigComponent.class);

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
                logger.error("Server Config[{}] Load Failed.", path);
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
        Config=null;
    }
}
