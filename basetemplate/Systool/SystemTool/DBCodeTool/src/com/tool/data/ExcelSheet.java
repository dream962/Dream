package com.tool.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tool.code.FieldInfo;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;

public class ExcelSheet
{
    public static final class OneRowData
    {
        /** 一行的数据<位置，数值> */
        public Map<Integer, String> data = new HashMap<>();
    }

    static final int START_ROW = 4; // 从第4行开始

    /** 标志位 */
    private static final String FLAG = "[D]";

    public String tableName;

    public String sheetName;

    public List<String> primaryKeys;

    public int primaryKeyCount;

    public String insertStr;

    public String createStr;

    public String deleteStr;

    /** 表字段信息 */
    public Map<String, FieldInfo> map;

    /** 所有行的数据 */
    public List<OneRowData> dataList;

    public ExcelSheet()
    {
        map = new LinkedHashMap<>();
        primaryKeys = new ArrayList<>();
        dataList = new ArrayList<>();
    }

    public boolean init(Sheet sheet)
    {
        try
        {
            if (sheet.getRow(0).length < 4)
                return false;

            tableName = sheet.getRow(0)[3].getContents();
            sheetName = sheet.getName();
            String countTemp = sheet.getRow(0)[1].getContents();
            primaryKeyCount = countTemp != null && !countTemp.trim().isEmpty() ? Integer.valueOf(countTemp) : 1;
            primaryKeyCount = primaryKeyCount < 1 ? 1 : primaryKeyCount;

            // 首行E填写主键key信息，或者B填写主键个数，默认前几列为主键信息
            if (sheet.getRow(0).length >= 5)
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
            createStr(sheet);
            insertStr(sheet);
            deleteStr(sheet);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Init Excel Sheet Exception: tableName:" + tableName + " sheetName:" + sheetName);
            e.printStackTrace();
        }

        return false;
    }

    private void initData(Sheet sheet)
    {
        if (sheet.getRows() >= START_ROW)
        {
            for (int i = START_ROW; i < sheet.getRows(); i++)
            {
                Cell[] execlData = sheet.getRow(i);
                OneRowData data = new OneRowData();
                if (execlData != null)
                {
                    for (FieldInfo info : map.values())
                    {
                        if (info.cellIndex < execlData.length)
                        {
                            Cell cell = execlData[info.cellIndex];
                            data.data.put(info.cellIndex, cell.getContents());
                        }
                        else
                        {
                            data.data.put(info.cellIndex, "");
                        }
                    }
                }
                dataList.add(data);
            }
        }

        // System.err.println(String.format("Import Table:%s -- Count:%d", tableName, dataList.size()));
    }

    private void initFieldInfo(Sheet sheet)
    {
        Cell[] contexts = sheet.getRow(1);
        Cell[] types = sheet.getRow(2);
        Cell[] columns = sheet.getRow(3);

        List<Integer> cellIndex = new ArrayList<>();

        // 排除掉无效的列
        for (int i = 0; i < contexts.length; i++)
        {
            if (contexts[i].getType() == CellType.EMPTY)
                continue;

            if (contexts[i].getContents().indexOf(FLAG) >= 0)
                continue;

            if (columns[i].getContents().indexOf(FLAG) >= 0)
                continue;

            if (columns[i].getType() == CellType.EMPTY)
                continue;

            cellIndex.add(i);
        }

        for (int i = 0; i < cellIndex.size(); i++)
        {
            FieldInfo info = new FieldInfo();
            info.cellIndex = cellIndex.get(i);
            info.name = columns[cellIndex.get(i)].getContents();
            info.comment = contexts[cellIndex.get(i)].getContents();
            setType(types[cellIndex.get(i)].getContents(), info);
            if (primaryKeys.contains(info.name))
            {
                info.isPrimaryKey = true;
            }

            if (primaryKeyCount > 0)
            {
                if (i < primaryKeyCount)
                {
                    info.isPrimaryKey = true;
                    info.primaryKeyIndex = i + 1;
                }
            }

            map.put(info.name, info);
        }
    }

    private void setType(String type, FieldInfo info)
    {
        if (type.toLowerCase().indexOf("string") >= 0)
        {
            String[] strings = type.split("\\|");
            info.javaType = "String";
            info.sqlType = "varchar";
            info.len = Integer.valueOf(strings[1].trim());
        }
        else if (type.toLowerCase().indexOf("int") >= 0)
        {
            info.javaType = "int";
            info.sqlType = type;
            info.len = 0;
        }
        else if (type.toLowerCase().indexOf("boolean") >= 0)
        {
            info.javaType = "boolean";
            info.sqlType = "boolean";
            info.len = 0;
        }
        else if (type.toLowerCase().indexOf("float") >= 0)
        {
            info.javaType = "float";
            info.sqlType = type;
            info.len = 0;
        }
        else
        {
            info.javaType = type;
            info.sqlType = type;
            info.len = 0;
        }
    }

    private void createStr(Sheet sheet)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("drop table if exists ").append(tableName).append(";").append("\n");
        builder.append("create table ").append(tableName).append("\n");
        builder.append("(").append("\n");

        String primaryKey = "";
        for (FieldInfo info : map.values())
        {
            builder.append("    ").append(info.name).append("    ");

            if (info.sqlType.trim().equalsIgnoreCase("varchar"))
                builder.append("varchar(").append(info.len).append(")  ");
            else if (info.sqlType.trim().equalsIgnoreCase("boolean"))
                builder.append("tinyint(1)  ");
            else
                builder.append(info.sqlType).append("  ");

            if (info.isPrimaryKey)
            {
                builder.append("not null  ");
                primaryKey += info.name + ",";
            }

            builder.append("comment    '").append(info.comment).append("',").append("\n");
        }

        if (!primaryKey.isEmpty())
            primaryKey = primaryKey.substring(0, primaryKey.length() - 1);

        builder.append("primary key (").append(primaryKey).append(")").append("\n");

        builder.append(");").append("\n");
        builder.append("alter table ").append(tableName).append(" comment '").append(sheet.getName()).append(
                "';").append("\n");
        createStr = builder.toString();
    }

    private void insertStr(Sheet sheet)
    {
        String keys = "";
        String values = "";

        for (FieldInfo info : map.values())
        {
            values += "?,";
            keys += "`" + info.name + "`,";
        }
        keys = keys.substring(0, keys.length() - 1);
        values = values.substring(0, values.length() - 1);
        insertStr = "INSERT INTO `" + tableName + "`(" + keys + ") VALUES(" + values + ")";
    }

    private void deleteStr(Sheet sheet)
    {
        deleteStr = "Delete From " + tableName;
    }

    public String getTableName()
    {
        return this.tableName;
    }

    public Map<String, FieldInfo> getFieldMap()
    {
        Map<String, FieldInfo> map = new LinkedHashMap<>();
        map.putAll(this.map);
        return map;
    }

    public List<OneRowData> getData()
    {
        return dataList;
    }
}
