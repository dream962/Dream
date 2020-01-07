package com.deploy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.deploy.util.ClientVerFileHandler;
import com.deploy.util.Config;
import com.deploy.util.DirectoryUtil;

public class ClientVerMain
{

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
        String verPath = Config.getValue("verDir");
        
        String exceptString = Config.getValue("filterFileName");
        ClientVerFileHandler fileHandler = new ClientVerFileHandler(mainDeployPath, lastDeployPath,
                verPath);
        DirectoryUtil directoryUtil = new DirectoryUtil(fileHandler, exceptString);
        List<File> fileList = new ArrayList<File>();
        directoryUtil.refreshFileList(mainDeployPath, fileList);
        fileHandler.init();

        for (final File file : fileList)
        {
            fileHandler.done(file);
        }
        
//        fileHandler.createMd5File();
        System.err.println("生成成功");
    }
}
