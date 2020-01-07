package com.base.mesh;

public class MeshPathPoint
{
    public MeshTriangle triangle;
    public MeshPoint point;
    /**
     * 是否已经被添加，防止重复添加（当拐点是多个三角形的顶点时，可能会重复）
     */
    public boolean isAdd;

    public MeshPathPoint(MeshTriangle tri, MeshPoint pt)
    {
        this.triangle = tri;
        this.point = pt;
        this.isAdd=false;
    }
}
