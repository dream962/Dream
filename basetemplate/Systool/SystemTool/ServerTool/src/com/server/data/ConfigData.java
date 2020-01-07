package com.server.data;

public class ConfigData
{
    public DataBaseConfig data = new DataBaseConfig();
    public DataBaseConfig compareData = new DataBaseConfig();
    
    public static final class DataBaseConfig
    {
        public String driverName;
        public String url;
        public String userName;
        public String password;
    }
}
