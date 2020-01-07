package com.base.mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.util.structure.BinaryHeap;

/**
 * 导航区域
 * @author dream
 *
 */
public class MeshArea
{
    private int areaID;
    
    private List<MeshTriangle> areaTriangleList;
   
    private BinaryHeap<MeshTriangle> openList;
    
    private List<MeshTriangle> closeList;
    
    /**
     * 标记路径是否走过
     */
    private int pathSessionId = 0;
    
    public MeshArea(int areaID)
    {
        this.areaID=areaID;
        areaTriangleList=new ArrayList<>();
        openList = new BinaryHeap<MeshTriangle>(new Comparator<MeshTriangle>()
        {
            @Override
            public int compare(MeshTriangle o1, MeshTriangle o2)
            {
                return (int) (o1.f - o2.f);
            }
        });
        closeList = new ArrayList<MeshTriangle>();
    }
    
    public void addTriangle(MeshTriangle triangle)
    {
        areaTriangleList.add(triangle);
    }
    
    public void calculate()
    {
        for (MeshTriangle tri : areaTriangleList)
        {
            tri.calculate();
            for (MeshTriangle that : areaTriangleList)
            {
                if (tri != that)
                    tri.markNeighborTriangle(that);
            }
        }
    }
    
    public List<MeshPoint> aStar(MeshTriangle startTri, MeshPoint startPt, MeshTriangle endTri, MeshPoint endPt)
    {
        pathSessionId++;

        openList.push(endTri);
        endTri.f = 0;
        endTri.h = 0;
        endTri.isOpen = false;
        endTri.parent = null;
        endTri.sessionId = pathSessionId;

        MeshTriangle currNode;
        MeshTriangle adjacentTmp;

        while (openList.getSize() > 0)
        {
            currNode = openList.pop();
            closeList.add(currNode);

            if (currNode == startTri)
            {
                break;
            }

            for (int i = 0; i < 3; i++)
            {
                adjacentTmp = currNode.neighbors[i];
                if (adjacentTmp == null || adjacentTmp.isCross==false)
                    continue;

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
            if(wp.isAdd==false)
                result.add(wp.point);
        }

        return result;
    }

    /**
     * 根据拐点计算法获得导航网格的下一个拐点
     * @param wp 当前所在的路点
     * @param path 路径
     * @param endPoint 终点
     * @return
     */
    private MeshPathPoint getFurthestWayPoint(MeshPathPoint wp, List<MeshTriangle> path, MeshPoint endPoint)
    {
        MeshPoint startPoint = wp.point;
        MeshTriangle triangle = wp.triangle;

        int startIndex = path.indexOf(triangle);

        int lastCW = startIndex;
        int lastCCW = startIndex;

        //如果是最后一个三角形
        if (startIndex == path.size() - 1)
        {
            return new MeshPathPoint(triangle, endPoint);
        }

        //穿出边的端点
        MeshPoint leftPoint = triangle.arrivalWall.startPoint;
        MeshPoint rightPoint = triangle.arrivalWall.endPoint;

        //如果穿出边的端点是路点
        if (leftPoint.equals(startPoint) || rightPoint.equals(startPoint))
        {
            wp =new MeshPathPoint(path.get(startIndex + 1), startPoint);
            wp.isAdd=true;
            return wp;
        }

        int reverse = MeshEdge.classifyPoint(leftPoint, startPoint, rightPoint);
        //点到三角形出边端点的连线
        MeshLine leftEdge;//左边
        MeshLine rightEdge;//右边

        //如果A点在PB边的右边
        if (reverse < 0)
        {
            leftEdge = new MeshLine(startPoint, rightPoint);
            rightEdge = new MeshLine(startPoint, leftPoint);
        }
        else
        {
            leftEdge = new MeshLine(startPoint, leftPoint);
            rightEdge = new MeshLine(startPoint, rightPoint);
        }

        //循环判断端点是否在边之间
        for (int i = startIndex + 1; i < path.size(); i++)
        {
            triangle = path.get(i);
            //下一个三角形的左点
            MeshPoint nextLeftPoint;
            //下一个三角形的有点
            MeshPoint nextRightPoint;

            if (i < path.size() - 1)
            {
                reverse = MeshEdge.classifyPoint(triangle.arrivalWall.startPoint, startPoint, triangle.arrivalWall.endPoint);

                if (reverse < 0)
                {
                    nextLeftPoint = triangle.arrivalWall.endPoint;
                    nextRightPoint = triangle.arrivalWall.startPoint;
                }
                else
                {
                    nextLeftPoint = triangle.arrivalWall.startPoint;
                    nextRightPoint = triangle.arrivalWall.endPoint;
                }
            }
            else
            {
                nextLeftPoint = endPoint;
                nextRightPoint = endPoint;
            }

            //左边的端点不等于临边的左点
            if (!leftEdge.end.equals(nextLeftPoint))
            {
                //如果新点testPtB已经在线段的左边了，那testPtA就更是了，所以edgeA.q点是拐点，返回
                if (MeshEdge.classifyPoint(nextRightPoint, leftEdge.start, leftEdge.end) > 0)
                {
                    return new MeshPathPoint(path.get(lastCW + 1), leftEdge.end);
                }

                //如果ptA点在A点的右边，更新左边的A点
                if (MeshEdge.classifyPoint(nextLeftPoint, leftEdge.start, leftEdge.end) < 0)
                {
                    leftEdge.end = nextLeftPoint;
                    lastCW = i;
                }
            }

            //右边的端点不等于临边的右点
            if (!rightEdge.end.equals(nextRightPoint))
            {
                //如果PtA还在右边之右，则edgeB.q是拐点
                if (MeshEdge.classifyPoint(nextLeftPoint, rightEdge.start, rightEdge.end) < 0)
                {
                    return new MeshPathPoint(path.get(lastCCW + 1), rightEdge.end);
                }

                //如果PtB在边之间，更新B点
                if (MeshEdge.classifyPoint(nextRightPoint, rightEdge.start, rightEdge.end) > 0)
                {
                    rightEdge.end = nextRightPoint;
                    lastCCW = i;
                }
            }
        }

        return new MeshPathPoint(path.get(path.size() - 1), endPoint);
    }
    
    public MeshTriangle findTriangle(MeshPoint point)
    {
        for(MeshTriangle tri:areaTriangleList)
        {
            if(tri.isPointInside(point))
                return tri;
        }
        
        return null;
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
        openList.clear();
        closeList.clear();
        for(MeshTriangle triangle:areaTriangleList)
        {
            triangle.reset();
        }
    }
}
