package com.data.factory;

import com.data.dao.ILineRankInfoDao;
import com.data.dao.impl.LineRankInfoDaoImpl;


/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public final class LineRankInfoFactory implements IDaoFactory
{
    private static ILineRankInfoDao dao;

    public static ILineRankInfoDao getDao()
    {
		if (dao == null)
			dao = new LineRankInfoDaoImpl(mainHelper);

        return dao;
    }
}