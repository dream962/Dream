package com.compare.data;

import java.util.List;
import java.util.Map;

public interface IDataSource
{
    /**
     * 获得所有的文件或者数据库
     */
    public List<String> getDatabase();

    /**
     * 获得所有表名
     */
    public List<String> getTables(String name);

    /**
     * 获得数据表具体信息
     */
    public Map<String, FieldInfo> getTableFieldList(String name,String tableName);
}
