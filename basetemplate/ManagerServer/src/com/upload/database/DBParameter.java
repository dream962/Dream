package com.upload.database;

public class DBParameter
{
    private Object info;

    public DBParameter(int Types, Object info)
    {
        this.info = info;
    }

    public Object getResult()
    {
        return info;
    }

}
