package com.tool.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class XmlCommonData
{
    public static class XmlData
    {
        @XmlAttribute(name="code")
        public String code;

        @XmlAttribute(name="desc")
        public String desc;

        @XmlAttribute(name="value")
        public int value;
        
        @XmlAttribute(name="attrDesc")
        public String attrDesc;
    }

    public static class XmlEnumData
    {
        @XmlAttribute(name="name")
        public String name;

        @XmlAttribute(name="desc")
        public String desc;

        @XmlElement(name="detail")
        public List<XmlData> data;
    }

    @XmlRootElement(name = "root")
    public static class XmlListData
    {
        @XmlElement(name="enum")
        public List<XmlEnumData> list;
    }
}
