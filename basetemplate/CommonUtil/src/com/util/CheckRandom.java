package com.util;

/**
 * 梅森旋转随机算法多语言通用版(Java版本)
 * 
 * @author dream
 *
 */
public class CheckRandom
{
    /* Create a length 624 array to store the state of the generator */
    private int[] data = new int[624];
    private int index;

    public int result = 0;

    public CheckRandom(int seed)
    {
        init(seed);
    }

    /**
     * 由一个seed初始化随机数产生器
     * 首先将传入的seed赋给MT[0]作为初值，然后根据递推式：MT[i] = f × (MT[i-1] ⊕ (MT[i-1] >> (w-2))) + i递推求出梅森旋转链
     * 
     * @param seed
     */
    private void init(int seed)
    {
        int i;
        int p;
        index = 0;
        data[0] = seed;
        /* loop over each other element */
        for (i = 1; i < 624; ++i)
        {
            p = 1812433253 * (data[i - 1] ^ (data[i - 1] >> 30)) + i;
            data[i] = p & 0xffffffff; /* get last 32 bits of p */
        }
    }

    private int calculate()
    {
        if (index == 0)
            generator();

        int y = data[index];
        y = y ^ (y >> 11);
        y = y ^ ((y << 7) & (int) 2636928640l);
        y = y ^ ((y << 15) & (int) 4022730752l);
        y = y ^ (y >> 18);

        /* increment idx mod 624 */
        index = (index + 1) % 624;
        return y;
    }

    private void generator()
    {
        int i;
        int y;
        for (i = 0; i < 624; ++i)
        {
            y = (data[i] & 0x80000000) + (data[(i + 1) % 624] & 0x7fffffff);
            data[i] = data[(i + 397) % 624] ^ (y >> 1);
            if (y % 2 != 0)
            { /* y is odd */
                data[i] = data[i] ^ (int) (2567483615l);
            }
        }
    }

    public void changeSeed(int seed)
    {
        init(seed);
    }

    public int rand(int min, int max)
    {
        double random = calculate() * 1.0d / Integer.MAX_VALUE;
        return (int) ((max - min) * random);
    }

    public int rand(int max)
    {
        float random = calculate() * 1.0f / Integer.MAX_VALUE;
        return (int) (max * random);
    }

    public static void main(String[] args)
    {
        CheckRandom randomUtil = new CheckRandom(100);
        for (int a = 0; a < 100; a++)
        {
            int bb = randomUtil.rand(1, 1000);
            System.err.print(bb + ",");
        }
    }
}
