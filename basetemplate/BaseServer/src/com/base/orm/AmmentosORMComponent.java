package com.base.orm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.component.AbstractComponent;
import com.base.component.GlobalConfigComponent;
import com.base.config.DatabaseConfig.BonePoolConfig;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import it.biobytes.ammentos.Ammentos;
import it.biobytes.ammentos.PersistenceException;
import it.biobytes.ammentos.query.Query;
import it.biobytes.ammentos.query.QueryFilter;

/**
 * 模板数据组件
 * @author dream
 *
 */
public final class AmmentosORMComponent extends AbstractComponent
{
    /**
     * 连接数据库的url，包括有连接地址，用户名和密码
     */
    private String url = null;
    private String dbName;
    private String dbPass;
    private static final Logger LOGGER = LoggerFactory.getLogger(AmmentosORMComponent.class);

    /**
     * 解析配置文件
     * 
     * @param element
     *            orm的数据库配置文件
     */
    public void initWithDbXML(List<BonePoolConfig> dbs)
    {
        String privateModulus = GlobalConfigComponent.getConfig().database.privateModulus;
        String privateExponent = GlobalConfigComponent.getConfig().database.privateExponent;
        String url = "";
        String name = "";
        String password = "";
        if (dbs.size() > 0)
        {
            BonePoolConfig db =  dbs.get(0);// 获取DB的节点，可以有多个DB节点
            url = db.url;
            name = db.username;
            password = db.password;

//                try
//                {
//                    url = RSAUtil.decrypt(privateModulus, privateExponent, url)
//                            .trim();
//                    name = RSAUtil.decrypt(privateModulus, privateExponent,
//                            name).trim();
//                    password = RSAUtil.decrypt(privateModulus, privateExponent,
//                            password).trim();
//                }
//                catch (Exception e)
//                {
//                    LOGGER.error(
//                            "decrypt database url or name or password error!",
//                            e);
//                }
            
        }
        this.url = url;
        this.dbName=name;
        this.dbPass=password;
    }

    @Override
    public boolean initialize()
    {
        initWithDbXML(GlobalConfigComponent.getConfig().database.orm);
        try
        {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUrl(url);
            dataSource.setUser(dbName);
            dataSource.setPassword(dbPass);
            Ammentos.setDataSource(dataSource);
        }
        catch (Exception e)
        {
            LOGGER.error("启动出错 : " + e.toString());
            return false;
        }
        return true;
    }

    /**
     * 获取对应的classname的所有实体
     * 
     * @param classname
     *            实体class
     * @return
     * @throws PersistenceException
     */
    public static <T> List<T> listAll(Class<T> classname)
    {
        try
        {
            return Ammentos.load(classname, new Query());
        }
        catch (PersistenceException e)
        {
            LOGGER.error("获取系统模板数据失败 : " + classname.getName(), e);
        }
        
        return null;
    }

    public static boolean addData(List<?>... dataList)
    {
        try
        {
            if (null != dataList)
            {
                for (List<?> TList : dataList)
                {
                    if (null != TList)
                    {
                        for (int i = 0, max = TList.size(); i < max; i++)
                        {
                            Ammentos.save(TList.get(i));
                        }
                    }
                }
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static <T> boolean removeData(T data)
    {
        try
        {
            if (null != data)
            {
                Ammentos.delete(data);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param classname
     *            classname 实体class
     * @param filter
     * @return
     * @throws PersistenceException
     */
    public static <T> List<T> name(Class<T> classname, QueryFilter filter)
            throws PersistenceException
    {
        try
        {
            return Ammentos.load(classname, filter);
        }
        catch (PersistenceException e)
        {
            LOGGER.error("获取数据失败 : " + e.toString());
            throw e;
        }
    }

    /**
     * 根据主键值获取classname的实体
     * 
     * @param classname
     *            实体class
     * @param keys
     *            主键
     * @return
     * @throws PersistenceException
     */
    public static <T> T getEntity(Class<T> classname, Object... keys) throws PersistenceException
    {
        try
        {
            return Ammentos.load(classname, keys);
        }
        catch (PersistenceException e)
        {
            LOGGER.error("获取数据失败 : " + e.toString());
            throw e;
        }
    }

    @Override
    public void stop()
    {
        
    }
}
