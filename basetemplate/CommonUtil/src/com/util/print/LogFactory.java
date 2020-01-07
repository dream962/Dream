package com.util.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工厂
 * @author dream
 *
 */
public class LogFactory
{
    private static Logger logger = LoggerFactory.getLogger(LogFactory.class);

    public static void warn(String content, Object... arg0)
    {
        logger.debug(content, arg0);
    }

    public static void warn(String content, Throwable arg0)
    {
        logger.debug(content, arg0);
    }

    public static void error(String content, Object... arg0)
    {
        logger.error(content, arg0);
    }

    public static void error(String content, Throwable arg0)
    {
        logger.error(content, arg0);
    }

    public static void debug(String content, Object... arg0)
    {
        logger.info(content, arg0);
    }

    public static void debug(String content, Throwable arg0)
    {
        logger.info(content, arg0);
    }

    public static void info(String content, Object... arg0)
    {
        logger.info(content, arg0);
    }

    public static void info(String content, Throwable arg0)
    {
        logger.info(content, arg0);
    }
}
