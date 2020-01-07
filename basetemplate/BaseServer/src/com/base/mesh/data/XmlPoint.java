package com.base.mesh.data;

//import javax.xml.bind.annotation.XmlAttribute;

import com.util.MathsUtil;

public class XmlPoint
{
    private float x;
    private float y;

    public XmlPoint()
    {

    }

//    @XmlAttribute(name="x")
    public float getX()
    {
        x= MathsUtil.round(x,4);
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

//    @XmlAttribute(name="y")
    public float getY()
    {
        y= MathsUtil.round(y, 4);
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}

