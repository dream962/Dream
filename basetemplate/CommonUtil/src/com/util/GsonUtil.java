/**
 * 
 */
package com.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @date May 4, 2017 2:46:44 PM
 * @author dansen
 * @desc
 */
public class GsonUtil
{
    public static class DateDeserializer implements JsonDeserializer<java.util.Date>
    {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException
        {
            return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    public static class DateSerializer implements JsonSerializer<Date>
    {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context)
        {
            return new JsonPrimitive(src.getTime());
        }
    }
    
    public static final Gson gson = new Gson();
    public static final Gson gsonMin = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    public static final Gson gsonSec = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static final Gson gsonDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static final Gson gsonTime = new GsonBuilder().setDateFormat("HH:mm:ss").create();
    
    //返回时间为long型整数
    public static Gson gsonTimeLong = null;

    public static Gson create()
    {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        Gson gson = gb.create();
        return gson;
    }

    static
    {
        gsonTimeLong = create();
    }

}
