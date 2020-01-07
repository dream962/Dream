package com.util.wrapper;

/**
 * 向量的封装
 * 
 * @author dream
 *
 */
public class WrapVector
{
    public int x;

    public int y;

    public WrapVector()
    {
        x = 0;
        y = 0;
    }

    public WrapVector(int x, int y)
    {
        this.x = x;
        this.y = y;
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

    @Override
    public WrapVector clone()
    {
        return new WrapVector(x, y);
    }
}
