package com.tool;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.tool.excel.ExcelFactory;

public class ExcelToJsonMain
{
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.err.println("请输入配置文件");
            return;
        }

        InputStream in = null;
        try
        {
            in = new FileInputStream(args[0]);
            Properties prop = new Properties();
            prop.load(in);

            String excelPath = prop.getProperty("excel");
            excelPath = new String(excelPath.getBytes("ISO-8859-1"), "UTF-8");

            String jsonPath = prop.getProperty("json");
            jsonPath = new String(jsonPath.getBytes("ISO-8859-1"), "UTF-8");

            ExcelFactory.createJsonFileList(excelPath, jsonPath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
