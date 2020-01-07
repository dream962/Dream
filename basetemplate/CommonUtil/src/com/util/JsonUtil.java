package com.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.util.print.LogFactory;

/**
 * JSON处理工具类
 * 
 * @author dream.wang
 *
 */
public class JsonUtil
{
    /**
     * 反序列化对象列表
     * 
     * @param path
     * @param clazz
     * @return
     */
    public static final <T> List<T> parseListObject(String json, Class<T> clazz)
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

    /**
     * 序列化对象
     * 
     * @param path
     * @param clazz
     * @return
     */
    public static final <T> T parseSingleObject(String path, Class<T> clazz)
    {
        try
        {
            String textString = FileUtil.readTxt(path, "UTF-8");
            T t = JSON.parseObject(textString, clazz);
            return t;
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
    public static final <T> List<T> parseFileToListObject(String path, Class<T> clazz)
    {
        try
        {
            String textString = FileUtil.readTxt(path, "UTF-8");
            return JSON.parseArray(textString, clazz);
        }
        catch (Exception e)
        {
            LogFactory.error("Path:" + path + " -- class:" + clazz.getName(), e);
        }
        return null;
    }

    public static final String parseObjectToString(Object obj)
    {
        try
        {
            return JSON.toJSONString(obj);
        }
        catch (Exception e)
        {
            LogFactory.error("Object:" + obj.toString(), e);
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

    public static <T> T parseJsonWithGson(String jsonData, Class<T> type)
    {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

}
