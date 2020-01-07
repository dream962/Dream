package com.tool.data;

import java.util.List;
import java.util.Map;

import com.tool.code.FieldInfo;
import com.tool.data.ExcelSheet.OneRowData;

public interface IDataSource
{
    /**
     * 获得所有的文件或者数据库
     */
    public List<String> getDatabaseOrExcel();

    /**
     * 获得所有表名
     */
    public List<String> getTables(String name);

    /**
     * 获得数据表具体信息
     */
    public Map<String, FieldInfo> getTableFieldList(String name,String tableName);
    
    /**
     * 取得注释
     * @param name
     * @return
     */
    public String getCommont(String name);
    
    /**
     * 取得所有数据
     * @param name
     * @return
     */
    public List<OneRowData> getData(String name);
}
