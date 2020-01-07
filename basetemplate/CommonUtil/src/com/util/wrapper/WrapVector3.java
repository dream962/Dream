package com.util.wrapper;

/**
 * 向量的封装
 * @author dream
 *
 */
public class WrapVector3
{
    public int x;
    
    public int y;
    
    public int z;
    
    public WrapVector3()
    {
        x=0;
        y=0;
        z=0;
    }
    
    public WrapVector3(String str)
    {
        String[] temp = str.split("\\,");
        if (temp.length >= 3)
        {
            this.x = Integer.valueOf(temp[0]);
            this.y = Integer.valueOf(temp[1]);
            this.z = Integer.valueOf(temp[2]);
        }
    }
    
    public WrapVector3(int x, int y,int z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    @Override
    public WrapVector3 clone()
    {
        return new WrapVector3(x, y,z);
    }
}
