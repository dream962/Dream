package com.upload.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ServerConfig
{
    public String keyName;
    public String url;
    public String username;
    public String password;

    public String serverUrl;
    public String mailKey;
    public String mailID;

    @XmlElementWrapper(name = "mailSender")
    @XmlElement(name = "detail")
    public List<String> mailSender;
}
