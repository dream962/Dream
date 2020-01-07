package com.base.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * 寻路结果
 * 
 * @author dream
 *
 */
public class MeshPathResult
{
    /**
     * 拐点信息：<AreaID,区域点集合>
     */
    private List<MeshPoint> path;

    /**
     * 寻路结果
     */
    private boolean result;

    public MeshPathResult()
    {
        path = new ArrayList<>();
        result = true;
    }

    public void setResult(boolean type)
    {
        this.result = type;
    }

    public boolean getResult()
    {
        return this.result;
    }

    public void addPoint(int area, List<MeshPoint> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list.get(i).areaID = area;
            path.add(list.get(i));
        }
    }

    public List<MeshPoint> getPath()
    {
        return path;
    }
}
