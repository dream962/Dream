package com.data.factory;

import com.data.dao.ITablesInfoDao;
import com.data.dao.impl.TablesInfoDaoImpl;

public class TablesInfoFactory implements IDaoFactory
{
    private static final ITablesInfoDao dao = new TablesInfoDaoImpl(mainHelper);

    public static ITablesInfoDao getDao()
    {
        return dao;
    }
}