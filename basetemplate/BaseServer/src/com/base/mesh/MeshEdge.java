package com.base.mesh;

import java.util.ArrayList;
import java.util.List;


public class MeshEdge
{
    public MeshPoint startPoint;
    public MeshPoint endPoint;

    public MeshEdge(MeshPoint p1, MeshPoint p2)
    {
        if (p1 == null || p2 == null)
            return;

        boolean swap = false;

        if (p1.y > p2.y)
        {
            swap = true;
        }
        else if (p1.y == p2.y)
        {
            swap = (p1.x > p2.x);
        }
        else
        {
            swap = false;
        }

        if (swap)
        {
            this.endPoint = p1;
            this.startPoint = p2;
        }
        else
        {
            this.startPoint = p1;
            this.endPoint = p2;
        }

        this.endPoint.getEdgeList().add(this);
    }

    /**
     * 判断点在线段的方向（左侧，右侧，线上）
     * 向量叉乘算法 -1 右边 1 左边 0 线上
     * 
     * @param pt
     * @param epsilon
     * @return
     */
    public int classifyPoint(MeshPoint pt, float epsilon)
    {
        float xa = endPoint.x - startPoint.x;
        float ya = endPoint.y - startPoint.y;

        float xb = pt.x - startPoint.x;
        float yb = pt.y - startPoint.y;

        float s = xa * yb - xb * ya;
        if (s == 0)
            return 0;
        if (s > 0)
            return 1;
        if (s < 0)
            return -1;

        return 0;
    }

    /**
     * 判断点在线段的方向（左侧，右侧，线上）
     * 向量叉乘算法 -1 右边 1 左边 0 线上
     * 
     * @param pt
     * @param p
     * @param q
     * @return -1右边   1左边   0线上
     */
    public static int classifyPoint(MeshPoint pt, MeshPoint p, MeshPoint q)
    {
        float xa = q.x - p.x;
        float ya = q.y - p.y;

        float xb = pt.x - p.x;
        float yb = pt.y - p.y;

        float s = xa * yb - xb * ya;
        if (s == 0)
            return 0;
        if (s > 0)
            return 1;
        if (s < 0)
            return -1;

        return 0;
    }

    static public List<MeshPoint> getUniquePointsFromEdges(List<MeshEdge> edges)
    {
        List<MeshPoint> points = new ArrayList<MeshPoint>();
        for (MeshEdge edge : edges)
        {
            points.add(edge.startPoint);
            points.add(edge.endPoint);
        }
        return MeshPoint.getUniqueList(points);
    }

    @Override
    public String toString()
    {
        return "MeshEdge:{" + this.startPoint.toString() + ", " + this.endPoint.toString() + "}";
    }
}
