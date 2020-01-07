package com.base.mesh.split;

import com.base.mesh.MeshPoint;
import com.base.mesh.MeshTriangle;

public class MeshNode
{
    public MeshPoint point;
    public MeshTriangle triangle;
    public MeshNode prev;
    public MeshNode next;
    public float value;

    public MeshNode(MeshPoint point, MeshTriangle triangle)
    {
        this.point = point;
        this.triangle = triangle;
        this.value = this.point.x;
    }

    /**
     *
     * @param node - middle node
     * @return the angle between 3 front nodes
     */
    public float getHoleAngle()
    {
        /* Complex plane
         * ab = cosA +i*sinA
         * ab = (ax + ay*i)(bx + by*i) = (ax*bx + ay*by) + i(ax*by-ay*bx)
         * atan2(y,x) computes the principal value of the argument function
         * applied to the complex number x+iy
         * Where x = ax*bx + ay*by
         *       y = ax*by - ay*bx
         */
            float ax = this.next.point.x - this.point.x;
            float ay = this.next.point.y - this.point.y;
            float bx = this.prev.point.x - this.point.x;
            float by = this.prev.point.y - this.point.y;
            return (float)Math.atan2(ax * by - ay * bx, ax * bx + ay * by);
    }

    public float getBasinAngle()
    {
        return (float)Math.atan2(
            this.point.y - this.next.next.point.y, // ay
            this.point.x - this.next.next.point.x);  // ax);
    }
}
