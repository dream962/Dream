package com.upload.data.factory;

import com.upload.data.dao.IStaffBeanDao;
import com.upload.data.dao.impl.StaffBeanDaoImpl;

public class StaffBeanFactory implements IDaoFactory
{
    private static final IStaffBeanDao dao = new StaffBeanDaoImpl(dbHelper);

    public static IStaffBeanDao getDao()
    {
        return dao;
    }
}