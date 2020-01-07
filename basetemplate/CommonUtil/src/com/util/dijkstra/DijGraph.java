package com.util.dijkstra;

import java.util.ArrayList;
import java.util.List;

import com.util.JsonUtil;

public class DijGraph
{
    /** 节点列表从1开始，多一个0的空节点 */
    private List<DijNode> vexList;

    public int getNodeCount()
    {
        // 少一个0位置的
        return vexList.size() - 1;
    }

    public List<DijNode> getNodeList()
    {
        return vexList;
    }

    public DijNode getNode(int nodeID)
    {
        if (nodeID < 1 || nodeID > vexList.size())
            return null;

        return vexList.get(nodeID);
    }

    public boolean init(ConfigTest config)
    {
        List<Integer> nodeList = config.nodeList;
        vexList = new ArrayList<>(nodeList.size() + 1);
        for (int i = 0; i < nodeList.size() + 1; i++)
        {
            vexList.add(null);
        }

        // 节点初始化
        for (int node : nodeList)
        {
            DijNode mapNode = new DijNode(node);
            vexList.set(node, mapNode);
        }

        // 边的初始化
        for (ConfigEdgeTest edge : config.edgeList)
        {
            DijNode node1 = vexList.get(edge.n1);
            DijNode node2 = vexList.get(edge.n2);

            DijEdge mapEdge = new DijEdge(edge.n1, edge.n2, edge.weight);
            node1.addEdge(mapEdge);
            node2.addEdge(mapEdge);
        }

        return true;
    }

    /**
     * 查找路径
     * 
     * @param startNode
     * @param endNode
     * @return
     */
    public DjiPath find(int startNode, int endNode)
    {
        DijkstraFind find = new DijkstraFind();
        DjiPath path = find.findPath(startNode, endNode, this);
        return path;
    }

    private static class ConfigTest
    {
        public List<Integer> nodeList = new ArrayList<>();
        public List<ConfigEdgeTest> edgeList = new ArrayList<>();

        public void addEdges(int i, int j, int k)
        {
            ConfigEdgeTest test = new ConfigEdgeTest();
            test.n1 = i;
            test.n2 = j;
            test.weight = k;

            edgeList.add(test);
        }
    }

    private static class ConfigEdgeTest
    {
        public int n1;
        public int n2;
        public int weight;
    }

    public static void main(String[] args)
    {
        ConfigTest builder = new ConfigTest();

        for (int i = 1; i <= 16; i++)
        {
            builder.nodeList.add(i);
        }

        builder.addEdges(1, 4, 1);
        builder.addEdges(1, 10, 3);
        builder.addEdges(1, 3, 3);
        builder.addEdges(1, 5, 2);
        builder.addEdges(2, 3, 7);
        builder.addEdges(2, 6, 6);
        builder.addEdges(2, 7, 4);
        builder.addEdges(2, 9, 7);
        builder.addEdges(3, 10, 4);
        builder.addEdges(3, 5, 2);
        builder.addEdges(3, 7, 5);
        builder.addEdges(3, 6, 3);
        builder.addEdges(4, 6, 5);
        builder.addEdges(5, 7, 7);
        builder.addEdges(5, 8, 3);
        builder.addEdges(7, 8, 8);
        builder.addEdges(7, 9, 1);
        builder.addEdges(8, 10, 6);
        builder.addEdges(11, 2, 3);
        builder.addEdges(12, 9, 4);
        builder.addEdges(13, 7, 5);
        builder.addEdges(14, 8, 6);
        builder.addEdges(15, 6, 7);
        builder.addEdges(16, 1, 3);

        DijGraph graph = new DijGraph();
        graph.init(builder);

        DjiPath path = graph.find(1, 7);
        if (path != null)
            System.err.println(JsonUtil.parseObjectToString(path));
    }
}
