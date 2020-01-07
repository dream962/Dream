package com.util.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.util.serialize.ClassSizeUtil;

/**
 * 序列化工具
 * @author dansen
 * 2015年8月31日 下午2:44:23
 */

public class SerializeUtil
{
    public static byte[] serialize(Object object)
    {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Object unserialize(byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }
        ByteArrayInputStream bais = null;
        try
        {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args)
    {
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<3;i++)
            list.add(i);
        
        ClassSizeUtil util=ClassSizeUtil.forJRockitVM();
        int size=util.sizeof(list);
        
        byte[] test = serialize(list);
        System.err.println("List Memory:"+size+",Serialize:"+test.length);
        
        int[] array=new int[3];
        for(int i=0;i<3;i++)
            array[i]=i;

        size=util.sizeof(array);
        
        test = serialize(array);
        System.err.println("Array Memory:"+size+",Serialize:"+test.length);
    }
}
