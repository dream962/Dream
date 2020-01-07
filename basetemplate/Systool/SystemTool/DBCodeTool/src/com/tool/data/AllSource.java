package com.tool.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tool.code.FieldInfo;
import com.tool.data.ExcelSheet.OneRowData;

/**
 * DB 和 Excel的数据
 * 
 * @author dream
 *
 */
public class AllSource implements IDataSource
{
    public ExcelSource excelSource;
    public DBSource dBSource;
    private String config;

    public AllSource(String config)
    {
        this.config = config;
    }

    public void initDB()
    {
        if (dBSource == null)
            dBSource = new DBSource();

        dBSource.init(config);
    }

    public void initExcel()
    {
        if (excelSource == null)
            excelSource = new ExcelSource();

        excelSource.init(config);
    }

    @Override
    public List<String> getDatabaseOrExcel()
    {
        List<String> list = new ArrayList<>();
        if (excelSource != null)
            list.addAll(excelSource.getDatabaseOrExcel());
        if (dBSource != null)
            list.addAll(dBSource.getDatabaseOrExcel());
        return list;
    }

    @Override
    public List<String> getTables(String name)
    {
        if (excelSource != null)
        {
            List<String> list = excelSource.getTables(name);
            if (!list.isEmpty())
                return list;
        }

        if (dBSource != null)
            return dBSource.getTables(name);

        return null;
    }

    @Override
    public Map<String, FieldInfo> getTableFieldList(String name, String tableName)
    {
        if (excelSource != null)
        {
            Map<String, FieldInfo> map = excelSource.getTableFieldList(name, tableName);
            if (map != null && !map.isEmpty())
                return map;
        }

        return dBSource.getTableFieldList(name, tableName);
    }

    @Override
    public String getCommont(String name)
    {
        if (excelSource != null && excelSource.getCommont(name) != null)
            return excelSource.getCommont(name);

        return dBSource.getCommont(name);
    }

    @Override
    public List<OneRowData> getData(String name)
    {
        if (excelSource != null && excelSource.getData(name) != null)
            return excelSource.getData(name);

        return dBSource.getData(name);
    }
}
