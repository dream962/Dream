package com.tool.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;

/**
 * excel sheet的数据
 * @author dream
 *
 */
public class ExcelSheet
{
    static final int START_ROW = 4; // 从第4行开始
    
    /** 标志位*/
    private static final String FLAG="[D]";

    /** 表名称*/
    public String tableName;

    /** 主键*/
    public List<String> primaryKeys;

    /** 表字段信息 <字段名称，字段信息>*/
    public Map<String, FieldInfo> map;

    /** 所有行的数据<字段名称，字段数据> */
    public List<Map<String, String>> dataList;
    
    private static String BeanPath="com.data.bean.%s";

    public ExcelSheet()
    {
        map = new HashMap<>();
        primaryKeys = new ArrayList<>();
        dataList = new ArrayList<>();
    }

    public boolean init(Sheet sheet)
    {
        if (sheet.getRow(0).length < 4)
            return false;

        tableName = createBeanName(sheet.getRow(0)[3].getContents());
        
        if (sheet.getRow(0).length > 5)
        {
            String str = sheet.getRow(0)[4].getContents();
            if (str != null && !str.isEmpty())
            {
                String[] temp = str.split("\\|");
                for (int i = 0; i < temp.length; i++)
                {
                    primaryKeys.add(temp[i]);
                }
            }
        }

        if (tableName == null || tableName.isEmpty())
            return false;

        initFieldInfo(sheet);
        initData(sheet);

        return true;
    }

    private void initData(Sheet sheet)
    {
        if (sheet.getRows() >= START_ROW)
        {
            for (int i = START_ROW; i < sheet.getRows(); i++)
            {
                Cell[] execlData = sheet.getRow(i);
                Map<String, String> data = new HashMap<>();
                if (execlData != null)
                {
                    for (FieldInfo info : map.values())
                    {
                        if (info.cellIndex < execlData.length)
                        {
                            Cell cell = execlData[info.cellIndex];
                            if(cell.getContents()==null)
                                data.put(info.name, "");
                            else
                                data.put(info.name, cell.getContents());
                        }
                        else
                        {
                            data.put(info.name, "");
                        }
                    }
                }
                dataList.add(data);
            }
        }
    }

    private void initFieldInfo(Sheet sheet)
    {
        Cell[] contexts = sheet.getRow(1);
        Cell[] types = sheet.getRow(2);
        Cell[] columns = sheet.getRow(3);
        
        List<Integer> cellIndex=new ArrayList<>();
        
        // 排除掉无效的列
        for(int i=0;i<contexts.length;i++)
        {
            if(contexts[i].getType()==CellType.EMPTY || contexts[i].getContents().equalsIgnoreCase(FLAG))
                continue;
            
            cellIndex.add(i);
        }

        for (Integer i:cellIndex)
        {
            FieldInfo info = new FieldInfo();
            info.cellIndex = i;
            info.name = columns[i].getContents();
            setType(types[i].getContents(), info);
            if (primaryKeys.contains(info.name))
            {
                info.isPrimaryKey = true;
            }
            map.put(info.name, info);
        }
    }

    private void setType(String type, FieldInfo info)
    {
        if (type.indexOf("String") >= 0)
        {
            String[] strings = type.split("\\|");
            info.javaType = strings[0];
        }
        else
        {
            info.javaType = type;
        }
    }

    public String getTableName()
    {
        return this.tableName;
    }

    public FieldInfo getFieldInfo(String name)
    {
        return map.get(name);
    }

    public static String createBeanName(String tableName)
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
        return String.format(BeanPath, _tableName+"Bean");
    }

    public static String toUpperName(String entityName)
    {
        return entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
    }
}
