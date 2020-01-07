/**
 * 
 */
package com.util.press.proto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.GeneratedMessage;

/**
 * @date Mar 28, 2017 5:01:08 PM
 * @author dansen
 * @desc
 */
public class ProtoParser
{
    public static boolean isList(String key, GeneratedMessage message)
    {
        try
        {
            Field field = message.getClass().getDeclaredField(key + "_");
            if (field.getType().getName().equals(List.class.getTypeName()))
            {
                return true;
            }
        }
        catch (Exception e)
        {
            
        }

        return false;
    }

    private static void put(Map<String, Object> values, String key, Object value, GeneratedMessage message)
    {
        key = key.trim();

        if (value instanceof String)
        {
            value = ((String) value).trim();
        }

        if (values.containsKey(key))
        {
            if (values.get(key) instanceof List)
            {
                List<Object> objects = (List<Object>) values.get(key);
                objects.add(value);
            }
            else
            {
                List<Object> objects = new ArrayList<>();
                objects.add(values.get(key));
                objects.add(value);
                values.remove(key);
                values.put(key, objects);
            }
        }
        else
        {
            if (isList(key, message))
            {
                List<Object> objects = new ArrayList<>();
                objects.add(value);
                values.put(key, objects);
            }
            else
            {
                values.put(key, value);
            }

        }
    }

    public static Map<String, Object> parse(String data, GeneratedMessage message)
    {
        int state = 0;
        // : 1; \n && 1 -> 0;{->2; }&&2->0
        String key = "";

        Map<String, Object> values = new HashMap<>();

        String val = "";
        for (int i = 0; i < data.length(); ++i)
        {
            char c = data.charAt(i);

            if (c == ':' && state == 0)
            {
                key = val;
                val = "";
                state = 1;
                continue;
            }

            if (c == '\n' && state == 1)
            {
                put(values, key, val, message);
                val = "";
                state = 0;
                continue;
            }

            if (c == '{')
            {
                key = val;
                val = "";
                state = 2;
                continue;
            }

            if (c == '}' && state == 2)
            {
                put(values, key, parse(val.trim() + "\n", message), message);
                val = "";
                state = 0;
                continue;
            }

            val += c;
        }

        return values;
    }
}
