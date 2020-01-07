package com.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.tool.dts.DBConfig;
import com.tool.dts.DtsXml;
import com.tool.dts.ErrorMgr;
import com.tool.dts.ExcelFile;
import com.tool.dts.ExcelSheet;

import jxl.Cell;

/**
 * 数据库DTS工具：config文件夹下：db，config配置文件，config文件里面的version属性指定版本
 *             template文件夹下：xls数据文件
 * @author Dream
 * @date 2013-5-22
 * @version 1.0
 */
public class DTSDBTools
{
    private static String PATH="config";
    
    private static String TEMPLATE="template";
    
    public static Properties properties;
    
    private static final boolean ISCREATE=false;
    
    public static void main(String[] args)
    {
        String dirPath = System.getProperty("user.dir") + File.separator+PATH;
        try
        {
            InputStream in = new BufferedInputStream(new FileInputStream(dirPath+ File.separator + "db.ini"));
            properties = new Properties();
            properties.load(in);
            in.close();
        }
        catch (Exception e)
        {
            ErrorMgr.error("没找到配置文件", e);
        }
        
        run(dirPath + File.separator);
    }
    
    public static Connection initConnection(DBConfig dbConfig)
    {
        // 第一步，配置Connection语句
        Connection conn = dbConfig.getConnection();
        if (conn == null)
        {
            ErrorMgr.info(String.format("第一步：获取Connection 失败,URL%s",dbConfig.getUrl()));
            return conn;
        }
        else
        {
            ErrorMgr.info(String.format("第一步：获取Connection 成功,URL%s",dbConfig.getUrl()));
        }
        
        return conn;
    }
    
    public static DtsXml initDts(String dirPath)
    {
        // 第二步，配置XML语句
        try
        {
            if(dirPath.trim().isEmpty())
                dirPath=System.getProperty("user.dir") + File.separator+TEMPLATE;
            else
                dirPath = new String(dirPath.getBytes("ISO-8859-1"),"UTF-8");
            
            DtsXml dtsXml = new DtsXml(dirPath);
            if (!dtsXml.checkExecl())
            {
                ErrorMgr.info("第二步：检测Execl 失败");
                return null;
            }
            else
            {
                ErrorMgr.info("第二步 ：检测Execl 成功");
            }
            return dtsXml;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ExcelSheet getExcelSheet(ExcelFile file, int tableIndex)
    {
        return file.getSheet(tableIndex);
    }
    
    public static void handleSheet(DBConfig dbConfig, Connection conn, ExcelSheet sheet)
    {
        // 1、获取表名-第一行第四个字段
        String tableName = sheet.getTableName();
        
        if(tableName.isEmpty())
            return;
        
        // 2、删除记录
        if (sheet.isTableDelete())
        {
            String delStr = "truncate " + tableName;
            dbConfig.executeNoneQuery(conn, delStr);
        }

        // 3、插入记录
        int ignore = 0;
        int count = 0;
        ExcelSheet execlSheet = sheet;
        
        final int START_ROW = 4; //从第4行开始
        
        Cell[] types = execlSheet.getSheet(2);
        
        if (execlSheet.getRows() >= START_ROW)
        {
            for (int i = START_ROW; i < execlSheet.getRows(); i++)
            {
                Cell[] execlData = execlSheet.getSheet(i);
                if (execlData != null)
                {
                    //排除行
                    if(execlData[0].getContents().startsWith(DBConfig.DeleteFlag) || execlData[0].getContents().isEmpty())
                        continue;
                    
                    String insertStr = execlSheet.getInsertStr();
                    
                    int[] fieldRow = execlSheet.getFieldRow();
                    int result = dbConfig.executeNoneQuery(conn, insertStr,
                                                           execlData, types,
                                                           fieldRow);
                    ++count;
                    if (result == -1)
                    {
                        ignore = i;
                    }
                    if (ignore > 0)
                    {
                        ErrorMgr.info(String.format("添加execl%s失败,第%s行出错!",
                                tableName, ignore));
                        return;
                    }
                }

            }
        }
        
        System.out.println(String.format("sheet(%d) 数据库表(%s) 记录(%d)", sheet.getIndex(), sheet.getTableName(), count));
    }
    
    private static int totalFile;
    private static int current;
    private static int add;
    
    public static void handleExcel(Connection conn, DBConfig dbConfig, DtsXml dtsXml, String excelName)
    {
        ExcelFile excelFile = new ExcelFile(dtsXml.dirPath, excelName);
        System.out.println("------- begin  "+dtsXml.dirPath + File.separator + excelName+"  import.  -------");
        
        for(int i=ExcelFile.START_INDEX; i<excelFile.sheetCount(); ++i){
            handleSheet(dbConfig, conn, getExcelSheet(excelFile, i));
        }
        System.out.println("------- finish "+dtsXml.dirPath + File.separator + excelName+"  successfully. -----");
        System.out.println();
        excelFile.close();
    }
    
    public static void closeConnection(Connection conn)
    {
    	// 第四步，关闭connection
        if (conn != null)
        {
            try
            {
                if (!conn.isClosed())
                {
                    conn.close();
                }
            }
            catch (SQLException e)
            {
                ErrorMgr.error("关闭Connection出错", e);
            }
        }
    }
    
    public static boolean run(String dirPath)
    {
        DBConfig dbConfig = new DBConfig(properties);
        Connection connection = initConnection(dbConfig);
        DtsXml dtsXml = initDts(properties.getProperty("path"));
        
        for (String excelName : dtsXml.getExcelFileList())
        {
            handleExcel(connection, dbConfig, dtsXml, excelName);
        }
        
        return true;
    }

    public static String formatString(String targetStr, int strLength)
    {
        if (targetStr==null || targetStr=="")
            return targetStr;
        
        int curLength = targetStr.getBytes().length;
        if (curLength > strLength)
        {
            targetStr = targetStr.substring(0, strLength);
        }

        int cutLength = strLength - targetStr.getBytes().length;
        
        StringBuilder sBuilder = new StringBuilder(targetStr);
        for (int i = 0; i < cutLength; i++)
            sBuilder.append(" ");
        
        return sBuilder.toString();
    }

}
