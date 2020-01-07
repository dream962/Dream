package com.tool.excel;

/**
 * 字段对象：用于保存单个字段信息
 */
public class FieldInfo
{
    /**是否主键*/
    public boolean isPrimaryKey;
    
    /** 所在Cell的位置*/
    public int cellIndex;

    /**字段名称*/
    public String name;

    /**字段Java类型*/
    public String javaType;
    
    public String toLowerCaseName()
    {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
