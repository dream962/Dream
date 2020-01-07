package com.base.database;

public class DBParameter
{
    private Object info;

    public DBParameter(Object info)
    {
        this.info = info;
    }

    public Object getResult()
    {
        return info;
    }

}
