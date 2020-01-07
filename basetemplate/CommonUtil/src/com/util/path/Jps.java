package com.util.path;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.util.structure.BinaryHeap;

/**
 * @author dream
 *
 */
public class Jps
{
    private BinaryHeap<ANode> open;
    private List<ANode> closed;
    private MapGrid nodeGrid;
    private ANode endNode;
    private ANode startNode;
    private List<ANode> path;
    private float straightCost = 1.0f;
    // 45度根2值Math.sqrt(2)，这里约等1.414
    private float diagCost = 1.414f;

    private List<ANode> floydPath;

    public Jps()
    {
    }

    public Jps(MapGrid grid)
    {
        nodeGrid = grid;
    }

    public void setGrid(MapGrid grid)
    {
        nodeGrid = grid;
    }

    public boolean findPath(ANode beginNode, ANode endNode)
    {
        open = new BinaryHeap<ANode>(new Comparator<ANode>()
        {
            @Override
            public int compare(ANode o1, ANode o2)
            {
                return (int) (o1.f - o2.f);
            }
        });
        closed = new ArrayList<ANode>();

        this.startNode = beginNode;
        this.endNode = endNode;

        startNode.g = 0;
        startNode.h = diagonal(startNode);
        startNode.f = startNode.g + startNode.h;

        return search();
    }

    public boolean search()
    {
        // 异步运算。当上一次遍历超出最大允许值后停止遍历，下一次从上次暂停处开始继续遍历
        ANode node = startNode;

        while (node != endNode)
        {
            int startX = 0 > node.x - 1 ? 0 : node.x - 1;
            int endX = nodeGrid.getNumCols() - 1 < node.x + 1 ? nodeGrid.getNumCols() - 1 : node.x + 1;
            int startY = 0 > node.y - 1 ? 0 : node.y - 1;
            int endY = nodeGrid.getNumRows() - 1 < node.y + 1 ? nodeGrid.getNumRows() - 1 : node.y + 1;

            // 寻找当前节点周围8个点
            for (int i = startX; i <= endX; i++)
            {
                for (int j = startY; j <= endY; j++)
                {
                    ANode test = nodeGrid.getNode(i, j);
                    if (test == node || !test.walkable || !isDiagonalWalkable(node, test))
                    {
                        continue;
                    }

                    // 花费，直线为1，折线为1.414
                    float cost = straightCost;
                    if (!((node.x == test.x) || (node.y == test.y)))
                        cost = diagCost;

                    float g = node.g + cost * test.costMultiplier;
                    float h = diagonal(test);
                    float f = g + h;
                    // 判断是否已经在openList里面
                    boolean isInOpen = open.indexOf(test) != -1;
                    if (isInOpen || closed.indexOf(test) != -1)
                    {
                        if (test.f > f)
                        {
                            test.f = f;
                            test.g = g;
                            test.h = h;
                            test.parent = node;
                            if (isInOpen)
                                open.updateNode(test);
                        }
                    }
                    else
                    {
                        // 加入到openList里面
                        test.f = f;
                        test.g = g;
                        test.h = h;
                        test.parent = node;
                        open.push(test);
                    }

                }
            }
            closed.add(node);
            if (open.getSize() == 0)
            {
                return false;
            }

            node = open.pop();
        }
        buildPath();

        return true;
    }

    /**
     * 弗洛伊德路径平滑处理
     * form http://wonderfl.net/c/aWCe
     */
    public void floyd()
    {
        if (path == null)
            return;

        floydPath = new ArrayList<ANode>();
        floydPath.addAll(path);
        int len = floydPath.size();
        if (len > 2)
        {
            ANode vector = new ANode(0, 0);
            ANode tempVector = new ANode(0, 0);
            // 遍历路径数组中全部路径节点，合并在同一直线上的路径节点
            // 假设有1,2,3,三点，若2与1的横、纵坐标差值分别与3与2的横、纵坐标差值相等则
            // 判断此三点共线，此时可以删除中间点2
            floydVector(vector, floydPath.get(len - 1), floydPath.get(len - 2));
            for (int i = floydPath.size() - 3; i >= 0; i--)
            {
                floydVector(tempVector, floydPath.get(i + 1), floydPath.get(i));
                if (vector.x == tempVector.x && vector.y == tempVector.y)
                {
                    floydPath.remove(i + 1);
                }
                else
                {
                    vector.x = tempVector.x;
                    vector.y = tempVector.y;
                }
            }
        }
        // 合并共线节点后进行第二步，消除拐点操作。算法流程如下：
        // 如果一个路径由1-10十个节点组成，那么由节点10从1开始检查
        // 节点间是否存在障碍物，若它们之间不存在障碍物，则直接合并
        // 此两路径节点间所有节点。
        len = floydPath.size();
        for (int i = len - 1; i >= 0; i--)
        {
            for (int j = 0; j <= i - 2; j++)
            {
                if (nodeGrid.hasBarrier(floydPath.get(i).x, floydPath.get(i).y, floydPath.get(j).x, floydPath.get(j).y) == false)
                {
                    for (int k = i - 1; k > j; k--)
                    {
                        floydPath.remove(k);
                    }
                    i = j;
                    len = floydPath.size();
                    break;
                }
            }
        }
    }

    private void buildPath()
    {
        path = new ArrayList<ANode>();
        ANode node = endNode;
        path.add(node);

        while (node != startNode)
        {
            node = node.parent;
            path.add(0, node);
        }
    }

    private void floydVector(ANode target, ANode n1, ANode n2)
    {
        target.x = n1.x - n2.x;
        target.y = n1.y - n2.y;
    }

    /** 判断两个节点的对角线路线是否可走 */
    private boolean isDiagonalWalkable(ANode node1, ANode node2)
    {
        ANode nearByNode1 = nodeGrid.getNode(node1.x, node2.y);
        ANode nearByNode2 = nodeGrid.getNode(node2.x, node1.y);

        if (nearByNode1.walkable && nearByNode2.walkable)
            return true;

        return false;
    }

    /**
     * 曼哈顿算法
     * 
     * @param node
     * @return
     */
    @SuppressWarnings("unused")
    private float manhattan(ANode node)
    {
        return Math.abs(node.x - endNode.x) * straightCost + Math.abs(node.y + endNode.y) * straightCost;
    }

    /**
     * 欧几里何算法
     * 
     * @param node
     * @return
     */
    @SuppressWarnings("unused")
    private float euclidian(ANode node)
    {
        int dx = node.x - endNode.x;
        int dy = node.y - endNode.y;
        return (float) (Math.sqrt(dx * dx + dy * dy) * straightCost);
    }

    /**
     * 对角线算法(对角线+直线)
     * 
     * @param node
     * @return
     */
    private float diagonal(ANode node)
    {
        int dx = node.x - endNode.x < 0 ? endNode.x - node.x : node.x - endNode.x;
        int dy = node.y - endNode.y < 0 ? endNode.y - node.y : node.y - endNode.y;
        int diag = dx < dy ? dx : dy;
        int straight = dx + dy;
        // 折线的距离(小的距离差)(45度角根号2)+直线的距离
        return diagCost * diag + straightCost * (straight - 2 * diag);
    }

    /**
     * 没有优化的路径
     * 
     * @return
     */
    public List<ANode> getPath()
    {
        return path;
    }

    /**
     * 平滑处理的路径
     * 
     * @return
     */
    public List<ANode> getFloydPath()
    {
        return floydPath;
    }
}
