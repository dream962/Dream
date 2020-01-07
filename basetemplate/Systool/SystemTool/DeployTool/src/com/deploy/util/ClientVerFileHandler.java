package com.deploy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 版本处理
 * @author dream
 *
 */
public class ClientVerFileHandler implements IFileHandler
{
    //当前版本目录
    private String rootCurDire;
    
    //老版本目录
    private String rootOldDire;
    
    // 版本文件生成路径
    private String newVerPath;
    
    //已处理的同名文件，则不用再比较
    private Map<String, Integer> haveDoneFiles;
    
    //文件的md5缓存
    private Map<String, String> md5Cache;
    
    public ClientVerFileHandler(String rootCurDire, String rootOldDire, String verPath)
    {
        super();
        this.rootCurDire = rootCurDire;
        this.rootOldDire = rootOldDire;
        this.newVerPath = verPath;
        haveDoneFiles = new HashMap<String, Integer>();
        md5Cache = new HashMap<String, String>();
        init();
    }

    /**
     * @return the rootCurDire
     */
    public String getRootCurDire()
    {
        return rootCurDire;
    }

    /**
     * @return the rootOldDire
     */
    public String getRootOldDire()
    {
        return rootOldDire;
    }


    /**
     * @param rootCurDire the rootCurDire to set
     */
    public void setRootCurDire(String rootCurDire)
    {
        this.rootCurDire = rootCurDire;
    }

    /**
     * @param rootOldDire the rootOldDire to set
     */
    public void setRootOldDire(String rootOldDire)
    {
        this.rootOldDire = rootOldDire;
    }


    /**
     * 清除增量包里已有的数据
     */
    public void init()
    {
//        File file = new File(rootIncrementDire);
//        
//        if (file.exists())
//        {
//            FileUtil.deleteFile(file);
//        }
//        file = null;
//        haveDoneFiles.clear();
    }

    /**
     * 返回0，表示不用操作
     * 返回1，表示是新增文件
     * 返回2，表示 是修改过的文件
     */
    @Override
    public int done(File files)
    {        
        int result = 0;
        
        //是否需要进行拷贝
        boolean shouldCopy = false;

        //如果主目录和次目录是一样的，则不处理
        if (rootCurDire.equals(rootOldDire))
        {
            return 0;
        }
        
        //如果这个文件已经处理过,则不处理,已处理文件表中只添加需要copy的文件
        if (haveDoneFiles.containsKey(files.getName()))
        {
            shouldCopy = true;
            result = haveDoneFiles.get(files.getName());
        }

        String curFilePath = files.getAbsolutePath();

        String oldFilePath = curFilePath.replace(rootCurDire, rootOldDire);
         
        File oldFile = new File(oldFilePath);
        
        // 如果老版本不存在这个文件，则新版本直接拷贝这个文件
        if (!oldFile.exists())
        {
            shouldCopy = true;
            result = 1;
        }
        
        //如果新老版本的长度不一样，则需要拷贝
        if (!shouldCopy && oldFile.length() != files.length())
        {
            shouldCopy = true;
            result = 2;
        }
        
        //如果文件内容不同，则需要拷贝
        String file1Md5 = FileGetMD5.getMD5(files);
        if (!shouldCopy)
        {
            String file2Md5 = FileGetMD5.getMD5(oldFile);
            if(!FileUtil.isSameFile(file1Md5, file2Md5))
            {
                shouldCopy = true;
                result = 2;
            }
        }

        //如果当前的文件在需要进行做版本对比的目录下面
        if (curFilePath.contains(rootCurDire))
        {
            String curPath = curFilePath.replace(rootCurDire, "");
            try
            {
                //linux下要换斜杠
                curPath = curPath.replace("/", "\\");
                System.out.println("当前生成的文件："+curPath);
                String md5Key = MD5Security.code(curPath, 16);
                md5Cache.put(md5Key, file1Md5);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        if (!shouldCopy)
        {
            return 0;
        }
        
//        System.err.println(curFilePath);
        
        //对应的文件在增量包中的完整路径
//        String incrementDire = curFilePath.replace(rootCurDire, rootIncrementDire);
//        File incrementFile = new File(incrementDire);
//
//        FileUtil.fileCopy(files, incrementFile);
//        haveDoneFiles.put(files.getAbsolutePath(), result);
        return result;
    }

    /**
     * 创建缓存文件
     */
    public void createMd5File()
    {
//        String md5FilePath = rootVerPath.replace(rootCurDire, rootIncrementDire);
        File file = new File(newVerPath + "//" + Config.getValue("vername"));
        FileOutputStream output;
        try
        {
            output = new FileOutputStream(file);
            StringBuilder sBuilder = new StringBuilder();
            for (Entry<String, String> entry : md5Cache.entrySet())
            {
                String data = entry.getKey() + " " + entry.getValue() + '\n';
                sBuilder.append(data);
            }
            output.write(CompressUtil.compress(sBuilder.toString()));
            output.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

}
