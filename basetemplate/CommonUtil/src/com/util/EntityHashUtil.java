package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.util.print.LogFactory;

/**
 * 实体与hash转换
 * 
 * @author dream
 *
 */
public class EntityHashUtil
{
    /**
     * 对象转散列表
     * 
     * @param obj
     * @return
     */
    public static Map<String, String> objectToHash(Object obj)
    {
        Map<String, String> result = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);

            String type = field.getType().getSimpleName();

            // 静态字段排除
            if (Modifier.isStatic(field.getModifiers()))
            {
                continue;
            }

            Object val = new Object();
            try
            {
                val = field.get(obj);
                if (type.equalsIgnoreCase("byte[]"))
                {
                    if (val == null)
                    {
                        result.put(field.getName(), null);
                    }
                    else
                    {
                        result.put(field.getName(), new String((byte[]) val));
                    }
                }
                else if (type.equalsIgnoreCase("Date"))
                {
                    if (val == null)
                    {
                        result.put(field.getName(), String.valueOf(new Date().getTime()));
                    }
                    else
                    {
                        result.put(field.getName(), String.valueOf(((Date) val).getTime()));
                    }
                }
                else
                {
                    result.put(field.getName(), val.toString());
                }
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
                LogFactory.error("EntityHashUtil.objectToHash() Exception: ", e);
                return null;
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                LogFactory.error("EntityHashUtil.objectToHash() Exception:", e);
                return null;
            }
        }

        return result;
    }

    /**
     * 散列表转对象
     * 
     * @param map
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T hashToObject(Map<String, String> map, Class<?> t)
    {
        try
        {
            Object obj = t.newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                field.setAccessible(true);
                String value = map.get(field.getName());
                String type = field.getType().getSimpleName();

                // 静态字段不设置
                if (Modifier.isStatic(field.getModifiers()))
                {
                    continue;
                }

                if (type.equalsIgnoreCase("string"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, value);
                    else
                        field.set(obj, "");
                }
                else if (type.equalsIgnoreCase("Date"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, new Date(Long.valueOf(value)));
                    else
                        field.set(obj, null);
                }
                else if (type.equalsIgnoreCase("byte[]"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, value.getBytes());
                    else
                        field.set(obj, null);
                }
                else if (type.equalsIgnoreCase("byte"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Byte.valueOf(value));
                    else
                        field.set(obj, (byte) 0);
                }
                else if (type.equalsIgnoreCase("boolean"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Boolean.valueOf(value));
                    else
                        field.set(obj, false);
                }
                else if (type.equalsIgnoreCase("int") || type.equals("Integer"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Integer.valueOf(value));
                    else
                        field.set(obj, 0);
                }
                else if (type.equalsIgnoreCase("short") || type.equals("Short"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Short.valueOf(value));
                    else
                        field.set(obj, (short) 0);
                }
                else if (type.equalsIgnoreCase("double"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Double.valueOf(value));
                    else
                        field.set(obj, 0);
                }
                else if (type.equalsIgnoreCase("long"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Long.valueOf(value));
                    else
                        field.set(obj, 0);
                }
                else if (type.equalsIgnoreCase("BigDecimal"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, new BigDecimal(value));
                    else
                        field.set(obj, 0);
                }
                else if (type.equalsIgnoreCase("float"))
                {
                    if (value != null && !value.isEmpty())
                        field.set(obj, Float.valueOf(value));
                    else
                        field.set(obj, 0);
                }
                else
                {
                    LogFactory.error("不支持的数据类型：" + field.getName() + " : " + type);
                    return null;
                }
            }

            return (T) obj;
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            LogFactory.error("EntityHashUtil.hashToObject() transform Exception:", e);
        }
        return null;
    }

}
