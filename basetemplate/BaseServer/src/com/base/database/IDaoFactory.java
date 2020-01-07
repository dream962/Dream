package com.base.database;

import com.base.database.pool.DBHelper;

public interface IDaoFactory
{
    /** 游戏主库 */
    static final DBHelper mainHelper = DBPoolComponent.getDBHelper(DatabaseType.DB_MAIN);

    /** 数据集市 */
    static final DBHelper martHelper = DBPoolComponent.getDBHelper(DatabaseType.DB_MART);
}
