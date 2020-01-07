package com.base.mesh;

/**
 * 多点之间的排序关系
 * @author dream
 *
 */
public class PointOrderType
{
    /**
     * cw为顺时针旋转clockwise (C.W.) 
     */
    public static final int CW = 1;
    
    /**
     * ccw为逆时针旋转 counterclockwise
     */
    public static final int CCW = -1;
    
    /**
     * 共线
     */
    public static final int COLLINEAR = 0;
}
