package com.upload.data.factory;


import com.upload.database.DBPoolComponent;
import com.upload.database.pool.DBHelper;

public interface IDaoFactory
{
    /** 上传库 */
    static final DBHelper dbHelper = DBPoolComponent.getDBHelper(DatabaseType.DB_JUMP);
}
