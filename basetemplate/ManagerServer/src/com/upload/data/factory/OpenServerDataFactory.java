package com.upload.data.factory;

import com.upload.data.dao.IOpenServerDataDao;
import com.upload.data.dao.impl.OpenServerDataDaoImpl;

public class OpenServerDataFactory implements IDaoFactory
{
    private static final IOpenServerDataDao dao = new OpenServerDataDaoImpl(dbHelper);

    public static IOpenServerDataDao getDao()
    {
        return dao;
    }
}