package com.upload.data;

public class ResponseInfo
{
    private int code;

    private Object json;

    public ResponseInfo()
    {
    }

    public ResponseInfo(int code, Object json)
    {
        this.code = code;
        this.json = json;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public Object getJson()
    {
        return json;
    }

    public void setJson(Object json)
    {
        this.json = json;
    }

}
