package com.upload.data.factory;

import com.upload.data.dao.IServerDataDao;
import com.upload.data.dao.impl.ServerDataDaoImpl;

/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public final class ServerDataFactory implements IDaoFactory
{
    private static IServerDataDao dao;

    public static IServerDataDao getDao()
    {
        if (dao == null)
            dao = new ServerDataDaoImpl(dbHelper);

        return dao;
    }
}
