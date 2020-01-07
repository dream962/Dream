package com.util.wrapper;

/**
 * 点的封装
 * @author dream
 *
 */
public class WrapPoint
{
    public int x;

    public int y;

    public WrapPoint(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    
    public WrapPoint()
    {
        this(0, 0);
    }
    
    public void reset(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void translate(int dx, int dy) 
    {
        this.x += dx;
        this.y += dy;
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
    
    public WrapPoint clone()
    {
        return new WrapPoint(x, y);
    }
}
