package com.base.orm;

import java.util.List;

/**
 * ORM的dao类的基类接口
 */
public interface IBaseOrmDao<V>
{
    /**
     * 获取所有实体
     * 
     * @return
     */
    List<V> listAll();
}
