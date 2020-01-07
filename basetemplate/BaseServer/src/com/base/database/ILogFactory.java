package com.base.database;

import com.base.database.DBPoolComponent;
import com.base.database.DatabaseType;
import com.base.database.pool.DBHelper;

public interface ILogFactory
{
    /** 日志库 */
    static final DBHelper logHelper = DBPoolComponent.getDBHelper(DatabaseType.DB_LOG);
}
