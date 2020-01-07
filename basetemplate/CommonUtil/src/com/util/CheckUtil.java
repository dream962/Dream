package com.util;

import org.slf4j.Logger;

public class CheckUtil
{
    public static <T> T checkNull(Logger logger, T object, Class<T> c, int keyID)
    {
        if (object == null)
        {
            logger.warn(String.format("%s为null:%d", c.getName(), keyID));
        }

        return object;
    }

    public static <T> T checkNull(Logger logger, T object, String name, int keyID)
    {
        if (object == null)
        {
            logger.warn(String.format("%s为null:%d", name, keyID));
        }

        return object;
    }

    public static <T> T checkNull(Logger logger, T object, Class<T> c, int keyID, int keyID2)
    {
        if (object == null)
        {
            logger.warn(String.format("%s为null:%d %d", c.getName(), keyID, keyID2));
        }

        return object;
    }
}
