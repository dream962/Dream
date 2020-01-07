/**
 * 
 */
package com.util.press.proto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.GeneratedMessage;
import com.util.TimeUtil;

/**
 * @date Mar 28, 2017 4:43:21 PM
 * @author dansen
 * @desc
 */
public class ProtoUtil
{
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;
    private static FileOutputStream stream;
    
    private static void init()
    {
        if(file == null){
            try
            {
                file = new File("../PressPowerGUI/press-save/press.log");
                stream = new FileOutputStream(file, true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static String parse(String raw, GeneratedMessage message)
    {
        Map<String, Object> object = ProtoParser.parse(raw, message);
        return gson.toJson(object);
    }
    
    public static void log(String head, GeneratedMessage message)
    {
        try
        {
            init();
            String rawData = message.toString();
            String data = parse(rawData, message);
            String line = TimeUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss")+ " "+head+"\n";
            stream.write(line.getBytes());
            stream.write(data.getBytes());
            stream.write("\n".getBytes());
            stream.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void save(String filename, GeneratedMessage message)
    {
        try
        {
            if (!new File("../PressPowerGUI/press-save").exists())
            {
                return;
            }
            
            File file = new File("press-save/" + filename + ".json");
            String rawData = message.toString();
            String data = parse(rawData, message);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
        }
        catch (Exception e)
        {

        }
    }
}
