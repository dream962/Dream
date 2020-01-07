package com.util.structure;

public class UnityData
{
    public static class Vector2
    {
        public float x;
        public float y;

        public Vector2(float i, float f)
        {
            this.x = i;
            this.y = f;
        }

        public static Vector2 zero()
        {
            return new Vector2(0, 0);
        }
    }

    public static class Vector3
    {
        public float x;
        public float y;
        public float z;

        public Vector3(float i, float f, float j)
        {
            this.x = i;
            this.y = f;
            this.z = j;
        }

        public static Vector3 zero()
        {
            return new Vector3(0, 0, 0);
        }
    }
}
