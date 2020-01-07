package com.test;

public class Define
{
    /** 平台主题类型 */
    public static enum PlatformTheme
    {
        Wood,         // 木头
        Fire,         // 火山
        Grass,        // 草原
        Winter        // 冰川
    }

    /** 玩法模式类型 */
    public enum ModeType
    {
        Practice,           // 练习
        Plot,               // 剧情
        EndLess,            // 无尽
        TimeLimit,          // 限时
        RAC,                // 竞速
        RandomMatch,        // 随机匹配
        LAN,                // 局域网
        RandomProp,         // 随机道具
    }

    public static enum NewPlatformGroupType
    {
        Grass,
        Winter
    }

    public static class ManagerVars
    {
        public float nextYPos;
        public float nextXPos;
        public Sprite[] platformThemeSpriteList;
    }

    /** 平台类型 */
    public static enum PlatformType
    {
        /** 常规类型 */
        Normal,
        /** 通用组合类型 */
        Common,
        /** 主题组合类型 */
        Theme,
        /** 尖刺组合类型 */
        Spike,
    }

    public static class Sprite
    {

    }

    public static class GameObject
    {

    }

    public static class Time
    {
        public static float deltaTime;

    }

    public static class Vector2
    {
        public static Vector2 zero = new Vector2(0, 0);

        public Vector2(float i, float f)
        {
            this.x = i;
            this.y = f;
        }

        public float x;
        public float y;
    }

    public static class Vector3
    {
        public static Vector3 zero = new Vector3(0, 0, 0);

        public Vector3(float i, float f, float j)
        {
            this.x = i;
            this.y = f;
            this.z = j;
        }

        public float x;
        public float y;
        public float z;
    }

    // 平台组合的信息
    public static class PlatformInfo
    {
        /** 平台类型 */
        public PlatformType m_platformType;     // 平台的类型
        /** 平台位置 */
        public Vector3 m_pos;                   // 平台的位置
        /** 障碍物的方向(1：左 0:右) */
        public int m_obstacleDir;               // 障碍物的方向(1：左 0:右)
        /** 平台掉落时间(单机才有) */
        public float m_fallTime;                // 平台掉落时间(单机才有)
        /** 是否有钻石 */
        public boolean m_bHasDiamond;              // 是否有钻石
        /** 显示是是否需要Tween（地图前五个地块为false） */
        public boolean m_bShowTween;               // 显示是是否需要Tween（地图前五个地块为false）
    }
}
