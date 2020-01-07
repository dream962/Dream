/**
 * Date: 2013-6-4
 * 
 * Copyright (C) 2013-2015 7Road. All rights reserved.
 */

package com.base.orm;

import it.biobytes.ammentos.Ammentos;
import it.biobytes.ammentos.PersistenceException;
import it.biobytes.ammentos.query.Query;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IBaseOrmDao的接口的实现类
 * 
 * @author jinjin.chen
 */
public class BaseOrmDao<V>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseOrmDao.class);
    protected Class<V> clazz;

    @SuppressWarnings("unchecked")
    public BaseOrmDao()
    {
        @SuppressWarnings("rawtypes")
        Class clazz = getClass();
        while (clazz != Object.class)
        {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType)
            {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class)
                {
                    this.clazz = (Class<V>) args[0];// 获取泛型的class
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public List<V> listAll()
    {
        try
        {
            return Ammentos.load(clazz, new Query());
        }
        catch (PersistenceException e)
        {
            LOGGER.error(e.toString());
        }

        return null;
    }
}
