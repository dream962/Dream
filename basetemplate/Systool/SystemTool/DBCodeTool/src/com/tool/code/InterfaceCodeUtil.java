package com.tool.code;

import java.util.Map;

public class InterfaceCodeUtil
{
    public static String generateInterfaceCode(String entityName,
            String tableName, Map<String, Boolean> methods, String userIDType)
    {
        StringBuffer result = new StringBuffer();
        /* 所属包 */
        result.append("package " + CommonUtil.default_interfacePackage).append(";\n\n");

        /* 导入框架内部引用 */
        result.append(CommonUtil.default_dao_interfaceImport).append("\n");

        /* 导入上层引用 */
        result.append(generateImport(entityName, tableName)).append(";\n\n");

        /* 生成接口名 */
        result.append(generateInterfaceName(entityName, tableName)).append("\n");
        result.append("{\n");

        if (methods.get("getMultiByUserID") != null)
        {
            result.append(generateGetEntityByUserID(entityName, tableName, userIDType)).append("\n");
            result.append("\n");
        }

        if (methods.get("getSingleByUserID") != null)
        {
            result.append(generateGetSingleByUserID(entityName, tableName, userIDType)).append("\n");
            result.append("\n");
        }

        result.append("}\n");
        return result.toString();
    }

    public static String generateLogInterfaceCode(String log, String entityName,
            String tableName, Map<String, Boolean> methods, String userIDType)
    {
        StringBuffer result = new StringBuffer();
        /* 所属包 */
        String pack = CommonUtil.Default_Root_Package + "." + log + "." + CommonUtil.interfacePath;
        result.append("package " + pack).append(";\n\n");

        /* 导入框架内部引用 */
        result.append(CommonUtil.default_dao_interfaceImport).append("\n");

        /* 导入上层引用 */
        result.append(generateImport(entityName, tableName)).append(";\n\n");

        /* 生成接口名 */
        result.append(generateInterfaceName(entityName, tableName)).append("\n");
        result.append("{\n");

        if (methods.get("getMultiByUserID") != null)
        {
            result.append(generateGetEntityByUserID(entityName, tableName, userIDType)).append("\n");
        }
        result.append("\n");
        if (methods.get("getSingleByUserID") != null)
        {
            result.append(generateGetSingleByUserID(entityName, tableName, userIDType)).append("\n");
        }

        result.append("}\n");
        return result.toString();
    }

    private static String generateGetEntityByUserID(String entityName, String tableName, String userIDType)
    {
        StringBuffer result = new StringBuffer();
        result.append("\t").append("List<").append(CommonUtil.CreateEntityName(entityName, tableName)).append("> ");
        result.append(
                "get" + CommonUtil.CreateEntityName(entityName, tableName) + "ByUserID(" + userIDType + " userID);");
        return result.toString();
    }

    private static String generateGetSingleByUserID(String entityName, String tableName, String userIDType)
    {
        StringBuffer result = new StringBuffer();
        result.append("\t").append(CommonUtil.CreateEntityName(entityName, tableName)).append(" ");
        result.append(
                "get" + CommonUtil.CreateEntityName(entityName, tableName) + "ByUserID(" + userIDType + " userID);");
        return result.toString();
    }

    private static String generateImport(String entityName, String tableName)
    {
        StringBuffer result = new StringBuffer();
        result.append("import "
                + CommonUtil.generateDaoEntityPackage(tableName) + "."
                + CommonUtil.CreateEntityName(entityName, tableName));
        return result.toString();
    }

    private static String generateInterfaceName(String entityName,
            String tableName)
    {
        StringBuffer result = new StringBuffer();
        result.append(CommonUtil.comment());
        result.append("public interface "
                + CommonUtil.CreateInterfaceName(entityName, tableName)
                + " extends IBaseDao<"
                + CommonUtil.CreateEntityName(entityName, tableName) + ">");
        return result.toString();
    }
}
