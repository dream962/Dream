package com.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

/**
 * 图片工具类
 * 
 * @author dream.wang
 * 
 */
public class ImageUtil
{

    public static void createImgFromData(String outPath, String dataPath)
    {
        try
        {
            File file = new File(dataPath);
            ByteBuffer byteBuff;
            int length = (int) file.length();
            FileInputStream fInputStream = new FileInputStream(file);
            byte[] bytebuf = new byte[length];
            fInputStream.read(bytebuf);
            byteBuff = ByteBuffer.allocate(length);
            byteBuff.put(bytebuf);
            byteBuff.order(ByteOrder.LITTLE_ENDIAN);
            byteBuff.flip();
            // 前4个字节是地图的长宽
            int width = byteBuff.getShort();
            int height = byteBuff.getShort();
            int dataLen = byteBuff.getInt();
            byte[] data = new byte[dataLen];
            byteBuff.get(data, 0, dataLen);

            createImgFromByte(outPath, data, width, height);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 创建图片
     * 
     * @param outPath
     * @param data
     * @param width
     * @param height
     */
    public static void createImgFromByte(String outPath, byte[] data, int width, int height)
    {
        try
        {
            File imageFile = new File(outPath);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, width, height);

            for (int a = 0; a < data.length; a++)
            {
                int pY = (height - 1) - (a * 8) / width;// 取得像素点在图片的y位置
                int pX = (a * 8) % width;// 取得像素点在图片的x位置
                for (int b = 0; b < 8; b++)
                {
                    byte flag = (byte) (0x01 << (7 - b));
                    byte result = (byte) (data[a] & flag);
                    if (result == 1)
                        bi.setRGB(pX, pY, 0xFF0000);
                    else
                        bi.setRGB(pX, pY, 0xFFFFFF);
                }
            }

            ImageIO.write(bi, "jpg", imageFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void createImgFromDataOld(String outPath, String dataPath)
    {
        try
        {
            File file = new File(dataPath);
            ByteBuffer byteBuff;
            int length = (int) file.length();
            FileInputStream fInputStream = new FileInputStream(file);
            byte[] bytebuf = new byte[length];
            fInputStream.read(bytebuf);
            byteBuff = ByteBuffer.allocate(length);
            byteBuff.put(bytebuf);
            byteBuff.order(ByteOrder.LITTLE_ENDIAN);
            byteBuff.flip();
            // 前4个字节是地图的长宽
            int width = byteBuff.getInt();
            int height = byteBuff.getInt();
            byte[] data = new byte[length - byteBuff.position()];
            byteBuff.get(data, 0, length - 8);

            File imageFile = new File(outPath);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, width, height);

            for (int a = 0; a < data.length; a++)
            {
                int pY = height - 1 - (a * 8) / width;// 取得像素点在图片的y位置
                int pX = (a * 8) % width;// 取得像素点在图片的x位置
                for (int b = 0; b < 8; b++)
                {
                    byte flag = (byte) (0x01 << (7 - b));
                    byte result = (byte) (data[a] & flag);
                    if (result == 1)
                        bi.setRGB(pX, pY, 0xFF0000);
                    else
                        bi.setRGB(pX, pY, 0xFFFFFF);
                }
            }

            ImageIO.write(bi, "jpg", imageFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        // String outPath = "C:\\Users\\dream.wang\\Desktop\\aa.jpg";
        // String filePath = "F:\\MobileDDT\\trunk\\Server\\FightServer\\scripts\\map\\1075.map";
        //
        // ImageUtil.createImgFromData(outPath, filePath);

//        String outPath = "C:\\Users\\dream\\Desktop\\2.PNG";
//
//        ByteBuffer buffer=ImageUtil.readImage(outPath);
//        byte[] data = buffer.array();
//
//        ImageUtil.createImgFromByteNoBorder("C:\\Users\\dream\\Desktop\\aa.PNG", data, 1701, 1701);
//
//        System.err.println("finish.");
        
        
    }

    public static ByteBuffer readImage(String path)
    {
        File file = new File(path);
        if (!file.exists())
            return null;

        try
        {
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
    
            int dataLen=width * height / 8 + 1;
            ByteBuffer buffer=ByteBuffer.allocate(dataLen+8);
            buffer.putInt(width);
            buffer.putInt(height);
            
            byte[] data = new byte[dataLen];
            int total = 0;
            byte temp = 0;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int pos = total / 8;
                    int offset = total % 8;
                    if (offset == 0)
                        temp = 0;

                    int a = image.getColorModel().getAlpha(image.getRaster().getDataElements(x, y, null));
                    if (a != 0)
                    {
                        temp |= 0x01 << offset;
                        int rgb=image.getRGB(x, y);
//                        Color color=new Color(rgb);
//                        System.err.println(x+","+y+"-rgb:"+color.getRed()+","+color.getGreen()+","+color.getBlue()+" - temp:"+temp+","+(0x01<<offset));
                    }

                    data[pos] = temp;
                    total++;
                }
            }
            
            buffer.put(data);
            return buffer;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 创建图片
     * 
     * @param outPath
     * @param data
     * @param width
     * @param height
     */
    public static void createImgFromByteNoBorder(String outPath, byte[] data, int width, int height)
    {
        try
        {
            File imageFile = new File(outPath);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setBackground(Color.white);
            int total = 0;

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int pos = total / 8;
                    int offset = total % 8;
                    byte temp = data[pos];

                    int flag = temp & (0x01 << offset);

                    if (flag >= 1)
                    {
                        bi.setRGB(x, y, 0);
                    }
                    else
                    {
                        bi.setRGB(x, y, 0xffffff);
                    }

                    total++;
                }
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.dispose();

            ImageIO.write(bi, "jpg", imageFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
