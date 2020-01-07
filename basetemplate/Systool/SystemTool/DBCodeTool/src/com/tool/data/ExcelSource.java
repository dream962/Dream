package com.tool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.tool.code.FieldInfo;
import com.tool.data.ExcelSheet.OneRowData;

public class ExcelSource implements IDataSource
{
    /** excel文件信息 */
    public Map<String, ExcelFile> fileMap = new LinkedHashMap<>();

    public void init(String config)
    {
        fileMap.clear();
        InputStream in = null;
        try
        {
            in = new FileInputStream(config);
            Properties prop = new Properties();
            prop.load(in);

            String path = prop.getProperty("excel");
            path = new String(path.getBytes("ISO-8859-1"), "UTF-8");

            File file = new File(path);

            for (File xls : file.listFiles())
            {
                if (!xls.isDirectory() && xls.getName().endsWith("xls"))
                {
                    ExcelFile excelFile = new ExcelFile(path, xls.getName());
                    fileMap.put(xls.getName(), excelFile);
                }
            }

            System.err.println("********excel:" + path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public List<String> getDatabaseOrExcel()
    {
        List<String> excelList = new ArrayList<>();
        excelList.addAll(fileMap.keySet());
        return excelList;
    }

    @Override
    public List<String> getTables(String name)
    {
        List<String> list = new ArrayList<>();
        ExcelFile file = fileMap.get(name);
        if (file != null)
            list = file.getTableNameList();
        return list;
    }

    @Override
    public Map<String, FieldInfo> getTableFieldList(String name, String tableName)
    {
        ExcelFile file = fileMap.get(name);
        if (file != null)
            return file.getTableColumnMap(tableName);

        return null;
    }

    @Override
    public String getCommont(String name)
    {
        for (Entry<String, ExcelFile> entry : fileMap.entrySet())
        {
            List<ExcelSheet> list = entry.getValue().getSheetList();
            for (ExcelSheet sheet : list)
            {
                if (sheet.getTableName().equals(name))
                    return entry.getKey() + " : " + sheet.sheetName + " - " + sheet.getTableName();
            }
        }

        return null;
    }

    @Override
    public List<OneRowData> getData(String name)
    {
        for (Entry<String, ExcelFile> entry : fileMap.entrySet())
        {
            List<ExcelSheet> list = entry.getValue().getSheetList();
            for (ExcelSheet sheet : list)
            {
                if (sheet.getTableName().equals(name))
                {
                    return sheet.getData();
                }
            }
        }

        return null;
    }

}
