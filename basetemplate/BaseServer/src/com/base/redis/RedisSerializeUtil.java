package com.base.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.component.GlobalConfigComponent;
import com.util.CompressUtil;
import com.util.JsonUtil;

public class RedisSerializeUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSerializeUtil.class);

    /** 启用压缩的最小值 */
    public static int COMPRESS_COUNT = 100;

    public static byte[] serialize(Object object)
    {
        return serializeJson(object);
    }

    public static <T> T unserialize(byte[] bytes, Class<T> class1)
    {
        return unserializeJson(bytes, class1);
    }

    /********************************************** 类 保存 ******************************************************/

    public static byte[] serializeClass(Object object)
    {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            byte[] bytes = baos.toByteArray();

            if (GlobalConfigComponent.getConfig() != null && GlobalConfigComponent.getConfig().cacheServer.isCompress)
                return CompressUtil.compress(baos.toByteArray());
            else
                return bytes;
        }
        catch (Exception e)
        {
            LOGGER.error("Exception:", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unserializeClass(byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }
        ByteArrayInputStream bais = null;
        try
        {
            if (GlobalConfigComponent.getConfig() != null && GlobalConfigComponent.getConfig().cacheServer.isCompress)
                bytes = CompressUtil.decompressToBytes(bytes);

            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        }
        catch (Exception e)
        {
            LOGGER.error("Exception:", e);
        }
        return null;
    }

    /******************************************** json 保存 ****************************************************/

    public static byte[] serializeJson(Object object)
    {
        try
        {
            // 序列化
            String result = JsonUtil.parseObjectToString(object);
            byte[] bytes = result.getBytes();

            if (bytes.length > COMPRESS_COUNT && GlobalConfigComponent.getConfig() != null
                    && GlobalConfigComponent.getConfig().cacheServer.isCompress)
                return CompressUtil.compress(bytes);
            else
                return bytes;
        }
        catch (Exception e)
        {
            LOGGER.error("Exception:", e);
        }
        return null;
    }

    public static <T> T unserializeJson(byte[] bytes, Class<T> class1)
    {
        if (bytes == null)
        {
            return null;
        }

        try
        {
            if (bytes.length > COMPRESS_COUNT && GlobalConfigComponent.getConfig() != null
                    && GlobalConfigComponent.getConfig().cacheServer.isCompress)
                bytes = CompressUtil.decompressToBytes(bytes);

            // 反序列化
            String str = new String(bytes);
            return JsonUtil.parseStringToObject(str, class1);
        }
        catch (Exception e)
        {
            LOGGER.error("Exception:", e);
        }
        return null;
    }
}
