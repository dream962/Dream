package com.tool.code;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 实体代码生成
 */
public class EntityCodeUtil
{
    /**
     * 生成实体代码
     * 
     * @param packageName
     * @param className
     * @param properties
     * @return
     */
    /**
     * @param packageName
     * @param entityName
     * @param tableName
     * @param fieldMap
     * @return
     */
    public static String generateEntityCode(String packageName, String entityName, String tableName,
            Map<String, FieldInfo> fieldMap, boolean isAddChangedMap)
    {
        StringBuffer result = new StringBuffer();

        /* 所属包 */
        result.append("package " + CommonUtil.generateDaoEntityPackage(tableName)).append(";\n\n");

        /* 导入类 */
        result.append(generateImport(fieldMap)).append("\n");

        // 序列化的信息
        // result.append("import java.io.Serializable;").append("\n");

        result.append("import com.base.data.ChangedObject;").append("\n");

        /* Class 开头 */
        String className = CommonUtil.CreateEntityName(entityName, tableName);
        result.append(CommonUtil.comment());
        result.append("public final class " + className + " extends ChangedObject \n{\n");

        // Dream 添加序列化信息
//        result.append("    private static final long serialVersionUID = 1L;" + "\n\n");

        /* 各个私有属性 */
        result.append(generatePropertiesPrivate(fieldMap));

        // 构造函数
        result.append("    public " + className + "()").append("\n");
        result.append("    {").append("\n");
        result.append("   ").append("    ").append("super();").append("\n");

        // 如果有isExistz字段
        if (fieldMap.containsKey("IsExist"))
            result.append("   ").append("    ").append("isExist=true;").append("\n");

        result.append("    }").append("\n\n");

        /* Get Set 方法 */
        result.append(generateMethodGetterAndSetterPublic(fieldMap, isAddChangedMap));

        /* clone 方法 */
        result.append(generateMethodClonePublic(entityName, tableName, fieldMap));

        /* reset 方法 */
        result.append(generateMethodResetPublic(entityName, tableName, fieldMap));

        /* Class 结尾 */
        result.append("}");

        return result.toString();
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
            if (field.getJavaType() == "Date") // 非完全匹配
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
    private static StringBuffer generatePropertiesPrivate(Map<String, FieldInfo> fieldMap)
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
                result.append(addCommentProperty(field.getComment()));
                result.append("\tprivate ");
                
                if(field.getJavaType().equalsIgnoreCase("int"))
                {
                    if(field.getSqlType().equalsIgnoreCase("tinyint"))
                    {
                        result.append("byte ");
                    }
                    
                    if(field.getSqlType().equalsIgnoreCase("smallint"))
                    {
                        result.append("short ");
                    }
                }
                
                result.append(field.getJavaType() + " " + CommonUtil.toLowerName(field.getName()) + ";\n");
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
    private static StringBuffer generateMethodGetterAndSetterPublic(Map<String, FieldInfo> fieldMap,boolean isAddChangedMap)
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

                result.append(makeGetter(CommonUtil.toLowerName(field.getName()), field.getJavaType()));
                result.append("\n");

                // 加注释
                result.append(addComment(field.getComment()));

                result.append(makeSetter(CommonUtil.toLowerName(field.getName()), field.getJavaType(), field.isFlag(),
                        isAddChangedMap));

                result.append("\n");
            }
        }
        return result.append("\n");

    }

    private static StringBuffer makeSetter(String var, String type, boolean flag, boolean isAddChangedMap)
    {
        flag = true;
        StringBuffer result = new StringBuffer("\tpublic ");
        result.append("void" + " set" + var.substring(0, 1).toUpperCase() + var.substring(1) + "(" + type + " " + var
                + ")\n");
        result.append("\t{\n");
        if (flag)
        {
            // if (beginDate != null)
            // {
            // if (!beginDate.equals(this.beginDate))
            // {
            // this.beginDate = beginDate;
            // setChanged(true);
            // addChangedMap("beginDate", this.beginDate);
            // }
            // }
            // else
            // {
            // if (beginDate != this.beginDate)
            // {
            // this.beginDate = beginDate;
            // setChanged(true);
            // addChangedMap("beginDate", this.beginDate);
            // }
            // }

            String changeStr = String.format("addChangedMap(\"%s\", this.%s);", var, var);

            if (type == "String" || type == "Date")
            {
                result.append("\t\tif(" + var + " != null)\n");
                result.append("\t\t{").append("\n");

                result.append("\t\t\t").append(String.format("if(!%s.equals(this.%s))", var, var)).append("\n");
                result.append("\t\t\t{").append("\n");
                result.append("\t\t\t\tthis." + var + " = " + var + ";\n");
                result.append("\t\t\t\tsetChanged(true);\n");

                if (isAddChangedMap)
                    result.append("\t\t\t\t" + changeStr + "\n");

                result.append("\t\t\t}\n");
                result.append("\t\t}\n");
                result.append("\t\telse\n");
                result.append("\t\t{\n");

                result.append("\t\t\tif(" + var + " != this." + var + ")\n");
                result.append("\t\t\t{\n");
                result.append("\t\t\t\tthis." + var + " = " + var + ";\n");
                result.append("\t\t\t\tsetChanged(true);\n");

                if (isAddChangedMap)
                    result.append("\t\t\t\t" + changeStr + "\n");

                result.append("\t\t\t}\n");

                result.append("\t\t}\n");
            }
            else
            {
                result.append("\t\tif(" + var + " != this." + var + ")\n");
                result.append("\t\t{\n");
                result.append("\t\t\tthis." + var + " = " + var + ";\n");
                result.append("\t\t\tsetChanged(true);\n");

                if (isAddChangedMap)
                    result.append("\t\t\t" + changeStr + "\n");

                result.append("\t\t}\n");
            }
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
        result.append("\t").append("/**").append("\n");
        result.append("\t").append(" * ").append(value).append("\n");
        result.append("\t").append(" */").append("\n");
        return result;
    }
    
    /**
     * 添加字段注释
     * 
     * @param value
     * @return
     */
    private static StringBuffer addCommentProperty(String value)
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
        result.append("\tpublic void ").append("reset(").append(className).append(" info)\n");
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
                        "(info.get").append(CommonUtil.toUpperName(fieldName)).append("());\n");
            }
        }

        result.append("\t}\n");
        return result.append("\n");
    }

}
