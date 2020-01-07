package com.util.dijkstra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class DijkstraFind
{
    public static class PreNode
    {
        /** 最优前一个节点 */
        public int preNodeNum;
        /** 最小步长 */
        public int nodeStep;

        public PreNode(int node, int step)
        {
            preNodeNum = node;
            nodeStep = step;
        }
    }

    /** 地图信息 */
    private DijGraph graph;
    /** 移除节点 */
    private HashSet<Integer> outNode;
    /** 起点到各点的步长，《目的节点，到目的节点的步长》 */
    private HashMap<Integer, PreNode> nodeStep;
    /** 下一次计算的节点 */
    private LinkedList<Integer> nextNode;
    /** 起点 */
    private int startNode;
    /** 终点 */
    private int endNode;

    /**
     * 查找最短距离
     * 
     * @param start
     * @param end
     * @param map
     * @return
     */
    public DjiPath findPath(int start, int end, final DijGraph map)
    {
        DjiPath path = new DjiPath(false, -1);

        this.graph = map;

        // 起点、终点不在目标节点内，返回不可达
        if (this.graph.getNode(start) == null || this.graph.getNode(end) == null)
            return path;

        outNode = new HashSet<Integer>();
        nodeStep = new HashMap<Integer, PreNode>();
        nextNode = new LinkedList<Integer>();
        nextNode.add(start);
        startNode = start;
        endNode = end;

        // 优化处理，只有一条连接线的点排除
        List<DijNode> list = graph.getNodeList();
        for (DijNode node : list)
        {
            if (node != null && start != node.getNodeID() && end != node.getNodeID() && node.getEdgeCount() <= 1)
            {
                outNode.add(node.getNodeID());
            }
        }

        step();

        // 返回最短路径
        if (nodeStep.containsKey(end))
        {
            path.reachable = true;
            path.step = new LinkedList<Integer>();
            path.minStep = nodeStep.get(endNode).nodeStep;

            int nodeNum = endNode;
            path.step.addFirst(nodeNum);
            while (nodeStep.containsKey(nodeNum))
            {
                int node = nodeStep.get(nodeNum).preNodeNum;
                path.step.addFirst(node);
                nodeNum = node;
            }

            return path;
        }

        return path;
    }

    /**
     * 查找计算
     */
    private void step()
    {
        if (nextNode.size() < 1 || outNode.size() == graph.getNodeCount())
            return;

        // 获取下一个计算节点
        int start = nextNode.removeFirst();
        // 到达该节点的最小距离
        int step = 0;
        if (nodeStep.containsKey(start))
        {
            step = nodeStep.get(start).nodeStep;
        }

        DijNode nextStep = graph.getNode(start);
        // 获取该节点可达节点的信息
        List<DijEdge> list = nextStep.getEdgeList();
        for (DijEdge entry : list)
        {
            int key = entry.getEndNode(start);
            // 如果是起点到起点，不计算之间的步长
            if (key == startNode)
                continue;

            // 起点到可达节点的距离
            int value = entry.getWeight() + step;
            if (!nextNode.contains(key) && !outNode.contains(key))
            {
                nextNode.add(key);
            }

            if (nodeStep.containsKey(key))
            {
                if (value < nodeStep.get(key).nodeStep)
                {
                    nodeStep.put(key, new PreNode(start, value));
                }
            }
            else
            {
                nodeStep.put(key, new PreNode(start, value));
            }
        }

        // 将该节点移除
        outNode.add(start);

        // 计算下一个节点
        step();
    }

}
