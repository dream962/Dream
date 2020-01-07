package com.upload.config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
public class AllConfigList
{
    public ServerConfig server;
    
    public FightProxyConfig fightProxy;
    
    public CacheServerConfig cacheServer;
    
    public WebServerConfig web;
    
    public DatabaseConfig database;
    
    public RmiConfig rmi;
}
