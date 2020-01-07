package com.deploy.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @version 
 * 目录处理相关类，主要用来遍历目录下所有文件，对文件的处理委托给FileHandle
 */
public class DirectoryUtil
{
    private IFileHandler fileHandler;
    
    private FilenameFilter filenameFilter;
    
    public DirectoryUtil(IFileHandler fileHandler, String filterString)
    {
        super();
        this.setFileHandler(fileHandler);
        this.filenameFilter = new DirFilter(filterString);
    }
    
    /**
     * 遍历rootDirctory下的所有目录和文件
     * @param rootDirctory
     * @param except
     */
    public void refreshFileList(String rootDirctory, List<File> fileList)
    {
        File dir = new File(rootDirctory);
        File[] files = dir.listFiles(filenameFilter);

        if (files == null)
            return;
        
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                refreshFileList(files[i].getAbsolutePath(), fileList);
            }
            else
            {
               // fileHandler.done(files[i]);
                fileList.add(files[i]);
            }
        }
    }
    
    public void setFileHandler(IFileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
    }

    public IFileHandler getFileHandler()
    {
        return fileHandler;
    }

    /**
     * 文件过滤器
     * @author : Cookie
     * @date : 2012-1-12
     * @version 
     *
     */
    private class DirFilter implements FilenameFilter
    {
        private Pattern pattern;
        
        public DirFilter(String regex)
        {
            super();
            this.pattern = Pattern.compile(regex);
        }

        /* (non-Javadoc)
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
        @Override
        public boolean accept(File dir, String name)
        {
            return !pattern.matcher(name).matches();
        }
    }
}
