package com.base.mesh.split;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.mesh.MeshPoint;
import com.base.mesh.MeshTriangle;

public class SweepContext
{
    /**
     * Inital triangle factor, seed triangle will extend 30% of
     * PointSet width to both left and right.
     */
    public static final float kAlpha = 0.3f;
    
    public List<MeshTriangle> triangles;
    public List<MeshPoint> points;
    
    public Map<String,MeshTriangle> map;
    
    public AdvancingFront front;
    public MeshPoint head;
    public MeshPoint tail;
    
    public MeshNode af_head;
    public MeshNode af_middle;
    public MeshNode af_tail;
    
    public SweepContext(List<MeshPoint> polyline) 
    {
        this.triangles = new ArrayList<MeshTriangle>();
        this.points = new ArrayList<MeshPoint>();
        this.map = new HashMap<String,MeshTriangle>();
        
        if(polyline!=null)
            this.addPolyline(polyline);
    }
    
    protected void addPoints(List<MeshPoint> points)
    {
        for (MeshPoint point : points) 
            this.points.add(point);
    }
    
    public void addPolyline(List<MeshPoint> polyline)
    {
        if (polyline == null) 
            return;
        this.addPoints(polyline);
    }

    /**
     * An alias of addPolyline.
     *
     * @param   polyline
     */
    public void addHole(List<MeshPoint> polyline)
    {
        addPolyline(polyline);
    }

//    protected void initEdges(List<Point> polyline)
//    {
//        for (int n = 0; n < polyline.size(); n++) 
//        {
//            Point node=polyline.get(n);
//            Point node1=polyline.get((n + 1) % polyline.size());
//            this.edge_list.add(new Edge(node, node1));
//        }
//    }

    public void addToMap(MeshTriangle triangle)
    {
        this.map.put(triangle.toString(),triangle);
    }

    public void initTriangulation() 
    {
        float xmin = this.points.get(0).x, xmax = this.points.get(0).x;
        float ymin = this.points.get(0).y, ymax = this.points.get(0).y;
        
        // Calculate bounds
        for (MeshPoint p : this.points) 
        {
            if (p.x > xmax) xmax = p.x;
            if (p.x < xmin) xmin = p.x;
            if (p.y > ymax) ymax = p.y;
            if (p.y < ymin) ymin = p.y;
        }

        float dx = kAlpha * (xmax - xmin);
        float dy = kAlpha * (ymax - ymin);
        this.head = new MeshPoint(xmax + dx, ymin - dy);
        this.tail = new MeshPoint(xmin - dy, ymin - dy);

        // Sort points along y-axis
        MeshPoint.sortPoints(this.points);
    }
    
    public MeshNode locateNode(MeshPoint point)
    {
        return this.front.locateNode(point.x);
    }

    public void createAdvancingFront()
    {
        // Initial triangle
        MeshTriangle triangle = new MeshTriangle(this.points.get(0), this.tail, this.head);

        addToMap(triangle);

        MeshNode head    = new MeshNode(triangle.points[1], triangle);
        MeshNode middle  = new MeshNode(triangle.points[0], triangle);
        MeshNode tail    = new MeshNode(triangle.points[2],null);

        this.front  = new AdvancingFront(head, tail);

        head.next   = middle;
        middle.next = tail;
        middle.prev = head;
        tail.prev   = middle;
    }

    public void removeNode(MeshNode node)
    {
        // do nothing
    }

    public void mapTriangleToNodes(MeshTriangle triangle)
    {
        for (int n = 0; n < 3; n++) 
        {
            if (triangle.neighbors[n] == null) {
                MeshNode neighbor = this.front.locatePoint(triangle.pointCW(triangle.points[n]));
                if (neighbor != null) neighbor.triangle = triangle;
            }
        }
    }
    
    public void removeFromMap(MeshTriangle triangle)
    {
        this.map.remove(triangle.toString());
    }

    public void meshClean(MeshTriangle triangle, int level)
    {
        if (triangle == null || triangle.interior) 
            return;

        triangle.interior = true;
        this.triangles.add(triangle);

        for (int n = 0; n < 3; n++) 
        {
            if (!triangle.constrainedEdge[n]) 
            {
                this.meshClean(triangle.neighbors[n], level + 1);
            }
        }
    }
}
