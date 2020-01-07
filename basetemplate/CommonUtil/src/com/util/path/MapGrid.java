package com.util.path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图网格
 * 
 * @author dream
 * 
 */
public class MapGrid
{
    private ANode[][] nodes;
    private int numNodeCols;
    private int numNodeRows;

    public MapGrid(int numCols, int numRows)
    {
        numNodeCols = numCols;
        numNodeRows = numRows;
        nodes = new ANode[numNodeRows][numNodeCols];

        for (int i = 0; i < numNodeRows; i++)
        {
            for (int j = 0; j < numNodeCols; j++)
            {
                nodes[i][j] = new ANode(j, i);
            }
        }
    }

    /**
     * 取得节点
     * 
     * @param x
     *            The x coord.
     * @param y
     *            The y coord.
     */
    public ANode getNode(int x, int y)
    {
        if(x >= numNodeCols || y >= numNodeRows){
            return null;
        }
        return nodes[y][x];
    }

    /**
     * 设置是否可行
     * 
     * @param x
     *            The x coord.-col
     * @param y
     *            The y coord.-row
     */
    public void setWalkable(int x, int y, boolean value)
    {
        nodes[y][x].walkable = value;
    }

    /**
     * 判断两节点之间是否存在障碍物
     * 
     * @param point1
     * @param point2
     * @return
     * 
     */
    public boolean hasBarrier(int startX, int startY, int endX, int endY)
    {
        // 如果起点终点是同一个点那傻子都知道它们间是没有障碍物的
        if (startX == endX && startY == endY)
            return false;
        if (getNode(endX, endY).walkable == false)
            return true;

        // 两节点中心位置
        Point point1 = new Point();
        point1.setLocation(startX + 0.5f, startY + 0.5f);
        Point point2 = new Point();
        point2.setLocation(endX + 0.5f, endY + 0.5f);

        int distX = Math.abs(endX - startX);
        int distY = Math.abs(endY - startY);

        /** 遍历方向，为true则为横向遍历，否则为纵向遍历 */
        boolean loopDirection = distX > distY ? true : false;

        /** 循环递增量 */
        float i;

        /** 循环起始值 */
        int loopStart;

        /** 循环终结值 */
        int loopEnd;

        /** 起终点连线所经过的节点 */
        List<ANode> nodesPassed = new ArrayList<>();

        // 为了运算方便，以下运算全部假设格子尺寸为1，格子坐标就等于它们的行、列号
        if (loopDirection)
        {
            loopStart = Math.min(startX, endX);
            loopEnd = Math.max(startX, endX);

            // 开始横向遍历起点与终点间的节点看是否存在障碍(不可移动点)
            for (i = loopStart; i <= loopEnd; i++)
            {
                // 由于线段方程是根据终起点中心点连线算出的，所以对于起始点来说需要根据其中心点
                // 位置来算，而对于其他点则根据左上角来算
                if (i == loopStart)
                    i += .5;
                // 根据x得到直线上的y值
                float yPos = PathMathUtil.getLineFunc(point1, point2, 0, i);

                nodesPassed = getNodesUnderPoint((int) i, (int) yPos, null);

                for (ANode elem : nodesPassed)
                {
                    if (elem.walkable == false)
                        return true;
                }

                if (i == loopStart + .5)
                    i -= .5;
            }
        }
        else
        {
            loopStart = Math.min(startY, endY);
            loopEnd = Math.max(startY, endY);

            // 开始纵向遍历起点与终点间的节点看是否存在障碍(不可移动点)
            for (i = loopStart; i <= loopEnd; i++)
            {
                if (i == loopStart)
                    i += .5;
                // 根据y得到直线上的x值
                float xPos = PathMathUtil.getLineFunc(point1, point2, 1, i);

                nodesPassed = getNodesUnderPoint((int) xPos, (int) i, null);
                for (ANode elem : nodesPassed)
                {
                    if (elem!=null && elem.walkable == false)
                        return true;
                }

                if (i == loopStart + .5)
                    i -= .5;
            }
        }

        return false;
    }

    /**
     * 得到一个点下的所有节点
     * 
     * @param xPos
     *            点的横向位置
     * @param yPos
     *            点的纵向位置
     * @param grid
     *            所在网格
     * @param exception
     *            例外格，若其值不为空，则在得到一个点下的所有节点后会排除这些例外格
     * @return 共享此点的所有节点
     * 
     */
    public List<ANode> getNodesUnderPoint(int xPos, int yPos, List<ANode> exception)
    {
        List<ANode> result = new ArrayList<>();
        boolean xIsInt = xPos % 1 == 0;
        boolean yIsInt = yPos % 1 == 0;

        // 点由四节点共享情况
        if (xIsInt && yIsInt)
        {
            if(xPos-1>=0 && yPos-1>=0)
                result.add(getNode(xPos - 1, yPos - 1));
            
            if(yPos-1>=0)
                result.add(getNode(xPos, yPos - 1));
            
            if(xPos-1>=0)
                result.add(getNode(xPos - 1, yPos));
            
            result.add(getNode(xPos, yPos));
        }
        // 点由2节点共享情况 点落在两节点左右临边上
        else if (xIsInt && !yIsInt)
        {
            if(xPos-1>=0)
                result.add(getNode(xPos - 1, yPos));
            
            result.add(getNode(xPos, yPos));
        }
        // 点落在两节点上下临边上
        else if (!xIsInt && yIsInt)
        {
            if(yPos-1>=0)
                result.add(getNode(xPos, yPos - 1));
            
            result.add(getNode(xPos, yPos));
        }
        // 点由一节点独享情况
        else
        {
            result.add(getNode(xPos, yPos));
        }

        // 在返回结果前检查结果中是否包含例外点，若包含则排除掉
        if (exception != null && exception.size() > 0)
        {
            for (int i = result.size() - 1; i >= 0; i--)
            {
                if (exception.indexOf(result.get(i)) != -1)
                {
                    result.remove(i);
                }
            }
        }

        return result;
    }

    /** 当终点不可移动时寻找一个离原终点最近的可移动点来替代之 */
    public ANode findReplacer(ANode fromNode, ANode toNode)
    {
        // 若终点可移动则根本无需寻找替代点
        if (toNode.walkable)
            return toNode;

        // 遍历终点周围节点以寻找离起始点最近一个可移动点作为替代点
        // 根据节点的埋葬深度选择遍历的圈
        // 若该节点是第一次遍历，则计算其埋葬深度
        ANode result = null;
        if (toNode.buriedDepth == -1)
        {
            toNode.buriedDepth = getNodeBuriedDepth(toNode, Math.max(numNodeCols, numNodeRows));
        }
        int xFrom = toNode.x - toNode.buriedDepth < 0 ? 0 : toNode.x - toNode.buriedDepth;
        int xTo = toNode.x + toNode.buriedDepth > numNodeCols - 1 ? numNodeCols - 1 : toNode.x + toNode.buriedDepth;
        int yFrom = toNode.y - toNode.buriedDepth < 0 ? 0 : toNode.y - toNode.buriedDepth;
        int yTo = toNode.y + toNode.buriedDepth > numNodeRows - 1 ? numNodeRows - 1 : toNode.y + toNode.buriedDepth;

        ANode n;// 当前遍历节点
        for (int i = xFrom; i <= xTo; i++)
        {
            for (int j = yFrom; j <= yTo; j++)
            {
                if ((i > xFrom && i < xTo) && (j > yFrom && j < yTo))
                {
                    continue;
                }
                n = getNode(i, j);
                if (n.walkable)
                {
                    // 计算此候选节点到起点的距离，记录离起点最近的候选点为替代点
                    n.getDistanceTo(fromNode);

                    if (result == null)
                    {
                        result = n;
                    }
                    else if (n.distance < result.distance)
                    {
                        result = n;
                    }
                }
            }
        }

        return result;
    }

    /** 计算全部路径点的埋葬深度 */
    public void calculateBuriedDepth()
    {
        ANode node;
        for (int i = 0; i < numNodeCols; i++)
        {
            for (int j = 0; j < numNodeRows; j++)
            {
                node = nodes[i][j];
                if (node.walkable)
                {
                    node.buriedDepth = 0;
                }
                else
                {
                    node.buriedDepth = getNodeBuriedDepth(node, Math.max(numNodeCols, numNodeRows));
                }
            }
        }
    }

    /**
     * 计算一个节点的埋葬深度
     * 
     * @param node
     *            欲计算深度的节点
     * @param loopCount
     *            计算深度时遍历此节点外围圈数。默认值为10
     */
    private int getNodeBuriedDepth(ANode node, int loopCount)
    {
        // 如果检测节点本身是不可移动的则默认它的深度为1
        int result = node.walkable ? 0 : 1;
        int l = 1;

        while (l <= loopCount)
        {
            int startX = node.x - l < 0 ? 0 : node.x - l;
            int endX = node.x + l > numNodeCols - 1 ? numNodeCols - 1 : node.x + l;
            int startY = node.y - l < 0 ? 0 : node.y - l;
            int endY = node.y + l > numNodeRows - 1 ? numNodeRows - 1 : node.y + l;

            ANode n;
            // 遍历一个节点周围一圈看是否周围一圈全部是不可移动点，若是，则深度加一，
            // 否则返回当前累积的深度值
            for (int i = startX; i <= endX; i++)
            {
                for (int j = startY; j <= endY; j++)
                {
                    n = getNode(i, j);
                    if (n != node && n.walkable)
                    {
                        return result;
                    }
                }
            }

            // 遍历完一圈，没发现一个可移动点，则埋葬深度加一。接着遍历下一圈
            result++;
            l++;
        }
        return result;
    }

    public int getNumCols()
    {
        return numNodeCols;
    }

    public int getNumRows()
    {
        return numNodeRows;
    }

}
