package com.util.wrapper;

/**
 * 圆的封装
 * @author dream
 *
 */
public class WrapCircle
{
    public int x;//位置x
    
    public int y;//位置y
    
    public int radius;//半径
    
    public WrapCircle(int radius)
    {
        this.radius=radius;
    }
    
    public WrapCircle(int x,int y,int radius)
    {
        this.radius=radius;
        this.x=x;
        this.y=y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    
    @Override
    public WrapCircle clone()
    {
        WrapCircle circle=new WrapCircle(this.radius);
        circle.x=this.x;
        circle.y=this.y;
        
        return circle;
    }
}
