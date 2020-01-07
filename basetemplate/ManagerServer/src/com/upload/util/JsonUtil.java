package com.upload.util;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * JSON处理工具类
 * 
 * @author dream.wang
 *
 */
public class JsonUtil
{
    public static final String parseObjectToString(Object obj)
    {
        try
        {
            return JSON.toJSONString(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public static final <T> T parseStringToObject(String content, Class<T> clazz)
    {
        try
        {
            return JSON.parseObject(content, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象列表
     * 
     * @param path
     * @param clazz
     * @return
     */
    public static final <T> List<T> parseJsonToListObject(String json, Class<T> clazz)
    {
        try
        {
            return JSON.parseArray(json, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
