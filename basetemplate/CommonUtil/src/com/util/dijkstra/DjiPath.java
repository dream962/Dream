package com.util.dijkstra;

import java.util.LinkedList;

public class DjiPath
{
    /** 是否可达 */
    public boolean reachable;
    /** 最短距离 */
    public int minStep;
    /** 路径 */
    public LinkedList<Integer> step;
    
    public DjiPath(boolean reachable, int minStep)
    {
        this.reachable = reachable;
        this.minStep = minStep;
    }
}
