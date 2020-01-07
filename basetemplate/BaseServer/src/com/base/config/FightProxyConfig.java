package com.base.config;

import java.util.List;

/**
 * 战斗服代理配置
 * @author dream
 *
 */
public class FightProxyConfig
{
    public static class FightProxy
    {
//        @XmlAttribute(name="id")
        public int id;
        
//        @XmlAttribute(name="type")
        public int type;
        
//        @XmlAttribute(name="ip")
        public String ip;
        
//        @XmlAttribute(name="isOpen")
        public boolean isOpen;

//        @XmlAttribute(name="multiConnectionCount")
        public int multiConnectionCount; // TCP多线连接的数量
        
//        @XmlAttribute(name="ports")
        public String ports;
    }
    
    public String publicModulus;
    
    public String publicExponent;
    
//    @XmlElement(name="server")
    public List<FightProxy> list;
}
