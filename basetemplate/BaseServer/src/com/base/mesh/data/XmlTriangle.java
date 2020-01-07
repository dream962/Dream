package com.base.mesh.data;

// import javax.xml.bind.annotation.XmlAttribute;

public class XmlTriangle
{
    private XmlPoint[] vertices = new XmlPoint[3];
    private int areaID;

    public XmlTriangle()
    {

    }

    // @XmlElement(name="point")
    public XmlPoint[] getVertices()
    {
        return vertices;
    }

    public void setVertices(XmlPoint[] vertices)
    {
        this.vertices = vertices;
    }

    // @XmlAttribute(name="area")
    public int getAreaID()
    {
        return areaID;
    }

    public void setAreaID(int areaID)
    {
        this.areaID = areaID;
    }
}
