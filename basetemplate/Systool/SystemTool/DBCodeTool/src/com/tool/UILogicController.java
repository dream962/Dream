package com.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import com.tool.code.CacheCodeUtil;
import com.tool.code.CommonUtil;
import com.tool.code.DaoCodeUtil;
import com.tool.code.EntityCodeUtil;
import com.tool.code.FieldInfo;
import com.tool.code.InterfaceCodeUtil;
import com.tool.code.LuaUtil;
import com.tool.code.OrmEntityCodeUtil;
import com.tool.code.SystemEntityCodeUtil;
import com.tool.data.ExcelSheet;
import com.tool.data.ExcelSheet.OneRowData;

public class UILogicController
{
    private static File entityFilePath;// 实体文件
    private static File interfaceFilePath;// 接口文件
    private static File implFilePath;// 接口实现文件
    private static File factoryFilePath;// 工厂实现文件

    private static void createFilePath(String tableName)
    {
        String rootPath = CommonUtil.RootPath;
        File file = new File(rootPath);
        if (!file.exists())
        {
            file.mkdirs();
        }
        String entityPath = rootPath + "//" + CommonUtil.entityPath;
        String subPath = CommonUtil.getEntitySubPath(tableName);
        if (!subPath.equals(""))
        {
            entityPath += "//" + subPath;
        }
        entityFilePath = new File(entityPath);
        if (!entityFilePath.exists())
        {
            entityFilePath.mkdirs();
        }

        if (subPath.equals("bean"))
        {
            String factory = entityPath + "//factory";
            File factoryPath = new File(factory);
            if (!factoryPath.exists())
            {
                factoryPath.mkdirs();
            }

        }

        // 日志的特殊处理
        if (subPath.equals("log") || subPath.equals("entity"))
        {
            // dao 接口路径
            String interfacePath = entityFilePath + "//" + CommonUtil.interfacePath;
            interfaceFilePath = new File(interfacePath);
            if (!interfaceFilePath.exists())
            {
                interfaceFilePath.mkdir();
            }

            // dao 实现路径
            String implPath = entityFilePath + "//" + CommonUtil.implPath;
            implFilePath = new File(implPath);
            if (!implFilePath.exists())
            {
                implFilePath.mkdir();
            }

            // 工厂方法路径
            String factoryPath = entityFilePath + "//" + CommonUtil.factoryPath;
            factoryFilePath = new File(factoryPath);
            if (!factoryFilePath.exists())
            {
                factoryFilePath.mkdir();
            }
        }
        else
        {

            // dao 接口路径
            String interfacePath = rootPath + "//" + CommonUtil.interfacePath;
            interfaceFilePath = new File(interfacePath);
            if (!interfaceFilePath.exists())
            {
                interfaceFilePath.mkdir();
            }

            // dao 实现路径
            String implPath = rootPath + "//" + CommonUtil.implPath;
            implFilePath = new File(implPath);
            if (!implFilePath.exists())
            {
                implFilePath.mkdir();
            }

            // 工厂方法路径
            String factoryPath = rootPath + "//" + CommonUtil.factoryPath;
            factoryFilePath = new File(factoryPath);
            if (!factoryFilePath.exists())
            {
                factoryFilePath.mkdir();
            }
        }
    }

    /**
     * 生成lua文件
     * 
     * @param tableName
     * @param selectExtMethod
     */
    public static void generateLuaCode(String tableName)
    {
        // lua 文件名称
        String luaFileName = LuaUtil.createLuaName(tableName);
        // lua 文件路径
        String filePath = LuaUtil.createLuaFilePath(luaFileName);

        String commont = MainWindow.dataSource.getCommont(tableName);

        List<OneRowData> list = MainWindow.dataSource.getData(tableName);
        // lua 内容
        String luaContext = LuaUtil.createLuaContext(luaFileName, commont, MainWindow.fieldMap, list);

        LuaUtil.createLuaFile(filePath, luaContext);
    }

    public static void generateORMCode(String entityName, String tableName, Map<String, Boolean> selectExtMethod)
    {
        createFilePath(tableName);// 首先创建目录文件

        String entityFileName = CommonUtil.CreateEntityName(entityName, MainWindow.currentSelectedTable) + ".java";
        String entityContent = OrmEntityCodeUtil.generateEntityCode(entityName, tableName, MainWindow.fieldMap,
                selectExtMethod);
        generateFile(entityFilePath, entityFileName, entityContent);

        // String interfaceFileName = CommonUtil.CreateInterfaceName(entityName, currentSelectedTable);
        // String interfaceContent = OrmInterfaceCodeUtil.generateInterfaceCode(entityName, currentSelectedTable);
        // generateFile(interfaceFilePath, interfaceFileName + ".java", interfaceContent, entityName);
        //
        // String implFileName = CommonUtil.CreateImplName(entityName, currentSelectedTable);
        // String implContent = OrmDaoCdoeUtil.generateDaoImplCode(entityName, currentSelectedTable);
        // generateFile(implFilePath, implFileName + ".java", implContent, entityName);
    }

    public static void generateSystemCode(String entityName, String tableName)
    {
        createFilePath(tableName);// 首先创建目录文件

        String beanName = CommonUtil.CreateEntityName(entityName, tableName);
        String entityFileName = beanName + ".java";
        String context = MainWindow.dataSource.getCommont(tableName);
        String entityContent = SystemEntityCodeUtil.generateEntityCode(entityName, tableName, MainWindow.fieldMap,context);

        generateFile(entityFilePath, entityFileName, entityContent);

        // BeanFactory
        String factoryPath = entityFilePath + "\\factory";
        String factoryName = beanName + "Factory";
        String factoryFileName = factoryName + ".java";
        String factoryContent = SystemEntityCodeUtil.generateEntityFactory(beanName, MainWindow.fieldMap);

        generateFile(new File(factoryPath), factoryFileName, factoryContent);
    }

    public static void generateAllCode(ExcelSheet sheet)
    {
        String tableName = sheet.getTableName();
        String entityName = CommonUtil.generateEntityNameBySourceTable(tableName);

        createFilePath(tableName);// 首先创建目录文件

        String beanName = CommonUtil.CreateEntityName(entityName, tableName);
        String entityFileName = beanName + ".java";
        String context = MainWindow.dataSource.getCommont(tableName);
        String entityContent = SystemEntityCodeUtil.generateEntityCode(entityName, tableName, sheet.getFieldMap(),context);
        generateFile(entityFilePath, entityFileName, entityContent);

        // BeanFactory
        String factoryPath = entityFilePath + "\\factory";
        String factoryName = beanName + "Factory";
        String factoryFileName = factoryName + ".java";
        String factoryContent = SystemEntityCodeUtil.generateEntityFactory(beanName, sheet.getFieldMap());

        generateFile(new File(factoryPath), factoryFileName, factoryContent);
    }

    private static void generateCacheData(String entityName, String tableName, Map<String, FieldInfo> fieldMap)
    {
        String rootPath = CommonUtil.RootPath;
        String cache = rootPath + "//cache";
        File cachePath = new File(cache);
        if (!cachePath.exists())
        {
            cachePath.mkdirs();
        }

        String cacheFileName = CommonUtil.CreateCacheName(entityName, tableName)+".java";
        String cacheContent = CacheCodeUtil.generateCacheCode(entityName,MainWindow.currentSelectedTable, fieldMap);
        
        generateFile(cachePath, cacheFileName, cacheContent);
    }

    public static void generateUserTableCode(String entityName, String tableName, Map<String, FieldInfo> fieldMap,
            List<FieldInfo> fields, Map<String, Boolean> selectExtMethod, boolean isAddChangedMap,boolean isCreateCache
            ,boolean isDBOperate)
    {
        // 生成缓存
        if (isCreateCache)
        {
            generateCacheData(entityName, tableName, fieldMap);
        }

        createFilePath(tableName);// 首先创建目录文件

        String subPath = CommonUtil.getEntitySubPath(tableName);

        String entityFileName = CommonUtil.CreateEntityName(entityName, MainWindow.currentSelectedTable) + ".java";
        String entityContent = EntityCodeUtil.generateEntityCode(CommonUtil.entityPath, entityName,
                MainWindow.currentSelectedTable, fieldMap, isAddChangedMap);
        generateFile(entityFilePath, entityFileName, entityContent);

        String type = "int";
        boolean isContainUserID=false;
        if (!selectExtMethod.isEmpty())
        {
            for (FieldInfo info : fields)
            {
                if (info.getName().equalsIgnoreCase("UserID"))
                {
                    type = info.getJavaType();
                    isContainUserID=true;
                    break;
                }
            }
        }
        
        // 如果不包含UserID，扩展方法不需要
        if(!isContainUserID)
        {
            selectExtMethod.clear();
        }

        if (subPath.equals("log") || subPath.equals("entity"))
        {
            String interfaceFileName = CommonUtil.CreateInterfaceName(entityName, MainWindow.currentSelectedTable);

            String interfaceContent = InterfaceCodeUtil.generateLogInterfaceCode(subPath, entityName,
                    MainWindow.currentSelectedTable,selectExtMethod, type);
            generateFile(interfaceFilePath, interfaceFileName + ".java", interfaceContent);

            String implFileName = CommonUtil.CreateImplName(entityName, MainWindow.currentSelectedTable);
            String implContent = DaoCodeUtil.generateLogDaoImplCode(subPath, entityName, tableName, fields,
                    selectExtMethod, type);
            generateFile(implFilePath, implFileName + ".java", implContent);

            String factoryFileName = CommonUtil.CreateFactoryName(entityName, MainWindow.currentSelectedTable)
                    + ".java";
            String factoryContent = DaoCodeUtil.generateLogFactoryCode(subPath, entityName, tableName,
                    interfaceFileName, implFileName);
            generateFile(factoryFilePath, factoryFileName, factoryContent);
        }
        else
        {
            String interfaceFileName = CommonUtil.CreateInterfaceName(entityName, MainWindow.currentSelectedTable);

            String interfaceContent = InterfaceCodeUtil.generateInterfaceCode(entityName,
                    MainWindow.currentSelectedTable, selectExtMethod, type);
            generateFile(interfaceFilePath, interfaceFileName + ".java", interfaceContent);

            String implFileName = CommonUtil.CreateImplName(entityName, MainWindow.currentSelectedTable);
            String implContent = DaoCodeUtil.generateDaoImplCode(entityName, tableName, fields, selectExtMethod, type,isDBOperate);
            generateFile(implFilePath, implFileName + ".java", implContent);

            String factoryFileName = CommonUtil.CreateFactoryName(entityName, MainWindow.currentSelectedTable)
                    + ".java";
            String factoryContent = DaoCodeUtil.generateFactoryCode(entityName, tableName, interfaceFileName,
                    implFileName);
            generateFile(factoryFilePath, factoryFileName, factoryContent);
        }
    }

    private static void generateFile(File rootFile, String fileName, String content)
    {
        File entityFile = new File(rootFile, fileName);
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

            System.err.println("Create File：" + rootFile + " " + fileName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
