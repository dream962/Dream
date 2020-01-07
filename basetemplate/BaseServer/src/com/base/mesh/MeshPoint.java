package com.base.mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshPoint 
{
    public static final int VERTEX_POINT=0;//顶点
    public static final int START_POINT=1;//起点
    public static final int END_POINT=2;//终点
    public static final int TRANSFORM_POINT=3;//拐点
    public static final int JUMP_START=4;//跳转起点
    public static final int JUMP_END=5;//跳转终点
    
    public float x;
    public float y;
    /** 网格点类型*/
    public int type;
    /** 所属区域ID*/
    public int areaID;

    /**
     * 以当前点作为终点的线段集合
     */
    private List<MeshEdge> edgeList;

    public List<MeshEdge> getEdgeList()
    {
        if (this.edgeList == null)
            this.edgeList = new ArrayList<MeshEdge>();
        return this.edgeList;
    }

    /**
     * 得到标准长度
     * @return
     */
    public int getLength()
    {
       return (int)Math.sqrt((x * x + y * y));
    }
    
    public MeshPoint()
    {
        this.x=0;
        this.y=0;
        this.type=MeshPoint.VERTEX_POINT;
    }

    public MeshPoint(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setZero()
    {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     *  Negate this point.
     */
    public void neg()
    {
        this.x = -this.x;
        this.y = -this.y;
    }

    /**
     *  Add a point to this point.
     * @param v
     */
    public void add(MeshPoint v)
    {
        this.x += v.x;
        this.y += v.y;
    }

    /**
     *  Subtract a point from this point.
     * @param v
     */
    public void sub(MeshPoint v)
    {
        this.x -= v.x;
        this.y -= v.y;
    }

    public MeshPoint subtract(MeshPoint v)
    {
        return new MeshPoint(x - v.x, y - v.y);
    }

    /**
     *  Multiply this point by a scalar.
     * @param s
     */
    public void mul(float s)
    {
        this.x *= s;
        this.y *= s;
    }

    /**
     * 标准化当前点
     * @return
     */
    public float normalize()
    {
        float cachedLength = getLength();
        this.x /= cachedLength;
        this.y /= cachedLength;
        return cachedLength;
    }

    @Override
    public boolean equals(Object that)
    {
        MeshPoint p=(MeshPoint)that;
        return (this.x == p.x) && (this.y == p.y);
    }

    @Override
    public MeshPoint clone()
    {
        return new MeshPoint(x, y);
    }

    /**
     * 得到点列表中不重复的点
     * @param nonUniqueList
     * @return
     */
    public static List<MeshPoint> getUniqueList(List<MeshPoint> nonUniqueList)
    {
        Map<String, Boolean> pointsMap = new HashMap<>();
        List<MeshPoint> uniqueList = new ArrayList<MeshPoint>();

        for(MeshPoint point : nonUniqueList)
        {
            String hash = point.toString();
            if (pointsMap.get(hash) == null)
            {
                pointsMap.put(hash, true);
                uniqueList.add(point);
            }
        }

        return uniqueList;
    }

    public static void sortPoints(List<MeshPoint> points)
    {
        points.sort(new Comparator<MeshPoint>()
        {
            @Override
            public int compare(MeshPoint l, MeshPoint r)
            {
                float ret = l.y - r.y;
                if (ret == 0) ret = l.x - r.x;
                if (ret < 0) return -1;
                if (ret > 0) return +1;
                return 0;
            }
        });
    }
    
    /**
     * 判断abc三个点的排列顺序
                     向量叉积判断 
                    如果AB*AC大于0,则三角形ABC是逆时针的
                    如果AB*AC小于0,则三角形ABC是顺时针的
                    设A(x1,y1),B(x2,y2),C(x3,y3),则三角形两边的矢量分别是：
                    AB=(x2-x1,y2-y1), AC=(x3-x1,y3-y1)
                    则AB和AC的叉积为：(2*2的行列式)
                    |x2-x1, y2-y1|
                    |x3-x1, y3-y1|
                    值为：(x2-x1)*(y3-y1) - (y2-y1)*(x3-x1)
     * @param pa
     * @param pb
     * @param pc
     * @return
     */
    public static int orient2d(MeshPoint pa, MeshPoint pb, MeshPoint pc)
    {
        float detleft = (pa.x - pc.x) * (pb.y - pc.y);
        float detright = (pa.y - pc.y) * (pb.x - pc.x);
        float val = detleft - detright;

        if (val > -Float.MIN_EXPONENT && val < Float.MAX_EXPONENT)
            return PointOrderType.COLLINEAR;

        if (val > 0)
            return PointOrderType.CCW;

        return PointOrderType.CW;
    }

    @Override
    public String toString()
    {
        return "MeshPoint(" + x + "f, "+"0.1f," + y + "f) -- Type:"+type+"  areaID:"+areaID;
    }
}
