package com.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.util.wrapper.WrapPoint;
import com.util.wrapper.WrapVector;

/**
 * 数学公式计算工具类
 * 
 * @author dream.wang
 * 
 */
public class MathsUtil
{
    public static final float BasePercentNumber = 10000.0f;

    public static final float PI = 3.1415926f;

    /**
     * 在固定点周围r范围随机一个点
     * 
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public static WrapPoint randomPoint(int x, int y, int radius)
    {
        int tx = ThreadSafeRandom.next(x, x + radius);
        int ty = ThreadSafeRandom.next(y, y + radius);

        return new WrapPoint(tx, ty);
    }

    /**
     * 对比两个点
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param limits
     * @return
     */
    public static boolean comparePoint(int x1, int y1, int x2, int y2, int limits)
    {
        if (Math.abs(x1 - x2) <= limits && Math.abs(y1 - y2) <= limits)
            return true;

        return false;
    }

    /**
     * 判断点是否在圆内
     * 
     * @param pointX
     * @param pointY
     * @param circleX
     * @param circleY
     * @param radius
     * @return true 包含 ；false 不包含
     */
    public static boolean pointInCircle(int pointX, int pointY, int circleX, int circleY, int radius)
    {
        int dx = circleX - pointX;
        int dy = circleY - pointY;

        int d1 = dx * dx + dy * dy;
        int d2 = radius * radius;
        return d1 < d2;
    }

    /**
     * 判断点是否在圆环内
     * 
     * @param pointX
     * @param pointY
     * @param circleX
     * @param circleY
     * @param minRadius
     * @param maxRadius
     * @return true 包含 ；false 不包含
     */
    public static boolean pointInCirque(int pointX, int pointY, int circleX, int circleY, int minRadius, int maxRadius)
    {
        int dx = circleX - pointX;
        int dy = circleY - pointY;
        if (dx * dx + dy * dy <= maxRadius * maxRadius && dx * dx + dy * dy >= minRadius * minRadius)
            return true;

        return false;
    }

    /**
     * 计算点是否在扇形区内
     * 
     * @param pointX
     *            点坐标
     * @param pointY
     *            点坐标
     * @param sectorX
     *            扇形坐标
     * @param sectorY
     *            扇形坐标
     * @param radius
     *            扇形半径(cm)
     * @param sectorAngle
     *            扇形角度(0-360)
     * @param dir
     *            扇形方向(0-360)
     * @return
     */
    public static boolean pointInSector(int pointX, int pointY, int sectorX, int sectorY, int radius, int sectorAngle,
            int dir)
    {
        int distance = (sectorX - pointX) * (sectorX - pointX) + (sectorY - pointY) * (sectorY - pointY);
        if (distance <= radius * radius)
        {
            int angle = (int) twoVectorAngle(angleTransVector(dir), new WrapVector(pointX - sectorX, pointY - sectorY));
            if (angle <= sectorAngle / 2)
                return true;
            // else
            // System.err.println(String.format("MathsUtil.PointInSector -- angle：%s sector angle:%s", angle,
            // sectorAngle / 2));

            // 距离非常近的时候，也算击中
            if (distance <= 2500)
                return true;
        }
        // else
        // System.err.println(String.format("MathsUtil.PointInSector -- distance：%s radius power:%s", distance, radius *
        // radius));

        return false;
    }

    /**
     * 计算点是否在扇形环区内
     * 
     * @param pointX
     *            点坐标
     * @param pointY
     *            点坐标
     * @param sectorX
     *            扇形坐标
     * @param sectorY
     *            扇形坐标
     * @param minRadius
     *            扇形半径
     * @param maxRadius
     *            扇形半径
     * @param sectorAngle
     *            扇形角度
     * @param dir
     *            扇形方向
     * @return
     */
    public static boolean pointInSectorCirque(int pointX, int pointY, int sectorX, int sectorY, float minRadius,
            float maxRadius,
            float sectorAngle, float dir)
    {
        float distance = (sectorX - pointX) * (sectorX - pointX) + (sectorY - pointY) * (sectorY - pointY);
        if (distance <= maxRadius * maxRadius && distance >= minRadius * minRadius)
        {
            float angle = twoVectorAngle(angleTransVector(dir), new WrapVector(pointX - sectorX, pointY - sectorY));
            if (angle <= sectorAngle / 2)
                return true;
        }
        return false;
    }

    /**
     * 计算点是否在扇形区内(两个角度)
     * 
     * @param pointX
     *            点坐标
     * @param pointY
     *            点坐标
     * @param sectorX
     *            扇形坐标
     * @param sectorY
     *            扇形坐标
     * @param radius
     *            扇形半径
     * @param sectorAngle
     *            扇形角度
     * @param dir
     *            扇形方向
     * @return
     */
    public static boolean pointInSectorBetweenAngle(int pointX, int pointY, int sectorX, int sectorY, float radius,
            float minAngle,
            float maxAngle)
    {
        float distance = (sectorX - pointX) * (sectorX - pointX) + (sectorY - pointY) * (sectorY - pointY);
        if (distance <= radius * radius)
        {
            float angle = vectorTransAngle(new WrapVector(pointX - sectorX, pointY - sectorY));
            if (minAngle < 0 && maxAngle < 0)
            {
                minAngle = 360 + minAngle;
                maxAngle = 360 + maxAngle;
            }
            if (angle <= maxAngle && angle >= minAngle)
                return true;
        }
        return false;
    }

    /**
     * 计算两个向量的夹角
     * 向量是无起始结束方向的（不超过180度）
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static float twoVectorAngle(WrapVector v1, WrapVector v2)
    {
        float a = v1.x * v1.x + v1.y * v1.y;
        float b = v2.x * v2.x + v2.y * v2.y;
        float ab = v1.x * v2.x + v1.y * v2.y;
        return (float) Math.toDegrees(Math.acos(ab / Math.sqrt(a * b)));
    }

    /**
     * 计算两个向量的夹角(带方向)
     * 向量是有起始结束方向的（可能超过180度）
     * 
     * @param begin
     * @param end
     * @return
     */
    public static float twoVectorAngle1(WrapVector begin, WrapVector end)
    {
        float degree1 = vectorTransAngle(begin);
        float degree2 = vectorTransAngle(end);

        return degree2 - degree1 >= 0 ? degree2 - degree1 : 360 + degree2 - degree1;
    }

    /**
     * 判断点是否在矩形框中
     * 
     * @param pointX
     *            点x
     * @param pointY
     *            点y
     * @param rectX
     *            矩形左下x
     * @param rectY
     *            矩形左下y
     * @param width
     *            矩形宽
     * @param height
     *            矩形高
     * @return
     */
    public static boolean pointInRectange(int pointX, int pointY, int rectX, int rectY, int width, int height)
    {
        int dx = Math.abs(rectX - pointX);
        int dy = Math.abs(rectX - pointY);
        return dx < width / 2 && dy < height / 2;
    }

    /**
     * 四舍五入
     */
    public static int round(float value)
    {
        return (int) (value + 0.5f);
    }

    /**
     * 对double数据进行取精度.
     * <p>
     * For example: <br>
     * double value = 100.345678; <br>
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>
     * ret为100.3457 <br>
     * 
     * @param value
     *            double数据.
     * @param scale
     *            精度位数(保留的小数位数).
     * @param roundingMode
     *            精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode)
    {
        return new BigDecimal(value).setScale(scale, roundingMode).doubleValue();
    }

    public static double round(double value, int scale)
    {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 对float数据进行取精度.
     * <p>
     * For example: <br>
     * double value = 100.345678; <br>
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>
     * ret为100.3457 <br>
     * 
     * @param value
     *            double数据.
     * @param scale
     *            精度位数(保留的小数位数).
     * @param roundingMode
     *            精度取值方式.
     * @return 精度计算后的数据.
     */
    public static float round(float value, int scale, int roundingMode)
    {
        return new BigDecimal(value).setScale(scale, roundingMode).floatValue();
    }

    public static float round(float value, int scale)
    {
        return new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 角度转向量(单位向量位1000)
     * 
     * @param angle
     * @return
     */
    public static WrapVector angleTransVector(float angle)
    {
        WrapVector vector = new WrapVector();
        vector.x = (int) (Math.cos(Math.toRadians(angle)) * 1000);
        vector.y = (int) (Math.sin(Math.toRadians(angle)) * 1000);

        return vector;
    }

    /**
     * 向量转角度(0-360)
     * 
     * @param vector
     * @return(0-360)
     */
    public static float vectorTransAngle(WrapVector vector)
    {
        if (vector.x == 0 && vector.y == 0)
            return (float) Math.toDegrees(0);

        float angle = (float) Math.toDegrees(Math.atan2(vector.y, vector.x));
        angle = formatAngle(angle);
        return angle;
    }

    /**
     * 两点的角度(0-360)
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static int twoPointAngle(int x1, int y1, int x2, int y2)
    {
        WrapVector vector = new WrapVector(x2 - x1, y2 - y1);

        return (int) vectorTransAngle(vector);
    }

    /**
     * 两点之间的距离
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float distance(int x1, int y1, int x2, int y2)
    {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 两点之间的距离
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float distance(float x1, float y1, float x2, float y2)
    {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 两点之间的距离平方
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float distanceSquare(int x1, int y1, int x2, int y2)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    /**
     * 点(px, py)到过点(x1,y1),(x2,y2)的直线的距离
     * 先带入两点式，在根据直线式求
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param px
     * @param py
     * @return
     */
    public static int pointToLine(int x1, int y1, int x2, int y2, int px, int py)
    {
        int a = y1 - y2;
        int b = x2 - x1;
        int c = x1 * y2 - x2 * y1;
        return (int) (Math.abs(a * px + b * py + c) / Math.sqrt(a * a + b * b));
    }

    /**
     * 计算点（x，y）延angle角度前进distance距离后的点
     * 
     * @param x1
     * @param y1
     * @param angle
     *            (0-360)
     * @param distance
     * @return
     */
    public static WrapPoint pointAngleDistance(int x1, int y1, float angle, float distance)
    {
        int x = (int) (x1 + distance * Math.cos(Math.toRadians(angle)));
        int y = (int) (y1 + distance * Math.sin(Math.toRadians(angle)));
        return new WrapPoint(x, y);
    }

    /**
     * 计算两点之间A(x1,y1)，B(x2,y2)距离A点长度为distance的点C的位置</br>
     * 求点坐标 ：已知两点A,B 和 C点离起始点A的距离
     * 
     * @param x1
     *            起始点
     * @param y1
     *            起始点
     * @param x2
     *            终点
     * @param y2
     *            终点
     * @param distance
     *            点离起始点的距离
     * @return
     */
    public static WrapPoint pointInTwoPointLine(int x1, int y1, int x2, int y2, int distance)
    {
        float angle = twoPointAngle(x1, y1, x2, y2);
        return pointAngleDistance(x1, y1, angle, distance);
    }

    /**
     * 格式化角度 (0~360)
     * 
     * @param angle
     * @return
     */
    public static float formatAngle(float angle)
    {
        angle = angle % 360;
        if (angle < 0)
            return angle + 360;

        return angle;
    }

    /**
     * 根据相对坐标转换偏移量到新坐标点
     * 
     * @param x
     * @param y
     * @param angle
     */
    public static WrapPoint transformOffsetCoordinate(int x, int y, float angle, int offsetX, int offsetY)
    {
        WrapPoint point = new WrapPoint();
        // 求出偏移的距离
        int distance = (int) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
        // 偏移的角度
        float offsetAngle = vectorTransAngle(new WrapVector(offsetX, offsetY));
        // 新坐标点
        point = pointAngleDistance(x, y, angle + offsetAngle, distance);

        return point;
    }

    /**
     * 判断点是否在一个旋转矩形里面：判断一个点到这个矩形的两条中线的距离是否小于长宽。
     * 
     * @param px
     *            特定点x
     * @param py
     *            特定点y
     * @param recX
     *            矩形左下点
     * @param recY
     *            矩形左下点
     * @param w
     *            宽
     * @param h
     *            高
     * @param angle
     *            旋转角度(0-360)
     * @return
     */
    public static boolean pointInAngleRectangle(int px, int py, int recX, int recY, int w, int h, float angle)
    {
        // 求底边的中点
        WrapPoint aPoint = pointAngleDistance(recX, recY, angle + 90, h / 2);
        // 求底边的中点
        WrapPoint bPoint = pointAngleDistance(recX, recY, angle, w / 2);
        // 求中心点
        WrapPoint cPoint = pointAngleDistance(aPoint.x, aPoint.y, angle, w / 2);

        int distanceW = pointToLine(bPoint.x, bPoint.y, cPoint.x, cPoint.y, px, py);
        int distanceH = pointToLine(aPoint.x, aPoint.y, cPoint.x, cPoint.y, px, py);

        // 判断距离
        if (distanceH > h / 2 || distanceW > w / 2)
        {
            return false;
        }

        return true;
    }

    /**
     * 判断点是否在一个旋转矩形里面：判断一个点到这个矩形的两条中线的距离是否小于长宽。
     * 
     * @param px
     * @param py
     * @param centerX:矩形的中心点
     * @param centerY：矩形的中心点
     * @param w
     * @param h
     * @param angle
     * @return
     */
    public static boolean pointInAngleRectangleCenter(int px, int py, int centerX, int centerY, int w, int h,
            float angle)
    {
        // 中心点沿angel+90度移动h/2距离的点
        WrapPoint aPoint = pointAngleDistance(centerX, centerY, angle + 90, h / 2);
        // 中心点沿angel移动w/2距离的点
        WrapPoint bPoint = pointAngleDistance(centerX, centerY, angle, w / 2);

        int distanceW = pointToLine(bPoint.x, bPoint.y, centerX, centerY, px, py);
        int distanceH = pointToLine(aPoint.x, aPoint.y, centerX, centerY, px, py);

        // 判断距离
        if (distanceH > h / 2 || distanceW > w / 2)
        {
            return false;
        }

        return true;
    }

    /**
     * 判断圆是否和一个旋转矩形碰撞：先求出圆点与矩形中点连线与圆的交点，然后判断焦点是否在矩形中
     * 
     * @param px
     * @param py
     * @param radius
     * @param recX
     * @param recY
     * @param w
     * @param h
     * @param angle
     * @return
     */
    public static boolean circleInAngleRectangle(int px, int py, int radius, int recX, int recY, int w, int h,
            float angle)
    {
        // 求坐边的中点（竖边）
        WrapPoint aPoint = pointAngleDistance(recX, recY, angle + 90, h / 2);
        // 求底边的中点（横边）
        WrapPoint bPoint = pointAngleDistance(recX, recY, angle, w / 2);
        // 求中心点
        WrapPoint cPoint = pointAngleDistance(aPoint.x, aPoint.y, angle, w / 2);
        // 点到高中线的距离
        int distanceW = pointToLine(bPoint.x, bPoint.y, cPoint.x, cPoint.y, px, py);
        // 点到宽中线的距离
        int distanceH = pointToLine(aPoint.x, aPoint.y, cPoint.x, cPoint.y, px, py);

        // 判断距离
        if ((distanceH - radius) > h / 2 || (distanceW - radius) > w / 2)
        {
            return false;
        }

        return true;
    }

    /**
     * 判断圆是否和一个旋转矩形碰撞：先求出圆点与矩形中点连线与圆的交点，然后判断焦点是否在矩形中
     * 
     * @param px
     * @param py
     * @param radius
     * @param centerX:矩形中心点
     * @param centerY:矩形中心点
     * @param w
     * @param h
     * @param angle
     * @return
     */
    public static boolean circleInAngleRectangleCenter(int px, int py, int radius, int centerX, int centerY, int w,
            int h, float angle)
    {
        // 中心点沿angel+90度移动h/2距离的点
        WrapPoint aPoint = pointAngleDistance(centerX, centerY, angle + 90, h / 2);
        // 中心点沿angel移动w/2距离的点
        WrapPoint bPoint = pointAngleDistance(centerX, centerY, angle, w / 2);

        int distanceH = pointToLine(bPoint.x, bPoint.y, centerX, centerY, px, py);
        int distanceW = pointToLine(aPoint.x, aPoint.y, centerX, centerY, px, py);

        // 判断距离
        if ((distanceH - radius) > h / 2 || (distanceW - radius) > w / 2)
        {
            return false;
        }

        return true;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @return
     */
    public static int calculateCircleRandomInt(List<Integer> list)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int v : list)
        {
            sum += v;
            temp.add(sum);
        }

        int value = ThreadSafeRandom.next(1, sum + 1);

        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @return
     */
    public static int calculateCircleRandomInt(int[] list)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int v : list)
        {
            sum += v;
            temp.add(sum);
        }

        int value = ThreadSafeRandom.next(1, sum + 1);
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @return
     */
    public static int calculateCircleCheckRandomInt(List<Integer> list, int seed)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i);
            temp.add(sum);
        }

        CheckRandom random = new CheckRandom(seed);
        int value = random.rand(1, sum);
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @param random
     * @return
     */
    public static int calculateCircleCheckRandomInt(List<Integer> list, CheckRandom random)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i);
            temp.add(sum);
        }

        int value = random.rand(1, sum);
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @return
     */
    public static int calculateCircleRandomFloat(List<Float> list)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i) * 10000;
            temp.add(sum);
        }

        int value = ThreadSafeRandom.next(1, sum + 1);
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆盘算法，返回位置索引
     * 
     * @param list
     * @param random
     * @return
     */
    public static int calculateCircleRandomFloat(List<Float> list, CheckRandom random)
    {
        List<Integer> temp = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i) * 10000;
            temp.add(sum);
        }

        int value = random.rand(1, sum);
        random.result = value;
        for (int i = 0; i < temp.size(); i++)
        {
            if (value <= temp.get(i))
                return i;
        }
        return 0;
    }

    /**
     * 圆内随机点
     * 
     * @param point
     * @param radius
     * @return
     */
    public static WrapPoint randomInCircle(WrapPoint point, int radius)
    {
        int x = ThreadSafeRandom.next(point.x - radius, point.x + radius);
        int y = ThreadSafeRandom.next(point.y - radius, point.y + radius);

        float distance = MathsUtil.distance(x, y, point.x, point.y);
        if (distance <= radius)
        {
            return new WrapPoint(x, y);
        }
        else
        {
            return randomInCircle(point, radius);
        }
    }

    /**
     * 计算两个直线的交点（或者已知这两条线段一定相交）
     * 
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public WrapPoint calTwoLinesIntr(WrapPoint a, WrapPoint b, WrapPoint c, WrapPoint d)
    {
        /** 1 解线性方程组, 求线段交点. **/
        // 如果分母为0 则平行或共线, 不相交,斜率相同平行，不想交
        float denominator = (b.y - a.y) * (d.x - c.x) - (a.x - b.x) * (c.y - d.y);
        if (denominator == 0)
            return null;

        // 线段所在直线的交点坐标 (x , y)，根据二元一次方程式求
        float x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y) + (b.y - a.y) * (d.x - c.x) * a.x
                - (d.y - c.y) * (b.x - a.x) * c.x)
                / denominator;
        float y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x) + (b.x - a.x) * (d.y - c.y) * a.y
                - (d.x - c.x) * (b.y - a.y) * c.y)
                / denominator;

        return new WrapPoint((int) x, (int) y);
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
    public WrapPoint calTwoSegmentsIntr(WrapPoint a, WrapPoint b, WrapPoint c, WrapPoint d)
    {
        // 解线性方程组, 求线段交点, 如果分母为0 则平行或共线, 不相交,斜率相同平行，不想交
        float denominator = (b.y - a.y) * (d.x - c.x) - (a.x - b.x) * (c.y - d.y);
        if (denominator == 0)
            return null;

        // 线段所在直线的交点坐标 (x , y)，根据二元一次方程式求
        float x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y) + (b.y - a.y) * (d.x - c.x) * a.x
                - (d.y - c.y) * (b.x - a.x) * c.x)
                / denominator;
        float y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x) + (b.x - a.x) * (d.y - c.y) * a.y
                - (d.x - c.x) * (b.y - a.y) * c.y)
                / denominator;

        // 判断交点是否在两条线段上 交点在线段1上
        if ((x - a.x) * (x - b.x) <= 0 && (y - a.y) * (y - b.y) <= 0)
        {
            // 且交点也在线段2上
            if ((x - c.x) * (x - d.x) <= 0 && (y - c.y) * (y - d.y) <= 0)
            {
                return new WrapPoint((int) x, (int) y);
            }
        }

        // 否则不相交
        return null;
    }

    public static void main(String[] args)
    {
        // WrapVector v0 = new WrapVector(1, 1);
        // float f1=MathsUtil.vectorTransAngle(v0);
        // System.err.println(f1);
        //
        // WrapVector v1 = new WrapVector(1, -1);
        // WrapVector v2 = new WrapVector(1, 1);
        // float angle = MathsUtil.twoVectorAngle1(v2, v1);
        //
        // System.err.println("angle:" + angle);
        // WrapVector vector = MathsUtil.angleTransVector(271);
        // System.err.println("VX:"+vector.x+",VY:"+vector.y);

        // y/x
        // float a=(float) (MathsUtil.vectorTransAngle(new WrapVector(0, 0))*(180/Math.PI));
        // System.err.println("a:"+a);
    }
}
