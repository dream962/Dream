package com.base.mesh;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeshTriangle
{
    /**
     * Return the point clockwise to the given point.
     * Return the point counter-clockwise to the given point.
     *
     * Return the neighbor clockwise to given point.
     * Return the neighbor counter-clockwise to given point.
     */
    private final int CW_OFFSET = +1;

    private final int CCW_OFFSET = -1;

    public int arrival = 0;

    /**
     * 所在区ID
     */
    private int areaID;

    /**
     * 三角形在数组中的索引
     */
    public int index = -1;

    public int sessionId;

    public float f;

    public float h;

    public boolean isOpen = false;
    /**
     * 是否可以通过
     */
    public boolean isCross = true;

    public MeshTriangle parent;

    public MeshEdge arrivalWall;

    /**
     * 各个边的中点
     */
    public MeshPoint[] wallMidpoint = new MeshPoint[3];

    /**
     * 各个边的距离(中点距离)0-1, 1-2, 2-0
     */
    public float[] wallDistance = new float[3];

    /**
     * 中心点
     */
    private MeshPoint center;

    /**
     * 包围盒
     */
    private Rectangle boundBox;

    /**
     * 各个顶点
     */
    public MeshPoint[] points = new MeshPoint[3];

    /**
     * 临边
     */
    public MeshTriangle[] neighbors = new MeshTriangle[3];

    /**
     * 是否被标记为内部三角形
     */
    public boolean interior = false;

    /**
     * 约束边
     */
    public boolean[] constrainedEdge = { false, false, false };

    /**
     * 德洛内边
     */
    public boolean[] delaunayEdge = { false, false, false };

    public MeshTriangle(MeshPoint p1, MeshPoint p2, MeshPoint p3, int orientation)
    {
        if (MeshPoint.orient2d(p1, p2, p3) != orientation)
        {
            this.points[0] = p1;
            this.points[1] = p3;
            this.points[2] = p2;
        }
        else
        {
            this.points[0] = p1;
            this.points[1] = p2;
            this.points[2] = p3;
        }
    }

    public void setCross(boolean isCross)
    {
        this.isCross = isCross;
    }

    public boolean getCross()
    {
        return this.isCross;
    }

    public void calculate()
    {
        wallMidpoint[0] = new MeshPoint((points[0].x + points[1].x) / 2.0f, (points[0].y + points[1].y) / 2.0f);
        wallMidpoint[1] = new MeshPoint((points[2].x + points[1].x) / 2.0f, (points[2].y + points[1].y) / 2.0f);
        wallMidpoint[2] = new MeshPoint((points[2].x + points[0].x) / 2.0f, (points[2].y + points[0].y) / 2.0f);

        MeshPoint wallVector;
        wallVector = wallMidpoint[0].subtract(wallMidpoint[1]);
        wallDistance[0] = wallVector.getLength();

        wallVector = wallMidpoint[1].subtract(wallMidpoint[2]);
        wallDistance[1] = wallVector.getLength();

        wallVector = wallMidpoint[2].subtract(wallMidpoint[0]);
        wallDistance[2] = wallVector.getLength();

        float x = (points[0].x + points[1].x + points[2].x) / 3;
        float y = (points[0].y + points[1].y + points[2].y) / 3;
        center = new MeshPoint(x, y);
    }

    public MeshTriangle(MeshPoint p1, MeshPoint p2, MeshPoint p3)
    {
        this(p1, p2, p3, PointOrderType.CW);
    }

    /**
     * 计算H估算值：当前三角形中点到目标点的曼哈顿距离
     * 
     * @param goal
     */
    public void computeHeuristic(MeshPoint goal)
    {
        float XDelta = Math.abs(goal.x - center.x);
        float YDelta = Math.abs(goal.y - center.y);

        h = XDelta + YDelta;
    }

    public MeshPoint getCenterPoint()
    {
        return center;
    }

    public Rectangle getBoundBox()
    {
        if (boundBox == null)
        {
            float xMin = points[0].x;
            float xMax = xMin;
            float yMin = points[0].y;
            float yMax = yMin;

            for (int i = 1; i < 2; i++)
            {
                if (points[i].x < xMin)
                    xMin = points[i].x;
                if (points[i].x > xMax)
                    xMax = points[i].x;
                if (points[i].y < yMin)
                    yMin = points[i].y;
                if (points[i].y > yMax)
                    yMax = points[i].y;
            }

            boundBox = new Rectangle((int) xMin, (int) yMin, (int) (xMax - xMin), (int) (yMax - yMin));
        }

        return boundBox;
    }

    public void setAndGetArrivalWall(int index)
    {
        MeshTriangle t = null;

        if (neighbors[0] != null && index == neighbors[0].index)
        {
            t = neighbors[0];
            arrival = 0;
        }
        else if (neighbors[1] != null && index == neighbors[1].index)
        {
            t = neighbors[1];
            arrival = 1;
        }
        else if (neighbors[2] != null && index == neighbors[2].index)
        {
            t = neighbors[2];
            arrival = 2;
        }

        // m_ArrivalWall = getNeighborEdge(t);
    }

    public MeshEdge getNeighborEdge(MeshTriangle t)
    {
        List<MeshPoint> res = new ArrayList<MeshPoint>();

        if (t.points[0].equals(points[0]) || t.points[0].equals(points[1]) || t.points[0].equals(points[2]))
            res.add(t.points[0].clone());
        if (t.points[1].equals(points[0]) || t.points[1].equals(points[1]) || t.points[1].equals(points[2]))
            res.add(t.points[1].clone());
        if (t.points[2].equals(points[0]) || t.points[2].equals(points[1]) || t.points[2].equals(points[2]))
            res.add(t.points[2].clone());
        if (res.size() == 2)
            return new MeshEdge(res.get(0), res.get(1));

        return null;
    }

    /**
     * Test if this Triangle contains the Point object given as parameter as its vertices.
     *
     * @return <code>True</code> if the Point objects are of the Triangle's vertices,
     *         <code>false</code> otherwise.
     */
    public boolean containsPoint(MeshPoint point)
    {
        return point.equals(points[0]) || point.equals(points[1]) || point.equals(points[2]);
    }

    /**
     * Test if this Triangle contains the Edge object given as parameters as its bounding edges.
     * 
     * @return <code>True</code> if the Edge objects are of the Triangle's bounding
     *         edges, <code>false</code> otherwise.
     */
    public boolean containsEdge(MeshEdge edge)
    {
        // In a triangle to check if contains and edge is enough to check if it contains the two vertices.
        return containsEdgePoints(edge.startPoint, edge.endPoint);
    }

    public boolean containsEdgePoints(MeshPoint p1, MeshPoint p2)
    {
        // In a triangle to check if contains and edge is enough to check if it contains the two vertices.
        return containsPoint(p1) && containsPoint(p2);
    }

    /**
     * Update neighbor pointers.<br>
     * This method takes either 3 parameters (<code>p1</code>, <code>p2</code> and
     * <code>t</code>) or 1 parameter (<code>t</code>).
     * 
     * @param t
     *            Triangle object.
     * @param p1
     *            Point object.
     * @param p2
     *            Point object.
     */
    public void markNeighbor(MeshTriangle t, MeshPoint p1, MeshPoint p2)
    {
        if ((p1.equals(this.points[2]) && p2.equals(this.points[1]))
                || (p1.equals(this.points[1]) && p2.equals(this.points[2])))
        {
            this.neighbors[0] = t;
            return;
        }
        if ((p1.equals(this.points[0]) && p2.equals(this.points[2]))
                || (p1.equals(this.points[2]) && p2.equals(this.points[0])))
        {
            this.neighbors[1] = t;
            return;
        }
        if ((p1.equals(this.points[0]) && p2.equals(this.points[1]))
                || (p1.equals(this.points[1]) && p2.equals(this.points[0])))
        {
            this.neighbors[2] = t;
            return;
        }
    }

    public void markNeighborTriangle(MeshTriangle that)
    {
        // exhaustive search to update neighbor pointers
        if (that.containsEdgePoints(this.points[1], this.points[2]))
        {
            this.neighbors[0] = that;
            that.markNeighbor(this, this.points[1], this.points[2]);
            return;
        }

        if (that.containsEdgePoints(this.points[0], this.points[2]))
        {
            this.neighbors[1] = that;
            that.markNeighbor(this, this.points[0], this.points[2]);
            return;
        }

        if (that.containsEdgePoints(this.points[0], this.points[1]))
        {
            this.neighbors[2] = that;
            that.markNeighbor(this, this.points[0], this.points[1]);
            return;
        }
    }

    public int getPointIndexOffset(MeshPoint p, int offset)
    {
        int no = offset;
        for (int n = 0; n < 3; n++, no++)
        {
            while (no < 0)
                no += 3;
            while (no > 2)
                no -= 3;
            if (p.equals(this.points[n]))
                return no;
        }
        return 0;
    }

    public MeshPoint pointCW(MeshPoint p)
    {
        return this.points[getPointIndexOffset(p, CCW_OFFSET)];
    }

    public MeshPoint pointCCW(MeshPoint p)
    {
        return this.points[getPointIndexOffset(p, CW_OFFSET)];
    }

    public MeshTriangle neighborCW(MeshPoint p)
    {
        return this.neighbors[getPointIndexOffset(p, CW_OFFSET)];
    }

    public MeshTriangle neighborCCW(MeshPoint p)
    {
        return this.neighbors[getPointIndexOffset(p, CCW_OFFSET)];
    }

    public boolean getConstrainedEdgeCW(MeshPoint p)
    {
        return this.constrainedEdge[getPointIndexOffset(p, CW_OFFSET)];
    }

    public boolean setConstrainedEdgeCW(MeshPoint p, boolean ce)
    {
        return this.constrainedEdge[getPointIndexOffset(p, CW_OFFSET)] = ce;
    }

    public boolean getConstrainedEdgeCCW(MeshPoint p)
    {
        return this.constrainedEdge[getPointIndexOffset(p, CCW_OFFSET)];
    }

    public boolean setConstrainedEdgeCCW(MeshPoint p, boolean ce)
    {
        return this.constrainedEdge[getPointIndexOffset(p, CCW_OFFSET)] = ce;
    }

    public boolean getDelaunayEdgeCW(MeshPoint p)
    {
        return this.delaunayEdge[getPointIndexOffset(p, CW_OFFSET)];
    }

    public boolean setDelaunayEdgeCW(MeshPoint p, boolean e)
    {
        return this.delaunayEdge[getPointIndexOffset(p, CW_OFFSET)] = e;
    }

    public boolean getDelaunayEdgeCCW(MeshPoint p)
    {
        return this.delaunayEdge[getPointIndexOffset(p, CCW_OFFSET)];
    }

    public boolean setDelaunayEdgeCCW(MeshPoint p, boolean e)
    {
        return this.delaunayEdge[getPointIndexOffset(p, CCW_OFFSET)] = e;
    }

    /**
     * The neighbor across to given point.
     */
    public MeshTriangle neighborAcross(MeshPoint p)
    {
        return this.neighbors[getPointIndexOffset(p, 0)];
    }

    public MeshPoint oppositePoint(MeshTriangle t, MeshPoint p)
    {
        return this.pointCW(t.pointCW(p));
    }

    /**
     * 合法化 根据某一个或两个点，顺时针排序点
     * Legalize triangle by rotating clockwise.<br>
     * This method takes either 1 parameter (then the triangle is rotated around
     * points(0)) or 2 parameters (then the triangle is rotated around the first
     * parameter).
     */
    public void legalize(MeshPoint opoint, MeshPoint npoint)
    {
        if (npoint == null)
        {
            this.legalize(this.points[0], opoint);
            return;
        }

        if (opoint.equals(this.points[0]))
        {
            this.points[1] = this.points[0];
            this.points[0] = this.points[2];
            this.points[2] = npoint;
        }
        else if (opoint.equals(this.points[1]))
        {
            this.points[2] = this.points[1];
            this.points[1] = this.points[0];
            this.points[0] = npoint;
        }
        else if (opoint.equals(this.points[2]))
        {
            this.points[0] = this.points[2];
            this.points[2] = this.points[1];
            this.points[1] = npoint;
        }
    }

    /**
     * Alias for getPointIndexOffset
     *
     * @param p
     */
    public int index(MeshPoint p)
    {
        return this.getPointIndexOffset(p, 0);
    }

    public int edgeIndex(MeshPoint p1, MeshPoint p2)
    {
        if (p1.equals(this.points[0]))
        {
            if (p2.equals(this.points[1]))
                return 2;
            if (p2.equals(this.points[2]))
                return 1;
        }
        else if (p1.equals(this.points[1]))
        {
            if (p2.equals(this.points[2]))
                return 0;
            if (p2.equals(this.points[0]))
                return 2;
        }
        else if (p1.equals(this.points[2]))
        {
            if (p2.equals(this.points[0]))
                return 1;
            if (p2.equals(this.points[1]))
                return 0;
        }
        return -1;
    }

    /**
     * Mark an edge of this triangle as constrained.<br>
     * This method takes either 1 parameter (an edge index or an Edge instance) or
     * 2 parameters (two Point instances defining the edge of the triangle).
     */
    public void markConstrainedEdgeByIndex(int index)
    {
        this.constrainedEdge[index] = true;
    }

    public void markConstrainedEdgeByEdge(MeshEdge edge)
    {
        this.markConstrainedEdgeByPoints(edge.startPoint, edge.endPoint);
    }

    public void markConstrainedEdgeByPoints(MeshPoint p, MeshPoint q)
    {
        if ((q.equals(this.points[0]) && p.equals(this.points[1]))
                || (q.equals(this.points[1]) && p.equals(this.points[0])))
        {
            this.constrainedEdge[2] = true;
            return;
        }

        if ((q.equals(this.points[0]) && p.equals(this.points[2]))
                || (q.equals(this.points[2]) && p.equals(this.points[0])))
        {
            this.constrainedEdge[1] = true;
            return;
        }

        if ((q.equals(this.points[1]) && p.equals(this.points[2]))
                || (q.equals(this.points[2]) && p.equals(this.points[1])))
        {
            this.constrainedEdge[0] = true;
            return;
        }
    }

    /**
     * Checks if a side from this triangle is an edge side.
     * If sides are not marked they will be marked.
     *
     * @param ep
     * @param eq
     * @return
     */
    public boolean isEdgeSide(MeshPoint ep, MeshPoint eq)
    {
        int index = this.edgeIndex(ep, eq);
        if (index == -1)
            return false;

        this.markConstrainedEdgeByIndex(index);
        MeshTriangle that = this.neighbors[index];
        if (that != null)
            that.markConstrainedEdgeByPoints(ep, eq);
        return true;
    }

    /**
     * Rotates a triangle pair one vertex CW
     * 
     * <pre>
     *       n2                    n2
     *  P +-----+             P +-----+
     *    | t  /|               |\  t |
     *    |   / |               | \   |
     *  n1|  /  |n3           n1|  \  |n3
     *    | /   |    after CW   |   \ |
     *    |/ oT |               | oT \|
     *    +-----+ oP            +-----+
     *       n4                    n4
     * </pre>
     */
    static public void rotateTrianglePair(MeshTriangle t, MeshPoint p, MeshTriangle ot, MeshPoint op)
    {
        MeshTriangle n1 = t.neighborCCW(p);
        MeshTriangle n2 = t.neighborCW(p);
        MeshTriangle n3 = ot.neighborCCW(op);
        MeshTriangle n4 = ot.neighborCW(op);

        boolean ce1 = t.getConstrainedEdgeCCW(p);
        boolean ce2 = t.getConstrainedEdgeCW(p);
        boolean ce3 = ot.getConstrainedEdgeCCW(op);
        boolean ce4 = ot.getConstrainedEdgeCW(op);

        boolean de1 = t.getDelaunayEdgeCCW(p);
        boolean de2 = t.getDelaunayEdgeCW(p);
        boolean de3 = ot.getDelaunayEdgeCCW(op);
        boolean de4 = ot.getDelaunayEdgeCW(op);

        t.legalize(p, op);
        ot.legalize(op, p);

        // Remap delaunay_edge
        ot.setDelaunayEdgeCCW(p, de1);
        t.setDelaunayEdgeCW(p, de2);
        t.setDelaunayEdgeCCW(op, de3);
        ot.setDelaunayEdgeCW(op, de4);

        // Remap constrained_edge
        ot.setConstrainedEdgeCCW(p, ce1);
        t.setConstrainedEdgeCW(p, ce2);
        t.setConstrainedEdgeCCW(op, ce3);
        ot.setConstrainedEdgeCW(op, ce4);

        // Remap neighbors
        // XXX: might optimize the markNeighbor by keeping track of
        // what side should be assigned to what neighbor after the
        // rotation. Now mark neighbor does lots of testing to find
        // the right side.
        t.clearNeigbors();
        ot.clearNeigbors();
        if (n1 != null)
            ot.markNeighborTriangle(n1);
        if (n2 != null)
            t.markNeighborTriangle(n2);
        if (n3 != null)
            t.markNeighborTriangle(n3);
        if (n4 != null)
            ot.markNeighborTriangle(n4);
        t.markNeighborTriangle(ot);
    }

    public void clearNeigbors()
    {
        this.neighbors[0] = null;
        this.neighbors[1] = null;
        this.neighbors[2] = null;
    }

    public void clearDelunayEdges()
    {
        this.delaunayEdge[0] = false;
        this.delaunayEdge[1] = false;
        this.delaunayEdge[2] = false;
    }

    public boolean equals(Object triangle)
    {
        MeshTriangle that = (MeshTriangle) triangle;

        for (int n = 0; n < 3; n++)
        {
            if (!this.points[n].equals(that.points[n]))
                return false;
        }
        return true;
    }

    /**
     * 判断点是否在三角形的内部
     * 
     * @param pt
     * @return
     */
    public boolean isPointInside(MeshPoint pt)
    {
        if (isOnSameSide(pt, points[0], points[1], points[2])
                || isOnSameSide(pt, points[1], points[2], points[0])
                || isOnSameSide(pt, points[2], points[0], points[1]))
            return false;

        return true;
    }

    /**
     * 判断两点是否在线的同一侧
     * 算法：首先p0,p1确定一条直线；把其余两点带进去，如果在同侧》0 不在同侧《0
     * 运用了不等式；Ax+By+C>0表示一个平面区域，Ax+By+C<>0表示另一个相对的平面区域
     * 
     * @param pt0
     * @param pt1
     * @param pStart
     * @param pEnd
     * @return
     */
    public static boolean isOnSameSide(MeshPoint pt0, MeshPoint pt1, MeshPoint pStart, MeshPoint pEnd)
    {
        float a = pt0.y - pt1.y;
        float b = pt1.x - pt0.x;
        float c = pt0.x * pt1.y - pt1.x * pt0.y;
        if ((a * pStart.x + b * pStart.y + c) * (a * pEnd.x + b * pEnd.y + c) > 0)
            return true;

        return false;
    }

    /**
     * 返回一个三角形列表中不重复的点
     * 
     * @param triangles
     * @return
     */
    public static List<MeshPoint> getUniquePointsFromTriangles(List<MeshTriangle> triangles)
    {
        List<MeshPoint> points = new ArrayList<MeshPoint>();
        for (MeshTriangle triangle : triangles)
        {
            for (MeshPoint point : triangle.points)
                points.add(point);
        }
        return MeshPoint.getUniqueList(points);
    }

    public static void traceList(List<MeshTriangle> triangles)
    {
        List<MeshPoint> pointsList = MeshTriangle.getUniquePointsFromTriangles(triangles);
        Map<String, Integer> pointsMap = new HashMap<String, Integer>();

        int points_length = 0;
        for (MeshPoint point : pointsList)
            pointsMap.put(point.toString(), ++points_length);

        for (MeshPoint point : pointsList)
        {
            System.err.println("  " + "p" + pointsMap.get(point.toString()) + " = " + point);
        }

        for (MeshTriangle triangle : triangles)
        {
            System.err.println("  Triangle(" + "p" + pointsMap.get(triangle.points[0].toString()) + ", "
                    + pointsMap.get(triangle.points[1].toString()) + ", " + pointsMap.get(triangle.points[2].toString()) + ")");
        }
    }

    public String toString()
    {
        return "Triangle(" + this.points[0].toString() + ", " + this.points[1].toString() + ", "
                + this.points[2].toString() + ")";
    }

    public int getAreaID()
    {
        return areaID;
    }

    public void setAreaID(int areaID)
    {
        this.areaID = areaID;
    }

    public void reset()
    {
        this.sessionId = 0;
        this.h = 0;
        this.f = 0;
        this.isOpen = false;
        this.arrival = 0;
        this.parent = null;
    }
}
