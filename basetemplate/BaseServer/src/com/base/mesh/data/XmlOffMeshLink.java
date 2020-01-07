package com.base.mesh.data;

//import javax.xml.bind.annotation.XmlAttribute;

public class XmlOffMeshLink
{
    private XmlPoint begin;
    private XmlPoint end;
    /** 是否双工通行*/
    private boolean isBiDirectional;

    public XmlOffMeshLink()
    {
    }

    public XmlPoint getBegin()
    {
        return begin;
    }

    public void setBegin(XmlPoint begin)
    {
        this.begin = begin;
    }

    public XmlPoint getEnd()
    {
        return end;
    }

    public void setEnd(XmlPoint end)
    {
        this.end = end;
    }

//    @XmlAttribute(name="isBiDirectional")
    public boolean isBiDirectional()
    {
        return isBiDirectional;
    }

    public void setBiDirectional(boolean isBiDirectional)
    {
        this.isBiDirectional = isBiDirectional;
    }
}

