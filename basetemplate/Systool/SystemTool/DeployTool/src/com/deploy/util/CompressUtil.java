package com.deploy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author Dream
 * @date 2011-5-12
 * @version
 * 
 */
public class CompressUtil
{
    /**
     * 压缩
     * 
     * @param str
     * @return
     */
    public static byte[] compress(String str)
    {
        byte[] input;
        try
        {
            byte[] output = null;
            input = str.getBytes("UTF-8");

            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
            byte[] buf = new byte[1024 * 10];
            while (!compresser.finished())
            {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();

            try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            compresser.end();
            return output;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] compress(byte[] src, int start, int len)
    {
        byte[] input = new byte[len];
        System.arraycopy(src, start, input, 0, len);

        byte[] output = null;

        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[1024 * 10];
        while (!compresser.finished())
        {
            int i = compresser.deflate(buf);
            bos.write(buf, 0, i);
        }
        output = bos.toByteArray();

        try
        {
            bos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        compresser.end();
        return output;
    }

    /**
     * 压缩文件
     * 
     * @param path
     *  
     * @param str
     *  
     * @param fileName
     *  
     * @param isCompress
     *  
     * @param message
     * 
     * @return
     */
    public static String createCompressXml(String path, String str,
            String fileName, boolean isCompress, String message)
    {
        String filePath = path + fileName + ".xml";
        try
        {
            File myFilePath = new File(filePath);

            myFilePath.delete();
            myFilePath.createNewFile();

            FileOutputStream writer = new FileOutputStream(myFilePath);
            writer.write(isCompress ? compress(str) : str.getBytes());
            writer.close();

            return "Build:" + filePath + "------ " + message;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Build:" + filePath + "------ Fail!";
        }
    }

    /**
     * 压缩xml
     * 
     * @param path
     *            
     * @param str
     *            
     * @param fileName
     *            
     * @param isCompress
     *            
     * @param message
     *            
     * @return
     */
    public static void setCompressXml(String path, String str, String fileName,
            boolean isCompress, String message)
    {
        String filePath = path + fileName;
        try
        {
            // FileWriter writer = new FileWriter(filePath, true);
            // writer.write(str);
            // writer.close();
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            long length = file.length();
            int index = 0;
            if (length > 0)
                index = 38;
            file.seek(length);
            file.writeBytes(str.substring(index, str.length()));
            file.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩
     * 
     * @param compressed
     * @return
     */
    public static String decompress(byte[] compressed)
    {
        try
        {
            Inflater decompresser = new Inflater();
            decompresser.setInput(compressed, 0, compressed.length);
            byte[] result = new byte[1024];
            int resultLength;
            resultLength = decompresser.inflate(result);
            decompresser.end();
            return new String(result, 0, resultLength, "UTF-8");
        }
        catch (DataFormatException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] t)
    {

        // String ss = decompress(compress("<xml>test</xml>"));
        // System.out.println(ss);

        // Document result = DocumentHelper.createDocument();
        // Element element = result.addElement("Result");
        // element.addAttribute("test", "dsssd");
        // element.addAttribute("ids", "12df3");
        // createCompressXml("D:\\", result, "test", false, "success");

        FileInputStream fis;
        try
        {
            fis = new FileInputStream(
                    "E:\\LinuxDDT\\Server\\src\\Workspace\\Request\\WebContent\\DropItemForNewRegister.xml");
            byte[] b1 = new byte[fis.available()];
            fis.read(b1);
            String dString = decompress(b1);
            System.out.println(dString);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
