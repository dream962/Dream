package com.data.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelTool
{
    private static final Logger LOGGER=LoggerFactory.getLogger(ExcelTool.class);
    
    public static Object createObject(String className,Map<String, String> data)
    {
        String desc="";
        try
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = loader.loadClass(className);
            Object instance = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for(Field field : fields)
            {
                //如果字段final修饰
                if(Modifier.isFinal(field.getModifiers()))
                    continue;
                
                field.setAccessible(true);
                String type = field.getGenericType().getTypeName();
                String value= "";
                for(Entry<String, String> entry : data.entrySet())
                {
                    if(entry.getKey().equalsIgnoreCase(field.getName()))
                    {
                        value=entry.getValue();
                        break;
                    }
                }
                
                desc=className+":"+field.getName()+",type:"+type+",value:"+value;
                
                if(type.equals("int"))
                {
                    int setValue=value.trim().isEmpty()?0:Integer.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if(type.equals("long"))
                {
                    long setValue=value.trim().isEmpty()?0:Long.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("java.lang.String")) 
                {
                    field.set(instance, value);
                }
                else if (type.equals("boolean")) 
                {
                    boolean setValue=false;
                    if(value.isEmpty() || value.equals("0"))
                        setValue=false;
                    else
                        setValue=true;
                    field.set(instance, setValue);
                }
                else if (type.equals("float")) 
                {
                    float setValue=value.trim().isEmpty()?0.0f:Float.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("double")) 
                {
                    double setValue=value.trim().isEmpty()?0:Double.valueOf(value.trim());
                    field.set(instance, setValue);
                }
                else if (type.equals("java.util.Date")) 
                {
                    if(value.isEmpty())
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.set(2000, 1,1);
                        Date date= new Date(cal.getTimeInMillis());
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
            LOGGER.error("Excel Initialize Exception:"+desc,e);
        }
        return null;
    }
}
