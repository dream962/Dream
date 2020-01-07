package com.tool.excel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelTool
{
    public static Object createObject(String className, Map<String, String> data)
    {
        try
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = loader.loadClass(className);
            Object instance = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields)
            {
                // 如果字段final修饰
                if (Modifier.isFinal(field.getModifiers()))
                    continue;

                field.setAccessible(true);
                String type = field.getGenericType().getTypeName();
                String value = "";
                for (Entry<String, String> entry : data.entrySet())
                {
                    if (entry.getKey().equalsIgnoreCase(field.getName()))
                    {
                        value = entry.getValue();
                        break;
                    }
                }

                if (type.equals("int"))
                {
                    int setValue = value.trim().isEmpty() ? 0 : Integer.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("long"))
                {
                    long setValue = value.trim().isEmpty() ? 0 : Long.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("java.lang.String"))
                {
                    field.set(instance, value);
                }
                else if (type.equals("boolean"))
                {
                    boolean setValue = false;
                    if (value.isEmpty() || value.equals("0"))
                        setValue = false;
                    else
                        setValue = true;
                    field.set(instance, setValue);
                }
                else if (type.equals("float"))
                {
                    float setValue = value.trim().isEmpty() ? 0.0f : Float.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("double"))
                {
                    double setValue = value.trim().isEmpty() ? 0 : Double.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("java.util.Date"))
                {
                    if (value.isEmpty())
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.set(2000, 1, 1);
                        Date date = new Date(cal.getTimeInMillis());
                        field.set(instance, date);
                    }
                    else
                    {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date;
                        try
                        {
                            date = df.parse(value);
                            field.set(instance, date);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                else
                {
                    field.set(instance, value);
                }
            }

            return instance;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String createJson(ExcelSheet sheet)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append("\n");
        if(sheet.dataList.isEmpty())
        {
            builder.append("]");
            return builder.toString();
        }

        for (Map<String, String> entry : sheet.dataList)
        {
            builder.append("\t{").append("\n");

            for (Entry<String, String> data : entry.entrySet())
            {
                FieldInfo fieldInfo = sheet.map.get(data.getKey().trim());

                builder.append("\t\"").append(fieldInfo.toLowerCaseName()).append("\":");

                if (fieldInfo.javaType.equalsIgnoreCase("string"))
                {
                    String content=data.getValue();
                    if(content.indexOf("\n")>=0)
                        content=content.replace("\n", "\\n");
                    if(content.indexOf("\"")>=0)
                        content=content.replace("\"", "\\\"");
                    builder.append("\"").append(content).append("\",").append("\n");
                }
                else if (fieldInfo.javaType.equalsIgnoreCase("int")
                        || fieldInfo.javaType.equalsIgnoreCase("float")
                        || fieldInfo.javaType.equalsIgnoreCase("double")
                        || fieldInfo.javaType.equalsIgnoreCase("long")
                        || fieldInfo.javaType.equalsIgnoreCase("short"))
                    builder.append(data.getValue().trim().isEmpty() ? "0" : data.getValue().trim()).append(",").append("\n");
                else if (fieldInfo.javaType.equalsIgnoreCase("boolean"))
                {
                    if (data.getValue().trim().isEmpty())
                        builder.append("false").append(",").append("\n");
                    else if (Integer.valueOf(data.getValue().trim().trim()) > 0)
                        builder.append("true").append(",").append("\n");
                    else
                        builder.append("false").append(",").append("\n");
                }
                else if (fieldInfo.javaType.equalsIgnoreCase("date"))
                    builder.append(data.getValue().trim().isEmpty() ? "\"2000-01-01 00:00:00\""
                            : data.getValue().trim()).append(",").append("\n");
                else
                    builder.append(data.getValue().trim().isEmpty() ? "0" : data.getValue().trim()).append(",").append("\n");
            }

            // 删除最后一个逗号
            builder.delete(builder.length() - 2, builder.length() - 1);

            builder.append("\t},").append("\n");
        }

        // 删除最后一个逗号
        builder.delete(builder.length() - 2, builder.length() - 1);

        builder.append("]");
        return builder.toString();
    }
}
