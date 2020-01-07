package com.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Text;

import com.server.data.DBSource;
import com.server.data.FieldInfo;


public class CompareFactory
{
    public static class CompareData
    {
        public String sql;
        public String desc;
    }
    
    public static class DetailData
    {
        public String dbName;
        public HashMap<String, Map<String, FieldInfo>> dbTables=new HashMap<>();
    }

    private static DetailData compareDB=new DetailData();
    
    private static DetailData updateDB=new DetailData();
    
    public static void init(String selectCompareDB, String selectUpdateDB,Text text)
    {
        DBSource updateDataSource = ServerWindow.updateDataSource;
        DBSource compareDataSource = ServerWindow.compareDataSource;
        
        List<String> compareList = compareDataSource.getTables(selectCompareDB);
        List<String> updateList = updateDataSource.getTables(selectUpdateDB);
        
        compareDB.dbTables.clear();
        for(String str: compareList)
        {
            compareDB.dbName=selectCompareDB;
            Map<String, FieldInfo> map=compareDataSource.getTableFieldList(selectCompareDB, str);
            compareDB.dbTables.put(str, map);
            text.append("load table:"+str+"\n");
        }
        
        updateDB.dbTables.clear();
        for(String str:updateList)
        {
            updateDB.dbName=selectUpdateDB;
            Map<String, FieldInfo> map=updateDataSource.getTableFieldList(selectUpdateDB, str);
            updateDB.dbTables.put(str, map);
            text.append("load table:"+str+"\n");
        }
    }
    
    /**
     * 对比
     * @param SelectCompareDB 标准
     * @param SelectUpdateDB 修改
     * @return
     */
    public static CompareData compare(String SelectCompareDB, String SelectUpdateDB,Text text)
    {
        if(!compareDB.dbName.equalsIgnoreCase(SelectCompareDB) || !updateDB.dbName.equalsIgnoreCase(SelectUpdateDB))
        {
            init(SelectCompareDB, SelectUpdateDB,text);
        }

        CompareData data = new CompareData();

        Set<String> compareList = compareDB.dbTables.keySet();
        Set<String> updateList = updateDB.dbTables.keySet();

        StringBuilder builder = new StringBuilder();

        // 修改的SQL
        List<String> sqlList = new ArrayList<>();

        // 添加表 - 查找更新库，是否存在对比库的表
        for (String s : compareList)
        {
            boolean flag = true;
            for (String t : updateList)
            {
                if (t.equalsIgnoreCase(s))
                {
                    flag = false;
                    break;
                }
            }

            if (flag)
            {
                String sql = createTable(s);
                builder.append("Add Table:").append(s).append("\n");
                sqlList.add(sql);
            }
        }

        // 删除表
        for (String s : updateList)
        {
            boolean flag = true;
            for (String t : compareList)
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
        for (String s : updateList)
        {
            for (String t : compareList)
            {
                if (t.equalsIgnoreCase(s))
                {
                    String str = modifyTable(s, builder);
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

    private static String modifyTable(String tableName, StringBuilder desc)
    {
        StringBuilder builder = new StringBuilder();

        Map<String, FieldInfo> fightMap = compareDB.dbTables.get(tableName);
        Map<String, FieldInfo> testMap = updateDB.dbTables.get(tableName);

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

    /**
     * 创建库逻辑
     * @param tableName
     * @return
     */
    private static String createTable(String tableName)
    {
        Map<String, FieldInfo> fields = compareDB.dbTables.get(tableName);
        
        StringBuilder builder = new StringBuilder();
        //builder.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n");

        
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
        else if(info.sqlType.equalsIgnoreCase("date"))
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

    public static String deleteDB()
    {
        StringBuilder builder=new StringBuilder(); 
        builder.append("******** clear ").append(compareDB.dbName).append(" ***********\n");
        
        Set<String> compareList = compareDB.dbTables.keySet();
        for (String s : compareList)
        {
            builder.append("DELETE FROM `").append(s).append("`;\n");
        }
        
        return builder.toString();
    }
}
