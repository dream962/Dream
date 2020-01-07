package com.base.mesh;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.mesh.data.XmlOffMeshLink;
import com.base.mesh.data.XmlPoint;
import com.base.mesh.data.XmlTriangle;
import com.base.mesh.data.XmlTriangleList;
import com.util.FileUtil;
import com.util.XmlUtil;

/**
 * 导航地图
 * 
 * @author dream
 *
 */
public class MeshMap
{
    private Logger logger = LoggerFactory.getLogger(MeshMap.class);

    /**
     * 区域集合<区域ID,区域>
     */
    private Map<Integer, MeshArea> areaMap;

    /**
     * 区域链接集合<起始区域,链接列表>
     */
    private Map<Integer, List<MeshLink>> linkMap;

    public MeshMap()
    {
        areaMap = new HashMap<>();
        linkMap = new HashMap<>();
    }

    /**
     * 初始化地图
     * 
     * @param list
     * @return
     */
    public boolean init(XmlTriangleList list)
    {
        try
        {
            // 地形三角形
            for (int i = 0; i < list.getTriangleList().size(); i++)
            {
                XmlTriangle tri = list.getTriangleList().get(i);

                if (!areaMap.containsKey(tri.getAreaID()))
                    areaMap.put(tri.getAreaID(), new MeshArea(tri.getAreaID()));

                MeshArea area = areaMap.get(tri.getAreaID());

                MeshPoint p1 = new MeshPoint(tri.getVertices()[0].getX(), tri.getVertices()[0].getY());
                MeshPoint p2 = new MeshPoint(tri.getVertices()[1].getX(), tri.getVertices()[1].getY());
                MeshPoint p3 = new MeshPoint(tri.getVertices()[2].getX(), tri.getVertices()[2].getY());
                MeshTriangle triangle = new MeshTriangle(p1, p2, p3);
                triangle.setAreaID(tri.getAreaID());
                triangle.index = i;
                area.addTriangle(triangle);
            }

            // 三角形关系初始化
            for (MeshArea area : areaMap.values())
            {
                area.calculate();
            }

            // 地形连接
            int linkID = 0;
            for (XmlOffMeshLink link : list.getLinkList())
            {
                MeshPoint begin = new MeshPoint(link.getBegin().getX(), link.getBegin().getY());
                MeshPoint end = new MeshPoint(link.getEnd().getX(), link.getEnd().getY());
                MeshLink meshLink = new MeshLink(begin, end);
                meshLink.setLinkID(linkID++);

                MeshTriangle beginTriangle = findTriangleByPoint(begin);
                MeshTriangle endTriangle = findTriangleByPoint(end);

                if (beginTriangle == null || endTriangle == null)
                {
                    logger.error("LinkPoint Container is Null.");
                    return false;
                }

                meshLink.setBeginTriangle(beginTriangle);
                meshLink.setEndTriangle(endTriangle);

                if (!linkMap.containsKey(beginTriangle.getAreaID()))
                    linkMap.put(beginTriangle.getAreaID(), new ArrayList<MeshLink>());

                List<MeshLink> links = linkMap.get(beginTriangle.getAreaID());
                links.add(meshLink);

                if (link.isBiDirectional())
                {
                    MeshLink meshLink1 = new MeshLink(end.clone(), begin.clone());
                    meshLink1.setLinkID(linkID++);
                    meshLink1.setBeginTriangle(endTriangle);
                    meshLink1.setEndTriangle(beginTriangle);

                    if (!linkMap.containsKey(endTriangle.getAreaID()))
                        linkMap.put(endTriangle.getAreaID(), new ArrayList<MeshLink>());

                    List<MeshLink> links1 = linkMap.get(endTriangle.getAreaID());
                    links1.add(meshLink1);
                }
            }

        }
        catch (Exception e)
        {
            logger.error("Build Mesh Exception:", e);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 查找点所在的三角形
     * 
     * @param pt
     * @return
     */
    public MeshTriangle findTriangleByPoint(MeshPoint pt)
    {
        for (MeshArea area : areaMap.values())
        {
            MeshTriangle triangle = area.findTriangle(pt);
            if (triangle != null)
                return triangle;
        }

        return null;
    }

    /**
     * 查找起始-终点的link列表
     * 
     * @param start
     * @param end
     * @return
     */
    public List<MeshLink> findMeshLink(MeshPoint start, MeshPoint end)
    {
        List<MeshLink> list = new ArrayList<>();

        MeshTriangle startTri = findTriangleByPoint(start);
        MeshTriangle endTri = findTriangleByPoint(end);

        if (startTri == null || endTri == null || startTri.getAreaID() == endTri.getAreaID())
            return list;

        findMeshLink(startTri.getAreaID(), endTri.getAreaID(), list);

        return list;
    }

    /**
     * 递归查询link列表
     * 
     * @param startArea
     * @param endArea
     * @param list
     * @return
     */
    private boolean findMeshLink(int startArea, int endArea, List<MeshLink> list)
    {
        List<MeshLink> links = linkMap.get(startArea);

        if (links == null)
            return false;

        for (MeshLink link : links)
        {
            if (link.getEndArea() == endArea)
            {
                list.add(link);
                return true;
            }
            else
            {
                boolean isContain = findMeshLink(link.getEndArea(), endArea, list);
                if (isContain)
                {
                    list.add(link);
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * 查询路径
     * 
     * @param start
     * @param end
     * @return
     */
    public MeshPathResult findPath(MeshPoint start, MeshPoint end)
    {
        MeshPathResult result = new MeshPathResult();

        MeshTriangle startTri = findTriangleByPoint(start);
        MeshTriangle endTri = findTriangleByPoint(end);
        start.type = MeshPoint.START_POINT;
        end.type = MeshPoint.END_POINT;

        if (startTri == null || endTri == null)
        {
            result.setResult(false);
            return result;
        }

        // 如果起始点相同
        if (startTri == endTri)
        {
            List<MeshPoint> res = new ArrayList<MeshPoint>();
            res.add(start);
            res.add(end);
            result.addPoint(startTri.getAreaID(), res);

            return result;
        }

        reset();

        // 如果起始区域相同
        if (startTri.getAreaID() == endTri.getAreaID())
        {
            List<MeshPoint> res = new ArrayList<MeshPoint>();
            MeshArea area = areaMap.get(startTri.getAreaID());
            res = area.aStar(startTri, start, endTri, end);
            result.addPoint(startTri.getAreaID(), res);
            return result;
        }

        // 得到跳转link列表
        List<MeshLink> meshLinks = findMeshLink(start, end);

        // 起始区域不同，但没有路径
        if (meshLinks.size() <= 0)
        {
            result.setResult(false);
            return result;
        }

        // 开始区域的路径
        MeshArea startArea = areaMap.get(startTri.getAreaID());
        MeshLink startLink = meshLinks.get(meshLinks.size() - 1);
        List<MeshPoint> list1 = startArea.aStar(startTri, start, startLink.getBeginTriangle(), startLink.getBeginPoint());
        result.addPoint(startArea.getAreaID(), list1);

        // 中间跳转路径
        for (int i = meshLinks.size() - 1; i > 0; i--)
        {
            MeshLink l1 = meshLinks.get(i);
            MeshLink l2 = meshLinks.get(i - 1);
            MeshArea area = areaMap.get(l1.getEndArea());
            List<MeshPoint> listPoint = area.aStar(l1.getEndTriangle(), l1.getEndPoint(), l2.getBeginTriangle(), l2.getBeginPoint());
            result.addPoint(area.getAreaID(), listPoint);
        }

        // 结束区域的路径
        MeshArea endArea = areaMap.get(endTri.getAreaID());
        MeshLink endLink = meshLinks.get(0);
        List<MeshPoint> list2 = endArea.aStar(endLink.getEndTriangle(), endLink.getEndPoint(), endTri, end);
        result.addPoint(endArea.getAreaID(), list2);

        return result;
    }

    /**
     * 设置三角形是否可以通过
     * 
     * @param triangle
     * @param isCross
     */
    public void setTriangleCross(MeshTriangle triangle, boolean isCross)
    {
        triangle.setCross(isCross);
    }

    /**
     * 检查点是否合法
     * 
     * @param x
     *            单位 M
     * @param y
     *            单位 M
     * @return
     */
    public boolean checkCanMove(float x, float y)
    {
        MeshTriangle triangle = findTriangleByPoint(new MeshPoint(x, y));
        if (triangle != null)
            return true;

        return false;
    }

    /**
     * 计算三角形内一点与三角形外一点与边的交点
     * 
     * @param ix
     *            :内点
     * @param iy
     *            :内点
     * @param tx
     *            :外点
     * @param ty
     *            :外点
     * @return
     */
    public MeshPoint calPointTriangleIncr(float ix, float iy, float ox, float oy)
    {
        MeshTriangle triangle = findTriangleByPoint(new MeshPoint(ix, iy));
        if (triangle == null)
            return null;

        MeshPoint p1 = null, p2 = null;

        p1 = triangle.points[1];
        p2 = triangle.points[2];

        MeshPoint point = calTwoSegmentsIntr(new MeshPoint(ix, iy), new MeshPoint(ox, oy), p1, p2);
        if (point != null)
            return point;

        p1 = triangle.points[0];
        p2 = triangle.points[2];
        point = calTwoSegmentsIntr(new MeshPoint(ix, iy), new MeshPoint(ox, oy), p1, p2);
        if (point != null)
            return point;

        p1 = triangle.points[0];
        p2 = triangle.points[1];
        point = calTwoSegmentsIntr(new MeshPoint(ix, iy), new MeshPoint(ox, oy), p1, p2);
        if (point != null)
            return point;

        return point;
    }

    /**
     * 计算两线段的交点，不一定相交
     * 
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public MeshPoint calTwoSegmentsIntr(MeshPoint a, MeshPoint b, MeshPoint c, MeshPoint d)
    {
        // 解线性方程组, 求线段交点, 如果分母为0 则平行或共线, 不相交,斜率相同平行，不想交
        float denominator = (b.y - a.y) * (d.x - c.x) - (a.x - b.x) * (c.y - d.y);
        if (denominator == 0)
            return null;

        // 线段所在直线的交点坐标 (x , y)，根据二元一次方程式求
        float x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y) + (b.y - a.y) * (d.x - c.x) * a.x - (d.y - c.y) * (b.x - a.x) * c.x)
                / denominator;
        float y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x) + (b.x - a.x) * (d.y - c.y) * a.y - (d.x - c.x) * (b.y - a.y) * c.y)
                / denominator;

        // 判断交点是否在两条线段上 交点在线段1上
        if ((x - a.x) * (x - b.x) <= 0 && (y - a.y) * (y - b.y) <= 0)
        {
            // 且交点也在线段2上
            if ((x - c.x) * (x - d.x) <= 0 && (y - c.y) * (y - d.y) <= 0)
            {
                return new MeshPoint(x, y);
            }
        }

        // 否则不相交
        return null;
    }

    /**
     * 重置查询信息
     */
    public void reset()
    {
        for (MeshArea area : areaMap.values())
        {
            area.reset();
        }
    }

    public static void main(String[] args)
    {
        try
        {
            String xmlStr = FileUtil.readTxt("C:\\server\\fight\\trunk\\GameServer\\config\\map\\4602.xml", "UTF-8");
            XmlTriangleList list = XmlUtil.toObject(xmlStr, XmlTriangleList.class);

            MeshMap map = new MeshMap();
            map.init(list);

//            long time = System.currentTimeMillis();
//            for (int i = 0; i < 1800; i++)
//            {
//                map.checkCanMove(i, i + 1);
//            }
//            System.err.println("stop:" + (System.currentTimeMillis() - time));
            
            int VALUE=100;

            // 画三角形
            String outPath = "c:/bb.jpg";
            int width = 4096*2;
            int height = 4096*2;
            try
            {
                File imageFile = new File(outPath);
                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = (Graphics2D) bi.getGraphics();

                g2.fillRect(0, 0, width, height);// 填充区域，将区域背景设置为白色

                g2.setStroke(new BasicStroke(1));// 设置画笔的粗度

                for (XmlTriangle t : list.getTriangleList())
                {
                    g2.setPaint(Color.red);
                    XmlPoint[] points = t.getVertices();
                    g2.fillPolygon(new int[] {
                            (int) ((points[0].getX() + VALUE) * 10),
                            (int) ((points[1].getX() + VALUE) * 10),
                            (int) ((points[2].getX() + VALUE) * 10), },
                            new int[] {
                                    (int) ((points[0].getY() + VALUE) * 10),
                                    (int) ((points[1].getY() + VALUE) * 10),
                                    (int) ((points[2].getY() + VALUE) * 10),
                            }, 3);
                    
                    System.err.println(String.format("%s,%s,%s-%s,%s,%s",(int) ((points[0].getX() + VALUE) * 10),
                            (int) ((points[1].getX() + VALUE) * 10),
                            (int) ((points[2].getX() + VALUE) * 10), (int) ((points[0].getY() + VALUE) * 10),
                                    (int) ((points[1].getY() + VALUE) * 10),
                                    (int) ((points[2].getY() + VALUE) * 10)));

                    g2.setPaint(Color.black);
                    g2.drawPolygon(new int[] {
                            (int) ((points[0].getX() + VALUE) * 10),
                            (int) ((points[1].getX() + VALUE) * 10),
                            (int) ((points[2].getX() + VALUE) * 10), },
                            new int[] {
                                    (int) ((points[0].getY() + VALUE) * 10),
                                    (int) ((points[1].getY() + VALUE) * 10),
                                    (int) ((points[2].getY() + VALUE) * 10),
                            }, 3);
                }

//                MeshPoint p1 = new MeshPoint(9.04f, -21.25f);
//                MeshPoint p2 = new MeshPoint(10.0f, -50.0f);
//                time = System.currentTimeMillis();
//                for (int i = 0; i < 1000; i++)
//                {
//                    map.calPointTriangleIncr(p1.x, p1.y, p2.x, p2.y);
//                }
//                System.err.println("Find:" + (System.currentTimeMillis() - time));
//
//                MeshPathResult result = map.findPath(p1, p2);
//
//                List<MeshPoint> all = result.getPath();
//                all.add(0, p1);
//                all.add(p2);

                // 查找打印三角形离外面一点连线的交点
//                MeshPoint point = map.calPointTriangleIncr(p1.x, p1.y, p2.x, p2.y);
//                if (point != null)
//                {
//                    g2.setPaint(Color.blue);
//                    g2.drawLine((int) (p1.x + 100) * 10, (int) (p1.y + 100) * 10, (int) (p2.x + 100) * 10,
//                            (int) (p2.y + 100) * 10);
//                    g2.fillRect((int) (p1.x + 100) * 10, (int) (p1.y + 100) * 10, 5, 5);
//                    g2.fillRect((int) (p2.x + 100) * 10, (int) (p2.y + 100) * 10, 5, 5);
//
//                    g2.fillRect((int) (point.x + 100) * 10, (int) (point.y + 100) * 10, 5, 5);
//                }

//                g2.setPaint(Color.blue);
//                for (int i = 0; i < all.size() - 1; i++)
//                {
//                    MeshPoint entry0 = all.get(i);
//                    MeshPoint entry1 = all.get(i + 1);
//                    g2.drawLine((int) (entry0.x + 100) * 10, (int) (entry0.y + 100) * 10, (int) (entry1.x + 100) * 10,
//                            (int) (entry1.y + 100) * 10);
//                    g2.fillRect((int) (entry0.x + 100) * 10, (int) (entry0.y + 100) * 10, 5, 5);
//                    g2.fillRect((int) (entry1.x + 100) * 10, (int) (entry1.y + 100) * 10, 5, 5);
//
//                    System.err.println(entry0.toString());
//                }

                ImageIO.write(bi, "png", imageFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
