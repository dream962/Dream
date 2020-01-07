package com.base.mesh;


public class MeshLine
{
    public MeshPoint start;
    public MeshPoint end;

    public MeshLine(MeshPoint start, MeshPoint end) 
    {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() 
    {
        return "MeshLine:{ " + start.toString() + " , " + end.toString() + "}";
    }
}
