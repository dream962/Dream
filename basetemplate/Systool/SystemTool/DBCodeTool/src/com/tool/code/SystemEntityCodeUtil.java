package com.tool.code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SystemEntityCodeUtil
{
    /**
     * 生成实体代码
     * 
     * @param packageName
     * @param className
     * @param properties
     * @return
     */
    public static String generateEntityCode(String name, String tableName, Map<String, FieldInfo> fieldMap,String context)
    {
        StringBuffer result = new StringBuffer();

        /* 所属包 */
        result.append("package " + CommonUtil.generateORMEntityPackage(tableName)).append(";\n\n");

        /* 导入类 */
        result.append(generateImport(fieldMap)).append("\n");

        /* Class 开头 */
        String tmp = CommonUtil.CreateEntityName(name, tableName);

        /* 类注释 */
        result.append("/**").append("\n");
        result.append(" * ").append(context).append("\n");
        result.append(" * @author = auto generate code.").append("\n");
        result.append(" */").append("\n");

        result.append("public final class " + tmp + "\n");

        result.append("{").append("\n");

        /* 各个私有属性 */
        result.append(generatePropertiesPrivate(fieldMap));

        /* Get Set 方法 */
        result.append(generateMethodGetterAndSetterPublic(fieldMap));

        /* clone 方法 */
        result.append(generateMethodClonePublic(name, tableName, fieldMap));

        /* reset 方法 */
        result.append(generateMethodResetPublic(name, tableName, fieldMap));

        /* Class 结尾 */
        result.append("}");

        return result.toString();
    }

    /**
     * @return
     */
    private static Object generateOrmConfig(String tableName, Map<String, FieldInfo> fieldMap)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("@PersistentEntity(sourceDomain = ").append("\"" + tableName + "\"").append(", targetDomain = ");
        sb.append("\"" + tableName + "\"");

        Set<Entry<String, FieldInfo>> sets = fieldMap.entrySet();
        sb.append(", primaryKey = \"");
        boolean isAdd = false;
        for (Entry<String, FieldInfo> entry : sets)
        {
            FieldInfo fieldInfo = entry.getValue();
            if (fieldInfo.isPrimaryKey())
            {
                isAdd = true;
                sb.append(fieldInfo.getName() + ",");
            }
        }
        if (isAdd)
            sb.delete(sb.length() - 1, sb.length());
        sb.append("\")");

        return sb.toString();
    }

    /**
     * @param fieldMap
     * @return
     */
    private static Object generateFrameImport(Map<String, FieldInfo> fieldMap)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(CommonUtil.default_orm_entityImport).append("\n");
        return sb.toString();
    }

    /**
     * 应该只有 Date 需要另外导入, 其他都是默认包 java.lang...
     * 
     * @param types
     * @return
     */
    private static String generateImport(Map<String, FieldInfo> fieldMap)
    {
        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();
        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            if (field.getJavaType().equalsIgnoreCase("Date")) // 非完全匹配
            {
                return "import java.util.Date;\n";
            }
        }
        return "";
    }

    /**
     * 生成私有属性, 属性命名与表字段统一
     * 
     * @param properties
     * @return
     */
    private static StringBuffer generatePropertiesPrivate(
            Map<String, FieldInfo> fieldMap)
    {
        StringBuffer result = new StringBuffer();

        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();
        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            if (field.flag)
            {
                // 加注释
                result.append(addComment(field.getComment()));
                result.append("\tprivate ");
                result.append(field.getJavaType() + " "
                        + CommonUtil.toLowerName(field.getName()) + ";\n");
                result.append("\n");
            }
        }
        return result.append("\n");
    }

    /**
     * 生成公开的 setter 和 getter 方法
     * 
     * @param properties
     * @return
     */
    private static StringBuffer generateMethodGetterAndSetterPublic(
            Map<String, FieldInfo> fieldMap)
    {
        StringBuffer result = new StringBuffer();

        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();
        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            if (field.flag)
            {
                // 加注释
                result.append(addComment(field.getComment()));

                result.append(makeGetter(CommonUtil.toLowerName(field.getName()),
                        field.getJavaType()));
                result.append("\n");

                // 加注释
                result.append(addComment(field.getComment()));

                result.append(makeSetter(CommonUtil.toLowerName(field.getName()),
                        field.getJavaType(), field.isFlag()));
                result.append("\n");
            }
        }
        return result.append("\n");

    }

    private static StringBuffer makeSetter(String var, String type, boolean flag)
    {
        StringBuffer result = new StringBuffer("\tpublic ");
        result.append("void" + " set" + CommonUtil.toUpperName(var) + "("
                + type + " " + var + ")\n");
        result.append("\t{\n");

        if (type.equalsIgnoreCase("string"))
        {
            result.append("\t\tif(").append(var).append("==null)\n");
            result.append("\t\t\tthis." + var + " = \"\";\n");
            result.append("\t\telse\n");
            result.append("\t\t\tthis." + var + " = " + var + ";\n");
        }
        else
        {
            result.append("\t\tthis." + var + " = " + var + ";\n");
        }

        result.append("\t}\n");
        return result;
    }

    private static StringBuffer makeGetter(String var, String type)
    {
        StringBuffer result = new StringBuffer("\tpublic ");
        result.append(type + " get" + CommonUtil.toUpperName(var) + "()\n");
        result.append("\t{\n");
        result.append("\t\treturn " + var + ";\n");
        result.append("\t}\n");
        return result;
    }

    /**
     * 添加字段注释
     * 
     * @param value
     * @return
     */
    private static StringBuffer addComment(String value)
    {

        StringBuffer result = new StringBuffer("");
        result.append("\t").append("/** ").append(value).append(" */").append("\n");
        return result;
    }

    /**
     * 生成 clone方法
     * 
     * @return
     */
    private static StringBuffer generateMethodClonePublic(String entityName, String tableName,
            Map<String, FieldInfo> fieldMap)
    {
        String className = CommonUtil.CreateEntityName(entityName, tableName);
        StringBuffer result = new StringBuffer();
        result.append(addComment("x.clone() != x"));
        result.append("\tpublic ").append(className).append(" clone()\n");
        result.append("\t{\n");
        result.append("\t\t").append(className).append(" clone = new ").append(className).append("();\n");
        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();
        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            if (field.flag)
            {
                String fieldName = field.getName();
                result.append("\t\tclone.").append("set").append(CommonUtil.toUpperName(fieldName)).append(
                        "(this.get").append(CommonUtil.toUpperName(fieldName)).append("());\n");
            }
        }
        result.append("\t\treturn clone;\n");
        result.append("\t}\n");
        return result.append("\n");
    }

    /**
     * 生成 reset方法
     * 
     * @return
     */
    private static StringBuffer generateMethodResetPublic(String entityName, String tableName,
            Map<String, FieldInfo> fieldMap)
    {
        String className = CommonUtil.CreateEntityName(entityName, tableName);
        StringBuffer result = new StringBuffer();
        result.append(addComment("重置信息"));
        result.append("\tpublic void ").append("reset(").append(className).append(" bean)\n");
        result.append("\t{\n");
        Collection<FieldInfo> c = fieldMap.values();
        Iterator<FieldInfo> it = c.iterator();

        while (it.hasNext())
        {
            FieldInfo field = (FieldInfo) it.next();
            if (field.flag)
            {
                String fieldName = field.getName();
                result.append("\t\tthis.").append("set").append(CommonUtil.toUpperName(fieldName)).append(
                        "(bean.get").append(CommonUtil.toUpperName(fieldName)).append("());\n");
            }
        }

        result.append("\t}\n");
        return result.append("\n");
    }

    public static String generateEntityFactory(String beanName, Map<String, FieldInfo> fieldMap)
    {
        // 主键的个数
        List<FieldInfo> primaryKeyList = new ArrayList<>();
        for (FieldInfo info : fieldMap.values())
        {
            if (info.isPrimaryKey())
            {
                primaryKeyList.add(info);
            }
        }

        // 主键升序排列
        primaryKeyList.sort((p1, p2) -> {
            return p1.primaryKeyIndex - p2.primaryKeyIndex;
        });

        String mapName = CommonUtil.toLowerName(beanName) + "Map";

        if (primaryKeyList.size() == 1)
            return generateOneKeyFactory(beanName, mapName, primaryKeyList);

        if (primaryKeyList.size() == 2)
            return generateTwoKeyFactory(beanName, mapName, primaryKeyList);

        if (primaryKeyList.size() == 3)
            return generateThreeKeyFactory(beanName, mapName, primaryKeyList);

        return generateOneKeyFactory(beanName, mapName, primaryKeyList);
    }

    private static String generateOneKeyFactory(String beanName, String mapName, List<FieldInfo> primaryKeyList)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.data.bean.factory;\n");
        builder.append("import java.util.HashMap;").append("\n");
        builder.append("import java.util.List;").append("\n");
        builder.append("import java.util.ArrayList;").append("\n");
        builder.append("import java.util.Map;").append("\n");
        builder.append("import java.util.concurrent.locks.ReadWriteLock;").append("\n");
        builder.append("import java.util.concurrent.locks.ReentrantReadWriteLock;").append("\n");
        builder.append("import com.base.data.IBeanFactory;").append("\n");
        builder.append("import com.data.component.SystemDataComponent;").append("\n");
        builder.append("import com.util.print.LogFactory;").append("\n");
        builder.append("import com.data.bean.").append(beanName + ";").append("\n");
        builder.append("").append("\n");

        // class 开始
        builder.append("/**").append("\n");
        builder.append(" * auto generate,don't modify.").append("\n");
        builder.append(" * @author system").append("\n");
        builder.append("*/").append("\n");
        builder.append(String.format("public class %sFactory implements IBeanFactory", beanName)).append("\n");
        builder.append("{").append("\n");
        builder.append("    private static ReadWriteLock lock = new ReentrantReadWriteLock();").append("\n");
        builder.append("").append("\n");

        builder.append(String.format("    /**《%s，数据》 */", primaryKeyList.get(0).getComment())).append("\n");
        builder.append(String.format("    private static Map<%s, %s> %s = new HashMap<>();",
                CommonUtil.dataToObject(primaryKeyList.get(0).getJavaType()), beanName, mapName)).append("\n");

        // load 方法
        builder.append("").append("\n");
        builder.append("    @Override").append("\n");
        builder.append("    public boolean load()").append("\n");
        builder.append("    {").append("\n");
        builder.append(String.format("        List<%s> list = SystemDataComponent.getBeanList(%s.class);",
                beanName, beanName)).append("\n");
        builder.append(String.format("        %s.clear();", mapName)).append("\n");
        builder.append(String.format("        for (%s bean : list)", beanName)).append("\n");
        builder.append("        {").append("\n");
        builder.append(String.format("            %s.put(bean.%s(), bean);",
                mapName, "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("        }").append("\n");
        builder.append("        return true;").append("\n");
        builder.append("    }").append("\n");

        // reload 方法
        builder.append("").append("\n");
        builder.append("\t@Override").append("\n");
        builder.append("\tpublic boolean reload()").append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.writeLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = SystemDataComponent.getBeanList(%s.class);", beanName,
                beanName)).append("\n");
        builder.append(String.format("\t\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\tif (%s.containsKey(bean.%s()))",
                mapName, "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("\t\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\t\t%s.get(bean.%s()).reset(bean);", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("\t\t\t\telse").append("\n");
        builder.append("\t\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\t\t%s.put(bean.%s(), bean);", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("\t\t\t}").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn true;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.writeLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getAll()方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static List<%s> getAll()", beanName)).append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = new ArrayList<>();", beanName)).append("\n");
        builder.append(String.format("\t\t\tlist.addAll(%s.values());", mapName)).append("\n");
        builder.append("\t\t\treturn list;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getXXXBean 方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static %s get%s(%s %s)", beanName, beanName,
                primaryKeyList.get(0).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\t%s bean = %s.get(%s);",
                beanName, mapName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\tif (bean == null)").append("\n");
        builder.append(String.format("\t\t\t\tLogFactory.error(\"%s is Null.%s:{}\", %s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\treturn bean;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");
        builder.append(" }").append("\n");

        builder.append("").append("\n");
        return builder.toString();
    }

    private static String generateTwoKeyFactory(String beanName, String mapName, List<FieldInfo> primaryKeyList)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.data.bean.factory;\n");
        builder.append("import java.util.HashMap;").append("\n");
        builder.append("import java.util.List;").append("\n");
        builder.append("import java.util.ArrayList;").append("\n");
        builder.append("import java.util.Map;").append("\n");
        builder.append("import java.util.concurrent.locks.ReadWriteLock;").append("\n");
        builder.append("import java.util.concurrent.locks.ReentrantReadWriteLock;").append("\n");
        builder.append("import com.base.data.IBeanFactory;").append("\n");
        builder.append("import com.data.component.SystemDataComponent;").append("\n");
        builder.append("import com.util.print.LogFactory;").append("\n");
        builder.append("import com.data.bean.").append(beanName + ";").append("\n");
        builder.append("").append("\n");

        // class 开始
        builder.append("/**").append("\n");
        builder.append(" * auto generate,don't modify.").append("\n");
        builder.append(" * @author system").append("\n");
        builder.append("*/").append("\n");
        builder.append(String.format("public class %sFactory implements IBeanFactory", beanName)).append("\n");
        builder.append("{").append("\n");
        builder.append("    private static ReadWriteLock lock = new ReentrantReadWriteLock();").append("\n");
        builder.append("").append("\n");

        builder.append(String.format("    /**《%s，数据列表》 */", primaryKeyList.get(0).getComment())).append("\n");
        builder.append(String.format("    private static Map<%s, List<%s>> %s = new HashMap<>();",
                CommonUtil.dataToObject(primaryKeyList.get(0).getJavaType()), beanName, mapName)).append("\n");

        // load 方法
        builder.append("").append("\n");
        builder.append("\t@Override").append("\n");
        builder.append("\tpublic boolean load()").append("\n");
        builder.append("\t{").append("\n");
        builder.append(String.format("\t\tList<%s> list = SystemDataComponent.getBeanList(%s.class);",
                beanName, beanName)).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t%s.clear();", mapName)).append("\n");
        builder.append(String.format("\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t{").append("\n");

        builder.append(String.format("\t\t\tif (!%s.containsKey(bean.%s()))", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append(String.format("\t\t\t\t%s.put(bean.%s(), new ArrayList<>());", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t\t%s.get(bean.%s()).add(bean);", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t}").append("\n");
        builder.append("\t\treturn true;").append("\n");
        builder.append("\t}").append("\n");

        // reload 方法
        builder.append("").append("\n");
        builder.append("\t@Override").append("\n");
        builder.append("\tpublic boolean reload()").append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.writeLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = SystemDataComponent.getBeanList(%s.class);", beanName,
                beanName)).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t\t{").append("\n");

        builder.append(String.format("\t\t\t\tif (!%s.containsKey(bean.%s()))",
                mapName, "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append(String.format("\t\t\t\t\t%s.put(bean.%s(), new ArrayList<>());", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\t\tList<%s> list2 = %s.get(bean.%s());", beanName, mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("\t\t\t\tboolean isContain = false;").append("\n");
        builder.append(String.format("\t\t\t\tfor (%s bean1 : list2)", beanName)).append("\n");
        builder.append("\t\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\t\tif (bean1.%s() == bean.%s())",
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()),
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()))).append("\n");
        builder.append("\t\t\t\t\t{").append("\n");
        builder.append("\t\t\t\t\t\tisContain = true;").append("\n");
        builder.append("\t\t\t\t\t\tbean1.reset(bean);").append("\n");
        builder.append("\t\t\t\t\t\tbreak;").append("\n");
        builder.append("\t\t\t\t\t}").append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\t\tif (isContain == false)").append("\n");
        builder.append("\t\t\t\t\tlist2.add(bean);").append("\n");
        builder.append("\t\t\t}").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn true;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.writeLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getAll()方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static List<%s> getAll()", beanName)).append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = new ArrayList<>();", beanName)).append("\n");
        builder.append(String.format("\t\t\t%s.forEach((k, v) -> {", mapName)).append("\n");
        builder.append("\t\t\t\tlist.addAll(v);").append("\n");
        builder.append("\t\t\t});").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn list;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getXXXBeanList 方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static List<%s> get%sList(%s %s)", beanName, beanName,
                primaryKeyList.get(0).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = %s.get(%s);",
                beanName, mapName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\tif (list == null)").append("\n");
        builder.append(String.format("\t\t\t\tLogFactory.error(\"%s List is Null.%s:{}\", %s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append("\t\t\treturn list;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getXXXBean 方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static %s get%s(%s %s, %s %s)", beanName, beanName,
                primaryKeyList.get(0).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                primaryKeyList.get(1).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()))).append("\n");

        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = %s.get(%s);",
                beanName, mapName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\tif (list == null)").append("\n");
        builder.append(String.format("\t\t\t\tLogFactory.error(\"%s List is Null.%s:{}\", %s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\tif (bean.%s() == %s)",
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()))).append("\n");

        builder.append("\t\t\t\t{").append("\n");
        builder.append("\t\t\t\t\treturn bean;").append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("\t\t\t}").append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\tLogFactory.error(\"%s is Null.%s:{},%s:{}.\", %s,%s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()))).append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn null;").append("\n");

        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");
        builder.append(" }").append("\n");

        return builder.toString();
    }

    private static String generateThreeKeyFactory(String beanName, String mapName, List<FieldInfo> primaryKeyList)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.data.bean.factory;\n");
        builder.append("import java.util.HashMap;").append("\n");
        builder.append("import java.util.List;").append("\n");
        builder.append("import java.util.ArrayList;").append("\n");
        builder.append("import java.util.Map;").append("\n");
        builder.append("import java.util.concurrent.locks.ReadWriteLock;").append("\n");
        builder.append("import java.util.concurrent.locks.ReentrantReadWriteLock;").append("\n");
        builder.append("import com.base.data.IBeanFactory;").append("\n");
        builder.append("import com.data.component.SystemDataComponent;").append("\n");
        builder.append("import com.util.print.LogFactory;").append("\n");
        builder.append("import com.data.bean.").append(beanName + ";").append("\n");
        builder.append("").append("\n");

        // class 开始
        builder.append("/**").append("\n");
        builder.append(" * auto generate,don't modify.").append("\n");
        builder.append(" * @author system").append("\n");
        builder.append("*/").append("\n");
        builder.append(String.format("public class %sFactory implements IBeanFactory", beanName)).append("\n");
        builder.append("{").append("\n");
        builder.append("    private static ReadWriteLock lock = new ReentrantReadWriteLock();").append("\n");
        builder.append("").append("\n");

        builder.append(String.format("    /**《%s，数据列表》 */", primaryKeyList.get(0).getComment())).append("\n");
        builder.append(String.format("    private static Map<%s, List<%s>> %s = new HashMap<>();",
                CommonUtil.dataToObject(primaryKeyList.get(0).getJavaType()), beanName, mapName)).append("\n");

        // load 方法
        builder.append("").append("\n");
        builder.append("\t@Override").append("\n");
        builder.append("\tpublic boolean load()").append("\n");
        builder.append("\t{").append("\n");
        builder.append(String.format("\t\tList<%s> list = SystemDataComponent.getBeanList(%s.class);",
                beanName, beanName)).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t%s.clear();", mapName)).append("\n");
        builder.append(String.format("\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t{").append("\n");

        builder.append(String.format("\t\t\tif (!%s.containsKey(bean.%s()))", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append(String.format("\t\t\t\t%s.put(bean.%s(), new ArrayList<>());", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t\t%s.get(bean.%s()).add(bean);", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t}").append("\n");
        builder.append("\t\treturn true;").append("\n");
        builder.append("\t}").append("\n");

        // reload 方法
        builder.append("").append("\n");
        builder.append("\t@Override").append("\n");
        builder.append("\tpublic boolean reload()").append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.writeLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = SystemDataComponent.getBeanList(%s.class);", beanName,
                beanName)).append("\n");
        builder.append("").append("\n");
        builder.append(String.format("\t\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t\t{").append("\n");

        builder.append(String.format("\t\t\t\tif (!%s.containsKey(bean.%s()))",
                mapName, "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append(String.format("\t\t\t\t\t%s.put(bean.%s(), new ArrayList<>());", mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\t\tList<%s> list2 = %s.get(bean.%s());", beanName, mapName,
                "get" + CommonUtil.toUpperName(primaryKeyList.get(0).getName()))).append("\n");
        builder.append("\t\t\t\tboolean isContain = false;").append("\n");
        builder.append(String.format("\t\t\t\tfor (%s bean1 : list2)", beanName)).append("\n");
        builder.append("\t\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\t\tif (bean1.%s() == bean.%s() && bean1.%s()==bean.%s())",
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()),
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()),
                "get" + CommonUtil.toUpperName(primaryKeyList.get(2).getName()),
                "get" + CommonUtil.toUpperName(primaryKeyList.get(2).getName()))).append("\n");
        builder.append("\t\t\t\t\t{").append("\n");
        builder.append("\t\t\t\t\t\tisContain = true;").append("\n");
        builder.append("\t\t\t\t\t\tbean1.reset(bean);").append("\n");
        builder.append("\t\t\t\t\t\tbreak;").append("\n");
        builder.append("\t\t\t\t\t}").append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\t\tif (isContain == false)").append("\n");
        builder.append("\t\t\t\t\tlist2.add(bean);").append("\n");
        builder.append("\t\t\t}").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn true;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.writeLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getAll()方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static List<%s> getAll()", beanName)).append("\n");
        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = new ArrayList<>();", beanName)).append("\n");
        builder.append(String.format("\t\t\t%s.forEach((k, v) -> {", mapName)).append("\n");
        builder.append("\t\t\t\tlist.addAll(v);").append("\n");
        builder.append("\t\t\t});").append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn list;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getXXXBeanList 方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static List<%s> get%sList(%s %s)", beanName, beanName,
                primaryKeyList.get(0).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = %s.get(%s);",
                beanName, mapName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\tif (list == null)").append("\n");
        builder.append(String.format("\t\t\t\tLogFactory.error(\"%s List is Null.%s:{}\", %s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append("\t\t\treturn list;").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");

        // getXXXBean 方法
        builder.append("").append("\n");
        builder.append(String.format("\tpublic static %s get%s(%s %s, %s %s,%s %s)", beanName, beanName,
                primaryKeyList.get(0).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                primaryKeyList.get(1).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()),
                primaryKeyList.get(2).getJavaType(),
                CommonUtil.toLowerName(primaryKeyList.get(2).getName()))).append("\n");

        builder.append("\t{").append("\n");
        builder.append("\t\tlock.readLock().lock();").append("\n");
        builder.append("\t\ttry").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append(String.format("\t\t\tList<%s> list = %s.get(%s);",
                beanName, mapName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("\t\t\tif (list == null)").append("\n");
        builder.append(String.format("\t\t\t\tLogFactory.error(\"%s List is Null.%s:{}\", %s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()))).append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\tfor (%s bean : list)", beanName)).append("\n");
        builder.append("\t\t\t{").append("\n");
        builder.append(String.format("\t\t\t\tif (bean.%s() == %s && bean.%s() == %s)",
                "get" + CommonUtil.toUpperName(primaryKeyList.get(1).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()),
                "get" + CommonUtil.toUpperName(primaryKeyList.get(2).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(2).getName()))).append("\n");

        builder.append("\t\t\t\t{").append("\n");
        builder.append("\t\t\t\t\treturn bean;").append("\n");
        builder.append("\t\t\t\t}").append("\n");
        builder.append("\t\t\t}").append("\n");

        builder.append("").append("\n");
        builder.append(String.format("\t\t\tLogFactory.error(\"%s is Null.%s:{},%s:{},%s:{}.\", %s,%s,%s);",
                beanName, CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(2).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(0).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(1).getName()),
                CommonUtil.toLowerName(primaryKeyList.get(2).getName()))).append("\n");
        builder.append("").append("\n");
        builder.append("\t\t\treturn null;").append("\n");

        builder.append("\t\t}").append("\n");
        builder.append("\t\tfinally").append("\n");
        builder.append("\t\t{").append("\n");
        builder.append("\t\t\tlock.readLock().unlock();").append("\n");
        builder.append("\t\t}").append("\n");
        builder.append("\t}").append("\n");
        builder.append(" }").append("\n");

        return builder.toString();
    }

}
