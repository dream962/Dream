package com.util.dijkstra;

/**
 * 地图节点邻接边
 * 
 * @author dream
 *
 */
public class DijEdge
{
    /** 节点ID */
    private int vexNodeID1;

    /** 节点ID */
    private int vexNodeID2;

    /** 权重 */
    private int weight;

    public DijEdge(int n1,int n2,int w)
    {
        weight = w;
        vexNodeID1 = n1;
        vexNodeID2 = n2;
    }

    public int getNodeID1()
    {
        return vexNodeID1;
    }

    public int getNodeID2()
    {
        return vexNodeID2;
    }

    public int getWeight()
    {
        return weight;
    }

    /**
     * 取得连接的另一个点
     * 
     * @param start
     * @return
     */
    public int getEndNode(int start)
    {
        if (vexNodeID1 == start)
            return vexNodeID2;

        if (vexNodeID2 == start)
            return vexNodeID1;

        return -1;
    }

    public boolean equals(DijEdge obj)
    {
        if (obj.getNodeID1() == vexNodeID1)
        {
            if (obj.getNodeID2() == vexNodeID2)
                return true;
        }

        if (obj.getNodeID2() == vexNodeID1)
        {
            if (obj.getNodeID1() == vexNodeID2)
                return true;
        }

        return false;
    }
}
