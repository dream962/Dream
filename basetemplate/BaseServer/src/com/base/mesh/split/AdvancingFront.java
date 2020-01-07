package com.base.mesh.split;

import com.base.mesh.MeshPoint;

public class AdvancingFront
{
    public MeshNode head;
    public MeshNode tail;
    public MeshNode search_node;

    public AdvancingFront(MeshNode head, MeshNode tail)
    {
        this.search_node = this.head = head;
        this.tail = tail;
    }

    /*
     * function findSearchNode(x) {
     * return this.search_node;
     * }
     */

    public MeshNode locateNode(float x)
    {
        MeshNode node = this.search_node;

        if (x < node.value)
        {
            while ((node = node.prev) != null)
            {
                if (x >= node.value)
                {
                    this.search_node = node;
                    return node;
                }
            }
        }
        else
        {
            while ((node = node.next) != null)
            {
                if (x < node.value)
                {
                    this.search_node = node.prev;
                    return node.prev;
                }
            }
        }
        return null;
    }

    public MeshNode locatePoint(MeshPoint point)
    {
        float px = point.x;
        // var node:* = this.FindSearchNode(px);
        MeshNode node = this.search_node;
        float nx = node.point.x;

        if (px == nx)
        {
            if (!point.equals(node.point))
            {
                // We might have two nodes with same x value for a short time
                if (point.equals(node.prev.point))
                {
                    node = node.prev;
                }
                else if (point.equals(node.next.point))
                {
                    node = node.next;
                }
                else
                {
                    node = null;
                }
            }
        }
        else if (px < nx)
        {
            while ((node = node.prev) != null)
                if (point.equals(node.point))
                    break;
        }
        else
        {
            while ((node = node.next) != null)
                if (point.equals(node.point))
                    break;
        }

        if (node != null)
            this.search_node = node;

        return node;
    }
}
