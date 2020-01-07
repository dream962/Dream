package com.util;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

//
// @XmlRootElement 将一个Java类映射为一段XML的根节点
//
// 参数：name 定义这个根节点的名称
//
// namespace 定义这个根节点命名空间
//
//
//
// @XmlAccessorType 定义映射这个类中的何种类型需要映射到XML。可接收四个参数，分别是：
//
// XmlAccessType.FIELD：映射这个类中的所有字段到XML
//
// XmlAccessType.PROPERTY：映射这个类中的属性（get/set方法）到XML
//
// XmlAccessType.PUBLIC_MEMBER：将这个类中的所有public的field或property同时映射到XML（默认）
//
// XmlAccessType.NONE：不映射
//
//
//
// @XmlElement 指定一个字段或get/set方法映射到XML的节点。如，当一个类的XmlAccessorType 被标注为PROPERTY时，在某一个没有get/set方法的字段上标注此注解，即可将该字段映射到XML。
//
// 参数：defaultValue 指定节点默认值
//
// name 指定节点名称
//
// namespace 指定节点命名空间
//
// required 是否必须（默认为false）
//
// nillable 该字段是否包含 nillable="true" 属性（默认为false）
//
// type 定义该字段或属性的关联类型
//
//
//
// @XmlAttribute 指定一个字段或get/set方法映射到XML的属性。
//
// 参数：name 指定属性名称
//
// namespace 指定属性命名空间
//
// required 是否必须（默认为false）
//
//
//
// @XmlTransient 定义某一字段或属性不需要被映射为XML。如，当一个类的XmlAccessorType 被标注为PROPERTY时，在某一get/set方法的字段上标注此注解，那么该属性则不会被映射。
//
//
//
// @XmlType 定义映射的一些相关规则
//
// 参数：propOrder 指定映射XML时的节点顺序
//
// factoryClass 指定UnMarshal时生成映射类实例所需的工厂类，默认为这个类本身
//
// factoryMethod 指定工厂类的工厂方法
//
// name 定义XML Schema中type的名称
//
// namespace 指定Schema中的命名空间
//
//
//
// @XmlElementWrapper 为数组元素或集合元素定义一个父节点。如，类中有一元素为List items，若不加此注解，该元素将被映射为
//
// <items>...</items>
//
// <items>...</items>
//
// 这种形式，此注解可将这个元素进行包装，如：
//
// @XmlElementWrapper(name="items")
// @XmlElement(name="item")
// public List items;
//
// 将会生成这样的XML样式：
//
// <items>
//
// <item>...</item>
//
// <item>...</item>
//
// </items>

// @XmlJavaTypeAdapter 自定义某一字段或属性映射到XML的适配器。如，类中包含一个接口，我们可以定义一个适配器（继承自
// javax.xml.bind.annotation.adapters.XmlAdapter类），指定这个接口如何映射到XML。

public class XmlUtil
{
    @SuppressWarnings("unchecked")
    public static <T> T toObject(String str, Class<T> cla)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(cla);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            T result = (T) unmarshaller.unmarshal(new StringReader(str));
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> boolean toPrint(Class<T> t, T obj)
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(t);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, System.out);
            return true;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static <T> boolean toFile(String path, Class<T> t, T obj)
    {
        try
        {
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(t);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, file);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    /******************************************* dom4j **********************************************/

    public static class User
    {
        public String name;
        public String age;
        public String sex;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getAge()
        {
            return age;
        }

        public void setAge(String age)
        {
            this.age = age;
        }

        public String getSex()
        {
            return sex;
        }

        public void setSex(String sex)
        {
            this.sex = sex;
        }

        public User()
        {

        }

        public User(String name, String age, String sex)
        {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }
    }

    public static void main(String[] args)
    {
        User user = new User();
        User user1 = new User("姓名1", "18", "男");
        User user2 = new User("姓名2", "19", "女");
        User user3 = new User("石头", "20", "女");

        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        writeXmlDocument(user, users, "GBK", "WebRoot\\WEB-INF\\student.xml");

        List<User> list = readXML("WebRoot\\WEB-INF\\student.xml", user);
        System.out.println("XML文件读取结果");
        for (int i = 0; i < list.size(); i++)
        {
            User usename = (User) list.get(i);
            System.out.println("name" + usename.getName());
            System.out.println("age" + usename.getAge());
            System.out.println("sax" + usename.getSex());
        }
    }

    /**
     * DMO4J写入XML
     * 
     * @param obj
     *            泛型对象
     * @param entityPropertys
     *            泛型对象的List集合
     * @param Encode
     *            XML自定义编码类型(推荐使用GBK)
     * @param XMLPathAndName
     *            XML文件的路径及文件名
     */
    public static <T> void writeXmlDocument(T obj, List<T> entityPropertys, String Encode, String XMLPathAndName)
    {
        long lasting = System.currentTimeMillis();// 效率检测

        try
        {
            XMLWriter writer = null;// 声明写XML的对象
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(Encode);// 设置XML文件的编码格式

            String filePath = XMLPathAndName;// 获得文件地址
            File file = new File(filePath);// 获得文件

            if (file.exists())
            {
                file.delete();

            }
            // 新建student.xml文件并新增内容
            Document document = DocumentHelper.createDocument();
            String rootname = obj.getClass().getSimpleName();// 获得类名
            Element root = document.addElement(rootname + "s");// 添加根节点
            Field[] properties = obj.getClass().getDeclaredFields();// 获得实体类的所有属性

            for (T t : entityPropertys)
            {                                // 递归实体
                Element secondRoot = root.addElement(rootname);            // 二级节点

                for (int i = 0; i < properties.length; i++)
                {
                    // 反射get方法
                    Method meth = t.getClass().getMethod(
                            "get"
                                    + properties[i].getName().substring(0, 1).toUpperCase()
                                    + properties[i].getName().substring(1));
                    // 为二级节点添加属性，属性值为对应属性的值
                    secondRoot.addElement(properties[i].getName()).setText(
                            meth.invoke(t).toString());

                }
            }
            // 生成XML文件
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
            writer.close();
            long lasting2 = System.currentTimeMillis();
            System.out.println("写入XML文件结束,用时" + (lasting2 - lasting) + "ms");
        }
        catch (Exception e)
        {
            System.out.println("XML文件写入失败");
        }

    }

    /**
     * 
     * @param <T>
     * @param XMLPathAndName
     *            XML文件的路径和地址
     * @param t
     *            泛型对象
     * @return
     */

    @SuppressWarnings("unchecked")
    public static <T> List<T> readXML(String XMLPathAndName, T t)
    {
        long lasting = System.currentTimeMillis();// 效率检测
        List<T> list = new ArrayList<T>();// 创建list集合
        try
        {
            File f = new File(XMLPathAndName);// 读取文件
            SAXReader reader = new SAXReader();
            Document doc = reader.read(f);// dom4j读取
            Element root = doc.getRootElement();// 获得根节点
            Element foo;// 二级节点
            Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
            // 实例的get方法
            Method getmeth;
            // 实例的set方法
            Method setmeth;

            for (Iterator i = root.elementIterator(t.getClass().getSimpleName()); i.hasNext();)
            {// 遍历t.getClass().getSimpleName()节点
                foo = (Element) i.next();// 下一个二级节点

                t = (T) t.getClass().newInstance();// 获得对象的新的实例

                for (int j = 0; j < properties.length; j++)
                {// 遍历所有孙子节点

                    // 实例的set方法
                    setmeth = t.getClass().getMethod(
                            "set"
                                    + properties[j].getName().substring(0, 1).toUpperCase()
                                    + properties[j].getName().substring(1),
                            properties[j].getType());
                    // properties[j].getType()为set方法入口参数的参数类型(Class类型)
                    setmeth.invoke(t, foo.elementText(properties[j].getName()));// 将对应节点的值存入

                }

                list.add(t);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        long lasting2 = System.currentTimeMillis();
        System.out.println("读取XML文件结束,用时" + (lasting2 - lasting) + "ms");
        return list;
    }

}
