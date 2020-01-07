/**
 * 
 */
package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @date 2016年12月16日 下午3:34:04
 * @author dansen
 * @desc
 */
public class ActionUtil
{
    private static final Logger logger = LoggerFactory.getLogger(ActionUtil.class);

    /**
     * 设置类的变量值
     * 
     * @param object
     * @param jsonObject
     */
    public static void setTemplateClassField(Object object, JsonObject jsonObject)
    {
        List<Field> allField = new ArrayList<>(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
        List<Field> fields = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));

        allField.addAll(fields);

        for (Field field : allField)
        {
            try
            {
                if (field == null || jsonObject == null)
                {
                    continue;
                }
                
                JsonElement element = jsonObject.get(field.getName());
                
                if (element != null)
                {
                    field.setAccessible(true);
                    if (field.getType().isPrimitive())
                    {
                        String typeName = field.getType().getName();
                        switch (typeName)
                        {
                        case "long":
                            if (!element.getAsString().isEmpty())
                            {
                                field.setLong(object, element.getAsLong());
                            }
                            break;
                        case "int":
                            if (!element.getAsString().isEmpty())
                            {
                                field.setInt(object, element.getAsInt());
                            }
                            break;
                        case "boolean":
                            if (!element.getAsString().isEmpty())
                            {
                                field.setBoolean(object, element.getAsBoolean());
                            }
                            break;
                        default:
                            break;
                        }
                    }
                    else
                    {
                        if (field.getType() == String.class)
                        {
                            field.set(object, element.getAsString());
                        }
                        else if (field.getType() == Date.class)
                        {
                            String val = element.getAsString();
                            if (val.length() == 16)
                            {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                field.set(object, format.parse(val));
                            }
                            else if (val.length() == 19)
                            {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                field.set(object, format.parse(val));
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("template设置值出错:" + field, e);
            }
        }
    }

    /**
     * @param jsonServlet
     */
    public static void setTemplateClassFieldZeor(Object object)
    {
        List<Field> allField = new ArrayList<>(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
        List<Field> fields = new ArrayList<>(Arrays.asList(object.getClass().getDeclaredFields()));

        allField.addAll(fields);

        for (Field field : allField)
        {
            try
            {
                field.setAccessible(true);

                if (field.getType().isPrimitive() && !Modifier.isFinal(field.getModifiers()))
                {
                    String typeName = field.getType().getName();
                    switch (typeName)
                    {
                    case "long":
                        field.setLong(object, 0L);
                        break;
                    case "int":
                        field.setInt(object, 0);
                        break;
                    case "boolean":
                        field.setBoolean(object, false);
                        break;
                    default:
                        break;
                    }
                }
                else
                {
                    if (field.getType() == String.class)
                    {
                        field.set(object, null);
                    }
                    else if (field.getType() == Date.class)
                    {
                        field.set(object, null);
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("template设置值出错:" + field, e);
            }
        }
    }
}
