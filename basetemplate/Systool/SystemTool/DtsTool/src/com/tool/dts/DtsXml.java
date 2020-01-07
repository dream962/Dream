package com.tool.dts;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author xiaov
 * @date 2011-05-04
 * @version XML文件
 * 
 */
public class DtsXml
{
    // 以excel的file_name字段为key，excel节点为value的映射
    private Hashtable<String, Hashtable<String, String>> xmlInfo = null;
    private List<String> excelFileList = new ArrayList<String>();
    public String dirPath;
    
    public String version;

    public DtsXml(String dirPath)
    {
        if (!load(dirPath))
        {
            ErrorMgr.info("没有找到" + dirPath);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean load(String dirPath)
    {
        this.dirPath = dirPath;
        
        try
        {
            File file = new File(dirPath);
            
            for(File xls : file.listFiles()){
                if(!xls.isDirectory() && xls.getName().endsWith("xls")){
                    excelFileList.add(xls.getName());
                }
            }
        }
        catch (Exception e)
        {
            ErrorMgr.error("读取XML节点错误!", e);
            return false;
        }
        return true;
    }

    public boolean checkExecl()
    {
        boolean flag = true;
        
        return flag;
    }

    public Hashtable<String, Hashtable<String, String>> getXmlInfo()
    {
        return xmlInfo;
    }

    // 取得需要导入的excel文件列表（按xml的配置顺序）
    public List<String> getExcelFileList()
    {
        return excelFileList;
    }

    public Hashtable<String, String> getValue(String key)
    {
        return xmlInfo.get(key);
    }
}
