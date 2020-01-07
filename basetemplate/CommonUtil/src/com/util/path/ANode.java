package com.util.path;

/**
 * @author dream
 *
 */
public class ANode
{
    /** 节点列号 */
    public int x;
    
    /** 节点行号 */
    public int y;
    
    public float f;//总的移动量
    public float g;//从起始点到当前node的移动量
    public float h;//从当前node到终点的估算值
    public boolean walkable = true;//是否可行
    public ANode parent;
    public float costMultiplier = 1.0f;//当前节点的花费，默认都为1
    
    /** 屏幕坐标系中的x坐标 */
    public int posX;
    /** 屏幕坐标系中的y坐标 */
    public int posY;
    
    /** 埋葬深度 */
    public int buriedDepth = -1;
    
    /** 距离 */
    public int distance;
    
    public ANode(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /** 得到此节点到另一节点的网格距离 */
    public int getDistanceTo(ANode targetNode)
    {
        int disX = targetNode.x - x;
        int disY = targetNode.y - y;
        distance = (int) Math.sqrt( disX * disX + disY * disY );
        return distance;
    }
}
