package com.util.wrapper;

public class WrapQuaternion
{
    public static class EulerAngles
    {
        public float x;
        public float y;
        public float z;
    }
    
    public float w;

    public float x;

    public float y;

    public float z;
    
    public EulerAngles eulerAngles;
    
    public WrapQuaternion()
    {
        
    }
    
    public WrapQuaternion(float x, float y, float z, float w)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
    }
}
