package com.data.info;

import java.io.Serializable;

public class TablesInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 表ID
     */
    private int tableID;

    /**
     * 表ID
     */
    private int value;

    /**
     * 表ID
     */
    public int getTableID()
    {
        return tableID;
    }

    /**
     * 表ID
     */
    public void setTableID(int tableID)
    {
        this.tableID = tableID;
    }

    /**
     * 表ID
     */
    public int getValue()
    {
        return value;
    }

    /**
     * 表ID
     */
    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * x.clone() != x
     */
    public TablesInfo clone()
    {
        TablesInfo clone = new TablesInfo();
        clone.setTableID(this.getTableID());
        clone.setValue(this.getValue());
        return clone;
    }

    /**
     * 重置信息
     */
    public void reset(TablesInfo info)
    {
        this.setTableID(info.getTableID());
        this.setValue(info.getValue());
    }

}
