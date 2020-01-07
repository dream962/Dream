package com.tool.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tool.data.ExcelSheet.OneRowData;

public class LuaUtil
{
    public static String toUpperName(String entityName)
    {
        return entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
    }

    /**
     * lua 文件名称
     * 
     * @param name
     * @return
     */
    public static String createLuaName(String tableName)
    {
        String[] sub = tableName.split("_");
        String _tableName = "";
        if (sub.length < 2)
        {
            return tableName;
        }
        for (int i = 2; i < sub.length; i++)
        {
            _tableName += toUpperName(sub[i]);
        }
        return _tableName+"Bean";
    }

    /**
     * lua 文件路径
     * 
     * @param fileName
     * @return
     */
    public static String createLuaFilePath(String fileName)
    {
        String rootPath = CommonUtil.LuaPath;
        File file = new File(rootPath);
        if (!file.exists())
        {
            file.mkdirs();
        }

        // lua 文件路径
        String filePath = rootPath + "//" + fileName + ".lua";

        return filePath;
    }

    /**
     * 创建lua文件
     * 
     * @param path
     * @param content
     * @return
     */
    public static boolean createLuaFile(String path, String content)
    {
        File entityFile = new File(path);
        if (!entityFile.exists())
        {
            try
            {
                entityFile.createNewFile();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(entityFile), "UTF-8");
            BufferedWriter writer = new BufferedWriter(out);
            writer.write(content);
            writer.flush();
            writer.close();

            System.err.println("Create Lua File：" + path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * lua 文件内容
     * 
     * @param fileName
     * @return
     */

    public static String createLuaContext(String fileName, String commont, Map<String, FieldInfo> fieldMap,List<OneRowData> list)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("------------------------------------------------------------------------------");
        builder.append("-- ").append("System Auto Generate,Do Not Edit It！").append("\n");
        builder.append("-- ").append(commont).append("\n");
        builder.append("------------------------------------------------------------------------------");
        builder.append("\n");

        builder.append(fileName).append(" = ").append("\n");
        builder.append("{").append("\n");

        // 列信息
        List<FieldInfo> columns = new ArrayList<>(fieldMap.values());
        columns.sort((p1, p2) -> p1.cellIndex - p2.cellIndex);

        for (int i = 0; i < columns.size(); i++)
        {
            FieldInfo field = columns.get(i);
            String temp=CommonUtil.toLowerName(field.getName())+" = "+(field.cellIndex + 1)+",";
            if(temp.length()<50)
            {
                int len=50-temp.length();
                for(int a=0;a<len;a++)
                    temp+=" ";
            }
            
            builder.append("  ").append(temp).append("  --").append(field.getComment())
            .append("  Type:").append(field.getJavaType()).append("\n");
        }

        builder.append("\n");

        // 筛选主键
        List<FieldInfo> keys = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++)
        {
            FieldInfo field = columns.get(i);
            if (field.isPrimaryKey)
                keys.add(field);
        }

        // 主键空，默认第一列
        if (keys.isEmpty())
            keys.add(columns.get(0));

        if (keys.size() == 1)
        {
            builder.append("  -- 详细数据   key:[").append(keys.get(0).getName()).append("]\n");
        }
        else
        {
            String key = "";
            for (int a = 0; a < keys.size(); a++)
                key += keys.get(a).name + "-";

            key = key.substring(0, key.length() - 1);

            builder.append("  -- 详细数据   key:[").append(key).append("]\n");
        }

        // data信息
        builder.append("  data = ").append("\n");
        builder.append("  {").append("\n");
        for (OneRowData data : list)
        {
            // 主键设置
            String key = "";
            if (keys.size() == 1)
            {
                key += data.data.get(keys.get(0).cellIndex);
                if (keys.get(0).javaType.equals("String"))
                {
                    key = "\"" + key + "\"";
                }
            }
            else
            {
                for (int a = 0; a < keys.size(); a++)
                {
                    key += data.data.get(keys.get(a).cellIndex) + "-";
                }

                key = "\"" + key.substring(0, key.length() - 1) + "\"";
            }

            // 一行数据
            builder.append("    [").append(key).append("] = {");

            StringBuilder dataStr = new StringBuilder();
            for (int i = 0; i < columns.size(); i++)
            {
                FieldInfo field = columns.get(i);
                String str = data.data.get(i);

                if (field.javaType.equals("String"))
                {
                    dataStr.append(" ").append("\"").append(str).append("\"").append(",");
                }
                else if (field.javaType.equals("boolean"))
                {
                    if(str.isEmpty() || str.trim().equals("0"))
                        dataStr.append(" ").append("false").append(",");
                    else
                        dataStr.append(" ").append("true").append(",");
                }
                else
                {
                    if (str.trim().isEmpty())
                        dataStr.append(" ").append(0).append(",");
                    else
                        dataStr.append(" ").append(str).append(",");
                }
            }

            builder.append(dataStr.subSequence(0, dataStr.length() - 1)).append(" },").append("\n");
        }

        builder.append("  }").append("\n");
        builder.append("}").append("\n");
        
        //创建读取方法
        String param="";
        String param1="";
        if (keys.size() == 1)
        {
            param=CommonUtil.toLowerName(keys.get(0).getName());
            param1=CommonUtil.toLowerName(keys.get(0).getName());
        }
        else
        {
            for (int a = 0; a < keys.size(); a++)
            {
                param += CommonUtil.toLowerName(keys.get(a).name) + ",";
                param1 += CommonUtil.toLowerName(keys.get(a).name) + "-";
            }

            param = param.substring(0, param.length() - 1);
            param1 = param1.substring(0, param1.length() - 1);
            param1 = param1.replace("-", "..\"-\"..");
        }
        builder.append("\n");
        builder.append("function ").append(fileName).append(".findData(").append(param).append(")").append("\n");
        builder.append("    local temp = ").append(fileName).append(".data[").append(param1).append("]").append("\n");
        builder.append("    if temp ~=nil then ").append("\n");
        builder.append("        return temp ").append("\n");
        builder.append("    else").append("\n");
        builder.append("        print(\"").append(fileName).append(" can't find data as key =\"..").append(param1).append(") \n");
        builder.append("    end").append("\n");
        builder.append("end").append("\n");
        
        builder.append("\n");
        builder.append("function ").append(fileName).append(".findAll()").append("\n");
        builder.append("    return ").append(fileName).append(".data").append("\n");
        builder.append("end").append("\n");

        return builder.toString();
    }

}
