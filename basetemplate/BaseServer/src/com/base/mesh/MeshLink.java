package com.base.mesh;

public class MeshLink
{
    private int linkID;
    private MeshTriangle beginTriangle;
    private MeshTriangle endTriangle;
    
    private MeshPoint beginPoint;
    private MeshPoint endPoint;
    
    public MeshLink(MeshPoint begin,MeshPoint end)
    {
        this.beginPoint=begin;
        this.beginPoint.type=MeshPoint.JUMP_START;
        this.endPoint=end;
        this.endPoint.type=MeshPoint.JUMP_END;
    }
    
    public int getBeginArea()
    {
        return beginTriangle.getAreaID();
    }
    
    public int getEndArea()
    {
        return endTriangle.getAreaID();
    }

    public MeshTriangle getBeginTriangle()
    {
        return beginTriangle;
    }

    public void setBeginTriangle(MeshTriangle beginTriangle)
    {
        this.beginTriangle = beginTriangle;
    }

    public MeshTriangle getEndTriangle()
    {
        return endTriangle;
    }

    public void setEndTriangle(MeshTriangle endTriangle)
    {
        this.endTriangle = endTriangle;
    }

    public MeshPoint getBeginPoint()
    {
        return beginPoint;
    }

    public void setBeginPoint(MeshPoint beginPoint)
    {
        this.beginPoint = beginPoint;
    }

    public MeshPoint getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(MeshPoint endPoint)
    {
        this.endPoint = endPoint;
    }

    public int getLinkID()
    {
        return linkID;
    }

    public void setLinkID(int linkID)
    {
        this.linkID = linkID;
    }
}
