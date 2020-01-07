package com.base.mesh.data;

import java.util.ArrayList;
import java.util.List;

import com.util.FileUtil;
import com.util.XmlUtil;

//@XmlRootElement(name = "root")
public class XmlTriangleList
{
    public static enum TestType
    {
        Test1(1),
        Test2(2),
        ;
        private int v;
        
        TestType(int value)
        {
            this.v=value;
        }
        
        public TestType parse(String str)
        {
            return TestType.valueOf(str);
        }
    };
    
    private List<XmlTriangle> triangleList;
    
    private List<XmlOffMeshLink> linkList;

    public XmlTriangleList()
    {
        triangleList = new ArrayList<>();
        linkList=new ArrayList<>();
    }
    
//    @XmlElement(name="link")
    public List<XmlOffMeshLink> getLinkList()
    {
        return linkList;
    }

    public void setLinkList(List<XmlOffMeshLink> linkList)
    {
        this.linkList = linkList;
    }
    
    /**
     * 注解添加到get方法中
     * @return
     */
//    @XmlElement(name="triangle")
    public List<XmlTriangle> getTriangleList()
    {
        return triangleList;
    }

    public void setTriangleList(List<XmlTriangle> triangleList)
    {
        this.triangleList = triangleList;
    }

    public static void main(String[] args)
    {
        try
        {
            String xmlStr = FileUtil.readTxt("F:/aa.xml","UTF-8");
            XmlTriangleList list=XmlUtil.toObject(xmlStr, XmlTriangleList.class);
 
            XmlUtil.toPrint(XmlTriangleList.class,list);
            XmlUtil.toFile("F:/bb.xml", XmlTriangleList.class,list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
