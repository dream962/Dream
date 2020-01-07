package com.data.account.factory;

import com.base.database.DBPoolComponent;
import com.base.database.pool.DBHelper;

public interface IDaoFactory
{
    /** 游戏主库 */
    static final DBHelper mainHelper = DBPoolComponent.getDBHelper(DatabaseType.DB_ACCOUNT);
}
