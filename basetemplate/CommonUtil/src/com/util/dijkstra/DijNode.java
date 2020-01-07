package com.util.dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图节点
 * 
 * @author dream
 *
 */
public class DijNode
{
    private int nodeID;

    private int x;

    private int y;

    private List<DijEdge> edgeList = new ArrayList<>();

    public DijNode(int n)
    {
        nodeID = n;
    }

    public List<DijEdge> getEdgeList()
    {
        return edgeList;
    }
    
    public int getEdgeCount()
    {
        return edgeList.size();
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getNodeID()
    {
        return nodeID;
    }

    public DijEdge getEdge(int nodeID)
    {
        for (DijEdge edge : edgeList)
        {
            if (edge.getNodeID1() == nodeID || edge.getNodeID2() == nodeID)
                return edge;
        }

        return null;
    }

    /**
     * 添加边的信息
     * 
     * @param edge
     */
    public boolean addEdge(DijEdge mapEdge)
    {
        if (mapEdge.getNodeID1() != nodeID && mapEdge.getNodeID2() != nodeID)
            return false;

        for (DijEdge edge : edgeList)
        {
            if(edge.equals(mapEdge))
                return false;
        }
        
        edgeList.add(mapEdge);
        return true;
    }
}
