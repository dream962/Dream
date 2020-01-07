package com.upload.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * web配置
 * 
 * @author dream
 *
 */
public class WebServerConfig
{
    public static class ServletConfig
    {
        @XmlAttribute(name = "name")
        public String name;

        @XmlAttribute(name = "classPath")
        public String classPath;

        @XmlAttribute(name = "url")
        public String url;
    }

    /** servlet的端口 */
    public int port;

    /** handler所在包名 */
    public String packages;

    /** web资源路径目录路径 */
    public String resourcePath;

    /** 是否显示文件夹信息 */
    public boolean isShowDirectory;

    /** 默认显示主页 */
    public String welcomeFile;

    /** 用于RSA解密私钥 */
    public String httpPrivateKey;
    
    /** 用于RSA加密公钥 */
    public String httpPublicKey;

    @XmlElementWrapper(name = "mapping")
    @XmlElement(name = "servlet")
    public List<ServletConfig> mapping;
}
