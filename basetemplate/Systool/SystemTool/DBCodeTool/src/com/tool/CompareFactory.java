package com.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tool.code.FieldInfo;
import com.tool.data.DBSource;

public class CompareFactory
{
    public static class CompareData
    {
        public String sql;
        public String desc;
    }

    public static CompareData compare(String fightDB, String testDB)
    {
        CompareData data = new CompareData();
        DBSource source = MainWindow.dataSource.dBSource;

        List<String> fightList = source.getTables(fightDB);
        List<String> testList = source.getTables(testDB);

        StringBuilder builder = new StringBuilder();

        // 修改的SQL
        List<String> sqlList = new ArrayList<>();

        // 添加表
        for (String s : fightList)
        {
            boolean flag = true;
            for (String t : testList)
            {
                if (t.equalsIgnoreCase(s))
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                String sql = createTable(fightDB, s);
                builder.append("Add Table:").append(s).append("\n");
                sqlList.add(sql);
            }
        }

        // 删除表
        for (String s : testList)
        {
            boolean flag = true;
            for (String t : fightList)
            {
                if (t.equalsIgnoreCase(s))
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                String sql = dropTable(s);
                builder.append("Drop Table:").append(s).append("\n");
                sqlList.add(sql);
            }
        }

        // 修改表
        for (String s : testList)
        {
            for (String t : fightList)
            {
                if (t.equalsIgnoreCase(s))
                {
                    String str = modifyTable(fightDB, testDB, s, builder);
                    sqlList.add(str);
                }
            }
        }

        String sql = "";
        for (String s : sqlList)
            sql += s;

        data.sql = sql;
        data.desc = builder.toString();

        return data;

    }

    private static String modifyTable(String fightDB, String testDB, String tableName, StringBuilder desc)
    {
        StringBuilder builder = new StringBuilder();

        DBSource source = MainWindow.dataSource.dBSource;

        Map<String, FieldInfo> fightMap = source.getTableFieldList(fightDB, tableName);
        Map<String, FieldInfo> testMap = source.getTableFieldList(testDB, tableName);

        // 添加
        for (String s : fightMap.keySet())
        {
            boolean flag = true;
            for (String t : testMap.keySet())
            {
                if (s.equalsIgnoreCase(t))
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                FieldInfo info = fightMap.get(s);
                String fieldSql = wrapField(info);
                builder.append("ALTER TABLE `").append(tableName).append("` ADD COLUMN ").append(fieldSql).append(
                        ";\n");
                desc.append("Modify Table:").append(tableName).append(" - Add Column : ").append(info.name).append(
                        ";\n");
            }
        }

        // 删除
        for (String t : testMap.keySet())
        {
            boolean flag = true;
            for (String s : fightMap.keySet())
            {
                if (s.equalsIgnoreCase(t))
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                // ALTER TABLE `test`.`t_u_test1` DROP COLUMN `TestCol1`;
                builder.append("ALTER TABLE `").append(tableName).append("` DROP COLUMN `").append(t).append(
                        "`;").append("\n");
                desc.append("Modify Table:").append(tableName).append(" - Drop Column : ").append(t).append(";\n");
            }
        }

        // 修改
        for (String t : testMap.keySet())
        {
            for (String s : fightMap.keySet())
            {
                if (s.equalsIgnoreCase(t))
                {
                    // ALTER TABLE `test`.`t_u_test1` CHANGE `TestCol` `TestCol` FLOAT(11) UNSIGNED NOT NULL COMMENT
                    // '测试添加';
                    FieldInfo info = fightMap.get(s);
                    FieldInfo test = testMap.get(t);
                    if (!info.equal(test))
                    {
                        String fieldSql = wrapField(info);
                        String testSql = wrapField(test);
                        builder.append("ALTER TABLE `").append(tableName).append("` CHANGE `").append(t).append(
                                "` ").append(fieldSql).append(";").append("\n");

                        desc.append("Modify Table:").append(tableName).append(" - Change Column : ").append(
                                testSql).append("  -->  ").append(
                                        fieldSql).append(";\n");
                    }
                }
            }
        }

        // 主键
        List<FieldInfo> keys = new ArrayList<>();
        for (FieldInfo info : fightMap.values())
        {
            if (info.isPrimaryKey)
            {
                keys.add(info);
            }
        }

        List<FieldInfo> testKeys = new ArrayList<>();
        for (FieldInfo info : testMap.values())
        {
            if (info.isPrimaryKey)
            {
                testKeys.add(info);
            }
        }

        if (keys.size() > 0)
        {
            boolean flag = false;

            for (FieldInfo f : keys)
            {
                boolean temp = true;
                for (FieldInfo t : testKeys)
                {
                    if (f.name.equalsIgnoreCase(t.name))
                    {
                        temp = false;
                        System.err.println();
                        break;
                    }
                }

                if (temp)
                    flag = true;
            }

            if (flag)
            {
                // ALTER TABLE `t_u_test1` DROP PRIMARY KEY,ADD PRIMARY KEY(`TestCol`);
                String string = "";
                for (FieldInfo s : keys)
                {
                    string += "`" + s.name + "`,";
                }

                string = string.substring(0, string.length() - 1);
                
                builder.append("ALTER TABLE `").append(tableName).append("` ");
                if(testKeys.size()>0)
                {
                    builder.append("DROP PRIMARY KEY,");
                }
                
                builder.append("ADD PRIMARY KEY(").append(string).append(");\n");

                desc.append("Modify Table:").append(tableName).append(" - Change Primary Key : ").append(
                        string).append(
                                ";\n");
            }

        }
        else
        {
            if (testKeys.size() > 0)
            {
                // ALTER TABLE `t_u_test1` DROP PRIMARY KEY
                builder.append("ALTER TABLE `").append(tableName).append("` DROP PRIMARY KEY;").append("\n");
                desc.append("Modify Table:").append(tableName).append(" - Drop Primary Key : ").append(";\n");

            }
        }

        // 自动递增属性
        for (String t : testMap.keySet())
        {
            for (String s : fightMap.keySet())
            {
                if (s.equalsIgnoreCase(t))
                {
                    // ALTER TABLE `test`.`t_u_test1` CHANGE `TestCol` `TestCol` FLOAT(11) UNSIGNED NOT NULL COMMENT
                    // '测试添加';
                    FieldInfo info = fightMap.get(s);
                    FieldInfo test = testMap.get(t);
                    if (info.isAotuIncreamte != test.isAotuIncreamte)
                    {
                        String fieldSql = wrapAutoField(info);
                        String testSql = wrapAutoField(test);
                        builder.append("ALTER TABLE `").append(tableName).append("` CHANGE `").append(t).append(
                                "` ").append(fieldSql).append(";").append("\n");

                        desc.append("Modify Table:").append(tableName).append(" - Change Column : ").append(
                                testSql).append("  -->  ").append(
                                        fieldSql).append(";\n");
                    }
                }
            }
        }

        return builder.toString();
    }

    private static String dropTable(String tableName)
    {
        String sql = "DROP TABLE %s;\n";

        return String.format(sql, tableName);
    }

    private static String createTable(String dbName, String tableName)
    {
        DBSource source = MainWindow.dataSource.dBSource;

        Map<String, FieldInfo> fields = source.getTableFieldList(dbName, tableName);

        StringBuilder builder = new StringBuilder();

        builder.append("create table ").append(tableName).append("\n");
        builder.append("(").append("\n");

        String keyStr = "primary key (%s)";
        List<FieldInfo> keys = new ArrayList<>();

        for (FieldInfo info : fields.values())
        {
            String s = wrapField(info);
            builder.append(s).append(",\n");

            if (info.isPrimaryKey)
                keys.add(info);
        }

        if (keys.size() > 0)
        {
            String string = "";
            for (FieldInfo s : keys)
            {
                string += s.name + ",";
            }

            string = string.substring(0, string.length() - 1);
            string = String.format(keyStr, string);

            builder.append(string).append("\n");
        }
        else
        {
            builder.delete(builder.length() - 2, builder.length());
            builder.append("\n");
        }

        builder.append(");").append("\n");

        return builder.toString();
    }

    /**
     * 封装字段信息
     * 
     * @param info
     * @return
     */
    private static String wrapField(FieldInfo info)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("`").append(info.name).append("`\t");
        if (info.sqlType.equalsIgnoreCase("datetime"))
        {
            builder.append(info.sqlType).append("\t");
        }
        else
        {
            builder.append(info.sqlType).append("(").append(info.len).append(")").append("\t");
        }

        if (!info.isNull)
        {
            builder.append("NOT NULL").append("\t");
        }

        builder.append("comment ").append("'").append(info.comment).append("'");

        return builder.toString();
    }

    private static String wrapAutoField(FieldInfo info)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("`").append(info.name).append("`\t");
        if (info.sqlType.equalsIgnoreCase("datetime"))
        {
            builder.append(info.sqlType).append("\t");
        }
        else
        {
            builder.append(info.sqlType).append("(").append(info.len).append(")").append("\t");
        }

        if (!info.isNull)
        {
            builder.append("NOT NULL").append("\t");
        }

        if (info.isAotuIncreamte)
        {
            builder.append("auto_increment").append("\t");
        }

        builder.append("comment ").append("'").append(info.comment).append("'");

        return builder.toString();
    }

    public static int execute(String dbName, String sql)
    {
        Connection connection = null;
        PreparedStatement pstm = null;
        try
        {
            connection = DBSource.getConn(dbName);
            pstm = connection.prepareStatement(sql);
            int result = pstm.executeUpdate();
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.err.println(sql);
            DBSource.close(null, pstm, connection);
        }

        return -1;
    }

}
