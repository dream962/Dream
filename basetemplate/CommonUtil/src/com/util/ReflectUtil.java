/**
 * 
 */
package com.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date Sep 11, 2017 11:45:37 AM
 * @author dansen
 * @desc
 */
public class ReflectUtil
{
    public static int getInt(Object object, String name)
    {
        List<Field> allField = new ArrayList<>(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
        List<Field> fields = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));
        allField.addAll(fields);

        int value = 0;

        for (Field field : allField)
        {
            try
            {
                if (field.getName().equals(name))
                {
                    field.setAccessible(true);
                    value = field.getInt(object);
                    break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return value;
    }

    public static String getName(Object object, String name)
    {
        List<Field> allField = new ArrayList<>(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
        List<Field> fields = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));
        allField.addAll(fields);

        String value = "";

        for (Field field : allField)
        {
            try
            {
                if (field.getName().equals(name))
                {
                    field.setAccessible(true);
                    value = (String) (field.get(object) == null ? "" : field.get(object));
                    break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return value;
    }

    public static Map<String, Object> getMap(Object object)
    {
        // 定义一个返回的对象
        Map<String, Object> resp = new HashMap<String, Object>();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            String validParam = field.getName();

            try
            {
                resp.put(validParam, field.get(object));
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        return resp;
    }
}
