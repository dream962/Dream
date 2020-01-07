package com.upload.data.factory;

import com.upload.data.dao.IServerListDataDao;
import com.upload.data.dao.impl.ServerListDataDaoImpl;

public class ServerListDataFactory implements IDaoFactory
{
    private static final IServerListDataDao dao = new ServerListDataDaoImpl(dbHelper);

    public static IServerListDataDao getDao()
    {
        return dao;
    }
}