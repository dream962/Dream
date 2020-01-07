package com.base.rpc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.util.print.LogFactory;

/**
 * RPC工具类
 * 
 * @author dream
 *
 */
public class RpcUtil
{
    /**
     * 反射执行方法（可以扩展cglib库等其他更高效的库）
     * 
     * @param serviceClass
     * @param serviceBean
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     */
    public static Object methodInvoke(Class<?> serviceClass, Object serviceBean, String methodName, Class<?>[] parameterTypes, Object[] parameters)
    {
        try
        {
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceBean, parameters);
            return result;
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
        }

        // FastClass serviceFastClass = FastClass.create(serviceClass);
        // int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        // return serviceFastClass.invoke(methodIndex, serviceBean, parameters);

        return null;
    }

    /**
     * 序列化 使用protostuff序列化
     * 
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj)
    {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try
        {
            // 线程安全、自带缓存处理
            Schema<T> schema = RuntimeSchema.getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            buffer.clear();
        }
        return null;
    }

    /**
     * 反序列化 protostuff反序列化
     * 
     * @param data
     * @param clazz
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz)
    {
        try
        {
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
