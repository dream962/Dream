package com.upload.adapter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-25 21:42:47
 * @description
 *              Gson适配器，解决Gson无法将空字符串转换为Integer(int)类型
 */
public class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer>
{
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context)
            throws JsonParseException
    {
        try
        {
            if (json.getAsString().equals("") || json.getAsString().equals("null"))
            {
                return 0;
            }
        }
        catch (Exception ignore)
        {
            
        }
        try
        {
            return json.getAsInt();
        }
        catch (NumberFormatException e)
        {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(src);
    }
}
