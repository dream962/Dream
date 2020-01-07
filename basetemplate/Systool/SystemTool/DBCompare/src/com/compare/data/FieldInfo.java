package com.compare.data;

/**
 * 字段对象：用于保存单个字段信息
 */
public class FieldInfo
{
    /** 是否自增 */
    public boolean isAotuIncreamte = false;

    /** 是否主键 */
    public boolean isPrimaryKey = false;

    public boolean isNull = true;

    /** 所在Cell的位置 */
    public int cellIndex;

    /** 字段名称 */
    public String name;

    /** 字段Java类型 */
    public String javaType;

    /** 字段数据库类型 */
    public String sqlType;

    /** 字段值长度 */
    public int len;

    /** 字段注释 */
    public String comment;

    /** 标志是否加更改说明 */
    public boolean flag = true;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getJavaType()
    {
        return javaType;
    }

    public void setJavaType(String javaType)
    {
        this.javaType = javaType;
    }

    public String getSqlType()
    {
        return sqlType;
    }

    public void setSqlType(String sqlType)
    {
        this.sqlType = sqlType;
    }

    public boolean isNull()
    {
        return isNull;
    }

    public void setNull(boolean isNull)
    {
        this.isNull = isNull;
    }

    public int getLen()
    {
        return len;
    }

    public void setLen(int len)
    {
        this.len = len;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isAotuIncreamte()
    {
        return isAotuIncreamte;
    }

    public void setAotuIncreamte(boolean isAotuIncreamte)
    {
        this.isAotuIncreamte = isAotuIncreamte;
    }

    public int getCellIndex()
    {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex)
    {
        this.cellIndex = cellIndex;
    }

    public boolean equal(FieldInfo info)
    {
        if (info.sqlType.equalsIgnoreCase("float") || info.sqlType.equalsIgnoreCase("double") || info.sqlType.equalsIgnoreCase("datetime"))
        {
            if (this.name.equalsIgnoreCase(info.name) &&
                    this.isAotuIncreamte == info.isAotuIncreamte &&
                    this.isNull == info.isNull &&
                    this.sqlType.equalsIgnoreCase(info.sqlType))
            {
                return true;
            }
        }
        else
        {
            if (this.name.equalsIgnoreCase(info.name) &&
                    this.isAotuIncreamte == info.isAotuIncreamte &&
                    this.isNull == info.isNull &&
                    this.sqlType.equalsIgnoreCase(info.sqlType) &&
                    this.len == info.len)
            {
                return true;
            }
        }

        return false;
    }
}
