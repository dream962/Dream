package com.deploy.main;

import com.deploy.ui.MainFrame;
import com.deploy.util.Config;

/**
 * 对比deploy文件夹，产生增量包
 */
public class Launcher
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        if (args.length <= 0)
        {
            System.err.println("没有正确设置配置文件路径.");
            return;
        }
        if (!Config.initConfig(args[0]))
        {
            System.err.println("配置文件初始化失败.");
            return;
        }
        
        String mainDeployPath = Config.getValue("curdeploy");
        String lastDeployPath = Config.getValue("lastdeploy");
        String incrementDeployPath = Config.getValue("incrementdeploy");
        String verPath = Config.getValue("verDir");
        
        MainFrame mainFrame = new MainFrame();
        mainFrame.setCurDirector(mainDeployPath);
        mainFrame.setLastDirector(lastDeployPath);
        mainFrame.setIncrementDirector(incrementDeployPath);
        mainFrame.setVerDirector(verPath);
        mainFrame.start();

    }

}
