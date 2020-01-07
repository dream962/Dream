package com.deploy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @version 
 * 文件相关处理，比较两个文件的内容是不是一模一样
 */
public class FileUtil
{
    /**
     * 比较两个文件是不是一样
     * @param file1Md52
     * @param file1Md53
     * @return
     * @throws IOException 
     */
    public static boolean isSameFile(String file1Md5, String file2Md5)
    {
        return file1Md5.equals(file2Md5);
    }
    
    /**
     * 将文件从srcFile拷贝到destFile
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean fileCopy(String srcFile, String destFile) throws IOException
    {
        FileInputStream input = new FileInputStream(srcFile);
        try
        {
            FileOutputStream output = new FileOutputStream(destFile);
            try
            {
                byte[] buffer = new byte[8192];
                int n = 0;
                while (-1 != (n = input.read(buffer)))
                {
                    output.write(buffer, 0, n);
                }
            }
            finally
            {
                try
                {
                    if (output != null)
                    {
                        output.close();
                    }
                }
                catch (IOException ioe)
                {
                    return false;
                }
            }
        }
        finally
        {
            try
            {
                if (input != null)
                {
                    input.close();
                }
            }
            catch (IOException ioe)
            {
                return false;
            }
        }
        return true;
    }
   
    /**
     * 拷贝文件
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean fileCopy(File srcFile, File destFile)
    {
        try
        {
            if (!destFile.exists())
            {
                destFile.getParentFile().mkdirs();
            }
            return fileCopy(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteFile(File file)
    {
        if (file.isFile())
        {
            return file.delete();
        }
        
        File [] files = file.listFiles();
        for (File file2 : files)
        {
            deleteFile(file2);
        }
        return file.delete();
    }
}
