package com.tool.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DaoCodeUtil
{

    /**
     * Excel表头
     * 
     * @param className
     * @return
     */
    public static String generateDaoImplCode(String name, Map<String, FieldInfo> fieldMap)
    {
        StringBuffer result = new StringBuffer();

        // Map 转化为List
        List<FieldInfo> fieldList = new ArrayList<FieldInfo>();
        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();
        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            fieldList.add(field);
        }

        // 注释行
        for (int i = 0; i < fieldList.size(); i++)
        {
            result.append(fieldList.get(i).getComment() + "\t");
        }
        result.append("\n");

        // 类型行
        for (int i = 0; i < fieldList.size(); i++)
        {
            FieldInfo info = fieldList.get(i);
            if (info.getJavaType().equals("String"))
                result.append(info.getJavaType() + "|" + info.getLen() + "\t");
            else
                result.append(info.getJavaType() + "\t");
        }
        result.append("\n");

        // 字段行
        for (int i = 0; i < fieldList.size(); i++)
        {
            result.append(fieldList.get(i).getName() + "\t");
        }
        result.append("\n");

        return result.toString();
    }

    public static String generateDaoImplCode(String entityName, String tableName, List<FieldInfo> fields,
            Map<String, Boolean> methods, String userIDType,boolean isDBOperate)
    {
        StringBuffer result = new StringBuffer();

        /* 所属包 */
        result.append("package " + CommonUtil.default_dao_implPackage).append(";\n\n");

        /* 导入默认引用 */
        // result.append("import java.sql.Connection;").append("\n");
        result.append("import com.base.database.DataReader;").append("\n");
        result.append(CommonUtil.default_dao_implImport).append("\n");

        /* 导入上层类引用 */
        result.append(generateImport("", entityName, tableName)).append("\n");

        /* 生成类名 */
        result.append(generateImplementName(entityName, tableName)).append("\n");
        /* { */
        result.append("{").append("\n");
        /* 生成构造函数 */
        result.append(generateConstructor(entityName, tableName)).append("\n\n");
        /* add方法 */
        result.append(generateAddMethod(entityName, tableName, fields)).append("\n\n");
        /* update方法 */
        result.append(generateUpdateMethod(entityName, tableName, fields)).append("\n\n");
        /* delete方法 */
        result.append(generateDeleteMethod(entityName, tableName, fields)).append("\n\n");
        /* addOrUpdate方法 */
        result.append(generateAddOrUpdateMethod(entityName, tableName, fields)).append("\n\n");
        /* DeleteByKey方法 */
        result.append(genrerateDeleteByKeymethod(entityName, tableName, fields)).append("\n\n");
        /* GetByKey方法 */
        result.append(generateGetByKeyMethod(entityName, tableName, fields)).append("\n\n");
        /* ListAll方法 */
        result.append(generateListAllMethod(entityName, tableName, fields)).append("\n");
        /* addOrUpdateBatch方法 */
        result.append(generateaddOrUpdateBatchMethod(entityName, tableName, fields)).append("\n").append("\n");
        /* deleteBatch方法 */
        result.append(generatedeleteBatchMethod(entityName, tableName, fields)).append("\n");
        /* rsToEntity方法 */
        result.append(generatersToEntityMethod(entityName, tableName, fields)).append("\n");
        /* } */

        if (methods.get("getMultiByUserID") != null
                && methods.get("getMultiByUserID") == false)
        {
            result.append(generateGetEntityByUserID(entityName, tableName, userIDType)).append("\n");
        }

        if (methods.get("getSingleByUserID") != null
                && methods.get("getSingleByUserID") == false)
        {
            result.append(generateGetSingleByUserID(entityName, tableName, userIDType)).append("\n");
        }

        if (methods.get("getMultiByUserID") != null
                && methods.get("getMultiByUserID") == true)
        {
            result.append(generateGetEntityExistByUserID(entityName, tableName, userIDType)).append("\n");
        }

        if (methods.get("getSingleByUserID") != null
                && methods.get("getSingleByUserID") == true)
        {
            result.append(generateGetSingleExistByUserID(entityName, tableName, userIDType)).append("\n");
        }

        result.append("}");
        return result.toString();
    }

    /**
     * log 库实现类
     * 
     * @param suffix
     * @param entityName
     * @param tableName
     * @param fields
     * @param methods
     * @return
     */
    public static String generateLogDaoImplCode(String suffix, String entityName, String tableName,
            List<FieldInfo> fields, Map<String, Boolean> methods, String userIDType)
    {
        StringBuffer result = new StringBuffer();
        /* 所属包 */
        String pack = CommonUtil.Default_Root_Package + "." + suffix + CommonUtil.Default_Dao_Impl;
        result.append("package " + pack).append(";\n\n");

        /* 导入默认引用 */
        // result.append("import java.sql.Connection;").append("\n");
        result.append("import com.base.database.DataReader;").append("\n");
        result.append(CommonUtil.default_dao_implImport).append("\n");

        /* 导入上层类引用 */
        result.append(generateImport(suffix, entityName, tableName)).append("\n");

        /* 生成类名 */
        result.append(generateImplementName(entityName, tableName)).append("\n");
        /* { */
        result.append("{").append("\n");
        /* 生产日志器 */
        // result.append("\t").append(generateLogger(entityName,
        // tableName)).append("\n\n");
        /* 生成构造函数 */
        result.append(generateConstructor(entityName, tableName)).append("\n\n");
        /* add方法 */
        result.append(generateAddMethod(entityName, tableName, fields)).append(
                "\n\n");
        /* update方法 */
        result.append(generateUpdateMethod(entityName, tableName, fields)).append("\n\n");
        /* delete方法 */
        result.append(generateDeleteMethod(entityName, tableName, fields)).append("\n\n");
        /* addOrUpdate方法 */
        result.append(generateAddOrUpdateMethod(entityName, tableName, fields)).append("\n\n");
        /* DeleteByKey方法 */
        result.append(genrerateDeleteByKeymethod(entityName, tableName, fields)).append("\n\n");
        /* GetByKey方法 */
        result.append(generateGetByKeyMethod(entityName, tableName, fields)).append("\n\n");
        /* ListAll方法 */
        result.append(generateListAllMethod(entityName, tableName, fields)).append("\n");
        /* addOrUpdateBatch方法 */
        result.append(
                generateaddOrUpdateBatchMethod(entityName, tableName, fields)).append("\n").append("\n");

        /* deleteBatch方法 */
        result.append(generatedeleteBatchMethod(entityName, tableName, fields)).append("\n");
        /* rsToEntity方法 */
        result.append(generatersToEntityMethod(entityName, tableName, fields)).append("\n");
        /* } */

        if (methods.get("getMultiByUserID") != null)
        {
            result.append(generateGetEntityByUserID(entityName, tableName, userIDType)).append("\n");
        }

        if (methods.get("getSingleByUserID") != null)
        {
            result.append(generateGetSingleByUserID(entityName, tableName, userIDType)).append("\n");
        }

        result.append("}");
        return result.toString();
    }

    /**
     * 生成工厂类函数
     * 
     * @param entityName
     * @param tableName
     * @param interfaceName
     * @param implName
     * @return
     */
    public static String generateFactoryCode(String entityName, String tableName, String interfaceName, String implName)
    {
        StringBuffer result = new StringBuffer();

        /* 所属包 */
        result.append("package " + CommonUtil.default_factory_Package).append(";\n\n");

        result.append("import com.base.database.IDaoFactory;\n");
        result.append(
                "import " + CommonUtil.default_interfacePackage + "."
                        + CommonUtil.CreateInterfaceName(entityName, tableName)).append(";\n");
        result.append(
                "import " + CommonUtil.default_dao_implPackage + "."
                        + CommonUtil.CreateImplName(entityName, tableName)).append(";\n\n");

        /* 生成类名 */
        result.append(CommonUtil.comment());
        result.append("public final class " + CommonUtil.CreateFactoryName(entityName, tableName)
                + " implements IDaoFactory").append("\n");
        /* { */
        result.append("{").append("\n");

        result.append("    private static ").append(interfaceName).append(" ").append("dao;\n\n");
        ;

        result.append("    public static ").append(interfaceName).append(" getDao()").append("\n");
        result.append("    {").append("\n");
        result.append("\t\tif (dao == null)\n");
        result.append("\t\t\tdao").append(" = new ").append(implName).append("(mainHelper);").append("\n").append("\n");
        ;

        result.append("        return ").append("dao").append(";").append("\n");
        result.append("    }").append("\n");

        result.append("}");
        return result.toString();
    }

    /**
     * 生成工厂类函数
     * 
     * @param entityName
     * @param tableName
     * @param interfaceName
     * @param implName
     * @return
     */
    public static String generateLogFactoryCode(String suffix, String entityName, String tableName,
            String interfaceName, String implName)
    {
        StringBuffer result = new StringBuffer();

        String logName = CommonUtil.CreateEntityName(entityName, tableName);

        /* 导入引用 */
        String pack = CommonUtil.Default_Root_Package + "." + suffix + "." + CommonUtil.factoryPath;
        result.append("package " + pack).append(";\n\n");
        result.append("import " + CommonUtil.Default_Root_Package + "." + suffix + CommonUtil.Default_Dao + "."
                + CommonUtil.CreateInterfaceName(entityName, tableName)).append(";\n");
        result.append("import " + CommonUtil.Default_Root_Package + "." + suffix + CommonUtil.Default_Dao_Impl + "."
                + CommonUtil.CreateImplName(entityName, tableName)).append(";\n\n");
        result.append("import ").append("com.util.print.LogFactory;\n");
        result.append("import ").append("java.util.ArrayList;\n");
        result.append("import ").append("java.util.List;\n");

        result.append("import " + CommonUtil.generateDaoEntityPackage(tableName) + "." + logName + ";\n");

        /* 生成类名 */
        String factoryName = CommonUtil.CreateFactoryName(entityName, tableName);
        result.append(CommonUtil.comment());
        result.append("public final class " + factoryName + " implements ILogFactory").append("\n");
        result.append("{").append("\n");
        result.append("\tprivate static ").append(interfaceName).append(" ").append("dao;\n\n");
        result.append(String.format("\tprivate static List<%s> dataList = new ArrayList<>();", logName)).append("\n\n");

        // getDao方法
        result.append("\tpublic static ").append(interfaceName).append(" getDao()").append("\n");
        result.append("\t{").append("\n");
        result.append("\t\tif (dao == null)\n");
        result.append("\t\t\tdao").append(" = new ").append(implName).append("(" + suffix).append("Helper);").append(
                "\n\n");
        result.append("\t\treturn ").append("dao").append(";").append("\n");
        result.append("\t}").append("\n\n");

        // save方法
        result.append("\tpublic void save()").append("\n");
        result.append("\t{").append("\n");
        result.append("\t\ttry").append("\n");
        result.append("\t\t{").append("\n");
        result.append("\t\t\t").append(String.format("List<%s> temp = new ArrayList<>();", logName)).append("\n");
        result.append("\t\t\tsynchronized (dataList)").append("\n");
        result.append("\t\t\t{").append("\n");
        result.append("\t\t\t\ttemp.addAll(dataList);").append("\n");
        result.append("\t\t\t\tdataList.clear();").append("\n");
        result.append("\t\t\t}").append("\n\n");
        result.append("\t\t\tif (!temp.isEmpty())").append("\n");
        result.append("\t\t\t{").append("\n");
        result.append("\t\t\t\t").append("if (getDao().addOrUpdateBatch(temp) == null)").append("\n");
        result.append("\t\t\t\t").append("{").append("\n");
        result.append("\t\t\t\t\t").append("synchronized (dataList)").append("\n");
        result.append("\t\t\t\t\t").append("{").append("\n");
        result.append("\t\t\t\t\t\t").append("if (dataList.size() < 10000 && temp.size() < 10000)").append("\n");
        result.append("\t\t\t\t\t\t\t").append("dataList.addAll(temp);").append("\n");
        result.append("\t\t\t\t\t\t").append("else").append("\n");
        result.append("\t\t\t\t\t\t\t").append("dataList.addAll(temp.subList(0, temp.size() / 2));").append("\n");
        result.append("\t\t\t\t\t").append("}").append("\n\n");
        result.append("\t\t\t\t\t").append(
                String.format("LogFactory.error(\"{} - 日志保存失败。size:{}.\", \"%s\", temp.size());", factoryName)).append(
                        "\n");
        result.append("\t\t\t\t").append("}").append("\n");
        result.append("\t\t\t}").append("\n");
        result.append("\t\t}").append("\n");
        result.append("\t\tcatch (Exception e)").append("\n");
        result.append("\t\t{").append("\n");
        result.append("\t\t\tLogFactory.error(\"\", e);").append("\n");
        result.append("\t\t}").append("\n");
        result.append("\t}").append("\n\n");

        // 添加方法
        result.append("\t").append(String.format("public void addLog(%s log)\n", logName));
        result.append("\t{").append("\n");
        result.append("\t\tsynchronized (dataList)").append("\n");
        result.append("\t\t{").append("\n");
        result.append("\t\t\tdataList.add(log);").append("\n");
        result.append("\t\t}").append("\n");
        result.append("\t}").append("\n");

        result.append("}");
        return result.toString();
    }

    /**
     * @param entityName
     * @param tableName
     * @param fields
     * @return
     */
    private static Object generateGetMaxIdMethod(String entityName, String tableName, List<FieldInfo> fields)
    {
        String firstName = fields.get(0).getName();

        StringBuffer result = new StringBuffer();
        result.append("\t").append("@Override").append("\n");
        result.append("\t").append("public int getMaxId()").append("\n");
        ;
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("String sql = \"select max(`" + firstName + "`) from " + tableName
                + " ;\";").append("\n");

        result.append("\t\tInteger val = getDBHelper().executeQuery(sql, new DataReader<Integer>()\n");
        result.append("\t\t{\n");
        result.append("\t\t\t@Override\n");
        result.append("\t\t\tpublic Integer readData(ResultSet rs, Object... objects) throws Exception{\n");
        result.append("\t\t\t\tif(rs.next()){\n");
        result.append("\t\t\t\t\treturn rs.getInt(1);\n");
        result.append("\t\t\t\t}\n");
        result.append("\t\t\t\treturn 0;\n");
        result.append("\t\t\t}\n");
        result.append("\t\t});\n");
        result.append("\t\treturn val;\n\t}");
        return result.toString();
    }

    private static String generateGetEntityByUserID(String entityName,
            String tableName, String userIDType)
    {

        StringBuffer result = new StringBuffer();
        result.append("\t").append("@Override").append("\n");
        result.append("\t").append("public List<").append(CommonUtil.CreateEntityName(entityName, tableName)).append(
                "> ").append("get"
                        + CommonUtil.CreateEntityName(entityName, tableName)
                        + "ByUserID(" + userIDType + " userID)").append("\n");
        ;
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("String sql = \"select * from " + tableName
                + " where `userID` = ?;\";").append("\n");
        result.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        result.append("\t\t").append("params.put(userID);").append("\n");
        result.append("\t\t").append("List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + "> "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " = queryList(sql,params);").append("\n");
        result.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ";").append("\n");
        result.append("\t").append("}").append("\n");
        return result.toString();
    }

    private static String generateGetEntityExistByUserID(String entityName,
            String tableName, String userIDType)
    {

        StringBuffer result = new StringBuffer();
        result.append("\t").append("@Override").append("\n");
        result.append("\t").append("public List<").append(CommonUtil.CreateEntityName(entityName, tableName)).append(
                "> ").append("get"
                        + CommonUtil.CreateEntityName(entityName, tableName)
                        + "ByUserID(" + userIDType + " userID)").append("\n");
        ;
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("String sql = \"select * from " + tableName
                + " where `userID` = ? and IsExist=1;\";").append("\n");
        result.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        result.append("\t\t").append("params.put(userID);").append("\n");
        result.append("\t\t").append("List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + "> "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " = queryList(sql,params);").append("\n");
        result.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ";").append("\n");
        result.append("\t").append("}").append("\n");
        return result.toString();
    }

    private static String generateGetSingleByUserID(String entityName,
            String tableName, String userIDType)
    {

        StringBuffer result = new StringBuffer();
        result.append("\t").append("@Override").append("\n");
        result.append("\t").append("public ").append(CommonUtil.CreateEntityName(entityName, tableName)).append(
                " ").append("get"
                        + CommonUtil.CreateEntityName(entityName, tableName)
                        + "ByUserID(" + userIDType + " userID)").append("\n");
        ;
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("String sql = \"select * from " + tableName
                + " where `userID` = ?;\";").append("\n");
        result.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        result.append("\t\t").append("params.put(userID);").append("\n");
        result.append("\t\t").append(""
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " = query(sql,params);").append("\n");
        result.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ";").append("\n");
        result.append("\t").append("}").append("\n");
        return result.toString();
    }

    private static String generateGetSingleExistByUserID(String entityName,
            String tableName, String userIDType)
    {

        StringBuffer result = new StringBuffer();
        result.append("\t").append("@Override").append("\n");
        result.append("\t").append("public ").append(CommonUtil.CreateEntityName(entityName, tableName)).append(
                " ").append("get"
                        + CommonUtil.CreateEntityName(entityName, tableName)
                        + "ByUserID(" + userIDType + " userID)").append("\n");
        ;
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("String sql = \"select * from " + tableName
                + " where `userID` = ? and IsExist=1;\";").append("\n");
        result.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        result.append("\t\t").append("params.put(userID);").append("\n");
        result.append("\t\t").append(""
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " = query(sql,params);").append("\n");
        result.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ";").append("\n");
        result.append("\t").append("}").append("\n");
        return result.toString();
    }

    /**
     * @param entityName
     * @param tableName
     * @param fields
     * @return
     */
    private static String generatersToEntityMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = CommonUtil.CreateEntityName(entityName, tableName);
        String methodName = "rsToEntity";
        String params = "ResultSet rs";
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)
                + " throws SQLException").append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append(CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " = new "
                + CommonUtil.CreateEntityName(entityName, tableName)
                + "();").append("\n");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                sb.append("\t\t").append(
                        createSet(CommonUtil.CreateEntityParameterName(entityName,
                                tableName), fields.get(i).getName(), fields.get(i).getJavaType()));
            }
        }
        sb.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)).append(";\n");
        sb.append("\t").append("}").append("\n");
        return sb.toString();
    }

    /**
     * @param entityName
     * @param tableName
     * @param fields
     * @return
     */
    private static String generatedeleteBatchMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "int[]";
        String methodName = "deleteBatch";
        String params = "List<"
                + CommonUtil.CreateEntityName(entityName, tableName) + "> "
                + CommonUtil.CreateEntityPluralParaName(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("String sql = " + createDeleteSql(tableName, fields)
                + ";").append("\n");

        sb.append("\t\t").append("int[] effectedRows = getDBHelper().sqlBatch(sql, "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ", new DataExecutor<int[]>()").append("\n");
        sb.append("\t\t").append("{").append("\n");
        sb.append("\t\t\t").append("@Override").append("\n");
        sb.append("\t\t").append(
                "public int[] execute(PreparedStatement statement, Object... objects) throws Exception").append("\n");
        sb.append("\t\t").append("{").append("\n");
        sb.append("\t\t\t").append("").append("\n");
        sb.append("\t\t\t").append("@SuppressWarnings(\"unchecked\")").append("\n");
        sb.append("\t\t\t").append("List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + ">"
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + " = (List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + ">)objects[0];").append("\n");

        sb.append("\t\t\t").append("for ("
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " : "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ")").append("\n");
        sb.append("\t\t\t").append("{").append("\n");
        sb.append("\t\t\t\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    continue;
                }
                sb.append("\t\t\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                        fields.get(i).getSqlType(), entityName, tableName)).append("\n");
            }
        }

        sb.append("\t\t\t\t\t").append(
                "statement = getDBHelper().prepareCommand(statement,params.getParams());").append("\n");
        sb.append("\t\t\t\t\t").append("statement.addBatch();").append("\n");
        sb.append("\t\t\t\t").append("}").append("\n");
        sb.append("\t\t\t\t").append("return statement.executeBatch();").append("\n");
        sb.append("\t\t\t").append("}").append("\n");
        sb.append("\t\t").append("});").append("\n");
        sb.append("\t\t").append("return effectedRows;").append("\n");
        sb.append("\t").append("}");

        return sb.toString();
    }

    /**
     * @param entityName
     * @param tableName
     * @param fields
     * @return
     */
    private static String generateaddOrUpdateBatchMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "int[]";
        String methodName = "addOrUpdateBatch";
        String params = "List<"
                + CommonUtil.CreateEntityName(entityName, tableName) + "> "
                + CommonUtil.CreateEntityPluralParaName(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append(String.format("\t\tif (%s == null || %s.isEmpty())\n",
                CommonUtil.CreateEntityPluralParaName(entityName, tableName),
                CommonUtil.CreateEntityPluralParaName(entityName, tableName)));
        sb.append("\t\t\treturn new int[1];\n");
        sb.append("\t\t").append("String sql = "
                + createaddOrUpdateSql(tableName, fields) + ";").append("\n");
        sb.append("\t\t").append("int[] effectedRows = getDBHelper().sqlBatch(sql, "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ", new DataExecutor<int[]>()").append("\n");
        sb.append("\t\t\t").append("{").append("\n");
        sb.append("\t\t\t\t").append("@Override").append("\n");
        sb.append("\t\t\t\t").append(
                "public int[] execute(PreparedStatement statement, Object... objects) throws Exception").append("\n");
        sb.append("\t\t\t\t").append("{").append("\n");
        sb.append("\t\t\t\t\t").append("@SuppressWarnings(\"unchecked\")").append("\n");
        sb.append("\t\t\t\t\t").append("List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + ">"
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + " = (List<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + ">)objects[0];").append("\n");

        sb.append("\t\t\t\t\t").append("for ("
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " : "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ")").append("\n");
        sb.append("\t\t\t\t\t").append("{").append("\n");
        sb.append("\t\t\t\t\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                sb.append("\t\t\t\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                        fields.get(i).getSqlType(), entityName, tableName)).append("\n");

            }
        }

        // 字段都是key
        boolean flag = true;

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    flag = false;
                    break;
                }
            }
        }

        if (flag)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    sb.append("\t\t\t\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                            fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                }
            }
        }
        else
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    if (fields.get(i).isPrimaryKey())
                    {
                        continue;
                    }
                    sb.append("\t\t\t\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                            fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                    // tag++;
                }
            }
        }

        sb.append("\t\t\t\t\t\t").append(
                "statement = getDBHelper().prepareCommand(statement,params.getParams());").append("\n");
        sb.append("\t\t\t\t\t\t").append("statement.addBatch();").append("\n");
        sb.append("\t\t\t\t\t").append("}").append("\n");
        sb.append("\t\t\t\t\t").append("return statement.executeBatch();").append("\n");
        sb.append("\t\t\t\t").append("}").append("\n");
        sb.append("\t\t\t").append("});").append("\n");
        sb.append("\t\t").append("return effectedRows;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateAddBatchMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "void";
        String methodName = "addBatch";
        String params = "List<"
                + CommonUtil.CreateEntityName(entityName, tableName) + "> "
                + CommonUtil.CreateEntityPluralParaName(entityName, tableName)
                + ", Connection conn";
        String sql = createInsertSql(tableName, fields);
        sql = sql.substring(0, sql.length() - 2);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append(
                " throws SQLException").append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("String sql = " + sql).append("\";\n");
        sb.append("\t\t").append(
                "PreparedStatement statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);\n");

        sb.append("\t\t").append("for ("
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " : "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ")").append("\n");
        sb.append("\t\t").append("{").append("\n");
        sb.append("\t\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        // int tag = 1;
        for (int i = 0; i < fields.size(); i++)
        {
            if (!fields.get(i).isAotuIncreamte())
            {
                sb.append("\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                        fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                // tag++;
            }
        }

        sb.append("\t\t\t").append("statement = getDBHelper().prepareCommand(statement,params.getParams());").append(
                "\n");
        sb.append("\t\t\t").append("statement.addBatch();").append("\n");
        sb.append("\t\t").append("}").append("\n");
        sb.append("\t\t").append("statement.executeBatch();").append("\n");
        sb.append("\t\t").append("super.close(statement);").append("\n");
        sb.append("\t").append("}").append("\n");
        return sb.toString();
    }

    private static String generateUpdateBatchMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "void";
        String methodName = "updateBatch";
        String params = "List<"
                + CommonUtil.CreateEntityName(entityName, tableName) + "> "
                + CommonUtil.CreateEntityPluralParaName(entityName, tableName)
                + ", Connection conn";
        String sql = createUpdateSql(tableName, fields);
        sql = sql.substring(0, sql.length() - 2);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append(
                " throws SQLException").append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("String sql = " + sql).append("\";\n");
        sb.append("\t\t").append(
                "PreparedStatement statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);\n");

        sb.append("\t\t").append("for ("
                + CommonUtil.CreateEntityName(entityName, tableName)
                + " "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + " : "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ")").append("\n");
        sb.append("\t\t").append("{").append("\n");
        sb.append("\t\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");

        List<FieldInfo> keys = new ArrayList<FieldInfo>();
        // int tag = 1;
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).isPrimaryKey())
            {
                keys.add(fields.get(i));
                continue;
            }
            sb.append("\t\t\t").append(createSetParams("params", fields.get(i).getName(),
                    fields.get(i).getSqlType(), entityName, tableName)).append("\n");
            // tag++;
        }
        for (FieldInfo fieldInfo : keys)
        {
            sb.append("\t\t\t").append(createSetParams("params", fieldInfo.getName(),
                    fieldInfo.getSqlType(), entityName, tableName)).append("\n");
            // tag++;
        }

        sb.append("\t\t\t").append("statement = getDBHelper().prepareCommand(statement,params.getParams());").append(
                "\n");
        sb.append("\t\t\t").append("statement.addBatch();").append("\n");
        sb.append("\t\t").append("}").append("\n");
        sb.append("\t\t").append("statement.executeBatch();").append("\n");
        sb.append("\t\t").append("super.close(statement);").append("\n");
        sb.append("\t").append("}").append("\n");
        return sb.toString();
    }

    /**
     * @param entityName
     * @param tableName
     * @param fields
     * @return
     */
    private static Object genrerateDeleteByKeymethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "boolean";
        String methodName = "deleteByKey";
        String params = "Object... ids";
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("boolean result = false;").append("\n");
        sb.append("\t\t").append("String sql = " + createDeleteSql(tableName, fields)
                + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        for (int i = 0; i < fields.size(); i++)
        {
            if (!fields.get(i).isPrimaryKey())
            {
                continue;
            }
            sb.append("\t\t").append(createSetParams("params", "ids", fields.get(i).getSqlType(), i)).append("\n");
        }
        sb.append("\t\t").append("result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;").append("\n");
        sb.append("\t\t").append("return result;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateGetByKeyMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = CommonUtil.CreateEntityName(entityName, tableName);
        String methodName = "getByKey";
        String params = "Object... ids";

        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("String sql = " + createGetByIDSql(tableName, fields)
                + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    continue;
                }
                sb.append("\t\t").append(createSetParams("params", "ids", fields.get(i).getSqlType(), i)).append("\n");
            }
        }
        sb.append("\t\t").append(
                CommonUtil.CreateEntityName(entityName, tableName)
                        + " "
                        + CommonUtil.CreateEntityParameterName(entityName,
                                tableName)
                        + " = query(sql, params);");
        // sb.append("\t\t").append(CommonUtil.CreateEntityName(entityName,
        // tableName) + " " + CommonUtil.CreateEntityParameterName(entityName,
        // tableName) +
        // " = getDBHelper().executeQuery(sql, para, new DataReader<" +
        // CommonUtil.CreateEntityName(entityName, tableName) +
        // ">()").append("\n");
        // sb.append("\t\t").append("{").append("\n");
        // sb.append("\t\t\t").append("@Override").append("\n");
        // sb.append("\t\t\t").append("public " +
        // CommonUtil.CreateEntityName(entityName, tableName) +
        // " readData(ResultSet rs,Object... objects) throws Exception").append("\n");
        // sb.append("\t\t\t").append("{").append("\n");
        // sb.append("\t\t\t\t").append("if (rs.last())").append("\n");
        // sb.append("\t\t\t\t").append("{").append("\n");
        // sb.append("\t\t\t\t\t").append("return " +
        // CommonUtil.CreateImplName(entityName, tableName) +
        // ".this.rsToEntity(rs);").append("\n");
        // sb.append("\t\t\t\t").append("}").append("\n");
        // sb.append("\t\t\t\t").append("return null;").append("\n");
        // sb.append("\t\t\t").append("}").append("\n");
        // sb.append("\t\t").append("});").append("\n");
        sb.append("\t\t").append("return "
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ";").append("\n");
        sb.append("\t").append("}").append("\n");
        return sb.toString();
    }

    /**
     * @return
     */
    private static String generateAddOrUpdateMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {

        String scope = "public";
        String _return = "boolean";
        String methodName = "addOrUpdate";
        String params = generateParam(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("boolean result = false;").append("\n");
        sb.append("\t\t").append("String sql = "
                + createaddOrUpdateSql(tableName, fields) + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                        fields.get(i).getSqlType(), entityName, tableName)).append("\n");
            }
        }

        // 字段都是key
        boolean flag = true;

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    flag = false;
                    break;
                }
            }
        }

        if (flag)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                            fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                }
            }
        }
        else
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    if (fields.get(i).isPrimaryKey())
                    {
                        continue;
                    }
                    sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                            fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                }
            }
        }

        sb.append("\t\t").append("result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;").append("\n");
        sb.append("\t\t").append("return result;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateListAllMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "List<"
                + CommonUtil.CreateEntityName(entityName, tableName) + ">";
        String methodName = "listAll";
        String params = "";

        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("String sql = \"select * from " + tableName + ";\"").append(";\n");
        sb.append("\t\t").append(
                "List<"
                        + CommonUtil.CreateEntityName(entityName, tableName)
                        + "> "
                        + CommonUtil.CreateEntityPluralParaName(entityName,
                                tableName)
                        + " = queryList(sql);");
        // sb.append("\t\t").append("List<"+
        // CommonUtil.CreateEntityName(entityName, tableName) + "> "+
        // CommonUtil.CreateEntityPluralParaName(entityName,tableName) +
        // " = getDBHelper().executeQuery(sql, new DataReader<List<" +
        // CommonUtil.CreateEntityName(entityName, tableName)
        // +">>()").append("\n");
        // sb.append("\t\t").append("{").append("\n");
        // sb.append("\t\t\t").append("@Override").append("\n");
        // sb.append("\t\t\t").append("public List<" +
        // CommonUtil.CreateEntityName(entityName, tableName) +
        // "> readData(ResultSet rs,Object... objects) throws Exception").append("\n");
        // sb.append("\t\t\t").append("{").append("\n");
        // sb.append("\t\t\t\t").append("return " +
        // CommonUtil.CreateImplName(entityName, tableName) +
        // ".this.rsToEntityList(rs);").append("\n");
        // sb.append("\t\t\t").append("}").append("\n");
        // sb.append("\t\t").append("});").append("\n");
        sb.append("\t\t").append("return "
                + CommonUtil.CreateEntityPluralParaName(entityName,
                        tableName)
                + ";").append("\n");
        sb.append("\t").append("}").append("\n");

        return sb.toString();
    }

    private static String generateDeleteMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "boolean";
        String methodName = "delete";
        String params = generateParam(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("boolean result = false;").append("\n");
        sb.append("\t\t").append("String sql = " + createDeleteSql(tableName, fields)
                + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    continue;
                }
                sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                        fields.get(i).getSqlType(), entityName, tableName)).append("\n");
            }
        }
        sb.append("\t\t").append("result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;").append("\n");
        sb.append("\t\t").append("return result;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateUpdateMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "boolean";
        String methodName = "update";
        String params = generateParam(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("boolean result = false;").append("\n");
        sb.append("\t\t").append("String sql = " + createUpdateSql(tableName, fields)
                + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        List<FieldInfo> keys = new ArrayList<FieldInfo>();

        // 如果全部字段是主键，没有普通字段
        boolean flag = true;

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (fields.get(i).isPrimaryKey())
                {
                    keys.add(fields.get(i));
                    continue;
                }
                else
                    flag = false;
            }
        }

        if (flag)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                            fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                    break;
                }
            }
        }
        else
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    if (!fields.get(i).isPrimaryKey())
                    {
                        sb.append("\t\t").append(createSetParams("params", fields.get(i).getName(),
                                fields.get(i).getSqlType(), entityName, tableName)).append("\n");
                    }
                }
            }
        }

        for (FieldInfo fieldInfo : keys)
        {
            if (fieldInfo.flag)
            {
                sb.append("\t\t").append(createSetParams("params", fieldInfo.getName(),
                        fieldInfo.getSqlType(), entityName, tableName)).append("\n");
            }
            // tag++;
        }
        sb.append("\t\t").append("result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;").append("\n");
        sb.append("\t\t").append("return result;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateAddMethod(String entityName,
            String tableName, List<FieldInfo> fields)
    {
        String scope = "public";
        String _return = "boolean";
        String methodName = "add";
        String params = generateParam(entityName, tableName);
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append("@Override").append("\n");
        sb.append("\t").append(generateMethod(scope, _return, methodName, params)).append("\n");
        sb.append("\t").append("{").append("\n");
        sb.append("\t\t").append("boolean result = false;").append("\n");
        sb.append("\t\t").append("String sql = " + createInsertSql(tableName, fields)
                + ";").append("\n");
        sb.append("\t\t").append("DBParamWrapper params = new DBParamWrapper();").append("\n");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isAotuIncreamte())
                {
                    sb.append("\t\t").append(
                            createSetParams("params", fields.get(i).getName(), fields.get(i).getSqlType(),
                                    entityName, tableName)).append("\n");
                }
            }
        }
        sb.append("\t\t").append("result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;").append("\n");
        sb.append("\t\t").append("return result;").append("\n");
        sb.append("\t").append("}");
        return sb.toString();
    }

    private static String generateConstructor(String entityName,
            String tableName)
    {
        StringBuffer result = new StringBuffer();
        result.append("\t").append("public "
                + CommonUtil.CreateImplName(entityName, tableName)
                + "(DBHelper helper)").append("\n");
        result.append("\t").append("{").append("\n");
        result.append("\t\t").append("super(helper);").append("\n");
        result.append("\t").append("}").append("\n");
        return result.toString();
    }

    // /**
    // * @param entityName
    // * @return
    // */
    // private static String generateLogger(String entityName, String tableName)
    // {
    // StringBuffer result = new StringBuffer();
    // result.append(
    // "private static final Logger LOGGER = LoggerFactory.getLogger("
    // + CommonUtil.CreateImplName(entityName, tableName)
    // + ".class)").append(";");
    // return result.toString();
    // }

    private static String generateImplementName(String entityName,
            String tableName)
    {
        StringBuffer result = new StringBuffer();
        result.append(CommonUtil.comment());
        result.append("public class "
                + CommonUtil.CreateImplName(entityName, tableName)
                + " extends BaseDao<"
                + CommonUtil.CreateEntityName(entityName, tableName)
                + "> implements "
                + CommonUtil.CreateInterfaceName(entityName, tableName));
        return result.toString();
    }

    private static String generateImport(String suffix, String entityName, String tableName)
    {
        StringBuffer result = new StringBuffer();
        if (!suffix.isEmpty())
            suffix = "." + suffix;
        result.append(
                "import " + CommonUtil.Default_Root_Package + suffix + CommonUtil.Default_Dao + "."
                        + CommonUtil.CreateInterfaceName(entityName, tableName)).append(";\n");
        result.append(
                "import " + CommonUtil.generateDaoEntityPackage(tableName)
                        + "."
                        + CommonUtil.CreateEntityName(entityName, tableName)).append(";\n\n");
        return result.toString();

    }

    private static String generateParam(String entityName, String tableName)
    {
        return CommonUtil.CreateEntityName(entityName, tableName) + " "
                + CommonUtil.CreateEntityParameterName(entityName, tableName);
    }

    private static String generateMethod(String scope, String _return,
            String methodName, String params)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(scope + " " + _return + " " + methodName + "(" + params + ")");
        return sb.toString();
    }

    private static String createInsertSql(String tableName,
            List<FieldInfo> fields)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("\"insert into " + tableName);
        sql.append("(");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isAotuIncreamte())
                {
                    sql.append("`").append(fields.get(i).getName()).append("`, ");
                }
            }
        }
        sql.delete(sql.length() - 2, sql.length());

        sql.append(") values(");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isAotuIncreamte())
                {
                    sql.append("?, ");
                }
            }
        }
        sql.delete(sql.length() - 2, sql.length());

        sql.append(");\"");
        return sql.toString();
    }

    private static String createUpdateSql(String tableName,
            List<FieldInfo> fields)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("\"update " + tableName + " set ");

        // 如果全部字段是主键，没有普通字段
        boolean flag = true;

        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    flag = false;
                }
            }
        }

        if (flag)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    sql.append("`").append(fields.get(i).getName()).append("`=?, ");
                    break;
                }
            }
        }
        else
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    if (fields.get(i).isPrimaryKey())
                    {
                        continue;
                    }
                    sql.append("`").append(fields.get(i).getName()).append("`=?, ");
                }
            }
        }

        sql.delete(sql.length() - 2, sql.length());

        sql.append(" where ");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    continue;
                }
                sql.append("`").append(fields.get(i).getName()).append("`=?");
                sql.append(" and ");
            }
        }
        if (sql.lastIndexOf(" and ") > 0)
        {
            sql.delete(sql.length() - 5, sql.length());
        }

        sql.append(";\"");
        return sql.toString();
    }

    private static String createaddOrUpdateSql(String tableName,
            List<FieldInfo> fields)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("\"insert into " + tableName);
        sql.append("(");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                sql.append("`").append(fields.get(i).getName()).append("`, ");
            }

        }
        sql.delete(sql.length() - 2, sql.length());

        sql.append(") values(");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                sql.append("?, ");
            }
        }
        sql.delete(sql.length() - 2, sql.length());

        // 如果字段都是key
        boolean flag = true;
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    flag = false;
                }
            }
        }

        sql.append(") on DUPLICATE KEY update ");

        if (flag)
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    sql.append("`" + fields.get(i).getName() + "`=?,");
                }
            }
        }
        else
        {
            for (int i = 0; i < fields.size(); i++)
            {
                if (fields.get(i).flag)
                {
                    if (!fields.get(i).isPrimaryKey())
                    {
                        sql.append("`" + fields.get(i).getName() + "`=?,");
                    }
                }
            }
        }

        return sql.substring(0, sql.length() - 1) + ";\"";
    }

    private static String createDeleteSql(String tableName,
            List<FieldInfo> fields)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("\"delete from " + tableName);
        sql.append(" where ");
        for (int i = 0; i < fields.size(); i++)
        {
            if (fields.get(i).flag)
            {
                if (!fields.get(i).isPrimaryKey())
                {
                    continue;
                }
                sql.append("`").append(fields.get(i).getName()).append("`=?");
                sql.append(" and ");
            }
        }
        if (sql.lastIndexOf(" and ") > 0)
        {
            sql.delete(sql.length() - 5, sql.length());
        }

        sql.append(";\"");
        return sql.toString();
    }

    private static String createGetByIDSql(String tableName,
            List<FieldInfo> fields)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("\"select * from " + tableName);
        sql.append(" where ");
        for (int i = 0; i < fields.size(); i++)
        {
            if (!fields.get(i).isPrimaryKey())
            {
                continue;
            }
            sql.append("`").append(fields.get(i).getName()).append("`=?");
            sql.append(" and ");
        }
        if (sql.lastIndexOf(" and ") > 0)
        {
            sql.delete(sql.length() - 5, sql.length());
        }

        sql.append(";\"");
        return sql.toString();
    }

    private static String createSetParams(String paramName, String var,
            String type, String entityName, String tableName)
    {
        if (type.equals("INT"))
            type = "INTEGER";
        if (type.equals("DATETIME"))
            type = "TIMESTAMP";
        StringBuffer result = new StringBuffer();
        result.append(paramName).append(".put(").append("Types.").append(type).append(","
                + CommonUtil.CreateEntityParameterName(entityName,
                        tableName)
                + ".");
        result.append("get" + var.substring(0, 1).toUpperCase()
                + var.substring(1) + "());");
        return result.toString();
    }

    private static String createSetParams(String paramName, String var,
            String type, int index)
    {
        if (type.equals("INT"))
            type = "INTEGER";
        if (type.equals("DATETIME"))
            type = "TIMESTAMP";
        StringBuffer result = new StringBuffer();
        result.append(paramName).append(".put(").append("Types.").append(type).append(",ids[" + index + "]);");
        return result.toString();
    }

    private static StringBuffer createSet(String objName, String var,
            String type)
    {
        if (type.equals("byte[]"))
            type = "Bytes";
        if (type.equals("int"))
            type = "Int";
        if (type.equals("short"))
            type = "Short";
        if (type.equals("byte"))
            type = "Byte";
        if (type.equals("float"))
            type = "Float";
        if (type.equals("long"))
            type = "Long";
        if (type.equals("boolean"))
            type = "Boolean";
        if (type.equals("Date"))
            type = "Timestamp";
        if (type.equals("java.math.BigDecimal"))
        {
            type = "BigDecimal";
        }
        StringBuffer result = new StringBuffer();
        result.append(objName).append(".");
        result.append("set" + var.substring(0, 1).toUpperCase()
                + var.substring(1) + "(rs.get" + type + "(");
        result.append("\"").append(var).append("\"").append("));\n");
        return result;
    }
}
