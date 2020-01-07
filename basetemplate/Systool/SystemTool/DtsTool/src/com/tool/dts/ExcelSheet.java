package com.tool.dts;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelSheet
{
    private int index;
    
    /**
     * execl 工作区
     */
    private Sheet sheet = null;

    /**
     * 表名
     */
    private String tableName = "";

    private boolean deleteTable = false;

    /**
     * Insert语句
     */
    private String insertStr = "";

    private String createStr = "";

    /**
     * 有效字段
     */
    private StringBuilder fieldRow = new StringBuilder();

    public int getIndex()
    {
        return index;
    }

    public boolean isTableDelete()
    {
        return deleteTable;
    }

    public ExcelSheet(Workbook book, int index)
    {
        try
        {
            this.index = index;
            sheet = book.getSheet(index);

            if (index == 0)
            {
                return;
            }

            if (sheet != null)
            {
                if (sheet.getRow(0).length < 4)
                    return;

                tableName = sheet.getRow(0)[3].getContents();
                if (tableName.isEmpty())
                    return;

                deleteTable = true;

                insertStr = insertStr(tableName);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getTableName()
    {
        return this.tableName;
    }

    /**
     * 获取Insert语句
     * 
     * @return
     */
    public String getInsertStr()
    {
        return this.insertStr;
    }

    public String getCreateStr()
    {
        return this.createStr;
    }

    public Cell[] getSheet(int row)
    {
        Cell[] cell = sheet.getRow(row);
        try
        {
            if (cell.length <= 0 || cell[0].getContents() == "")
            {
                return null;
            }
        }
        catch (NumberFormatException e)
        {
            System.err.print(e.getMessage());
            return null;
        }
        return cell;
    }

    public int getRows()
    {
        return sheet.getRows();
    }

    public int[] getFieldRow()
    {
        String[] fieldArry = this.fieldRow.toString().split(",");
        int[] fieldData = new int[fieldArry.length];
        for (int i = 0; i < fieldArry.length; i++)
        {
            if (fieldArry[i].trim().isEmpty())
                continue;
            fieldData[i] = Integer.parseInt(fieldArry[i]);
        }
        return fieldData;
    }

    private Cell[] types = null;

    public String insertStr(String tableName)
    {
        if (types == null)
        {
            types = sheet.getRow(2);
        }

        Cell[] cell = sheet.getRow(3);
        String keys = "";
        String values = "";

        for (int i = 0; i < cell.length; i++)
        {
            if (cell[i].getContents().startsWith(DBConfig.DeleteFlag))
                continue;

            if (!cell[i].getContents().equals(""))
            {
                values += "?,";
                keys += "`" + cell[i].getContents() + "`,";
            }
            else
            {
                String content = "";
                if (types[i].equals("int") || types[i].equals("float") || types[i].equals("date"))
                {
                    content = "0";
                }

                values += "?,";
                keys += "`" + content + "`,";
            }

            fieldRow.append(i + ",");
        }
        keys = keys.substring(0, keys.length() - 1);
        values = values.substring(0, values.length() - 1);
        return "INSERT INTO `" + tableName + "`(" + keys + ") VALUES(" + values
                + ")";
    }

    public String delStr(String tableName)
    {
        return "Delete From " + tableName;
    }
}
