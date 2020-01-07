package com.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * java标准库只实现了bitset，每次操作1位，此扩展，可以支持 2的倍数的bit单元（2,4,8,16,32,64），支持不同位置存储数值
 * 
 * @author dream
 *
 */
public class MultiBitSet implements Cloneable
{
    /** words是long型，64位，即2的6次方，对64求除，相当于右移6位 */
    private final static int ADDRESS_BITS_PER_WORD = 6;

    /** 一个word支持的最大单元 */
    private final static int BITS_PER_WORD = 64;

    /** 默认的一个单元一个bit位 */
    private final static int DEFAULT_UNITSIZE = 1;

    /** 默认初始32个单元 */
    private final static int DEFAULT_UNITCOUNT = 32;

    /** 实际存储数据 */
    private long[] words;

    /**
     * 多少个bit作为一个unit(官方的bitset相当于此处的unitSize=1,unitSize这里只支持1 <= unitSize <= 64 且必须为2的幂数)
     */
    private int unitSize;

    /** 当前实际上申请了多少个word内存单元 */
    private int wordsInUse = 0;

    /**
     * 创建一个MultiBitSet
     * 
     * @param data
     *            数据组
     * @param unitSize
     *            每单位多少个bit位
     */
    public MultiBitSet(byte[] data, int unitSize)
    {
        ByteBuffer bb = ByteBuffer.wrap(data);
        this.unitSize = unitSize;

        bb = bb.slice().order(ByteOrder.LITTLE_ENDIAN);
        // 保留空数据
        int n = bb.remaining();

        // 空数据剔除的方式
        // for (n = bb.remaining(); n > 0 && bb.get(n - 1) == 0; n--)
        // ;

        long[] words = new long[(n + 7) / 8];
        bb.limit(n);
        int i = 0;
        while (bb.remaining() >= 8)
            words[i++] = bb.getLong();
        for (int remaining = bb.remaining(), j = 0; j < remaining; j++)
            words[i] |= (bb.get() & 0xffL) << (8 * j);
        this.words = words;
        this.wordsInUse = words.length;

        assert (wordsInUse == 0 || words[wordsInUse - 1] != 0);
        assert (wordsInUse >= 0 && wordsInUse <= words.length);
        assert (wordsInUse == words.length || words[wordsInUse] == 0);
    }

    /**
     * 创建一个MultiBitSet
     * 
     * @param unitSize
     *            一个单元的bit位数
     * @param unitCount
     *            初始化多少个单元
     */
    public MultiBitSet(int unitSize, int unitCount)
    {
        super();
        if (unitSize < 0)
            throw new NegativeArraySizeException("unitSize < 0: " + unitSize);

        if (unitCount < 0)
            throw new NegativeArraySizeException("unitCount < 0: " + unitCount);

        // 如果参数错误，则给个默认值
        if (unitSize > BITS_PER_WORD)
        {
            unitSize = DEFAULT_UNITSIZE;
            unitCount = DEFAULT_UNITCOUNT;
        }
        this.unitSize = unitSize;
        initWords(unitCount);
    }

    /**
     * 创建
     * 
     * @param unitSize
     *            一个单元bit的个数
     */
    public MultiBitSet(int unitSize)
    {
        this(unitSize, DEFAULT_UNITCOUNT);
    }

    /**
     * 取得单元的个数
     * 
     * @return
     */
    public int getUnitSize()
    {
        return wordsInUse * (BITS_PER_WORD / unitSize);
    }

    /**
     * 返回当前的unit的word索引
     * 
     * @param unitIndex
     *            unit索引，从0开始
     * @return word索引，从0开始
     */
    private int wordIndex(int unitIndex)
    {
        // unit单元的位置+1（从0开始）*每个单元的bit位数-1（从0开始），即取得指定单元最大的bit位位置
        // 再除以64（或右移6位），得到指定单元在long[]里面的位置
        return ((unitIndex + 1) * unitSize - 1) >>> ADDRESS_BITS_PER_WORD;
    }

    /**
     * 初始化内存
     * 
     * @param nUnits
     *            unit的个数
     */
    private void initWords(int nUnits)
    {
        words = new long[wordIndex(nUnits - 1) + 1];
        this.wordsInUse = words.length;
    }

    /**
     * 设置unitIndex指向的块值
     * 
     * @param unitIndex
     *            要设置的块的下标
     * @param value
     *            要设置的值(如果unitSize < 8,比如unitSize = 4,则value只后4位生效)
     */
    private void set(int unitIndex, long value)
    {
        if (unitIndex < 0)
            throw new NegativeArraySizeException("unitIndex < 0: " + unitIndex);

        if (value >= (((long) 1) << unitSize))
            throw new IllegalArgumentException(unitIndex
                    + " pos value should not bigger than "
                    + ((1 << unitSize) - 1));

        int wordIndex = wordIndex(unitIndex);
        expandTo(wordIndex);

        // 计算mask
        long valueMask = ((long) 1 << unitSize) - 1;
        long valueMask1 = valueMask;
        valueMask &= value;
        int unitSizePerWord = BITS_PER_WORD / unitSize;
        unitIndex %= unitSizePerWord;
        unitSizePerWord -= (unitIndex + 1);
        valueMask <<= (unitSizePerWord * unitSize);
        valueMask1 <<= (unitSizePerWord * unitSize);
        valueMask1 = ~valueMask1;
        words[wordIndex] &= valueMask1;
        words[wordIndex] |= valueMask;
    }

    /**
     * 设置unitIndex指向的块值
     * 
     * @param unitIndex
     *            : 单元的位置
     * @param bitOffset
     *            ： 此单元里指定的bit位置
     * @param value
     *            ： 设置值
     */
    public void set(int unitIndex, int bitOffset, boolean value)
    {
        if (unitIndex < 0)
            throw new NegativeArraySizeException("unitIndex < 0: " + unitIndex);

        if (bitOffset >= unitSize)
            throw new NegativeArraySizeException("unitIndex > max: " + unitIndex);

        int wordIndex = wordIndex(unitIndex);
        expandTo(wordIndex);

        long data = get(unitIndex);

        if (value)
            data |= (((long) 0x01) << bitOffset);
        else
            data &= (~(((long) 0x01) << bitOffset));

        set(unitIndex, data);
    }
    
    /**
     * 设置unitIndex指向的块值
     * @param unitIndex 单元的位置
     * @param beginOffset 此单元里指定的bit开始位置
     * @param endOffset 此单元里指定的bit结束位置
     * @param value 具体值
     */
    public void set(int unitIndex, int beginOffset,int endOffset, int value)
    {
        long base = get(unitIndex);
        
        for(int i=beginOffset;i<=endOffset;i++)
            base &= (~(((long) 0x01) << i));
        
        long data = Long.MAX_VALUE & (value << beginOffset);
        data |= base;
        set(unitIndex, data);
    }

    /**
     * 得到一个unit单元的值
     * 
     * @param unitIndex
     *            单元的位置
     * @return 单元的表现值
     */
    private long get(int unitIndex)
    {
        if (unitIndex < 0)
            throw new IndexOutOfBoundsException("unitIndex < 0: " + unitIndex);

        // 一个单元的最大值，例：如果size=8，mask = 11111111
        long valueMask = (((long) 1) << unitSize) - 1;

        long valueMask1 = valueMask;

        // 在word里的索引
        int wordIndex = wordIndex(unitIndex);

        if (wordIndex >= wordsInUse)
            throw new IndexOutOfBoundsException("bitIndex < 0: " + unitIndex);

        // 每个word里面包含多少个单元
        int unitSizePerWord = BITS_PER_WORD / unitSize;

        // 取得在一个word里的offset
        unitIndex %= unitSizePerWord;

        unitSizePerWord -= (unitIndex + 1);
        valueMask <<= (unitSizePerWord * unitSize);
        valueMask = (words[wordIndex] & valueMask);
        valueMask >>>= (unitSizePerWord * unitSize);
        valueMask &= valueMask1;
        return valueMask;
    }

    /**
     * 得到一个unit单元,指定偏移的值
     * 
     * @param unitIndex
     *            单元的位置
     * @return 单元的表现值
     */
    public boolean get(int unitIndex, int bitOffset)
    {
        if (unitIndex < 0)
            throw new NegativeArraySizeException("unitIndex < 0: " + unitIndex);

        if (bitOffset >= unitSize)
            throw new NegativeArraySizeException("unitIndex > max: " + unitIndex);

        long data = get(unitIndex);
        boolean result = (data & ((long) 0x01 << bitOffset)) != 0;
        return result;
    }

    /**
     * 得到一个unit单元,指定偏移范围的值
     * 
     * @param unitIndex
     *            单元的位置
     * @return 单元的表现值
     */
    public int get(int unitIndex, int beginOffset, int endOffset)
    {
        if (unitIndex < 0)
            throw new NegativeArraySizeException("unitIndex < 0: " + unitIndex);

        if (beginOffset < 0)
            throw new NegativeArraySizeException("unitIndex < 0: " + unitIndex);

        if (endOffset >= unitSize)
            throw new NegativeArraySizeException("unitIndex > max: " + unitIndex);

        // 得到一个unit单元整体的值
        long data = get(unitIndex);
        data <<= (BITS_PER_WORD - endOffset);
        data >>>= (beginOffset + (BITS_PER_WORD - endOffset));
        return (int) data;
    }

    /**
     * 申请的内存不够时重新申请
     * 
     * @param wordIndex
     *            .
     */
    private void expandTo(int wordIndex)
    {
        int wordsRequired = wordIndex + 1;
        if (wordsInUse < wordsRequired)
        {
            ensureCapacity(wordsRequired);
            wordsInUse = wordsRequired;
        }
    }

    /**
     * 保证容量
     * 
     * @param wordsRequired
     */
    private void ensureCapacity(int wordsRequired)
    {
        if (words.length < wordsRequired)
        {
            // Allocate larger of doubled size or required size
            int request = Math.max(2 * words.length, wordsRequired);
            words = Arrays.copyOf(words, request);
        }
    }

    public byte[] toByteArray()
    {
        int n = wordsInUse;
        if (n == 0)
            return new byte[0];
        int len = 8 * (n - 1);
        for (long x = words[n - 1]; x != 0; x >>>= 8)
            len++;
        byte[] bytes = new byte[len];
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < n - 1; i++)
            bb.putLong(words[i]);
        for (long x = words[n - 1]; x != 0; x >>>= 8)
            bb.put((byte) (x & 0xff));
        return bytes;
    }

    /**
     * 实现克隆接口
     */
    public MultiBitSet clone()
    {
        MultiBitSet o = null;
        try
        {
            o = (MultiBitSet) super.clone();
            o.words = (long[]) words.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return o;
    }

    public static void main(String[] args)
    {
        MultiBitSet multiBitSet = new MultiBitSet(32, 2);
        multiBitSet.set(0, 0, true);
        multiBitSet.set(0, 4, 11,123);
        multiBitSet.set(0, 4, 11,1);
        
        multiBitSet.set(0, 4, 11,12);

        System.out.println(multiBitSet.get(0, 0));
        System.out.println(multiBitSet.get(0, 4, 11));

        byte[] data = multiBitSet.toByteArray();

        MultiBitSet n = new MultiBitSet(data, 32);
        
        n.set(0, 4, 11,0);

        System.out.println(n.get(0, 0));
        System.out.println(n.get(0, 4, 11));


    }
}
