package com.base.mesh.split;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.base.mesh.MeshEdge;
import com.base.mesh.MeshLine;
import com.base.mesh.MeshPathPoint;
import com.base.mesh.MeshPoint;
import com.base.mesh.MeshTriangle;
import com.util.structure.BinaryHeap;

public class PathSeeker
{
    public SweepContext sweepContext;
    protected boolean triangulated;
    private BinaryHeap<MeshTriangle> openList;
    private List<MeshTriangle> closeList;
    /**
     * 
     */
    private int pathSessionId = 0;

    public PathSeeker(List<MeshPoint> list)
    {
        sweepContext = new SweepContext(list);
    }

    public List<MeshPoint> findPath(MeshPoint start, MeshPoint end)
    {
        MeshTriangle startTri = findTriangleContainer(start);
        MeshTriangle endTri = findTriangleContainer(end);

        if (startTri == null || endTri == null)
        {
            return null;
        }

        List<MeshPoint> res = new ArrayList<MeshPoint>();
        if (startTri == endTri)
        {
            res.add(start);
            res.add(end);

            return res;
        }
        else
        {
            openList = new BinaryHeap<MeshTriangle>(new Comparator<MeshTriangle>()
            {
                @Override
                public int compare(MeshTriangle o1, MeshTriangle o2)
                {
                    return (int) (o1.f - o2.f);
                }
            });
            closeList = new ArrayList<MeshTriangle>();

            pathSessionId++;

            res = aStar(startTri, start, endTri, end);
        }

        return null;
    }

    public MeshTriangle findTriangleContainer(MeshPoint pt)
    {
        int l = sweepContext.triangles.size();
        for (int i = 0; i < l; i++)
        {
            MeshTriangle tri = sweepContext.triangles.get(i);
            if (tri.isPointInside(pt))
            {
                return tri;
            }
        }

        return null;
    }

    public List<MeshPoint> aStar(MeshTriangle startTri, MeshPoint startPt, MeshTriangle endTri, MeshPoint endPt)
    {
        openList.clear();
        closeList.clear();

        openList.push(endTri);
        endTri.f = 0;
        endTri.h = 0;
        endTri.isOpen = false;
        endTri.parent = null;
        endTri.sessionId = pathSessionId;

        boolean foundPath = false;
        MeshTriangle currNode;
        MeshTriangle adjacentTmp;

        while (openList.getSize() > 0)
        {
            currNode = openList.pop();
            closeList.add(currNode);

            if (currNode == startTri)
            {
                foundPath = true;
                break;
            }

            for (int i = 0; i < 3; i++)
            {
                adjacentTmp = currNode.neighbors[i];
                if (adjacentTmp == null || adjacentTmp.interior == false)
                {
                    continue;
                }

                if (adjacentTmp != null)
                {
                    adjacentTmp.arrivalWall = null;

                    if (adjacentTmp.sessionId != pathSessionId)
                    {
                        adjacentTmp.sessionId = pathSessionId;
                        adjacentTmp.parent = currNode;
                        adjacentTmp.isOpen = true;

                        adjacentTmp.computeHeuristic(startPt);
                        adjacentTmp.f = currNode.f + adjacentTmp.wallDistance[Math.abs(i - currNode.arrival)];

                        openList.push(adjacentTmp);
                        adjacentTmp.setAndGetArrivalWall(currNode.index);
                    }
                    else
                    {
                        if (adjacentTmp.isOpen)
                        {
                            if (currNode.f + adjacentTmp.wallDistance[Math.abs(i - currNode.arrival)] < adjacentTmp.f)
                            {
                                adjacentTmp.f = currNode.f;
                                adjacentTmp.parent = currNode;

                                adjacentTmp.setAndGetArrivalWall(currNode.index);
                            }
                        }
                        else
                        {
                            adjacentTmp = null;
                            continue;
                        }
                    }
                }
            }
        }

        List<MeshTriangle> path = new ArrayList<MeshTriangle>();
        MeshTriangle s = closeList.get(closeList.size() - 1);

        while (s.parent != null)
        {
            path.add(s);
            s.arrivalWall = s.getNeighborEdge(s.parent);
            s = s.parent;
        }

        path.add(endTri);

        if (path == null || path.size() == 0) 
            return null;

        List<MeshPoint> result = new ArrayList<MeshPoint>();
        result.add(startPt);

        if (path.size() == 1)
        {
            result.add(endPt);
            return result;
        }

        MeshPathPoint wp = new MeshPathPoint(path.get(0), startPt);

        while (!wp.point.equals(endPt))
        {
            wp = getFurthestWayPoint(wp, path, endPt);
            result.add(wp.point);
        }

        return result;

    }

    private MeshPathPoint getFurthestWayPoint(MeshPathPoint wp, List<MeshTriangle> path, MeshPoint endPt)
    {
        MeshPoint startPt = wp.point;
        MeshTriangle tri = wp.triangle;

        int l = path.size();
        int startIndex = path.indexOf(tri);

        int lastCW = startIndex;
        int lastCCW = startIndex;

        System.err.println("new loop: " + tri.index);

        if (startIndex == l - 1)
        {
            return new MeshPathPoint(tri, endPt);
        }

        MeshPoint ptA = tri.arrivalWall.startPoint;
        MeshPoint ptB = tri.arrivalWall.endPoint;

        if (ptA.equals(startPt) || ptB.equals(startPt))
        {
            System.err.println("illegal start");
            return new MeshPathPoint(path.get(startIndex + 1), startPt);
        }

        int reverse = MeshEdge.classifyPoint(ptA, startPt, ptB);
        MeshLine edgeA;
        MeshLine edgeB;

        if (reverse < 0)
        {
            edgeA = new MeshLine(startPt, ptB);
            edgeB = new MeshLine(startPt, ptA);
        }
        else
        {
            edgeA = new MeshLine(startPt, ptA);
            edgeB = new MeshLine(startPt, ptB);
        }

        //trace("edgeA : " + edgeA + " | edgeB : " + edgeB);

        for (int i = startIndex + 1; i < path.size(); i++)
        {
            tri = path.get(i);

            System.err.println("test: " + tri.index);
            MeshPoint testPtA;
            MeshPoint testPtB;

            if (i < l - 1)
            {
                reverse = MeshEdge.classifyPoint(tri.arrivalWall.startPoint, startPt, tri.arrivalWall.endPoint);

                if (reverse < 0)
                {
                    testPtA = tri.arrivalWall.endPoint;
                    testPtB = tri.arrivalWall.startPoint;
                }
                else
                {
                    testPtA = tri.arrivalWall.startPoint;
                    testPtB = tri.arrivalWall.endPoint;
                }
            }
            else
            {
                testPtA = endPt;
                testPtB = endPt;
            }

            int r0 = 0;
            int r1 = 0;


            if (!edgeA.end.equals(testPtA))
            {
                if (MeshEdge.classifyPoint(testPtB, edgeA.start, edgeA.end) > 0)
                {
                    System.err.println("RETURN CW: " + lastCW);
                    return new MeshPathPoint(path.get(lastCW + 1), edgeA.end);
                }

                r0 = MeshEdge.classifyPoint(testPtA, edgeA.start, edgeA.end);

                if (r0 < 0)
                {
                    System.err.println("UPDATE CW");
                    edgeA.end = testPtA;
                    lastCW = i;
                }
            }

            if (!edgeB.end.equals(testPtB))
            {
                if (MeshEdge.classifyPoint(testPtA, edgeB.start, edgeB.end) < 0)
                {
                    System.err.println("RETURN CCW: " + lastCCW);
                    return new MeshPathPoint(path.get(lastCCW + 1), edgeB.end);
                }

                r1 = MeshEdge.classifyPoint(testPtB, edgeB.start, edgeB.end);
                System.err.println( tri.index + " | r1: " + r1 + " | " + testPtB + " | " + edgeB.end);

                if (r1 > 0)
                {
                    System.err.println("UPDATE CCW");
                    edgeB.end = testPtB;
                    lastCCW = i;
                }
            }

        }

        return null;
    }

}
