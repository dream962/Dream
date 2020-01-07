package com.util.path;

import java.awt.Point;
import java.util.List;

import com.util.ThreadSafeRandom;

/**
 * @author dream
 *
 */
public class TestPath
{
    private int _numCols = 10;
    private int _numRow = 10;
    private int _numBlock = 30;

    private MapGrid _grid;

    public static void main(String[] args)
    {
        TestPath testPath = new TestPath();
        testPath.makeGrid();

        Point point = testPath.makePlayer();

        testPath.makePath(point);
    }

    public void makePath(Point beginPoint)
    {
        ANode startNode = _grid.getNode(beginPoint.x, beginPoint.y);

        int randomCol = ThreadSafeRandom.next(0, _numCols);
        int randomRow = ThreadSafeRandom.next(0, _numRow);
        ANode endNode = _grid.getNode(randomCol, randomRow);
        // 保证终点可行
        while (_grid.getNode(randomCol, randomRow).walkable == false)
        {
            randomCol = ThreadSafeRandom.next(0, _numCols);
            randomRow = ThreadSafeRandom.next(0, _numRow);
        }
        endNode = _grid.getNode(randomCol, randomRow);

        StringBuilder builder = new StringBuilder();
        builder.append("begin(").append(beginPoint.x).append(",").append(beginPoint.y).append(") --> end(").append(randomCol).append(",").append(randomRow).append(
                "," + endNode.walkable).append(")").append("\n");

//        if (endNode.walkable == false)
//        {
//            ANode replacer = _grid.findReplacer(startNode, endNode);
//            if (replacer != null)
//            {
//                randomCol = replacer.x;
//                randomRow = replacer.y;
//            }
//        }

        // 开始寻路
        AStar astar = new AStar(_grid);
        long time = System.currentTimeMillis();
        boolean isPath = astar.findPath(_grid.getNode(beginPoint.x, beginPoint.y), _grid.getNode(randomCol, randomRow));
        long interval = System.currentTimeMillis() - time;
        if (isPath)
        {
            // 得到平滑路径
            astar.floyd();
            List<ANode> path = astar.getFloydPath();
            if(!path.isEmpty())
            {
                builder.append("优化路径:");
            }
            for (ANode node : path)
            {
                builder.append("(" + node.x + "," + node.y + ")").append("->");
            }
            if(!path.isEmpty())
            {
                builder.replace(builder.length()-2, builder.length(),"");
            }

            builder.append("\n原始路径:");
            for (ANode node : astar.getPath())
            {
                builder.append("(" + node.x + "," + node.y + ")").append("->");
            }
            
            if(!astar.getPath().isEmpty())
            {
                builder.replace(builder.length()-2, builder.length(),"");
            }
        }
        builder.append("\n").append(" time:" + interval);
        System.out.println(builder.toString());
    }

    public Point makePlayer()
    {
        int randomCol = ThreadSafeRandom.next(0, _numCols);
        int randomRow = ThreadSafeRandom.next(0, _numRow);
        while (_grid.getNode(randomCol, randomRow).walkable == false)
        {
            randomCol = ThreadSafeRandom.next(0, _numCols);
            randomRow = ThreadSafeRandom.next(0, _numRow);
        }

        return new Point(randomCol, randomRow);
    }

    public void makeGrid()
    {
        _grid = new MapGrid(_numCols, _numRow);
        for (int i = 0; i < _numBlock; i++)
        {
            _grid.setWalkable(ThreadSafeRandom.next(0, _numCols), ThreadSafeRandom.next(0, _numRow), false);
        }

        int randomCenterX = ThreadSafeRandom.next(0, _numCols);
        int randomCenterY = ThreadSafeRandom.next(0, _numRow);
        int radius = 3;
        int startX = Math.max(randomCenterX - radius, 0);
        int endX = Math.min(randomCenterX + radius, _grid.getNumCols());
        int startY = Math.max(randomCenterY - radius, 0);
        int endY = Math.min(randomCenterY + radius, _grid.getNumRows());
        for (int i = startX; i < endX; i++)
        {
            for (int j = startY; j < endY; j++)
            {
                _grid.setWalkable(i, j, false);
            }
        }

        StringBuilder builder = new StringBuilder();
        // 列头
        builder.append("   ");
        for (int j = 0; j < _grid.getNumCols(); j++)
        {
            builder.append(j).append("   ");
        }
        builder.append("\n");
        for (int i = 0; i < _grid.getNumRows(); i++)
        {
            builder.append(i).append("  ");
            for (int j = 0; j < _grid.getNumCols(); j++)
            {
                ANode node = _grid.getNode(j, i);
                if (j >= 10)
                    builder.append(" ");
                if (node.walkable)
                    builder.append("O").append("   ");
                else
                    builder.append("X").append("   ");
            }
            builder.append("\n");
        }

        System.err.println(builder.toString());
    }
}
