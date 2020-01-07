package com.tool.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.Workbook;

/**
 * excel数据工厂
 * 
 * @author dream
 *
 */
public class ExcelFactory
{
    /** 第一个sheet页是说明 */
    public static final int SHEET_START = 1;

    /** 所有数据 */
    private static Map<String, List<Object>> dataMap = new HashMap<>();

    public static boolean init(String path)
    {
        System.err.println("配置表路径：" + path);

        File file = new File(path);

        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        // 构造sheet信息
        for (File xls : file.listFiles())
        {
            try
            {
                if (!xls.isDirectory() && xls.getName().endsWith("xls"))
                {
                    System.out.println(xls.getName());
                    Workbook book = Workbook.getWorkbook(new File(path, xls.getName()));

                    // 第一个sheet页是说明
                    for (int i = SHEET_START; i < book.getNumberOfSheets(); ++i)
                    {
                        if (book.getSheet(i) != null)
                        {
                            ExcelSheet sheet = new ExcelSheet();
                            if (sheet.init(book.getSheet(i)))
                            {
                                sheetMap.put(sheet.getTableName(), sheet);
                                try
                                {
                                    Thread.currentThread().getContextClassLoader().loadClass(sheet.getTableName());
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    System.err.println(
                                            String.format("配置表（%s）第%d页（%s）出错", xls.getName(), i, sheet.getTableName()));
                                    Runtime.getRuntime().halt(0);
                                }
                            }
                        }
                    }

                    book.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.err.println("Excel File Init Exception. Path:" + xls.getName());
            }

        }

        dataMap.clear();

        // 导入数据
        for (Entry<String, ExcelSheet> entry : sheetMap.entrySet())
        {
            List<Object> dataList = new ArrayList<>();
            for (Map<String, String> data : entry.getValue().dataList)
            {
                Object obj = ExcelTool.createObject(entry.getKey(), data);
                dataList.add(obj);
            }

            dataMap.put(entry.getKey(), dataList);
        }

        // 清空sheet
        sheetMap.clear();

        return true;
    }

    /**
     * 加载特定的表
     * 
     * @param path
     * @param filename
     * @return
     */
    public static boolean init(String path, String filename)
    {
        System.err.println("配置表路径：" + path);

        File file = new File(path);

        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        // 构造sheet信息
        for (File xls : file.listFiles())
        {
            try
            {
                if (!xls.isDirectory() && xls.getName().endsWith("xls") && xls.getName().equals(filename))
                {
                    System.out.println(xls.getName());
                    Workbook book = Workbook.getWorkbook(new File(path, xls.getName()));

                    // 第一个sheet页是说明
                    for (int i = SHEET_START; i < book.getNumberOfSheets(); ++i)
                    {
                        if (book.getSheet(i) != null)
                        {
                            ExcelSheet sheet = new ExcelSheet();
                            if (sheet.init(book.getSheet(i)))
                            {
                                sheetMap.put(sheet.getTableName(), sheet);
                                try
                                {
                                    Thread.currentThread().getContextClassLoader().loadClass(sheet.getTableName());
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    System.err.println(
                                            String.format("配置表（%s）第%d页（%s）出错", xls.getName(), i, sheet.getTableName()));
                                    Runtime.getRuntime().halt(0);
                                }
                            }
                        }
                    }

                    book.close();
                }
            }
            catch (Exception e)
            {
                System.err.println("Excel File Init Exception. Path:" + xls.getName());
                e.printStackTrace();
                return false;
            }
        }

        dataMap.clear();

        // 导入数据
        for (Entry<String, ExcelSheet> entry : sheetMap.entrySet())
        {
            List<Object> dataList = new ArrayList<>();
            for (Map<String, String> data : entry.getValue().dataList)
            {
                Object obj = ExcelTool.createObject(entry.getKey(), data);
                dataList.add(obj);
            }

            dataMap.put(entry.getKey(), dataList);
        }

        // 清空sheet
        sheetMap.clear();

        return true;
    }

    public static boolean reloadFile(String path, String filename)
    {
        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        // 构造sheet信息
        try
        {
            File xls = new File(path, filename);
            if (!xls.isDirectory() && xls.getName().endsWith("xls") && xls.getName().equals(filename))
            {
                Workbook book = Workbook.getWorkbook(new File(path, xls.getName()));

                // 第一个sheet页是说明
                for (int i = SHEET_START; i < book.getNumberOfSheets(); ++i)
                {
                    if (book.getSheet(i) != null)
                    {
                        ExcelSheet sheet = new ExcelSheet();
                        if (sheet.init(book.getSheet(i)))
                        {
                            sheetMap.put(sheet.getTableName(), sheet);
                        }
                    }
                }

                book.close();
            }
        }
        catch (Exception e)
        {
            System.err.println("Excel File Init Exception. Path:" + filename);
            e.printStackTrace();
            return false;
        }

        // 导入数据
        for (Entry<String, ExcelSheet> entry : sheetMap.entrySet())
        {
            List<Object> dataList = new ArrayList<>();
            for (Map<String, String> data : entry.getValue().dataList)
            {
                Object obj = ExcelTool.createObject(entry.getKey(), data);
                dataList.add(obj);
            }

            dataMap.put(entry.getKey(), dataList);
        }

        // 清空sheet
        sheetMap.clear();

        return true;
    }

    /**
     * 根据excel的路径，生成json文件
     * 
     * @param path
     * @param filename
     * @param outPath
     */
    public static boolean createJsonFile(String path, String filename, String outPath)
    {
        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        try
        {
            File xls = new File(path, filename);
            if (!xls.isDirectory() && xls.getName().endsWith("xls") && xls.getName().equals(filename))
            {
                Workbook book = Workbook.getWorkbook(new File(path, xls.getName()));

                // 第一个sheet页是说明
                for (int i = SHEET_START; i < book.getNumberOfSheets(); ++i)
                {
                    if (book.getSheet(i) != null)
                    {
                        ExcelSheet sheet = new ExcelSheet();
                        if (sheet.init(book.getSheet(i)))
                        {
                            sheetMap.put(sheet.getTableName(), sheet);
                        }
                    }
                }

                book.close();
            }

            for (Entry<String, ExcelSheet> entry : sheetMap.entrySet())
            {
                String content=ExcelTool.createJson(entry.getValue());

                String tempPath = "";
                if (outPath.trim().isEmpty())
                    tempPath = System.getProperty("user.dir") + entry.getKey() + ".json";
                else
                    tempPath = outPath + entry.getKey() + ".json";
                
//                if(tempPath.indexOf("com.data.bean")>=0)
//                    tempPath=tempPath.replace("com.data.bean.", "");

                FileUtil.createFile(tempPath, content, "UTF-8");
                System.err.println("创建json配置： " + tempPath);
            }

            // 清空sheet
            sheetMap.clear();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("创建错误：" + filename);
        }
        return false;
    }

    /**
     * 根据excel的路径，生成json文件
     * 
     * @param path
     * @param filename
     * @param outPath
     */
    public static boolean createJsonFile1(String path, String filename, String outPath)
    {
        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        try
        {
            File xls = new File(path, filename);
            if (!xls.isDirectory() && xls.getName().endsWith("xls") && xls.getName().equals(filename))
            {
                Workbook book = Workbook.getWorkbook(new File(path, xls.getName()));

                // 第一个sheet页是说明
                for (int i = SHEET_START; i < book.getNumberOfSheets(); ++i)
                {
                    if (book.getSheet(i) != null)
                    {
                        ExcelSheet sheet = new ExcelSheet();
                        if (sheet.init(book.getSheet(i)))
                        {
                            sheetMap.put(sheet.getTableName(), sheet);
                        }
                    }
                }

                book.close();
            }

            for (Entry<String, ExcelSheet> entry : sheetMap.entrySet())
            {
                List<Object> dataList = new ArrayList<>();
                for (Map<String, String> data : entry.getValue().dataList)
                {
                    Object obj = ExcelTool.createObject(entry.getKey(), data);
                    dataList.add(obj);
                }

                String tempPath = "";
                if (outPath.trim().isEmpty())
                    tempPath = System.getProperty("user.dir") + File.separatorChar + entry.getKey() + ".json";
                else
                    tempPath = outPath + File.separatorChar + entry.getKey() + ".json";

                String str = JsonUtil.parseObjectToString(dataList);
                FileUtil.createFile(tempPath, str, "UTF-8");
                System.err.println("创建json配置： " + tempPath);
            }

            // 清空sheet
            sheetMap.clear();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("创建错误：" + filename);
        }
        return false;
    }

    
    public static void createJsonFileList(String excelPath, String jsonPath)
    {
        System.err.println("**********************生成json配置文件开始。**************************");
        File file = new File(excelPath);

        if (file.isDirectory())
        {
            // 构造sheet信息
            for (File xls : file.listFiles())
            {
                try
                {
                    if (!xls.isDirectory() && xls.getName().endsWith("xls"))
                    {
                        createJsonFile(excelPath, xls.getName(), jsonPath);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        System.err.println("***********************生成json配置文件完成。***************************");
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeanList(Class<T> className)
    {

        List<Object> data = dataMap.get(className.getName());
        if (data == null)
            data = new ArrayList<>();
        return (List<T>) data;
    }

    public static void clear()
    {
        dataMap.clear();
    }
}
