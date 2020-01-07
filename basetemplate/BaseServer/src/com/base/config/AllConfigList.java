package com.base.config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
public class AllConfigList
{
    public ServerConfig server;

    public CacheServerConfig cacheServer;

    public WebServerConfig web;

    public DatabaseConfig database;

    public FightProxyConfig fightProxy;
}
