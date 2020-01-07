package com.upload.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerLogUtil
{
    private static final Logger logger = LoggerFactory.getLogger(ServerLogUtil.class);
    
    public static void stoppingLog(int serverID, String serverName)
    {
        logger.error("Server is stopping.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
    
    public static void stoppedLog(int serverID, String serverName)
    {
        logger.error("Server has been stopped.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
    
    public static void startingLog(int serverID, String serverName)
    {
        logger.error("Server is starting.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
    
    public static void startedLog(int serverID, String serverName)
    {
        logger.error("Server has been started successfully.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
    
    public static void updatingLog(int serverID, String serverName)
    {
        logger.error("Server resources is Updating.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
    
    public static void updatedLog(int serverID, String serverName)
    {
        logger.error("Server resources updated successfully.... ServerID:" + serverID + ";ServerName:" + serverName);
    }
}
