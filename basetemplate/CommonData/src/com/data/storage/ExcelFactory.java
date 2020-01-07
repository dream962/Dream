package com.data.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.FileUtil;
import com.util.JsonUtil;

import jxl.Workbook;

/**
 * excel数据工厂
 * 
 * @author dream
 *
 */
public class ExcelFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFactory.class);

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
                                    LOGGER.error(String.format("配置表（%s）第%d页（%s）出错", xls.getName(), i, sheet.getTableName()), e);
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
                LOGGER.error("Excel File Init Exception. Path:" + xls.getName(), e);
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
                                    LOGGER.error(String.format("配置表（%s）第%d页（%s）出错", xls.getName(), i, sheet.getTableName()), e);
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
                LOGGER.error("Excel File Init Exception. Path:" + xls.getName(), e);
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
            LOGGER.error("Excel File Init Exception. Path:" + filename, e);
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

    public static boolean reloadXmlFile(String path)
    {
        try
        {
            File directory = new File(path);
            if (directory.isDirectory())
            {
                String[] fileList = directory.list();
                for (int i = 0; i < fileList.length; i++)
                {
                    String file = path + File.separator + fileList[i];
                    if (fileList[i].endsWith(".json"))
                    {
                        String clazz = fileList[i].replace(".json", "");
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Class<?> cls = loader.loadClass(clazz);

                        @SuppressWarnings("unchecked")
                        List<Object> list = (List<Object>) JsonUtil.parseFileToListObject(file, cls);
                        if (list != null)
                        {
                            dataMap.put(clazz, list);
                        }
                    }
                }
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    public static void createJsonFile(String path, String filename, String outPath)
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
            LOGGER.error("Excel File Init Exception. Path:" + filename, e);
        }

        try
        {
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
                ;

                String str = JsonUtil.parseObjectToString(dataList);
                FileUtil.createFile(tempPath, str, "UTF-8");
                System.err.println("create " + tempPath);
            }

            // 清空sheet
            sheetMap.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

    private static int size = 0;

    public static void main1(String[] args)
    {
        String path = "./excel";
        // ExcelFactory.createXmlFile(path, "奖励配置表.xls", path);
        ExcelFactory.reloadXmlFile(path);
    }

    public static void main(String[] args)
    {
        long time = System.currentTimeMillis();

        String path = "G:\\project4\\cehua\\6-14版本\\配置表";


        Map<String, ExcelSheet> sheetMap = new HashMap<>();

        try
        {

                Workbook book = Workbook.getWorkbook(new File(path, "主城配置表.xls"));

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
        catch (Exception e)
        {
        }


        System.err.println("dddd:" + sheetMap.size() + "," + (System.currentTimeMillis() - time));
    }
}
